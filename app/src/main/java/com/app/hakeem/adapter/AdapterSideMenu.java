package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.SideMenuItem;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterSideMenu extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<SideMenuItem> sideMenuItems;

    public AdapterSideMenu(Activity activity, ArrayList<SideMenuItem> sideMenuItems) {
        this.activity = activity;
        this.sideMenuItems = sideMenuItems;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return sideMenuItems.size();
    }

    @Override
    public SideMenuItem getItem(int position) {
        return sideMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = mInflater.inflate(
                    R.layout.item_menu_queue, parent, false);
        }

        TextView tvTotalPatient = (TextView) convertView.findViewById(R.id.tvTotalPatient);
        if (getItem(position).getNameResourse() != R.string.queue)
            tvTotalPatient.setVisibility(View.GONE);
        else {
            tvTotalPatient.setVisibility(View.VISIBLE);
            tvTotalPatient.setText(getItem(position).getVal());
        }
        TextView tvMenuName = (TextView) convertView.findViewById(R.id.tvMenuName);
        tvMenuName.setText(activity.getResources().getString(getItem(position).getNameResourse()));
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
        ivIcon.setImageResource((getItem(position).getImageNameResource()));

        return convertView;
    }

    void setTotalPatient(String val) {

    }
}
