package com.example.og;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ReportCase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_case);

        findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
                String Name = sharedPreferences.getString("sUser","");
                String isSick="";
                ContentValues contentValues = new ContentValues();
                contentValues.put("name",Name);
                contentValues.put("isSick",isSick);
                Report(ReportCase.this, contentValues);

            }
        });

    }
    private static void Report(final Context c, ContentValues contentValues) {


        new server("https://lamp.ms.wits.ac.za/home/s1853035/display.php", contentValues) {
            @Override
            protected void onPostExecute(String output) {

                if (output.equals("1")) {
                    Toast.makeText(c, "Case Successfully Reported", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(c, "Case Reporting Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}