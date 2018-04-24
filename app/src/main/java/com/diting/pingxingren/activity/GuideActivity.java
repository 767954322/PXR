package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.util.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2018 on 2018/2/22.
 * 引导页,第一次进入应用时打开此页面.只通过是否第一次打开应用判断是否打开.
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private Button goIn;//进入
    private LinearLayout pointParent;//圆点容器
    private List<View> resList;//图片资源集合
    private ImageView currentPoint;//当前圆点
    private View lastItem;
    private ImageView lastImg;

    private int pointRadius = 20;//圆点的直径
    private int pointPadding = 15;//圆点的外距

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initViews();
        initData();
        initEvents();
    }

    @Override
    protected void initViews() {
        lastItem = LayoutInflater.from(this).inflate(R.layout.guide_last_item, null);
        goIn = lastItem.findViewById(R.id.bt_go_in);
        lastImg = lastItem.findViewById(R.id.iv);
        viewPager = findViewById(R.id.vp);
        pointParent = findViewById(R.id.point_parent);
        currentPoint = findViewById(R.id.current_point);
    }

    private void initData(){
        resList = new ArrayList<>();
        ArrayList<Integer> l = new ArrayList<>();
        l.add(R.drawable.guide_1);
        l.add(R.drawable.guide_2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pointRadius, pointRadius);
        params.setMargins(pointPadding,0,pointPadding,0);
        for (int i = 0; i < l.size(); i++) {
            pointParent.addView(getPoint(),params);
            ImageView view = new ImageView(this);
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(p);
            view.setImageResource(l.get(i));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            if(i == l.size() - 1){
                lastImg.setImageResource(l.get(i));
                resList.add(lastItem);
            }else{
                resList.add(view);
            }
        }
        MyPagerAdapter pagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        RelativeLayout.LayoutParams currentParams = new RelativeLayout.LayoutParams(pointRadius, pointRadius);
        currentParams.setMargins(pointPadding, 0, 0, 0);
        currentPoint.setLayoutParams(currentParams);
    }

    /**
     * 创建圆点
     */
    private ImageView getPoint(){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.drawable.white_point);
        return imageView;
    }

    @Override
    protected void initEvents() {
        goIn.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) currentPoint.getLayoutParams();
        params.leftMargin = pointPadding + position * (pointRadius + pointPadding * 2);
        currentPoint.setLayoutParams(params);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_go_in:
                MySharedPreferences.isFirstOpen(false);
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return resList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(resList.get(position));
            return resList.get(position);
        }
    }
}
