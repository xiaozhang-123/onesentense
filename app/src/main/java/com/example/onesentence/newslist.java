package com.example.onesentence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class newslist extends AppCompatActivity {
SharedPreferences.Editor editor;
SharedPreferences.Editor editor1;
String str;
List<newsitem> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);
        inititem();
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.newsrecyclerlist);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);//注意传参
        recyclerView.setLayoutManager(layoutManager);
        newsadapter itemadapter=new newsadapter(list);
        recyclerView.setAdapter(itemadapter);
    }
    public void inititem(){//引进列表
         editor=getSharedPreferences("news",MODE_PRIVATE).edit();
         editor.clear();
        String date,imageurl,title,url;
        SharedPreferences sharedPreferences=getSharedPreferences("sentence",MODE_PRIVATE);
        String str=sharedPreferences.getString("gsondata","");
        Gson gson=new Gson();
        gsondata data=gson.fromJson(str,gsondata.class);
        List<gsondata.NewsBean> newslist=data.getNews();
        for(gsondata.NewsBean item:newslist){
        date=item.getNews_time();
        imageurl=item.getNews_img();
        title=item.getNews_title();
        url=item.getNews_url();
        list.add(new newsitem(imageurl,title+"\n"+date));
        editor.putString(title+"\n"+date,url);
        }
        editor.apply();
    }


}
