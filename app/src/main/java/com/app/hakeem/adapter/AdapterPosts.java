package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.Post;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterPosts extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<Post> Posts;
ImageLoader imageLoader;
    public AdapterPosts(Activity activity, ArrayList<Post> Posts) {
        this.activity = activity;
        this.Posts = Posts;
        mInflater = LayoutInflater.from(activity);
        imageLoader =new ImageLoader(activity);

    }

    @Override
    public int getCount() {
        return Posts.size();
    }

    @Override
    public Post getItem(int position) {
        return Posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_post, parent, false);
        }

        TextView tvDoctorName = (TextView)convertView.findViewById(R.id.tvDoctorName);
        ImageView ivDoctor = (ImageView) convertView.findViewById(R.id.ivDoctor);
        ImageView ivSpecialityIcon = (ImageView) convertView.findViewById(R.id.ivSpeciality);
        TextView tvMsg = (TextView)convertView.findViewById(R.id.tvMsg);
        ImageView ivMsg = (ImageView) convertView.findViewById(R.id.ivMsgImg);
        TextView tvLiked = (TextView)convertView.findViewById(R.id.tvLike);
        TextView tvShare = (TextView)convertView.findViewById(R.id.tvShare);
        TextView tvComment = (TextView)convertView.findViewById(R.id.tvComment);


        Post post =getItem(position);
        tvDoctorName.setText(post.getPostBy());
        if(C.PHOTO.equals(post.getType())) {
            tvMsg.setVisibility(View.GONE);
            ivMsg.setVisibility(View.VISIBLE);
            imageLoader.DisplayImage(post.getUrl(),ivMsg);

        }
        else if(C.VIDEO.equals(post.getType()))
        {
            tvMsg.setVisibility(View.GONE);
            ivMsg.setVisibility(View.VISIBLE);
            imageLoader.DisplayImage(post.getUrl(),ivMsg);
        }
        else if(C.TEXT.equals(post.getType()))
        {

            ivMsg.setVisibility(View.GONE);
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(post.getContent());
        }

        imageLoader.DisplayImage(post.getIconUrl(),ivSpecialityIcon);
        imageLoader.DisplayImage(post.getIconUrl(),ivDoctor);
        tvLiked.setText(post.getTotalLikes());
        tvComment.setText(post.getTotalLikes());
        tvShare.setText("5");
        return convertView;
    }
}
