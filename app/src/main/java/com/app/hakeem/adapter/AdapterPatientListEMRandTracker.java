package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.Child;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterPatientListEMRandTracker extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<Child> children;

    public AdapterPatientListEMRandTracker(Activity activity, ArrayList<Child> child) {
        this.activity = activity;
        this.children = child;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public Child getItem(int position) {
        return children.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_dependent, parent, false);
        }


        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvName.setText(getItem(position).getName());
        tvName.setTextColor(activity.getResources().getColor(R.color.blue));

        Button btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
        btnDelete.setEnabled(false);
        btnDelete.setText("");
        btnDelete.setBackgroundResource(R.drawable.liked);

        return convertView;
    }


}
