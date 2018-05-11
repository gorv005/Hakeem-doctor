package com.app.hakeem.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterCityList;
import com.app.hakeem.adapter.AdapterDoctorEducationManage;
import com.app.hakeem.adapter.AdapterDoctorExperienceManage;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.CityList;
import com.app.hakeem.pojo.DoctorProfile;
import com.app.hakeem.pojo.DoctorProfileData;
import com.app.hakeem.pojo.Education;
import com.app.hakeem.pojo.ExperienceDoc;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.UploadFileRes;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.MultipartUtility;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentManageDoctorProfile extends Fragment {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etAbountMe)
    EditText etAbountMe;
    @BindView(R.id.lvEducation)
    ListView lvEducation;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.lvExperiance)
    ListView lvExperiance;
    @BindView(R.id.ivImageProfile)
    ImageView ivImageProfile;
    @BindView(R.id.ivAddExperience)
    TextView ivAddExperience;
    @BindView(R.id.ivAddEducation)
    TextView ivAddEducation;
    @BindView(R.id.rlProfile)
    RelativeLayout rlProfile;
    private Dialog dialog;
    AdapterDoctorExperienceManage adapterDoctorExperienceManage;
    AdapterDoctorEducationManage adapterDoctorEducationManage;
    ImageLoader imageLoader;
    boolean isExpAdd=false;
    java.util.Date startDate,endDate;
    EditText etHospitalName;

    EditText etWorkingSince;
    EditText etUniversityName;

    EditText etDescription;
    EditText etResignedSince;
    Button btnConfirmExperiance;
    Calendar myCalendar = Calendar.getInstance(Locale.US);
    boolean isWorkingSince=false;
    boolean isEdit=false;
    boolean isImageSelected = false;
    private int GALLERY = 1, CAMERA = 2;
    private Uri fileUri;
    Uri contentURI;
    String filePath;
    private AdapterCityList adapter;
    DoctorProfile doctorProfile;
    private ProgressDialog mProgressDialog;
    public FragmentManageDoctorProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Util.hideSoftKeyboardFromDialog(getActivity(),view);
        imageLoader=new ImageLoader(getActivity());
        ivAddEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpToAddEducation();
            }
        });

        ivAddExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openPopUpToAddExperience();
            }
        });
        final List<Education> educations = new ArrayList<>();

        adapterDoctorEducationManage=new AdapterDoctorEducationManage(getActivity(),educations,isEdit);
            lvEducation.setAdapter(adapterDoctorEducationManage);

        final List<ExperienceDoc> experiences = new ArrayList<>();

        adapterDoctorExperienceManage=new AdapterDoctorExperienceManage(getActivity(),experiences,isEdit);
        lvExperiance.setAdapter(adapterDoctorExperienceManage);
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCitySelector();
            }
        });
        disableViews();
        if(getArguments()!=null) {
            String docId = getArguments().getString(C.CHAT_DOCTOR_ID);
            if (docId != null) {
                getDocData(docId);


            } else {
                getDocData(SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());

            }
        }
        else {
            getDocData(SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());

        }

        ActivityContainer.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEdit) {
                    ActivityContainer.tvEdit.setText(getString(R.string.save));
                    enableView();
                    adapterDoctorExperienceManage=new AdapterDoctorExperienceManage(getActivity(),adapterDoctorExperienceManage.getAllItem(),isEdit);
                    lvExperiance.setAdapter(adapterDoctorExperienceManage);
                    adapterDoctorEducationManage=new AdapterDoctorEducationManage(getActivity(),adapterDoctorEducationManage.getAllItem(),isEdit);
                    lvEducation.setAdapter(adapterDoctorEducationManage);
                    setListView();
                }
                else {
                    if(isImageSelected) {
                        new UploadFileFroURL(getActivity()).execute(filePath);
                    }
                    else {
                        getProfileData(doctorProfile.getData().getUpload());
                    }

                }
            }
        });

        ivImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit) {
                    if (Util.isCameraPermissionGranted(getActivity())) {
                        showPictureDialog();
                    } else {
                        requestPermissionForCamera();
                    }
                }
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                getString(R.string.select_from_gallary),
                getString(R.string.select_from_camera)};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    private void requestPermissionForCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)) {
            //     ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Utils.PERMISSION_REQUEST_CODE);
            //TODO     getDailogConfirm("Please allow camera permission in App Settings for additional functionality.", "4");
            //  Toast.makeText(getActivity(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 2);
        }
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        try {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            File file = createImageFile();
            boolean isDirectoryCreated = file.getParentFile().mkdirs();
            Log.e("DEBUG", "openCamera: isDirectoryCreated: " + isDirectoryCreated);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri tempFileUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                        "com.app.hakeem", // As defined in Manifest
                        file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
            } else {
                Uri tempFileUri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
            }
            // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        File file = new File(C.IMAGE_PATH, "IMG_" + timeStamp +
                ".jpg");
        fileUri = Uri.fromFile(file);
        return file;
    }

    public class UploadFileFroURL extends AsyncTask<String, Integer, String> {

        private String upLoadServerUri = "http://www.dataheadstudio.com/test/api/upload";
        private File sourceFile;
        private int serverResonseCode;
        private Activity activity;


        public UploadFileFroURL(Activity activity) {
            // TODO Auto-generated constructor stub
            this.activity = activity;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setTitle("Upload");
            mProgressDialog.setMessage("Uploading, Please Wait!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... content) {

//               String response = uploadFile(content[0]);
            String response = upload(content[0]);
            return response;
        }

        String upload(String sourceFileUri) {
            sourceFile = new File(sourceFileUri);
            String requestURL = "http://www.dataheadstudio.com/test/api/upload";

            try {
                MultipartUtility multipart = new MultipartUtility(new MultipartUtility.ProgressListener() {
                    @Override
                    public void transferred(int num) {
                        publishProgress(num);

                    }
                }, requestURL, "profileUserAuthKey", "uploadprofileimage", getActivity());


//                multipart.addFormField(C.ACTION, "uploadprofileimage");
//                multipart.addFormField("profileUserAuthKey", SharedPreference.getInstance(getActivity()).getString(C.AUTH_KEY_PROFILE));
                String fileName = renameFile(MimeTypeMap.getFileExtensionFromUrl(sourceFileUri));
                multipart.addFilePart("photo", sourceFile, fileName);
//                multipart.addFilePart("document", sourceFile, fileName);
                List<String> response = multipart.finish();

                System.out.println("SERVER REPLIED:");

                for (String line : response) {
                    System.out.println("DEBUG==" + line);
                }

                return response.get(0);
            } catch (IOException ex) {
                System.err.println(ex);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

            mProgressDialog.setProgress(progress[0]);
        }


        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            Log.e("Response :", result);
//            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            Gson gson = new Gson(); // Or use new GsonBuilder().create();
            UploadFileRes fileRes = gson.fromJson(result, UploadFileRes.class);
            if (fileRes.getStatusCode().equals(C.STATUS_SUCCESS)) {
                //doctorRegistration.setPhoto(fileRes.getUrls().getPhoto());
                getProfileData(fileRes.getUrls().getPhoto());
            }
        }





    }
    void getProfileData(String url){
        DoctorProfileData doctorProfileData=new DoctorProfileData();
        if(etUserName.getText().toString().contains(getString(R.string.dr))) {
          String s=  etUserName.getText().toString().replaceAll(getString(R.string.dr),"");
            doctorProfileData.setFname(s);
        }
        else {
            doctorProfileData.setFname(etUserName.getText().toString());

        }
        doctorProfileData.setLname(" ");

        doctorProfileData.setDoctorIid(SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());
        doctorProfileData.setAboutMe(etAbountMe.getText().toString());
            doctorProfileData.setEducation(adapterDoctorEducationManage.getAllItem());
        doctorProfileData.setExperience(adapterDoctorExperienceManage.getAllItem());
        doctorProfileData.setUrl(url);
        doctorProfileData.setLocation(tvLocation.getText().toString());

        postProfile(doctorProfileData);
    }
    private void postProfile(final DoctorProfileData doctorProfileData) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();

        Gson gson = new Gson();
        String json = gson.toJson(doctorProfileData);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
            for(int i=0;i<obj.getJSONArray("experience").length();i++){
                obj.getJSONArray("experience").getJSONObject(i).remove("worked_since");
                obj.getJSONArray("experience").getJSONObject(i).remove("resigned_since");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyService volleyService = new VolleyService(getActivity());
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.e("Response :", response.toString());
                dialog.dismiss();

                try {
                    Gson gson = new Gson();
                    Response responseLogin = gson.fromJson(response.toString(), Response.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        disableViews();
                        //  getDocData();
                        Util.showAlertForToast(getActivity(),getString(R.string.success),responseLogin.getMessage(),getString(R.string.ok),R.drawable.success,false);

                    } else {
                        Util.showAlert(getActivity(), getString(R.string.error), responseLogin.getMessage(), getString(R.string.ok), R.drawable.error);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {
                dialog.dismiss();
                Log.e("Response :", error.toString());

            }
        }, "login", C.API_UPDATE_PROFILE_DOCTOR, Util.getHeader(getActivity()), obj);


    }

    private String renameFile(String fileName) {
        Random random = new Random();
        int num = 1000 + random.nextInt(9999);
        String newFileName = "LR" + System.currentTimeMillis() + num + "." + fileName;
        return newFileName;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    isImageSelected = true;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    //   String path = saveImage(bitmap);
                    bitmap = Util.scaleDown(bitmap, 500, true);
                    ivImageProfile.setImageBitmap(bitmap);
                    filePath = Util.getPath(contentURI, getActivity());
                 /*   String profileImage= Utils.getBase64Image(bitmap);
                    if(C.isloggedIn) {
                        profile.setProfilePic(profileImage);
                        profile.setProfilePicname(Utils.getCurrentTimeStamp()+".jpg");
                    }
                    else {
                        profileDetail.setPicture(profileImage);
                        profileDetail.setPicturename(Utils.getCurrentTimeStamp()+".jpg");
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        } else if (requestCode == CAMERA) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                        options);
                bitmap = Util.rotateImageIfRequired(bitmap, fileUri.getPath());
                isImageSelected = true;
                ivImageProfile.setImageBitmap(bitmap);
                contentURI = fileUri;
                filePath = fileUri.getPath();
               /* String profileImage = Utils.getBase64Image(bitmap);
                if (C.isloggedIn) {
                    profile.setProfilePic(profileImage);
                    profile.setProfilePicname(Utils.getCurrentTimeStamp() + ".jpg");
                } else {
                    profileDetail.setPicture(profileImage);
                    profileDetail.setPicturename(Utils.getCurrentTimeStamp() + ".jpg");
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void disableViews(){
        isEdit=false;
        Util.hideSoftKeyboard(getActivity());
        Util.hideSoftKeyboard(getActivity());

        ActivityContainer.tvEdit.setText(getString(R.string.Edit));

        adapterDoctorExperienceManage=new AdapterDoctorExperienceManage(getActivity(),adapterDoctorExperienceManage.getAllItem(),isEdit);
        lvExperiance.setAdapter(adapterDoctorExperienceManage);
        adapterDoctorEducationManage=new AdapterDoctorEducationManage(getActivity(),adapterDoctorEducationManage.getAllItem(),isEdit);
        lvEducation.setAdapter(adapterDoctorEducationManage);
        setListView();

        //viewUploadImage.setClickable(false);
        etUserName.setEnabled(false);
        /*etUserName.setInputType(InputType.TYPE_NULL);
        etUserName.setFocusable(false);*/
        etAbountMe.setEnabled(false);
        tvLocation.setEnabled(false);
        etUserName.setBackgroundResource(0);
        etAbountMe.setBackgroundResource(R.drawable.card_background_selector);
        tvLocation.setBackgroundResource(0);
        ivAddExperience.setVisibility(View.GONE);
        ivAddEducation.setVisibility(View.GONE);
      /*  ivImageProfile.setFocusable(false);
        ivImageProfile.setClickable(false);*/

    }

    void enableView(){
        isEdit=true;

        //viewUploadImage.setClickable(false);
        etUserName.setEnabled(true);
       /* etUserName.setInputType(InputType.TYPE_CLASS_TEXT);
        etUserName.setFocusable(true);*/
        etAbountMe.setEnabled(true);
        tvLocation.setEnabled(true);
        etUserName.setBackgroundResource(R.drawable.edittext_deselect_blue);
        etAbountMe.setBackgroundResource(R.drawable.edittext_deselect_blue);
        tvLocation.setBackgroundResource(R.drawable.edittext_deselect_blue);
        ivAddExperience.setVisibility(View.VISIBLE);
        ivAddEducation.setVisibility(View.VISIBLE);
     /*   ivImageProfile.setFocusable(true);
        ivImageProfile.setEnabled(false);
        ivImageProfile.setClickable(true);*/

    }
    private void openCitySelector() {


        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pop_up_city_list);
        dialog.setTitle(R.string.select_city);

        if (adapter == null) {
            String city = Util.loadCityJson(getActivity());

            Gson gson = new Gson();

            CityList cityList = gson.fromJson(city, CityList.class);
            adapter = new AdapterCityList(getActivity(), cityList.getCities());
        }
        ListView lvCities = (ListView) dialog.findViewById(R.id.List);
        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tvLocation.setText(adapter.getItem(position).getName());
                dialog.dismiss();
            }
        });
        lvCities.setAdapter(adapter);
        dialog.show();


    }

    public boolean isAllVaildDetailOfEducation() {
        if (etUniversityName.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.University_required),getString(R.string.ok),R.drawable.warning);

            etUniversityName.requestFocus();
            return false;
        }

        else if (etDescription.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.description_required),getString(R.string.ok),R.drawable.warning);

            etDescription.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isAllVaildDetailOfExp() {
        if (etHospitalName.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.hospital_name_required),getString(R.string.ok),R.drawable.warning);

            etHospitalName.requestFocus();
            return false;
        }
        else if (etWorkingSince.getText().toString().length() == 0) {
            // etWorkingSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.working_since_required),getString(R.string.ok),R.drawable.warning);

            etWorkingSince.requestFocus();
            return false;
        }
        else if (etResignedSince.getText().toString().length() == 0) {
            //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.resigned_date_required),getString(R.string.ok),R.drawable.warning);

            etResignedSince.requestFocus();
            return false;
        }
        else if (endDate!=null) {
            //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            if (!Util.isValidToFromDates(startDate,endDate)) {
                //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
                Util.showAlert(getActivity(), getString(R.string.warning), getString(R.string.resigned_date_should_be_less_than_joining), getString(R.string.ok), R.drawable.warning);

                etResignedSince.requestFocus();
                return false;
            }
        }
        else if (etDescription.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.description_required),getString(R.string.ok),R.drawable.warning);

            etDescription.requestFocus();
            return false;
        }
        return true;
    }

    public void setListView(){
        Util.setListViewHeightBasedOnChildren(lvEducation);
        Util.setListViewHeightBasedOnChildren(lvExperiance);


    }
    public void openPopUpToEditEducation(final int pos) {

        startDate=null;
        endDate=null;
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_education, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);



        etUniversityName = (EditText) deleteDialogView.findViewById(R.id.etUniversityName);

        etDescription = (EditText) deleteDialogView.findViewById(R.id.etDescription);

        btnConfirmExperiance = (Button) deleteDialogView.findViewById(R.id.btnConfirmAddExperiance);


        Education education=adapterDoctorEducationManage.getItem(pos);
        etUniversityName.setText(education.getUniversityName());
        etDescription.setText(education.getDescription());
        btnConfirmExperiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllVaildDetailOfEducation()) {
                    Education education=new Education();
                    education.setEduId("");

                    education.setUniversityName(etUniversityName.getText().toString());
                    education.setDescription(etDescription.getText().toString());

                    // experiences.add(experience);
                    adapterDoctorEducationManage.updateItem(pos,education);

                    Util.setListViewHeightBasedOnChildren(lvEducation);
                    dialog.dismiss();
                }
            }
        });


        dialog.show();


    }

    private void openPopUpToAddEducation() {

        startDate=null;
        endDate=null;
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_education, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);



        etUniversityName = (EditText) deleteDialogView.findViewById(R.id.etUniversityName);

        etDescription = (EditText) deleteDialogView.findViewById(R.id.etDescription);

        btnConfirmExperiance = (Button) deleteDialogView.findViewById(R.id.btnConfirmAddExperiance);



        btnConfirmExperiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllVaildDetailOfEducation()) {
                    Education education=new Education();
                    education.setEduId("na");

                    education.setUniversityName(etUniversityName.getText().toString());
                    education.setDescription(etDescription.getText().toString());

                    // experiences.add(experience);
                    adapterDoctorEducationManage.addItem(education);

                    Util.setListViewHeightBasedOnChildren(lvEducation);
                    dialog.dismiss();
                }
            }
        });


        dialog.show();


    }
    public void openPopUpToUpdateExperience(final int pos) {

        startDate=null;
        endDate=null;
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_experience_manage, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);



        etHospitalName = (EditText) deleteDialogView.findViewById(R.id.etHospitalName);
        etWorkingSince = (EditText) deleteDialogView.findViewById(R.id.etWorkingSince);
        etResignedSince = (EditText) deleteDialogView.findViewById(R.id.etResignedSince);
        etDescription = (EditText) deleteDialogView.findViewById(R.id.etDescription);

        btnConfirmExperiance = (Button) deleteDialogView.findViewById(R.id.btnConfirmAddExperiance);


        ExperienceDoc experienceDoc=adapterDoctorExperienceManage.getItem(pos);
        etHospitalName.setText(experienceDoc.getHospitalName());
        etWorkingSince.setText(Util.getDateFromFormats(experienceDoc.getWorkedSince(),C.DATE_FORMAT_FOR_REPORT,C.DATE_FORMAT));
        etResignedSince.setText(Util.getDateFromFormats(experienceDoc.getResignedSince(),C.DATE_FORMAT_FOR_REPORT,C.DATE_FORMAT));
        etDescription.setText(experienceDoc.getDescription());
        btnConfirmExperiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllVaildDetailOfExp()) {
                    ExperienceDoc experience=new ExperienceDoc();
                    experience.setExpId("");
                    experience.setHospitalName(etHospitalName.getText().toString());
                    experience.setWorkedSince(Util.getDateFromString(etWorkingSince.getText().toString()));
                    experience.setResignedSince(Util.getDateFromString(etResignedSince.getText().toString()));
                    experience.setJoining_date(Util.getDateFromString(etWorkingSince.getText().toString()));
                    experience.setResigned_date(Util.getDateFromString(etResignedSince.getText().toString()));
                    experience.setDescription(etDescription.getText().toString());

                    // experiences.add(experience);
                    adapterDoctorExperienceManage.updateItem(pos,experience);
                    isExpAdd=true;
                    Util.setListViewHeightBasedOnChildren(lvExperiance);
                    dialog.dismiss();
                }
            }
        });

        etWorkingSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWorkingSince=true;
                openCalender();
            }
        });

        etResignedSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWorkingSince=false;
                openCalender();
            }
        });
        dialog.show();


    }

    private void openPopUpToAddExperience() {

        startDate=null;
        endDate=null;
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_experience_manage, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);



        etHospitalName = (EditText) deleteDialogView.findViewById(R.id.etHospitalName);
        etWorkingSince = (EditText) deleteDialogView.findViewById(R.id.etWorkingSince);
        etResignedSince = (EditText) deleteDialogView.findViewById(R.id.etResignedSince);
        etDescription = (EditText) deleteDialogView.findViewById(R.id.etDescription);

        btnConfirmExperiance = (Button) deleteDialogView.findViewById(R.id.btnConfirmAddExperiance);



        btnConfirmExperiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllVaildDetailOfExp()) {
                    ExperienceDoc experience=new ExperienceDoc();
                    experience.setExpId("na");
                    experience.setHospitalName(etHospitalName.getText().toString());
                    experience.setWorkedSince(Util.getDateFromString(etWorkingSince.getText().toString()));
                    experience.setResignedSince(Util.getDateFromString(etResignedSince.getText().toString()));
                    experience.setJoining_date(Util.getDateFromString(etWorkingSince.getText().toString()));
                    experience.setResigned_date(Util.getDateFromString(etResignedSince.getText().toString()));
                    experience.setDescription(etDescription.getText().toString());

                    // experiences.add(experience);
                    adapterDoctorExperienceManage.addItem(experience);
                    isExpAdd=true;
                    Util.setListViewHeightBasedOnChildren(lvExperiance);
                    dialog.dismiss();
                }
            }
        });

        etWorkingSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWorkingSince=true;
                openCalender();
            }
        });

        etResignedSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWorkingSince=false;
                openCalender();
            }
        });
        dialog.show();


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

        String myFormat = C.DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        if (isWorkingSince) {
            startDate=myCalendar.getTime();
            etWorkingSince.setText(sdf.format(myCalendar.getTime()));
        } else {
            endDate=myCalendar.getTime();

            etResignedSince.setText(sdf.format(myCalendar.getTime()));
        }

    }

    private void getDocData(String docId) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("doctor_id",  docId);
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
                Log.e("Response :", response.toString());
                dialog.dismiss();

                try {
                    Gson gson = new Gson();
                     doctorProfile = gson.fromJson(response.toString(), DoctorProfile.class);
                    if (doctorProfile.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        rlProfile.setVisibility(View.VISIBLE);
                        imageLoader.DisplayImage(doctorProfile.getData().getUpload(),ivImageProfile);
                        if(!doctorProfile.getData().getName().contains(getString(R.string.dr))) {
                            etUserName.setText(getString(R.string.dr) + "" + doctorProfile.getData().getName().trim());
                        }
                        else {
                            etUserName.setText(doctorProfile.getData().getName().trim());
                        }
                        tvLocation.setText(doctorProfile.getData().getHomeLocation());
                        etAbountMe.setText(doctorProfile.getData().getAboutMe());
                        for (int i=0;i<doctorProfile.getData().getEducation().size();i++) {
                            adapterDoctorEducationManage.addItem(doctorProfile.getData().getEducation().get(i));
                        }
                        Util.setListViewHeightBasedOnChildren(lvEducation);
                        for (int i=0;i<doctorProfile.getData().getExperience().size();i++) {
                            doctorProfile.getData().getExperience().get(i).setJoining_date(doctorProfile.getData().getExperience().get(i).getWorkedSince());
                            doctorProfile.getData().getExperience().get(i).setResigned_date(doctorProfile.getData().getExperience().get(i).getResignedSince());
                            adapterDoctorExperienceManage.addItem(doctorProfile.getData().getExperience().get(i));
                        }
                        Util.setListViewHeightBasedOnChildren(lvExperiance);
                      //  Util.showAlertBackPress(getActivity(),getString(R.string.success),doctorProfile.getMessage(),getString(R.string.ok),R.drawable.success,true);

                    } else {
                        rlProfile.setVisibility(View.GONE);

                        Util.showAlert(getActivity(), getString(R.string.error), doctorProfile.getMessage(), getString(R.string.ok), R.drawable.error);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {
                dialog.dismiss();
                Log.e("Response :", error.toString());

            }
        }, "login", C.API_GET_DOC_DATA, Util.getHeader(getActivity()), obj);
    }

}
