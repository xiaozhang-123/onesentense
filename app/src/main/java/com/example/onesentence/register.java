package com.example.onesentence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText acounttext=(EditText) findViewById(R.id.account);
        final EditText secret=(EditText) findViewById(R.id.secret);
        final EditText secretagain=(EditText) findViewById(R.id.secretagain);
        Button button=(Button) findViewById(R.id.registerbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=getSharedPreferences("login",MODE_PRIVATE).edit();
                SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                String acountstr=acounttext.getText().toString();
                String secretstr=secret.getText().toString();
                String secretagainstr=secretagain.getText().toString();
                if(length(acountstr,6)){//判断账号长度
                    if(secretstr.equals(secretagainstr)){//判断两次密码是否相同
                        if(judge(secretstr) && length(secretstr,6)){//判断是否密码符合要求
                            if(sharedPreferences.getString(acountstr,"").equals("")){//如果没注册，那么就会是""
                                editor.putString(acountstr,secretstr);
                                editor.apply();
                                Toast.makeText(register.this,"注册成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{//如果不为空，那么就是注册了
                                Toast.makeText(register.this,"该账号已注册",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(register.this,"密码必须含有字母，数字，标点符号且长度不小于6",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(register.this,"两次输入不匹配，请重试",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(register.this,"账号长度不能过短",Toast.LENGTH_SHORT).show();
                }//即首先账号不能过短，然后两次密码要一样，然后密码要符合要求，然后账号不能被注册，最后注册成功
            }
        });
    }
    public boolean length(String string,int num){//判断长度
        if(string.length()<=num)
        return false;
        else
            return true;
    }
    public boolean judge(String string){//判断成分
        boolean a=false,b=false,c=false;
        String str=",.;[]?\\//-=";
        for(int i=0;i<string.length();i++){
            if(Character.isDigit(string.charAt(i))){//如果有数字，提前返回
               a=true;
            }
            if(str.indexOf(string.charAt(i))!=-1){//如果有包含，那么就进行返回正确
                b=true;
            }
            if(Character.isLetter(string.charAt(i))){
                c=true;
            }
        }
        if(a && b && c){
            return true;
        }
        return false;//否则没有
    }


}
