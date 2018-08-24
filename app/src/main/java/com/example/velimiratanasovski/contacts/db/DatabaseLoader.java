package com.example.velimiratanasovski.contacts.db;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.List;

public class DatabaseLoader extends AbstractDataLoader<Contact> {

    private DbHelper mHelper;
    private DbManager mDbManager;

    public DatabaseLoader(@NonNull Context context, DbManager dbManager) {
        super(context);
        mHelper = new DbHelper(context);
        mDbManager = dbManager;
    }

    @Override
    protected List<Contact> buildList() {
        return mDbManager.readAll((mHelper));
    }
}
