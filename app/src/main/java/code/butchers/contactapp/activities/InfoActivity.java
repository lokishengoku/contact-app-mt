package code.butchers.contactapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import code.butchers.contactapp.databinding.ActivityInfoBinding;
import code.butchers.contactapp.db.ContactRepository;
import code.butchers.contactapp.models.Contact;

public class InfoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_EDIT_CONTACT = 3;
    private TextView txtFullName, txtAvatar, txtPhoneNumber, txtEmail, txtDetail;
    private CardView imgAvatar, btnCall, btnMessage, btnVideoCall, btnEdit, btnDelete;
    private ImageView btnBack;
    private int position, cid;
    private ContactRepository contactRepository;
    private boolean isUpdate=false;
    private Contact updatedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInfoBinding binding = ActivityInfoBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        Objects.requireNonNull(getSupportActionBar()).hide();

        txtFullName = binding.txtFullName;
        txtAvatar = binding.txtAvatar;
        txtPhoneNumber = binding.txtPhoneNumber;
        txtEmail = binding.txtEmail;
        txtDetail = binding.txtDetail;
        imgAvatar = binding.imgAvatar;
        btnCall = binding.btnCall;
        btnMessage = binding.btnMessage;
        btnVideoCall = binding.btnVideoCall;
        btnEdit = binding.btnEdit;
        btnDelete = binding.btnDelete;
        btnBack = binding.btnBack;

        Intent intent = getIntent();
        String fullName = intent.getStringExtra("fullName");
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String email = intent.getStringExtra("email");
        String detail = intent.getStringExtra("detail");
        int pos = intent.getIntExtra("pos", -1);
        int color = intent.getIntExtra("color", 0xda23a4);
        cid = intent.getIntExtra("cid", -1);
        contactRepository = (ContactRepository) intent.getSerializableExtra("repo");

        txtFullName.setText(fullName);
        txtAvatar.setText(intent.getStringExtra("firstChar"));
        txtPhoneNumber.setText(phoneNumber);
        txtEmail.setText(email);
        txtDetail.setText(detail);
        imgAvatar.setCardBackgroundColor(color);
        position = pos;

        btnBack.setOnClickListener(view -> {
            if (isUpdate){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("action", "update");
                returnIntent.putExtra("pos", position);
                returnIntent.putExtra("updatedContact", updatedContact);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else onBackPressed();

        });
        btnDelete.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("action", "delete");
            returnIntent.putExtra("pos", position);
            setResult(RESULT_OK, returnIntent);
            finish();
        });
        btnEdit.setOnClickListener(view -> {
            Intent intent1 = new Intent(InfoActivity.this, EditContactActivity.class);
            intent1.putExtra("isNew", false);
            intent1.putExtra("fullName", fullName);
            intent1.putExtra("phoneNumber", phoneNumber);
            intent1.putExtra("email", email);
            intent1.putExtra("detail", detail);
            intent1.putExtra("pos", pos);
            intent1.putExtra("color", color);
            startActivityForResult(intent1, REQUEST_CODE_EDIT_CONTACT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK) && (requestCode==REQUEST_CODE_EDIT_CONTACT)){
            assert data != null;
            updatedContact = (Contact) data.getSerializableExtra("contact");
            assert updatedContact != null;
            updatedContact.setCid(cid);
            contactRepository.updateContact(updatedContact);
            isUpdate=true;

            String fullName = updatedContact.getFullName();
            String phoneNumber = updatedContact.getPhoneNumber();
            String email = updatedContact.getEmail();
            String detail = updatedContact.getDetail();
            int color = updatedContact.getAvatarColor();

            txtFullName.setText(fullName);
            txtAvatar.setText(updatedContact.getFirstChar());
            txtPhoneNumber.setText(phoneNumber);
            txtEmail.setText(email);
            txtDetail.setText(detail);
            imgAvatar.setCardBackgroundColor(color);
        }
    }
}