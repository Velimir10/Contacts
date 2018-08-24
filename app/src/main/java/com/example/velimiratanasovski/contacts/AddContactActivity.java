package com.example.velimiratanasovski.contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.velimiratanasovski.contacts.model.Contact;

public class AddContactActivity extends AppCompatActivity {

    public static final String NEW_CONTACT = "NEW CONTACT";
    private EditText mName, mLastname, mAddress, mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mName = findViewById(R.id.editText_name);
        mLastname = findViewById(R.id.editText_lastname);
        mAddress = findViewById(R.id.editText_address);
        mPhoneNumber = findViewById(R.id.editText_number);

    }


    public void onAddButtonClick(View view){
        Contact mNewContact = getContact();
        if(mNewContact != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(NEW_CONTACT, mNewContact);
            // Send data back to main activity
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private Contact getContact() {

        if (TextUtils.isEmpty(mName.getText()) || TextUtils.isEmpty(mLastname.getText())
                || TextUtils.isEmpty(mAddress.getText())
                || TextUtils.isEmpty(mPhoneNumber.getText())) {
            Toast.makeText(this, R.string.toast_fill_all_fields, Toast.LENGTH_SHORT).show();

        } else {
            return new Contact(mName.getText().toString(),
                    mLastname.getText().toString(),
                    mAddress.getText().toString(),
                    mPhoneNumber.getText().toString());

        }
        return null;
    }
}
