package com.zaaach.toprightmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Author：Bro0cL on 2016/12/26.
 */
public class TRMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MenuItem> menuItemList;
    private boolean showIcon;
    private boolean showRight;
    private int showRightPosition;
    private TopRightMenu mTopRightMenu;
    private int mItemLayoutId = -1;
    private TopRightMenu.OnMenuItemClickListener onMenuItemClickListener;

    public TRMenuAdapter(Context context, TopRightMenu topRightMenu, List<MenuItem> menuItemList, boolean show) {
        this.mContext = context;
        this.mTopRightMenu = topRightMenu;
        this.menuItemList = menuItemList;
        this.showIcon = show;
    }

    public TRMenuAdapter(Context context, TopRightMenu topRightMenu,
                         List<MenuItem> menuItemList, boolean showIcon, boolean showRight, int itemLayoutId) {
        this.mContext = context;
        this.mTopRightMenu = topRightMenu;
        this.menuItemList = menuItemList;
        this.showIcon = showIcon;
        this.showRight = showRight;
        this.mItemLayoutId = itemLayoutId;
    }

    public void setData(List<MenuItem> data) {
        menuItemList = data;
        notifyDataSetChanged();
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        notifyDataSetChanged();
    }

    public void setShowRight(boolean showRight, int position) {
        this.showRight = showRight;
        this.showRightPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mItemLayoutId != -1 ?
                mItemLayoutId : R.layout.trm_item_popup_menu_list, parent, false);
        return mItemLayoutId != -1 ? new ViewHolder(view) : new TRMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mItemLayoutId == -1) {
            TRMViewHolder trmViewHolder = (TRMViewHolder) holder;
            final MenuItem menuItem = menuItemList.get(position);
            if (menuItem.isHasPoint()) {
                trmViewHolder.point.setVisibility(View.VISIBLE);
            } else {
                trmViewHolder.point.setVisibility(View.GONE);
            }
            if (showIcon) {
                trmViewHolder.icon.setVisibility(View.VISIBLE);
                int resId = menuItem.getIcon();
                trmViewHolder.icon.setImageResource(resId < 0 ? 0 : resId);
            } else {
                trmViewHolder.icon.setVisibility(View.GONE);
            }
            trmViewHolder.text.setText(menuItem.getText());

            if (position == 0) {
                trmViewHolder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_top_pressed));
            } else if (position == menuItemList.size() - 1) {
                trmViewHolder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_bottom_pressed));
            } else {
                trmViewHolder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_middle_pressed));
            }
            final int pos = holder.getAdapterPosition();
            trmViewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null) {
                        mTopRightMenu.dismiss();
                        onMenuItemClickListener.onMenuItemClick(pos);
                    }
                }
            });
        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            final int pos = holder.getAdapterPosition();
            final MenuItem menuItem = menuItemList.get(position);

            if (showIcon) {
                viewHolder.icon.setVisibility(View.VISIBLE);
                int resId = menuItem.getIcon();
                viewHolder.icon.setImageResource(resId < 0 ? 0 : resId);
            } else {
                viewHolder.icon.setVisibility(View.GONE);
            }

            if (showRight && pos == showRightPosition) {
                viewHolder.right.setVisibility(View.VISIBLE);
                int resId = menuItem.getRightIcon();
                viewHolder.right.setImageResource(resId < 0 ? 0 : resId);
            } else {
                viewHolder.right.setVisibility(View.GONE);
            }

            viewHolder.text.setText(menuItem.getText());
            if (position == 0) {
                viewHolder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_top_pressed));
            } else if (position == menuItemList.size() - 1) {
                viewHolder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_bottom_pressed));
            } else {
                viewHolder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_middle_pressed));
            }

            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null) {
                        mTopRightMenu.dismiss();
                        onMenuItemClickListener.onMenuItemClick(pos);
                    }
                }
            });
        }
    }

    private StateListDrawable addStateDrawable(Context context, int normalId, int pressedId) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = normalId == -1 ? null : context.getResources().getDrawable(normalId);
        Drawable pressed = pressedId == -1 ? null : context.getResources().getDrawable(pressedId);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{}, normal);
        return sd;
    }

    @Override
    public int getItemCount() {
        return menuItemList == null ? 0 : menuItemList.size();
    }

    class TRMViewHolder extends RecyclerView.ViewHolder {
        ViewGroup container;
        ImageView icon;
        TextView text;
        View point;

        TRMViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView;
            icon = (ImageView) itemView.findViewById(R.id.trm_menu_item_icon);
            text = (TextView) itemView.findViewById(R.id.trm_menu_item_text);
            point = itemView.findViewById(R.id.point);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup container;
        ImageView icon;
        TextView text;
        ImageView right;

        ViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView;
            icon = (ImageView) itemView.findViewById(R.id.trm_menu_item_icon);
            text = (TextView) itemView.findViewById(R.id.trm_menu_item_text);
            right = (ImageView) itemView.findViewById(R.id.ivRight);
        }
    }

    public void setOnMenuItemClickListener(TopRightMenu.OnMenuItemClickListener listener) {
        this.onMenuItemClickListener = listener;
    }
}
