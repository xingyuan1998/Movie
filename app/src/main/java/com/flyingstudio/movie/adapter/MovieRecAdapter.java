package com.flyingstudio.movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyingstudio.movie.R;
import com.flyingstudio.movie.bean.MovieBean;
import com.flyingstudio.movie.ui.MovieDetailActivity;

import java.util.List;

/**
 * Created by xingy on 2017-8-7.
 *
 */

public class MovieRecAdapter extends RecyclerView.Adapter<MovieRecAdapter.ViewHold> {
    private Context context;
    private List<MovieBean>movies;

    public MovieRecAdapter(Context context,List<MovieBean>movies){
        this.context=context;
        this.movies=movies;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        final ViewHold viewHold= new ViewHold(view);

        viewHold.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos=viewHold.getAdapterPosition();
                final MovieBean movie=movies.get(pos);
                Intent intent=new Intent(context,MovieDetailActivity.class);
                intent.putExtra("id",movie.getId());
                //Toast.makeText(context,movie.getId(),Toast.LENGTH_SHORT).show();
                intent.putExtra("title",movie.getTitle());
                String url="http://static.bd-dy.com/img/"+movie.getCoverPic()+"/w/400";
                intent.putExtra("url",url);
                context.startActivity(intent);
            }
        });
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        MovieBean movie=movies.get(position);
        String url="http://static.bd-dy.com/img/"+movie.getCoverPic()+"/w/400";
        Glide.with(context).load(url).override(300,300).centerCrop().into(holder.imageView);
        holder.textView.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;
        private View view;
        public ViewHold(View itemView) {
            super(itemView);
            this.view=itemView;
            imageView= (ImageView) itemView.findViewById(R.id.image);
            textView= (TextView) itemView.findViewById(R.id.text);
        }
    }
}
