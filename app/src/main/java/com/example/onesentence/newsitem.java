package com.example.onesentence;

import android.view.View;

public class newsitem {

    String itemimageurl;
    String newstext;
    public newsitem(String str1,String str2){
        this.itemimageurl=str1;
        this.newstext=str2;
    }
    public String getItemimageurl(){
        return itemimageurl;
    }
    public String getNewstext(){
        return newstext;
    }
}
