package com.example.onesentence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class login extends AppCompatActivity {
SharedPreferences.Editor editor;
SharedPreferences sharedPreferences;
String accountstring;
String secretstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button registerbutton=(Button) findViewById(R.id.registerbutton);
        final EditText accounttext=(EditText) findViewById(R.id.account);
        final EditText secrettext=(EditText) findViewById(R.id.secret);
        final CheckBox checkBox=(CheckBox) findViewById(R.id.checkBox);
        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        accounttext.setText(sharedPreferences.getString("account",""));
        if(!sharedPreferences.getString("secret","").equals("")){
            secrettext.setText(sharedPreferences.getString("secret",""));
        }
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerintent=new Intent(login.this,register.class);
                startActivity(registerintent);
            }
        });//注册按钮

        Button loginbutton=(Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountstring=accounttext.getText().toString();
                secretstring=secrettext.getText().toString().toString();
                if(accountstring.equals("") || secretstring.equals("")){
                    Toast.makeText(login.this,"输入账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                    if(secretstring.equals(sharedPreferences.getString(accountstring,""))) {//判断输入的账号是否存在，或密码错误,这里注意要是equal才行，否则==判断引用是否相同不成立
                        editor=getSharedPreferences("login",MODE_PRIVATE).edit();

                        editor.putString("account",accountstring);//记录当前登录的账号
                        if(checkBox.isChecked()){
                            editor.putBoolean("remember",true);
                            editor.putString("secret",secretstring);
                        }
                        editor.putBoolean("judge",true);
                        editor.apply();
                        editor=getSharedPreferences("daynight",MODE_PRIVATE).edit();
                        editor.putBoolean("mode",false);
                        editor.apply();
                        Toast.makeText(login.this,"欢迎你，"+accountstring,Toast.LENGTH_SHORT).show();
                        Timer timer=new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent mainintent = new Intent(login.this, MainActivity.class);
                                mainintent.putExtra("accountname",accountstring);//发送账号名
                                startActivity(mainintent);
                                finish();
                            }
                        },1000);

                    }else{
                        Toast.makeText(login.this,"密码错误或者未注册",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });//登录按钮
    }
}
