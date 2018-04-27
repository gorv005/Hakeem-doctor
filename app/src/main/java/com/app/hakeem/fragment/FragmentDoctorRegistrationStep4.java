package com.app.hakeem.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.ActivityMain;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.DoctorRegistration;
import com.app.hakeem.pojo.LoginCredential;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.ResponseLogin;
import com.app.hakeem.pojo.UploadFileRes;
import com.app.hakeem.util.C;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorRegistrationStep4 extends Fragment {

    @BindView(R.id.etIban)
    EditText etIban;

    @BindView(R.id.checkBox_terms_and_cond)
    CheckBox checkBox_terms_and_cond;

    @BindView(R.id.etConfirmIban)
    EditText etConfirmIban;

    @BindView(R.id.btnPhotoUpload)
    Button btnPhotoUpload;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @BindView(R.id.btnCompleteRegistration)
    Button btnCompleteRegistration;

    @BindView(R.id.tvTerm_and_condition)
    TextView tvTerm_and_condition;
    boolean isImageSelected = false;
    private int GALLERY = 1, CAMERA = 2;
    String action = "";
    private Uri fileUri;
    Uri contentURI;
    private Dialog dialog;
    String filePath;
    DoctorRegistration doctorRegistration;
    private ProgressDialog mProgressDialog;
    private Dialog dialogQueue;


    public FragmentDoctorRegistrationStep4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            doctorRegistration = (DoctorRegistration) bundle.getSerializable(C.USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_registration_step4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        btnPhotoUpload.setOnClickListener(mBtnPicUploadClickListner);
        btnCompleteRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllValid()) {
                    doctorRegistration.setIban(etIban.getText().toString());
                    new UploadFileFroURL(getActivity()).execute(filePath);
                }
            }
        });

        /*etIban.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (etIban.getText().length() == 0)
                    etIban.setText(getString(R.string.sa));
                return false;
            }
        });*/
      /*  etIban.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus && etIban.getText().toString().equals(getString(R.string.sa)))
                    etIban.setText("");
            }
        });*/
        tvTerm_and_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_TERMS_AND_CONDITION,null);
            }
        });
        etIban.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("SA")){
                    etIban.setText("SA");
                    Selection.setSelection(etIban.getText(), etIban.getText().length());

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


//                 if(s.length()>1) {
//                    if (!s.toString().startsWith(getString(R.string.sa))) {
//                        etIban.setText(getString(R.string.sa)+s.toString());
//                        Selection.setSelection(etIban.getText(), etIban
//                                .getText().length());
//
//                    }
//
//                }
//
//                if (s.toString().equals(getString(R.string.sa))) {
//                    etIban.setText("");
//
//                }



            }
        });
        etConfirmIban.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("SA")){
                    etConfirmIban.setText("SA");
                    Selection.setSelection(etConfirmIban.getText(), etConfirmIban.getText().length());

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


//                 if(s.length()>1) {
//                    if (!s.toString().startsWith(getString(R.string.sa))) {
//                        etIban.setText(getString(R.string.sa)+s.toString());
//                        Selection.setSelection(etIban.getText(), etIban
//                                .getText().length());
//
//                    }
//
//                }
//
//                if (s.toString().equals(getString(R.string.sa))) {
//                    etIban.setText("");
//
//                }



            }
        });

    }

    View.OnClickListener mBtnPicUploadClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Util.isCameraPermissionGranted(getActivity())) {
                showPictureDialog();
            } else {
                requestPermissionForCamera();
            }
        }
    };

    public boolean isAllValid() {
        if (etIban.getText().toString().length() == 0) {
            //   etIban.setError(getActivity().getResources().getString(R.string.Iban_required));
            Util.showAlert(getActivity(), getString(R.string.warning), getString(R.string.Iban_required), getString(R.string.ok), R.drawable.warning);

            etIban.requestFocus();
            return false;
        } else if (etConfirmIban.getText().toString().length() == 0) {
            //   etConfirmIban.setError(getActivity().getResources().getString(R.string.Iban_required));
            Util.showAlert(getActivity(), getString(R.string.warning), getString(R.string.Iban_required), getString(R.string.ok), R.drawable.warning);

            etConfirmIban.requestFocus();
            return false;
        } else if (!etConfirmIban.getText().toString().equals(etIban.getText().toString())) {
            //    etConfirmIban.setError(getActivity().getResources().getString(R.string.iban_mismatch));
            Util.showAlert(getActivity(), getString(R.string.warning), getString(R.string.iban_mismatch), getString(R.string.ok), R.drawable.warning);

            etConfirmIban.requestFocus();
            return false;
        } else if (!isImageSelected) {
            //   btnPhotoUpload.setError(getActivity().getResources().getString(R.string.select_image));
            Util.showAlert(getActivity(), getString(R.string.warning), getString(R.string.select_image), getString(R.string.ok), R.drawable.warning);

            btnPhotoUpload.requestFocus();
            return false;
        }
        if(!checkBox_terms_and_cond.isChecked()){
            Util.showAlert(getActivity(), getString(R.string.warning), getString(R.string.please_select_terms_and_condition), getString(R.string.ok), R.drawable.warning);
            return false;
        }
        return true;
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


    private void requestPermissionForCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)) {
            //     ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Utils.PERMISSION_REQUEST_CODE);
            //TODO     getDailogConfirm("Please allow camera permission in App Settings for additional functionality.", "4");
            //  Toast.makeText(getActivity(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 2);
        }
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
                    ivProfileImage.setImageBitmap(bitmap);
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
                ivProfileImage.setImageBitmap(bitmap);
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
                doctorRegistration.setPhoto(fileRes.getUrls().getPhoto());
                doctorRegistration.setDocument(fileRes.getUrls().getPhoto());

                doctorRegistration.setUserGroup("2");
                doDoctorReg(doctorRegistration);
            }
        }

        private String renameFile(String fileName) {
            Random random = new Random();
            int num = 1000 + random.nextInt(9999);
            String newFileName = "LR" + System.currentTimeMillis() + num + "." + fileName;
            return newFileName;
        }


    }

    private void doDoctorReg(final DoctorRegistration doctorRegistration) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();

        Gson gson = new Gson();
        String json = gson.toJson(doctorRegistration);
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
                    ResponseLogin responseLogin = gson.fromJson(response.toString(), ResponseLogin.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_DOCOTR_ONLINE, false);
                        doLogin(doctorRegistration.getEmail(), doctorRegistration.getPassword());
                        //  Toast.makeText(getActivity(),responseLogin.getMessage(),Toast.LENGTH_LONG).show();

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
        }, "login", C.API_DOC_REG, Util.getHeader(getActivity()), obj);


    }

    private void doLogin(String email, String password) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        LoginCredential loginCredential = new LoginCredential();
        loginCredential.setEmail(email);
        loginCredential.setPassword(password);
        Gson gson = new Gson();
        String json = gson.toJson(loginCredential);
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
                    ResponseLogin responseLogin = gson.fromJson(response.toString(), ResponseLogin.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {

                    /*    SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, true);
                        SharedPreference.getInstance(getActivity()).setString(C.AUTH_TOKEN, responseLogin.getUser().getToken());
                        SharedPreference.getInstance(getActivity()).setUser(C.LOGIN_USER, responseLogin.getUser());
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_NOFICATION,true);

                        goOnline();
*/
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(C.DETAILS,doctorRegistration);
                        bundle.putBoolean(C.IS_DOC,true);
                        bundle.putString(C.USER_ID,responseLogin.getUser().getUserId());

                        ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_OTP,bundle);
                    } else {

                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, false);
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_NOFICATION,false);
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
        }, "login", C.API_LOGIN, Util.getHeader(getActivity()), obj);


    }


    private void goOnline() {


        dialogQueue = Util.getProgressDialog(getActivity(), R.string.loading);
        dialogQueue.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("doctor_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId() + "");
        hashMap.put("status_id", "1");


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
                dialogQueue.dismiss();

                try {
                    Gson gson = new Gson();
                    Response responseLogin = gson.fromJson(response.toString(), Response.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {

                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_DOCOTR_ONLINE, true);
                        openMainActivity();
                    } else {

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());
                dialogQueue.dismiss();

            }
        }, "online_doctor", C.API_GO_ONLINE, Util.getHeader(getActivity()), obj);


    }

    private void openMainActivity() {
        Intent intent = new Intent(getActivity(), ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);
    }

}
