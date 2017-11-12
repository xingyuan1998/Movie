package com.flyingstudio.movie.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyingstudio.movie.R;
import com.flyingstudio.movie.adapter.PlayListRecAdapter;
import com.flyingstudio.movie.bean.PlayList;
import com.flyingstudio.movie.http.Myokhttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView header_bg;
    private ImageView header_img;
    private TextView content;
    private String title;
    private String imgUrl;
    private int id;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PlayListRecAdapter adapter;
    private List<PlayList>lists=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initView();
        setFab();
        getData();
        setToolBar();
        setImgView();
        new GetList().execute("http://bd-dy.com/"+id+".htm");



    }

    private void initView() {
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager=new LinearLayoutManager(MovieDetailActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getData() {
        Intent intent=getIntent();
        imgUrl=intent.getStringExtra("url");
        title=intent.getStringExtra("title");
        id=intent.getIntExtra("id",12);
        setTitle(title);
    }

    private void setImgView() {

        header_bg= (ImageView) findViewById(R.id.header_bg);
        header_img= (ImageView) findViewById(R.id.header_img);

        Glide.with(this).load(imgUrl)
                .bitmapTransform(new BlurTransformation(this,14,3))
                .crossFade(500)
                .override(120,120)
                .into(header_bg);


        Glide.with(this).load(imgUrl)
                .crossFade(500)
                .into(header_img);

    }
    public class GetList extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return Myokhttp.GetData(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ddd", "onPostExecute: "+s);
            if (TextUtils.isEmpty(s)){
                Toast.makeText(MovieDetailActivity.this,"获取播放列表失败",Toast.LENGTH_SHORT).show();
            }else {
                Document document= Jsoup.parse(s);
                Element ul = document.select("ul.play-list").first();
                Log.d("ul", "onPostExecute: ");
                Elements lis = ul.select("li");
                for (int i = 0; i < lis.size(); i++) {
                    Element a = lis.get(i).select("a").first();
                    PlayList playList =new PlayList();
                    playList.setTitle(a.text());
                    playList.setUrl(a.attr("href"));
                    Log.d("href", "onPostExecute: "+playList.getUrl());
                    lists.add(playList);
                }
                adapter=new PlayListRecAdapter(lists,MovieDetailActivity.this);
                recyclerView.setAdapter(adapter);


//                JSONObject jsonObject=null;
//                String jsonData=null;
//                String jsonData2=null;
//                try {
//                    Gson gson=new Gson();
//                    jsonObject=new JSONObject(s);
//                    jsonData=jsonObject.getString("playUrls");
//                    Log.d("ddd", "onPostExecute: "+jsonData);
//                    lists=gson.fromJson(jsonData,new TypeToken<List<PlayList>>(){}.getType());
//                    adapter=new PlayListRecAdapter(lists,MovieDetailActivity.this);
//                    recyclerView.setAdapter(adapter);
//                    Log.d("ddd", "onPostExecute: ");
//                    layoutManager=new LinearLayoutManager(MovieDetailActivity.this,LinearLayoutManager.VERTICAL,false);
//                    recyclerView.setLayoutManager(layoutManager);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

        }
    }

}
