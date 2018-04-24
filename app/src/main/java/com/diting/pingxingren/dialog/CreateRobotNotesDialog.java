package com.diting.pingxingren.dialog;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.StringAdapter;
import com.diting.pingxingren.adapter.decoration.RecyclerViewDivider;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.Utils;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 18, 13:52.
 * Description: .
 */

public class CreateRobotNotesDialog extends DefaultDialog implements View.OnClickListener {

    public CreateRobotNotesDialog(Activity activity) {
        super(activity);
        mDialog = DialogUtil.createDialog(activity, R.layout.layout_create_child_robot_notes, R.style.CustomDialog);
        mDialog.setCancelable(true);

        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            layoutParams.width = (int) (ScreenUtil.getScreenWidth(activity) * 0.8);
            // layoutParams.height = (int) (ScreenUtil.getScreenHeight(activity) * 0.5);
            dialogWindow.setAttributes(layoutParams);
        }

        RecyclerView notesRecyclerView = mDialog.findViewById(R.id.notesRecycler);
        RecyclerViewDivider viewDivider = new RecyclerViewDivider(mActivity, RecyclerViewDivider.GRID);
        viewDivider.setDividerHeight(2);
        viewDivider.setDividerColor(R.color.transparent);
        notesRecyclerView.addItemDecoration(viewDivider);
        notesRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        StringAdapter adapter = new StringAdapter(Utils.sCreateRobotNotes);
        notesRecyclerView.setAdapter(adapter);
        Button defineButton = mDialog.findViewById(R.id.define);
        defineButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.define:
                dismissDialog();
                break;
        }
    }
}
