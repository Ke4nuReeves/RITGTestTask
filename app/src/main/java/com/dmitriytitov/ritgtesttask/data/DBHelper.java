package com.dmitriytitov.ritgtesttask.data;

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
    public static final String COUNTRY_LIST = "COUNTRY_LIST";
    public static final String COUNTRY_NAME = "COUNTRY_NAME";
    public static final String CAPITAL_NAME = "CAPITAL_NAME";

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + COUNTRY_LIST + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COUNTRY_NAME + " TEXT, "
                + CAPITAL_NAME + " TEXT);");
        insertData(db, "Россия", "Москва");
        insertData(db, "Китай", "Пекин");
        insertData(db, "Япония", "Токио");
        insertData(db, "США", "Вашингтон");
    }

    private void insertData(SQLiteDatabase db, String countryName, String capitalName) {
        ContentValues values = new ContentValues();
        values.put(COUNTRY_NAME, countryName);
        values.put(CAPITAL_NAME, capitalName);
        db.insert(COUNTRY_LIST, null, values);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
