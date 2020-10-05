package code.butchers.contactapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import code.butchers.contactapp.R;
import code.butchers.contactapp.activities.InfoActivity;
import code.butchers.contactapp.activities.MainActivity;
import code.butchers.contactapp.databinding.ContactItemBinding;
import code.butchers.contactapp.db.ContactRepository;
import code.butchers.contactapp.db.ContactRepository;
import code.butchers.contactapp.models.Contact;

import static code.butchers.contactapp.activities.MainActivity.REQUEST_CODE_INFO_CONTACT;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "ContactsAdapter";
    private List<Contact> contacts;
    private List<Contact> contactsAll;
    private Context mContext;
    private ContactRepository contactRepository;

    public void setContact(List<Contact> contacts) {
        this.contacts = contacts;
        this.contactsAll = new ArrayList<>(contacts);
    }

    public ContactsAdapter(Context mContext, ContactRepository contactRepository) {
        this.contacts = new ArrayList<>();
        this.contactsAll = new ArrayList<>(contacts);
        this.mContext = mContext;
        this.contactRepository = contactRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactItemBinding binding = ContactItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact currentContact = contacts.get(position);
        holder.txtFullName.setText(currentContact.getFullName());
        holder.txtEmail.setText(currentContact.getEmail());
        holder.txtAvatar.setText(currentContact.getFirstChar());
        holder.imgAvatar.setCardBackgroundColor(currentContact.getAvatarColor());

        final boolean isExpanded = position == ContactRepository.getPreviousExpandId();
        if(isExpanded) {
            ContactRepository.setPreviousExpandId(position);
            showView(holder.contactItemExpand);
        }
        else hideView(holder.contactItemExpand);
        holder.contactItemMain.setOnClickListener(view -> {
            notifyItemChanged(ContactRepository.getPreviousExpandId());
            ContactRepository.setPreviousExpandId(isExpanded?-1:position);
            notifyItemChanged(position);
        });
        holder.btnCall.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", currentContact.getPhoneNumber(), null));
            mContext.startActivity(intent);
        });
        holder.btnMessage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", currentContact.getPhoneNumber(), null));
            mContext.startActivity(intent);
        });
        holder.btnDelete.setOnClickListener(view -> {
            contactRepository.deleteContact(contacts.get(position));
            contactsAll.remove(currentContact);
            contacts.remove(position);
            notifyDataSetChanged();
        });
        holder.btnInfo.setOnClickListener(view -> {

            Intent intent = new Intent(mContext, InfoActivity.class);
            intent.putExtra("fullName",currentContact.getFullName());
            intent.putExtra("phoneNumber",currentContact.getPhoneNumber());
            intent.putExtra("email",currentContact.getEmail());
            intent.putExtra("firstChar",currentContact.getFirstChar());
            intent.putExtra("detail",currentContact.getDetail());
            intent.putExtra("color", currentContact.getAvatarColor());
            intent.putExtra("pos", position);
            intent.putExtra("cid", currentContact.getCid());
            intent.putExtra("repo", contactRepository);
            ((Activity)mContext).startActivityForResult(intent, REQUEST_CODE_INFO_CONTACT);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Contact> filteredContacts = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredContacts.addAll(contactsAll);
            } else {
                for (Contact contact : contactsAll){
                    if (contact.getFullName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredContacts.add(contact);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredContacts;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ContactRepository.setPreviousExpandId(-1);
            contacts.clear();
            contacts.addAll((List<Contact>) filterResults.values);

            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFullName, txtEmail, txtAvatar;
        private CardView imgAvatar, contactItemMain, btnInfo, btnDelete, btnCall, btnMessage;
        private RelativeLayout contactItemExpand;
        public ViewHolder(ContactItemBinding binding) {
            super(binding.getRoot());
            txtFullName = binding.txtFullName;
            txtEmail = binding.txtEmail;
            txtAvatar = binding.txtAvatar;
            imgAvatar = binding.imgAvatar;
            contactItemMain = binding.contactItemMain;
            btnInfo = binding.btnInfo;
            btnDelete = binding.btnDelete;
            btnCall = binding.btnCall;
            btnMessage = binding.btnMessage;
            contactItemExpand = binding.contactItemExpand;
        }

    }

    private void showView(final View view){
        Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        view.setVisibility(View.VISIBLE);
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(slideDown);
    }

    private void hideView(final View view){
        Animation slideUp= AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(slideUp);
    }
}
