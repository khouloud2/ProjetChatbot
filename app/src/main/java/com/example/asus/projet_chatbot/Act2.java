package com.example.asus.projet_chatbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.asus.projet_chatbot.chatbot.ChatMessage;
import com.example.asus.projet_chatbot.chatbot.ChatMessageAdapter;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import java.util.ArrayList;

public class Act2 extends AppCompatActivity implements View.OnClickListener {
    ConversationService myConversationService;
    private ListView mListView;
    private EditText sendmessage;
    private ImageView mImageView, mButtonSend;
    private ChatMessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_act2 );
        declaration ();
        initView ();
        mButtonSend.setOnClickListener ( this );
        mImageView.setOnClickListener ( this );

    }

    private void sendMessage(final String message) {
        ChatMessage chatMessage = new ChatMessage ( message, true, false );
        mAdapter.add ( chatMessage );
        MessageRequest request = new MessageRequest.Builder ()
                .inputText ( message )
                .build ();


        myConversationService
                .message ( getString ( R.string.workspaceAN ), request )
                .enqueue ( new ServiceCallback<MessageResponse> () {
                    @Override
                    public void onResponse(final MessageResponse response) {

                        Log.d ( "MESSAGE WATSON RESONSE", response.getInputText () );
                        final String outputText = response.getText ().get ( 0 );
                        Log.d ( "MyOutputMesssage", outputText );
                        Thread timer = new Thread () {
                            @Override
                            public void run() {
                                runOnUiThread ( new Runnable () {
                                    @Override
                                    public void run() {
                                        mimicOtherMessage ( outputText );
                                    }
                                } );
                            }
                        };
                        timer.start ();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d ( "execption", e.toString () );

                    }
                } );
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage ( message, false, false );
        mAdapter.add ( chatMessage );
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage ( null, true, true );
        mAdapter.add ( chatMessage );

        mimicOtherMessage ();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage ( null, false, true );
        mAdapter.add ( chatMessage );


    }

    private void initView() {
        myConversationService = new ConversationService (
                "2018-07-12", getResources ().getString ( R.string.workspace_watson_username ),
                getString ( R.string.workspace_watson_password )
        );
    }

    private void declaration() {
        mListView = (ListView) findViewById ( R.id.boite );
        mButtonSend = (ImageView) findViewById ( R.id.send );
        sendmessage = (EditText) findViewById ( R.id.et_message );
        mImageView = (ImageView) findViewById ( R.id.iv_image );
        mAdapter = new ChatMessageAdapter ( this, new ArrayList<ChatMessage> () );
        mListView.setAdapter ( mAdapter );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId ()) {

            case R.id.send:

                String message = sendmessage.getText ().toString ();
                Log.d ( "input", message );
                sendMessage ( message );
                sendmessage.setText ( "" );
                mListView.setSelection ( mAdapter.getCount () - 1 );

                // code for button when user clicks buttonOne.
                break;

            case R.id.iv_image:
                // do your code
                Intent intent = new Intent ( Act2.this, act.class );
                startActivity ( intent );
                Act2.this.finish ();
                break;

        }
    }
}

