package com.app.hakeem.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.app.hakeem.interfaces.FileUploadListener;
import com.app.hakeem.pojo.UploadFileRes;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by aditya.singh on 2/22/2018.
 */

public class UploadFileFroURL extends AsyncTask<String, Integer, String> {

    private String upLoadServerUri = "http://www.dataheadstudio.com/test/api/upload";
    private File sourceFile;
    private int serverResonseCode;
    private Activity activity;
    private ProgressDialog mProgressDialog;

    FileUploadListener fileUploadListener;

    public UploadFileFroURL(Activity activity, FileUploadListener fileUploadListener) {
        // TODO Auto-generated constructor stub
        this.fileUploadListener = fileUploadListener;
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("Please Wait!");
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
            }, requestURL, "profileUserAuthKey", "uploadprofileimage", activity);


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
            fileUploadListener.onFileUploded(fileRes);
        }
    }

    private String renameFile(String fileName) {
        Random random = new Random();
        int num = 1000 + random.nextInt(9999);
        String newFileName = "LR" + System.currentTimeMillis() + num + "." + fileName;
        return newFileName;
    }


}

