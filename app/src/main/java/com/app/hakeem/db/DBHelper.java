package com.app.hakeem.db;

/**
 * Created by aditya.singh on 9/9/2016.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.hakeem.App;
import com.app.hakeem.pojo.ChatMessage;
import com.app.hakeem.util.ChatRepo;


public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION =8;
    // Database Name
    private static final String DATABASE_NAME = "sqliteDBMultiTbl.db";
    private static final String TAG = DBHelper.class.getSimpleName().toString();

    public DBHelper( ) {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        db.execSQL(ChatRepo.createTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + ChatMessage.TABLE);

        onCreate(db);
    }

}
