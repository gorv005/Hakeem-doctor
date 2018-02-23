package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.DoctorPatientList;

import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterDoctorPatientsList extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private List<DoctorPatientList> children;

    public AdapterDoctorPatientsList( Activity activity, List<DoctorPatientList> child) {
        this.activity = activity;
        this.children = child;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public DoctorPatientList getItem(int position) {
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
                    R.layout.item_patients, parent, false);
        }


        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvName.setText(getItem(position).getFirstName());
        tvName.setTextColor(activity.getResources().getColor(R.color.blue));




        return convertView;
    }

    public List<DoctorPatientList> getAllItem() {
        return children;
    }

    public void addItem(DoctorPatientList child) {

        this.children.add(child);
        this.notifyDataSetChanged();
    }


}
