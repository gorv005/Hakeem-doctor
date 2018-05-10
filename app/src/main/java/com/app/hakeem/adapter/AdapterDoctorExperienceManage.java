package com.app.hakeem.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.pojo.ExperienceDoc;
import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;

import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterDoctorExperienceManage extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private List<ExperienceDoc> children;
    boolean isEdit;
    public AdapterDoctorExperienceManage(Activity activity, List<ExperienceDoc> child, boolean isEdit) {
        this.activity = activity;
        this.children = child;
        mInflater = LayoutInflater.from(activity);
        this.isEdit=isEdit;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_experience, parent, false);
        }


        LinearLayout llExperience = (LinearLayout) convertView.findViewById(R.id.llExperience);
        TextView tvHospitalnameName = (TextView)convertView.findViewById(R.id.tvHospitalnameName);
        TextView tvExperienceDate = (TextView)convertView.findViewById(R.id.tvExperienceDate);
        TextView tvExperienceDesc = (TextView)convertView.findViewById(R.id.tvExperienceDesc);
        ImageView ivDelete = (ImageView)convertView.findViewById(R.id.ivDelete);


        tvHospitalnameName.setText(getItem(position).getHospitalName());
        tvExperienceDate.setText(Util.getDateFromFormats(getItem(position).getWorkedSince(), C.DATE_FORMAT_FOR_REPORT,C.DATE_FORMAT).split("/")[2]+" - "+
                Util.getDateFromFormats(getItem(position).getResignedSince(), C.DATE_FORMAT_FOR_REPORT,C.DATE_FORMAT).split("/")[2]);
        tvExperienceDesc.setText(getItem(position).getDescription());
        if(isEdit) {
            llExperience.setBackgroundResource(R.drawable.edittext_deselect_blue);

            ivDelete.setVisibility(View.VISIBLE);
            llExperience.setClickable(true);
        }
        else {
            llExperience.setBackgroundResource(R.drawable.card_background_selector);
            ivDelete.setVisibility(View.GONE);
            llExperience.setClickable(false);
        }
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForConfirm(activity, activity.getString(R.string.delete), activity.getString(R.string.are_sure_you_want_to_delete), activity.getString(R.string.yes), activity.getString(R.string.no), R.drawable.warning, false,position);

            }
        });
        llExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit) {
                    ((ActivityContainer) activity).openPopUpToUpdateExperience(position);
                }
            }
        });
        return convertView;
    }

    public List<ExperienceDoc> getAllItem() {
        return children;
    }
    public void showAlertForConfirm(final Activity context, String title, String msg, String btnText1, String btnText2, int img, final boolean finishActivity, final int pos) {


        final LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_alert_with_two_button, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(deleteDialogView);


        TextView tvMsg = (TextView) deleteDialogView.findViewById(R.id.tvMsg);
        tvMsg.setText(msg);

        TextView tvTitle = (TextView) deleteDialogView.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        ImageView ivAlertImage = (ImageView) deleteDialogView.findViewById(R.id.ivAlertImage);
        ivAlertImage.setImageResource(img);
        Button btnDone = (Button) deleteDialogView.findViewById(R.id.btnDone);
        btnDone.setText(btnText1);
        Button btnCancel = (Button) deleteDialogView.findViewById(R.id.btnCancel);
        btnCancel.setText(btnText2);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                deleteExperience(pos);


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();


            }
        });

        dialog.show();


    }

    void deleteExperience(int pos){
        children.remove(pos);
        this.notifyDataSetChanged();
        ((ActivityContainer)activity).setListView();

    }
   public void addItem(ExperienceDoc child) {

        this.children.add(child);
        this.notifyDataSetChanged();
    }
    public void updateItem(int pos, ExperienceDoc child) {

        this.children.set(pos,child);
        this.notifyDataSetChanged();
    }
}
