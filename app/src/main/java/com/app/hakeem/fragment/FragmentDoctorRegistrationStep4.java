package com.app.hakeem.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;


import com.app.hakeem.R;
import com.app.hakeem.pojo.DoctorRegistration;
import com.app.hakeem.pojo.UploadFileRes;
import com.app.hakeem.util.C;
import com.app.hakeem.util.MultipartUtility;
import com.app.hakeem.util.Util;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static com.app.hakeem.util.C.API_UPLOAD_PIC;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorRegistrationStep4 extends Fragment {

    @BindView(R.id.etIban)
    EditText etIban;

    @BindView(R.id.etConfirmIban)
    EditText etConfirmIban;

    @BindView(R.id.btnPhotoUpload)
    Button btnPhotoUpload;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @BindView(R.id.btnCompleteRegistration)
    Button btnCompleteRegistration;

    boolean isImageSelected=false;
    private int GALLERY = 1, CAMERA = 2;
    String action="";
    private Uri fileUri;
    Uri contentURI;
    DoctorRegistration doctorRegistration;
    private ProgressDialog mProgressDialog;


    public FragmentDoctorRegistrationStep4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
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
                if(isAllValid()){
            //       imageUpload(Util.getPath(contentURI,getActivity()),Util.getHeaderForImage(getActivity()));
                  new  UploadFileFroURL(getActivity()).execute(Util.getPath(contentURI,getActivity()));
                }
            }
        });
    }

    View.OnClickListener mBtnPicUploadClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isCameraPermissionGranted()) {
                showPictureDialog();
            } else {
                requestPermissionForCamera();
            }
        }
    };

    public boolean isAllValid() {
        if (etIban.getText().toString().length() == 0) {
            etIban.setError(getActivity().getResources().getString(R.string.please_enter_date_of_birth));
            etIban.requestFocus();
            return false;
        } else if (etConfirmIban.getText().toString().length() == 0) {
            etConfirmIban.setError(getActivity().getResources().getString(R.string.please_enter_city));
            etConfirmIban.requestFocus();
            return false;
        }
        else if (!isImageSelected) {
            btnPhotoUpload.setError(getActivity().getResources().getString(R.string.please_enter_city));
            btnPhotoUpload.requestFocus();
            return false;
        }
        return true;
    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                getString(R.string.select_from_gallary),
                getString(R.string.select_from_camera) };
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
        }
        catch (Exception e){
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
    public  boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                // ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }


    }

    private void requestPermissionForCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)){
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
                    isImageSelected=true;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    //   String path = saveImage(bitmap);
                    bitmap= Util.scaleDown(bitmap, 500, true);
                    ivProfileImage.setImageBitmap(bitmap);
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
                bitmap=  rotateImageIfRequired(bitmap,fileUri.getPath());
                isImageSelected=true;
                ivProfileImage.setImageBitmap(bitmap);
                contentURI=fileUri;
               /* String profileImage = Utils.getBase64Image(bitmap);
                if (C.isloggedIn) {
                    profile.setProfilePic(profileImage);
                    profile.setProfilePicname(Utils.getCurrentTimeStamp() + ".jpg");
                } else {
                    profileDetail.setPicture(profileImage);
                    profileDetail.setPicturename(Utils.getCurrentTimeStamp() + ".jpg");
                }*/
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static Bitmap rotateImageIfRequired(Bitmap img, String selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
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
            if(fileRes.getStatusCode().equals(C.STATUS_SUCCESS)){
                fileRes.getUrls().getPhoto();
            }
        }

        private String renameFile(String fileName) {
            Random random = new Random();
            int num = 1000 + random.nextInt(9999);
            String newFileName = "LR" + System.currentTimeMillis() + num + "." + fileName;
            return newFileName;
        }


    }

}
