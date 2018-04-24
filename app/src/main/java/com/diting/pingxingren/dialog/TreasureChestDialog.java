package com.diting.pingxingren.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.util.ScreenUtil;

/**
 * Creator: Gu FanFan.
 * Date: 2017/11/1, 10:27.
 * Description: .
 */

public class TreasureChestDialog extends DefaultDialog {

    private RelativeLayout mRelativeLayout;

    public TreasureChestDialog(final Activity activity, final int position) {
        super(activity);
        mDialog = DialogUtil.createDialog(activity, R.layout.layout_treasure_dialog, R.style.CustomDialog);
        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = (int) (ScreenUtil.getScreenWidth(activity) * 0.9);
            layoutParams.height = (int) (ScreenUtil.getScreenHeight(activity) * 0.9);
            dialogWindow.setAttributes(layoutParams);
        }

        mRelativeLayout = (RelativeLayout) mDialog.findViewById(R.id.rlTreasure);

        Button button = (Button) mDialog.findViewById(R.id.btHaveLearned);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                /*switch (position) {
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }*/
                activity.finish();
            }
        });
        ImageView imageView = (ImageView) mDialog.findViewById(R.id.ivClose);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }

    private int getBackgroundRes(int position) {
        switch (position) {
            case 2:
                return R.drawable.ic_treasure_chest_turned_prompt;
            case 3:
                return R.drawable.ic_treasure_chest_mail_prompt;
            case 4:
                return R.drawable.ic_treasure_chest_call_prompt;
            case 5:
                return R.drawable.ic_treasure_chest_find_prompt;
            default:
                return -1;
        }
    }

    public void setPosition(int position) {
        int backGroundRes = getBackgroundRes(position);
        if (backGroundRes != -1)
            mRelativeLayout.setBackgroundResource(backGroundRes);
    }
}
