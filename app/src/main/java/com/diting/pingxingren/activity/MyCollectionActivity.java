package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.MyCollectionAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.CollectionModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.CollectionListObserver;
import com.diting.pingxingren.net.observers.DeleteCollectionObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, MyCollectionAdapter.onDelCollectionListener, MyCollectionAdapter.OnDetailsListener {
    private TitleBarView titleBarView;
    private RelativeLayout mTextColl;
    private RelativeLayout mVideoColl;
    private RelativeLayout mImageColl;
    private RelativeLayout mFileColl;
    private RelativeLayout mAudioColl;
    private RelativeLayout mAllColl;
    private LinearLayout mClassify;

    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyCollectionAdapter mAdapter;
    private List<CollectionModel> collectionModels = new ArrayList<>();
    private CollectionModel collectionModel;
    private String sort = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        initViews();
        initEvents();
        initTitleView();
        initDate();
    }

    private void initDate() {
        onRefresh();
    }

    @Override
    protected void initViews() {

        mSwipeRefreshWidget = findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyCollectionAdapter(R.layout.item_collection, collectionModels);
        View view = LayoutInflater.from(MyCollectionActivity.this).inflate(R.layout.empty, null);
        TextView textView = view.findViewById(R.id.tv_tip);
        textView.setText(R.string.tv_no_collection);
        mAdapter.setEmptyView(view);
        mRecyclerView.setAdapter(mAdapter);
        mTextColl = findViewById(R.id.rel_call_text);
        mVideoColl = findViewById(R.id.rel_call_video);
        mImageColl = findViewById(R.id.rel_call_image);
        mFileColl = findViewById(R.id.rel_call_file);
        mClassify = findViewById(R.id.lay_classify);
        mAudioColl = findViewById(R.id.rel_call_audio);
        mAllColl = findViewById(R.id.rel_call_all);

    }

    @Override
    protected void initEvents() {
        mTextColl.setOnClickListener(this);
        mVideoColl.setOnClickListener(this);
        mImageColl.setOnClickListener(this);
        mFileColl.setOnClickListener(this);
        mAudioColl.setOnClickListener(this);
        mAllColl.setOnClickListener(this);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        mAdapter.setOnDelCollectionListener(this);
        mAdapter.setOnDetailsListener(this);


    }

    private void initTitleView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("我的收藏");
        titleBarView.setBtnRight(R.mipmap.icon_more, null);
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChangeView();
            }
        });
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_call_all:
                isChangeView();
                sort = "";
                ApiManager.getCollectionList(sort, new CollectionListObserver(mResultCallBack));
                break;
            case R.id.rel_call_text://5.文本
                isChangeView();
                sort = "5";
                ApiManager.getCollectionList(sort, new CollectionListObserver(mResultCallBack));
                break;
            case R.id.rel_call_video:// 4.视频
                sort = "4";
                ApiManager.getCollectionList(sort, new CollectionListObserver(mResultCallBack));
                isChangeView();
                break;
            case R.id.rel_call_image:// 3. 图片
                sort = "3";
                ApiManager.getCollectionList(sort, new CollectionListObserver(mResultCallBack));
                isChangeView();
                break;
            case R.id.rel_call_file://2.文件
                sort = "2";
                ApiManager.getCollectionList(sort, new CollectionListObserver(mResultCallBack));
                isChangeView();
                break;
            case R.id.rel_call_audio://1.音频
                sort = "1";

                ApiManager.getCollectionList(sort, new CollectionListObserver(mResultCallBack));
                isChangeView();
                break;
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshWidget.setRefreshing(false);

        ApiManager.getCollectionList(sort, new CollectionListObserver(mResultCallBack));
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof CollectionModel) {
                dismissLoadingDialog();
                showShortToast("删除收藏成功");
                mAdapter.remove(mAdapter.getData().indexOf(collectionModel));
            }
        }

        @Override
        public void onResult(List<?> resultList) {
            mSwipeRefreshWidget.setVisibility(View.VISIBLE);
            collectionModels = (List<CollectionModel>) resultList;
            mAdapter.setNewData(collectionModels);
            mAdapter.notifyDataSetChanged();

        }

        @Override
        public void onError(Object o) {
            dismissLoadingDialog();
            if (o instanceof String) {
                showShortToast((String) o);
            }
        }
    };


    private void isChangeView() {
        if (mSwipeRefreshWidget.getVisibility()
                == View.VISIBLE) {
            mSwipeRefreshWidget.setVisibility(View.GONE);
            mClassify.setVisibility(View.VISIBLE);
        } else {
            mClassify.setVisibility(View.GONE);

            mSwipeRefreshWidget.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void deleteCollection(CollectionModel item) {
        showDialog(item);
    }

    //删除收藏
    private void showDialog(final CollectionModel item) {
        collectionModel = item;
        final MyDialog myDialog = new MyDialog(MyCollectionActivity.this);
        myDialog.setTitle("提示");
        myDialog.setMessage("确定要删除收藏吗");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                showLoadingDialog("请求中");
                ApiManager.deleteCollection(item.getId(), new DeleteCollectionObserver(mResultCallBack));
                myDialog.dismiss();
            }
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    @Override
    public void goToCollectionDetails(CollectionModel item) {
        startActivity(CollectionDetailsActivity.getCallingIntent(this, item));
    }
}
