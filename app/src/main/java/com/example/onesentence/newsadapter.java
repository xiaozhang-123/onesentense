package com.example.onesentence;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

class newsadapter extends RecyclerView.Adapter<newsadapter.ViewHolder>{//适配器
    private List<newsitem> list;
    private Context parentcontent;
    static class ViewHolder extends RecyclerView.ViewHolder{//先创内部类
        View itemview;//这是子布局，方便点击事件
        ImageView imageView;
        TextView textView;
        public ViewHolder(View view){//这个内部类最主要就是要进行对控件的资源加载
            super(view);
            itemview=view;//注意这里多了一个
            imageView=(ImageView) view.findViewById(R.id.imageitem);
            textView=(TextView) view.findViewById(R.id.newsitem);
        }
   }
        public newsadapter(List<newsitem> itemlist){
            list=itemlist;
        }//外部类的构造器初始化列表

        public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){//创建时调用，首先要获得父类的view,然后融合子类的布局，最后输出这个子布局
        parentcontent=parent.getContext();//相当于newslist.this,这里我们也可以弄点击事件
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.newsitem,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.itemview.setOnClickListener(new View.OnClickListener() {//我们调用子类监听器，以holder里面的属性
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();//获取这个holder在列表中的位置
                newsitem item=list.get(position);
                Intent webviewintent=new Intent(v.getContext(), com.example.onesentence.webview.class);//注意这里是v.getcontex()
                SharedPreferences sharedPreferences=parentcontent.getSharedPreferences("news",Context.MODE_PRIVATE);//要调用父类的Parentcontext
                Toast.makeText(v.getContext(),sharedPreferences.getString(item.getNewstext(),""),Toast.LENGTH_SHORT).show();
                webviewintent.putExtra("url",sharedPreferences.getString(item.getNewstext(),""));
                parentcontent.startActivity(webviewintent);//这里用activityd 的方法，注意用parent传进来的对象
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//然后设置属性时调用，这里注意就是set
        newsitem item=list.get(position);
        holder.textView.setText(item.getNewstext());
        Glide.with(parentcontent).load(item.getItemimageurl()).into(holder.imageView);//注意我们弄全局的变量parentcontent来使用glide

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}