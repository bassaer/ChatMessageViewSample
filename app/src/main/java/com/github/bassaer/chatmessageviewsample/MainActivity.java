package com.github.bassaer.chatmessageviewsample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.bassaer.chatmessageview.model.ChatUser;
import com.github.bassaer.chatmessageview.model.IChatUser;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.util.ChatBot;
import com.github.bassaer.chatmessageview.util.DefaultTimeFormatter;
import com.github.bassaer.chatmessageview.view.ChatView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ChatView mChatView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        final IChatUser me = new ChatUser(myId, myName, myIcon);
        final IChatUser you = new ChatUser(yourId, yourName, yourIcon);

        mChatView = findViewById(R.id.chat_view);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new message
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRight(true)
                        .setText(mChatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                mChatView.send(message);
                //Reset edit text
                mChatView.setInputText("");

                ChatBot bot = ChatBot.INSTANCE;

                //Receive message
                final Message receivedMessage = new Message.Builder()
                        .setUser(you)
                        .setRight(false)
                        .setText(bot.talk(me.getName(), message.getText()))
                        .build();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.receive(receivedMessage);
                    }
                }, 1000);
            }

        });

         List<Message> messages = new ArrayList<>();

        for (int i = 0; i < 105; i++) {
            messages.add(new Message.Builder()
                    .setUser(me)
                    .setRight(true)
                    .setText("Sent " + i)
                    .hideIcon(true)
                    .setSendTimeFormatter(new DefaultTimeFormatter())
                    .build());

            messages.add(new Message.Builder()
                    .setUser(you)
                    .setText("received " + i)
                    .setSendTimeFormatter(new DefaultTimeFormatter())
                    .build());
        }

        mChatView.getMessageView().init(messages);
        mChatView.setAutoScroll(true);
        mChatView.getMessageView().setSelection(mChatView.getMessageView().getMessageList().size());
    }

}
