package com.example.velimiratanasovski.contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.velimiratanasovski.contacts.model.Contact;

public class DetailContactActivity extends AppCompatActivity {

    public static final String CONTACT_POSITION = "CONTACT_POSITION";
    public static final String EDIT_CONTACT = "EDIT_CONTACT";
    private Contact mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
        final TextView eMail = findViewById(R.id.textView_eMail);

        if (mContact == null) {
            finish();
            return;
        }
        name.setText(mContact.getName());
        lastName.setText(mContact.getLastName());
        address.setText(mContact.getAddress());
        phone.setText(mContact.getPhoneNumber());
        eMail.setText(mContact.getEmail());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.toolbar_edit:
                Intent intent = new Intent(this, AddContactActivity.class);
                intent.putExtra(EDIT_CONTACT, mContact);
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
