package com.app.hakeem;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hakeem.adapter.AdapterChatList;
import com.app.hakeem.interfaces.ChatMsgListener;
import com.app.hakeem.interfaces.FileUploadListener;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.ChatMessage;
import com.app.hakeem.pojo.MessageAttachment;
import com.app.hakeem.pojo.QueuePerson;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.UploadFileRes;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ChatRepo;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.UploadFileFroURL;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.MyXMPP;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityChatDoctor extends AppCompatActivity {


    private AdapterChatList adapterSideMenu;
    private MyXMPP myXMPP;
    private String sender;
    private String receiver;
    private int GALLERY = 1, CAMERA = 2;

    @BindView(R.id.llFollowUp)
    LinearLayout llFollowUp;
    @BindView(R.id.llDiagnosis)
    LinearLayout llDiagnosis;
    @BindView(R.id.llPrescription)
    LinearLayout llPrescription;
    @BindView(R.id.switchOnline)
    Switch switchOnline;
    @BindView(R.id.tvTotalPatient)
    TextView tvTotalPatient;

    @BindView(R.id.tvPatient)
    TextView tvPatient;

    @BindView(R.id.btnAttach)
    ImageButton btnAttach;
    @BindView(R.id.btnSend)
    ImageButton btnSend;
    @BindView(R.id.btnCamera)
    ImageButton btnCamera;

    @BindView(R.id.etMsg)
    EditText etMsg;

    @BindView(R.id.lvMsg)
    ListView lvMsg;

    @BindView(R.id.ibEndChat)
    ImageButton ibEndChat;


    private Uri contentURI;
    private Uri fileUri;
    private String filePath;
    ChatRepo chatRepo;

    UploadFileFroURL uploadFileFroURL;
    private Dialog dialogQueue;
    private QueuePerson queuePerson;
    private int totalQueuePerson;
    private String currentPatientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_doctor);
        ButterKnife.bind(this);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        queuePerson = (QueuePerson) getIntent().getSerializableExtra(C.USER);
        totalQueuePerson = getIntent().getIntExtra(C.TOTAL_PERSON_INQUEUE, 0);
        tvTotalPatient.setText(totalQueuePerson + "");

        currentPatientId = queuePerson.getPatientId() + "";
        tvPatient.setText(queuePerson.getName());
        receiver = queuePerson.getEmail();
        receiver = receiver.replace("@", "");
        sender = SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getEmail();
        sender = sender.replace("@", "");

        chatRepo = new ChatRepo();
        myXMPP = new MyXMPP();
        myXMPP.init(sender, C.PASSWORD);
        myXMPP.connectConnection();
        myXMPP.chatmanager.addChatListener(new ChatManagerListenerImpl());

        switchOnline.setChecked(SharedPreference.getInstance(this).getBoolean(C.IS_DOCOTR_ONLINE));
        switchOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                goOnline(isChecked ? "1" : "2");
            }
        });

//        OnlineDoctor onlineDoctor = (OnlineDoctor) getIntent().getSerializableExtra(C.DOCTOR);
//        tvPatient.setText(onlineDoctor.getClassification());
//
//        receiver = receiver.replace("@", "");


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityChatDoctor.this, "connection : " + myXMPP.connection.isConnected() + " Auth : " + myXMPP.connection.isAuthenticated(), Toast.LENGTH_SHORT);
                if (myXMPP.connection.isConnected() && myXMPP.connection.isAuthenticated()) {
                    sendMsg(etMsg.getText().toString());
                }


            }
        });

        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myXMPP.connection.isConnected() && myXMPP.connection.isAuthenticated()) {

                    choosePhotoFromGallary();
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.isCameraPermissionGranted(ActivityChatDoctor.this)) {
                    takePhotoFromCamera();
                } else {
                    requestPermissionForCamera();
                }
            }
        });

        uploadFileFroURL = new UploadFileFroURL(this, fileUploadListener);


        etMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().length() == 0) {
                    btnCamera.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.GONE);
                } else {
                    btnCamera.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }
            }
        });
        btnCamera.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);
        ibEndChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAndEndChat(C.API_END_CHAT);
            }
        });


        if (!Util.isNetworkConnectivity(this)) {
            Util.showToast(this, R.string.please_connect_to_the_internet, true);

        } else {

            startAndEndChat(C.API_START_CHAT);
        }

    }


    void loadChatOnConnect() {
        lvMsg.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvMsg.setStackFromBottom(true);
        adapterSideMenu = new AdapterChatList(this, chatRepo.getChatMessages(sender, receiver));
        lvMsg.setAdapter(adapterSideMenu);

    }


    private void sendMsg(final String msg) {

        myXMPP.sendMsg(msg, receiver, new ChatMsgListener() {

            @Override
            public void messageSent() {

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSender(sender);
                chatMessage.setReceiver(receiver);
                chatMessage.setMessage(msg);
                chatMessage.setMessageStatus(ChatMessage.STATUS_MESSAGE_SENT);
                chatMessage.setDelivered(C.FALSE);
                chatMessage.setId(System.currentTimeMillis());
                chatRepo.insert(chatMessage);
                adapterSideMenu.getList().add(chatMessage);
                adapterSideMenu.notifyDataSetChanged();
                scrollMyListViewToBottom();
                etMsg.setText("");
            }
        });

    }

    public void startAndEndChat(final String url) {


        dialogQueue = Util.getProgressDialog(this, R.string.loading);
        dialogQueue.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("doctor_id", SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getUserId() + "");
        hashMap.put("patient_id", queuePerson.getPatientId() + "");
        hashMap.put("start_datetime", Util.getTimeAM());
        hashMap.put("queue_id", queuePerson.getQueueId() + "");


        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyService volleyService = new VolleyService(this);
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.e("Response :", response.toString());
                dialogQueue.dismiss();

                try {
                    Gson gson = new Gson();
                    Response responseLogin = gson.fromJson(response.toString(), Response.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        if (url.equals(C.API_END_CHAT)) {
                            onBackPressed();
                        } else {
                            loadChatOnConnect();
                            tvTotalPatient.setText(totalQueuePerson - 1);
                        }
                    } else if (responseLogin.getStatusCode().equals(C.STATUS_FAIL) && url.equals(C.API_START_CHAT) && responseLogin.getPatient() != null) {
                        receiver = responseLogin.getPatient().getEmail().replace("@", "");
                        currentPatientId = responseLogin.getPatient().getId();
                        tvPatient.setText(responseLogin.getPatient().getName());
                        loadChatOnConnect();
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
        }, "start_chat", url, Util.getHeader(this), obj);


    }


    private class ChatManagerListenerImpl implements ChatManagerListener {

        @Override
        public void chatCreated(final Chat chat, final boolean createdLocally) {


            chat.addMessageListener(new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, final Message message) {


                    ActivityChatDoctor.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ChatMessage chatMessage = new ChatMessage();
                            chatMessage.setMessage(message.getBody());
                            chatMessage.setSender(sender);
                            chatMessage.setReceiver(receiver);
                            chatMessage.setMessageStatus(ChatMessage.STATUS_MESSAGE_RECIVED);
                            chatMessage.setDelivered(C.TRUE);
                            chatMessage.setId(System.currentTimeMillis());
                            chatRepo.insert(chatMessage);
                            adapterSideMenu.getList().add(chatMessage);
                            adapterSideMenu.notifyDataSetChanged();
                            scrollMyListViewToBottom();
                        }
                    });
                }
            });
        }


    }


    FileUploadListener fileUploadListener = new FileUploadListener() {
        @Override
        public void onFileUploded(UploadFileRes response) {
            MessageAttachment messageAttachment = new MessageAttachment();
            messageAttachment.setType(C.IMAGE);
            messageAttachment.setUrl(response.getUrls().getPhoto());
            Gson gson = new Gson();
            String msg = gson.toJson(messageAttachment);
            sendMsg(msg);
        }
    };

    private void requestPermissionForCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            //     ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Utils.PERMISSION_REQUEST_CODE);
            //TODO     getDailogConfirm("Please allow camera permission in App Settings for additional functionality.", "4");
            //  Toast.makeText(this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 2);
        }
    }


    private void takePhotoFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            File file = createImageFile();
            boolean isDirectoryCreated = file.getParentFile().mkdirs();
            Log.e("DEBUG", "openCamera: isDirectoryCreated: " + isDirectoryCreated);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri tempFileUri = FileProvider.getUriForFile(ActivityChatDoctor.this.getApplicationContext(),
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

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        File file = new File(C.IMAGE_PATH, "IMG_" + timeStamp +
                ".jpg");
        fileUri = Uri.fromFile(file);
        return file;
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //   String path = saveImage(bitmap);
                    bitmap = Util.scaleDown(bitmap, 500, true);
//                    ivProfileImage.setImageBitmap(bitmap);
                    filePath = Util.getPath(contentURI, ActivityChatDoctor.this);
                 /*   String profileImage= Utils.getBase64Image(bitmap);
                    if(C.isloggedIn) {
                        profile.setProfilePic(profileImage);
                        profile.setProfilePicname(Utils.getCurrentTimeStamp()+".jpg");
                    }
                    else {
                        profileDetail.setPicture(profileImage);
                        profileDetail.setPicturename(Utils.getCurrentTimeStamp()+".jpg");
                    }*/

                    uploadFileFroURL.execute(filePath);
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
//                bitmap=  Util.rotateImageIfRequired(bitmap,fileUri.getPath());
                contentURI = fileUri;
                filePath = fileUri.getPath();

                uploadFileFroURL.execute(filePath);
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


    private void scrollMyListViewToBottom() {
        lvMsg.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lvMsg.setSelection(adapterSideMenu.getCount() - 1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)

            onBackPressed();
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myXMPP.disconnectConnection();
    }


    private void goOnline(final String status) {


        dialogQueue = Util.getProgressDialog(this, R.string.loading);
        dialogQueue.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("doctor_id", SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getUserId() + "");
        hashMap.put("status_id", status);


        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyService volleyService = new VolleyService(this);
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.e("Response :", response.toString());
                dialogQueue.dismiss();

                try {
                    Gson gson = new Gson();
                    Response responseLogin = gson.fromJson(response.toString(), Response.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {

                        SharedPreference.getInstance(ActivityChatDoctor.this).setBoolean(C.IS_DOCOTR_ONLINE, status.equals("1") ? true : false);
                        Util.showToast(ActivityChatDoctor.this, responseLogin.getMessage(), !SharedPreference.getInstance(ActivityChatDoctor.this).getBoolean(C.IS_DOCOTR_ONLINE));

                    } else {
                        Util.showToast(ActivityChatDoctor.this, responseLogin.getMessage(), false);
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
        }, "online_doctor", C.API_GO_ONLINE, Util.getHeader(this), obj);


    }


}
