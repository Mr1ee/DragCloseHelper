package com.dragclosehelper.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dragclosehelper.library.DragCloseHelper;

/**
 * @Description: ImagePreviewActivity
 * @Author: lihuayong
 * @CreateDate: 2019-05-29 14:41
 * @UpdateUser:
 * @UpdateDate: 2019-05-29 14:41
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ImagePreviewActivity extends BaseActivity {

    private static final String EXTRA_RES_ID = "extra_res_id";

    private DragCloseHelper dragCloseHelper;

    public static Intent buildIntent(BaseActivity activity, @DrawableRes int resId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RES_ID, resId);
        intent.setClass(activity, ImagePreviewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        //如果在拖拽返回关闭的时候，导航栏上又出现拖拽的view的情况，就用以下代码。就和微信的表现形式一样
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        int resId = getIntent().getIntExtra(EXTRA_RES_ID, 0);
        if (resId == 0) {
            ImagePreviewActivity.this.finish();
        } else {
            ImageView ivImage = findViewById(R.id.iv_image);
            ImageView ivClose = findViewById(R.id.iv_close);
            ivImage.setImageResource(resId);

            ivClose.setOnClickListener(v -> onBackPressed());

            ConstraintLayout ivPreviewCl = findViewById(R.id.iv_preview_cl);

            //初始化拖拽返回
            dragCloseHelper = new DragCloseHelper(this);
            dragCloseHelper.setShareElementMode(true);
            dragCloseHelper.setDragCloseViews(ivPreviewCl, ivImage);
            dragCloseHelper.setDragCloseListener(new DragCloseHelper.DragCloseListener() {
                @Override
                public boolean intercept() {
                    return false;
                }

                @Override
                public void dragStart() {
                    //拖拽开始。可以在此额外处理一些逻辑
                }

                @Override
                public void dragging(float percent) {
                    //拖拽中。percent当前的进度，取值0-1，可以在此额外处理一些逻辑
                }

                @Override
                public void dragCancel() {
                    //拖拽取消，会自动复原。可以在此额外处理一些逻辑
                }

                @Override
                public void dragClose(boolean isShareElementMode) {
                    //拖拽关闭，如果是共享元素的页面，需要执行activity的onBackPressed方法，注意如果使用finish方法，则返回的时候没有共享元素的返回动画
                    if (isShareElementMode) {
                        onBackPressed();
                    }
                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dragCloseHelper.handleEvent(event)) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

}
