package com.diting.pingxingren.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.entity.ChatMessageItem;
import com.diting.pingxingren.entity.Msg;
import com.diting.pingxingren.model.CollectionModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.AddCollectionObserver;

import java.util.List;

/**
 * Created by asus on 2017/4/12.
 */

public class PopupWinUtil {
    public static void showPopWindow(final String text, Object object) {
        final Activity activity;
        if (object instanceof Activity) {
            activity = (Activity) object;
        } else {
            activity = ((Fragment) object).getActivity();
        }
        View parent = activity.getWindow().getDecorView();
        LayoutInflater inflater = LayoutInflater.from(activity);//获取一个填充器
        View view = inflater.inflate(R.layout.popupwin_copy, null);//填充我们自定义的布局
        Button btn_copy = (Button) view.findViewById(R.id.btn_copy);
        Button btn_collection = (Button) view.findViewById(R.id.btn_collection);
        btn_collection.setVisibility(View.GONE);
        //创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
        final PopupWindow popWindow = new PopupWindow(view, 2 * parent.getWidth() / 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//设置PopupWindow的背景为一个空的Drawable对象，如果不设置这个，那么PopupWindow弹出后就无法退出了
        popWindow.setOutsideTouchable(true);//设置是否点击PopupWindow外退出PopupWindow
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText("copy", text));
                popWindow.dismiss();
                Toast.makeText(activity, "已复制", Toast.LENGTH_SHORT).show();
            }
        });
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.8f;//设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        activity.getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {//如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                activity.getWindow().setAttributes(params);
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public static void showAddCollPopWindow(ChatMessageItem chatMessageItem, final String chatrobotname, final String chatuniqueid, Object object) {
        final Activity activity;
        if (object instanceof Activity) {
            activity = (Activity) object;
        } else {
            activity = ((Fragment) object).getActivity();
        }
        View parent = activity.getWindow().getDecorView();
        LayoutInflater inflater = LayoutInflater.from(activity);//获取一个填充器
        View view = inflater.inflate(R.layout.popupwin_copy, null);//填充我们自定义的布局
        Button btn_copy = view.findViewById(R.id.btn_copy);
        Button btn_collection = view.findViewById(R.id.btn_collection);
        btn_copy.setVisibility(View.GONE);
        //创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
        final PopupWindow popWindow = new PopupWindow(view, 2 * parent.getWidth() / 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//设置PopupWindow的背景为一个空的Drawable对象，如果不设置这个，那么PopupWindow弹出后就无法退出了
        popWindow.setOutsideTouchable(true);//设置是否点击PopupWindow外退出PopupWindow
        String url = "";
        int sort = 0;    //1.音频  2.文件  3.  图片  4.视频  5.文本
        final String text = chatMessageItem.getContent();
        if (!Utils.isEmpty(chatMessageItem.getAudioUrl())) {
            btn_copy.setVisibility(View.GONE);
            sort = 1;
            url = chatMessageItem.getAudioUrl();
        } else if (!Utils.isEmpty(chatMessageItem.getFileUrl())) {
            btn_copy.setVisibility(View.GONE);
            sort = 2;
            url = chatMessageItem.getFileUrl();
        } else if (!Utils.isEmpty(chatMessageItem.getImgUrl())) {
            btn_copy.setVisibility(View.GONE);
            sort = 3;
            url = chatMessageItem.getImgUrl();
        } else if (!Utils.isEmpty(chatMessageItem.getVideoUrl())) {
            btn_copy.setVisibility(View.GONE);
            sort = 4;
            url = chatMessageItem.getVideoUrl();
        } else if (Utils.isEmpty(chatMessageItem.getAudioUrl())
                && Utils.isEmpty(chatMessageItem.getAudioUrl())
                && Utils.isEmpty(chatMessageItem.getAudioUrl())
                && Utils.isEmpty(chatMessageItem.getAudioUrl())) {
            sort = 5;
            url = "";
            btn_copy.setVisibility(View.VISIBLE);
        }


        final String finalUrl = url;
        final int finalSort = sort;
        btn_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                ApiManager.addToCollection(finalUrl, text, finalSort, chatrobotname, chatuniqueid, new AddCollectionObserver(new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        CollectionModel collectionModel= (CollectionModel) result;
                        ToastUtils.showShortToast("收藏成功");
                    }

                    @Override
                    public void onResult(List<?> resultList) {

                    }

                    @Override
                    public void onError(Object o) {

                    }
                }));

            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText("copy", text));
                popWindow.dismiss();
                Toast.makeText(activity, "已复制", Toast.LENGTH_SHORT).show();
            }
        });
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.8f;//设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        activity.getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {//如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                activity.getWindow().setAttributes(params);
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
}
