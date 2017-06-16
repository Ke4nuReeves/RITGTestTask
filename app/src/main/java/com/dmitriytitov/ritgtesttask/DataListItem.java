package com.dmitriytitov.ritgtesttask;

/**
 * Created by Dmitriy Titov on 16.06.2017.
 */

public class DataListItem {
    private long id;
    private String stringData;

    DataListItem(long id, String stringData) {
        this.id = id;
        this.stringData = stringData;
    }

    public long getId() {
        return id;
    }

    public String getStringData() {
        return stringData;
    }
}
