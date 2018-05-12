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
import com.app.hakeem.pojo.HTWeightReportData;
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
    @BindView(R.id.ivAddWight)
    ImageView ivAddWight;
    @BindView(R.id.ivDownloadPdf)
    ImageView ivDownloadPdf;
    ImageView ivObesityWeight;
    ImageView ivOverWeight;
    ImageView ivNormalWeight;
    ImageView ivUnderWeight;
    private boolean isFrom=false;
    String weightValue="60",hrValue="90",heightValue="150";
     AlertDialog dialogAddweight;
     String mTo,mFrom;
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
        if(SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserType().equals(C.DOCTOR)) {
            ivAddWight.setVisibility(View.GONE);
        }
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightReport();
            }
        });
        mTo=Util.getCurrentDate();
        etFrom.setText(Util.get2MonthNextDateWithoutLocale(Util.getCurrentDateWithoutLocale()));
        mFrom=Util.get2MonthNextDate(Util.getCurrentDate());
        etTo.setText(Util.getCurrentDateWithoutLocale());
        getWeightReport();
        ivAddWight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpAddWeight();
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
        hashMap.put("report", "2");


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
                    new DownloadPdf(getActivity(),responsePDF.getDownloadUrl(),"Weight_report_"+Util.getCurrentTimeStamp()+".pdf");

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

    private void openPopUpAddWeight() {


        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_weight, null);
        dialogAddweight = new AlertDialog.Builder(getActivity()).create();
        dialogAddweight.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddweight.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogAddweight.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAddweight.setView(deleteDialogView);
        dialogAddweight.setCancelable(true);


        final EditText   etWeight = (EditText) deleteDialogView.findViewById(R.id.etWeight);
        final EditText   etHR = (EditText) deleteDialogView.findViewById(R.id.etHR);
        final EditText etHeight = (EditText) deleteDialogView.findViewById(R.id.etHeight);
        final EditText etComment = (EditText) deleteDialogView.findViewById(R.id.etComment);
        ivUnderWeight = (ImageView) deleteDialogView.findViewById(R.id.ivUnderWeight);
        ivNormalWeight = (ImageView) deleteDialogView.findViewById(R.id.ivNormalWeight);
        ivOverWeight = (ImageView) deleteDialogView.findViewById(R.id.ivOverWeight);
        ivObesityWeight = (ImageView) deleteDialogView.findViewById(R.id.ivObesityWeight);

        final Spinner spinnerWeight = (Spinner) deleteDialogView.findViewById(R.id.spinnerWeight);
        final Spinner spinnerHR = (Spinner) deleteDialogView.findViewById(R.id.spinnerHR);
        final Spinner spinnerHeight = (Spinner) deleteDialogView.findViewById(R.id.spinnerHeight);

      Button  btnSubmit = (Button) deleteDialogView.findViewById(R.id.btnSubmit);
        final List weight = new ArrayList<Integer>();
        for (int i = 1; i <= 269; i++) {
            weight.add(Integer.toString(i));
        }
        final List<String> weightList = new ArrayList<>(weight);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,weightList){
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
        spinnerWeight.setAdapter(spinnerArrayAdapter);
        final List hr = new ArrayList<Integer>();
        for (int i = 90; i <= 290; i++) {
            hr.add(Integer.toString(i));
        }
        final List<String> hrList = new ArrayList<>(hr);
        final ArrayAdapter<String> spinnerArrayAdapterhr = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,hrList){
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
        spinnerArrayAdapterhr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHR.setAdapter(spinnerArrayAdapterhr);


        final List height = new ArrayList<Integer>();
        for (int i = 30; i <= 356; i++) {
            height.add(Integer.toString(i));
        }
        final List<String> heightList = new ArrayList<>(height);

        final ArrayAdapter<String> spinnerArrayAdapterheightList = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,heightList){
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
        spinnerArrayAdapterheightList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeight.setAdapter(spinnerArrayAdapterheightList);

        spinnerHR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hrValue=hrList.get(position);
                etHR.setText(hrValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heightValue=heightList.get(position);
                etHeight.setText(heightValue);
                getBMIValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weightValue=weightList.get(position);
                etWeight.setText(weightValue);
                getBMIValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etComment.getText().toString().trim().length()==0){
                addWeightReport("Weight");
            }
            else {
                addWeightReport(etComment.getText().toString());
            }

            }
        });


        etWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               spinnerWeight.performClick();
            }
        });

        etHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerHR.performClick();

            }
        });
        etHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerHeight.performClick();

            }
        });
        spinnerHeight.setSelection(120);
        spinnerHR.setSelection(0);
        spinnerWeight.setSelection(40);

        dialogAddweight.show();

        getBMIValue();

    }

    void getBMIValue(){
        float h=Float.parseFloat(heightValue)/100f;
        h=h*h;
        float bmi=Float.parseFloat(weightValue)/h;
        if(bmi<18.5){
            ivUnderWeight.setVisibility(View.VISIBLE);
            ivNormalWeight.setVisibility(View.INVISIBLE);
            ivOverWeight.setVisibility(View.INVISIBLE);
            ivObesityWeight.setVisibility(View.INVISIBLE);
        }
        else if(bmi>=18.5 && bmi<24.9){
            ivUnderWeight.setVisibility(View.INVISIBLE);
            ivNormalWeight.setVisibility(View.VISIBLE);
            ivOverWeight.setVisibility(View.INVISIBLE);
            ivObesityWeight.setVisibility(View.INVISIBLE);
        }
        else if(bmi>=25 && bmi<29.9){
            ivUnderWeight.setVisibility(View.INVISIBLE);
            ivNormalWeight.setVisibility(View.INVISIBLE);
            ivOverWeight.setVisibility(View.VISIBLE);
            ivObesityWeight.setVisibility(View.INVISIBLE);
        }
        else if(bmi>30){
            ivUnderWeight.setVisibility(View.INVISIBLE);
            ivNormalWeight.setVisibility(View.INVISIBLE);
            ivOverWeight.setVisibility(View.INVISIBLE);
            ivObesityWeight.setVisibility(View.VISIBLE);
        }
    }
    private void addWeightReport(String comment) {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();


            hashMap.put("dependent_id", dependentId);


        hashMap.put("patient_id", patientId);
        hashMap.put("weight", weightValue);
        hashMap.put("rest_hr", hrValue);
        hashMap.put("height",heightValue);

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
                    if(dialogAddweight!=null && dialogAddweight.isShowing()) {
                        dialogAddweight.dismiss();
                        getWeightReport();
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
        }, "callback", C.API_ADD_WEIGHT_REPORT, Util.getHeader(getActivity()), obj);


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

        leftAxis.setAxisMinimum(0);
        mChart.getAxisRight().setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
    }
    private void getWeightReport() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put("dependent_id", dependentId);


        hashMap.put("patient_id", patientId);
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
                HTWeightReportList responseServer = gson.fromJson(response.toString(), HTWeightReportList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    if(responseServer.getData()!=null && responseServer.getData().size()>0) {
                        ((ActivityContainer)getActivity()).setValues(responseServer.getData().get(responseServer.getData().size()-1).getHeight() +" "+getActivity().getString(R.string.cm),
                                responseServer.getData().get(responseServer.getData().size()-1).getWeight()+" "+getActivity().getString(R.string.kg));

                        initGraph(getMin(responseServer.getData()),getMax(responseServer.getData()),responseServer.getData());
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
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                // Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.network_error),getString(R.string.ok),R.drawable.error,false);

            }
        }, "callback", C.API_GET_WEIGHT_REPORT, Util.getHeader(getActivity()), obj);


    }

    protected LineData generateLineData(List<HTWeightReportData> htFeverReportData) {
        List<Entry> entriesHr = new ArrayList<Entry>();
        List<Entry> entriesWeight = new ArrayList<Entry>();

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
       // if(htFeverReportData.size()==1){
            entriesHr.add(new Entry(Float.parseFloat("0"), 0));
            entriesWeight.add(new Entry(Float.parseFloat("0"),0));
      //  }
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
    Calendar myCalendar = Calendar.getInstance(Locale.US);

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
