package com.diting.pingxingren.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.PersonalMailItemModel;
import com.diting.pingxingren.util.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class PersonalMailAdapter extends BaseQuickAdapter<PersonalMailItemModel, BaseViewHolder> {
    private PerMailDetailListener mailDetailListener;
    private boolean isShowPoint;
//    private boolean isRead = false;


    public void setShowPoint(boolean showPoint) {
        isShowPoint = showPoint;
    }

    public PersonalMailAdapter(int layoutResId, @Nullable List<PersonalMailItemModel> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder holder, final PersonalMailItemModel item) {
        RelativeLayout rl_mail = holder.getView(R.id.rl_mail);
        View point = holder.getView(R.id.point);
        ImageView iv_delete = holder.getView(R.id.iv_delete);
        holder.setText(R.id.tv_company_name, item.getForword_name());
        holder.setText(R.id.tv_time, TimeUtil.millis2String(item.getCreatedTime(), TimeUtil.PATTERN_YMD));
        iv_delete.setVisibility(View.VISIBLE);
        isShowPoint = item.getFlag().equals("1");


        if (!item.isIdRead()) {
            if (isShowPoint) {
                point.setVisibility(View.GONE);
            } else {
                point.setVisibility(View.VISIBLE);
            }
        } else {
            point.setVisibility(View.GONE);
        }

        rl_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailDetailListener.gotoPerMailDetail(item);
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailDetailListener.deletePerMail(item);
            }
        });

    }


    public interface PerMailDetailListener {
        void gotoPerMailDetail(PersonalMailItemModel personalMailItemModel);

        void deletePerMail(PersonalMailItemModel personalMailItemModel);
    }

    public void setMailDetailListener(PerMailDetailListener mailDetailListener) {
        this.mailDetailListener = mailDetailListener;
    }
}