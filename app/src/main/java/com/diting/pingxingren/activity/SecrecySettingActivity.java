package com.diting.pingxingren.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by asus on 2017/1/16.
 */

public class SecrecySettingActivity extends BaseActivity implements View.OnClickListener{
    private TitleBarView titleBarView;
    private ImageView iv_other;
    private ImageView iv_phone;
    private boolean enableOther;
    private boolean enablePhone;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private RadioGroup radioGroup;
    private int callState = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_secrecy_setting);
        initViews();
        initEvents();
        initData();
    }

    private void initData() {
        radioGroup.check(getResId(MySharedPreferences.getPhoneSwitch()));
//        if(MySharedPreferences.getPhoneSwitch()==1){
//            enableOther();
//        }else {
//            disableOther();
//        }
//        if(MySharedPreferences.getContactsPermission()){
//            enableContacts();
//        }else {
//            disableContacts();
//        }
    }

    private int getResId(int state){
        switch (state){
            case 0:
                return R.id.radio_button_0;
            case 1:
                return R.id.radio_button_1;
            case 2:
                return R.id.radio_button_2;
            case 3:
                return R.id.radio_button_3;
            default:
                return R.id.radio_button_1;
        }
    }

    private void initTitleBarView(){
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
        titleBarView.setBtnRightText("完成");
        titleBarView.setTitleText(R.string.tv_secrecy);
    }

    private void initTitleBarEvents(){
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCallState(callState);
            }
        });
    }

    @Override
    protected void initViews() {
        initTitleBarView();
        radioGroup =  findViewById(R.id.radio_group);
        iv_other =  findViewById(R.id.iv_other);
        iv_phone =   findViewById(R.id.iv_phone);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        iv_other.setOnClickListener(this);
        iv_phone.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radio_button_0:
                        callState = 0;
                        break;
                    case R.id.radio_button_1:
                        callState = 1;
                        break;
                    case R.id.radio_button_2:
                        callState = 2;
                        break;
                    case R.id.radio_button_3:
                        callState = 3;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void enableOther(){
        iv_other.setImageResource(R.mipmap.switch_on);
        enableOther = true;
        MySharedPreferences.savePhoneSwitch(1);
    }

    private void disableOther(){
        iv_other.setImageResource(R.mipmap.switch_off);
        enableOther = false;
        MySharedPreferences.savePhoneSwitch(0);
    }

    private void enableContacts(){
        iv_phone.setImageResource(R.mipmap.switch_on);
        enablePhone = true;
        MySharedPreferences.saveContactsPermission(true);
    }

    private void disableContacts(){
        iv_phone.setImageResource(R.mipmap.switch_off);
        enablePhone = false;
        MySharedPreferences.saveContactsPermission(false);
    }


    private void changeCallState(final int state){
        showLoadingDialog("请求中");


        ApiManager.setPhonePermission(state, new ResultCallBack() {
            @Override
            public void onResult(Object result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    dismissLoadingDialog();
                    if (jsonObject.getString("message").equals("修改成功！")) {
                        dismissLoadingDialog();
                        MySharedPreferences.savePhoneSwitch(state);
                        showShortToast("设置成功");
                        finish();
                    } else {
                        dismissLoadingDialog();
                        showShortToast("设置失败");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResult(List<?> resultList) {

            }

            @Override
            public void onError(Object o) {
                if (o instanceof String)
                    showShortToast((String) o);
            }
        });






//        Diting.setPhonePermission(state, new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                dismissLoadingDialog();
//                MySharedPreferences.savePhoneSwitch(state);
//                showShortToast("设置成功");
//                finish();
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                dismissLoadingDialog();
//                showShortToast("设置失败");
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_other:
                int type = enableOther ? 1 : 0;
                showLoadingDialog("请求中");


                ApiManager.setPhonePermission(type, new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            dismissLoadingDialog();
                            if (jsonObject.getString("message").equals("修改成功！")) {
                                dismissLoadingDialog();
                                if(enableOther){
                                    disableOther();
                                }else {
                                    enableOther();
                                }
                            } else {
                                dismissLoadingDialog();
                                showShortToast("设置失败");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onResult(List<?> resultList) {

                    }

                    @Override
                    public void onError(Object o) {
                        if (o instanceof String)
                            showShortToast((String) o);
                    }
                });




//                Diting.setPhonePermission(type, new HttpCallBack() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        dismissLoadingDialog();
//                        if(enableOther){
//                            disableOther();
//                        }else {
//                            enableOther();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(VolleyError error) {
//                        dismissLoadingDialog();
//                        showShortToast("设置失败");
//                    }
//                });
                break;
            case R.id.iv_phone:
                if(enablePhone){
                    disableContacts();
                }else {
                    if (ContextCompat.checkSelfPermission(SecrecySettingActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(SecrecySettingActivity.this, Manifest.permission.READ_CONTACTS)) {
                            Utils.showMissingPermissionDialog(SecrecySettingActivity.this, "通讯录");
                        } else {
                            ActivityCompat.requestPermissions(SecrecySettingActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    } else {
                        enableContacts();
                    }
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                enableContacts();
            } else
            {
                // Permission Denied
                Utils.showMissingPermissionDialog(SecrecySettingActivity.this, "通讯录");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
