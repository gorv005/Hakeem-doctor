package com.app.hakeem.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.ActivityMain;
import com.app.hakeem.R;
import com.app.hakeem.pojo.Post;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.SharedPreference;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterPosts extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<Post> Posts;
    private ArrayList<Post> arraylist;

    ImageLoader imageLoader;

    public AdapterPosts(Activity activity, ArrayList<Post> Posts) {
        this.activity = activity;
        this.Posts = Posts;
        mInflater = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(activity);
        this.arraylist = new ArrayList<Post>();
        this.arraylist.addAll(Posts);
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
        viewHolder.tvDoctorName.setText(activity.getString(R.string.dr)+post.getPostBy());
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
        viewHolder.tvLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityMain) activity).likePost(position);
            }
        });
        viewHolder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPreference.getInstance(activity).getBoolean(C.IS_LOGIN)) {
                    Intent intent = new Intent(activity, ActivityContainer.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(C.POST, getItem(position));
                    intent.putExtra(C.BUNDLE, bundle);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_COMMENTS);
                    activity.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(activity, ActivityContainer.class);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_LOGIN);
                    activity.startActivity(intent);
                }
            }
        });
        viewHolder.tvLiked.setText(post.getTotalLikes() + "");
        if(post.getIsLiked()==1) {
            viewHolder.tvLiked.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.liked, 0);
        }
        else {
            viewHolder.tvLiked.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.like, 0);

        }

        viewHolder.tvComment.setText(post.getTotal_comments() + "");
        viewHolder.tvShare.setText("0");
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

  public Post getPost(int pos){
        return getItem(pos);
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
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Posts.clear();
        if (charText.length() == 0) {
            Posts.addAll(arraylist);
        }
        else
        {
            for (Post wp : arraylist)
            {
                if (wp.getSpecialist().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    Posts.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
