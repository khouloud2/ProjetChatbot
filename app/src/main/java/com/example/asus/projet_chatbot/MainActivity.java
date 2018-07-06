package com.example.asus.projet_chatbot;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.asus.projet_chatbot.webService.MyRequest;
import com.example.asus.projet_chatbot.webService.VolleySingleton;
import com.example.asus.projet_chatbot.chatbot.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText temail, tpassword ;
    TextView txt;
    Button btn;
    private RequestQueue queue;
    private MyRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.ConnectChatbot);
        btn.setOnClickListener(this);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ConnectChatbot: {
                    connect();
                }


                break;

            }

        }


    private void connect() {
        String email = temail.getText().toString();
        String password = tpassword.getText().toString();
        request.connection(email, password, new MyRequest.LoginCallback() {
            @Override
            public void onSucces(int id_user, String email) {
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void init() {
        temail = (EditText) findViewById(R.id.email);
        tpassword = (EditText) findViewById(R.id.password);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);
        btn = (Button) findViewById(R.id.ConnectChatbot);
       txt = (TextView) findViewById(R.id.hy);

    }


    }
