package com.diting.pingxingren.smarteditor.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.news.listener.OnMClickListener;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.smarteditor.base.BaseActivity;
import com.diting.pingxingren.smarteditor.model.EditListModel;

/**
 * Created by 2018 on 2018/1/13.
 */

public class EditActivity extends BaseActivity implements OnMClickListener {

    private TitleBarView bar;
    private ImageView pic;
    private VideoView video;
    private String mDataPath = "";

    private EditListModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_edit);
        getData();
        initViews();
        initDatas();
    }

    private void getData() {
        model = getIntent().getExtras().getParcelable("bundle");
    }

    private void initViews() {
        bar = findViewById(R.id.title_bar);
        pic = findViewById(R.id.iv_pic);
        video = findViewById(R.id.video_player_view);
    }

    private void initDatas() {
        bar.initTitle(true, StringUtil.getString(R.string.edit_text), StringUtil.getString(R.string.submit), this);
        switch (model.getType()) {
            case EditListModel.TYPE_PICTURE:
                pic.setVisibility(View.VISIBLE);
                pic.setImageBitmap(BitmapFactory.decodeFile(model.getObj() + ""));
                break;
            case EditListModel.TYPE_VIDEO:
                video.setVisibility(View.VISIBLE);
                video.setVideoPath(model.getObj() + "");
                video.start();
                break;
        }
    }

    @Override
    public void onMClick(int id) {
        switch (id) {
            case R.id.title_btn_right://保存
                String content = "";
                Intent intent = new Intent();
                intent.putExtra("text", StringUtil.isNotEmpty(content) ? content : "");
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
