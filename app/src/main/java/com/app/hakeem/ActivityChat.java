package com.app.hakeem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hakeem.adapter.AdapterChatList;
import com.app.hakeem.interfaces.ChatMsgListener;
import com.app.hakeem.interfaces.FileUploadListener;
import com.app.hakeem.pojo.ChatMessage;
import com.app.hakeem.pojo.MessageAttachment;
import com.app.hakeem.pojo.OnlineDoctor;
import com.app.hakeem.pojo.UploadFileRes;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ChatRepo;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.UploadFileFroURL;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.MyXMPP;
import com.google.gson.Gson;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityChat extends AppCompatActivity {


    private EditText etMsg;
    private ListView lvMsg;
    private AdapterChatList adapterSideMenu;
    private MyXMPP myXMPP;
    private String sender;
    private String receiver;
    private int GALLERY = 1, CAMERA = 2;

    @BindView(R.id.btnCamera)
    ImageButton btnCamera;
    @BindView(R.id.btnSend)
    ImageButton btnSend;
    @BindView(R.id.btnAttach)
    ImageButton btnAttach;
    @BindView(R.id.tvClinic)
    TextView tvClinic;
    @BindView(R.id.tvDrName)
    TextView tvDrName;
    @BindView(R.id.ivDrProfile)
    CircleImageView ivDrProfile;
    @BindView(R.id.rlBgImg)
    RelativeLayout rlImage;

    private int speciality;
    private Uri contentURI;
    private Uri fileUri;
    private String filePath;
    ChatRepo chatRepo;

    UploadFileFroURL uploadFileFroURL;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        sender = getIntent().getStringExtra(C.SENDER);
        receiver = getIntent().getStringExtra(C.RECEIVER);

        chatRepo = new ChatRepo();


        OnlineDoctor onlineDoctor = (OnlineDoctor) getIntent().getSerializableExtra(C.DOCTOR);
        speciality = getIntent().getIntExtra(C.SPECIALITY, 1);

        ImageLoader imageLoader = new ImageLoader(this);
        imageLoader.DisplayImage(onlineDoctor.getPhoto(), ivDrProfile);

        tvClinic.setText(onlineDoctor.getClassification());

        tvDrName.setText(onlineDoctor.getFirstName());

        setBgImage(speciality);


        sender = sender.replace("@", "");
        receiver = receiver.replace("@", "");
        etMsg = (EditText) findViewById(R.id.etMsg);
        lvMsg = (ListView) findViewById(R.id.lvMsg);
        lvMsg.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        adapterSideMenu = new AdapterChatList(this, chatRepo.getChatMessages(sender, receiver));
        lvMsg.setAdapter(adapterSideMenu);
        lvMsg.setStackFromBottom(true);

        myXMPP = new MyXMPP();
        myXMPP.init(sender, C.PASSWORD);
        myXMPP.connectConnection();
        myXMPP.chatmanager.addChatListener(new ChatManagerListenerImpl());


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityChat.this, "connection : " + myXMPP.connection.isConnected() + " Auth : " + myXMPP.connection.isAuthenticated(), Toast.LENGTH_SHORT);
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

                if (Util.isCameraPermissionGranted(ActivityChat.this)) {
                    takePhotoFromCamera();
                } else {
                    requestPermissionForCamera();
                }
            }
        });


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
    }


    void setBgImage(int specialityId) {
        switch (specialityId) {
            case 1:
                rlImage.setBackgroundResource(R.drawable.family_blur);
                tvClinic.setText(R.string.family_and_community);
                break;
            case 2:
                rlImage.setBackgroundResource(R.drawable.psy_blur);
                tvClinic.setText(R.string.psychological);
                break;
            case 3:
                rlImage.setBackgroundResource(R.drawable.obde_blur);
                tvClinic.setText(R.string.abdominal);
                break;
            case 4:
                rlImage.setBackgroundResource(R.drawable.obgy_blur);
                tvClinic.setText(R.string.obgyne);
                break;
            case 5:
                rlImage.setBackgroundResource(R.drawable.pediatric_blur);
                tvClinic.setText(R.string.pediatrics);
                break;
        }
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


    private class ChatManagerListenerImpl implements ChatManagerListener {

        @Override
        public void chatCreated(final Chat chat, final boolean createdLocally) {


            chat.addMessageListener(new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, final Message message) {


                    ActivityChat.this.runOnUiThread(new Runnable() {
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
            //     ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Utils.PERMISSION_REQUEST_CODE);
            //TODO     getDailogConfirm("Please allow camera permission in App Settings for additional functionality.", "4");
            //  Toast.makeText(getActivity(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 2);
        }
    }


    private void takePhotoFromCamera() {
        try {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            File file = createImageFile();
            boolean isDirectoryCreated = file.getParentFile().mkdirs();
            Log.e("DEBUG", "openCamera: isDirectoryCreated: " + isDirectoryCreated);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri tempFileUri = FileProvider.getUriForFile(ActivityChat.this.getApplicationContext(),
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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
                    filePath = Util.getPath(contentURI, ActivityChat.this);
                 /*   String profileImage= Utils.getBase64Image(bitmap);
                    if(C.isloggedIn) {
                        profile.setProfilePic(profileImage);
                        profile.setProfilePicname(Utils.getCurrentTimeStamp()+".jpg");
                    }
                    else {
                        profileDetail.setPicture(profileImage);
                        profileDetail.setPicturename(Utils.getCurrentTimeStamp()+".jpg");
                    }*/
                    uploadFileFroURL = new UploadFileFroURL(this, fileUploadListener);

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

                uploadFileFroURL = new UploadFileFroURL(this, fileUploadListener);

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


   public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(C.END_CHAT))

            {

                Util.showAlertForToast(ActivityChat.this, getString(R.string.alert), getString(R.string.chat_ended), getString(R.string.ok), R.drawable.warning,true);

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(C.END_CHAT);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }


}
