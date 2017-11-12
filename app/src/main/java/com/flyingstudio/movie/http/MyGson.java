package com.flyingstudio.movie.http;

import com.flyingstudio.movie.bean.MovieBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xingy on 2017-8-7.
 */

public class MyGson {
     public static boolean jiexi(String s, List<MovieBean> movies){
        JSONObject jsonObject=null;
        Gson gson=new Gson();
        String jsonData=null;
        try {
            jsonObject=new JSONObject(s);
            jsonData=jsonObject.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (movies==null||movies.size()==0){
            movies = gson.fromJson(jsonData,new TypeToken<List<MovieBean>>(){}.getType());
            return true;
        }else {
            List<MovieBean> more = gson.fromJson(jsonData, new TypeToken<List<MovieBean>>() {
            }.getType());
            movies.addAll(more);
            return false;
        }
    }

}
