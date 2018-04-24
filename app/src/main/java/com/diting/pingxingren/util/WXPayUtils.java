package com.diting.pingxingren.util;

import android.util.Xml;

import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.model.WXPayModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 19, 10:18.
 * Description: .
 */

public class WXPayUtils {

    private static WXPayUtils sWXPayUtils;
    private IWXAPI mIWXApi;

    public static WXPayUtils getInstance() {
        if (sWXPayUtils == null) {
            synchronized (WXPayUtils.class) {
                if (sWXPayUtils == null)
                    sWXPayUtils = new WXPayUtils();
            }
        }
        return sWXPayUtils;
    }

    private WXPayUtils() {
        mIWXApi = WXAPIFactory.createWXAPI(MyApplication.getInstance(), Constants.WX_APP_ID);
        mIWXApi.registerApp(Constants.WX_APP_ID);
    }

    public boolean isInstalled(){
        return mIWXApi.isWXAppInstalled();
    }

    public boolean isSupport(){
        return mIWXApi.isWXAppSupportAPI();
    }

    public boolean isWXAppInstalledAndSupported() {
        return mIWXApi.isWXAppInstalled() && mIWXApi.isWXAppSupportAPI();
    }

    public void unifiedOrder(double totalFee, ResultCallBack resultCallBack) {
        String attach = "创建子机器人";
        String body = "平行人-子机器人创建";
        String nonce_str = MD5Utils.MD5(Utils.getRandomString(32));
        String notify_url = "http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php";
        String out_trade_no = createOutTradeNo();
        String spbill_create_ip = ShellUtils.getIP();

        List<WXPayModel> wxPayModels = new ArrayList<>();
        wxPayModels.add(new WXPayModel("appid", Constants.WX_APP_ID));
        wxPayModels.add(new WXPayModel("attach", attach));
        wxPayModels.add(new WXPayModel("body", body));
        wxPayModels.add(new WXPayModel("mch_id", Constants.WX_PARTNER_ID));
        wxPayModels.add(new WXPayModel("nonce_str", nonce_str));
        wxPayModels.add(new WXPayModel("notify_url", notify_url));
        wxPayModels.add(new WXPayModel("out_trade_no", out_trade_no));
        wxPayModels.add(new WXPayModel("spbill_create_ip", spbill_create_ip));
        wxPayModels.add(new WXPayModel("total_fee", (int) (totalFee * 100)));
        wxPayModels.add(new WXPayModel("trade_type", Constants.WX_TRADE_TYPE));
        wxPayModels.add(new WXPayModel("sign", getSign(wxPayModels)));

        resultCallBack.onResult(wxPayModels.get(6));
        String requestBody = null;
        try {
            requestBody = new String(toXml(wxPayModels).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (Utils.isNotEmpty(requestBody))
            ApiManager.wxUnifiedOrder(requestBody, resultCallBack);
        else
            resultCallBack.onError("请求失败.");
    }

    public void pay(String prepayId) {
        PayReq payReq = new PayReq();
        payReq.appId = Constants.WX_APP_ID;
        payReq.nonceStr = MD5Utils.MD5(Utils.getRandomString(32));
        ;
        payReq.packageValue = "Sign=WXPay";
        payReq.partnerId = Constants.WX_PARTNER_ID;
        payReq.prepayId = prepayId;
        payReq.timeStamp = String.valueOf(TimeUtil.getNowTimeMills() / 1000);

        List<WXPayModel> wxPayModels = new ArrayList<>();
        wxPayModels.add(new WXPayModel("appid", payReq.appId));
        wxPayModels.add(new WXPayModel("noncestr", payReq.nonceStr));
        wxPayModels.add(new WXPayModel("package", payReq.packageValue));
        wxPayModels.add(new WXPayModel("partnerid", payReq.partnerId));
        wxPayModels.add(new WXPayModel("prepayid", payReq.prepayId));
        wxPayModels.add(new WXPayModel("timestamp", payReq.timeStamp));
        payReq.sign = getSign(wxPayModels);
        mIWXApi.sendReq(payReq);
    }

    public void onDestroy() {
        sWXPayUtils = null;//否则再次浸入式WXApi会为null
        mIWXApi.detach();
        mIWXApi = null;
    }

    private String createOutTradeNo() {
        return TimeUtil.getTimeStamp() + Utils.getRandomString(14);
    }

    private String getSign(List<WXPayModel> wxPayModels) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < wxPayModels.size(); i++) {
            stringBuilder.append(wxPayModels.get(i).getKey());
            stringBuilder.append('=');
            stringBuilder.append(wxPayModels.get(i).getValue());
            stringBuilder.append('&');
        }
        stringBuilder.append("key=");
        stringBuilder.append(Constants.WX_PARTNER_KEY);
        return MD5Utils.MD5(stringBuilder.toString()).toUpperCase();
    }

    private String toXml(List<WXPayModel> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getKey() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
