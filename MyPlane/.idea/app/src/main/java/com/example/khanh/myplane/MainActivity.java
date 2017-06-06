package com.example.khanh.myplane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView start,exit;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=(TextView)findViewById(R.id.start);
        exit=(TextView)findViewById(R.id.exit);


        start.setOnClickListener(this);
        exit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.start:
                Intent myIntent=new Intent(this, Game.class);
                startActivity(myIntent);
                break;
            case R.id.exit:
                finish();
                break;
        }
    }
}
