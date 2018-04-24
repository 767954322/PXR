package com.diting.pingxingren.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.ContactLocalAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.SideBar;
import com.diting.pingxingren.databinding.ActivityContactLocalBinding;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.voice.data.body.Contact;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




/**
 * Created by Gu FanFan.
 * Date: 2017/4/10, 15:50.
 * 登录界面.
 */

public class ContactLocalActivity extends BaseActivity implements View.OnClickListener{

    private ActivityContactLocalBinding mBinding;
    private List<Contact> contactList = new ArrayList<Contact>();
    private ContactLocalAdapter mAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private LinearLayoutManager layoutManager;
    private boolean isFirst = true;
    private List<RobotConcern> robotConcernList;
    private static final String EXTRA = "robotConcernList";
    private List<Contact> selectedList = new ArrayList<Contact>();

    public static Intent getCallingIntent(Context context,List<RobotConcern> robotConcernList) {
        Intent intent = new Intent();
        intent.setClass(context, ContactLocalActivity.class);
        intent.putParcelableArrayListExtra(EXTRA,(ArrayList<RobotConcern>) robotConcernList);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_local);
        initViews();
        initEvents();
        requestReadContactPermission();
    }

    @Override
    protected void initViews() {
        mBinding.titleBar.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.GONE);
        mBinding.titleBar.setTitleText("本地通讯录");
        mBinding.titleBar.setBtnLeft(R.mipmap.icon_back,null);
        mBinding.titleBar.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initEvents() {
        mBinding.tvSelect.setOnClickListener(this);
//        mBinding.tvCancel.setOnClickListener(this);
        mBinding.tvInsert.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ContactLocalAdapter(R.layout.item_contact_local,contactList);
        mBinding.recyclerView.setAdapter(mAdapter);
        // mBinding.recyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mBinding.recyclerView));
        //mBinding.recyclerView.addItemDecoration(new RecyclerViewDivider(this,RecyclerViewDivider.HORIZONTAL));
        //mBinding.recyclerView.addItemDecoration(decoration);
        mBinding.sideBar.setIndexChangeListener(new SideBar.indexChangeListener() {
            @Override
            public void indexChanged(String tag) {
                if (TextUtils.isEmpty(tag) || contactList.size() <= 0) return;
                for (int i = 0; i < contactList.size(); i++) {
                    if (tag.equals(contactList.get(i).getIndexTag())) {
                        layoutManager.scrollToPositionWithOffset(i, 0);
//                        layoutManager.scrollToPosition(i);
                        return;
                    }
                }
            }
        });
        mBinding.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
                mBinding.sideBar.setFocusTag(contactList.get(firstItemPosition).getIndexTag());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mAdapter.setOnClickListener(new ContactLocalAdapter.IOnClickListener() {
            @Override
            public void onClick() {
                //mBinding.tvCancel.setVisibility(View.VISIBLE);
                mBinding.tvSelect.setText("取消");
            }
        });
    }

    private void requestReadContactPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                Utils.showMissingPermissionDialog(this,"读取联系人");
            }else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }else {
            initDatas();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initDatas(){
        readContacts();
    }

    private void readContacts(){
        robotConcernList = getIntent().getParcelableArrayListExtra(EXTRA);
        Cursor cursor = null;
        try{
            cursor = getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            while (cursor.moveToNext()){
                String displayName = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("-","").replace(" ","");
                if(number.startsWith("+86")){
                    number.substring(3,number.length());
                }
                if(Utils.isMobile(number)) {
                    int isUploaded = 0;
                    for(int i = 0;i < robotConcernList.size();i++){
                        if(number.equals(robotConcernList.get(i).getUsername())){
                            isUploaded = 1;
                            break;
                        }
                    }
                    contactList.add(new Contact(displayName, number,isUploaded));
                }
            }
            Utils.sortData(contactList);
            if(contactList!=null&&contactList.size()!=0) {
                String tagsStr = Utils.getTags(contactList);
                mBinding.sideBar.setIndexStr(tagsStr);
                mAdapter.notifyDataSetChanged();
                mBinding.sideBar.setFocusTag(contactList.get(0).getIndexTag());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null)
                cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initDatas();
            } else
            {
                // Permission Denied
                Utils.showMissingPermissionDialog(this,"读取联系人");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_select:
                if(mBinding.tvSelect.getText().toString().equals("选择")) {
                    //mBinding.tvCancel.setVisibility(View.VISIBLE);
                    mBinding.tvSelect.setText("取消");
                    mAdapter.setShowCheckBox(true);
                }else {
                    mAdapter.setShowCheckBox(false);
                    mBinding.tvSelect.setText("选择");
                    mAdapter.unCheckAll();
                }
                break;
//            case R.id.tv_select:
//                if(mBinding.tvSelect.getText().toString().equals("选择")) {
//                    mBinding.tvCancel.setVisibility(View.VISIBLE);
//                    mBinding.tvSelect.setText("全选");
//                    mAdapter.setShowCheckBox(true);
//                }else {
//                    mAdapter.checkAll();
//                }
//                break;
//            case R.id.tv_cancel:
//                mAdapter.setShowCheckBox(false);
//                mBinding.tvCancel.setVisibility(View.GONE);
//                mBinding.tvSelect.setText("选择");
//                mAdapter.unCheckAll();
//                break;
            case R.id.tv_insert:
                //int size = mAdapter.getSelectCount();
                int size = mAdapter.getCheckMap().size();
                if(size==0){
                    showShortToast("请选择联系人");
                    break;
                }
                if(size > 50){
                    showShortToast("一次最多导入50个联系人");
                    break;
                }
                //showShortToast("请求已发送");
                showLoadingDialog("导入中，请耐心等待");
                Iterator<Integer> iter = mAdapter.getCheckMap().keySet().iterator();
                while (iter.hasNext()){
                    selectedList.add(contactList.get(iter.next()));
                }
                Diting.uploadContacts(selectedList, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        dismissLoadingDialog();
                        for(int i = 0;i < selectedList.size();i++){
                            selectedList.get(i).setIsUploaded(1);
                        }
                        mAdapter.notifyDataSetChanged();
                        selectedList.clear();
                        mAdapter.getCheckMap().clear();
                        EventBus.getDefault().post("update");
                        showShortToast("导入成功");
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        dismissLoadingDialog();
                        showShortToast("导入失败");
                    }
                });
                break;
            default:
                break;
        }

    }

}
