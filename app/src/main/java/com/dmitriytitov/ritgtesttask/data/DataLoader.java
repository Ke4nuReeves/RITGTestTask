package com.dmitriytitov.ritgtesttask.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dmitriytitov.ritgtesttask.Constants;
import com.dmitriytitov.ritgtesttask.Country;
import com.dmitriytitov.ritgtesttask.fragments.RecyclerViewAdapter;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
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
    }

    public void requestData() {
        switch (requestType) {
            case COMBINED:
                new SQLiteRequestAfterDBSync().execute();
                break;
            case HTTP:
                new HttpRequest().execute();
                break;
            case SQLITE:
                new SQLiteRequest().execute();
                break;
        }
    }

    private class HttpRequest extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            ResponseEntity<List<Country>> response;
            try {
                RestTemplate template = new RestTemplate();
                response = template.exchange(Constants.URL.GET_COUNTRY_ITEMS,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Country>>() {
                        });
                countryList = response.getBody();
            } catch (RestClientException ex) {

                return false;
            }

            return response.getStatusCode() == HttpStatus.OK;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(countryList);
                recyclerView.setAdapter(rvAdapter);
            }
        }
    }

    private class SQLiteRequestAfterDBSync extends AsyncTask<Void,Void,Exception> {

        ContentValues countryValues;
        List<Country> temp;

        @Override
        protected void onPreExecute() {
            countryValues = new ContentValues();
        }

        @Override
        protected Exception doInBackground(Void... params) {

            try{
                ResponseEntity<List<Country>> response;
                RestTemplate template = new RestTemplate();
                response = template.exchange(Constants.URL.GET_COUNTRY_ITEMS,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Country>>() {});
                temp = response.getBody();

                SQLiteOpenHelper dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                for (Country country : temp) {
                    if (checkOnExisting(db, country.getId())) {
                        continue;
                    }
                    countryValues.put(DBHelper.ID, country.getId());
                    countryValues.put(DBHelper.COUNTRY_NAME, country.getName());
                    countryValues.put(DBHelper.CAPITAL_NAME, country.getCapitalName());
                    db.insert(DBHelper.COUNTRY_LIST, null, countryValues);
                }
                db.close();

                if (response.getStatusCode() == HttpStatus.OK) {

                    return null;
                }

                throw new RestClientException("Not ok");

            } catch (SQLiteException|RestClientException ex) {

                return ex;
            }
        }

        private boolean checkOnExisting(SQLiteDatabase db, long id) {
            Cursor cursor = db.query(DBHelper.COUNTRY_LIST, new String[] {DBHelper.ID}, DBHelper.ID + " = " + id,
                    null, null, null, null, null);
            int count = cursor.getCount();
            cursor.close();
            return count > 0;
        }

        @Override
        protected void onPostExecute(Exception ex) {
            if (ex != null) {
                if (ex instanceof SQLiteException) {
                    Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (ex instanceof RestClientException) {
                    Toast toast = Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                new SQLiteRequest().execute();
            }
        }
    }

    private class SQLiteRequest extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            countryList = new ArrayList<>();
        }

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
