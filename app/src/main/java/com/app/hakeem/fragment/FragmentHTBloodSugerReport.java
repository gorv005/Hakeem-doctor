package com.app.hakeem.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.HTBloodSugerReportData;
import com.app.hakeem.pojo.HTBloodSugerReportList;
import com.app.hakeem.pojo.HTWeightReportList;
import com.app.hakeem.pojo.ResponsePDF;
import com.app.hakeem.util.C;
import com.app.hakeem.util.DownloadPdf;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHTBloodSugerReport extends Fragment {

    private LineChart mChart;
    private Dialog progressDialog;
    String dependentId,patientId;
    /*@BindView(R.id.tvNoData)
    TextView tvNoData;*/
    @BindView(R.id.etFrom)
    EditText etFrom;
    @BindView(R.id.etTo)
    EditText etTo;
    @BindView(R.id.btnRefresh)
    Button btnRefresh;
    @BindView(R.id.ivAddBloodSuger)
    ImageView ivAddBloodSuger;
    @BindView(R.id.ivDownloadPdf)
    ImageView ivDownloadPdf;
    @BindView(R.id.etTiming)
    EditText etTiming;

    @BindView(R.id.spinnerTiming)
    Spinner spinnerTiming;
    private boolean isFrom=false;
    AlertDialog dialogAddBloodSuger;
    String timingValue="Pre-meal",readingValue="101-111";
    LineDataSet ds1;
    LineDataSet ds2;
    LineDataSet ds3;
    List<HTBloodSugerReportData> htBloodSugerReportData;
    String mTo,mFrom;
    public FragmentHTBloodSugerReport() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_htblood_suger_report, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mChart = (LineChart) view.findViewById(R.id.lineChart1);
        etTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerTiming.performClick();
            }
        });
        spinnerTiming.setOnItemSelectedListener(mspinnerTimingSelectListner);
        String[] timing = new String[]{
           /* "ALL",
            "PRE",
            "POST",
            "SLEEP"*/
                getActivity().getString(R.string.all),
                getActivity().getString(R.string.pre),
                getActivity().getString(R.string.post),
                getActivity().getString(R.string.sleep)

        };

        final List<String> specialityList = new ArrayList<>(Arrays.asList(timing));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,specialityList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return true;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiming.setAdapter(spinnerArrayAdapter);


        etFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom=true;
                openCalender();
            }
        });
        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom=false;
                openCalender();
            }
        });
        if(SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserType().equals(C.DOCTOR)) {
            ivAddBloodSuger.setVisibility(View.GONE);
        }
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBloodPressureReport();
            }
        });
        mTo=Util.getCurrentDate();
        etFrom.setText(Util.get2MonthNextDateWithoutLocale(Util.getCurrentDateWithoutLocale()));
        mFrom=Util.get2MonthNextDate(Util.getCurrentDate());
        etTo.setText(Util.getCurrentDateWithoutLocale());
        getBloodPressureReport();
        ivAddBloodSuger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     openPopUpAddBloodSuger();
            }
        });
        ivDownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReportPDF();
            }
        });
    }

    private void getReportPDF() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("patient_id", patientId);

        hashMap.put("dependent_id", dependentId);
        hashMap.put("report", "1");


        hashMap.put("from", mFrom);
        hashMap.put("to", mTo);
        hashMap.put("weight",  ((ActivityContainer)getActivity()).getWeight());
        hashMap.put("height", ((ActivityContainer)getActivity()).getHeight());

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
                progressDialog.dismiss();
                ResponsePDF responsePDF = gson.fromJson(response.toString(), ResponsePDF.class);
                if (responsePDF.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    new DownloadPdf(getActivity(),responsePDF.getDownloadUrl(),"Blood_Suger_report_"+Util.getCurrentTimeStamp()+".pdf");

                } else {
                    //Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.alert),responsePDF.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                // Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.network_error),getString(R.string.ok),R.drawable.error,false);

            }
        }, "callback", C.API_GET_REPORT_PDF, Util.getHeader(getActivity()), obj);


    }

    AdapterView.OnItemSelectedListener mspinnerTimingSelectListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(ds1!=null && ds2!=null && ds3!=null) {
                if (position == 0) {
                    etTiming.setText(getString(R.string.all));

                    initGraph(0,getMax(htBloodSugerReportData),htBloodSugerReportData,getResources().getString(R.string.all));

                } else if (position == 1) {
                    etTiming.setText(getString(R.string.pre));

                    initGraph(0,getMax(htBloodSugerReportData),htBloodSugerReportData,getResources().getString(R.string.pre));

                } else if (position == 2) {
                    etTiming.setText(getString(R.string.post));

                    initGraph(0,getMax(htBloodSugerReportData),htBloodSugerReportData,getResources().getString(R.string.post));

                } else if (position == 3) {
                    etTiming.setText(getString(R.string.sleep));

                    initGraph(0,getMax(htBloodSugerReportData),htBloodSugerReportData,getResources().getString(R.string.sleep));

                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    void initGraph(float min,float max,List<HTBloodSugerReportData> inputArray,String selected){
        Log.e("MIn="+min,"MAx="+max);
        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.setData(generateLineData(inputArray,selected));
        mChart.animateX(2000);
        //   Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");

       /* Legend l = mChart.getLegend();
        l.setTypeface(tf);*/

        YAxis leftAxis = mChart.getAxisLeft();
        //  leftAxis.setTypeface(tf);
        leftAxis.setAxisMaximum(max);
        leftAxis.setLabelCount(6);

        leftAxis.setAxisMinimum(0);
        mChart.getAxisRight().setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
    }



    private void getBloodPressureReport() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("patient_id", patientId);


            hashMap.put("dependent_id", dependentId);


        hashMap.put("from", mFrom);
        hashMap.put("to", mTo);

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
                progressDialog.dismiss();
                HTBloodSugerReportList responseServer = gson.fromJson(response.toString(), HTBloodSugerReportList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    if(responseServer.getData()!=null && responseServer.getData().size()>0) {
                       htBloodSugerReportData= responseServer.getData();
                        initGraph(getMin(responseServer.getData()),getMax(responseServer.getData()),htBloodSugerReportData,getResources().getString(R.string.all));
                    }
                    else {
                        Util.showAlertForToast(getActivity(),getString(R.string.alert),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                    }

                } else {
                    //Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.alert),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                // Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.network_error),getString(R.string.ok),R.drawable.warning,false);

            }
        }, "callback", C.API_GET_BLOOD_SUGER_REPORT, Util.getHeader(getActivity()), obj);


    }
    protected LineData generateLineData(List<HTBloodSugerReportData> htFeverReportData,String selected) {
        List<Entry> entriesPre = new ArrayList<Entry>();
        List<Entry> entriesPost = new ArrayList<Entry>();
        List<Entry> entriesSleep = new ArrayList<Entry>();

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();

            entriesPre.add(new Entry(Float.parseFloat("0"), 0));
            entriesPost.add(new Entry(Float.parseFloat("0"),0));
            entriesSleep.add(new Entry(Float.parseFloat("0"),0));


        for(int i=0;i<htFeverReportData.size();i++) {
            if(htFeverReportData.get(i).getTiming().equals(C.PRE_MEAL)) {
                String a[]=htFeverReportData.get(i).getReading().split("-");
                float m = (Float.parseFloat(a[0])+Float.parseFloat(a[1]))/2;
                entriesPre.add(new Entry(Float.parseFloat("" + i + 1),m));
            }
            else  if(htFeverReportData.get(i).getTiming().equals(C.POST_SLEEP)) {
                String a[]=htFeverReportData.get(i).getReading().split("-");
                float m = (Float.parseFloat(a[0])+Float.parseFloat(a[1]))/2;
                entriesPost.add(new Entry(Float.parseFloat("" + i + 1), m));
            }
            else {
                String a[]=htFeverReportData.get(i).getReading().split("-");
                float m = (Float.parseFloat(a[0])+Float.parseFloat(a[1]))/2;
                entriesSleep.add(new Entry(Float.parseFloat("" + i + 1), m));
            }
        }



         ds1 = new LineDataSet(entriesPre, getString(R.string.pre));
         ds2 = new LineDataSet(entriesPost, getString(R.string.post));
         ds3 = new LineDataSet(entriesSleep, getString(R.string.sleep));
        Log.e("DEBUG","dkkd");
        ds1.setLineWidth(2f);
        ds2.setLineWidth(2f);
        ds3.setLineWidth(2f);

        ds1.setDrawValues(false);
        ds2.setDrawValues(false);
        ds3.setDrawValues(false);

        ds1.setDrawCircles(false);
        ds2.setDrawCircles(false);
        ds3.setDrawCircles(false);

        ds1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ds2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ds3.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ds1.setColor(ContextCompat.getColor(getActivity(), R.color.green_dark));
        ds2.setColor(ContextCompat.getColor(getActivity(), R.color.orange));
        ds3.setColor(ContextCompat.getColor(getActivity(), R.color.red_text));

        // load DataSets from textfiles in assets folders
        if(selected.equals(getResources().getString(R.string.pre))){
            sets.add(ds1);

        }
        else  if(selected.equals(getResources().getString(R.string.post))){

            sets.add(ds2);
        }
        else  if(selected.equals(getResources().getString(R.string.sleep))){

            sets.add(ds3);
        }
        else  {
            sets.add(ds1);
            sets.add(ds2);
            sets.add(ds3);
        }


        LineData d = new LineData(sets);
        //  d.setValueTypeface(tf);
        return d;
    }
    Calendar myCalendar = Calendar.getInstance(Locale.US);


    private void openPopUpAddBloodSuger() {


        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_blood_suger, null);
        dialogAddBloodSuger = new AlertDialog.Builder(getActivity()).create();
        dialogAddBloodSuger.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddBloodSuger.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogAddBloodSuger.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAddBloodSuger.setView(deleteDialogView);
        dialogAddBloodSuger.setCancelable(true);


        final EditText   etTiming = (EditText) deleteDialogView.findViewById(R.id.etTiming);
        final EditText   etReading = (EditText) deleteDialogView.findViewById(R.id.etReading);
        final EditText etComment = (EditText) deleteDialogView.findViewById(R.id.etComment);

        final Spinner spinnerTiming = (Spinner) deleteDialogView.findViewById(R.id.spinnerTiming);
        final Spinner spinnerReading = (Spinner) deleteDialogView.findViewById(R.id.spinnerReading);

        Button  btnSubmit = (Button) deleteDialogView.findViewById(R.id.btnSubmit);

        String[] timing = new String[]{
                "Pre-meal",
                "Sleep",
                "Post-sleep"
        };
        final List<String> timingList = new ArrayList<>(Arrays.asList(timing));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,timingList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return true;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiming.setAdapter(spinnerArrayAdapter);

        final String[] reading = new String[]{
                "101-111",
                "111-121",
                "121-131",
                "131-141",
                "141-151",
                "151-161",
                "161-171",
                "171-181",
                "181-191",
                "191-201"
        };
        final List<String> readingList = new ArrayList<>(Arrays.asList(reading));
        final ArrayAdapter<String> spinnerArrayAdapterreading = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,readingList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return true;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapterreading.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReading.setAdapter(spinnerArrayAdapterreading);

        spinnerTiming.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timingValue=timingList.get(position);
                etTiming.setText(timingList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerReading.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                readingValue=readingList.get(position);
                etReading.setText(readingList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etComment.getText().toString().trim().length()==0){
                    addBloodSugereport("weight");
                }
                else {
                    addBloodSugereport(etComment.getText().toString());
                }

            }
        });


        etReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerReading.performClick();
            }
        });

        etTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerTiming.performClick();

            }
        });

        dialogAddBloodSuger.show();


    }


    private void addBloodSugereport(String comment) {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();


            hashMap.put("dependent_id", dependentId);


        hashMap.put("patient_id", patientId);
        hashMap.put("timing", timingValue);
        hashMap.put("reading", readingValue);
        hashMap.put("comment",comment);
        hashMap.put("date", Util.getCurrentDate());

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
                progressDialog.dismiss();
                HTWeightReportList responseServer = gson.fromJson(response.toString(), HTWeightReportList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    if(dialogAddBloodSuger!=null && dialogAddBloodSuger.isShowing()) {
                        dialogAddBloodSuger.dismiss();
                        getBloodPressureReport();
                    }
                    Util.showAlertForToast(getActivity(),getString(R.string.warning),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);
                } else {
                    //Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.error),responseServer.getMessage(),getString(R.string.ok),R.drawable.error,false);
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                // Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.network_error),getString(R.string.ok),R.drawable.error,false);

            }
        }, "callback", C.API_ADD_BLOOD_SUGER_REPORT, Util.getHeader(getActivity()), obj);


    }

    private void openCalender() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() );
        datePickerDialog.show();


    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = C.DATE_FORMAT_FOR_REPORT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat);

        if (isFrom) {
            mFrom=sdf.format(myCalendar.getTime());
            etFrom.setText(sdf1.format(myCalendar.getTime()));
        } else {
            mTo=sdf.format(myCalendar.getTime());
            etTo.setText(sdf1.format(myCalendar.getTime()));
        }


    }



    public  float getMax(List<HTBloodSugerReportData> inputArray){
        String a[]=inputArray.get(0).getReading().split("-");
        float maxValue = (Float.parseFloat(a[0])+Float.parseFloat(a[1]))/2;
        for(int i=1;i<inputArray.size();i++){
            String a1[]=inputArray.get(i).getReading().split("-");
            float m = (Float.parseFloat(a1[0])+Float.parseFloat(a1[1]))/2;
            if(m > maxValue){
                maxValue = m;
            }
        }
        float mm=maxValue/6;
        return maxValue+mm;
    }

    public  float getMin(List<HTBloodSugerReportData> inputArray){
      /*  String a[]=inputArray.get(0).getReading().split("-");
        float minValue = (Float.parseFloat(a[0])+Float.parseFloat(a[1]))/2;
        for(int i=1;i<inputArray.size();i++){
            String a1[]=inputArray.get(i).getReading().split("-");
            float m = (Float.parseFloat(a1[0])+Float.parseFloat(a1[1]))/2;
            if(m < minValue){
                minValue = m;
            }
        }
        if(inputArray.size()==1){
            return 0;
        }*/
        return 0;
    }
}
