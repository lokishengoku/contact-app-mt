package code.butchers.contactapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

import code.butchers.contactapp.R;
import code.butchers.contactapp.adapter.ContactsAdapter;
import code.butchers.contactapp.databinding.ActivityMainBinding;
import code.butchers.contactapp.db.ContactRepository;
import code.butchers.contactapp.models.Contact;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_CONTACT = 1;
    public static final int REQUEST_CODE_INFO_CONTACT = 2;
    private static final String TAG = "MainActivity";

    private ContactRepository contactRepository;
    private List<Contact> contacts;
    private ActivityMainBinding binding;
    private RecyclerView recViewContacts;
    private FloatingActionButton btnAddContact;
    private ContactsAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        contactRepository = new ContactRepository(getApplication());
        contacts = new ArrayList<>();
        recViewContacts = binding.recViewContacts;
        recViewContacts.setLayoutManager(new LinearLayoutManager(this));
        recViewContacts.setHasFixedSize(true);
        btnAddContact = binding.btnAddContact;
        btnAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
            intent.putExtra("isNew", true);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT);
        });
        adapter = new ContactsAdapter(this, contactRepository);
        adapter.setContact(contacts);
        recViewContacts.setAdapter(adapter);
        AsyncTask.execute(() -> {
            List<Contact> dbContacts = contactRepository.getContactDao().getAllContacts();
            contacts.addAll(dbContacts);
            adapter.setContact(contacts);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode==REQUEST_CODE_ADD_CONTACT) {
                assert data != null;
                Contact contact = (Contact) data.getSerializableExtra("contact");
                contacts.add(contact);
                adapter.setContact(contacts);
                adapter.notifyDataSetChanged();
                contactRepository.insertContact(contact);
                Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
            } else if (requestCode==REQUEST_CODE_INFO_CONTACT){
                assert data != null;
                String action = data.getStringExtra("action");
                int pos = data.getIntExtra("pos", -1);
                assert action != null;
                if(action.equals("delete")){
                    contactRepository.deleteContact(contacts.get(pos));
                    ContactRepository.setPreviousExpandId(-1);
                    contacts.remove(pos);
                    adapter.notifyDataSetChanged();
                } else if(action.equals("update")){
                    Log.d(TAG, "onActivityResult: 115");
                    Contact updatedContact = (Contact) data.getSerializableExtra("updatedContact");
                    contacts.add(pos, updatedContact);
                    contacts.remove(pos+1);
                    adapter.notifyItemChanged(pos);
                }
            }
        }
    }
}