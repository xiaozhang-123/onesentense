package com.example.onesentence;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class open extends AppCompatActivity {
SharedPreferences sharedPreferences;
Boolean a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                a=sharedPreferences.getBoolean("judge",false);
                if(a){
                    Intent mainintent=new Intent(open.this,MainActivity.class);
                    startActivity(mainintent);
                    finish();
                }else{
                    Intent loginintent=new Intent(open.this,login.class);
                    startActivity(loginintent);
                    finish();
                }
                finish();
            }
        },1500);
    }
}
