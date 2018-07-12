package com.example.asus.projet_chatbot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{
    String url = "http://192.168.190.2/myfiles/try.php";

    Button btn;
    EditText temail, tpassword;
    String password, email;
    TextView visiteur;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main2 );
        init();
        btn.setOnClickListener(this);
        visiteur.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId ()) {

            case R.id.ConnectChatbot:
                final String email = temail.getText ().toString ();
                final String password = tpassword.getText ().toString ();
                new AsyncLogin ().execute ( email, password );
                break;
            case R.id.link2:
                Intent intent = new Intent ( MainActivity2.this, act.class );
                startActivity ( intent );
                break ;
        }




    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog ( MainActivity2.this );
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute ();


            pdLoading.setMessage ( "\tLoading..." );
            pdLoading.setCancelable ( false );
            pdLoading.show ();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL ( "http://192.168.190.2/myfiles/try.php" );

            } catch (MalformedURLException e) {

                e.printStackTrace ();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection ();
                conn.setReadTimeout ( READ_TIMEOUT );
                conn.setConnectTimeout ( CONNECTION_TIMEOUT );
                conn.setRequestMethod ( "POST" );
                conn.setDoInput ( true );
                conn.setDoOutput ( true );
                Uri.Builder builder = new Uri.Builder ()
                        .appendQueryParameter ( "email", params[0] )
                        .appendQueryParameter ( "password", params[1] );
                String query = builder.build ().getEncodedQuery ();

                OutputStream os = conn.getOutputStream ();
                BufferedWriter writer = new BufferedWriter (
                        new OutputStreamWriter ( os, "UTF-8" ) );
                writer.write ( query );
                writer.flush ();
                writer.close ();
                os.close ();
                conn.connect ();
            } catch (IOException e1) {
                e1.printStackTrace ();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode ();


                if (response_code == HttpURLConnection.HTTP_OK) {


                    InputStream input = conn.getInputStream ();
                    BufferedReader reader = new BufferedReader ( new InputStreamReader ( input ) );
                    StringBuilder result = new StringBuilder ();
                    String line;

                    while ((line = reader.readLine ()) != null) {
                        result.append ( line );
                    }

                    // Pass data to onPostExecute method
                    return (result.toString ());

                } else {
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace ();
                return "exception";
            } finally {
                conn.disconnect ();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            Log.d ( "result", result );
            try {
                JSONObject jsonResult = new JSONObject ( result );
                if (!jsonResult.getBoolean (
                        "response" )) {
                    Toast.makeText ( MainActivity2.this, "Invalid email or password", Toast.LENGTH_LONG ).show ();
                } else {
                    Intent intent = new Intent ( MainActivity2.this, act.class );
                    startActivity ( intent );
                    MainActivity2.this.finish ();
                }

            } catch (JSONException e) {
                e.printStackTrace ();
            }
            pdLoading.dismiss ();

        }

    }
    private void init(){
        temail = (EditText) findViewById ( R.id.email );
        tpassword = (EditText) findViewById ( R.id.password );
        visiteur = (TextView) findViewById ( R.id.link2 );
        btn = (Button) findViewById ( R.id.ConnectChatbot );
    }
}
