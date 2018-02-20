package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.OnlineDoctor;
import com.app.hakeem.util.ImageLoader;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterConsultant extends BaseAdapter {

    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<OnlineDoctor> onlineDoctor;
    ImageLoader imageLoader;


    public AdapterConsultant(Activity activity, ArrayList<OnlineDoctor> onlineDoctor) {
        this.activity = activity;
        this.onlineDoctor = onlineDoctor;
        mInflater = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        return onlineDoctor.size();
    }

    @Override
    public OnlineDoctor getItem(int position) {
        return onlineDoctor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_consultant, parent, false);
        }

        TextView tvDoctorName = (TextView) convertView.findViewById(R.id.tvDoctorName);
        TextView tvSpeciality = (TextView) convertView.findViewById(R.id.tvDrSpeciality);
        TextView tvSubSpeciality = (TextView) convertView.findViewById(R.id.tvSubSpeciality);
        tvDoctorName.setText(getItem(position).getFirstName()+" ");
        tvSpeciality.setText(getItem(position).getClassification());
        tvSubSpeciality.setText(getItem(position).getSubSpecialist());
        imageLoader.DisplayImage(getItem(position).getPhoto(),(ImageView) convertView.findViewById(R.id.ivDoctor));
        return convertView;
    }
}
