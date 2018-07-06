package com.example.asus.projet_chatbot.webService;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRequest {
    private Context context ;
    private RequestQueue queue;

    public MyRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }
    public void connection(final String username, final String password, final LoginCallback callback){
        String url ="http://192.168.2.65/myfiles/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null ;
                Map<String, String> errors = new HashMap<>();
                try {
                     json = new JSONObject(response);
                     System.out.print(json);
                     Boolean error = json.getBoolean("error");
                     if(!error){
                         int id = json.getInt("id");
                         String email =json.getString("email");
                         callback.onSucces(id,email);
                     }else{
                         callback.onError(json.getString("message"));
                     }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


    }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError ){
                    callback.onError("impossible de se connecter");

                }else if (error instanceof  VolleyError) {
                    callback.onError("une erreur s'est produite");

                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("email",username);
                map.put("password",password);
                return map;

            }
        };
        queue.add(request);

    }
    public interface LoginCallback{
        void onSucces( int id, String email);
        void onError(String message);
    }
}
