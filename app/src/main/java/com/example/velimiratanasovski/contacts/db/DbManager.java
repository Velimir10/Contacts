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

    private SQLiteDatabase mDb;
    private static DbManager instance;
    private LoadListener mListener;


    private DbManager() {
    }

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

    public void setListener(LoadListener mListener){
        this.mListener = mListener;
    }

    synchronized private void setDb(DbHelper helper) {
        if (mDb == null) {
            mDb = helper.getWritableDatabase();
        }
    }


    public List<Contact> readAll(DbHelper helper) {
        setDb(helper);
        List<Contact> myContacts = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + ContactTable.TABLE_NAME, null);
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
        cursor.close();
        return myContacts;
    }

    public void insertContact(final Context context, final Contact contact,final DbHelper helper){
        setDb(helper);
        new AsyncTask<Contact, Void, Void>(){
            @Override
            protected Void doInBackground(Contact... contacts) {

                Contact contact = contacts[0];

                try {
                    ContentValues mValues = new ContentValues();
                    mValues.put(ContactTable.COLUMN_NAME, contact.getName());
                    mValues.put(ContactTable.COLUMN_LASTNAME, contact.getLastname());
                    mValues.put(ContactTable.COLUMN_ADDRESS, contact.getAddress());
                    mValues.put(ContactTable.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());

                    long rowId = mDb.insert(ContactTable.TABLE_NAME, null, mValues);
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
                mListener.onInsert();
            }
        }.execute(contact);

    }

    public interface LoadListener{
        void onInsert();
        void onUpdate();
        void onDelete();
    }
}
