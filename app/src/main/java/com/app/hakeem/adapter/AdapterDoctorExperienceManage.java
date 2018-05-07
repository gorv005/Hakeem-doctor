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
import com.app.hakeem.pojo.Experience;
import com.app.hakeem.pojo.ExperienceDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterDoctorExperienceManage extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private List<ExperienceDoc> children;

    public AdapterDoctorExperienceManage(Activity activity, List<ExperienceDoc> child) {
        this.activity = activity;
        this.children = child;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public ExperienceDoc getItem(int position) {
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
                    R.layout.item_experience, parent, false);
        }


        LinearLayout llExperience = (LinearLayout) convertView.findViewById(R.id.llExperience);
        TextView tvHospitalnameName = (TextView)convertView.findViewById(R.id.tvHospitalnameName);
        TextView tvExperienceDate = (TextView)convertView.findViewById(R.id.tvExperienceDate);
        TextView tvExperienceDesc = (TextView)convertView.findViewById(R.id.tvExperienceDesc);
        ImageView ivDelete = (ImageView)convertView.findViewById(R.id.ivDelete);

        //llMain.setBackgroundColor(activity.getResources().getColor(R.color.white));

        tvHospitalnameName.setText(getItem(position).getHospitalName());
        tvExperienceDate.setText(getItem(position).getWorkedSince());
        tvExperienceDesc.setTextColor(activity.getResources().getColor(R.color.blue));


        return convertView;
    }

    public List<ExperienceDoc> getAllItem() {
        return children;
    }

   public void addItem(ExperienceDoc child) {

        this.children.add(child);
        this.notifyDataSetChanged();
    }
}
