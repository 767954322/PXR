package com.diting.pingxingren.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.voice.data.body.Contact;
import com.diting.pingxingren.util.ColorGenerator;
import com.diting.pingxingren.util.TextDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactLocalAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
    private boolean isShowCheckBox = false;
    private Map<Integer,Boolean> checkMap = new HashMap<Integer, Boolean>();
    private List<Contact> contactList = new ArrayList<Contact>();
    private IOnClickListener onClickListener;

    public ContactLocalAdapter(int resId, List<Contact> list) {
        super(resId,list);
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, final Contact item) {
        TextDrawable drawable = mDrawableBuilder.build(String.valueOf(item.getName().charAt(0)), mColorGenerator.getColor(item.getName()));
        viewHolder.setText(R.id.tv_robot, item.getName())
                .setText(R.id.tv_phone,item.getPhone());
        ((ImageView) viewHolder.getView(R.id.image)).setImageDrawable(drawable);
        //((ImageView)viewHolder.getView(R.id.image)).setImageResource(R.mipmap.icon_right);
        final int position = viewHolder.getLayoutPosition();
        final CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.checkbox);
        final LinearLayout contactLayout = (LinearLayout)viewHolder.getView(R.id.ll_contact);
        if(isShowCheckBox){
            checkBox.setVisibility(View.VISIBLE);
        }else {
            checkBox.setVisibility(View.GONE);
        }
        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowCheckBox(true);
                checkBox.setChecked(!checkBox.isChecked());
                onClickListener.onClick();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        contactList.add(item);
                        checkMap.put(position, true);
                    }else{
                        contactList.remove(item);
                        checkMap.remove(position);
                    }
            }
        });
        checkBox.setChecked(checkMap.get(position) != null);
        //Glide.with(mContext).load(item.getHeadImgUrl()).crossFade().into((ImageView) viewHolder.getView(R.id.iv));
        if(item.getIsUploaded()==1){
            viewHolder.setVisible(R.id.tv_uploaded,true);
            contactLayout.setEnabled(false);
            viewHolder.getView(R.id.checkbox).setEnabled(false);
        }else {
            contactLayout.setEnabled(true);
            viewHolder.setVisible(R.id.tv_uploaded,false);
            viewHolder.getView(R.id.checkbox).setEnabled(true);
        }
    }

    public void checkAll(){
        for(int i = 0;i < mData.size();i++){
            checkMap.put(i,true);
        }
        notifyDataSetChanged();
    }
    public void unCheckAll(){
        checkMap.clear();
    }

    public Map<Integer, Boolean> getCheckMap() {
        return checkMap;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public int getSelectCount(){
        return contactList.size();
    }

    public interface IOnClickListener{
        void onClick();
    }

    public void setOnClickListener(IOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}