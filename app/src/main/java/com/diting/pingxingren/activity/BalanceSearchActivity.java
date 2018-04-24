package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 2017/1/19.
 */

public class BalanceSearchActivity extends BaseActivity {
    private TitleBarView titleBarView;
    private TextView tv_balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_balance_search);
        initViews();
        initEvents();
        searchBalance();
    }

    private void searchBalance() {
        Diting.searchBalance(new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String balance = response.getString("balance");
                    if(!Utils.isEmpty(balance)) {
                        tv_balance.setText(response.getString("balance"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        });
    }

    private void initTitleBarView(){
        titleBarView = (TitleBarView)findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
        titleBarView.setTitleText(R.string.balance_search);
    }

    private void initTitleBarEvents(){
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        initTitleBarView();
        tv_balance =  findViewById(R.id.tv_balance);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
    }
}
