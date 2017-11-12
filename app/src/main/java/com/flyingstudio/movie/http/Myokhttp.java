package com.flyingstudio.movie.http;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by xingy on 2017-8-7.
 */

public class Myokhttp {
    private static String data;
    public static String GetData(String url) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .build();
        try {
            data=client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
