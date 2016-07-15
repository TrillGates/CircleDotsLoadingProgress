package com.sunofbeaches.circledotsloadingprogress;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;

import com.sunofbeaches.circledotsloadingprogress.view.SCN_CircleDotsLoadingProgress;

public class MainActivity extends Activity {

    private ImageView mCircleProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mCircleProgress = (ImageView) stub.findViewById(R.id.circle_progress);

                //创建加载进度圈
                SCN_CircleDotsLoadingProgress progress = new SCN_CircleDotsLoadingProgress(MainActivity.this, 10);

                //设置为背景
                mCircleProgress.setBackground(progress);


            }
        });
    }
}
