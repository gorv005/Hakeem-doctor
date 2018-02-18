package com.app.hakeem.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.interfaces.DependentDelete;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.Child;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aditya.singh on 6/14/2016.
 */
public class AdapterPatientList extends BaseAdapter {


    private final LayoutInflater mInflater;
    private final DependentDelete dependentDelete;
    private Activity activity;
    private ArrayList<Child> children;
    private Dialog progressDialog;

    public AdapterPatientList(DependentDelete dependentDelete, Activity activity, ArrayList<Child> child) {
        this.activity = activity;
        this.children = child;
        this.dependentDelete = dependentDelete;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public Child getItem(int position) {
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
                    R.layout.item_dependent, parent, false);
        }


        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvName.setText(getItem(position).getName());
        tvName.setTextColor(activity.getResources().getColor(R.color.blue));

        Button btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDependent(position);
            }
        });


        return convertView;
    }

    public List<Child> getAllItem() {
        return children;
    }

    public void addItem(Child child) {

        this.children.add(child);
        this.notifyDataSetChanged();
    }


    private void deleteDependent(final int position) {

        progressDialog = Util.getProgressDialog(activity, R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("dependent_id", getItem(position).getChildId());
        hashMap.put("user_id", SharedPreference.getInstance(activity).getUser(C.LOGIN_USER).getUserId());
        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyService volleyService = new VolleyService(activity);
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                progressDialog.dismiss();

                Response responseServer = gson.fromJson(response.toString(), Response.class);


                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {

                 //   Util.showToast(activity, , false);
                    Util.showAlertForToast(activity,responseServer.getMessage(),responseServer.getMessage(),activity.getString(R.string.ok),R.drawable.warning,false);

                    dependentDelete.notifyDependentDeleted();

                } else {
              //      Util.showToast(activity, responseServer.getMessage(), false);
                    Util.showAlertForToast(activity,responseServer.getMessage(),responseServer.getMessage(),activity.getString(R.string.ok),R.drawable.warning,false);

                }

            }

            @Override
            public void notifyError(String requestType, String error) {
                progressDialog.dismiss();
                Util.showToast(activity, R.string.network_error, false);

            }
        }, "callback", C.API_DELETE_DPENDENT, Util.getHeader(activity), obj);


    }

}
