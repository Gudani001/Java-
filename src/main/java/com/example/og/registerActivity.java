package com.example.og;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class registerActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{5,}" +               //at least 5 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        final EditText
                name = findViewById(R.id.name),
                email = findViewById(R.id.email),
                password = findViewById(R.id.password),
                password2 = findViewById(R.id.password2);


        findViewById(R.id.reg).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                boolean alltrue = true;

                if ((!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches())) {
                    email.setError("Please enter a valid email address");
                    alltrue = false;
                }

                if (!PASSWORD_PATTERN.matcher(password.getText().toString().trim()).matches()) {
                    password.setError("Include upper Case,lower Case and a number");
                    alltrue = false;
                }

                if (!password2.getText().toString().trim().equals(password.getText().toString().trim())) {
                    password2.setError("Password does not match");
                    alltrue = false;
                }


                String sUser = name.getText().toString().trim();
                String sPass = password.getText().toString().trim();
                String Email = email.getText().toString().trim();

                ContentValues contentValues = new ContentValues();
                contentValues.put("name", sUser);
                contentValues.put("email", Email);
                contentValues.put("password", sPass);


                if (alltrue) {
                    register(registerActivity.this, contentValues);
                }
            }
        });

    }


    private static void register(final Context c, ContentValues contentValues) {


        new server("https://lamp.ms.wits.ac.za/home/s1853035/OG_SAVE.php", contentValues) {
            @Override
            protected void onPostExecute(String output) {

                if (output.equals("1")) {
                    c.startActivity(new Intent(c, MainActivity.class));
                    Toast.makeText(c, "successful Registration", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(c, "Registration fail", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

}