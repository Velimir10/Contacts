package com.example.velimiratanasovski.contacts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.velimiratanasovski.contacts.model.Contact;
import java.util.ArrayList;
import java.util.List;
import static com.example.velimiratanasovski.contacts.db.DatabaseContract.*;

public final class DbHelper extends SQLiteOpenHelper {

    private List<Contact> initialContacts = new ArrayList<>();
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactTable.SQL_CREATE_TABLE);
        addInitialContacts();
        DbManager.getInstance().insertInitialContacts(db, initialContacts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContactTable.SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void addInitialContacts(){
        initialContacts.add(new Contact("Stefan", "Stefanovic", "Miroslava Ilica 15", "0652288456", "sstefan@hotmail.com", "https://i.kinja-img.com/gawker-media/image/upload/s--PUQWGzrn--/c_scale,f_auto,fl_progressive,q_80,w_800/yktaqmkm7ninzswgkirs.jpg"));
        initialContacts.add(new Contact("Marko", "Markovic", "Djordja Jovanovic 7a", "0652282281", "markommm@hotmail.com","https://i.kinja-img.com/gawker-media/image/upload/s--PUQWGzrn--/c_scale,f_auto,fl_progressive,q_80,w_800/yktaqmkm7ninzswgkirs.jpg"));
        initialContacts.add(new Contact("Dusan", "Petrovic", "Hajduk Veljkova 118", "0634445451", "dule11@hotmail.com","https://static.hltv.org/images/galleries/converted/3703-thumbretina/1309108427.29.jpeg"));
    }
}
