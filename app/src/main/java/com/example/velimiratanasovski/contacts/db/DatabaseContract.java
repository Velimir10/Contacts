package com.example.velimiratanasovski.contacts.db;

import android.provider.BaseColumns;

public final class DatabaseContract {

     public static final String DATABASE_NAME = "Contacts.db";
     public static int DATABASE_VERSION = 7;

    private DatabaseContract(){}

    public static class ContactTable implements BaseColumns{

        public static final String TABLE_NAME = "Contact_table";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LASTNAME = "last_name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_PHONE_NUMBER = "phone";
        public static final String COLUMN_EMAIL_ADDRESS = "e_mail";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_LASTNAME + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                COLUMN_EMAIL_ADDRESS + " TEXT)";

        public static final String SQL_DELETE_TABLE = "DROP TABLE if EXISTS " + TABLE_NAME;



    }
}
