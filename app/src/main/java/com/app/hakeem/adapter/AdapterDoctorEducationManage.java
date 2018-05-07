package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.Education;
import com.app.hakeem.pojo.Experience;
import com.app.hakeem.pojo.ExperienceDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterDoctorEducationManage extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private List<Education> children;

    public AdapterDoctorEducationManage(Activity activity, List<Education> child) {
        this.activity = activity;
        this.children = child;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public Education getItem(int position) {
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
                    R.layout.item_education, parent, false);
        }


        LinearLayout llEducation = (LinearLayout) convertView.findViewById(R.id.llEducation);
        ImageView ivDelete = (ImageView)convertView.findViewById(R.id.ivDelete);
        TextView tvUniversityName = (TextView)convertView.findViewById(R.id.tvUniversityName);
        TextView tvUniversityDesc = (TextView)convertView.findViewById(R.id.tvUniversityDesc);

       // llMain.setBackgroundColor(activity.getResources().getColor(R.color.white));

        tvUniversityName.setText(getItem(position).getUniversityName());
        tvUniversityDesc.setText(getItem(position).getDescription());

        return convertView;
    }

    public List<Education> getAllItem() {
        return children;
    }

   public void addItem(Education child) {

        this.children.add(child);
        this.notifyDataSetChanged();
    }
}
