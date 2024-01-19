package com.example.og;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public abstract class server extends AsyncTask <String,String,String>{
    private final String address;
    private final ContentValues p;

    protected server(String address, ContentValues p) {
        this.address = address;
        this.p = p;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(2000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            if (p.size() > 0){
                Uri.Builder builder = new Uri.Builder();
                for (String s : p.keySet()) builder.appendQueryParameter(s, p.getAsString(s));
                String query = builder.build().getEncodedQuery();
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
                assert query != null;
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }
            BufferedReader guda = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String okid = guda.readLine();
            guda.close();

            return okid;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected abstract void onPostExecute(String output);
}



