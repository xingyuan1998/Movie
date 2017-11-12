package com.flyingstudio.movie.http;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.flyingstudio.movie.adapter.MovieRecAdapter;
import com.flyingstudio.movie.bean.MovieBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by xingy on 2017-8-7.
 */

public  class MovieTask extends AsyncTask<String,Void,String> {
    private List<MovieBean>movies;
    private Context context;
    private MovieRecAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    public MovieTask(SwipeRefreshLayout swipeRefreshLayout,RecyclerView recyclerView, List<MovieBean>movies, Context context, MovieRecAdapter adapter){
        this.movies=movies;
        this.context=context;
        this.adapter=adapter;
        this.recyclerView=recyclerView;
        this.swipeRefreshLayout=swipeRefreshLayout;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected String doInBackground(String... strings) {
        return Myokhttp.GetData(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute: "+s);
        if (TextUtils.isEmpty(s)){
            Toast.makeText(context,"网路出了一点小问题", Toast.LENGTH_SHORT).show();
        }else {
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
                movies=gson.fromJson(jsonData,new TypeToken<List<MovieBean>>(){}.getType());
            }else {
                List<MovieBean>more=gson.fromJson(jsonData,new TypeToken<List<MovieBean>>(){}.getType());
                movies.addAll(more);
            }
            if (adapter==null){
                adapter= new MovieRecAdapter(context,movies);
                recyclerView.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
            swipeRefreshLayout.setRefreshing(false);
        }

    }
}
