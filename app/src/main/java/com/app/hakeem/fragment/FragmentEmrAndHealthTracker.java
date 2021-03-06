package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.HTWeightReportList;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEmrAndHealthTracker extends Fragment {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDOB)
    TextView tvDOB;
    @BindView(R.id.tvWeight)
    TextView tvWeight;
    @BindView(R.id.tvHeight)
    TextView tvHeight;
    @BindView(R.id.tvChronic)
    TextView tvChronic;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.rbEMR)
    RadioButton rbEmr;
    @BindView(R.id.rbHeathTracker)
    RadioButton rbHeathTracker;
    private Fragment fragment;
    private int fragmentAction;
    String dependentId,patientId ;
    public FragmentEmrAndHealthTracker() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

            dependentId =  bundle.getString(C.DEPENDENT_ID);
            patientId =  bundle.getString(C.PATIENT_ID);

        }
    }

   public void setValues(String hr, String weight){
        tvHeight.setText(hr);
        tvWeight.setText(weight);

   }


    public String getWeight(){
       return tvWeight.getText().toString();

    }
    public String getHeight(){
        return tvHeight.getText().toString();

    }


    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.emr);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emr_and_health_tracker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getWeightReport();
        rbEmr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

          /*      if(rbEmr.isChecked()) {
                      rbHeathTracker.setChecked(false);
                    rbHeathTracker.setTextColor(getActivity().getResources().getColor(R.color.blue));
                    rbHeathTracker.setBackgroundResource(R.drawable.button_deselect_blue);

                    rbEmr.setBackgroundResource(R.drawable.button_select_blue);
                    rbEmr.setTextColor(getActivity().getResources().getColor(R.color.white));
                    Bundle bundle = getArguments();
                    fragmentAction = getArguments().getInt(C.FRAGMENT_ACTION, C.FRAGMENT_EMR);
                    fragmnetLoader(fragmentAction, bundle);
                }*/
            }
        });

        rbHeathTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbHeathTracker.setChecked(true);

                rbEmr.setChecked(false);
                rbEmr.setBackgroundResource(R.drawable.button_deselect_blue);
                rbEmr.setTextColor(getActivity().getResources().getColor(R.color.blue));

                rbHeathTracker.setBackgroundResource(R.drawable.button_select_blue);
                rbHeathTracker.setTextColor(getActivity().getResources().getColor(R.color.white));
                Bundle bundle = getArguments();
                fragmentAction = getArguments().getInt(C.FRAGMENT_ACTION, C.FRAGMENT_TRACKER);
                fragmnetLoader(fragmentAction, bundle);



            }
        });

        rbEmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbEmr.setChecked(true);

                rbHeathTracker.setChecked(false);
                rbHeathTracker.setTextColor(getActivity().getResources().getColor(R.color.blue));
                rbHeathTracker.setBackgroundResource(R.drawable.button_deselect_blue);

                rbEmr.setBackgroundResource(R.drawable.button_select_blue);
                rbEmr.setTextColor(getActivity().getResources().getColor(R.color.white));
                Bundle bundle = getArguments();
                fragmentAction = getArguments().getInt(C.FRAGMENT_ACTION, C.FRAGMENT_EMR);
                fragmnetLoader(fragmentAction, bundle);
            }
        });
        rbHeathTracker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             /*   if(rbHeathTracker.isChecked()) {
                    rbEmr.setChecked(false);
                    rbEmr.setBackgroundResource(R.drawable.button_deselect_blue);
                    rbEmr.setTextColor(getActivity().getResources().getColor(R.color.blue));

                    rbHeathTracker.setBackgroundResource(R.drawable.button_select_blue);
                    rbHeathTracker.setTextColor(getActivity().getResources().getColor(R.color.white));
                    Bundle bundle = getArguments();
                    fragmentAction = getArguments().getInt(C.FRAGMENT_ACTION, C.FRAGMENT_TRACKER);
                    fragmnetLoader(fragmentAction, bundle);
                }*/
            }
        });


      //  rbHeathTracker.setChecked(false);
        rbHeathTracker.setTextColor(getActivity().getResources().getColor(R.color.blue));
        rbHeathTracker.setBackgroundResource(R.drawable.button_deselect_blue);

        rbEmr.setBackgroundResource(R.drawable.button_select_blue);
        rbEmr.setTextColor(getActivity().getResources().getColor(R.color.white));

        Bundle bundle = getArguments();
        fragmentAction = getArguments().getInt(C.FRAGMENT_ACTION, C.FRAGMENT_EMR);
        fragmnetLoader(fragmentAction, bundle);
        if(bundle.getString(C.NAME)==null ||bundle.getString(C.NAME).equals("")) {
            tvName.setText(SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getFirstName());

        }
        else {
            tvName.setText(bundle.getString(C.NAME));

        }
        tvGender.setText(bundle.getString(C.GENDER));
        tvDOB.setText(bundle.getString(C.DOB));

    }


    private void getWeightReport() {

 /*       progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();*/

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("dependent_id", dependentId);


        hashMap.put("patient_id", patientId);
        hashMap.put("from", Util.get2MonthNextDate(Util.getCurrentDate()));
        hashMap.put("to", Util.getCurrentDate());

        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyService volleyService = new VolleyService(getActivity());
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                HTWeightReportList responseServer = gson.fromJson(response.toString(), HTWeightReportList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    if(responseServer.getData()!=null && responseServer.getData().size()>0) {
                        setValues(responseServer.getData().get(responseServer.getData().size()-1).getHeight() +" "+getActivity().getString(R.string.cm),
                                responseServer.getData().get(responseServer.getData().size()-1).getWeight()+" "+getActivity().getString(R.string.kg));

                    }
                    else {
                        //   ((ActivityContainer)getActivity()).setValues("","");
                        Util.showAlertForToast(getActivity(),getString(R.string.alert),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                    }

                } else {
                    //  ((ActivityContainer)getActivity()).setValues("","");

                    //Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.alert),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e( "Response", error.toString());
               // progressDialog.dismiss();
                // Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.network_error),getString(R.string.ok),R.drawable.error,false);

            }
        }, "callback", C.API_GET_WEIGHT_REPORT, Util.getHeader(getActivity()), obj);


    }


    public void fragmnetLoader(int fragmentType, Bundle bundle) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmentType) {

            case C.FRAGMENT_EMR:
                ActivityContainer.tvTitle.setText(R.string.emr);
//                tvTitle.setText(R.string.emr);
                fragment = new FragmentEMR();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);
                break;
            case C.FRAGMENT_TRACKER:
                ActivityContainer.tvTitle.setText(R.string.health_tracker);

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHealthTracker();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);
                break;
            case C.FRAGMENT_TRACKER_WEIGHT_REPORT:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHTWeightReport();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_TRACKER_WEIGHT_REPORT);
                break;
            case C.FRAGMENT_TRACKER_FEVER_REPORT:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHTFeverReport();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_TRACKER_FEVER_REPORT);
                break;
            case C.FRAGMENT_TRACKER_BLOOD_PRESSURE_REPORT:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHTBloodPressureReport();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_TRACKER_BLOOD_PRESSURE_REPORT);
                break;
            case C.FRAGMENT_TRACKER_BLOOD_SUGAR_REPORT:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHTBloodSugerReport();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_TRACKER_BLOOD_SUGAR_REPORT);
                break;

        }
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
        getChildFragmentManager().executePendingTransactions();

    }


}
