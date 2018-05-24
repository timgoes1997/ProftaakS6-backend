package com.github.fontys;

import okhttp3.*;

import java.io.IOException;
import java.util.Random;

public class BasicLocationTrackerTest {

    protected final Random random = new Random();
    protected final String url = Globals.BASE_URL + ":" + Globals.SERVER_PORT + Globals.API_PATH + "location/";
    protected static double locationX = 50;
    protected static double locationY = 9;

    protected Response post(String url, RequestBody body) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return client.newCall(request).execute();
    }
}
