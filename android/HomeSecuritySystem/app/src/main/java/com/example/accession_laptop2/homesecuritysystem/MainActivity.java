package com.example.accession_laptop2.homesecuritysystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;
import static com.android.volley.Response.success;
import static com.example.accession_laptop2.homesecuritysystem.Token.refreshedToken;


public class MainActivity extends AppCompatActivity {
    TextView textView, textView1;
    ImageView imageView;
    public static Button gallery;
    String name,tk,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tk=FirebaseInstanceId.getInstance().getToken();
        token=tk;
        TextView text1=(TextView) findViewById(R.id.text1);
        //text1.setText(token);
        gallery = (Button) findViewById(R.id.button);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select();
            }
        });

        Log.d(TAG, "Message data payload:" + FirebaseInstanceId.getInstance().getToken());
        if(Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            textView=(TextView) findViewById(R.id.text);

            imageView=(ImageView) findViewById(R.id.imageview);

            Intent intent1=getIntent();
            String body=intent1.getStringExtra("body");
            textView.setText(body);
            String imageurl=intent1.getStringExtra("imageurl");


            Log.d(TAG, "Message data payload:" +refreshedToken);

            try {
                URL url = new URL(imageurl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                imageView.setImageBitmap(myBitmap);
                //textView1.setText(imageurl);
            } catch (IOException e) {
                e.printStackTrace();

            }

        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this project the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.





        //imageView.setImageBitmap(bitmap);



    }
    public String Select() {


        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, " api", new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    name = response;
                    //String name1 = name.replace("\"", " ");
                    //data.setText(name1);

                    getimage();

                }

                private boolean getimage() {
                    try {
                        JSONObject jsono = new JSONObject(name);
                        JSONArray jarray = jsono.getJSONArray("Images");

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject object = jarray.getJSONObject(i);


                        }
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textView1.setText(error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";

                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        try {
                            responseString = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        /*responseString = String.valueOf(response.statusCode);*/
                        //responseString = String.valueOf(response.data);
                        // can get more details such as response.headers
                    }

                    return success(responseString, HttpHeaderParser.parseCacheHeaders(response));

                }


            };


            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

}