package com.example.velimiratanasovski.contacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.velimiratanasovski.contacts.model.Contact;


public class DetailContactActivity extends AppCompatActivity {

    public static final String CONTACT_POSITION = "CONTACT_POSITION";

    private Contact mContact;
    private TextView mName, mLastName, mAddress, mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        try {
            mContact = getIntent().getExtras().getParcelable(CONTACT_POSITION);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
            return;

        }

        mName = findViewById(R.id.textView_name);
        mLastName = findViewById(R.id.textView_lastname);
        mAddress = findViewById(R.id.textView_address);
        mPhone = findViewById(R.id.textView_phone);


        if(mContact == null){
            finish();
            return;
        }
        mName.setText(mContact.getName());
        mLastName.setText(mContact.getLastname());
        mAddress.setText(mContact.getAddress());
        mPhone.setText(mContact.getPhoneNumber());

    }




}
