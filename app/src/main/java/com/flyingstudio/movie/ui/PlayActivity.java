package com.flyingstudio.movie.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.flyingstudio.movie.R;
import com.flyingstudio.movie.http.Myokhttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class PlayActivity extends AppCompatActivity {
    private String url;
    private String all_url;
    private String title;
    private JCVideoPlayerStandard jcVideoPlayerStandard;
    private String playUrl=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
//        jcVideoPlayerStandard= (JCVideoPlayerStandard) findViewById(R.id.jcVideo);
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        url=intent.getStringExtra("url");
//        String[]aa=url.split("\\|");
//        all_url=aa[2]+"/"+title+".mp4";
//        Log.d("url", "onCreate: "+ Uri.parse(all_url));
//        Toast.makeText(PlayActivity.this,all_url,Toast.LENGTH_SHORT).show();
        jcVideoPlayerStandard= (JCVideoPlayerStandard) findViewById(R.id.videoplayer);


        new GetUrl().execute(url);
        FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(playUrl));
                //指定下载路径和下载文件名
                //request.setDestinationInExternalPublicDir("/download/", title+".mp4");
                request.setDestinationInExternalFilesDir(PlayActivity.this, Environment.DIRECTORY_DOWNLOADS,title+".mp4");
                //获取下载管理
                DownloadManager downloadManager= (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                //将下载任务加入下载队列，否则不会进行下载
                downloadManager.enqueue(request);
            }
        });

    }

    public class GetUrl extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ddddddd", "onPreExecute: ");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("string", "doInBackground: "+strings[0]);
            return Myokhttp.GetData(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("nishishei", "onPostExecute: "+s);
            if (TextUtils.isEmpty(s)){
                int a=1;
            }else {
                Document document= Jsoup.parse(s);
                Element source=document.select("source").first();
                playUrl=source.attr("src");
                Log.d("html", "onPostExecute11: "+playUrl);
                //player.setUp(playUrl, true , null, "这是title");
                jcVideoPlayerStandard.setUp(playUrl,JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,title);

            }

        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


}
