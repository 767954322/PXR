package com.diting.pingxingren.adapter;


import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.ImageCallBack;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.voice.data.body.VoiceCallInfo;
import com.diting.voice.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallLogAdapter extends BaseQuickAdapter<VoiceCallInfo, BaseViewHolder> {

private CallRecorditenListener callRecorditenListener;


    public CallLogAdapter(int resId, List<VoiceCallInfo> list) {
        super(resId,list);
    }


    @Override
    protected void convert(final BaseViewHolder viewHolder, final VoiceCallInfo item) {
        String robotName = null;
        String companyName = null;
        String headImgUrl = null;
        int imageId = R.drawable.icon_call_out;
        if(item.getFromUserName().equals(MySharedPreferences.getUsername())){
            robotName = item.getToRobotName();
            companyName = item.getToCompanyName();
            headImgUrl = item.getToheadImgUrl();
        }else {
            if(item.getCallType().equals(VoiceCallInfo.CallType.VOICE.toString())){
                switch (item.getEndType()){
                    case "NORMAL":
                        imageId =  R.drawable.icon_call_in;    //正常呼入
                        break;
                    case "REJECT":
                        imageId =  R.drawable.icon_call_reject;    //拒接
                        break;
                    case "NORESPONSE":
                        imageId =  R.drawable.icon_call_noresponse;   //未接
                        break;
                }
            }
            robotName = item.getFromRobotName();
            companyName = item.getFromCompanyName();
            headImgUrl = item.getFromHeadImgUrl();
        }
        viewHolder.setText(R.id.tv_robot_name, robotName)
                .setText(R.id.tv_company_name,companyName)
                .setText(R.id.tv_start_time,item.getStartTime())
                .setText(R.id.tv_time, Utils.second2time(item.getCallTime()));
        ((ImageView) viewHolder.getView(R.id.iv_state)).setImageResource(imageId);
        //((ImageView)viewHolder.getView(R.id.image)).setImageResource(R.mipmap.icon_right);
        CircleImageView imageView = (CircleImageView)viewHolder.getView(R.id.iv_photo);
        LinearLayout llItem=viewHolder.getView(R.id.llItem);
        if(com.diting.pingxingren.util.Utils.isEmpty(headImgUrl)){
            imageView.setImageResource(R.mipmap.icon_left);
        }else {
            Glide.with(mContext).load(headImgUrl).into(imageView);
        }
        llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRecorditenListener.onCallRecordItemClick(item);
            }
        });
    }

    public interface  CallRecorditenListener{
        void onCallRecordItemClick(VoiceCallInfo voiceCallInfo) ;
    }

    public void setCallRecorditenListener(CallRecorditenListener callRecorditenListener) {
        this.callRecorditenListener = callRecorditenListener;
    }
}