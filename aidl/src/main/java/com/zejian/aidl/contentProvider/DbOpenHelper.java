package com.zejian.aidl.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zejian on 2018/2/21
 * 数据库操作
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String BD_NAME="person_provider_db";
    public static final String PERSON_TABLE_NAME="person";
    public static final String USER_TABLE_NAME="user";

    private static final int DB_VERSION = 1;

    private String CREATE_PERSON_TABLE = "CREATE TABLE IF NOT EXISTS " + PERSON_TABLE_NAME +
            " (_id integer primary key,"+"name TEXT,"+"age Int)";


    public DbOpenHelper(Context context) {
        super(context, BD_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PERSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
