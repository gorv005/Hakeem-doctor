package com.app.hakeem.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.pojo.EMRDoctorData;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;

import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterEMRDoctorList extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private List<EMRDoctorData> EMRDoctorDataList;
    ImageLoader imageLoader;
    String patientId,depedentId;
    public AdapterEMRDoctorList(Activity activity, List<EMRDoctorData> EMRDoctorDataList,String patientId,String dependentId) {
        this.activity = activity;
        this.EMRDoctorDataList = EMRDoctorDataList;
        mInflater = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(activity);
        this.patientId=patientId;
        this.depedentId=dependentId;
    }

    @Override
    public int getCount() {
        return EMRDoctorDataList.size();
    }

    @Override
    public EMRDoctorData getItem(int position) {
        return EMRDoctorDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_emr, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvDocName = (TextView) convertView.findViewById(R.id.tvDocName);
            viewHolder.tvDocSpec = (TextView) convertView.findViewById(R.id.tvDocSpec);
            viewHolder.ivDoctor = (ImageView) convertView.findViewById(R.id.ivDoctor);
            viewHolder.ivDetails = (ImageView) convertView.findViewById(R.id.ivDetails);
            viewHolder.ivPrescription = (ImageView) convertView.findViewById(R.id.ivPrescription);
            viewHolder.ivFollowUp = (CheckBox) convertView.findViewById(R.id.cbfollowUp);
            viewHolder.rl = (View) convertView.findViewById(R.id.rl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            viewHolder.rl.setBackgroundColor(ContextCompat.getColor(activity, R.color.cream));
        }
        viewHolder.tvDocName.setText(activity.getString(R.string.dr) + " " + EMRDoctorDataList.get(position).getFirstName());
        viewHolder.tvDocSpec.setText(EMRDoctorDataList.get(position).getSubSpecialist());
        viewHolder.tvDate.setText(EMRDoctorDataList.get(position).getCreatedAt().split(" ")[0]);
        imageLoader.DisplayImage(EMRDoctorDataList.get(position).getPhoto(), viewHolder.ivDoctor);
        viewHolder.ivFollowUp.setChecked(EMRDoctorDataList.get(position).getIsFollow().equals("true") ? true : false);
        viewHolder.ivPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString(C.PATIENT_ID,patientId);
                bundle.putString(C.DEPENDENT_ID,depedentId);
                bundle.putString(C.CHAT_DOCTOR_ID,""+EMRDoctorDataList.get(position).getDoctorId());
                ((ActivityContainer)activity).fragmnetLoader(C.FRAGMENT_PRESCRIPTION,bundle);
            }
        });
        viewHolder.ivDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString(C.PATIENT_ID,patientId);
                bundle.putString(C.DEPENDENT_ID,depedentId);
                bundle.putString(C.CHAT_DOCTOR_ID,""+EMRDoctorDataList.get(position).getDoctorId());
                ((ActivityContainer)activity).fragmnetLoader(C.FRAGMENT_DIAGNOSIS,bundle);
            }
        });
//        viewHolder.ivFollowUp.setEnabled(false);
        return convertView;
    }


    class ViewHolder {

        TextView tvDate;
        ImageView ivDoctor;
        TextView tvDocName;
        TextView tvDocSpec;
        ImageView ivDetails;
        ImageView ivPrescription;
        CheckBox ivFollowUp;
        View rl;


    }

    void getDailogForImage(String url) {
        try {


            final Dialog dialog = new Dialog(activity);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //tell the Dialog to use the dialog.xml as it's layout description
            dialog.setContentView(R.layout.image_zoom_layout);
            // dialog.setTitle("Android Custom Dialog Box");
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            ;
            ImageView imageView = (ImageView) dialog.findViewById(R.id.imageViewZoom);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            imageLoader.DisplayImage(url, imageView);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
