package com.flyingstudio.movie.infragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyingstudio.movie.R;
import com.flyingstudio.movie.adapter.MovieRecAdapter;
import com.flyingstudio.movie.bean.MovieBean;
import com.flyingstudio.movie.http.Myokhttp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by xingy on 2017-9-21.
 */

public class MovieFragment extends Fragment {
    private int type;
    private int page;
    public int lastVisibleItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private MovieRecAdapter adapter;
    private List<MovieBean>movies=new ArrayList<>();
    public MovieFragment(int type){
        this.type=type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView()==null){
            View view=inflater.inflate(R.layout.movie_list,container,false);
            return view;
        }else return getView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setListen();
        new GetData().execute("http://bd-dy.com/app/movies?cid="+type+"&pageNum=1");
        swipeRefreshLayout.setRefreshing(false);

    }

    private void setListen() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +2>=layoutManager.getItemCount()) {
                    new GetData().execute("http://bd-dy.com/app/movies?cid="+type+"&pageNum="+(++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem =layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void initView() {
        swipeRefreshLayout= (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        layoutManager=new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setRefreshing(false);
    }

    private class GetData extends AsyncTask<String,Integer,String>{
        String data=null;
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
                Toast.makeText(getContext(),"我居然没获取数据，fuck",Toast.LENGTH_LONG).show();
            }else {
                Gson gson=new Gson();
                try {
                    JSONObject jsonObject =new JSONObject(s);
                    data=jsonObject.getString("content");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (movies==null||movies.size()==0) {
                    movies = gson.fromJson(data, new TypeToken<List<MovieBean>>() {
                    }.getType());
                }else {
                    List<MovieBean>more=gson.fromJson(data,new TypeToken<List<MovieBean>>(){}.getType());
                    movies.addAll(more);
                }
                if (adapter==null){
                    adapter=new MovieRecAdapter(getContext(),movies);
                    recyclerView.setAdapter(adapter);
                }else adapter.notifyDataSetChanged();


//                boolean a =MyGson.jiexi(s,movies);
//                if (adapter==null){
//                    adapter=new MovieRecAdapter(getContext(),movies);
//                }
//                recyclerView.setAdapter(adapter);
//                if (!a)adapter.notifyDataSetChanged();
            }
            swipeRefreshLayout.setRefreshing(false);

        }
    }





}
