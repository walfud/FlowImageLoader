package com.walfud.flowimageloaderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.walfud.flowimageloader.FlowImageLoader;


public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";

    private RecyclerView mRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = (RecyclerView) findViewById(R.id.rv);

        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRv.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_rv, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                String url = String.format("https://raw.githubusercontent.com/walfud/Cache/master/app/src/main/assets/%d.jpg", position);
                FlowImageLoader.with(MainActivity.this).load(url).into(holder.iv).pls();
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
