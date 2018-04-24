package com.diting.pingxingren.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.GridViewAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.entity.ExternalOptions;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.CommonFeaturesObserver;
import com.diting.pingxingren.util.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 03, 16:34.
 * Description: 通用技能.
 */

public class GeneralFunctionActivity extends BaseActivity {

    public static Intent callingIntent(Context context, ArrayList<ExternalOptions> externalOptions) {
        Intent intent = new Intent(context, GeneralFunctionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("functionList", externalOptions);
        intent.putExtras(bundle);
        return intent;
    }

    private GridView mAppGridView;
    private GridViewAdapter mAdapter;
    private ArrayList<ExternalOptions> mExternalOptionsList;
    private TextView mComplete;
    private LinearLayout mBack;
    private ArrayList<Integer> mOpenFunctionList;
    private ArrayList<Integer> mCloseFunctionList;
    private GeneralFunctionActivityHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_function);
        initViews();
        initEvents();
        initData();
    }

    @Override
    protected void initViews() {
        mAppGridView =  findViewById(R.id.gvApplication);
        mComplete =   findViewById(R.id.tvComplete);
        mBack =   findViewById(R.id.llBack);
    }

    @Override
    protected void initEvents() {
        mAppGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExternalOptions externalOptions = mExternalOptionsList.get(position);
                externalOptions.setChoose(!externalOptions.isChoose());
                mAdapter.notifyDataSetChanged();
            }
        });

        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenFunctionList.clear();
                mCloseFunctionList.clear();
                for (int i = 0; i < mExternalOptionsList.size(); i++) {
                    ExternalOptions externalOptions = mExternalOptionsList.get(i);
                    if (externalOptions.isChoose()) {
                        mOpenFunctionList.add(externalOptions.getId());
                    }
                    mCloseFunctionList.add(externalOptions.getId());
                }

                Intent intent = new Intent();
                intent.putExtra("functionList", mExternalOptionsList);
                intent.putIntegerArrayListExtra("openFunctionList", mOpenFunctionList);
                intent.putIntegerArrayListExtra("closeFunctionList", mCloseFunctionList);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mHandler = new GeneralFunctionActivityHandler(this);
        mOpenFunctionList = new ArrayList<>();
        mCloseFunctionList = new ArrayList<>();
        mExternalOptionsList = (ArrayList<ExternalOptions>) getIntent().getSerializableExtra("functionList");
        if (mExternalOptionsList == null)
            mExternalOptionsList = new ArrayList<>();
        mAdapter = new GridViewAdapter(this, R.layout.item_application, mExternalOptionsList);
        mAppGridView.setAdapter(mAdapter);
        if (mExternalOptionsList == null || mExternalOptionsList.size() == 0)
            ApiManager.getCommonFeatures(new CommonFeaturesObserver(mResultCallBack));
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
        }

        @Override
        public void onResult(List<?> resultList) {
            Class<?> aClass = resultList.get(0).getClass();
            if (aClass.getName().equals(ExternalOptions.class.getName())) {
                mExternalOptionsList = (ArrayList<ExternalOptions>) resultList;
                mHandler.sendEmptyMessage(0);
                ApiManager.getOpenCommonFeatures(this);
            } else if (aClass.getName().equals(Integer.class.getName())) {
                List<Integer> openIds = (List<Integer>) resultList;
                for (int i = 0; i < openIds.size(); i++) {
                    for (ExternalOptions externalOptions : mExternalOptionsList) {
                        if (openIds.get(i) == externalOptions.getId()) {
                            externalOptions.setChoose(true);
                        }
                    }
                }

                mHandler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onError(Object o) {
            if (o instanceof String)
                ToastUtils.showShortToastSafe((String) o);
        }
    };

    private static class GeneralFunctionActivityHandler extends Handler {
        private WeakReference<GeneralFunctionActivity> mActivityWeakReference;

        public GeneralFunctionActivityHandler(GeneralFunctionActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GeneralFunctionActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0 :
                        activity.mAdapter = new GridViewAdapter(activity, R.layout.item_application, activity.mExternalOptionsList);
                        activity.mAppGridView.setAdapter(activity.mAdapter);
                        break;
                }
            }
        }
    }
}
