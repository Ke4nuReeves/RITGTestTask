package com.dmitriytitov.ritgtesttask;

/**
 * Created by Dmitriy Titov on 17.06.2017.
 */

public class Constants {
    public static class URL {

        private static final String HOST = "http://192.168.238.2:8080/";

        public static final String GET_COUNTRY_ITEMS = HOST + "data_controller/get_countries";
        public static final String GET_MUSIC = HOST + "data_controller/get_music";
        public static final String POST_USER_DATA = HOST + "data_controller/post_user_data";
    }
}
