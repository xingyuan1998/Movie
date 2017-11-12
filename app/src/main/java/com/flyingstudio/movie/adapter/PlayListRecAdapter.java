package com.flyingstudio.movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingstudio.movie.R;
import com.flyingstudio.movie.bean.PlayList;
import com.flyingstudio.movie.ui.PlayActivity;

import java.util.List;

/**
 * Created y xingy on 2017-8-7.
 */

public class PlayListRecAdapter extends RecyclerView.Adapter<PlayListRecAdapter.MyViewHold> {
    private List<PlayList>lists;
    private Context context;
    public PlayListRecAdapter(List<PlayList>lists,Context context){
        this.lists=lists;
        this.context=context;

    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.playitem,parent,false);
        final MyViewHold viewHold=new MyViewHold(view);
        viewHold.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=viewHold.getAdapterPosition();
                PlayList list=lists.get(pos);
                Intent intent=new Intent(context,PlayActivity.class);
                intent.putExtra("url","http://bd-dy.com"+list.getUrl());
                intent.putExtra("title",list.getTitle());
                context.startActivity(intent);
            }
        });
        return viewHold;
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        PlayList list=lists.get(position);
        holder.textView.setText(list.getTitle());
        //holder.textView.setText("dddd");
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class MyViewHold extends RecyclerView.ViewHolder {
        private TextView textView;
        private View view;
        public MyViewHold(View itemView) {
            super(itemView);
            view=itemView;
            textView= (TextView) itemView.findViewById(R.id.title);
        }
    }
}
