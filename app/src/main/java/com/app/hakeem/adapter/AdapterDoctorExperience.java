package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.Experience;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterDoctorExperience extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<Experience> children;

    public AdapterDoctorExperience(Activity activity, ArrayList<Experience> child) {
        this.activity = activity;
        this.children = child;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public Experience getItem(int position) {
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
                    R.layout.header_item_experiance, parent, false);
        }


        LinearLayout llMain = (LinearLayout) convertView.findViewById(R.id.linearLayout);
        TextView tvSrNo = (TextView)convertView.findViewById(R.id.tvSrNo);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvGender = (TextView)convertView.findViewById(R.id.tvGender);
        TextView tvBirthDay = (TextView)convertView.findViewById(R.id.tvBirthDay);

        llMain.setBackgroundColor(activity.getResources().getColor(R.color.white));

        tvSrNo.setText((position+1)+"");
        tvSrNo.setTextColor(activity.getResources().getColor(R.color.blue));
        tvName.setText(getItem(position).getHospitalName());
        tvName.setTextColor(activity.getResources().getColor(R.color.blue));
        tvGender.setText(getItem(position).getWorkedSince());
        tvGender.setTextColor(activity.getResources().getColor(R.color.blue));
        tvBirthDay.setText(getItem(position).getResignedSince());
        tvBirthDay.setTextColor(activity.getResources().getColor(R.color.blue));

        return convertView;
    }

    public List<Experience> getAllItem() {
        return children;
    }

   public void addItem(Experience child) {

        this.children.add(child);
        this.notifyDataSetChanged();
    }
}
