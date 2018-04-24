package com.diting.pingxingren.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.Utils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;


public class PlayView extends RelativeLayout {

	private static final String TAG = "PlayView";
	private Context mContext;
	private TextView tv_person;
	private TextView tv_food;
	private ImageView iv_edit_person;
	private ImageView iv_edit_food;
	private Button btn_start;
	private static final int EDIT_PERSON = 1;
	private static final int EDIT_FOOD = 2;
	private String persons;
	private String foods;
	private String num;
	private MyThread myThread;

	public PlayView(Context context) {
		super(context);
		mContext = context;
		initView();
		initEvent();
	}

	public PlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
		initEvent();
	}


	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.item_play, this);
		tv_person = (TextView)findViewById(R.id.tv_person);
		tv_food = (TextView) findViewById(R.id.tv_food);
		iv_edit_person = (ImageView)findViewById(R.id.iv_edit_person);
		iv_edit_food = (ImageView)findViewById(R.id.iv_edit_food);
		btn_start = (Button)findViewById(R.id.btn_start);
	}

	private void initEvent(){
		iv_edit_person.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow(EDIT_PERSON);
			}
		});
		iv_edit_food.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow(EDIT_FOOD);
			}
		});
		btn_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(btn_start.getText().toString().equals("开始")) {
					if (Utils.isEmpty(persons)) {
						Toast.makeText(mContext, "请先编辑参与的人", Toast.LENGTH_SHORT).show();
						return;
					}
					if (Utils.isEmpty(foods)) {
						Toast.makeText(mContext, "请先编辑要吃什么", Toast.LENGTH_SHORT).show();
						return;
					}
					myThread = new MyThread();
					myThread.start();
					btn_start.setText("结束");
				}else {
					myThread.interrupt();
					myThread = null;
					btn_start.setText("开始");
				}
			}
		});
	}

	private void showPopupWindow(final int editType) {
		final Activity activity = (Activity)mContext;
		View parent = activity.getWindow().getDecorView();
		View popView = View.inflate(activity, R.layout.popupwin_play, null);
		final EditText et_content = (EditText) popView.findViewById(R.id.et_content);
		Button btn_commit = (Button)popView.findViewById(R.id.btn_commit);
		TextView tv_close = (TextView)popView.findViewById(R.id.tv_close);
		if(editType == EDIT_FOOD && !Utils.isEmpty(foods)){
			et_content.setText(foods);
			et_content.setSelection(foods.length());
		}else if(editType == EDIT_PERSON && !Utils.isEmpty(persons)){
			et_content.setText(persons);
			et_content.setSelection(persons.length());
		}
		final PopupWindow popupWindow = new PopupWindow(popView, parent.getWidth() - ScreenUtil.dip2px(activity,50), ViewGroup.LayoutParams.WRAP_CONTENT);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setClippingEnabled(false);
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);//设置允许在外点击消失
		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		//跟随键盘移动
		popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha=0.5f;
		activity.getWindow().setAttributes(lp);
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(editType == EDIT_FOOD){
					foods = et_content.getText().toString();
				}else {
					persons = et_content.getText().toString();
				}
				Diting.setGameInfo(persons, foods, num, new HttpCallBack() {
					@Override
					public void onSuccess(JSONObject response) {
						popupWindow.dismiss();
					}

					@Override
					public void onFailed(VolleyError error) {
						popupWindow.dismiss();
					}
				});
			}
		});
		tv_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
				lp.alpha = 1.0f;
				activity.getWindow().setAttributes(lp);
			}
		});
	}


	private class MyThread extends Thread {
//		private WeakReference<PlayView> playViewWeakReference;
//		public MyThread(PlayView playView){
//			playViewWeakReference = new WeakReference<PlayView>(playView);
//		}
		@Override
		public void run() {
			//final PlayView playView = playViewWeakReference.get();
			Activity activity = (Activity)mContext;
			while (true) {
				try {
					Thread.sleep(50);
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv_person.setText(Utils.getRandomString(persons.split(" ")));
							tv_food.setText(Utils.getRandomString(foods.split(" ")));
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String person = Utils.getRandomString(persons.split(" ")) + "请";
							String food = "吃" + Utils.getRandomString(foods.split(" "));
							tv_person.setText(person );
							tv_food.setText(food);
						}
					});
					break;
				}
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(myThread!=null&&!myThread.isInterrupted()){
			myThread.interrupt();
			myThread = null;
			btn_start.setText("开始");
			tv_person.setText(R.string.tv_person);
			tv_food.setText(R.string.tv_food);
		}
	}

	public PlayView setPersons(String persons) {
		this.persons = persons;
		return this;
	}

	public PlayView setFoods(String foods) {
		this.foods = foods;
		return this;
	}

	public PlayView setNum(String num) {
		this.num = num;
		return this;
	}

	public PlayView reset(){
		tv_person.setText(R.string.tv_person);
		tv_food.setText(R.string.tv_food);
		return this;
	}
}
