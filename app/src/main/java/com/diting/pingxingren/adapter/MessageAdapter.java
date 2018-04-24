package com.diting.pingxingren.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.FoodDetailActivity;
import com.diting.pingxingren.custom.PlayView;
import com.diting.pingxingren.entity.FoodInfo;
import com.diting.pingxingren.entity.Msg;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.MediaManager;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.Utils;
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

import static com.diting.pingxingren.entity.Msg.TYPE_RECEIVED_AUDIO;
import static com.diting.pingxingren.entity.Msg.TYPE_RECEIVED_FILE;
import static com.diting.pingxingren.entity.Msg.TYPE_RECEIVED_VIDEO;

/**
 * Created by asus on 2016/4/11.
 */
public class MessageAdapter extends BaseAdapter {
    private static final String TAG = "MessageAdapter";
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Msg> mMsgList;
    private static final int TYPE_RECEIVED_TEXT = 0;
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED_IMAGE = 2;
    private static final int TYPE_RECEIVED_TEXT_AND_IMAGE = 3;
    private static final int TYPE_RECEIVED_BOOK = 4;
    private static final int TYPE_SEND_VOICE = 5;
    private static final int TYPE_RECEIVED_VOICE = 6;
    private static final int TYPE_RECEIVED_MUSIC = 7;
    private static final int TYPE_RECEIVED_REMIND = 8;
    private static final int TYPE_RECEIVED_FOOD = 9;
    private static final int TYPE_PLAY = 10;
    private int voicePlayPosition = -1;
    private VoiceIsRead voiceIsRead;
    private List<String> unReadPosition = new ArrayList<String>();
    private SpeechSynthesizer mTts;
    private View.OnLongClickListener onLongClickListener;
    private View.OnClickListener onRightClickListener;
    private View.OnClickListener onLeftClickListener;
    private Map<Integer, Boolean> musicMap = new HashMap<Integer, Boolean>();
    private boolean isMine = false;
//    private static final int EDIT_PERSON = 1;
//    private static final int EDIT_FOOD = 2;
//    private static final int HANDLER_UPDATE_TEXT = 1;
//    private String persons;
//    private String foods;
//    private String num;
//    private SparseArray<MyThread> threadArray = new SparseArray<MyThread>();
//    private SparseArray<PlayEatInfo> playArray = new SparseArray<PlayEatInfo>();

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    public MessageAdapter(Context context, List<Msg> msgList) {
        this.mContext = context;
        this.mMsgList = msgList;
        this.mInflater = LayoutInflater.from(context);
        WindowManager wManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mTts = SpeechSynthesizer.createSynthesizer(context, mInitListener);
    }

    private void setParams() {
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置使用云端引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, MySharedPreferences.getVoicePeople());
    }


    public void setMessageList(List<Msg> msgList) {
        mMsgList = msgList;
        notifyDataSetChanged();
    }

    public void upDateMsg(Msg msg) {
        mMsgList.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 11;
    }

    @Override
    public int getItemViewType(int position) {
        //获取当前布局的数据
        Msg msg = mMsgList.get(position);
        switch (msg.getType()) {
            case Msg.TYPE_RECEIVED_TEXT:
                return TYPE_RECEIVED_TEXT;
            case Msg.TYPE_SENT:
                return TYPE_SENT;
            case Msg.TYPE_RECEIVED_IMAGE:
                return TYPE_RECEIVED_IMAGE;
            case Msg.TYPE_RECEIVED_TEXT_AND_IMAGE:
                return TYPE_RECEIVED_TEXT_AND_IMAGE;
            case Msg.TYPE_RECEIVED_BOOK:
                return TYPE_RECEIVED_BOOK;
            case Msg.TYPE_SEND_VOICE:
                return TYPE_SEND_VOICE;
            case Msg.TYPE_RECEIVED_VOICE:
                return TYPE_RECEIVED_VOICE;
            case Msg.TYPE_RECEIVED_MUSIC:
                return TYPE_RECEIVED_MUSIC;
            case Msg.TYPE_RECEIVED_REMIND:
                return TYPE_RECEIVED_REMIND;
            case TYPE_RECEIVED_FOOD:
                return TYPE_RECEIVED_FOOD;
            case Msg.TYPE_RECEIVED_MAIL:
                return TYPE_RECEIVED_TEXT;
            case Msg.TYPE_PLAY:
                return TYPE_PLAY;
            /*case TYPE_RECEIVED_VIDEO:
                return TYPE_RECEIVED_VIDEO;
            case TYPE_RECEIVED_FILE:
                return TYPE_RECEIVED_FILE;
            case TYPE_RECEIVED_AUDIO:
                return TYPE_RECEIVED_AUDIO;*/
            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Msg msg = mMsgList.get(position);
        switch (getItemViewType(position)) {
            case TYPE_RECEIVED_TEXT:
                ReceivedViewHolder holder;
                if (view == null) {
                    holder = new ReceivedViewHolder();
                    view = mInflater.inflate(R.layout.item_msg_robot, null);
                    holder.msg = (TextView) view.findViewById(R.id.left_msg);
                    holder.msg.setTextSize(MySharedPreferences.getFontSize());
                    holder.img = (ImageView) view.findViewById(R.id.image);
                    holder.head_img = (CircleImageView) view.findViewById(R.id.icon_left);
                    holder.ll_message = (LinearLayout) view.findViewById(R.id.ll_message);
                    view.setTag(holder);
                } else {
                    holder = (ReceivedViewHolder) view.getTag();
                }
                fromReceivedLayout((ReceivedViewHolder) holder, msg, position);
                break;
            case TYPE_SENT:
                SendViewHolder holder1;
                if (view == null) {
                    holder1 = new SendViewHolder();
                    view = mInflater.inflate(R.layout.item_msg_mine, null);
                    holder1.tv_time = (TextView) view.findViewById(R.id.tv_time);
                    holder1.msg = (TextView) view.findViewById(R.id.right_msg);
                    holder1.msg.setTextSize(MySharedPreferences.getFontSize());
                    holder1.head_img = (CircleImageView) view.findViewById(R.id.icon_right);
                    view.setTag(holder1);
                } else {
                    holder1 = (SendViewHolder) view.getTag();
                }
                fromSendLayout((SendViewHolder) holder1, msg, position);
                break;
            case TYPE_RECEIVED_IMAGE:
                ReceivedViewHolder holder2;
                if (view == null) {
                    holder2 = new ReceivedViewHolder();
                    view = mInflater.inflate(R.layout.item_msg_robot, null);
                    holder2.img = (ImageView) view.findViewById(R.id.image);
                    holder2.head_img = (CircleImageView) view.findViewById(R.id.icon_left);
                    holder2.msg = (TextView) view.findViewById(R.id.left_msg);
                    holder2.msg.setTextSize(MySharedPreferences.getFontSize());
                    holder2.ll_message = (LinearLayout) view.findViewById(R.id.ll_message);
                    view.setTag(holder2);
                } else {
                    holder2 = (ReceivedViewHolder) view.getTag();
                }
                fromReceivedLayout((ReceivedViewHolder) holder2, msg, position);
                break;


            case TYPE_RECEIVED_TEXT_AND_IMAGE:
                ReceivedViewHolder holder3;
                if (view == null) {
                    holder3 = new ReceivedViewHolder();
                    view = mInflater.inflate(R.layout.item_msg_robot, null);
                    holder3.img = (ImageView) view.findViewById(R.id.image);
                    holder3.msg = (TextView) view.findViewById(R.id.left_msg);
                    holder3.msg.setTextSize(MySharedPreferences.getFontSize());
                    holder3.head_img = (CircleImageView) view.findViewById(R.id.icon_left);
                    holder3.ll_message = (LinearLayout) view.findViewById(R.id.ll_message);
                    view.setTag(holder3);
                } else {
                    holder3 = (ReceivedViewHolder) view.getTag();
                }
                fromReceivedLayout((ReceivedViewHolder) holder3, msg, position);
                break;
            case TYPE_RECEIVED_BOOK:
                BookViewHolder holder4;
                if (view == null) {
                    holder4 = new BookViewHolder();
                    view = mInflater.inflate(R.layout.item_msg_book, null);
                    holder4.img = (ImageView) view.findViewById(R.id.image);
                    holder4.msg = (TextView) view.findViewById(R.id.left_msg);
                    holder4.msg.setTextSize(MySharedPreferences.getFontSize());
                    holder4.head_img = (CircleImageView) view.findViewById(R.id.icon_left);
                    holder4.book_name = (TextView) view.findViewById(R.id.tv_book_name);
                    holder4.book_author = (TextView) view.findViewById(R.id.tv_book_author);
                    holder4.book_partner = (TextView) view.findViewById(R.id.tv_book_partner);
                    holder4.book_price = (TextView) view.findViewById(R.id.tv_book_price);
                    holder4.book_profile = (TextView) view.findViewById(R.id.tv_book_profile);
                    view.setTag(holder4);
                } else {
                    holder4 = (BookViewHolder) view.getTag();
                }
                fromBookLayout((BookViewHolder) holder4, msg, position);
                break;
            case TYPE_SEND_VOICE:
                SendVoiceViewHolder holder5;
                if (view == null) {
                    holder5 = new SendVoiceViewHolder();
                    view = mInflater.inflate(R.layout.item_voice_send, null);
                    holder5.head_img = (CircleImageView) view.findViewById(R.id.icon_right);
                    holder5.tv_time = (TextView) view.findViewById(R.id.tv_time);
                    holder5.voice_time = (TextView) view.findViewById(R.id.voice_time);
                    holder5.recorder_anim = view.findViewById(R.id.recorder_anim);
                    holder5.voice_group = (LinearLayout) view.findViewById(R.id.voice_group);
                    holder5.voice_image = (LinearLayout) view.findViewById(R.id.voice_image);
                    holder5.tv_question = (TextView) view.findViewById(R.id.tv_question);
                    view.setTag(holder5);
                } else {
                    holder5 = (SendVoiceViewHolder) view.getTag();
                }
                fromSendVoiceLayout(holder5, msg, position);
                break;
            case TYPE_RECEIVED_VOICE:
                ReceivedVoiceViewHolder holder6;
                if (view == null) {
                    holder6 = new ReceivedVoiceViewHolder();
                    view = mInflater.inflate(R.layout.item_voice_receive, null);
                    holder6.head_img = (CircleImageView) view.findViewById(R.id.icon_left);
                    holder6.voice_group = (LinearLayout) view.findViewById(R.id.voice_group);
                    holder6.voice_time = (TextView) view.findViewById(R.id.voice_time);
                    holder6.receiver_voice_unread = view.findViewById(R.id.receiver_voice_unread);
                    holder6.voice_image = (LinearLayout) view.findViewById(R.id.voice_receiver_image);
                    holder6.voice_anim = view.findViewById(R.id.receiver_recorder_anim);
                    holder6.tv_answer = (TextView) view.findViewById(R.id.tv_answer);
                    view.setTag(holder6);
                } else {
                    holder6 = (ReceivedVoiceViewHolder) view.getTag();
                }
                fromReceivedVoiceLayout(holder6, msg, position);
                break;
            case TYPE_RECEIVED_MUSIC:
                ReceivedMusicViewHolder holder7;
                if (view == null) {
                    holder7 = new ReceivedMusicViewHolder();
                    view = mInflater.inflate(R.layout.item_music_receive, null);
                    holder7.head_img = (CircleImageView) view.findViewById(R.id.icon_left);
                    holder7.iv_play = (ImageView) view.findViewById(R.id.iv_play);
                    holder7.tv_music_name = (TextView) view.findViewById(R.id.tv_music_name);
                    holder7.tv_singer_name = (TextView) view.findViewById(R.id.tv_singer_name);
                    view.setTag(holder7);
                } else {
                    holder7 = (ReceivedMusicViewHolder) view.getTag();
                }
                fromReceivedMusicLayout(holder7, msg, position);
                break;
            case TYPE_RECEIVED_REMIND:
                ReceivedRemindViewHolder holder8;
                if (view == null) {
                    holder8 = new ReceivedRemindViewHolder();
                    view = mInflater.inflate(R.layout.item_msg_remind, null);
                    holder8.layout_remind = (RelativeLayout) view.findViewById(R.id.layout_remind);
                    holder8.head_img = (CircleImageView) view.findViewById(R.id.icon_left);
                    holder8.tv_remind_content = (TextView) view.findViewById(R.id.tv_remind_content);
                    holder8.tv_remind_check = (TextView) view.findViewById(R.id.tv_remind_check);
                    view.setTag(holder8);
                } else {
                    holder8 = (ReceivedRemindViewHolder) view.getTag();
                }
                fromReceivedRemindLayout(holder8, msg, position);
                break;
            case TYPE_RECEIVED_FOOD:
                ReceivedFOODViewHolder holder9;
                if (view == null) {
                    holder9 = new ReceivedFOODViewHolder();
                    view = mInflater.inflate(R.layout.item_msg_food, null);
                    holder9.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                    view.setTag(holder9);
                } else {
                    holder9 = (ReceivedFOODViewHolder) view.getTag();
                }
                holder9.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                final List<FoodInfo> list = (ArrayList<FoodInfo>) new Gson().fromJson(msg.getContent(), new TypeToken<List<FoodInfo>>() {
                }.getType());
                FoodInfoAdapter adapter = new FoodInfoAdapter(R.layout.item_food, list);
//                RecyclerViewDivider divider = new RecyclerViewDivider(mContext,RecyclerViewDivider.VERTICAL);
//                divider.setmDividerHeight(8);
//                holder9.recyclerView.addItemDecoration(divider);
                holder9.recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mContext.startActivity(FoodDetailActivity.getCallingIntent(mContext, list.get(position)));
                    }
                });
                break;
            case TYPE_PLAY:
                PlayViewHolder holder10;
                if (view == null) {
                    holder10 = new PlayViewHolder();
                    view = mInflater.inflate(R.layout.item_message_play, parent, false);
                    holder10.playView = (PlayView) view.findViewById(R.id.play_view);
                    view.setTag(holder10);
                } else {
                    holder10 = (PlayViewHolder) view.getTag();
                }
                holder10.playView.setFoods(Utils.isEmpty(msg.getContent()) ? Const.defaultFoods : msg.getContent())
                        .setPersons(msg.getFromUserName())
                        .setNum(msg.getImgUrl());
//                if(view == null){
//                    holder10 = new PlayViewHolder();
//                    view = mInflater.inflate(R.layout.item_play,parent,false);
//                    holder10.tv_person = (TextView)view.findViewById(R.id.tv_person);
//                    holder10.tv_food = (TextView) view.findViewById(R.id.tv_food);
//                    holder10.iv_edit_person = (ImageView)view.findViewById(R.id.iv_edit_person);
//                    holder10.iv_edit_food = (ImageView)view.findViewById(R.id.iv_edit_food);
//                    holder10.btn_start = (Button)view.findViewById(R.id.btn_start);
//                    view.setTag(holder10);
//                }else {
//                    holder10 = (PlayViewHolder) view.getTag();
//                }
//                fromPlayLayout(holder10,msg,position);
                break;
            case TYPE_RECEIVED_VIDEO:
                final VideoViewHolder videoViewHolder;
                if (view == null) {
                    view = mInflater.inflate(R.layout.item_msg_video, parent, false);
                    videoViewHolder = new VideoViewHolder(view);
                    view.setTag(videoViewHolder);
                } else {
                    videoViewHolder = (VideoViewHolder) view.getTag();
                }

                String answer = msg.getContent();
                if (!Utils.isEmpty(answer))
                    videoViewHolder.mAnswerView.setText(answer);
                String videoUrl = msg.getVideoUrl();
                if (!Utils.isEmpty(videoUrl)) {
                    videoViewHolder.mVideoView.setVideoURI(Uri.parse(videoUrl));
                    videoViewHolder.mVideoView.setMediaController(new MediaController(mContext));
                    videoViewHolder.mPlayArea.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            videoViewHolder.mVideoView.start();
                            videoViewHolder.mPlayArea.setVisibility(View.GONE);
                        }
                    });
                }
                break;
            case TYPE_RECEIVED_AUDIO:
                break;
            case TYPE_RECEIVED_FILE:
                break;
            default:
                break;
        }
        return view;
    }

//    private void fromPlayLayout(final PlayViewHolder holder, final Msg msg, final int position) {
//        if(!Utils.isEmpty(msg.getFromUserName())){
//            persons = msg.getFromUserName();
//        }
//        if(!Utils.isEmpty(msg.getContent())){
//            foods = msg.getContent();
//        }else {
//            foods = Const.defaultFoods;
//        }
//
//        PlayEatInfo eatInfo = playArray.get(position);
//        if(eatInfo!=null){
//            holder.tv_person.setText(eatInfo.getPerson());
//            holder.tv_food.setText(eatInfo.getFood());
//        }else {
//            holder.tv_person.setText(R.string.tv_person);
//            holder.tv_food.setText(R.string.tv_food);
//        }
//
//        num = msg.getImgUrl();
//        holder.iv_edit_person.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupWindow(holder,EDIT_PERSON);
//            }
//        });
//        holder.iv_edit_food.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupWindow(holder,EDIT_FOOD);
//            }
//        });
//        holder.btn_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(holder.btn_start.getText().toString().equals("开始")) {
//                    if (Utils.isEmpty(persons)) {
//                        Toast.makeText(mContext, "请先编辑参与的人", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (Utils.isEmpty(foods)) {
//                        Toast.makeText(mContext, "请先编辑要吃什么", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if(threadArray.get(position)==null) {
//                        MyThread mThread = new MyThread((Activity) mContext, holder,position);
//                        threadArray.append(position, mThread);
//                    }
//                    threadArray.get(position).start();
//                    holder.btn_start.setText("结束");
//                }else {
//                    threadArray.get(position).interrupt();
//                    //mThread.interrupt();
//                    holder.btn_start.setText("开始");
//                }
//            }
//        });
//    }
//
//    private class MyThread extends Thread {
//        private Activity activity;
//        private PlayViewHolder holder;
//        private int position;
//
//        public MyThread(Activity activity,PlayViewHolder holder,int position){
//            this.activity = activity;
//            this.holder = holder;
//            this.position = position;
//        }
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    Thread.sleep(50);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            holder.tv_person.setText(Utils.getRandomString(persons.split(" ")));
//                            holder.tv_food.setText(Utils.getRandomString(foods.split(" ")));
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            String person = Utils.getRandomString(persons.split(" ")) + "请";
//                            String food = "吃" + Utils.getRandomString(foods.split(" "));
//                            playArray.append(position,new PlayEatInfo(person,food));
//                            holder.tv_person.setText(person);
//                            holder.tv_food.setText(food);
//                        }
//                    });
//
//                    break;
//                }
//            }
//        }
//    }
//
//    private void showPopupWindow(final PlayViewHolder holder,final int editType) {
//        final Activity activity = (Activity)mContext;
//        View parent = activity.getWindow().getDecorView();
//        View popView = View.inflate(activity, R.layout.popupwin_play, null);
//        final EditText et_content = (EditText) popView.findViewById(R.id.et_content);
//        Button btn_commit = (Button)popView.findViewById(R.id.btn_commit);
//        TextView tv_close = (TextView)popView.findViewById(R.id.tv_close);
//        if(editType == EDIT_FOOD && !Utils.isEmpty(foods)){
//            et_content.setText(foods);
//            et_content.setSelection(foods.length());
//        }else if(editType == EDIT_PERSON && !Utils.isEmpty(persons)){
//            et_content.setText(persons);
//            et_content.setSelection(persons.length());
//        }
//        final PopupWindow popupWindow = new PopupWindow(popView, parent.getWidth() - ScreenUtil.dip2px(activity,50), ViewGroup.LayoutParams.WRAP_CONTENT);
//        ColorDrawable dw = new ColorDrawable(0x00000000);
//        popupWindow.setBackgroundDrawable(dw);
//        popupWindow.setClippingEnabled(false);
//        popupWindow.setFocusable(true);
//        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);//设置允许在外点击消失
//        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
//        //跟随键盘移动
//        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
//        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//        lp.alpha=0.5f;
//        activity.getWindow().setAttributes(lp);
//        btn_commit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(editType == EDIT_FOOD){
//                    foods = et_content.getText().toString();
//                }else {
//                    persons = et_content.getText().toString();
//                }
//                Diting.setGameInfo(persons, foods, num, new HttpCallBack() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        popupWindow.dismiss();
//                    }
//
//                    @Override
//                    public void onFailed(VolleyError error) {
//                        popupWindow.dismiss();
//                    }
//                });
//            }
//        });
//        tv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                activity.getWindow().setAttributes(lp);
//            }
//        });
//    }


    private void setLeftHeadImage(final CircleImageView imageView, Msg msg) {
        if (isMine) {
            if (!MySharedPreferences.getLeftHeadImage().equals("")) {
                Glide.with(mContext).load(MySharedPreferences.getLeftHeadImage()).into(imageView);
            }
            return;
        }
        final String head_img = msg.getHeadPortrait();
        if (!Utils.isEmpty(head_img)) {
            Glide.with(mContext).load(head_img).into(imageView);
//            Diting.loadImageFromNetwork(head_img, new ImageCallBack() {
//                @Override
//                public void onSuccess(Bitmap response) {
//                    imageView.setImageBitmap(ImageUtil.toRoundBitmap(response, null));
//                }
//
//                @Override
//                public void onFailed(VolleyError error) {
//                    imageView.setImageResource(R.mipmap.icon_left);
//                }
//            });
        } else {
            imageView.setImageResource(R.mipmap.icon_left);
        }

    }

    private void fromReceivedRemindLayout(final ReceivedRemindViewHolder holder8, Msg msg, int position) {
        setLeftHeadImage(holder8.head_img, msg);
        holder8.head_img.setOnClickListener(onLeftClickListener);
        holder8.tv_remind_content.setText(msg.getContent());
        holder8.layout_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setData(CalendarContract.Reminders.CONTENT_URI);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                       /* | Intent.FLAG_ACTIVITY_TASK_ON_HOME*/);
                intent.setDataAndType(Uri.parse("content://com.android.calendar/"), "time/epoch");
                mContext.startActivity(intent);
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//                    mContext.startActivity(new Intent(AlarmClock.ACTION_SHOW_ALARMS));
//                }else {
//                    mContext.startActivity(new Intent(AlarmClock.ACTION_SET_ALARM));
//                }
            }
        });
    }


    private void fromSendLayout(final SendViewHolder holder1, final Msg msg, int position) {
        ImageUtil.setRightHeadImage(mContext, holder1.head_img);
        holder1.head_img.setOnClickListener(onRightClickListener);
        holder1.tv_time.setText(TimeUtil.millis2String(msg.getTime(), TimeUtil.PATTERN_YMD_HM1));
        if (position > 0) {
            if ((msg.getTime() - mMsgList.get(position - 1).getTime()) / 1000 > 60) {
                holder1.tv_time.setVisibility(View.VISIBLE);
            } else {
                holder1.tv_time.setVisibility(View.GONE);
            }
        } else {
            holder1.tv_time.setVisibility(View.VISIBLE);
        }
        holder1.msg.setText(msg.getContent());
        holder1.msg.setTextSize(MySharedPreferences.getFontSize());
        Utils.addUrl(mContext, holder1.msg);
        holder1.msg.setOnLongClickListener(onLongClickListener);
//        holder1.msg.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                EventBus.getDefault().post(msg);
//                return false;
//            }
//        });
    }


    private void fromReceivedLayout(final ReceivedViewHolder holder, final Msg msg, int position) {
        setLeftHeadImage(holder.head_img, msg);
        holder.head_img.setOnClickListener(onLeftClickListener);
        holder.msg.setTextSize(MySharedPreferences.getFontSize());
        switch (msg.getType()) {
            case Msg.TYPE_RECEIVED_IMAGE:
                holder.ll_message.setBackgroundResource(R.drawable.message_left);
                holder.msg.setVisibility(View.GONE);
                holder.img.setVisibility(View.VISIBLE);
                //Diting.loadImgByVolley(msg.getImgUrl(),holder.img);
                Glide.with(mContext).load(msg.getImgUrl()).into(holder.img);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageUtil.gotoDetail((Activity) mContext, msg.getImgUrl(), v);
                    }
                });
                break;
            case Msg.TYPE_RECEIVED_TEXT:
                holder.ll_message.setBackgroundResource(R.drawable.message_left);
                //holder.msg.setText(msg.getContent());
                if (msg.getContent() != null) {
                    holder.msg.setText(Utils.fromHtml(msg.getContent()));
                    Utils.addUrl(mContext, holder.msg);
                }
                holder.msg.setOnLongClickListener(onLongClickListener);
                break;
            case Msg.TYPE_RECEIVED_TEXT_AND_IMAGE:
                holder.ll_message.setBackgroundResource(R.drawable.message_left);
                holder.img.setVisibility(View.VISIBLE);
                //Diting.loadImgByVolley(msg.getImgUrl(),holder.img);
                Glide.with(mContext).load(msg.getImgUrl()).into(holder.img);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageUtil.gotoDetail((Activity) mContext, msg.getImgUrl(), v);
                    }
                });
                //holder.msg.setText(msg.getContent());
                if (msg.getContent() != null) {
                    holder.msg.setText(Utils.fromHtml(msg.getContent()));
                    Utils.addUrl(mContext, holder.msg);
                }
                holder.msg.setOnLongClickListener(onLongClickListener);
                break;
            case Msg.TYPE_RECEIVED_MAIL:
                holder.ll_message.setBackgroundResource(R.drawable.message_mail);
                if (msg.getContent() != null) {
                    holder.msg.setText(Utils.fromHtml(msg.getContent()));
                    Utils.addUrl(mContext, holder.msg);
                }
                holder.msg.setOnLongClickListener(onLongClickListener);
                break;
            default:
                break;
        }
    }

    private void fromBookLayout(final BookViewHolder holder, Msg msg, int position) {
        final String head_img = msg.getHeadPortrait();
        setLeftHeadImage(holder.head_img, msg);
        if (Utils.isEmpty(msg.getBook().getPicture())) {
            holder.img.setImageResource(R.mipmap.icon_load_failed);
        } else {
            //Diting.loadImgByVolley(msg.getBook().getPicture(), holder.img);
            Glide.with(mContext).load(msg.getBook().getPicture()).into(holder.img);
        }
        holder.msg.setText(Html.fromHtml(msg.getContent()).toString());
        holder.msg.setTextSize(MySharedPreferences.getFontSize());
        holder.book_name.setText(msg.getBook().getName());
        holder.book_author.setText("作者: " + msg.getBook().getAuthor());
        holder.book_partner.setText("分享者: " + msg.getBook().getShare());
        holder.book_price.setText("价格: " + msg.getBook().getPrice());
        holder.book_profile.setText("简介: " + msg.getBook().getSynopsis());
    }

    private void fromSendVoiceLayout(final SendVoiceViewHolder holder, final Msg msg, final int position) {
        ImageUtil.setRightHeadImage(mContext, holder.head_img);
        holder.head_img.setOnClickListener(onRightClickListener);
        holder.tv_time.setText(TimeUtil.millis2String(msg.getTime(), TimeUtil.PATTERN_YMD_HM1));
        holder.tv_question.setText(msg.getContent());
        Utils.addUrl(mContext, holder.tv_question);
        holder.tv_question.setOnLongClickListener(onLongClickListener);
        if (position > 0) {
            if ((msg.getTime() - mMsgList.get(position - 1).getTime()) / 1000 > 60) {
                holder.tv_time.setVisibility(View.VISIBLE);
            } else {
                holder.tv_time.setVisibility(View.GONE);
            }
        } else {
            holder.tv_time.setVisibility(View.VISIBLE);
        }
        AnimationDrawable drawable;
        holder.recorder_anim.setId(position);
        if (position == voicePlayPosition) {
            holder.recorder_anim.setBackgroundResource(R.mipmap.adj);
            holder.recorder_anim
                    .setBackgroundResource(R.drawable.voice_play_send);
            drawable = (AnimationDrawable) holder.recorder_anim
                    .getBackground();
            drawable.start();
        } else {
            holder.recorder_anim.setBackgroundResource(R.mipmap.adj);
        }
        holder.voice_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                holder.recorder_anim.setBackgroundResource(R.mipmap.adj);
                stopPlayVoice();
                voicePlayPosition = holder.recorder_anim.getId();
                AnimationDrawable drawable;
                holder.recorder_anim
                        .setBackgroundResource(R.drawable.voice_play_send);
                drawable = (AnimationDrawable) holder.recorder_anim
                        .getBackground();
                drawable.start();
                String voicePath = msg.getVoicePath() == null ? "" : msg.getVoicePath();
                if (voiceIsRead != null) {
                    voiceIsRead.voiceOnClick(position);
                }
                MediaManager.playSound(voicePath,
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                voicePlayPosition = -1;
                                holder.recorder_anim.setBackgroundResource(R.mipmap.adj);
                            }
                        }, null);
//                MyMediaManager.PlayRecord(voicePath, new MyMediaManager.ICompleteListener() {
//                    @Override
//                    public void onComplete() {
//                        voicePlayPosition = -1;
//                        holder.recorder_anim.setBackgroundResource(R.mipmap.adj);
//                    }
//                });
            }

        });
        float voiceTime = msg.getVoiceTime();
        BigDecimal b = new BigDecimal(voiceTime);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.voice_time.setText(f1 + "\"");
//        ViewGroup.LayoutParams lParams = holder.voice_image
//                .getLayoutParams();
//        lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f
//                * (voiceTime < 60f ? voiceTime : 60f));
//        holder.voice_image.setLayoutParams(lParams);
    }


    private void fromReceivedVoiceLayout(final ReceivedVoiceViewHolder holder, final Msg msg, final int position) {
        setLeftHeadImage(holder.head_img, msg);
        holder.head_img.setOnClickListener(onLeftClickListener);
        if (holder.receiver_voice_unread != null)
            holder.receiver_voice_unread.setVisibility(View.GONE);
        if (holder.receiver_voice_unread != null && unReadPosition != null) {
            for (String unRead : unReadPosition) {
                if (unRead.equals(position + "")) {
                    holder.receiver_voice_unread
                            .setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        final String answer = Utils.fromHtml(msg.getContent());
        holder.tv_answer.setText(answer);
        Utils.addUrl(mContext, holder.tv_answer);
        holder.tv_answer.setOnLongClickListener(onLongClickListener);
        AnimationDrawable drawable;
        holder.voice_anim.setId(position);
        if (position == voicePlayPosition) {
            holder.voice_anim
                    .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
            holder.voice_anim
                    .setBackgroundResource(R.drawable.voice_play_receiver);
            drawable = (AnimationDrawable) holder.voice_anim
                    .getBackground();
            drawable.start();
        } else {
            holder.voice_anim
                    .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
        }
        holder.voice_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (holder.receiver_voice_unread != null)
                    holder.receiver_voice_unread.setVisibility(View.GONE);
                holder.voice_anim
                        .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                stopPlayVoice();
                voicePlayPosition = holder.voice_anim.getId();
                AnimationDrawable drawable;
                holder.voice_anim
                        .setBackgroundResource(R.drawable.voice_play_receiver);
                drawable = (AnimationDrawable) holder.voice_anim
                        .getBackground();
                drawable.start();
                if (voiceIsRead != null) {
                    voiceIsRead.voiceOnClick(position);
                }
                speak(answer, holder);

            }

        });
        float voiceTime = msg.getContent().length() * 0.3f;
        BigDecimal b = new BigDecimal(voiceTime);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.voice_time.setText(f1 + "\"");
//        ViewGroup.LayoutParams lParams = holder.voice_image
//                .getLayoutParams();
//        lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f
//                * (voiceTime < 60f ? voiceTime : 60f));
//        holder.voice_image.setLayoutParams(lParams);
    }

    private void fromReceivedMusicLayout(final ReceivedMusicViewHolder holder, final Msg msg, final int position) {
        setLeftHeadImage(holder.head_img, msg);
        holder.head_img.setOnClickListener(onLeftClickListener);
        final String[] music = msg.getContent().split(",");
        holder.tv_music_name.setText(music[0]);
        holder.tv_singer_name.setText(music[1]);
        Boolean isPlaying = musicMap.containsKey(position) ? musicMap.get(position) : false;
        if (isPlaying) {
            holder.iv_play.setImageResource(R.mipmap.icon_stop);
        } else {
            holder.iv_play.setImageResource(R.mipmap.icon_play);
        }
        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusicState(position);
//                final Boolean isPlaying = musicMap.containsKey(position) ? musicMap.get(position) : false;
//                if(isPlaying){
//                    holder.iv_play.setImageResource(R.mipmap.icon_play);
//                    MediaManager.pause();
//                    musicMap.clear();
//                    musicMap.put(position,false);
//                }else {
//                    holder.iv_play.setImageResource(R.mipmap.icon_stop);
//                    musicMap.clear();
//                    musicMap.put(position,true);
//                    MediaManager.playSound(msg.getVoicePath(), new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                musicMap.clear();
//                                musicMap.put(position,false);
//                                holder.iv_play.setImageResource(R.mipmap.icon_play);
//                            }
//                    });
//                }
//                notifyDataSetChanged();

            }
        });
    }

    public void stopMusic() {
        MediaManager.pause();
        musicMap.clear();
        notifyDataSetChanged();
    }

    public void speak(String text, final ReceivedVoiceViewHolder holder) {
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
                if (holder != null) {
                    voicePlayPosition = -1;
                    holder.voice_anim.setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
            }
        });
    }


    private static class SendViewHolder {
        TextView tv_time;
        TextView msg;
        CircleImageView head_img;
    }

    private static class ReceivedViewHolder {
        LinearLayout ll_message;
        TextView msg;
        ImageView img;
        CircleImageView head_img;
    }

    private static class BookViewHolder {
        TextView msg;
        ImageView img;
        CircleImageView head_img;
        TextView book_name;
        TextView book_author;
        TextView book_partner;
        TextView book_price;
        TextView book_profile;
    }

    private static class SendVoiceViewHolder {
        TextView tv_time;
        CircleImageView head_img;
        TextView voice_time;
        View recorder_anim;
        LinearLayout voice_group;
        LinearLayout voice_image;
        TextView tv_question;
    }

    private static class ReceivedVoiceViewHolder {
        CircleImageView head_img;
        LinearLayout voice_group;
        TextView voice_time;
        LinearLayout voice_image;
        View receiver_voice_unread;
        View voice_anim;
        TextView tv_answer;
    }

    private static class ReceivedMusicViewHolder {
        CircleImageView head_img;
        TextView tv_music_name;
        TextView tv_singer_name;
        ImageView iv_play;
    }

    private static class ReceivedRemindViewHolder {
        RelativeLayout layout_remind;
        CircleImageView head_img;
        TextView tv_remind_content;
        TextView tv_remind_check;
    }

    private static class ReceivedFOODViewHolder {
        RecyclerView recyclerView;
    }

    //    private static class PlayViewHolder{
//        TextView tv_person;
//        TextView tv_food;
//        ImageView iv_edit_person;
//        ImageView iv_edit_food;
//        Button btn_start;
//    }
    private static class PlayViewHolder {
        PlayView playView;
    }

    private static class VideoViewHolder {

        public final TextView mAnswerView;
        public final VideoView mVideoView;
        public final ImageView mPlayArea;

        public VideoViewHolder(View videoView) {
            mAnswerView = (TextView) videoView.findViewById(R.id.tvAnswer);
            mVideoView = (VideoView) videoView.findViewById(R.id.video_player_view);
            mPlayArea = (ImageView) videoView.findViewById(R.id.ivPlayArea);
        }
    }

    public void changeMusicState(final int position) {
        Msg msg = mMsgList.get(position);
        final Boolean isPlaying = musicMap.containsKey(position) ? musicMap.get(position) : false;
        View view = getView(position, null, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_play);
        if (isPlaying) {
            imageView.setImageResource(R.mipmap.icon_play);
            MediaManager.pause();
            musicMap.clear();
            musicMap.put(position, false);
        } else {
            imageView.setImageResource(R.mipmap.icon_stop);
            musicMap.clear();
            musicMap.put(position, true);
            MediaManager.playSound(msg.getVoicePath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    musicMap.clear();
                    musicMap.put(position, false);
                    imageView.setImageResource(R.mipmap.icon_play);
                }
            }, new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    musicMap.clear();
                    musicMap.put(position, false);
                    imageView.setImageResource(R.mipmap.icon_play);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "播放失败，请换一首", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
        notifyDataSetChanged();

    }


    public void stopPlayVoice() {
        if (voicePlayPosition != -1) {
            View voicePlay = (View) ((Activity) mContext)
                    .findViewById(voicePlayPosition);
            if (voicePlay != null) {
                if (getItemViewType(voicePlayPosition) == TYPE_RECEIVED_VOICE) {
                    voicePlay
                            .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                } else {
                    voicePlay.setBackgroundResource(R.mipmap.adj);
                }
            }
            MediaManager.pause();
            voicePlayPosition = -1;
        }
        mTts.stopSpeaking();
    }

    public List<String> getUnReadPosition() {
        return unReadPosition;
    }

    public void setUnReadPosition(List<String> unReadPosition) {
        this.unReadPosition = unReadPosition;
    }

    public interface VoiceIsRead {
        public void voiceOnClick(int position);
    }

    public void setVoiceIsReadListener(VoiceIsRead voiceIsRead) {
        this.voiceIsRead = voiceIsRead;
    }

    public View.OnLongClickListener getOnLongClickListener() {
        return onLongClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void destroy() {
        if (mTts != null) {
            mTts.destroy();
        }
//        if(mThread!=null&&!mThread.isInterrupted()){
//            mThread.interrupt();
//        }
//        for(int i = 0;i < threadArray.size();i++){
//            if(threadArray.get(i)!=null){
//                threadArray.get(i).interrupt();
//            }
//        }
//        threadArray.clear();
    }

    public void setOnRightClickListener(View.OnClickListener onClickListener) {
        this.onRightClickListener = onClickListener;
    }

    public void setOnLeftClickListener(View.OnClickListener onClickListener) {
        this.onLeftClickListener = onClickListener;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }


}
