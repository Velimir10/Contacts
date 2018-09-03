package com.example.velimiratanasovski.contacts.db;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.List;

public class DatabaseLoader extends AbstractDataLoader<Contact> {

    private DbHelper mHelper;
    private DbManager mDbManager;
    private String[] mColumns;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mLimit;

    public DatabaseLoader(@NonNull Context context, DbManager dbManager, String[] columns,
                          String selection,
                          String[] selectionArgs, String groupBy, String having, String limit) {
        super(context);
        mColumns = columns;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mLimit = limit;
        mHelper = new DbHelper(context);
        mDbManager = dbManager;
    }

    @Override
    protected List<Contact> buildList() {
        return mDbManager.read(mHelper, mColumns, mSelection, mSelectionArgs, mGroupBy, mHaving, mLimit);
    }
}
