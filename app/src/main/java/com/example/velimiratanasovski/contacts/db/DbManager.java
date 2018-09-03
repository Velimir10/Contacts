package com.example.velimiratanasovski.contacts.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.example.velimiratanasovski.contacts.db.DatabaseContract.ContactTable;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.ArrayList;
import java.util.List;

public final class DbManager {

    private final String orderBy;
    private static DbManager instance;
    private SQLiteDatabase mDb;
    private List<LoadListener> mListeners;

    private DbManager() {
        mListeners = new ArrayList<>();
        this.orderBy = ContactTable.COLUMN_NAME + ", " + ContactTable.COLUMN_LAST_NAME + " COLLATE NOCASE ASC";
    }

    public static DbManager getInstance() {
        if (instance == null) {
            synchronized (DbManager.class) {
                if (instance == null) {
                    instance = new DbManager();
                }
            }
        }
        return instance;
    }

    public void setListener(LoadListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void unsubscribeListener(LoadListener listener) {
        mListeners.remove(listener);

    }

    private void onInsertListener() {
        for (LoadListener listener : mListeners) {
            listener.onInsert();
        }
    }

    private void onUpdateListener() {
        for (LoadListener listener : mListeners) {
            listener.onUpdate();
        }
    }

    private void onDeleteListener() {
        for (LoadListener listener : mListeners) {
            listener.onDelete();
        }
    }

    synchronized private void setDb(DbHelper helper) {
        if (mDb == null) {
            mDb = helper.getWritableDatabase();
        }
    }

    public List<Contact> read(DbHelper helper, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String limit) {
        setDb(helper);

        List<Contact> myContacts = new ArrayList<>();
        Cursor cursor = mDb.query(ContactTable.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ContactTable._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_NAME));
                String lastname = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_LAST_NAME));
                String address = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_ADDRESS));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_PHONE_NUMBER));
                String eMailAdress = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_EMAIL_ADDRESS));

                Contact contact = new Contact(id, name, lastname, address, phoneNumber, eMailAdress);
                myContacts.add(contact);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        return myContacts;
    }

    public void insertContact(final Contact contact, final DbHelper helper) {
        setDb(helper);
        new AsyncTask<Contact, Void, Void>() {
            @Override
            protected Void doInBackground(Contact... contacts) {

                Contact contact = contacts[0];

                try {
                    long rowId = mDb.insert(ContactTable.TABLE_NAME, null, generateContentValuesFromContact(contact));
                    if (rowId != -1) {
                        contact.setId((int) rowId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                onInsertListener();
            }
        }.execute(contact);
    }

    public void insertInitialContacts(final SQLiteDatabase db, @NonNull final List<Contact> initialContacts) {

        new AsyncTask<List<Contact>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Contact>... lists) {

                for (int i = 0; i < initialContacts.size(); i++) {

                    try {
                        long rowId = db.insert(ContactTable.TABLE_NAME, null, generateContentValuesFromContact(initialContacts.get(i)));
                        if (rowId != -1) {
                            initialContacts.get(i).setId((int) rowId);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                onInsertListener();
            }
        }.execute(initialContacts);
    }

    public void deleteContacts(@NonNull List<Contact> contacts, final DbHelper helper) {
        setDb(helper);
        new AsyncTask<List<Contact>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Contact>... lists) {
                String queryArray = "(";
                for (int i =0 ; i < lists[0].size(); i++) {


                    if (i == lists[0].size() - 1) {
                        queryArray += lists[0].get(i).getId() + ")";
                    } else {
                        queryArray += lists[0].get(i).getId() + ", ";
                    }
                }
                String where = ContactTable._ID + " in " + queryArray;
                try {
                    mDb.delete(ContactTable.TABLE_NAME, where, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                onDeleteListener();
            }
        }.execute(contacts);
    }

    public void updateContact(final Contact contact, final DbHelper helper) {
        setDb(helper);
        new AsyncTask<Contact, Void, Void>() {
            @Override
            protected Void doInBackground(Contact... contacts) {

                Contact contact = contacts[0];

                try {
                    generateContentValuesFromContact(contact);

                    String where = ContactTable._ID + " = ?";
                    String[] args = {"" + contact.getId()};

                    mDb.update(ContactTable.TABLE_NAME, generateContentValuesFromContact(contact), where, args);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                onUpdateListener();
            }
        }.execute(contact);
    }

    private ContentValues generateContentValuesFromContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactTable.COLUMN_NAME, contact.getName());
        values.put(ContactTable.COLUMN_LAST_NAME, contact.getLastName());
        values.put(ContactTable.COLUMN_ADDRESS, contact.getAddress());
        values.put(ContactTable.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
        values.put(ContactTable.COLUMN_EMAIL_ADDRESS, contact.getEmail());
        return values;
    }

    public interface LoadListener {
        void onInsert();
        void onUpdate();
        void onDelete();
    }
}
