package com.example.velimiratanasovski.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.velimiratanasovski.contacts.db.DbHelper;
import com.example.velimiratanasovski.contacts.db.DbManager;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.List;

public class DatabaseLoader extends AsyncTaskLoader<List<Contact>> {

    private Context mContext;


    public DatabaseLoader(@NonNull Context context) {
        super(context);
        mContext = context;
        }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Contact> loadInBackground() {
        return DbManager.readAll(mContext);
        }

    @Override
    public void deliverResult(@Nullable List<Contact> data) {
        super.deliverResult(data);
    }
}
