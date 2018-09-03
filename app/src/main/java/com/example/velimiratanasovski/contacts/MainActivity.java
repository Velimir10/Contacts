package com.example.velimiratanasovski.contacts;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.velimiratanasovski.contacts.adapters.ContactRecyclerAdapter;
import com.example.velimiratanasovski.contacts.db.DatabaseContract.ContactTable;
import com.example.velimiratanasovski.contacts.db.DatabaseLoader;
import com.example.velimiratanasovski.contacts.db.DbHelper;
import com.example.velimiratanasovski.contacts.db.DbManager;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.List;
import static com.example.velimiratanasovski.contacts.AddContactActivity.NEW_CONTACT;
import static com.example.velimiratanasovski.contacts.DetailContactActivity.CONTACT_POSITION;

public class MainActivity extends AppCompatActivity implements ContactRecyclerAdapter.ItemClickListener,
        LoaderManager.LoaderCallbacks<List<Contact>>, DbManager.LoadListener,
        SearchView.OnQueryTextListener {

    public static final String ACTION_MODE_STATUS_KEY = "ACTION_MODE_STATUS_KEY";
    public static final String SELECTED_ITEMS_KEY = "SELECTED_ITEMS_KEY";
    public static final String SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY";
    public static final String SEARCH_ITEM_EXPAND_STATUS = "EXPANDED";
    public static final int ADD_ACTIVITY_REQUEST_CODE = 0;
    public static final int LOADER_ID = 1;
    public static final int DETAIL_ACTIVITY_REQUEST_CODE = 2;
    private ContactRecyclerAdapter mAdapter;
    private ActionMode mActionMode;
    private DbManager mDbManager;
    private DbHelper mHelper;
    private String mSearchText;
    private SearchView mSearchView;
    private MenuItem mSearchItem;
    private boolean mIsSearchItemExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DbHelper(this);
        mDbManager = DbManager.getInstance();
        mDbManager.setListener(this);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ContactRecyclerAdapter(this, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ACTION_MODE_STATUS_KEY, mActionMode != null);
        outState.putIntegerArrayList(SELECTED_ITEMS_KEY, mAdapter.getSelectedItems());
        outState.putString(SEARCH_TEXT_KEY, mSearchText);
        outState.putBoolean(SEARCH_ITEM_EXPAND_STATUS, mSearchItem.isActionViewExpanded());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean(ACTION_MODE_STATUS_KEY)) {
            List<Integer> selectedItems = savedInstanceState.getIntegerArrayList(SELECTED_ITEMS_KEY);
            mActionMode = startSupportActionMode(actionModeCallbacks);
            mAdapter.setSelectedItems(selectedItems);
        }
        mSearchText = savedInstanceState.getString(SEARCH_TEXT_KEY);
        mIsSearchItemExpanded = savedInstanceState.getBoolean(SEARCH_ITEM_EXPAND_STATUS);
    }

    public void onFabClick(View view) {
        if (mActionMode != null) {
            mActionMode.finish();
        }
        if(mSearchItem.isActionViewExpanded()){
            mSearchItem.collapseActionView();
        }
        Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
        startActivityForResult(intent, ADD_ACTIVITY_REQUEST_CODE);
    }
    // This method is called when AddContactActivity or DetailActivity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Contact newContact;
        if (requestCode == ADD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                newContact = data.getExtras().getParcelable(NEW_CONTACT);
                if (newContact != null) {
                    mDbManager.insertContact(newContact, mHelper);
                }
            }
        } else if (requestCode == DETAIL_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                newContact = data.getExtras().getParcelable(NEW_CONTACT);
                if (newContact != null) {
                    mDbManager.updateContact(newContact, mHelper);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOADER_ID);
        mHelper.close();
        mDbManager.unsubscribeListener(this);
    }

    @Override
    public void OnItemClick(Contact contact) {
        if (mActionMode == null) {
            Intent intent = new Intent(this, DetailContactActivity.class);
            intent.putExtra(CONTACT_POSITION, contact);
            startActivityForResult(intent, DETAIL_ACTIVITY_REQUEST_CODE);
        } else {
            mAdapter.selectItem(mAdapter.getItemPosition(contact));
        }
    }

    @Override
    public void OnLongItemClick(int position) {
        if (mActionMode == null) {
            mActionMode = startSupportActionMode(actionModeCallbacks);
            mAdapter.selectItem(position);
        } else {
            mAdapter.selectItem(position);
        }
    }

    @NonNull
    @Override
    public Loader<List<Contact>> onCreateLoader(int i, @Nullable Bundle bundle) {

        if (mSearchText != null) {
            String selection = ContactTable.COLUMN_NAME + " LIKE ? OR " + ContactTable.COLUMN_LAST_NAME + " LIKE ?";
            String[] selectedArg = {mSearchText + "%", mSearchText + "%"};

            return new DatabaseLoader(this, mDbManager, null, selection, selectedArg,
                    null, null, null);
        }
        return new DatabaseLoader(this, mDbManager, null, null, null, null,
                null, null);
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
        if (loader != null) {
            loader.onContentChanged();
        }
    }

    @Override
    public void onUpdate() {
        Loader loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if (loader != null) {
            loader.onContentChanged();
        }
    }

    @Override
    public void onDelete() {
        Loader loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if (loader != null) {
            loader.onContentChanged();
        }
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.contextual_action_mode_menu, menu);
            return true;
        }
        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.icon_delete) {
                DbManager.getInstance().deleteContacts(mAdapter.getSelectedContacts(), mHelper);
                mActionMode.finish();
                return true;
            }
            return false;
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
            mAdapter.clearSelectedItems();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        mSearchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) mSearchItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.search_hint));
        if(!TextUtils.isEmpty(mSearchText) || mIsSearchItemExpanded)
        {
             new Handler().post(new Runnable() {
                 @Override
                 public void run() {
                     mSearchView.setIconified(false);
                     mSearchItem.expandActionView();
                     mSearchView.setQuery(mSearchText,true);
                     mSearchView.setOnQueryTextListener(MainActivity.this);
                 }
             });
        } else {
            mSearchView.setOnQueryTextListener(this);
        }
        getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mSearchText = s;
        getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
        return true;
    }
}
