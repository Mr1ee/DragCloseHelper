package com.dragclosehelper.example;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private CheckBox checkBox;

    private int updateIndex;
    private RecyclerView recyclerView;
    private ImageAdapter mAdapter;

    private int[] resList2 = new int[]{R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxBus.get().register(this);

        checkBox = findViewById(R.id.checkbox);
        recyclerView = findViewById(R.id.rv_image);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ImageAdapter(resList2);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((v, position) -> {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v, "share_photo");
            if (checkBox.isChecked()) {
                Intent intent = new Intent();
                intent.putExtra("index", position);
                intent.setClass(MainActivity.this, ImageViewPreviewActivity.class);
                startActivity(intent, compat.toBundle());
            } else {
                Intent intent = ImagePreviewActivity.buildIntent(this, resList2[position]);
                startActivity(intent, compat.toBundle());
            }
        });

//        setExitSharedElementCallback(new SharedElementCallback() {
//            @Override
//            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
//                Log.d("test exit a", "onSharedElementStart");
//            }
//
//            @Override
//            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
//                Log.d("test exit a", "onSharedElementEnd");
//            }
//
//            @Override
//            public void onRejectSharedElements(List<View> rejectedSharedElements) {
//                super.onRejectSharedElements(rejectedSharedElements);
//                Log.d("test exit a", "onRejectSharedElements");
//            }
//
//            @Override
//            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//                super.onMapSharedElements(names, sharedElements);
//                Log.d("test exit a", "onMapSharedElements");
//                sharedElements.put("share_photo", recyclerView.getChildAt(updateIndex));
//            }
//
//            @Override
//            public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
//                Log.d("test exit a", "onCaptureSharedElementSnapshot");
//                sharedElement.setAlpha(1f);
//                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
//            }
//
//            @Override
//            public View onCreateSnapshotView(Context context, Parcelable snapshot) {
//                Log.d("test exit a", "onCreateSnapshotView");
//                return super.onCreateSnapshotView(context, snapshot);
//            }
//
//            @Override
//            public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
//                Log.d("test exit a", "onSharedElementsArrived");
//                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
//            }
//        });
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag("updateIndex")})
    public void updateIndex(Integer integer) {
        //此处使用rxbus通知对应的共享元素键值对更新
        updateIndex = integer;
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag("updateView")})
    public void updateView(Integer integer) {
        //此处使用rxbus通知对应的view重新显示出来

//        recyclerView.getChildAt(integer).setAlpha(1f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
