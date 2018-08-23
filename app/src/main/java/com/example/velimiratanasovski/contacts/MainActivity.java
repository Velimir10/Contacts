package com.example.velimiratanasovski.contacts;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.velimiratanasovski.contacts.adapters.MyContactRecyclerAdapter;
import com.example.velimiratanasovski.contacts.db.DbHelper;
import com.example.velimiratanasovski.contacts.db.DbManager;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.List;
import static com.example.velimiratanasovski.contacts.AddContactActivity.NEW_CONTACT;
import static com.example.velimiratanasovski.contacts.DetailContactActivity.CONTACT_POSITION;


public class MainActivity extends AppCompatActivity implements MyContactRecyclerAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<List<Contact>> {

    public static final int ADD_ACTIVITY_REQUEST_CODE = 0;
    public static final int MY_TASK_LOADER = 1;
    private RecyclerView mRecyclerView;
    private MyContactRecyclerAdapter mAdapter;
    private DbManager mDbmanager;
    private DbHelper mHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DbHelper(this);
        mDbmanager = DbManager.getInstance();
        getSupportLoaderManager().initLoader(MY_TASK_LOADER, null, this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyContactRecyclerAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

    }


    public void onFabClick(View view) {

        Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
        startActivityForResult(intent, ADD_ACTIVITY_REQUEST_CODE);

    }


    // This method is called when 2nd Activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                Contact newContact = data.getExtras().getParcelable(NEW_CONTACT);
                if (newContact != null) {
                    mDbmanager.insertContact(this,newContact,mHelper);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.close();

    }

    @Override
    public void OnItemClick(Contact contact) {
        Intent intent = new Intent(this, DetailContactActivity.class);
        intent.putExtra(CONTACT_POSITION, contact);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<List<Contact>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new DatabaseLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Contact>> loader, List<Contact> contacts) {
        //if (contacts != null)
            mAdapter.setContacts(contacts);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Contact>> loader) {
            mAdapter.setContacts(null);
    }
}
