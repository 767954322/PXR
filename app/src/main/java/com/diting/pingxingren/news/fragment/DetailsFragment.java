package com.diting.pingxingren.news.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diting.pingxingren.R;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.model.NewsListModel;
import com.diting.pingxingren.news.activity.NewsActivity;
import com.diting.pingxingren.news.base.LazyFragment;
import com.diting.pingxingren.news.custom.NewsListView;
import com.diting.pingxingren.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放新闻目录
 * Created by thinkpad on 2018/1/4.
 */
@SuppressLint("ValidFragment")
public class DetailsFragment extends LazyFragment implements ClickListener {

    public static final String MODEL = "MODEL";
    private static final String INDEX = "INDEX";

    private boolean isFirst = true;//是否第一次加載
    private int index = 0;
    private int onePageNewsCount = 6;//每页新闻数量

    private NewsListView mView;
    private List<NewsListModel> modelList;

    public static Fragment getInstance(int index, Bundle bundle){
        DetailsFragment frag = new DetailsFragment();
        Bundle b = new Bundle();
        b.putInt(INDEX, index);
        b.putBundle("bundle", bundle);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            this.index = args.getInt(INDEX);
            Bundle b = args.getBundle("bundle");
            if(b != null){
                this.modelList = b.getParcelableArrayList("list");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.news_frag_details, null);
//        NewsTitleBar bar = layout.findViewById(R.id.title_bar);
//        bar.setOtherTitle(null, true, true, this);
        mView = layout.findViewById(R.id.news_list_view);
        ArrayList<NewsListModel> models = new ArrayList<>();
        int firstPage = index * onePageNewsCount;
        for (int i = firstPage; i < firstPage + onePageNewsCount; i++) {
            models.add(modelList.get(i));
        }
        mView.upDateDatas(models);
        mView.setmClickListener(this);
        return layout;
    }

    @Override
    protected void lazyLoad() {
        if(isFirst){
            isFirst = false;
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClick(Object o) {
        NewsListModel model = (NewsListModel) o;
        if(StringUtil.isNotEmpty(model.getUrl())){
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            intent.putExtra("url", model.getUrl());
            intent.putExtra("title", model.getTitle());
            startActivity(intent);
        }
    }
}
