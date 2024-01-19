package com.example.og;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class app extends AppCompatActivity {
    CardView check,Risk,Disc,j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        check = findViewById(R.id.check_in);
        Risk = findViewById(R.id.rep);
        Disc = findViewById(R.id.Loc);
        j = findViewById(R.id.p);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(app.this, check_in.class));
            }
        });
        Risk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(app.this, ReportCase.class));
            }
        });
        Disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(app.this, DiscoveredCases.class));
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(app.this, Risk.class));
            }
        });
    }
}
