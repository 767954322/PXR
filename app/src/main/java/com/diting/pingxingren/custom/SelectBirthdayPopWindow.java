package com.diting.pingxingren.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.custom.wheelview.OnWheelChangedListener;
import com.diting.pingxingren.custom.wheelview.WheelView;
import com.diting.pingxingren.custom.wheelview.adapters.ArrayWheelAdapter;
import com.diting.pingxingren.util.BirthdayUtils;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.Utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 机器人设置界面的选择生日
 * Created by Administrator on 2017/11/16.
 */

public class SelectBirthdayPopWindow implements View.OnClickListener {

    private Context mCtx;
    //弹出框
    private PopupWindow mPopupWindow;
    //弹出view
    private View mPopupWindowView;
    //依赖view，在他下面
    private View mRootView;
    //取消
    private TextView tv_cancle;
    //确定
    private TextView tv_resure;

    //年月日的列表
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;

    private int[] mLocation = new int[2];
    private int mStartY;
    private int mScreenHeight;

    private boolean isLinter;
    private static int START_YEAR = 1942, END_YEAR = 2049;

    private Calendar calendar;

    String years[] = BirthdayUtils.getYera();
    String monthOfAlmanac[] = BirthdayUtils.monthOfAlmanac;
    String daysOfAlmanac[] = BirthdayUtils.daysOfAlmanac;


    int initial_year;
    int initial_month;
    int initial_day;

    public SelectBirthdayPopWindow(Context context, View mRootView) {
        mCtx = context;
        this.mRootView = mRootView;
        initPopWindow();
    }

    private void initPopWindow() {
        initView();
        //获取按键的位置
        mRootView.getLocationOnScreen(mLocation);
        mStartY = mLocation[1] + mRootView.getHeight();
        mScreenHeight = getScreenHeight((Activity) mCtx);

    }

    private void initView() {
        mPopupWindowView = View.inflate(mCtx, R.layout.popwindow_select_birthday, null);
        tv_cancle = (TextView) mPopupWindowView.findViewById(R.id.tv_cancle);
        tv_resure = (TextView) mPopupWindowView.findViewById(R.id.tv_resure);

        wv_year = (WheelView) mPopupWindowView.findViewById(R.id.wv_year);
        wv_month = (WheelView) mPopupWindowView.findViewById(R.id.wv_month);
        wv_day = (WheelView) mPopupWindowView.findViewById(R.id.wv_day);

        wv_year.setVisibleItems(5);
        wv_month.setVisibleItems(5);
        wv_day.setVisibleItems(5);

        wv_year.setCurrentItem(0);
        wv_month.setCurrentItem(0);
        wv_day.setCurrentItem(0);

        addListener();
    }


    public void show(String birthdar) {
        if (mPopupWindow == null) {
            initPopWindow();

            //设置弹框的大小  弹框位置在按钮以下，占据所有屏幕
            mPopupWindow = new PopupWindow(mPopupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x22000000));
            //mPopupWindow.setAnimationStyle(R.style.AnimationFade);
//            mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            calendar = Calendar.getInstance();
            if (Utils.isEmpty(birthdar)) {
                initial_year = calendar.get(Calendar.YEAR);
                initial_month = calendar.get(Calendar.MONTH);
                initial_day = calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                initial_year =Integer.parseInt(birthdar.split("[年]")[0]);
                String monthStr=birthdar.split("[年]")[1];
                initial_month = Integer.parseInt(monthStr.split("月")[0]);
                initial_day = Integer.parseInt(monthStr.split("月")[1].replace("日",""));
            }

            isLinter = true;
            initDateTimePicker(initial_year, initial_month, initial_day);
        }

        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            //设置弹框的位置
            mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
        }
    }

    public int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

    public void showlunarTimePicker() {
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setVisibleItems(5);
        wv_year.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, years));// 设置"年"的显示数据
        // 月
        wv_month.setCyclic(true);
        wv_month.setVisibleItems(5);
        wv_month.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, monthOfAlmanac));
        // 日
        wv_day.setCyclic(true);
        wv_day.setVisibleItems(5);
        wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, daysOfAlmanac));
    }
//
//    public void setTime() {
//
//
//
//        showlunarTimePicker();
//
//
//    }

    /**
     * @Description: TODO 弹出阳历日期时间选择器
     */
    public void initDateTimePicker(int year, int month, int day) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        // 年
//        wv_year.setLabel("年");// 添加文字
//        wv_year.setViewAdapter(new NumericWheelAdapter(mCtx,START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getYeraGregorian()));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        wv_year.setVisibleItems(5);
        // 月
        // wv_month = (WheelView) view.findViewById(R.id.month);
//        wv_month.setLabel("月");
//        wv_month.setViewAdapter(new NumericWheelAdapter(mC tx,1, 12));
        wv_month.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.monthsofGregorian));
        wv_month.setCyclic(true);
        wv_month.setCurrentItem(month - 1);
        wv_month.setVisibleItems(5);

        wv_day.setCyclic(true);

        if (isLinter) {

            if (list_big.contains(String.valueOf(month + 1))) {
//                wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 31));
                wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(31)));
            } else if (list_little.contains(String.valueOf(month + 1))) {
//                wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 30));
                wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(30)));
            } else {

                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
//                    wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 29));
                    wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(29)));
                else
//                    wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 28));
                    wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(28)));
            }
        }
        wv_day.setCurrentItem(day - 1);
        wv_day.setVisibleItems(5);

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (isLinter) {

                    int year_num = newValue + START_YEAR;
                    // 判断大小月及是否闰年,用来确定"日"的数据
                    if (list_big.contains(String.valueOf(wv_month
                            .getCurrentItem() + 1))) {
//                        wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 31));
                        wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(31)));
                    } else if (list_little.contains(String.valueOf(wv_month
                            .getCurrentItem() + 1))) {
//                        wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 30));
                        wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(30)));
                    } else {
                        if ((year_num % 4 == 0 && year_num % 100 != 0)
                                || year_num % 400 == 0)
//                            wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 29));
                            wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(29)));
                        else
//                            wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 28));
                            wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(28)));
                    }
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (isLinter) {


                    int month_num = newValue + 1;
                    // 判断大小月及是否闰年,用来确定"日"的数据
                    if (list_big.contains(String.valueOf(month_num))) {
//                        wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 31));
                        wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(31)));
                    } else if (list_little.contains(String.valueOf(month_num))) {
//                        wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 30));
                        wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(30)));
                    } else {
                        if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                                .getCurrentItem() + START_YEAR) % 100 != 0)
                                || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
//                            wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 29));
                            wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(29)));
                        else
//                            wv_day.setViewAdapter(new NumericWheelAdapter(mCtx,1, 28));
                            wv_day.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, BirthdayUtils.getDaysofGregorians(28)));
                    }
                }
            }
        };

        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);


    }

    //得到阳历的数据
    public String getYangTime() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + START_YEAR)).append("年")
                .append((wv_month.getCurrentItem() + 1)).append("月")
                .append((wv_day.getCurrentItem() + 1)).append("日");
        return sb.toString();
    }

    //得到阴历的数据
    public String getYinTmie() {
        StringBuffer sb = new StringBuffer();
        int monthPos = wv_month.getCurrentItem();
        int dayPos = wv_day.getCurrentItem();
        String month = monthOfAlmanac[monthPos];
        String day = daysOfAlmanac[dayPos];
        sb.append(translate((wv_year.getCurrentItem() + START_YEAR))).append("年")
                .append(month)
                .append(day).append("日");
        return sb.toString();
    }

    public int getYear() {

        return (wv_year.getCurrentItem() + START_YEAR);
    }

    public int getMonth() {
        return (wv_month.getCurrentItem() + 1);
    }

    public int getDay() {
        return (wv_day.getCurrentItem() + 1);
    }


    private void addListener() {
        tv_cancle.setOnClickListener(this);
        tv_resure.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle://取消
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.tv_resure://确定

//                if (iv_Lunar_calendar.isSelected()) {//阴历
//
//                    onTimeResultListener.onTimeResult(getYinTmie(),"yin");
////                    translate(getDay());
//
//                }else {//阳历
                onTimeResultListener.onTimeResult(getYangTime(), "yang");
//                }


                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;


        }
    }


    public interface OnTimeResultListener {
        void onTimeResult(String timeStr, String timeType);
    }

    private OnTimeResultListener onTimeResultListener;

    public void setOnTimeResultListener(OnTimeResultListener onTimeResultListener) {
        this.onTimeResultListener = onTimeResultListener;
    }

    public static String[] words = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    public static String translate(int n) {

        StringBuilder sb = new StringBuilder();
        if (n == 0) {
            sb.append(words[0]);
        } else if (n > 0) {
            int i = 0;
            while (n != 0) {
                ++i;
                int s = n % 10;
                sb.insert(0, words[s]);
                if (i % 4 == 0 && i != 0) {
                    sb.insert(0, "");
                }
                n /= 10;
            }
        }/*else{
            n=-n;
            int i=0;
            while(n!=0){
                ++i;
                int s=n%10;
                sb.insert(0,words[s]);
                if(i%4==0&&i!=0){
                    sb.insert(0," ");
                }
                n/=10;
            }
            sb.insert(0,"负 ");
        }*/
        return sb.toString();
    }


}
