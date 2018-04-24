package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

import com.diting.pingxingren.util.Constants;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 19, 17:13.
 * Description: .
 */

public class UnifiedOrderBody implements Parcelable {

    private String appid;
    private String attach;
    private String body;
    private String mch_id;
    private String nonce_str;
    private String notify_url;
    private String out_trade_no;
    private String spbill_create_ip;
    private int total_fee;
    private String trade_type;
    private String sign;

    public UnifiedOrderBody(String attach,
                            String body, String nonce_str,
                            String out_trade_no, String spbill_create_ip,
                            int total_fee, String sign) {
        this.appid = Constants.WX_APP_ID;
        this.attach = attach;
        this.body = body;
        this.mch_id = Constants.WX_PARTNER_ID;
        this.nonce_str = nonce_str;
        this.notify_url = "http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php";
        this.out_trade_no = out_trade_no;
        this.spbill_create_ip = spbill_create_ip;
        this.total_fee = total_fee;
        this.trade_type = Constants.WX_TRADE_TYPE;
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appid);
        dest.writeString(this.attach);
        dest.writeString(this.body);
        dest.writeString(this.mch_id);
        dest.writeString(this.nonce_str);
        dest.writeString(this.notify_url);
        dest.writeString(this.out_trade_no);
        dest.writeString(this.spbill_create_ip);
        dest.writeInt(this.total_fee);
        dest.writeString(this.trade_type);
        dest.writeString(this.sign);
    }

    protected UnifiedOrderBody(Parcel in) {
        this.appid = in.readString();
        this.attach = in.readString();
        this.body = in.readString();
        this.mch_id = in.readString();
        this.nonce_str = in.readString();
        this.notify_url = in.readString();
        this.out_trade_no = in.readString();
        this.spbill_create_ip = in.readString();
        this.total_fee = in.readInt();
        this.trade_type = in.readString();
        this.sign = in.readString();
    }

    public static final Parcelable.Creator<UnifiedOrderBody> CREATOR = new Parcelable.Creator<UnifiedOrderBody>() {
        @Override
        public UnifiedOrderBody createFromParcel(Parcel source) {
            return new UnifiedOrderBody(source);
        }

        @Override
        public UnifiedOrderBody[] newArray(int size) {
            return new UnifiedOrderBody[size];
        }
    };
}
