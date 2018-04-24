package com.diting.pingxingren.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.databinding.ActivityFoodDetailBinding;
import com.diting.pingxingren.entity.FoodInfo;
import com.diting.pingxingren.util.ColorGenerator;
import com.diting.pingxingren.util.PermissionUtils;
import com.diting.pingxingren.util.Utils;


/**
 * Created by asus on 2017/3/13.
 */

public class FoodDetailActivity extends BaseActivity {

    private ActivityFoodDetailBinding mBinding;
    private static final String EXTRA = "food";
    private String[] naywords = {
            "世界上没有什么事是一顿美食解决不了的，如果有，就两顿！",
            "生而为人，对不起，我想吃东西！",
            "我想要一所大房子，面朝大海，好好吃饭！",
            "真正的猛士，就是能吃下100个炸鸡腿！",
            "老夫聊发少年狂，左思右想吃什么？",
            "廉颇老矣，还是挺能吃的！",
            "如果你吃的不胖，你没辜负身材，如果你吃的胖，你没辜负美食，爱吃的你怎么都是对的！",
            "如果能让我回到10年前，我只想把抢我鸡腿的弟弟打一顿！",
            "世界上还有比撑死更美好的事吗？",
            "难道我要告诉你其实我很羡慕一头吃完就睡的猪吗？",
            "何以解忧？唯有吃饭！",
            "人世间的绝大部分痛苦都是源于没有吃撑！",
            "凡人！你对吃货的力量一无所知！",
            "如果谁请我吃好吃的，我今天就会是你的人！",
            "夫妻本是同林鸟，就是为了在一起吃好吃的！",
            "有奶便是娘，有吃的便是爸爸！"
    };
    private String phone[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_food_detail);
        initViews();
        initEvents();
        initData();
    }

    public static Intent getCallingIntent(Context context, FoodInfo foodInfo){
        Intent intent = new Intent();
        intent.setClass(context, FoodDetailActivity.class);
        intent.putExtra(EXTRA,foodInfo);
        return intent;
    }

    private void initTitleBarView(){
        mBinding.titleBar.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.GONE);
        mBinding.titleBar.setBtnLeft(R.mipmap.icon_back,null);
        mBinding.titleBar.setTitleText("谛听美食");
    }

    private void initTitleBarEvents(){
        mBinding.titleBar.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = mBinding.tvPhone.getText().toString().split(",");
                PermissionUtils.requestCallPermission(FoodDetailActivity.this,phone[0]);
            }
        });
    }

    @Override
    protected void initViews() {
        initTitleBarView();
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();

    }
    private void initData(){
        FoodInfo foodInfo =  getIntent().getParcelableExtra(EXTRA);
        mBinding.tvNayword.setTextColor(ColorGenerator.MATERIAL.getRandomColor());
        mBinding.tvDiting.setTextColor(ColorGenerator.MATERIAL.getRandomColor());
        mBinding.tvName.setText(foodInfo.getName());
        mBinding.tvDistance.setText((float)Math.round(foodInfo.getDistance()/100)/10+"km");
        mBinding.tvAddress.setText(foodInfo.getAddress());
        if(!TextUtils.isEmpty(foodInfo.getPhone())) {
            mBinding.tvPhone.setText(foodInfo.getPhone());
        }else {
            mBinding.tvPhone.setVisibility(View.GONE);
            mBinding.devide.setVisibility(View.GONE);
        }
        mBinding.tvNayword.setText(Utils.getRandomString(naywords));
        Glide.with(this).load(foodInfo.getPhotos()).into(mBinding.tvPhoto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == PermissionUtils.MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                PermissionUtils.callPhone(FoodDetailActivity.this, phone[0]);
            } else
            {
                // Permission Denied
                Utils.showMissingPermissionDialog(FoodDetailActivity.this, "拨号");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
