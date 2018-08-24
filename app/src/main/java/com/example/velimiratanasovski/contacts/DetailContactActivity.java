package com.example.velimiratanasovski.contacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.velimiratanasovski.contacts.model.Contact;

public class DetailContactActivity extends AppCompatActivity {

    public static final String CONTACT_POSITION = "CONTACT_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        Contact mContact;
        try {
            mContact = getIntent().getExtras().getParcelable(CONTACT_POSITION);
        } catch (Exception e) {
            Log.e("ContactParcelable --- >", e.getMessage());
            e.printStackTrace();
            finish();
            return;
        }

        final TextView name = findViewById(R.id.textView_name);
        final TextView lastName = findViewById(R.id.textView_last_name);
        final TextView address = findViewById(R.id.textView_address);
        final TextView phone = findViewById(R.id.textView_phone);

        if (mContact == null) {
            finish();
            return;
        }
        name.setText(mContact.getName());
        lastName.setText(mContact.getLastname());
        address.setText(mContact.getAddress());
        phone.setText(mContact.getPhoneNumber());
    }


}
