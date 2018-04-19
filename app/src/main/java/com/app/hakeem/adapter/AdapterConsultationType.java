package com.app.hakeem.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.pojo.Awareness;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterConsultationType extends BaseAdapter {

    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<Awareness> awareness;
    ImageLoader imageLoader;

    public AdapterConsultationType(Activity activity, ArrayList<Awareness> awareness) {
        this.activity = activity;
        this.awareness = awareness;
        mInflater = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        return awareness.size();
    }

    @Override
    public Awareness getItem(int position) {
        return awareness.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_consultation_type, parent, false);
        }

        Button btnConsType = (Button) convertView.findViewById(R.id.btnMedicalCons);
        int resid= activity.getResources().getIdentifier(getItem(position).getCategoryName().replaceAll(" ","_"), "string", activity.getPackageName());
        if(resid!=0) {
            btnConsType.setText(activity.getResources().getString(resid));
        }
        else {
            btnConsType.setText(getItem(position).getCategoryName());

        }
        btnConsType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(C.SPECIALITY, getItem(position).getCategoryId());
                ((ActivityContainer) activity).fragmnetLoader(C.FRAGMENT_SELECT_PATIENT_TO_QUEUE, bundle);
            }
        });
        imageLoader.DisplayImage(getItem(position).getIconUrl(), (ImageView) convertView.findViewById(R.id.ivIcon));
        ((ImageView) convertView.findViewById(R.id.ivIcon)).bringToFront();

        return convertView;
    }
}
