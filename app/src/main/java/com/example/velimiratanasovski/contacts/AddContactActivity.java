package com.example.velimiratanasovski.contacts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.velimiratanasovski.contacts.model.Contact;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.velimiratanasovski.contacts.DetailContactActivity.EDIT_CONTACT;

public class AddContactActivity extends AppCompatActivity {

    public static final String NEW_CONTACT = "NEW CONTACT";
    private EditText mName, mLastName, mAddress, mPhoneNumber, mEmailAddress;
    private CircleImageView mContactAvatar;
    private Contact mEditContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mName = findViewById(R.id.editText_name);
        mLastName = findViewById(R.id.editText_lastname);
        mAddress = findViewById(R.id.editText_address);
        mPhoneNumber = findViewById(R.id.editText_number);
        mEmailAddress = findViewById(R.id.editText_eMail);
        mContactAvatar = findViewById(R.id.contactAvatar);

        setSupportActionBar(toolbar);

        try {
            mEditContact = getIntent().getExtras().getParcelable(EDIT_CONTACT);
            if (mEditContact != null) {
                setContactValues();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSaveButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        if (TextUtils.isEmpty(mName.getText()) || TextUtils.isEmpty(mPhoneNumber.getText())) {
            Toast.makeText(this, R.string.toast_add_contact, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mEditContact != null) {
            mEditContact.setId(mEditContact.getId());
            mEditContact.setName(mName.getText().toString());
            mEditContact.setLastName(mLastName.getText().toString());
            mEditContact.setAddress(mAddress.getText().toString());
            mEditContact.setPhoneNumber(mPhoneNumber.getText().toString());
            mEditContact.seteMail(mEmailAddress.getText().toString());
            if (mEditContact.getAvatar() != null) {
                Picasso.get().load(mEditContact.getAvatar()).into(mContactAvatar);
            }

        } else {
            mEditContact = getContact();
        }
        intent.putExtra(NEW_CONTACT, mEditContact);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onCancelButtonClick(View view){
        finish();
    }

    private Contact getContact() {
        return new Contact(mName.getText().toString(),
                mLastName.getText().toString(),
                mAddress.getText().toString(),
                mPhoneNumber.getText().toString(),
                mEmailAddress.getText().toString(),null);
    }

    public void setContactValues() {
        mName.setText(mEditContact.getName());
        mLastName.setText(mEditContact.getLastName());
        mPhoneNumber.setText(mEditContact.getPhoneNumber());
        mAddress.setText(mEditContact.getAddress());
        mEmailAddress.setText(mEditContact.getEmail());
        if(mEditContact.getAvatar() != null) {
            Picasso.get().load(Uri.parse(mEditContact.getAvatar())).into(mContactAvatar);
        }
    }
}
