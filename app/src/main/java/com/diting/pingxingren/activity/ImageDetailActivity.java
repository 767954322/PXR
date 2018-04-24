package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.SmoothImageView;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.Utils;

import java.io.File;

public class ImageDetailActivity extends BaseActivity {
    //private int mRes;
    private String img_url;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private SmoothImageView imageView;
    private ImageView image_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_detail);
        //mRes = getIntent().getIntExtra("images", 0);
        img_url = getIntent().getStringExtra("images");
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        //imageView = new SmoothImageView(this);
        imageView =   findViewById(R.id.image);
        image_save =   findViewById(R.id.image_save);
        imageView.setDrawingCacheEnabled(true);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        //imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ScaleType.FIT_CENTER);
        //setContentView(imageView);
//		if (mRes != 0) {
//			imageView.setImageResource(mRes);
//		}
        if (!Utils.isEmpty(img_url)) {
            Glide.with(this).load(img_url).into(imageView);
        }
    }

    @Override
    protected void initEvents() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                imageView.transformOut();
            }
        });
        image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawingCache() != null) {
                    File file = FileSaveUtil.saveBitmap(imageView.getDrawingCache());
                    if (file != null) {
                        showShortToast("图片已保存至" + Const.IMAGE_PATH);
                        FileSaveUtil.sendSaveBroadcast(ImageDetailActivity.this, file);
                    } else {
                        showShortToast("保存失败");
                    }
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (isFinishing()) {
            imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
                @Override
                public void onTransformComplete(int mode) {
                    if (mode == 2) {
                        finish();
                    }
                }
            });
            imageView.transformOut();
        }else{
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

}
