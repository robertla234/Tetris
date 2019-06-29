package com.example.assignment3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.assignment3.views.CustomView;

public class MainActivity extends AppCompatActivity {

    private CustomView mCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomView = (CustomView) findViewById(R.id.customView);
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.shapeLeft();
            }
        });
        findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.shapeRight();
            }
        });
        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.shapeDown();
            }
        });
        findViewById(R.id.btn_counterclock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.shapeRotateLeft();
            }
        });
        findViewById(R.id.btn_clockwise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.shapeRotateRight();
            }
        });

    }
}
