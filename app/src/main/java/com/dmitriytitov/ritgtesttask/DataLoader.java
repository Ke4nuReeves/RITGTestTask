package com.dmitriytitov.ritgtesttask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Titov on 16.06.2017.
 */

public class DataLoader {

    public enum RequestType {COMBINED, SQLITE, HTTP}

    private Context context;
    private RecyclerView recyclerView;
    private RequestType requestType;
    private List<DataListItem> dataList;

    DataLoader(Context context, RecyclerView recyclerView, RequestType requestType) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.requestType = requestType;
        this.dataList = new ArrayList<>();
    }


    public void requestData() {
        switch (requestType) {
            case SQLITE:
                new SQLiteRequest().execute();
                break;
            default:
                for (int i = 0; i < 19; i++) {
                    dataList.add(new DataListItem(i,"RUSSIA THE BEST"));
                }
                RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(dataList);
                recyclerView.setAdapter(rvAdapter);
        }
    }

    private class ServerRequest extends AsyncTask<Void,Void,Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }
    }

    private class Synchronizer extends AsyncTask<Void,Void,Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }
    }

    private class SQLiteRequest extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            SQLiteOpenHelper dbHelper = new DBHelper(context);
            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor cursor = db.query(DBHelper.DATA_LIST,
                        null, null, null, null, null,
                        "_id");

                if(cursor.moveToFirst()){
                    do{
                        int id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID));
                        String stringData = cursor.getString(cursor.getColumnIndex(DBHelper.STRING_DATA));
                        dataList.add(new DataListItem(id, stringData));
                    }while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
                return true;
            } catch (SQLiteException ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(dataList);
                recyclerView.setAdapter(rvAdapter);
            }
        }
    }
}
