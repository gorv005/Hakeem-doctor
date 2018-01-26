package com.app.hakeem.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.City;

import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterCityList extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private List<City> cities;

    public AdapterCityList(Activity activity, List<City> cities) {
        this.activity = activity;
        this.cities = cities;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public City getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_city, parent, false);
        }

        TextView tvMenuName = (TextView)convertView.findViewById(R.id.tvCity);
        tvMenuName.setText(getItem(position).getName());
        return convertView;
    }
}
