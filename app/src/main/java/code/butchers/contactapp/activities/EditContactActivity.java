package code.butchers.contactapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Objects;

import code.butchers.contactapp.R;
import code.butchers.contactapp.databinding.ActivityEditContactBinding;
import code.butchers.contactapp.models.Contact;
import petrov.kristiyan.colorpicker.ColorPicker;

public class EditContactActivity extends AppCompatActivity {
    private ActivityEditContactBinding binding;

    private EditText edtFullName, edtPhoneNumber, edtEmail, edtDetail;
    private Button btnColorPicker;
    private ImageView imgAvatar;
    private CardView imgAvatarColor;
    private Intent intent;
    private boolean isNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditContactBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        edtFullName = binding.edtFullName;
        edtDetail = binding.edtDetail;
        edtEmail = binding.edtEmail;
        edtPhoneNumber = binding.edtPhoneNumber;
        btnColorPicker = binding.btnColorPicker;
        imgAvatar = binding.imgAvatar;
        imgAvatarColor = binding.imgAvatarColor;

        btnColorPicker.setOnClickListener(view -> {
            imgAvatar.setVisibility(View.GONE);
            imgAvatarColor.setVisibility(View.VISIBLE);
            final ColorPicker colorPicker = new ColorPicker(EditContactActivity.this);
            colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                @Override
                public void onChooseColor(int position, int color) {
                    imgAvatarColor.setCardBackgroundColor(color);
                }

                @Override
                public void onCancel() {

                }
            })
                    .setRoundColorButton(true)
                    .setDefaultColorButton(Color.parseColor("#f84c44"))
                    .setColumns(5)
                    .show();
            colorPicker.getNegativeButton().setEnabled(true);
        });

        intent = getIntent();
        isNew = intent.getBooleanExtra("isNew", true);
        if (isNew) setTitle("New Contact");
        else {
            setTitle("Edit Contact");
            edtFullName.setText(intent.getStringExtra("fullName"));
            edtPhoneNumber.setText(intent.getStringExtra("phoneNumber"));
            edtEmail.setText(intent.getStringExtra("email"));
            edtDetail.setText(intent.getStringExtra("detail"));
            int color = intent.getIntExtra("color", 0xda23a4);
            imgAvatarColor.setCardBackgroundColor(color);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_submit:
                if(isNew) createNewContact();
                else editContact();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void createNewContact() {
        if(dataValidate()){
            Contact newContact = new Contact(edtFullName.getText().toString(), edtPhoneNumber.getText().toString(),
                    edtEmail.getText().toString(), edtDetail.getText().toString());
            newContact.setAvatarColor(imgAvatarColor.getCardBackgroundColor().getDefaultColor());

            Intent returnIntent = new Intent();
            returnIntent.putExtra("contact", newContact);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    private void editContact() {
        if (dataValidate()){
            Contact newContact = new Contact(edtFullName.getText().toString(), edtPhoneNumber.getText().toString(),
                    edtEmail.getText().toString(), edtDetail.getText().toString());
            newContact.setAvatarColor(imgAvatarColor.getCardBackgroundColor().getDefaultColor());
            Toast.makeText(EditContactActivity.this, "Edited contact successfully!", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("contact", newContact);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    private boolean dataValidate(){
        if (edtFullName.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Full Name is Empty. Please try again!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPhoneNumber.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Phone Number is Empty. Please try again!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}