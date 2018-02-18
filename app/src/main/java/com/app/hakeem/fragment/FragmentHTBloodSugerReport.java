package com.app.hakeem.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.HTBloodSugerReportData;
import com.app.hakeem.pojo.HTBloodSugerReportList;
import com.app.hakeem.util.C;
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
    private boolean isFrom=false;
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
        etTo.setText(Util.getCurrentDate());
        etFrom.setText(Util.get2MonthNextDate(Util.getCurrentDate()));
        getBloodPressureReport();
    }



    void initGraph(float min,float max,List<HTBloodSugerReportData> inputArray){
        Log.e("MIn="+min,"MAx="+max);
        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.setData(generateLineData(inputArray));
        mChart.animateX(2000);

        //   Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");

       /* Legend l = mChart.getLegend();
        l.setTypeface(tf);*/

        YAxis leftAxis = mChart.getAxisLeft();
        //  leftAxis.setTypeface(tf);
        leftAxis.setAxisMaximum(max);
        leftAxis.setLabelCount(6);

        leftAxis.setAxisMinimum(min);
        mChart.getAxisRight().setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
    }
    private void getBloodPressureReport() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("patient_id", patientId);
        if(patientId.equals(dependentId)){
            hashMap.put("dependent_id", "");
        }
        else {
            hashMap.put("dependent_id", dependentId);

        }
        hashMap.put("from", etFrom.getText().toString());
        hashMap.put("to", etTo.getText().toString());

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
                        initGraph(getMin(responseServer.getData()),getMax(responseServer.getData()),responseServer.getData());
                    }
                    else {
                        Util.showAlertForToast(getActivity(),getString(R.string.alert),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                    }

                } else {
                    //Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.error),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

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
    protected LineData generateLineData(List<HTBloodSugerReportData> htFeverReportData) {
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



        LineDataSet ds1 = new LineDataSet(entriesPre, getString(R.string.pre));
        LineDataSet ds2 = new LineDataSet(entriesPost, getString(R.string.post));
        LineDataSet ds3 = new LineDataSet(entriesSleep, getString(R.string.sleep));
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
        sets.add(ds1);
        sets.add(ds2);
        sets.add(ds3);

        LineData d = new LineData(sets);
        //  d.setValueTypeface(tf);
        return d;
    }
    Calendar myCalendar = Calendar.getInstance();

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
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        if (isFrom) {

            etFrom.setText(sdf.format(myCalendar.getTime()));
        } else {
            etTo.setText(sdf.format(myCalendar.getTime()));
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
