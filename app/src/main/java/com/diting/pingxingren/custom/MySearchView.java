package com.diting.pingxingren.custom;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;

/**
 * Created by asus on 2017/3/14.
 */

public class MySearchView extends LinearLayout {
    private Context context;
    private ClearEditText et_search;
    private ImageView iv_search;
    public MySearchView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.search_view, this);
        et_search = (ClearEditText)findViewById(R.id.et_search);
        iv_search = (ImageView)findViewById(R.id.iv_search);
    }
    public void setOnEditorActionListener(TextView.OnEditorActionListener listener){
        et_search.setOnEditorActionListener(listener);
    }
    public void setSearchListener(OnClickListener listener){
        iv_search.setOnClickListener(listener);
    }
    public String getText(){
        return et_search.getText().toString();
    }
    public void addTextChangeListener(TextWatcher textWatcher){
        et_search.addTextChangedListener(textWatcher);
    }

    public ClearEditText getEt_search() {
        return et_search;
    }

    public ImageView getIv_search() {
        return iv_search;
    }

}
