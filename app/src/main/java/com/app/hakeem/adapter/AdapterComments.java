package com.app.hakeem.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.pojo.Comment;
import com.app.hakeem.util.ImageLoader;

import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterComments extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private List<Comment> Posts;
    ImageLoader imageLoader;

    public AdapterComments(Activity activity, List<Comment> Posts) {
        this.activity = activity;
        this.Posts = Posts;
        mInflater = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(activity);

    }

    @Override
    public int getCount() {
        return Posts.size();
    }

    @Override
    public Comment getItem(int position) {
        return Posts.get(position);
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
                    R.layout.item_comment, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tvDoctorName = (TextView) convertView.findViewById(R.id.tvDoctorName);
            viewHolder.ivDoctor = (ImageView) convertView.findViewById(R.id.ivDoctor);
            viewHolder.ivLine1 = (ImageView) convertView.findViewById(R.id.line1);
            viewHolder.ivLine2 = (ImageView) convertView.findViewById(R.id.line2);
            viewHolder.ivSpecialityIcon = (ImageView) convertView.findViewById(R.id.ivSpeciality);
            viewHolder.tvMsg = (TextView) convertView.findViewById(R.id.tvMsg);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0)
            viewHolder.ivLine1.setVisibility(View.VISIBLE);
        if (position == getCount() - 1)
            viewHolder.ivLine2.setVisibility(View.GONE);

        viewHolder.tvDoctorName.setText(getItem(position).getCommentBy());
        viewHolder.tvMsg.setText(getItem(position).getComment());
        imageLoader.DisplayImage(getItem(position).getProfile_pic(),viewHolder.ivDoctor);
        if(getItem(position).getSpecialization()!=null
                &&getItem(position).getSpecialization().equals(activity.getString(R.string.Abdominal_Clinic))){
                viewHolder.ivSpecialityIcon.setImageResource(R.drawable.sec_abdomen);

        }
        else if(getItem(position).getSpecialization()!=null
                &&getItem(position).getSpecialization().equals(activity.getString(R.string.Psychological_Clinic))){
            viewHolder.ivSpecialityIcon.setImageResource(R.drawable.sec_psy);

        }
        else if(getItem(position).getSpecialization()!=null
                &&getItem(position).getSpecialization().equals(activity.getString(R.string.Family_and_Community_Clinic))){
            viewHolder.ivSpecialityIcon.setImageResource(R.drawable.sec_family);

        }
        else if(getItem(position).getSpecialization()!=null
                &&getItem(position).getSpecialization().equals(activity.getString(R.string.Obgyne_Clinic))){
            viewHolder.ivSpecialityIcon.setImageResource(R.drawable.sec_obgyen);

        }
        else if(getItem(position).getSpecialization()!=null
                &&getItem(position).getSpecialization().equals(activity.getString(R.string.Pediatrics_Clinic))){
            viewHolder.ivSpecialityIcon.setImageResource(R.drawable.section_children);

        }
        else {
            viewHolder.ivSpecialityIcon.setVisibility(View.GONE);
        }
      /*  imageLoader.DisplayImage(post.getIconUrl(), viewHolder.ivSpecialityIcon);
        imageLoader.DisplayImage(post.getUserPic(), viewHolder.ivDoctor);*/

        return convertView;
    }


    class ViewHolder {

        TextView tvDoctorName;
        ImageView ivDoctor;
        ImageView ivLine1;
        ImageView ivLine2;
        ImageView ivSpecialityIcon;
        TextView tvMsg;
        ImageView ivMsg;
        TextView tvLiked;
        TextView tvShare;
        TextView tvComment;

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
