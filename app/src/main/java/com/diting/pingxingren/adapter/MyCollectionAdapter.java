package com.diting.pingxingren.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.CollectionModel;
import com.diting.pingxingren.util.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class MyCollectionAdapter extends BaseQuickAdapter<CollectionModel, BaseViewHolder> {
    public MyCollectionAdapter(int layoutResId, @Nullable List<CollectionModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final CollectionModel item) {
        String url = item.getUrl();
        String text = item.getText();
        int sort = item.getSort();   //1.音频  2.文件  3.  图片  4.视频  5.文本
        LinearLayout lay_collection = holder.getView(R.id.lay_collection);
        //头部
        TextView tv_collect = holder.getView(R.id.tv_from_text);
        TextView tv_collectTime = holder.getView(R.id.tv_collect_time);
        //文本
        RelativeLayout textRel = holder.getView(R.id.rel_collect_text);
        TextView collectText = holder.getView(R.id.tv_collect_text);
        //文件
        RelativeLayout fileRel = holder.getView(R.id.rel_collect_file);
        TextView collectFile = holder.getView(R.id.tv_collect_filename);
        //视频
        RelativeLayout videoRel = holder.getView(R.id.rel_collect_video);
        ImageView videoImage = holder.getView(R.id.iv_collect_video_image);
        TextView collectVideo = holder.getView(R.id.tv_collect_video_text);
        //图片
        RelativeLayout imageRel = holder.getView(R.id.rel_collect_image);
        ImageView imageUrl = holder.getView(R.id.iv_collect_image);
        TextView collectImage = holder.getView(R.id.tv_collect_image_text);
        //音频
        RelativeLayout audioRel = holder.getView(R.id.rel_collect_audio);
        TextView tv_collect_audioname = holder.getView(R.id.tv_collect_audioname);


        tv_collectTime.setText(TimeUtil.millis2String(item.getTimer(), TimeUtil.DEFAULT_PATTERN));
        String type = "";
        if (sort == 1) {//1.音频
            audioRel.setVisibility(View.VISIBLE);
            fileRel.setVisibility(View.GONE);
            imageRel.setVisibility(View.GONE);
            videoRel.setVisibility(View.GONE);
            textRel.setVisibility(View.GONE);
            tv_collect_audioname.setText(text);
            type = "音频";
        } else if (sort == 2) {//2.文件
            fileRel.setVisibility(View.VISIBLE);
            audioRel.setVisibility(View.GONE);
            imageRel.setVisibility(View.GONE);
            videoRel.setVisibility(View.GONE);
            textRel.setVisibility(View.GONE);
            collectFile.setText(text);
            type = "文件";
        } else if (sort == 3) {// 3.  图片
            imageRel.setVisibility(View.VISIBLE);
            fileRel.setVisibility(View.GONE);
            audioRel.setVisibility(View.GONE);
            videoRel.setVisibility(View.GONE);
            textRel.setVisibility(View.GONE);
            Glide.with(mContext).load(url).into(imageUrl);
            collectImage.setText(text);
            type = " 图片";
        } else if (sort == 4) {//   4.视频
            videoRel.setVisibility(View.VISIBLE);
            imageRel.setVisibility(View.GONE);
            fileRel.setVisibility(View.GONE);
            audioRel.setVisibility(View.GONE);
            textRel.setVisibility(View.GONE);
            Glide.with(mContext).load(R.drawable.icon_coll_video).into(videoImage);

            collectVideo.setText(text);
            type = "视频";
        } else if (sort == 5) {// 5.文本
            textRel.setVisibility(View.VISIBLE);
            videoRel.setVisibility(View.GONE);
            imageRel.setVisibility(View.GONE);
            fileRel.setVisibility(View.GONE);
            audioRel.setVisibility(View.GONE);
            collectText.setText(text);
            type = "文本";
        }
        tv_collect.setText("收藏自 “" + item.getChatrobotname() + " ”的" + type);
        lay_collection.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onDelCollectionListener.deleteCollection(item);
                return false;
            }
        });
        lay_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailsListener.goToCollectionDetails(item);
            }
        });
    }


    private onDelCollectionListener onDelCollectionListener;

    public void setOnDelCollectionListener(MyCollectionAdapter.onDelCollectionListener onDelCollectionListener) {
        this.onDelCollectionListener = onDelCollectionListener;
    }

    public interface onDelCollectionListener {
        void deleteCollection(CollectionModel item);
    }

    private OnDetailsListener onDetailsListener;

    public void setOnDetailsListener(OnDetailsListener onDetailsListener) {
        this.onDetailsListener = onDetailsListener;
    }

    public interface OnDetailsListener {
        void goToCollectionDetails(CollectionModel item);
    }

}
