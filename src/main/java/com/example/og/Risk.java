package com.example.og;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Risk extends AppCompatActivity {

    String []date;
    String  []address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk);

        OkHttpClient client = new OkHttpClient();

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String Name = sharedPreferences.getString("sUser","");

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s1853035/riskReq.php").newBuilder();
        urlBuilder.addQueryParameter("name", Name);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding

                // Read data on the worker thread
                final String responseData = response.body().string();

                // Run view-related code back on the main thread
                Risk.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doJson(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        (findViewById(R.id.ompha)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();

                SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
                String Name = sharedPreferences.getString("sUser","");

                String risk="";
                contentValues.put("name", Name);
                contentValues.put("risk", risk);
                riskCheck(Risk.this, contentValues);
            }
        });

    }
public void doJson(String json) throws JSONException {
   JSONArray jsonArray = new JSONArray(json);

    date= new String[jsonArray.length()];
    address= new String[jsonArray.length()];

   for (int i=0;i<jsonArray.length();i++) {
       JSONObject obj = jsonArray.getJSONObject(i);

       date[i] = obj.getString("date");
       address[i] = obj.getString("address");

       SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
       String Name = sharedPreferences.getString("sUser","");

       // takes all the current users date,address,name

       ContentValues contentValues = new ContentValues();
       contentValues.put("date", date[i]);
       contentValues.put("address", address[i]);
       contentValues.put("name",Name);

       risk(Risk.this, contentValues);
   }
}

/////////place a risk to db
    private static void risk(final Context c, ContentValues contentValues) {


        new server("https://lamp.ms.wits.ac.za/home/s1853035/risk.php", contentValues) {
            @Override
            protected void onPostExecute(String output) {

                if (output.equals("1")) {

                  //  Toast.makeText(c,"" , Toast.LENGTH_SHORT).show();
                }
                else {
                  //  Toast.makeText(c, "Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }



    //checks if someone is at risk
    private static void riskCheck(final Context c, ContentValues contentValues) {


        new server("https://lamp.ms.wits.ac.za/home/s1853035/riskCheck.php", contentValues) {
            @Override
            protected void onPostExecute(String output) {

                if (output.equals("1")) {

                    Toast.makeText(c, "You are at risk,There are cases that where reported at the location you have been", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(c, "you are not at risk", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}