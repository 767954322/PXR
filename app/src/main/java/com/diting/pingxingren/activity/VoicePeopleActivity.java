package com.diting.pingxingren.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.VoicePeopleAdapter;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.entity.VoicePeopleModel;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Gu FanFan.
 * Date: 2017/5/27, 11:29.
 */

public class VoicePeopleActivity extends Activity implements VoicePeopleAdapter.ItemClickListener {

    private VoicePeopleAdapter mPeopleAdapter;
    private TitleBarView titleBarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_voice_people);
        initView();
        initData();
    }

    private void initTitleBarView(){
        titleBarView = (TitleBarView)findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
        titleBarView.setTitleText("发音选择");
    }

    private void initTitleBarEvents(){
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        initTitleBarView();
        initTitleBarEvents();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.voicePeople);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPeopleAdapter = new VoicePeopleAdapter(this, null);
        mPeopleAdapter.setListener(this);
        recyclerView.setAdapter(mPeopleAdapter);
    }

    private void initData() {

        LogUtils.e("MySharedPreferences====="+MySharedPreferences.getVoicePeople());
        LinkedHashMap<String, ArrayList<VoicePeopleModel>> voiceHashMap = new LinkedHashMap<>();
        ArrayList<VoicePeopleModel>  original=new ArrayList<>();
        VoicePeopleModel peopleModel17 = new VoicePeopleModel();
        peopleModel17.setPeopleHead(R.mipmap.icon_left);
        peopleModel17.setPeopleName("原声");
        peopleModel17.setPeopleCode("");
        original.add(peopleModel17);
        voiceHashMap.put("原声", original);


        /* 少年 */
        ArrayList<VoicePeopleModel> juvenile = new ArrayList<>();
        VoicePeopleModel peopleModel1 = new VoicePeopleModel();
        peopleModel1.setPeopleHead(R.mipmap.xiaoxin);
        peopleModel1.setPeopleName("小新");
        peopleModel1.setPeopleCode("xiaoxin");
        juvenile.add(peopleModel1);
        VoicePeopleModel peopleModel2 = new VoicePeopleModel();
        peopleModel2.setPeopleHead(R.mipmap.xiaowanzi);
        peopleModel2.setPeopleName("小丸子");
        peopleModel2.setPeopleCode("xiaowanzi");
        juvenile.add(peopleModel2);
        VoicePeopleModel peopleModel3 = new VoicePeopleModel();
        peopleModel3.setPeopleHead(R.mipmap.vinn);
        peopleModel3.setPeopleName("楠楠");
        peopleModel3.setPeopleCode("vinn");
        juvenile.add(peopleModel3);
        voiceHashMap.put("少年", juvenile);

        /* 青年 */
        ArrayList<VoicePeopleModel> youth = new ArrayList<>();
        VoicePeopleModel peopleModel4 = new VoicePeopleModel();
        peopleModel4.setPeopleHead(R.mipmap.xiaoyan);
        peopleModel4.setPeopleName("小燕");
        peopleModel4.setPeopleCode("xiaoyan");
        youth.add(peopleModel4);
        VoicePeopleModel peopleModel5 = new VoicePeopleModel();
        peopleModel5.setPeopleHead(R.mipmap.xiaofeng);
        peopleModel5.setPeopleName("小峰");
        peopleModel5.setPeopleCode("xiaofeng");
        youth.add(peopleModel5);
        VoicePeopleModel peopleModel6 = new VoicePeopleModel();
        peopleModel6.setPeopleHead(R.mipmap.xiaoqi);
        peopleModel6.setPeopleName("小琪");
        peopleModel6.setPeopleCode("xiaoqi");
        youth.add(peopleModel6);
        VoicePeopleModel peopleModel7 = new VoicePeopleModel();
        peopleModel7.setPeopleHead(R.mipmap.yefang);
        peopleModel7.setPeopleName("叶芳");
        peopleModel7.setPeopleCode("yefang");
        youth.add(peopleModel7);
        VoicePeopleModel peopleModel8 = new VoicePeopleModel();
        peopleModel8.setPeopleHead(R.mipmap.aisxmeng);
        peopleModel8.setPeopleName("小梦");
        peopleModel8.setPeopleCode("aisxmeng");
        youth.add(peopleModel8);
        voiceHashMap.put("青年", youth);

        /* 中年 */
        ArrayList<VoicePeopleModel> middleAged = new ArrayList<>();
        VoicePeopleModel peopleModel9 = new VoicePeopleModel();
        peopleModel9.setPeopleHead(R.mipmap.vils);
        peopleModel9.setPeopleName("老孙");
        peopleModel9.setPeopleCode("vils");
        middleAged.add(peopleModel9);
        VoicePeopleModel peopleModel10 = new VoicePeopleModel();
        peopleModel10.setPeopleHead(R.mipmap.aisjying);
        peopleModel10.setPeopleName("小筠");
        peopleModel10.setPeopleCode("aisjying");
        middleAged.add(peopleModel10);
        voiceHashMap.put("中年", middleAged);

        /* 方言 */
        ArrayList<VoicePeopleModel> dialect = new ArrayList<>();
        VoicePeopleModel peopleModel11 = new VoicePeopleModel();
        peopleModel11.setPeopleHead(R.mipmap.dalong);
        peopleModel11.setPeopleName("粤语");
        peopleModel11.setPeopleCode("dalong");
        dialect.add(peopleModel11);
        VoicePeopleModel peopleModel12 = new VoicePeopleModel();
        peopleModel12.setPeopleHead(R.mipmap.xiaoqian);
        peopleModel12.setPeopleName("东北话");
        peopleModel12.setPeopleCode("xiaoqian");
        dialect.add(peopleModel12);
        VoicePeopleModel peopleModel13 = new VoicePeopleModel();
        peopleModel13.setPeopleHead(R.mipmap.aisxrong);
        peopleModel13.setPeopleName("四川话");
        peopleModel13.setPeopleCode("aisxrong");
        dialect.add(peopleModel13);
        VoicePeopleModel peopleModel14 = new VoicePeopleModel();
        peopleModel14.setPeopleHead(R.mipmap.xiaokun);
        peopleModel14.setPeopleName("河南话");
        peopleModel14.setPeopleCode("xiaokun");
        dialect.add(peopleModel14);
        VoicePeopleModel peopleModel15 = new VoicePeopleModel();
        peopleModel15.setPeopleHead(R.mipmap.aisxqiang);
        peopleModel15.setPeopleName("湖南话");
        peopleModel15.setPeopleCode("aisxqiang");
        dialect.add(peopleModel15);
        VoicePeopleModel peopleModel16 = new VoicePeopleModel();
        peopleModel16.setPeopleHead(R.mipmap.aisxying);
        peopleModel16.setPeopleName("陕西话");
        peopleModel16.setPeopleCode("aisxying");
        dialect.add(peopleModel16);
        voiceHashMap.put("方言", dialect);

        /* 英语 */
        /*ArrayList<VoicePeopleModel> english = new ArrayList<>();
        VoicePeopleModel peopleModel17 = new VoicePeopleModel();
        peopleModel17.setPeopleHead(R.mipmap.aiscatherine);
        peopleModel17.setPeopleName("凯瑟琳");
        peopleModel17.setPeopleCode("aiscatherine");
        english.add(peopleModel17);
        voiceHashMap.put("英语", english);*/
        mPeopleAdapter.setVoiceHashMap(voiceHashMap);
    }

    @Override
    public void onClick(VoicePeopleModel peopleModel) {
//        Intent intent = new Intent();
//        intent.setClass(this, ChatActivity.class);
//        intent.putExtra("voiceCode", peopleModel.getPeopleCode());
//        setResult(RESULT_OK, intent);\

        MySharedPreferences.saveVoicePeople(peopleModel.getPeopleCode());
        mPeopleAdapter.notifyDataSetChanged();
        finish();
    }
}
