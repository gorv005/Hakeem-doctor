package com.app.hakeem.fragment;


import android.app.AlertDialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.interfaces.ITempValue;
import com.app.hakeem.pojo.HTFeverReportData;
import com.app.hakeem.pojo.HTFeverReportList;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.util.VerticalSeekBar;
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
public class FragmentHTFeverReport extends Fragment implements ITempValue{

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
    @BindView(R.id.ivAddTemprature)
    ImageView ivAddTemprature;
    ImageView ivVeryCold;
    ImageView ivCold;
    ImageView ivNormal;
    ImageView ivHot;
    ImageView ivVeryHot;
    AlertDialog dialogAddBloodSuger;
    public static float tempvalue=42.0F;
    private boolean isFrom=false;
    VerticalSeekBar verticalSeekBar;
    public FragmentHTFeverReport() {
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
        return inflater.inflate(R.layout.fragment_htfever_report, container, false);
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
            ivAddTemprature.setVisibility(View.GONE);
        }
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFeverReport();
            }
        });
        etTo.setText(Util.getCurrentDate());
        etFrom.setText(Util.get2MonthNextDate(Util.getCurrentDate()));
        getFeverReport();
        ivAddTemprature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpAddBloodSuger();
            }
        });
    }

    private void openPopUpAddBloodSuger() {


        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_temprature, null);
        dialogAddBloodSuger = new AlertDialog.Builder(getActivity()).create();
        dialogAddBloodSuger.requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  dialogAddBloodSuger.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
      //  dialogAddBloodSuger.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAddBloodSuger.setView(deleteDialogView);
     //   dialogAddBloodSuger.getWindow().setLayout(280, 520);
        dialogAddBloodSuger.setCancelable(true);

        final EditText etComment = (EditText) deleteDialogView.findViewById(R.id.etComment);
        final TextView tvC = (TextView) deleteDialogView.findViewById(R.id.tvC);
        final TextView tvVeryHot = (TextView) deleteDialogView.findViewById(R.id.tvVeryHot);
        verticalSeekBar=(VerticalSeekBar)deleteDialogView.findViewById(R.id.seekBar);
        verticalSeekBar.initilize(FragmentHTFeverReport.this);
        final TextView tvHot = (TextView) deleteDialogView.findViewById(R.id.tvHot);
        final TextView tvNormal = (TextView) deleteDialogView.findViewById(R.id.tvNormal);
        final TextView tvCold = (TextView) deleteDialogView.findViewById(R.id.tvCold);
        final TextView tvVeryCold = (TextView) deleteDialogView.findViewById(R.id.tvVeryCold);
        ivVeryCold = (ImageView) deleteDialogView.findViewById(R.id.ivVeryCold);
        ivCold = (ImageView) deleteDialogView.findViewById(R.id.ivCold);
        ivNormal = (ImageView) deleteDialogView.findViewById(R.id.ivNormal);
        ivHot = (ImageView) deleteDialogView.findViewById(R.id.ivHot);
        ivVeryHot = (ImageView) deleteDialogView.findViewById(R.id.ivVeryHot);
        tvC.setText(""+(char) 0x00B0+"C");
        tvVeryHot.setText("Very Hot (ER) \n " + ">39" + (char) 0x00B0+"C");
        tvHot.setText("Hot \n " + "37.5 - 39" + (char) 0x00B0+"C");
        tvNormal.setText("Normal \n " + "36.5 - 37.5" + (char) 0x00B0+"C");
        tvCold.setText("Cold \n " + "36 - 36.5" + (char) 0x00B0+"C");
        tvVeryCold.setText("Very Cold (ER) \n " + "<36" + (char) 0x00B0+"C");


        Button  btnSubmit = (Button) deleteDialogView.findViewById(R.id.btnSubmit);




        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etComment.getText().toString().trim().length()==0){
                    addFeverReport("weight");
                }
                else {
                    addFeverReport(etComment.getText().toString());
                }

            }
        });




        dialogAddBloodSuger.show();


    }
    private void addFeverReport(String comment) {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put("dependent_id", dependentId);


        hashMap.put("patient_id", patientId);
        hashMap.put("temperature", ""+tempvalue);
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
                HTFeverReportList responseServer = gson.fromJson(response.toString(), HTFeverReportList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    if(dialogAddBloodSuger!=null && dialogAddBloodSuger.isShowing()) {
                        dialogAddBloodSuger.dismiss();
                    }
                    Util.showAlertForToast(getActivity(),getString(R.string.alert),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);
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
        }, "callback", C.API_ADD_FEVER_REPORT, Util.getHeader(getActivity()), obj);


    }


    void initGraph(float min,float max,List<HTFeverReportData> inputArray){
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
        leftAxis.setAxisMinimum(0);
        leftAxis.setLabelCount(6);

        mChart.getAxisRight().setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
    }
    private void getFeverReport() {

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
                HTFeverReportList responseServer = gson.fromJson(response.toString(), HTFeverReportList.class);
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
        }, "callback", C.API_GET_FEVER_REPORT, Util.getHeader(getActivity()), obj);


    }

    protected LineData generateLineData(List<HTFeverReportData> htFeverReportData) {
        List<Entry> entries = new ArrayList<Entry>();

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        if(htFeverReportData.size()==1){
            entries.add(new Entry(Float.parseFloat("0"), 0));
        }
        for(int i=0;i<htFeverReportData.size();i++) {
            entries.add(new Entry(Float.parseFloat(""+i+1), Float.parseFloat(htFeverReportData.get(i).getTemperature())));
        }

        LineDataSet ds1 = new LineDataSet(entries, getString(R.string.temprature));
       // LineDataSet ds2 = new LineDataSet(FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "cosine.txt"), "Cosine function");

        ds1.setLineWidth(2f);
  //      ds2.setLineWidth(2f);
        ds1.setDrawValues(false);

        ds1.setDrawCircles(false);
     //   ds2.setDrawCircles(false);
        ds1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ds1.setColor(ContextCompat.getColor(getActivity(), R.color.red_text));
      //  ds2.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);

        // load DataSets from textfiles in assets folders
        sets.add(ds1);
      //  sets.add(ds2);

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



    public  float getMax(List<HTFeverReportData> inputArray){
        float maxValue = Float.parseFloat(inputArray.get(0).getTemperature());
        for(int i=1;i<inputArray.size();i++){
            if(Float.parseFloat(inputArray.get(i).getTemperature()) > maxValue){
                maxValue = Float.parseFloat(inputArray.get(i).getTemperature());
            }
        }
        float mm=maxValue/6;
        return maxValue+mm;

    }
    public  float getMin(List<HTFeverReportData> inputArray){
        float minValue = Float.parseFloat(inputArray.get(0).getTemperature());
        for(int i=1;i<inputArray.size();i++){
            if(Float.parseFloat(inputArray.get(i).getTemperature()) < minValue){
                minValue = Float.parseFloat(inputArray.get(i).getTemperature());
            }
        }
        if(inputArray.size()==1){
            return 0;
        }
        return minValue-1;
    }

    @Override
    public void getValue(float value) {
        Log.e("DEBUG","v="+value);
        tempvalue=value;
        if(value>39){
            ivVeryHot.setVisibility(View.VISIBLE);
            ivCold.setVisibility(View.INVISIBLE);
            ivHot.setVisibility(View.INVISIBLE);
            ivNormal.setVisibility(View.INVISIBLE);
            ivVeryCold.setVisibility(View.INVISIBLE);
        }
        else if(value>37.5 && value<=39){
            ivVeryHot.setVisibility(View.INVISIBLE);
            ivCold.setVisibility(View.INVISIBLE);
            ivHot.setVisibility(View.VISIBLE);
            ivNormal.setVisibility(View.INVISIBLE);
            ivVeryCold.setVisibility(View.INVISIBLE);
        }
        else if(value>36.5 && value<=37.5){
            ivVeryHot.setVisibility(View.INVISIBLE);
            ivCold.setVisibility(View.INVISIBLE);
            ivHot.setVisibility(View.INVISIBLE);
            ivNormal.setVisibility(View.VISIBLE);
            ivVeryCold.setVisibility(View.INVISIBLE);
        }
        else if(value>36 && value<=36.5){
            ivVeryHot.setVisibility(View.INVISIBLE);
            ivCold.setVisibility(View.VISIBLE);
            ivHot.setVisibility(View.INVISIBLE);
            ivNormal.setVisibility(View.INVISIBLE);
            ivVeryCold.setVisibility(View.INVISIBLE);
        }
        else if(value<36){
            ivVeryHot.setVisibility(View.INVISIBLE);
            ivCold.setVisibility(View.INVISIBLE);
            ivHot.setVisibility(View.INVISIBLE);
            ivNormal.setVisibility(View.INVISIBLE);
            ivVeryCold.setVisibility(View.VISIBLE);
        }
    }
}
