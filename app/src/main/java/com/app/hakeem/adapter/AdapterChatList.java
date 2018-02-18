package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.ChatMessage;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterChatList extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<ChatMessage> sideMenuItems;

    public AdapterChatList(Activity activity, ArrayList<ChatMessage> sideMenuItems) {
        this.activity = activity;
        this.sideMenuItems = sideMenuItems;
        mInflater = LayoutInflater.from(activity);

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
                    getItem(position).isReciver()? R.layout.item_msg_receiver:R.layout.item_msg_sender, parent, false);
      //  }

        TextView tvMessage = (TextView)convertView.findViewById(R.id.tvMsg);
       tvMessage.setText(getItem(position).getMessage());

        return convertView;
    }

    public ArrayList<ChatMessage> getList() {
        return sideMenuItems;
    }
}
