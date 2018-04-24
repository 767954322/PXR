package com.diting.pingxingren.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.SystemMessageItemModel;
import com.diting.pingxingren.util.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class SystemMailAdapter  extends BaseQuickAdapter<SystemMessageItemModel, BaseViewHolder> {
    private MailDetailListener mailDetailListener;
    private boolean isShowPoint;

    public void setShowPoint(boolean showPoint) {
        isShowPoint = showPoint;
    }

    public SystemMailAdapter(int layoutResId, @Nullable List<SystemMessageItemModel> data ) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder holder, final SystemMessageItemModel item) {
        RelativeLayout rl_mail =   holder.getView(R.id.rl_mail);
          View point = holder.getView(R.id.point);
        ImageView iv_delete=holder.getView(R.id.iv_delete);
        holder.setText(R.id.tv_company_name, item.getBiaoti());
        holder.setText(R.id.tv_time, TimeUtil.millis2String(item.getCreatedTime(), TimeUtil.PATTERN_YMD));
        iv_delete.setVisibility(View.GONE);
        if (item.getMail_phone().size() == 0) {
            isShowPoint=false;
        } else {
            isShowPoint=item.getMail_phone().get(0).getFlg().equals("666");
        }


        if (!item.isIdRead()) {
            if (isShowPoint) {
                point.setVisibility(View.GONE);
            } else {
                point.setVisibility(View.VISIBLE);
            }
        } else {
            point.setVisibility(View.GONE);
        }



//        if (isShowPoint) {
//            point.setVisibility(View.INVISIBLE);
//        } else {
//            point.setVisibility(View.VISIBLE);
//        }

        rl_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mailDetailListener.gotoSysMailDetail(item);
            }
        });
    }


    public interface  MailDetailListener{
        void gotoSysMailDetail(SystemMessageItemModel  systemMessageItemModel);
    }

    public void setMailDetailListener( MailDetailListener mailDetailListener) {
        this.mailDetailListener = mailDetailListener;
    }
}
