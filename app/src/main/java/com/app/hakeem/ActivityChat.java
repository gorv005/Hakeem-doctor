package com.app.hakeem;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.app.hakeem.pojo.ChatMessage;
import com.app.hakeem.pojo.OnlineDoctor;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.webservices.MyXMPP;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityChat extends AppCompatActivity {


    private EditText etMsg;
    private ImageButton btnSend;
    private ListView lvMsg;
    private AdapterChatList adapterSideMenu;
    private MyXMPP myXMPP;
    private String sender;
    private String receiver;
    @BindView(R.id.ivDrProfile)
    CircleImageView circleImageView;

    @BindView(R.id.rlBgImg)
    RelativeLayout rlImage;

    @BindView(R.id.tvClinic)
    TextView tvClinic;

    @BindView(R.id.tvDrName)
    TextView tvDrName;
    private int speciality;


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
        OnlineDoctor onlineDoctor=(OnlineDoctor) getIntent().getSerializableExtra(C.DOCTOR);
        speciality = getIntent().getIntExtra(C.SPECIALITY,1);

        ImageLoader  imageLoader =new ImageLoader(this);
        imageLoader.DisplayImage(onlineDoctor.getPhoto(),circleImageView);

        tvClinic.setText(onlineDoctor.getClassification());

        tvDrName.setText(onlineDoctor.getFirstName());

        setBgImage(speciality);

        sender = sender.replace("@", "");
        receiver = receiver.replace("@", "");
        etMsg = (EditText) findViewById(R.id.etMsg);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        lvMsg = (ListView) findViewById(R.id.lvMsg);
        adapterSideMenu = new AdapterChatList(this, new ArrayList<ChatMessage>());
        lvMsg.setAdapter(adapterSideMenu);
        lvMsg.setStackFromBottom(true);

        myXMPP = new MyXMPP();
        myXMPP.init(sender, "password");
        myXMPP.connectConnection();
        myXMPP.chatmanager.addChatListener(new ChatManagerListenerImpl());


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityChat.this, "connection : " + myXMPP.connection.isConnected() + " Auth : " + myXMPP.connection.isAuthenticated(), Toast.LENGTH_SHORT);
                myXMPP.sendMsg(etMsg.getText().toString(), receiver, new ChatMsgListener() {

                    @Override
                    public void messageSent() {

                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setMessage(etMsg.getText().toString());
                        chatMessage.setMessageStatus(ChatMessage.STATUS_MESSAGE_SENT);
                        chatMessage.setReciver(false);
                        adapterSideMenu.getList().add(chatMessage);
                        adapterSideMenu.notifyDataSetChanged();

                        scrollMyListViewToBottom();

                        etMsg.setText("");
                    }
                });


            }
        });
    }

   void  setBgImage(int specialityId)
    {
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
                            chatMessage.setMessageStatus(ChatMessage.STATUS_MESSAGE_RECIVED);
                            chatMessage.setReciver(true);
                            adapterSideMenu.getList().add(chatMessage);
                            adapterSideMenu.notifyDataSetChanged();
                            scrollMyListViewToBottom();
                        }
                    });
                }
            });
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
}
