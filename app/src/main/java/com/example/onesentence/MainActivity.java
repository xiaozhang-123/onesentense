package com.example.onesentence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    TextView author;
    TextView passage;
    TextView sentence;
    ImageView imageView;
    TextView title;
    TextView navaccounttext;
    SwipeRefreshLayout refreshLayout;//刷新框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//首先drawlayout调出，然后再navigationview调出侧边框布局，然后我们设置监听，然后分别按钮监听
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View drawerview = navigationView.inflateHeaderView(R.layout.nav_header_main);//这里加载动态头布局，方便调用
        navaccounttext = (TextView) drawerview.findViewById(R.id.accounttext);
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        navaccounttext.setText(sharedPreferences.getString("account", ""));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);//左上角的按钮
        drawer.addDrawerListener(toggle);//判断当前状态的监听器
        toggle.syncState();//设置监听状态
        navigationView.setNavigationItemSelectedListener(this);//
        //内容开始

        author = (TextView) findViewById(R.id.author);
        sentence = (TextView) findViewById(R.id.sentence);
        imageView = (ImageView) findViewById(R.id.sentenceimage);
        passage = (TextView) findViewById(R.id.passage);
        title = (TextView) findViewById(R.id.title);
        passage.setText("");
        new Asynctaskstart().execute();//进来就刷新
        //接下来下拉刷新
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);//首先弄到控件id
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));//设置颜色
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置监听器
            @Override
            public void onRefresh() {
                new Asynctaskstart().execute();
            }
        });
        //Daynightinit();//进入页面立马判断是不是夜间模式,这个目前报错

    }
    @Override
    public void onBackPressed() {//按退出时，先关闭抽屉
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;//创右上角的按钮
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);//返回按钮选哪个
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//抽屉的按钮
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.news) {
            Intent newslistintent = new Intent(MainActivity.this, newslist.class);
            startActivity(newslistintent);
        } else if (id == R.id.quit) {
            editor = getSharedPreferences("login", MODE_PRIVATE).edit();
            editor.putBoolean("judge", false);
            if (getSharedPreferences("login", MODE_PRIVATE).getBoolean("remember", false) == false) {
                editor.putString("secret", "");
            }
            editor.putBoolean("remember", false);
            editor.apply();
            Intent loginintent = new Intent(MainActivity.this, login.class);
            startActivity(loginintent);
            finish();//打开登录页面，然后当前页面退出
        } else if (id == R.id.daynight) {
            Daynight();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);//这里是关闭抽屉
        return true;
    }

    class Asynctaskstart extends AsyncTask<Void, String, String> {//我们网上加载的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://meiriyikan.cn/api/json.php").build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return "出错了";
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("出错了")) {
                Toast.makeText(MainActivity.this, "网络开小差了", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);//这里关闭刷新条
                gsonedit(s);
            }
        }

        public void gsonedit(String str) {
            Gson gson = new Gson();
            gsondata data = gson.fromJson(str, gsondata.class);
            String titlestr, authorstr, sentencestr, imageurl, passagestr;
            editor = getSharedPreferences("sentence", MODE_PRIVATE).edit();
            editor.putString("gsondata", str);//存入gson
            titlestr = data.getArticle_title();//获取题目
            editor.putString("title", titlestr);
            title.setText(titlestr);
            authorstr = data.getArticle_author();//获取作者
            editor.putString("author", authorstr);
            author.setText(authorstr);
            sentencestr = data.getWxs();//获取句子
            editor.putString("sentence", sentencestr);
            sentence.setText(sentencestr);
            imageurl = data.getImg_url();
            editor.putString("image", imageurl);
            if (imageurl.indexOf(".gif") == -1) {
                Glide.with(MainActivity.this).load(imageurl).placeholder(R.drawable.loadfalse).error(R.drawable.loadfalse).into(imageView);
            } else {
                Glide.with(MainActivity.this).load(imageurl).asGif().into(imageView);
            }//加载图片
            passagestr = data.getArticle_content();
            passagestr = passagestr.replaceAll("<p>", "    ");
            passagestr = passagestr.replaceAll("</p>", "\n");//这里替换掉乱码
            editor.putString("passage", passagestr);
            passage.setText(passagestr);//加载文章内容
            editor.apply();
        }

    }

    public void Daynight() {//这里灵活切换日夜间模式，首先从文件获取当前状态，然后再appcompatdalegate修改，前提是我们已经有吧style里改成dainight，以及创立一个新文件夹
        sharedPreferences = getSharedPreferences("daynight", MODE_PRIVATE);
        Boolean mode = sharedPreferences.getBoolean("mode", false);//当前白天是faulse,夜晚true
        editor = getSharedPreferences("daynight", MODE_PRIVATE).edit();
        if (mode) {//如果是白天，false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("mode", false);//关闭并换成白天
            Toast.makeText(MainActivity.this,"日间模式",Toast.LENGTH_SHORT).show();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("mode", true);//现在是白天变晚上
            Toast.makeText(MainActivity.this,"夜间模式",Toast.LENGTH_SHORT).show();
        }
        editor.apply();
        //finish();
        //startActivity(new Intent( this, this.getClass()));//先关闭当前页面，然后再打开这个页面

        //overridePendingTransition(0, 0);//设置效果
        //recreate();
        //以上方法会报错
        finish();
        startActivity(getIntent());//用这两个方法不会报错


    }
}
