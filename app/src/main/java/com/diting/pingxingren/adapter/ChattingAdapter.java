package com.diting.pingxingren.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.EnclosureShowActivity;
import com.diting.pingxingren.activity.FoodDetailActivity;
import com.diting.pingxingren.entity.ChatMessageItem;
import com.diting.pingxingren.entity.FoodInfo;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.MediaManager;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;
import com.diting.pingxingren.util.VideoThumbLoaderUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 25, 10:10.
 * Description: .
 */

public class ChattingAdapter extends BaseMultiItemQuickAdapter<ChatMessageItem, BaseViewHolder> {

    private View.OnLongClickListener mOnLongClickListener;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private AddCollectionListener  onlongListener;
    private int mVoicePlayPosition = -1;
    private VoiceIsRead mVoiceIsRead;
    private DownloadFile mDownloadFile;
    private boolean mIsMine = false;
    private Map<Integer, Boolean> mMusicMap = new HashMap<>();
    private List<String> mUnReadPosition = new ArrayList<>();
    private SpeechSynthesizer mTts;

    public ChattingAdapter(Context context, @Nullable List<ChatMessageItem> data) {
        super(data);
        addItemType(Constants.CHAT_TYPE_SENT_TEXT, R.layout.item_msg_mine); // 问答文本
        addItemType(Constants.CHAT_TYPE_SENT_VOICE, R.layout.item_voice_send); // 问答语音
        addItemType(Constants.CHAT_TYPE_RECEIVED_TEXT, R.layout.item_msg_robot); // 回答文本
        addItemType(Constants.CHAT_TYPE_RECEIVED_IMAGE, R.layout.item_msg_robot); // 回答图片
        addItemType(Constants.CHAT_TYPE_RECEIVED_TEXT_AND_IMAGE, R.layout.item_msg_robot); // 回答文本 + 图片
        addItemType(Constants.CHAT_TYPE_RECEIVED_BOOK, R.layout.item_msg_book); // 回答书籍
        addItemType(Constants.CHAT_TYPE_RECEIVED_VOICE, R.layout.item_voice_receive); // 回答声音
        addItemType(Constants.CHAT_TYPE_RECEIVED_MUSIC, R.layout.item_music_receive); // 回答音乐
        addItemType(Constants.CHAT_TYPE_RECEIVED_REMIND, R.layout.item_msg_remind); // 回答提醒
        addItemType(Constants.CHAT_TYPE_RECEIVED_FOOD, R.layout.item_msg_food); // 回答美食
        addItemType(Constants.CHAT_TYPE_RECEIVED_MAIL, R.layout.item_msg_robot); // 回答私信
        addItemType(Constants.CHAT_TYPE_RECEIVED_VIDEO, R.layout.item_msg_video); // 回答视频
        addItemType(Constants.CHAT_TYPE_RECEIVED_AUDIO, R.layout.item_msg_video); // 回答音频
        addItemType(Constants.CHAT_TYPE_RECEIVED_FILE, R.layout.item_msg_video); // 回答文件
        addItemType(Constants.CHAT_TYPE_RECEIVED_HYPERLINK, R.layout.item_msg_hyperlink); // 回答超链接

        mTts = SpeechSynthesizer.createSynthesizer(context, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    ToastUtils.showShortToastSafe("初始化失败,错误码：" + code);
                }
            }
        });
    }

    private void setParams() {
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置使用云端引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, MySharedPreferences.getVoicePeople());
    }

    public void speak(String text, final View animView) {
        setParams();
        mTts.startSpeaking(text, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {
            }

            @Override
            public void onSpeakPaused() {
            }

            @Override
            public void onSpeakResumed() {
            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (speechError != null) {
                    ToastUtils.showShortToastSafe("播报失败");
                } else {
                    if (animView != null) {
                        mVoicePlayPosition = -1;
                        animView.setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                    }
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
            }
        });
    }

    public void stopPlayVoice() {
        if (mVoicePlayPosition != -1) {
            View voicePlay = ((Activity) mContext).findViewById(mVoicePlayPosition);
            if (voicePlay != null) {
                if (getDefItemViewType(mVoicePlayPosition) == Constants.CHAT_TYPE_RECEIVED_VOICE) {
                    voicePlay
                            .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                } else {
                    voicePlay.setBackgroundResource(R.mipmap.adj);
                }
            }
            MediaManager.pause();
            mVoicePlayPosition = -1;
        }

        mTts.stopSpeaking();
    }

//    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
//        mOnLongClickListener = onLongClickListener;
//    }

    public void setVoiceIsRead(VoiceIsRead voiceIsRead) {
        mVoiceIsRead = voiceIsRead;
    }

    public void setLeftClickListener(View.OnClickListener leftClickListener) {
        mLeftClickListener = leftClickListener;
    }

    public void setRightClickListener(View.OnClickListener rightClickListener) {
        mRightClickListener = rightClickListener;
    }

    public void setDownloadFile(DownloadFile downloadFile) {
        mDownloadFile = downloadFile;
    }

    public void setMine(boolean mine) {
        mIsMine = mine;
    }

    public List<String> getUnReadPosition() {
        return mUnReadPosition;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ChatMessageItem messageItem) {
        final int position = holder.getLayoutPosition();
        switch (holder.getItemViewType()) {
            case Constants.CHAT_TYPE_SENT_TEXT:
                initSentTextView(position, holder, messageItem);
                break;
            case Constants.CHAT_TYPE_SENT_VOICE:
                initSentVoiceView(position, holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_TEXT:
            case Constants.CHAT_TYPE_RECEIVED_IMAGE:
            case Constants.CHAT_TYPE_RECEIVED_TEXT_AND_IMAGE:
            case Constants.CHAT_TYPE_RECEIVED_MAIL:
                initCommonReceivedView(holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_BOOK:
                initReceivedBookView(holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_VOICE:
                initReceivedVoiceView(position, holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_MUSIC:
                initReceivedMusicView(position, holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_REMIND:
                initReceivedRemindView(holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_FOOD:
                initReceivedFoodView(holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_VIDEO:
                initReceivedVideoView(holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_AUDIO:
                initReceivedAudioView(holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_FILE:
                initReceivedFileView(holder, messageItem);
                break;
            case Constants.CHAT_TYPE_RECEIVED_HYPERLINK:
                initReceivedHyperLinkView(holder, messageItem);
                break;
        }
    }


    private void initSentTextView(int position, BaseViewHolder holder, final ChatMessageItem messageItem) {
        CircleImageView headImage = holder.getView(R.id.icon_right);
        ImageUtil.setRightHeadImage(mContext, headImage);
        headImage.setOnClickListener(mRightClickListener);

        holder.setText(R.id.tv_time, TimeUtil.millis2String(messageItem.getTime(), TimeUtil.PATTERN_YMD_HM1));
        if (position > 0) {
            if ((messageItem.getTime() - mData.get(position - 1).getTime()) / 1000 > 60) {
                holder.setVisible(R.id.tv_time, true);
            } else {
                holder.setVisible(R.id.tv_time, false);
            }
        } else {
            holder.setVisible(R.id.tv_time, true);
        }

        holder.setText(R.id.right_msg, messageItem.getContent());
        TextView textView = holder.getView(R.id.right_msg);
        textView.setTextSize(MySharedPreferences.getFontSize());
        Utils.addUrl(mContext, textView);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongListener.addCollection(messageItem);
                return false;
            }
        });
    }

    private void initSentVoiceView(final int position, BaseViewHolder holder, final ChatMessageItem messageItem) {
        CircleImageView headImage = holder.getView(R.id.icon_right);
        ImageUtil.setRightHeadImage(mContext, headImage);
        headImage.setOnClickListener(mRightClickListener);

        TextView questionView = holder.getView(R.id.tv_question);
        holder.setText(R.id.tv_time, TimeUtil.millis2String(messageItem.getTime(), TimeUtil.PATTERN_YMD_HM1))
                .setText(R.id.tv_question, messageItem.getContent());
        Utils.addUrl(mContext, questionView);
        questionView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongListener.addCollection(messageItem);
                return false;
            }
        });
        questionView.setTextSize(MySharedPreferences.getFontSize());
        if (position > 0) {
            if ((messageItem.getTime() - mData.get(position - 1).getTime()) / 1000 > 60) {
                holder.setVisible(R.id.tv_time, true);
            } else {
                holder.setVisible(R.id.tv_time, false);
            }
        } else {
            holder.setVisible(R.id.tv_time, true);
        }
        final View animView = holder.getView(R.id.recorder_anim);
        AnimationDrawable drawable;
        animView.setId(position);
        if (position == mVoicePlayPosition) {
            animView.setBackgroundResource(R.mipmap.adj);
            animView.setBackgroundResource(R.drawable.voice_play_send);
            drawable = (AnimationDrawable) animView
                    .getBackground();
            drawable.start();
        } else {
            animView.setBackgroundResource(R.mipmap.adj);
        }

        LinearLayout voiceGroup = holder.getView(R.id.voice_group);
        voiceGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                animView.setBackgroundResource(R.mipmap.adj);
                stopPlayVoice();
                mVoicePlayPosition = animView.getId();
                AnimationDrawable drawable;
                animView.setBackgroundResource(R.drawable.voice_play_send);
                drawable = (AnimationDrawable) animView.getBackground();
                drawable.start();
                String voicePath = messageItem.getVoicePath() == null ? "" : messageItem.getVoicePath();
                if (mVoiceIsRead != null) {
                    mVoiceIsRead.voiceOnClick(position);
                }
                MediaManager.playSound(voicePath,
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mVoicePlayPosition = -1;
                                animView.setBackgroundResource(R.mipmap.adj);
                            }
                        }, null);
            }
        });
        float voiceTime = messageItem.getVoiceTime();
        BigDecimal b = new BigDecimal(voiceTime);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.setText(R.id.voice_time, f1 + "\"");
    }

    private void initCommonReceivedView(BaseViewHolder holder, final ChatMessageItem messageItem) {
        int receivedType = messageItem.getItemType();
        String messageContent = messageItem.getContent();
        final String imageUrl = messageItem.getImgUrl();
        CircleImageView headImage = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImage, messageItem);
        TextView messageView = holder.getView(R.id.left_msg);
        messageView.setTextSize(MySharedPreferences.getFontSize());
        messageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongListener.addCollection(messageItem);
                return false;
            }
        });
        if (!Utils.isEmpty(messageContent)) {
            messageView.setText(Utils.fromHtml(messageContent));
            Utils.addUrl(mContext, messageView);
        }

        LinearLayout layout = holder.getView(R.id.ll_message);
        layout.setBackgroundResource(R.drawable.message_left);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onlongListener.addCollection(messageItem);
                return false;
            }
        });
        ImageView imageView = holder.getView(R.id.image);
        if (!Utils.isEmpty(imageUrl)) {
            Glide.with(mContext).load(imageUrl).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageUtil.gotoDetail((Activity) mContext, imageUrl, v);
                }
            });
        }
        headImage.setOnClickListener(mLeftClickListener);
        switch (receivedType) {
            case Constants.CHAT_TYPE_RECEIVED_TEXT:
//                headImage.setOnClickListener(mLeftClickListener);
                imageView.setVisibility(View.GONE);
                break;
            case Constants.CHAT_TYPE_RECEIVED_IMAGE:
                messageView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                break;
            case Constants.CHAT_TYPE_RECEIVED_TEXT_AND_IMAGE:
                imageView.setVisibility(View.VISIBLE);
                break;
            case Constants.CHAT_TYPE_RECEIVED_MAIL:
                layout.setBackgroundResource(R.drawable.message_mail);
                imageView.setVisibility(View.GONE);
                break;
        }
    }

    private void initReceivedHyperLinkView(BaseViewHolder holder, ChatMessageItem messageItem) {
        CircleImageView headImageView = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImageView, messageItem);
        String messageContent = messageItem.getContent();
        TextView messageView = holder.getView(R.id.left_msg);
        headImageView.setOnClickListener(mLeftClickListener);
        messageView.setTextSize(MySharedPreferences.getFontSize());
        messageView.setText(messageContent);
        TextView linkView = holder.getView(R.id.left_link);

//        linkView.setText(messageItem.getHyperlinkUrl());
        if (!Utils.isEmpty(messageItem.getHyperlinkUrl())) {
            linkView.setVisibility(View.VISIBLE);
            linkView.setText(Utils.fromHtml(messageItem.getHyperlinkUrl()));
            Utils.addUrl(mContext, linkView);
        }else {
            linkView.setVisibility(View.GONE);
        }
    }

    private void initReceivedBookView(BaseViewHolder holder, ChatMessageItem messageItem) {
        CircleImageView headImageView = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImageView, messageItem);
        String bookPicture = messageItem.getBook().getPicture();
        ImageView bookImageView = holder.getView(R.id.image);
        if (Utils.isEmpty(bookPicture)) {
            bookImageView.setImageResource(R.mipmap.icon_load_failed);
        } else {
            Glide.with(mContext).load(bookPicture).into(bookImageView);
        }

        TextView messageView = holder.getView(R.id.left_msg);
        messageView.setText(Html.fromHtml(messageItem.getContent()).toString());
        messageView.setTextSize(MySharedPreferences.getFontSize());
        holder.setText(R.id.tv_book_name, messageItem.getBook().getName())
                .setText(R.id.tv_book_author, "作者: " + messageItem.getBook().getAuthor())
                .setText(R.id.tv_book_partner, "分享者: " + messageItem.getBook().getShare())
                .setText(R.id.tv_book_price, "价格: " + messageItem.getBook().getPrice())
                .setText(R.id.tv_book_profile, "简介: " + messageItem.getBook().getSynopsis());
    }

    private void initReceivedVoiceView(final int position, final BaseViewHolder holder, final ChatMessageItem messageItem) {
        CircleImageView headImageView = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImageView, messageItem);
        headImageView.setOnClickListener(mLeftClickListener);
        final String answer = Utils.fromHtml(messageItem.getContent());
        TextView answerView = holder.getView(R.id.tv_answer);
        answerView.setText(answer);
        Utils.addUrl(mContext, answerView);
        answerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongListener.addCollection(messageItem);
                return false;
            }
        });
        answerView.setTextSize(MySharedPreferences.getFontSize());
        final View animView = holder.getView(R.id.receiver_recorder_anim);
        AnimationDrawable drawable;
        animView.setId(position);
        if (position == mVoicePlayPosition) {
            animView.setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
            animView.setBackgroundResource(R.drawable.voice_play_receiver);
            drawable = (AnimationDrawable) animView.getBackground();
            drawable.start();
        } else {
            animView.setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
        }

        LinearLayout voiceGroup = holder.getView(R.id.voice_group);
        voiceGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                animView.setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                stopPlayVoice();
                mVoicePlayPosition = animView.getId();
                AnimationDrawable drawable;
                animView.setBackgroundResource(R.drawable.voice_play_receiver);
                drawable = (AnimationDrawable) animView.getBackground();
                drawable.start();
                if (mVoiceIsRead != null) {
                    mVoiceIsRead.voiceOnClick(position);
                }
                speak(answer, animView);
            }
        });
        float voiceTime = messageItem.getContent().length() * 0.3f;
        BigDecimal b = new BigDecimal(voiceTime);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.setText(R.id.voice_time, f1 + "\"");
    }

    private void initReceivedMusicView(final int position, final BaseViewHolder holder, ChatMessageItem messageItem) {
        CircleImageView headImageView = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImageView, messageItem);
        headImageView.setOnClickListener(mLeftClickListener);

        final String[] music = messageItem.getContent().split(",");
        holder.setText(R.id.tv_music_name, music[0]).setText(R.id.tv_singer_name, music[1]);
        Boolean isPlaying = mMusicMap.containsKey(position) ? mMusicMap.get(position) : false;
        holder.setImageResource(R.id.iv_play, isPlaying ? R.mipmap.icon_stop : R.mipmap.icon_play);
        ImageView playImageView = holder.getView(R.id.iv_play);
        playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusicState(holder, position);
            }
        });
    }

    public void changeMusicState(BaseViewHolder holder, final int position) {
        ChatMessageItem msg = mData.get(position);
        final Boolean isPlaying = mMusicMap.containsKey(position) ? mMusicMap.get(position) : false;
        final ImageView imageView = holder.getView(R.id.iv_play);
        if (isPlaying) {
            imageView.setImageResource(R.mipmap.icon_play);
            MediaManager.pause();
            mMusicMap.clear();
            mMusicMap.put(position, false);
        } else {
            imageView.setImageResource(R.mipmap.icon_stop);
            mMusicMap.clear();
            mMusicMap.put(position, true);
            MediaManager.playSound(msg.getVoicePath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMusicMap.clear();
                    mMusicMap.put(position, false);
                    imageView.setImageResource(R.mipmap.icon_play);
                }
            }, new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMusicMap.clear();
                    mMusicMap.put(position, false);
                    imageView.setImageResource(R.mipmap.icon_play);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "播放失败，请换一首", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        notifyItemChanged(position);
    }

    public void stopMusic() {
        MediaManager.pause();
        mMusicMap.clear();
        notifyDataSetChanged();
    }

    private void initReceivedRemindView(BaseViewHolder holder, ChatMessageItem messageItem) {
        CircleImageView headImageView = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImageView, messageItem);
        headImageView.setOnClickListener(mLeftClickListener);

        holder.setText(R.id.tv_remind_content, messageItem.getContent());
        RelativeLayout remindLayout = holder.getView(R.id.layout_remind);
        remindLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("content://com.android.calendar/"), "time/epoch");
                mContext.startActivity(intent);
            }
        });
    }

    private void initReceivedFoodView(BaseViewHolder holder, ChatMessageItem messageItem) {
        RecyclerView recyclerView = holder.getView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                mContext, LinearLayoutManager.HORIZONTAL, false));
        final List<FoodInfo> foodInfo = (ArrayList<FoodInfo>) new Gson().fromJson(messageItem.getContent(),
                new TypeToken<List<FoodInfo>>() {
                }.getType());
        FoodInfoAdapter adapter = new FoodInfoAdapter(R.layout.item_food, foodInfo);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mContext.startActivity(
                        FoodDetailActivity.getCallingIntent(mContext, foodInfo.get(position)));
            }
        });
    }

    private void initReceivedVideoView(BaseViewHolder holder, final ChatMessageItem messageItem) {
        CircleImageView headImageView = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImageView, messageItem);
        headImageView.setOnClickListener(mLeftClickListener);
        RelativeLayout left_layout=holder.getView(R.id.left_layout);
        final String videoUrl = messageItem.getVideoUrl();
        String answer = messageItem.getContent();
        TextView answerView = holder.getView(R.id.tvAnswer);
        answerView.setTextSize(MySharedPreferences.getFontSize());
        if (!Utils.isEmpty(answer)) {
            holder.setText(R.id.tvAnswer, answer);
            holder.setVisible(R.id.tvAnswer, true);
        }
        final ImageView videoImageView = holder.getView(R.id.videoImage);
        left_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onlongListener.addCollection(messageItem);
                return false;
            }
        });
        videoImageView.setTag(videoUrl);
        holder.setVisible(R.id.audioOrFileLayout, false)
                .setVisible(R.id.videoLayout, true);
        VideoThumbLoaderUtil.getInstance().showThumbByAsyncTack(videoUrl, videoImageView);
        ImageView playImageView = holder.getView(R.id.ivPlayArea);
        playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent();
                detailIntent.setAction("video");
                detailIntent.putExtra("media_url", videoUrl);
                detailIntent.setClass(mContext, EnclosureShowActivity.class);
                mContext.startActivity(detailIntent);
            }
        });
    }

    private void initReceivedAudioView(BaseViewHolder holder, final ChatMessageItem messageItem) {
        CircleImageView headImageView = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImageView, messageItem);
        headImageView.setOnClickListener(mLeftClickListener);
        RelativeLayout  left_layout=holder.getView(R.id.left_layout);
        final String audioUrl = messageItem.getAudioUrl();
        String answer = messageItem.getContent();
        TextView answerView = holder.getView(R.id.tvAnswer);
        answerView.setTextSize(MySharedPreferences.getFontSize());
        if (!Utils.isEmpty(answer)) {
            holder.setText(R.id.tvAnswer, answer);
            holder.setVisible(R.id.tvAnswer, true);
        }
        holder.setVisible(R.id.audioOrFileLayout, true)
                .setVisible(R.id.videoLayout, false);
        final ImageView videoImageView = holder.getView(R.id.audioOrFileImage);
        videoImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_audio));

        ImageView playImageView = holder.getView(R.id.ivPlay);
        playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent();
                detailIntent.setAction("audio");
                detailIntent.putExtra("media_url", audioUrl);
                detailIntent.setClass(mContext, EnclosureShowActivity.class);
                mContext.startActivity(detailIntent);
            }
        });
        left_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onlongListener.addCollection(messageItem);
                return false;
            }
        });
    }

    private void initReceivedFileView(BaseViewHolder holder, final ChatMessageItem messageItem) {
        CircleImageView headImageView = holder.getView(R.id.icon_left);
        ImageUtil.setLeftHeadImage(mContext, mIsMine, headImageView, messageItem);
        headImageView.setOnClickListener(mLeftClickListener);
        RelativeLayout left_layout=holder.getView(R.id.left_layout);
        final String fileUrl = messageItem.getFileUrl();
        String answer = messageItem.getContent();
        TextView answerView = holder.getView(R.id.tvAnswer);
        answerView.setTextSize(MySharedPreferences.getFontSize());
        if (!Utils.isEmpty(answer)) {
            holder.setText(R.id.tvAnswer, answer);
            holder.setVisible(R.id.tvAnswer, true);
        }

        holder.setVisible(R.id.audioOrFileLayout, true)
                .setVisible(R.id.videoLayout, false);
        final ImageView videoImageView = holder.getView(R.id.audioOrFileImage);
        videoImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_file));

        ImageView playImageView = holder.getView(R.id.ivPlay);
        playImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_download));
        playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownloadFile != null) {
                    mDownloadFile.onDownload(fileUrl);
                }
            }
        });
        left_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onlongListener.addCollection(messageItem);
                return false;
            }
        });
    }

    public void destroy() {
        if (mTts != null)
            mTts.stopSpeaking();
    }

    public BaseViewHolder getViewHolderByPosition(int position) {
        return (BaseViewHolder) getRecyclerView().findViewHolderForLayoutPosition(position);
    }



    public interface VoiceIsRead {
        void voiceOnClick(int position);
    }

    public interface DownloadFile {
        void onDownload(String fileUrl);
    }


    public interface AddCollectionListener{
        void addCollection(ChatMessageItem  chatMessageItem);
    }

    public void setAddCollectionListener(AddCollectionListener addCollectionListener) {
        this.onlongListener = addCollectionListener;
    }
}
