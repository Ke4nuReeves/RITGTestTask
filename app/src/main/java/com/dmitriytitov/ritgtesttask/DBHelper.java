package com.dmitriytitov.ritgtesttask;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dmitriy Titov on 16.06.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ritg_test_task";
    private static final int DB_VERSION = 1;

    public static final String ID = "_id";
    public static final String DATA_LIST = "DATA_LIST";
    public static final String STRING_DATA = "STRING_DATA";

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATA_LIST + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STRING_DATA + " TEXT);");

        ContentValues values = new ContentValues();
        values.put(STRING_DATA, "AMERICA SUCKS");
        for (int i = 0; i < 15; i++) {
            db.insert(DATA_LIST, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
