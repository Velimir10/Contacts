package com.example.velimiratanasovski.contacts.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import com.example.velimiratanasovski.contacts.db.DatabaseContract.ContactTable;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.ArrayList;
import java.util.List;

public final class DbManager {

    private static DbManager instance;

    private DbManager() {}

    public  static DbManager getInstance() {
        if(instance == null){
            synchronized (DbManager.class){
                if (instance == null) {
                    instance = new DbManager();
                }
            }
        }

        return instance;
    }


    public static List<Contact> readAll(Context context) {

        List<Contact> myContacts = new ArrayList<>();

        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ContactTable.TABLE_NAME, null);
        try {
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_NAME));
                String lastname = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_LASTNAME));
                String address = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_ADDRESS));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_PHONE_NUMBER));

                myContacts.add(new Contact(name, lastname, address, phoneNumber));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myContacts;
    }

    public void insertContact(final Context context, final Contact contact,final DbHelper helper){


        new AsyncTask<Contact, Void, Void>(){
            @Override
            protected Void doInBackground(Contact... contacts) {

                Contact contact = contacts[0];
                SQLiteDatabase db = helper.getWritableDatabase();

                try {
                    ContentValues mValues = new ContentValues();
                    mValues.put(ContactTable.COLUMN_NAME, contact.getName());
                    mValues.put(ContactTable.COLUMN_LASTNAME, contact.getLastname());
                    mValues.put(ContactTable.COLUMN_ADDRESS, contact.getAddress());
                    mValues.put(ContactTable.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());

                    long rowId = db.insert(ContactTable.TABLE_NAME, null, mValues);
                    if (rowId != -1) {
                        contact.setId((int) rowId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute(contact);

    }



}
