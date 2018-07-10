package com.example.asus.projet_chatbot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.asus.projet_chatbot.chatbot.ChatMessage;
import com.example.asus.projet_chatbot.chatbot.ChatMessageAdapter;
import com.example.asus.projet_chatbot.chatbot.workspace;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import java.util.ArrayList;

public class act extends AppCompatActivity {
workspace w = new workspace();
    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText sendmessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;
    final ConversationService myConversationService =
            new ConversationService(
                    "2018-07-08",w.getUsername(),
w.getPassword()

            );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);



        mListView = (ListView) findViewById(R.id.boite);
        mButtonSend = (FloatingActionButton) findViewById(R.id.send);
        sendmessage = (EditText) findViewById(R.id.et_message);
        mImageView = (ImageView) findViewById(R.id.iv_image);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);

//code for sending the message
        mButtonSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String message = sendmessage.getText().toString();
                Log.d("input",message);

                sendMessage(message);
                mimicOtherMessage(message);
                sendmessage.setText("");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }
    private void sendMessage(final String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
        MessageRequest request = new MessageRequest.Builder()
                .inputText(message)
                .build();



        myConversationService
                .message(w.getIdWorkspace(), request)
                .enqueue(new ServiceCallback<MessageResponse>() {
                    @Override
                    public void onResponse(final MessageResponse response) {

                        Log.d("MESSAGE WATSON RESONSE", response.getInputText());
                        final String outputText = response.getText().get(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mimicOtherMessage(outputText);
                            }
                        }
                        );
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("execption", e.toString());

                    }
                });
    }
    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
            ChatMessage chatMessage = new ChatMessage(null, false, true);
            mAdapter.add(chatMessage);


        }








    }

