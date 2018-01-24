package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.Child;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterDependent extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<Child> children;

    public AdapterDependent(Activity activity, ArrayList<Child> child) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.header_item_dependent, parent, false);
        }

        TextView tvSrNo = (TextView)convertView.findViewById(R.id.tvSrNo);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvGender = (TextView)convertView.findViewById(R.id.tvGender);
        TextView tvBirthDay = (TextView)convertView.findViewById(R.id.tvBirthDay);

        tvSrNo.setText(position+"");
        tvName.setText(getItem(position).getName());
        tvGender.setText(getItem(position).getGender());
        tvBirthDay.setText(getItem(position).getDob());

        return convertView;
    }

    public List<Child> getAllItem() {
        return children;
    }

   public void addItem(Child child) {

        this.children.add(child);
        this.notifyDataSetChanged();
    }
}
