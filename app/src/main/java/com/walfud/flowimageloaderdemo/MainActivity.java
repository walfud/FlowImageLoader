package com.walfud.flowimageloaderdemo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle2.components.RxActivity;
import com.walfud.flowimageloader.FlowImageLoader;


public class MainActivity extends RxActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvFlowImageLoader = (RecyclerView) findViewById(R.id.rv);
        rvFlowImageLoader.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvFlowImageLoader.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_rv, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                String url = String.format("https://raw.githubusercontent.com/walfud/Cache/master/app/src/main/assets/%d.jpg", position);
                FlowImageLoader.with(MainActivity.this).load(url).into(holder.iv, R.drawable.loading, R.drawable.fail).cache().pls();
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });

        RecyclerView rvGlide = (RecyclerView) findViewById(R.id.rv_glide);
        rvGlide.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvGlide.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_rv, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                String url = String.format("https://raw.githubusercontent.com/walfud/Cache/master/app/src/main/assets/%d.jpg", position);
                Glide.with(MainActivity.this).load(url).placeholder(R.drawable.loading).error(R.drawable.fail).into(holder.iv);
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });

        RecyclerView rvPicasso = (RecyclerView) findViewById(R.id.rv_picasso);
        rvPicasso.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPicasso.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_rv, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                String url = String.format("https://raw.githubusercontent.com/walfud/Cache/master/app/src/main/assets/%d.jpg", position);
                Glide.with(MainActivity.this).load(url).placeholder(R.drawable.loading).error(R.drawable.fail).into(holder.iv);
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}
