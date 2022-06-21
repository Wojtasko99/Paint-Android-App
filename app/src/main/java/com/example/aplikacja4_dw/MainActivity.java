package com.example.aplikacja4_dw;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button red,blue,green,clear,yellow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        red = findViewById(R.id.red_BTN);
        blue = findViewById(R.id.blue_BTN);
        green = findViewById(R.id.green_BTN);
        yellow = findViewById(R.id.yellow_BTN);
        clear = findViewById(R.id.clear);

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowierzchniaRysunku.setColor(Color.RED);
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowierzchniaRysunku.setColor(Color.BLUE);
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowierzchniaRysunku.setColor(Color.GREEN);
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowierzchniaRysunku.setColor(Color.YELLOW);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowierzchniaRysunku.clear();

            }
        });
    }
}