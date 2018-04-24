package com.diting.pingxingren.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.AddKnowledgeActivity;
import com.diting.pingxingren.adapter.NewKnowledgeAdapter;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.LoadListView;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.entity.CommonLanguage;
import com.diting.pingxingren.entity.Knowledge;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/2/22.
 * 知识库列表页面
 */

public class TabKnowledgeFragment extends BaseFragment implements View.OnClickListener, LoadListView.ILoadListener, LoadListView.RLoadListener, NewKnowledgeAdapter.IknowledgeListener {
    private TitleBarView titleBarView;
    private LoadListView lv_knowledge;
    private NewKnowledgeAdapter adapter;
    private List<Knowledge> knowledgeList = new ArrayList<Knowledge>();
    private LinearLayout ll_no_knowledge;
    private TextView tv_add_question;
    private int moreCount = 1;
    private View mBottom_bar;
    private LinearLayout ll_add_knowledge;
    private CommonLanguage mCommonLanguage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge_tab, null);
        initViews(view);
        initEvents();
        return view;
    }

    private void initTitleBarView(View view) {
        titleBarView = view.findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("培养");
    }

    private void initViews(View view) {
        initTitleBarView(view);
        tv_add_question =   view.findViewById(R.id.tv_add_question);
//        tv_select_all = (TextView)findViewById(R.id.tv_select_all);
        ll_no_knowledge =   view.findViewById(R.id.ll_no_knowledge);
        lv_knowledge =  view.findViewById(R.id.lv_knowledge);
        mBottom_bar = getActivity().findViewById(R.id.rl_bottom);
        ll_add_knowledge =   view.findViewById(R.id.ll_add_knowledge);
    }

    private void initEvents() {
        mCommonLanguage = new CommonLanguage();
        ll_add_knowledge.setOnClickListener(this);
        lv_knowledge.setInterface(this);
        lv_knowledge.setReflashInterface(this);
        lv_knowledge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AddKnowledgeActivity.class);
                intent.putExtra("knowledge", knowledgeList.get(position - 1));
                startActivity(intent);
//                    EventBus.getDefault().postSticky(knowledgeList.get(position - 1));
//                    startActivity(AddKnowledgeActivity.class);
            }
        });
        adapter = new NewKnowledgeAdapter(getContext(), R.layout.knowledge_item_new, knowledgeList);
        lv_knowledge.setAdapter(adapter);
        lv_knowledge.setOnTouchListener(new View.OnTouchListener() {
            private float mEndY;
            private float mStartY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndY = event.getY();
                        float v1 = mEndY - mStartY;
                        if (v1 < -3) {
                            mBottom_bar.setVisibility(View.GONE);
                            mStartY = mEndY;
                            return false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mBottom_bar.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });
        adapter.setListener(this);
        getKnowledge(1, false);
    }

    private void getKnowledge(final int pageNo, boolean isRefresh) {
        if (pageNo == 1 && !isRefresh) {
            showLoadingDialog("加载中");
        }
        Diting.searchKnowledge(pageNo, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                dismissLoadingDialog();
                List<Knowledge> list = Utils.parseKnowledge(response);
                if (list == null) {
                    if (pageNo == 1) {
                        ll_no_knowledge.setVisibility(View.VISIBLE);
                    } else {
                        showShortToast("无更多数据");
                        lv_knowledge.loadCompleted();
                    }
                } else {
                    ll_no_knowledge.setVisibility(View.GONE);
                    moreCount++;
                    knowledgeList.addAll(list);
                    adapter.notifyDataSetChanged();
                    lv_knowledge.reflashComplete();
                    lv_knowledge.loadCompleted();
                    if (pageNo == 1) {
                        lv_knowledge.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                if (error.networkResponse == null) {
                    showShortToast("请求超时！");
                }
            }
        });
    }

    @Override
    public void onLoad() {
        getKnowledge(moreCount, false);
    }

    @Override
    public void onRefresh() {
        moreCount = 1;
        knowledgeList.clear();
        getKnowledge(1, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_knowledge:
                startActivity(AddKnowledgeActivity.class);
                //startActivity(ContactLocalActivity.class);
                break;
            default:
                break;
        }
    }


    private void showDialog(final Knowledge knowledge) {
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.setTitle("温馨提示");
        myDialog.setMessage("确定要删除所选项目吗？");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                myDialog.dismiss();
                showLoadingDialog("请求中");
                Diting.deleteKnowledge(knowledge.getKnowledgeId(), new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        dismissLoadingDialog();
                        showShortToast("删除成功");
                        knowledgeList.remove(knowledge);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        dismissLoadingDialog();
                        showShortToast("删除失败");
                    }
                });
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void addSuccess(String message) {
        if (message.equals("add_success")) {
            onRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void deleteKnowledge(Knowledge knowledge) {
        showDialog(knowledge);
    }

    @Override
    public void addCommonLanguage(String knowledgeQuestion) {

    }
}
