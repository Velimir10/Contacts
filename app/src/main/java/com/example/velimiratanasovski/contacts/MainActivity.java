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
import com.example.velimiratanasovski.contacts.db.DatabaseLoader;
import com.example.velimiratanasovski.contacts.db.DbHelper;
import com.example.velimiratanasovski.contacts.db.DbManager;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.List;
import static com.example.velimiratanasovski.contacts.AddContactActivity.NEW_CONTACT;
import static com.example.velimiratanasovski.contacts.DetailContactActivity.CONTACT_POSITION;


public class MainActivity extends AppCompatActivity implements MyContactRecyclerAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<List<Contact>>, DbManager.LoadListener {

    public static final int ADD_ACTIVITY_REQUEST_CODE = 0;
    public static final int LOADER_ID = 1;
    private MyContactRecyclerAdapter mAdapter;
    private DbManager mDbManager;
    private DbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DbHelper(this);
        mDbManager = DbManager.getInstance();
        mDbManager.setListener(this);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
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
                    mDbManager.insertContact(this,newContact,mHelper);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOADER_ID);
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
        return new DatabaseLoader(this, mDbManager);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Contact>> loader, List<Contact> contacts) {
            mAdapter.setContacts(contacts);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Contact>> loader) {
            mAdapter.setContacts(null);
    }

    @Override
    public void onInsert() {
        Loader loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if(loader != null){
            loader.onContentChanged();
        }
    }

    @Override
    public void onUpdate() {
        Loader loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if(loader != null){
            loader.onContentChanged();
        }
    }

    @Override
    public void onDelete() {
        Loader loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if(loader != null){
            loader.onContentChanged();
        }
    }
}
