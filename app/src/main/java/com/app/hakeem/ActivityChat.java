package com.app.hakeem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.app.hakeem.adapter.AdapterChatList;
import com.app.hakeem.interfaces.ChatMsgListener;
import com.app.hakeem.pojo.ChatMessage;
import com.app.hakeem.webservices.MyXMPP;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

public class ActivityChat extends AppCompatActivity {


    private EditText etMsg;
    private ImageButton btnSend;
    private ListView lvMsg;
    private AdapterChatList adapterSideMenu;
    private MyXMPP myXMPP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etMsg = (EditText) findViewById(R.id.etMsg);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        lvMsg = (ListView) findViewById(R.id.lvMsg);
        adapterSideMenu = new AdapterChatList(this, new ArrayList<ChatMessage>());
        lvMsg.setAdapter(adapterSideMenu);
        lvMsg.setStackFromBottom(true);

        myXMPP = new MyXMPP();
        myXMPP.init("ady", "password");
        myXMPP.connectConnection();
        myXMPP.chatmanager.addChatListener(new ChatManagerListenerImpl());


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityChat.this, "connection : " + myXMPP.connection.isConnected() + " Auth : " + myXMPP.connection.isAuthenticated(), Toast.LENGTH_SHORT);
                myXMPP.sendMsg(etMsg.getText().toString(), "admin", new ChatMsgListener() {

                    @Override
                    public void messageSent() {

                        ChatMessage chatMessage = new ChatMessage();
                        ;
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
}
