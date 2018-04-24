package com.diting.pingxingren.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 05, 14:07.
 * Description: .
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIWXApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        mIWXApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXApi.handleIntent(intent, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = baseResp.errCode;
            switch (code) {
                case 0:
                    EventBus.getDefault().post("paySuccess");
                    ToastUtils.showShortToastSafe("支付成功");
                    finish();
                    break;
                case -1:
                    ToastUtils.showShortToastSafe("支付失败");
                    finish();
                    break;
                case -2:
                    EventBus.getDefault().post("payCancel");
                    ToastUtils.showShortToastSafe("支付取消");
                    finish();
                    break;
                default:
                    ToastUtils.showShortToastSafe("支付失败");
                    finish();
                    break;
            }
        }
    }
}
