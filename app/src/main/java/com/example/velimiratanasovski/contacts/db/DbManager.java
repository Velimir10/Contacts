package com.example.velimiratanasovski.contacts.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.velimiratanasovski.contacts.db.DatabaseContract.ContactTable;
import com.example.velimiratanasovski.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

public final class DbManager {

    private static DbManager instance;
    private SQLiteDatabase mDb;
    private List<LoadListener> mListeners;

    private DbManager() {
        mListeners = new ArrayList<>();
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

    public void onInsertListener() {
        for (LoadListener listener : mListeners) {
            listener.onInsert();
        }
    }

    public void onUpdateListener() {
        for (LoadListener listener : mListeners) {
            listener.onUpdate();
        }
    }

    public void onDeleteListener() {
        for (LoadListener listener : mListeners) {
            listener.onDelete();
        }
    }

    synchronized private void setDb(DbHelper helper) {
        if (mDb == null) {
            mDb = helper.getWritableDatabase();
        }
    }


    public List<Contact> readAll(DbHelper helper) {
        setDb(helper);
        List<Contact> myContacts = new ArrayList<>();
        String query = "SELECT * FROM " + ContactTable.TABLE_NAME + " ORDER BY " +
                ContactTable.COLUMN_NAME +  "," + ContactTable.COLUMN_LASTNAME + " COLLATE NOCASE ASC";
        Cursor cursor = mDb.rawQuery(query, null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ContactTable._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_NAME));
                String lastname = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_LASTNAME));
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

    public void insertContact(final Context context, final Contact contact, final DbHelper helper) {
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

    public void updateContact(final Context context, final Contact contact, final DbHelper helper) {
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
        values.put(ContactTable.COLUMN_LASTNAME, contact.getLastName());
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
