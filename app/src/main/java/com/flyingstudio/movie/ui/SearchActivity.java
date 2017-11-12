package com.flyingstudio.movie.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.flyingstudio.movie.R;
import com.flyingstudio.movie.adapter.MovieRecAdapter;
import com.flyingstudio.movie.bean.MovieBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<MovieBean>movies=new ArrayList<>();
    private MovieRecAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView= (SearchView) findViewById(R.id.search);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new GetData().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private class GetData extends AsyncTask<String,Integer,String>{
        private String data;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client=new OkHttpClient();
                RequestBody requestBody=new FormBody.Builder()
                        .add("q",strings[0])
                        .add("curPage","1")
                        .build();
                Request request=new Request.Builder()
                        .url("http://bd-dy.com/app/search")
                        .post(requestBody)
                        .build();
                Response response=client.newCall(request).execute();
                data=response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("search", "onPostExecute: "+s);
            if (TextUtils.isEmpty(s)){
                Toast.makeText(SearchActivity.this,"网络有错",Toast.LENGTH_SHORT);
            }else {
                Gson gson=new Gson();
                String jsonData;
                try {
                    JSONObject jsonObject =new JSONObject(s);
                    jsonData=jsonObject.getString("content");
                    movies=gson.fromJson(jsonData,new TypeToken<List<MovieBean>>(){}.getType());
                    if (movies.size()==0){
                        Toast.makeText(SearchActivity.this,"找不到该数据啊，换个行不行",Toast.LENGTH_LONG).show();
                    }
                    MovieRecAdapter adapter=new MovieRecAdapter(SearchActivity.this,movies);
                    recyclerView.setAdapter(adapter);
                    GridLayoutManager layoutManager=new GridLayoutManager(SearchActivity.this,3,GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutManager);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }



}
