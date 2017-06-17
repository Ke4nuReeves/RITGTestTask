package com.dmitriytitov.ritgtesttask.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.dmitriytitov.ritgtesttask.Constants;
import com.dmitriytitov.ritgtesttask.Country;
import com.dmitriytitov.ritgtesttask.fragments.RecyclerViewAdapter;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
    private List<Country> countryList;

    public DataLoader(Context context, RecyclerView recyclerView, RequestType requestType) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.requestType = requestType;
        //TODO delete initialization here
        this.countryList = new ArrayList<>();
    }


    public void requestData() {
        switch (requestType) {
            /*case COMBINED:
                new DBSync().execute();
                break;*/
            case HTTP:
                new HttpRequest().execute();
                break;
            case SQLITE:
                new SQLiteRequest().execute();
                break;
            default:
                for (int i = 0; i < 19; i++) {
                    countryList.add(new Country(i,"Китай", "Гонг-Конг"));
                }
                RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(countryList);
                recyclerView.setAdapter(rvAdapter);
        }
    }

    private class HttpRequest extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            ResponseEntity<List<Country>> response = template.exchange(Constants.URL.GET_COUNTRY_ITEMS,
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Country>>() {});
            countryList = response.getBody();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(countryList);
            recyclerView.setAdapter(rvAdapter);
        }
    }

    private class DBSync extends AsyncTask<Void,Void,Boolean> {
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

                Cursor cursor = db.query(DBHelper.COUNTRY_LIST,
                        null, null, null, null, null,
                        "_id");

                if(cursor.moveToFirst()){
                    do{
                        int id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID));
                        String countryName = cursor.getString(cursor.getColumnIndex(DBHelper.COUNTRY_NAME));
                        String capitalName = cursor.getString(cursor.getColumnIndex(DBHelper.CAPITAL_NAME));
                        countryList.add(new Country(id, countryName, capitalName));
                    }while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
                return true;
            } catch (SQLiteException ex) {
                Log.d("log", ex.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(countryList);
                recyclerView.setAdapter(rvAdapter);
            }
        }
    }
}
