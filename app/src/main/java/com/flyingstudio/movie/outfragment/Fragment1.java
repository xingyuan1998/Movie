package com.flyingstudio.movie.outfragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyingstudio.movie.R;
import com.flyingstudio.movie.adapter.FragmentAdapter;
import com.flyingstudio.movie.bean.Type;
import com.flyingstudio.movie.http.Myokhttp;
import com.flyingstudio.movie.infragment.MovieFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by xingy on 2017-8-7.
 */

public class Fragment1 extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Fragment> fragments=new ArrayList<>();
    private List<Type>types=new ArrayList<>();
    private FragmentAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.layout1,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        new GetCag().execute("http://bd-dy.com/app/categories");
    }

    private void initView() {
        viewPager= (ViewPager) getView().findViewById(R.id.view);

        tabLayout= (TabLayout) getView().findViewById(R.id.tabLayout);



    }

    private class GetCag extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... strings) {
            return Myokhttp.GetData(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("目录", "onPostExecute: "+s);
            if (TextUtils.isEmpty(s)){
                Toast.makeText(getContext(),"网络出粗了 ，检查一下网络啊！！！",Toast.LENGTH_SHORT).show();
            }else {

                    Gson gson =new Gson();
                    types=gson.fromJson(s,new TypeToken<List<Type>>(){}.getType());
                for (int i =0; i <types.size() ; i++) {
                    Type type = types.get(i);
                    fragments.add(new MovieFragment(type.getId()));
                }
                adapter=new FragmentAdapter(getChildFragmentManager(),fragments);
                viewPager.setOffscreenPageLimit(types.size());
                tabLayout.setTabMode(MODE_SCROLLABLE);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
                for (int i = 0; i <types.size() ; i++) {
                    tabLayout.getTabAt(i).setText(types.get(i).getName());
                }



            }
        }
    }

}
