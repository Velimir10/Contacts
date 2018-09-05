package com.example.velimiratanasovski.contacts;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.velimiratanasovski.contacts.model.Contact;
import com.squareup.picasso.Picasso;

public class DetailContactActivity extends AppCompatActivity {

    public static final String CONTACT_POSITION = "CONTACT_POSITION";
    public static final String EDIT_CONTACT = "EDIT_CONTACT";
    private Contact mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);
        ImageView contactAvatar = findViewById(R.id.expandedImage);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        try {
            mContact = getIntent().getExtras().getParcelable(CONTACT_POSITION);
        } catch (Exception e) {
            Log.e("ContactParcelable --- >", e.getMessage());
            e.printStackTrace();
            finish();
            return;
        }

        final TextView address = findViewById(R.id.textView_address);
        final TextView phone = findViewById(R.id.textView_phone);
        final TextView eMail = findViewById(R.id.textView_eMail);

        if (mContact == null) {
            finish();
            return;
        }

        collapsingToolbar.setTitle(mContact.getName() + " " + mContact.getLastName());

        if(mContact.getAvatar() != null){
            Picasso.get().load(mContact.getAvatar()).into(contactAvatar);
        }
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

    public void onButtonCallClick(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + mContact.getPhoneNumber()));
        startActivity(intent);
    }
}
