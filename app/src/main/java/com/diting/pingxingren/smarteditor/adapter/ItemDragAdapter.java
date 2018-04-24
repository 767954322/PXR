package com.diting.pingxingren.smarteditor.adapter;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.smarteditor.listener.ClickListener;
import com.diting.pingxingren.smarteditor.listener.LongClickListener;
import com.diting.pingxingren.smarteditor.model.EditListModel;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.VideoThumbLoaderUtil;

import java.util.List;

/**
 * 添加内容适配器
 * Created by luoxw on 2016/6/20.
 */
public class ItemDragAdapter extends BaseItemDraggableAdapter<EditListModel, BaseViewHolder> {

    private ClickListener mClickListener;
    private LongClickListener mLongClickListener;

    public ItemDragAdapter(List<EditListModel> data, ClickListener clickListener, LongClickListener longClickListener) {
        super(R.layout.item_draggable_view, data);
        this.mClickListener = clickListener;
        this.mLongClickListener = longClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EditListModel item) {
        helper.getView(R.id.iv_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(v, item, helper.getLayoutPosition() - getHeaderLayoutCount());
                }
            }
        });
        helper.getView(R.id.rl_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(v, item, helper.getLayoutPosition() - getHeaderLayoutCount());
                }
            }
        });
        helper.getView(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(v, item.getObj(), helper.getLayoutPosition() - getHeaderLayoutCount());
                }
            }
        });
        helper.getView(R.id.iv_picture).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    mLongClickListener.onLongClick(v, helper.getLayoutPosition() - getHeaderLayoutCount());
                }
                return true;
            }
        });
        helper.getView(R.id.tv_text).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    mLongClickListener.onLongClick(v, helper.getLayoutPosition() - getHeaderLayoutCount());
                }
                return true;
            }
        });
        helper.getView(R.id.rl_video).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    mLongClickListener.onLongClick(v, helper.getLayoutPosition() - getHeaderLayoutCount());
                }
                return true;
            }
        });
        ImageView pictureImageView = helper.getView(R.id.iv_picture);
        ImageView videoImageView = helper.getView(R.id.iv_video);
        String content = item.getObj();
        videoImageView.setTag(content);
        switch (item.getType()) {
            case EditListModel.TYPE_TEXT:
                helper.setVisible(R.id.iv_picture, false);
                helper.setVisible(R.id.rl_video, false);
                helper.setVisible(R.id.tv_text, true);
                helper.setText(R.id.tv_text, content);
                break;
            case EditListModel.TYPE_PICTURE:
                helper.setVisible(R.id.tv_text, false);
                helper.setVisible(R.id.rl_video, false);
                helper.setVisible(R.id.iv_picture, true);
                if (content.contains("http")) {
                    Glide.with(pictureImageView.getContext()).load(content).into(pictureImageView);
                } else {
                    item.setThumbnail(ImageUtil.lessenUriImage(content));
                    pictureImageView.setImageBitmap((Bitmap) item.getThumbnail());
                }

                break;
            case EditListModel.TYPE_VIDEO:
                helper.setVisible(R.id.tv_text, false);
                helper.setVisible(R.id.iv_picture, false);
                helper.setVisible(R.id.rl_video, true);
                if (content.contains("http")) {
                    VideoThumbLoaderUtil.getInstance().showThumbByAsyncTack(content, videoImageView);
                } else {
                    item.setThumbnail(ThumbnailUtils.createVideoThumbnail(content, MediaStore.Video.Thumbnails.MINI_KIND));
                    videoImageView.setImageBitmap((Bitmap) item.getThumbnail());
                }
                break;
            /*case EditListModel.TYPE_SOUND:
                helper.setVisible(R.id.tv_text, true);
                helper.setVisible(R.id.iv_picture, false);
                helper.setVisible(R.id.rl_video, false);
                helper.setText(R.id.tv_text, content);
                break;*/
        }
        helper.setText(R.id.tv_time, item.getAddTime());
    }
}
