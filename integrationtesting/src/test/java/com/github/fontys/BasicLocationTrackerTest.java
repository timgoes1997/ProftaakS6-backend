package com.github.fontys;

import okhttp3.*;

import java.io.IOException;
import java.util.Random;

public class BasicLocationTrackerTest {

    public static final Random random = new Random();
    public static final String LOCATION_URL = Globals.BASE_URL + ":" + Globals.SERVER_PORT + Globals.API_PATH + "location/";
    public static final String ARRIVAL_URL = Globals.BASE_URL + ":" + Globals.SERVER_PORT + Globals.API_PATH + "arrival/";
    public static double locationX = 50;
    public static double locationY = 9;

    public static Response post(String url, RequestBody body) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return client.newCall(request).execute();
    }
}
