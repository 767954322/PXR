package com.diting.pingxingren.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.Mail;
import com.diting.pingxingren.model.SystemMessageItemChildModel;
import com.diting.pingxingren.model.SystemMessageItemModel;
import com.diting.voice.data.model.PublicChatInfoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/4/11.
 */
public class MailAdapter extends BaseQuickAdapter<SystemMessageItemModel, BaseViewHolder> {
    public static final int TYPE_ROBOT = 1;
    public static final int TYPE_SYSTEM = 2;
    private int isShowDelet;
    private DeletePersonalMailListener  deletePersonalMail;
    private MailDetailListener mailDetailListener;
    private boolean isShowPoint;


    public MailAdapter(int layoutResId, @Nullable List<SystemMessageItemModel> data, int type) {
        super(layoutResId, data);
        this.isShowDelet=type;

    }

    @Override
    protected void convert(BaseViewHolder holder, final SystemMessageItemModel item) {
        RelativeLayout    rl_mail =   holder.getView(R.id.rl_mail);
        View point = holder.getView(R.id.point);
        ImageView iv_delete=holder.getView(R.id.iv_delete);
        holder.setText(R.id.tv_company_name, item.getZhengwen());
        holder.setText(R.id.tv_time, new SimpleDateFormat("yyyy-MM-dd").format(item.getCreatedTime()));
        if (isShowDelet == TYPE_ROBOT) {
            iv_delete.setVisibility(View.VISIBLE);
           iv_delete.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePersonalMail.deletePersonalMail(item);
                }
            });
        }
//        if (item.getMail_phone().size() == 0) {
//            isShowPoint=false;
//        } else {
//            isShowPoint=item.getMail_phone().get(0).getFlg().equals("666");
//        }
        if (isShowPoint) {
             point.setVisibility(View.GONE);
        } else {
            point.setVisibility(View.VISIBLE);
        }


    }

//    public MailAdapter(int resource, List<Mail> objects, int type) {
//        super(resource, objects);
//        resourceId = resource;
//        this.type = type;
//    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        final Mail mail = getItem(position);
//        View view = null;
//        final ViewHolder viewHolder;
//        if (convertView == null) {
//            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
//            viewHolder = new ViewHolder();
//            viewHolder.rl_mail = (RelativeLayout) view.findViewById(R.id.rl_mail);
//            viewHolder.point = view.findViewById(R.id.point);
//            viewHolder.tv_company_name = (TextView) view.findViewById(R.id.tv_company_name);
//            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
//            viewHolder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
//            view.setTag(viewHolder);
//        } else {
//            view = convertView;
//            viewHolder = (ViewHolder) view.getTag();
//        }
//
//        if (type == TYPE_ROBOT) {
//            viewHolder.iv_delete.setVisibility(View.VISIBLE);
//            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onClickListener.onClick(mail);
//                }
//            });
//        }
//
//        if (!mail.isRead()) {
//            viewHolder.point.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.point.setVisibility(View.GONE);
//        }
//        viewHolder.tv_company_name.setText(mail.getTitle());
//        viewHolder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(mail.getTime()));
//        return view;
//    }

//
//    static class ViewHolder {
//        RelativeLayout rl_mail;
//        View point;
//        TextView tv_company_name;
//        TextView tv_time;
//        ImageView iv_delete;
//    }

//删除按钮
    public interface DeletePersonalMailListener{
        void deletePersonalMail(SystemMessageItemModel  systemMessageItemModel);
    }

    public void setDeletePersonalMail(DeletePersonalMailListener deletePersonalMail) {
        this.deletePersonalMail = deletePersonalMail;
    }


    public interface  MailDetailListener{
        void gotoDetail(SystemMessageItemModel  systemMessageItemModel);
    }

    public void setMailDetailListener(MailDetailListener mailDetailListener) {
        this.mailDetailListener = mailDetailListener;
    }
}
