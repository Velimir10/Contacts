package com.example.velimiratanasovski.contacts.db;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.List;

public class DatabaseLoader extends AbstractDataLoader<Contact> {

    private DbHelper mHelper;
    private DbManager mDbManager;
    private String[] columns;
    private String selection;
    private String[] selectionArgs;
    private String groupBy;
    private String having;
    private String limit;

    public DatabaseLoader(@NonNull Context context, DbManager dbManager, String[] columns,
                          String selection,
                          String[] selectionArgs, String groupBy, String having, String limit) {
        super(context);
        this.columns = columns;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy = groupBy;
        this.having = having;
        this.limit = limit;
        mHelper = new DbHelper(context);
        mDbManager = dbManager;
    }

    @Override
    protected List<Contact> buildList() {
        return mDbManager.read(mHelper,columns,selection,selectionArgs,groupBy,having,limit);
    }
}
