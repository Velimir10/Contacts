package com.example.velimiratanasovski.contacts;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
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

public class AddContactActivity extends AppCompatActivity implements CircleImageView.OnClickListener {

    public static final String NEW_CONTACT = "NEW CONTACT";
    private static final int GALLERY_REQUEST = 10;
    public static final String IMAGE_KEY = "IMAGE_KEY";
    private EditText mName, mLastName, mAddress, mPhoneNumber, mEmailAddress;
    private CircleImageView mContactAvatar;
    private Contact mEditContact;
    private String mAvatar;


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
        mContactAvatar.setOnClickListener(this);

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
            mEditContact.setAvatar(mAvatar);

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
                mEmailAddress.getText().toString(),
                mAvatar);
    }

    public void setContactValues() {
        mName.setText(mEditContact.getName());
        mLastName.setText(mEditContact.getLastName());
        mPhoneNumber.setText(mEditContact.getPhoneNumber());
        mAddress.setText(mEditContact.getAddress());
        mEmailAddress.setText(mEditContact.getEmail());
        Picasso.get().load(mEditContact.getAvatar()).into(mContactAvatar);

    }

    @Override
    public void onClick(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
                Uri uri  = data.getData();
                if(uri == null){
                    return;
                }
                mAvatar = uri.toString();
                Picasso.get().load(uri).placeholder(R.drawable.account_icon).into(mContactAvatar);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_KEY, mAvatar);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAvatar = savedInstanceState.getString(IMAGE_KEY);
        Picasso.get().load(mAvatar).placeholder(R.drawable.account_icon).into(mContactAvatar);
    }
}
