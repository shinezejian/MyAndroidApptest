package com.zejian.aidl.contentProvider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zejian.aidl.Person;
import com.zejian.aidl.R;

/**
 * Created by zejian on 2018/2/21
 */

public class ContentProviderActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri personUri = Uri.parse("content://com.zejian.content.provider/person");

//        ContentValues values = new ContentValues();
//        values.put("_id",2);

        Cursor cursor=getContentResolver().query(personUri,new String[]{"name,age"},null,null,null );
        while (cursor.moveToNext()){
            Person p = new Person(cursor.getString(0),cursor.getInt(1));
            Log.d(PersonContentProvider.TAG,"person:"+p.toString());
        }
        cursor.close();
    }
}
