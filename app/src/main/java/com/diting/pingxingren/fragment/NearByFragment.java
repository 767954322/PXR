package com.diting.pingxingren.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.NewChatActivity;
import com.diting.pingxingren.adapter.RobotNearbyAdapter;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.LoadListView;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.entity.RobotPageInfo;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by asus on 2017/1/6.
 */

public class NearByFragment extends BaseFragment implements LoadListView.ILoadListener, LoadListView.RLoadListener {
    private LoadListView lv_concern;
    private RobotNearbyAdapter adapter;
    private List<RobotConcern> robotList = new ArrayList<RobotConcern>();
    private LinearLayout ll_no_fans;
    private LinearLayout ll_main;
    private TextView tv_prompt;
    private int moreCount = 1;
    private boolean isViewCreated;
    private boolean isFisrt = true;
    private LocationClient locationClient;
    private String lat;
    private String lng;
    private boolean isFromRefresh = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fans, null);
        lv_concern =  view.findViewById(R.id.lv_concern);
        ll_no_fans =  view.findViewById(R.id.ll_no_fans);
        ll_main =  view.findViewById(R.id.ll_main);
        tv_prompt =   view.findViewById(R.id.tv_prompt);
        tv_prompt.setText(R.string.tv_nearby_no);
        adapter = new RobotNearbyAdapter(getActivity(), R.layout.item_robot_nearby, robotList);
        lv_concern.setAdapter(adapter);
        lv_concern.setReflashInterface(this);
        lv_concern.setInterface(this);
        lv_concern.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RobotConcern robotConcern = robotList.get(position - 1);
                /*Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("robot", robotConcern);*/
                startActivity(NewChatActivity.callingIntent(getActivity(), robotConcern));
            }
        });
        isViewCreated = true;
        //getRobotFansList(1, false);
        return view;
    }

    private void initLocation() {
        if (locationClient == null) {
            locationClient = new LocationClient(getActivity().getApplicationContext());
            locationClient.registerLocationListener(bdLocationListener);
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
            option.setIsNeedAddress(true);
            locationClient.setLocOption(option);
        }
        locationClient.start();
    }

    private BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            lat = bdLocation.getLatitude() + "";
            lng = bdLocation.getLongitude() + "";
            getRobotNearByList(1, isFromRefresh);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && isFisrt) {
            isFisrt = false;
            getRobotNearByList(false);
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Utils.showMissingPermissionDialog(getActivity(), "位置");
            } else {
                NearByFragment.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            initLocation();
        }
    }

    private void getRobotNearByList(boolean isRefresh) {
        isFromRefresh = isRefresh;
        requestLocationPermission();
    }

    private void getRobotNearByList(final int pageNo, final boolean isFromRefresh) {
        if (lat == null || lng == null) {
            showShortToast("请检查是否开启位置信息");
        }
        if (!isFromRefresh) {
            showLoadingDialog("加载中");
        }
        Diting.searchNearBy(lat, lng, pageNo, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                dismissLoadingDialog();
                Gson gson = new Gson();
                RobotPageInfo robotPageInfo = gson.fromJson(response.toString(), RobotPageInfo.class);
                List<RobotConcern> list = robotPageInfo.getRobotList();
                //List<RobotConcern> list = Utils.parseFans(response);
                if (list != null && list.size() > 0) {
                    ll_no_fans.setVisibility(View.GONE);
                    if (pageNo == 1) {
                        robotList.clear();
                    }
                    robotList.addAll(list);
                    adapter.notifyDataSetChanged();
                    lv_concern.reflashComplete();
                    lv_concern.loadCompleted();
                    if (list.size() < 15 && moreCount == 1) {
                        lv_concern.setEnableScroll(false);
                    } else {
                        lv_concern.setEnableScroll(true);
                    }
                    moreCount++;
                } else {
                    if (pageNo == 1) {
                        ll_no_fans.setVisibility(View.VISIBLE);
                    } else {
                        showShortToast("无更多数据");
                        lv_concern.loadCompleted();
                    }
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("请求超时！");
                if (isFromRefresh) {
                    lv_concern.reflashComplete();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            } else {
                // Permission Denied
                //showShortToast("请到设置中开启位置权限");
                Utils.showMissingPermissionDialog(getActivity(), "位置");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }

    @Override
    public void onLoad() {
        getRobotNearByList(moreCount, true);
    }


    private void showDialog(final RobotConcern robotConcern) {
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.setTitle("提示");
        myDialog.setMessage("确定要取消对" + robotConcern.getRobotName() + "的关注吗");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                showLoadingDialog("请求中");
                Diting.deleteConcern(robotConcern.getPhone(), new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        dismissLoadingDialog();
                        showShortToast("已取消关注");
                        EventBus.getDefault().post("update");
                        robotConcern.setConcerned(false);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        dismissLoadingDialog();
                        showShortToast("取消失败，请稍后再试");
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

    @Override
    public void onRefresh() {
        moreCount = 1;
        robotList.clear();
        getRobotNearByList(true);
    }
}
