package com.zejian.aidl.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by zejian on 2018/2/21
 * 自定义contentProvide，实现进程间通信，底层依靠binder
 */

public class PersonContentProvider extends ContentProvider {

    public static final String TAG="zejian0";

    /**
     * authority：整个提供程序的符号名称
     */
    public static final String AUTHORITY="com.zejian.content.provider";


    public static final Uri PERSON_CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/person");

    public static final int PERSON_URI_CODE = 0;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,"person",PERSON_URI_CODE);
    }

    SQLiteDatabase mDb;

    private String getTableName(Uri uri){
        String tableName  = null;
        switch (uriMatcher.match(uri)){
            case PERSON_URI_CODE:
                tableName = DbOpenHelper.PERSON_TABLE_NAME;
                break;
        }

        return tableName;
    }

    @Override
    public boolean onCreate() {
        initContentProviderData();
        return false;
    }

    private void initContentProviderData() {
        mDb= new DbOpenHelper(getContext()).getWritableDatabase();
        mDb.execSQL("delete from " + DbOpenHelper.PERSON_TABLE_NAME);
        mDb.execSQL("insert into person values (1,'宫本','39')");
        mDb.execSQL("insert into person values (2,'吴增','28')");
        mDb.execSQL("insert into person values (3,'龙丽','18')");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG,"query current thread:"+Thread.currentThread().getName());
        String table = getTableName(uri);

        if (table == null){
            throw  new IllegalArgumentException("Unsupported URI:"+uri);
        }
        return mDb.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
