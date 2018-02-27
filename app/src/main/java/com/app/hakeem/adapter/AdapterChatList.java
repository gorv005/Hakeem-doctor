package com.app.hakeem.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.ChatMessage;
import com.app.hakeem.pojo.MessageAttachment;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.Util;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterChatList extends BaseAdapter {


    private final LayoutInflater mInflater;
    private final ImageLoader imageLoader;
    private Activity activity;
    private ArrayList<ChatMessage> sideMenuItems;

    public AdapterChatList(Activity activity, ArrayList<ChatMessage> sideMenuItems) {
        this.activity = activity;
        this.sideMenuItems = sideMenuItems;
        mInflater = LayoutInflater.from(activity);

         imageLoader =new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        return sideMenuItems.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return sideMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       // if (convertView == null) {
            convertView = mInflater.inflate(
                    getItem(position).isDeliver().equals(C.TRUE)? R.layout.item_msg_receiver:R.layout.item_msg_sender, parent, false);
      //  }



        TextView tvMessage = (TextView)convertView.findViewById(R.id.tvMsg);
        ImageView iv = (ImageView) convertView.findViewById(R.id.ivMsg);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        tvTime.setText(Util.getMessageTimn(getItem(position).getId()));

        if(isJSONValid(getItem(position).getMessage())) {
            try {
                tvMessage.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                final MessageAttachment msg = gson.fromJson(getItem(position).getMessage(), MessageAttachment.class);
//                imageLoader.DisplayImage(msg.getUrl(), iv);
                Picasso.with(activity).load(msg.getUrl()).fit().centerCrop().into(iv);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showImage(msg.getUrl());
                    }
                });

            } catch (Exception e) {


            }
        }
        else {
            tvMessage.setText(getItem(position).getMessage());
            tvMessage.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
        }


        return convertView;
    }


    public void showImage(String imageUri) {
        Dialog builder = new Dialog(activity);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(activity);

        Picasso.with(activity).load(imageUri).fit().centerCrop().into(imageView);

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    public ArrayList<ChatMessage> getList() {
        return sideMenuItems;
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
