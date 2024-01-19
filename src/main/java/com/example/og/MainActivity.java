package com.example.og;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText
                name = findViewById(R.id.name);
        final EditText
                password =findViewById(R.id.password);

        final Button
                register = findViewById(R.id.btn_reg),
                login = findViewById(R.id.log_in);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sUser = name.getText().toString().trim();
                String sPass = password.getText().toString().trim();


                SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sUser", sUser);

                editor.apply();


                login(MainActivity.this, sUser, sPass);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, registerActivity.class));
            }
        });

    }
    private static  void login(final Context c, String name, String password){
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("password", password);


        new server("https://lamp.ms.wits.ac.za/home/s1853035/OG_REG.php", contentValues) {
            @Override
            protected void onPostExecute(String output) {
                if (output.equals("1")){

                    c.startActivity(new Intent(c, app.class));
                    Toast.makeText(c, "successful login", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(c, "login fail", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

    }
}
