package com.app.hakeem.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

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
        viewHolder.ivDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDailogForImage(getItem(position).getUserPic());
            }
        });
        viewHolder.ivMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDailogForImage(getItem(position).getUrl());
            }
        });
        viewHolder.tvLiked.setText(post.getTotalLikes() + "");
        if(post.getIsLiked()==1) {
            viewHolder.tvLiked.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.liked, 0);
        }

        viewHolder.tvComment.setText(post.getTotal_comments() + "");
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

    void getDailogForImage(String url) {
        try {


            final Dialog dialog = new Dialog(activity);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //tell the Dialog to use the dialog.xml as it's layout description
            dialog.setContentView(R.layout.image_zoom_layout);
            // dialog.setTitle("Android Custom Dialog Box");
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
;
            ImageView imageView = (ImageView) dialog.findViewById(R.id.imageViewZoom);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            imageLoader.DisplayImage(url,imageView);
            dialog.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
