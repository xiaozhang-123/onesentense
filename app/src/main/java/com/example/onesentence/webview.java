package com.example.onesentence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent=getIntent();
        String str=intent.getStringExtra("url");//取出网址
        WebView webpage=(WebView) findViewById(R.id.web);
        webpage.getSettings().setJavaScriptEnabled(true);
        webpage.setWebViewClient(new WebViewClient());
        webpage.loadUrl(str);
    }
}
