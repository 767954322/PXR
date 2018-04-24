package com.diting.pingxingren.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.diting.pingxingren.R;
import com.diting.pingxingren.custom.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by asus on 2017/2/19.
 */

public class BaseFragment extends Fragment {
    private LoadingDialog dialog;


    /** 短暂显示Toast提示(来自res) **/
    protected void showShortToast(int resId) {
        Toast.makeText(this.getActivity(), getString(resId), Toast.LENGTH_SHORT).show();
    }

    /** 短暂显示Toast提示(来自String) **/
    protected void showShortToast(String text) {
        Toast.makeText(this.getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    /** 长时间显示Toast提示(来自res) **/
    protected void showLongToast(int resId) {
        Toast.makeText(this.getActivity(), getString(resId), Toast.LENGTH_LONG).show();
    }

    /** 长时间显示Toast提示(来自String) **/
    protected void showLongToast(String text) {
        Toast.makeText(this.getActivity(), text, Toast.LENGTH_LONG).show();
    }

    protected void startActivity(Class<?> cls){
        startActivity(new Intent(getActivity(),cls));
    }

    protected void showLoadingDialog(String text){
        dialog = new LoadingDialog(this.getActivity(), R.style.mdialog);
        dialog.setLoadingText(text);
        dialog.show();
    }
    protected void dismissLoadingDialog(){
        if(dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected String getClassName(){
        return this.getClass().getName();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            MobclickAgent.onPageStart(getClassName());
        }else {
            MobclickAgent.onPageEnd(getClassName());
        }
    }
}
