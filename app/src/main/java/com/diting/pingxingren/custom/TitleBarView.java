package com.diting.pingxingren.custom;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.news.listener.OnMClickListener;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.util.ScreenUtil;


public class TitleBarView extends RelativeLayout implements View.OnClickListener {

	private static final String TAG = "TitleBarView";
	private Context mContext;
	private Button btnLeft;
	private Button btnRight;
	private Button btn_titleLeft;
	private Button btn_titleRight;
	private TextView tv_center;
	private LinearLayout common_tab;
	private PopWinSubmenu popWinSubmenu;
	private RelativeLayout rl_main;
	private View point;
	private OnMClickListener mClickListener;

	public TitleBarView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		point = findViewById(R.id.point);
		btnLeft = (Button) findViewById(R.id.title_btn_left);
		btnRight = (Button) findViewById(R.id.title_btn_right);
		btn_titleLeft = (Button) findViewById(R.id.concerned);
		btn_titleRight = (Button) findViewById(R.id.ranking_list);
		tv_center = (TextView) findViewById(R.id.title_txt);
		common_tab = (LinearLayout) findViewById(R.id.common_tab);
		rl_main = (RelativeLayout)findViewById(R.id.rl_main);
	}

	public void setCommonTitle(int LeftVisibility, int centerVisibility, int commonTabVisibiltey, int rightVisibility) {
		btnLeft.setVisibility(LeftVisibility);
		btnRight.setVisibility(rightVisibility);
		tv_center.setVisibility(centerVisibility);
		common_tab.setVisibility(commonTabVisibiltey);
	}

	/**
	 * 初始化标题栏
	 * @param isBack 是否显示返回按钮
	 * @param title 标题文字
	 * @param rightText 标题右侧文字
	 * @param listener 右侧按钮监听
	 */
	public void initTitle(boolean isBack, String title, String rightText, OnMClickListener listener) {
		common_tab.setVisibility(GONE);
		if (isBack) {
			Drawable img_off;
			Resources res = getResources();
			img_off = res.getDrawable(R.drawable.ic_back);
			img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
			btnLeft.setCompoundDrawables(img_off, null, null, null);
			btnLeft.setOnClickListener(this);
		}
		if (StringUtil.isNotEmpty(title)) tv_center.setText(title);
		if (StringUtil.isNotEmpty(rightText)) {
			btnRight.setText(rightText);
			btnRight.setOnClickListener(this);
			this.mClickListener = listener;
		}
	}

	public void showPoint(){
		point.setVisibility(VISIBLE);
	}

	public void hidePoint(){
		point.setVisibility(GONE);
	}

	public void setBtnLeft(int icon, String txtRes,int h) {
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = ScreenUtil.dip2px(mContext, h);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnLeft.setText(txtRes);
		btnLeft.setCompoundDrawables(img, null, null, null);
	}

	public void setBtnLeft(int icon, String txtRes) {
		setBtnLeft(icon,txtRes,20);
	}

	public void setBtnLeftText(String txtRes) {
		btnLeft.setText(txtRes);
	}

	public void setBtnLeftText(int txtRes) {
		btnLeft.setText(txtRes);
	}

	public void setBtnRight(int icon, String txtRes) {
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = ScreenUtil.dip2px(mContext, 30);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnRight.setText(txtRes);
		btnRight.setCompoundDrawables(null, null, img, null);
	}

	public void setBtnRightText(String txtRes) {
		btnRight.setText(txtRes);
	}

	public void setBtnRightText(int txtRes) {
		btnRight.setText(txtRes);
	}

	public void setTitleLeft(int resId) {
		btn_titleLeft.setText(resId);
	}

	public void setTitleLeft(String resId) {
		btn_titleLeft.setText(resId);
	}

	public void setTitleRight(int resId) {
		btn_titleRight.setText(resId);
	}

	public void setTitleRight(String resId) {
		btn_titleRight.setText(resId);
	}

	public void setTitleText(int txtRes) {
		tv_center.setText(txtRes);
	}

	public void setTitleText(String txtRes) {
		tv_center.setText(txtRes);
	}

	public void setBtnLeftOnclickListener(OnClickListener listener) {
		btnLeft.setOnClickListener(listener);
	}

	public void setBtnRightOnclickListener(OnClickListener listener) {
		btnRight.setOnClickListener(listener);
	}

	public Button getBtnLeft() {
		return btnLeft;
	}

	public Button getBtnRight() {
		return btnRight;
	}

	public Button getTitleLeft() {
		return btn_titleLeft;
	}

	public Button getTitleRight() {
		return btn_titleRight;
	}

	public void destoryView() {
		btnLeft.setText(null);
		btnRight.setText(null);
		tv_center.setText(null);
	}

	public void addSubmenu(View v) {
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popWinSubmenu == null) {
					//自定义的单击事件
					popWinSubmenu = new PopWinSubmenu((Activity)mContext, ScreenUtil.dip2px(mContext, 120), ScreenUtil.dip2px(mContext, 200));
					//监听窗口的焦点事件，点击窗口外面则取消显示
					popWinSubmenu.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (!hasFocus) {
								popWinSubmenu.dismiss();
							}
						}
					});
				}
//设置默认获取焦点
				popWinSubmenu.setFocusable(true);
//以某个控件的x和y的偏移量位置开始显示窗口
				popWinSubmenu.showAsDropDown(v, -ScreenUtil.dip2px(mContext, 30), ScreenUtil.dip2px(mContext, 4));
//如果窗口存在，则更新
				popWinSubmenu.update();
			}
		});
	}
	public void setBgColor(int color){
		rl_main.setBackgroundColor(color);
}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_btn_right:
				if(mClickListener != null){
					mClickListener.onMClick(R.id.title_btn_right);
				}
				break;
			case R.id.title_btn_left:
				((Activity)mContext).finish();
				break;
		}
	}
}
