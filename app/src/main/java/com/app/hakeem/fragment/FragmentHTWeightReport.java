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

import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.HTWeightReportData;
import com.app.hakeem.pojo.HTWeightReportList;
import com.app.hakeem.util.C;
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
public class FragmentHTWeightReport extends Fragment {

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
    private boolean isFrom=false;
    public FragmentHTWeightReport() {
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
        return inflater.inflate(R.layout.fragment_htweight_report, container, false);
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
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightReport();
            }
        });
        etTo.setText(Util.getCurrentDate());
        etFrom.setText(Util.get2MonthNextDate(Util.getCurrentDate()));
        getWeightReport();
    }



    void initGraph(float min,float max,List<HTWeightReportData> inputArray){
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
    private void getWeightReport() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("patient_id", patientId);
        hashMap.put("dependent_id", dependentId);
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
                HTWeightReportList responseServer = gson.fromJson(response.toString(), HTWeightReportList.class);
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
        }, "callback", C.API_GET_WEIGHT_REPORT, Util.getHeader(getActivity()), obj);


    }

    protected LineData generateLineData(List<HTWeightReportData> htFeverReportData) {
        List<Entry> entriesHr = new ArrayList<Entry>();
        List<Entry> entriesWeight = new ArrayList<Entry>();

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        if(htFeverReportData.size()==1){
            entriesHr.add(new Entry(Float.parseFloat("0"), 0));
            entriesWeight.add(new Entry(Float.parseFloat("0"),0));
        }
        for(int i=0;i<htFeverReportData.size();i++) {
            entriesHr.add(new Entry(Float.parseFloat(""+i+1), Float.parseFloat(htFeverReportData.get(i).getRestHr())));
            entriesWeight.add(new Entry(Float.parseFloat(""+i+1), Float.parseFloat(htFeverReportData.get(i).getWeight())));

        }

        LineDataSet ds1 = new LineDataSet(entriesHr, getString(R.string.hr));
         LineDataSet ds2 = new LineDataSet(entriesWeight, getString(R.string.weight));

        ds1.setLineWidth(2f);
        ds2.setLineWidth(2f);
        ds1.setDrawValues(false);
        ds2.setDrawValues(false);

        ds1.setDrawCircles(false);
        ds2.setDrawCircles(false);
        ds1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ds2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ds1.setColor(ContextCompat.getColor(getActivity(), R.color.green_dark));
       ds2.setColor(ContextCompat.getColor(getActivity(), R.color.orange));

        // load DataSets from textfiles in assets folders
        sets.add(ds1);
        sets.add(ds2);

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



    public  float getMax(List<HTWeightReportData> inputArray){
        float maxValue = Float.parseFloat(inputArray.get(0).getWeight());
        for(int i=1;i<inputArray.size();i++){
            if(Float.parseFloat(inputArray.get(i).getWeight()) > maxValue){
                maxValue = Float.parseFloat(inputArray.get(i).getWeight());
            }
        }

        float maxValue1 = Float.parseFloat(inputArray.get(0).getRestHr());
        for(int i=1;i<inputArray.size();i++){
            if(Float.parseFloat(inputArray.get(i).getRestHr()) > maxValue1){
                maxValue1 = Float.parseFloat(inputArray.get(i).getRestHr());
            }
        }

        if(maxValue>maxValue1){
            float mm=maxValue/6;
            return maxValue+mm;
        }
        else {
            float mm=maxValue1/6;
            return maxValue1+mm;
        }
    }
    public  float getMin(List<HTWeightReportData> inputArray){
        float minValue = Float.parseFloat(inputArray.get(0).getWeight());
        for(int i=1;i<inputArray.size();i++){
            if(Float.parseFloat(inputArray.get(i).getWeight()) < minValue){
                minValue = Float.parseFloat(inputArray.get(i).getWeight());
            }
        }

        float minValue1 = Float.parseFloat(inputArray.get(0).getRestHr());
        for(int i=1;i<inputArray.size();i++){
            if(Float.parseFloat(inputArray.get(i).getRestHr()) < minValue1){
                minValue1 = Float.parseFloat(inputArray.get(i).getRestHr());
            }
        }
        if(inputArray.size()==1){
            return 0;
        }
        if(minValue<minValue1){
            return minValue-1;
        }
        else {
            return minValue1-1;

        }

    }
}
