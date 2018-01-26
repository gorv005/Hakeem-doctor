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
        imageLoader = new ImageLoader(activity);

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

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_post, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tvDoctorName = (TextView) convertView.findViewById(R.id.tvDoctorName);
            viewHolder.ivDoctor = (ImageView) convertView.findViewById(R.id.ivDoctor);
            viewHolder.ivSpecialityIcon = (ImageView) convertView.findViewById(R.id.ivSpeciality);
            viewHolder.tvMsg = (TextView) convertView.findViewById(R.id.tvMsg);
            viewHolder.ivMsg = (ImageView) convertView.findViewById(R.id.ivMsgImg);
            viewHolder.tvLiked = (TextView) convertView.findViewById(R.id.tvLike);
            viewHolder.tvShare = (TextView) convertView.findViewById(R.id.tvShare);
            viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Post post = getItem(position);
        viewHolder.tvDoctorName.setText(post.getPostBy());
        if (C.PHOTO.equals(post.getType())) {
            viewHolder.tvMsg.setVisibility(View.GONE);
            viewHolder.ivMsg.setVisibility(View.VISIBLE);
            imageLoader.DisplayImage(post.getUrl(), viewHolder.ivMsg);

        } else if (C.VIDEO.equals(post.getType())) {
            viewHolder.tvMsg.setVisibility(View.GONE);
            viewHolder.ivMsg.setVisibility(View.VISIBLE);
            imageLoader.DisplayImage(post.getUrl(), viewHolder.ivMsg);
        } else if (C.TEXT.equals(post.getType())) {
            viewHolder.ivMsg.setVisibility(View.GONE);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
            viewHolder.tvMsg.setText(post.getContent());
        }

        imageLoader.DisplayImage(post.getIconUrl(), viewHolder.ivSpecialityIcon);
        imageLoader.DisplayImage(post.getUserPic(), viewHolder.ivDoctor);
        viewHolder.tvLiked.setText(post.getTotalLikes() + "");
        viewHolder.tvComment.setText(post.getTotalLikes() + "");
        viewHolder.tvShare.setText("5");
        return convertView;
    }


    class ViewHolder {

        TextView tvDoctorName;
        ImageView ivDoctor;
        ImageView ivSpecialityIcon;
        TextView tvMsg;
        ImageView ivMsg;
        TextView tvLiked;
        TextView tvShare;
        TextView tvComment;

    }
}
