package com.example.velimiratanasovski.contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.velimiratanasovski.contacts.model.Contact;

public class AddContactActivity extends AppCompatActivity {

    public static final String NEW_CONTACT = "NEW CONTACT";
    private EditText mName, mLastname, mAddress, mPhoneNumber;
    private Contact mNewContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mName = findViewById(R.id.editText_name);
        mLastname = findViewById(R.id.editText_lastname);
        mAddress = findViewById(R.id.editText_address);
        mPhoneNumber = findViewById(R.id.editText_number);

    }

    // Send data back to main activity
    public void onAddButtonClick(View view){
        mNewContact = getContact();
        if(mNewContact != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(NEW_CONTACT, mNewContact);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private Contact getContact() {

        if (mName.getText().toString().equals("") || mLastname.getText().toString().equals("")
                || mAddress.getText().toString().equals("")
                || mPhoneNumber.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();

        } else {
            return new Contact(mName.getText().toString(),
                    mLastname.getText().toString(),
                    mAddress.getText().toString(),
                    mPhoneNumber.getText().toString());

        }
        return null;

    }
}
