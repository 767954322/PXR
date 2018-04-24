package com.diting.pingxingren.util;

import android.os.Environment;

import java.nio.charset.Charset;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 11, 16:29.
 * Description: .
 */

public interface Constants {

    int CHAT_TYPE_SENT_TEXT = 0;
    int CHAT_TYPE_SENT_VOICE = 1;
    int CHAT_TYPE_RECEIVED_TEXT = 2;
    int CHAT_TYPE_RECEIVED_IMAGE = 3;
    int CHAT_TYPE_RECEIVED_TEXT_AND_IMAGE = 4;
    int CHAT_TYPE_RECEIVED_BOOK = 5;
    int CHAT_TYPE_RECEIVED_VOICE = 6;
    int CHAT_TYPE_RECEIVED_MUSIC = 7;
    int CHAT_TYPE_RECEIVED_REMIND = 8;
    int CHAT_TYPE_RECEIVED_FOOD = 9;
    int CHAT_TYPE_RECEIVED_MAIL = 10;
    int CHAT_TYPE_RECEIVED_VIDEO = 11;
    int CHAT_TYPE_RECEIVED_AUDIO = 12;
    int CHAT_TYPE_RECEIVED_FILE = 13;
    int CHAT_TYPE_RECEIVED_HYPERLINK = 14;

    int REQUEST_CODE_STORAGE = 0;
    int REQUEST_CODE_STORAGE_PHOTO = 1;
    int REQUEST_CODE_STORAGE_VIDEO = 2;
    int REQUEST_CODE_CAMERA = 3;
    int REQUEST_CODE_CAMERA_AND_STORAGE = 4;
    int REQUEST_CODE_RECORD_AUDIO = 5;
    int REQUEST_CODE_CALL_AUDIO = 6;
    int REQUEST_CODE_CALL_VIDEO = 7;
    int REQUEST_CODE_LOCATION = 8;
    int REQUEST_CODE_CALENDAR = 9;
    int REQUEST_CODE_CALENDAR_ADD = 10;
    int REQUEST_CODE_CALENDAR_DELETE = 11;

    int MODE_DAY = 1;//每日新闻的日间模式
    int MODE_NIGHT = 2;//每日新闻的夜间模式

    String FILE_PATH = Environment.getExternalStorageDirectory() + "/DTing";
    String FILE_PATH_FILES = FILE_PATH + "/files";
    String FILE_PATH_IMAGES = FILE_PATH_FILES + "/images";

    String DB_NAME = "db_chat";
    int DB_VERSION = 3;
    String TABLE_CHAT_NAME = "chat";

    int REQUEST_CODE = 999;

    String XIAODI_UNIQUEID = "7818c7f73c31468ca03c63b7375994b9";

    /******************** 存储相关常量 ********************/
    /**
     * KB与Byte的倍数
     */
    int KB = 1024;
    /**
     * MB与Byte的倍数
     */
    int MB = 1048576;
    /**
     * GB与Byte的倍数
     */
    int GB = 1073741824;

    enum MemoryUnit {
        BYTE,
        KB,
        MB,
        GB
    }

    /******************** 时间相关常量 ********************/
    /**
     * 秒与毫秒的倍数
     */
    int SEC  = 1000;
    /**
     * 分与毫秒的倍数
     */
    int MIN  = 60000;
    /**
     * 时与毫秒的倍数
     */
    int HOUR = 3600000;
    /**
     * 天与毫秒的倍数
     */
    int DAY  = 86400000;

    enum TimeUnit {
        MSEC,
        SEC,
        MIN,
        HOUR,
        DAY
    }

    /**
     * 正则：手机号（简单）
     */
    String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    String REGEX_MOBILE_EXACT  = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
    /**
     * 正则：电话号码
     */
    String REGEX_TEL           = "^0\\d{2,3}[- ]?\\d{7,8}";
    /**
     * 正则：身份证号码15位
     */
    String REGEX_ID_CARD15     = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 正则：身份证号码18位
     */
    String REGEX_ID_CARD18     = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * 正则：邮箱
     */
    String REGEX_EMAIL         = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 正则：URL
     */
    String REGEX_URL           = "[a-zA-z]+://[^\\s]*";
    /**
     * 正则：汉字
     */
    String REGEX_ZH            = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    String REGEX_USERNAME      = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * 正则：密码，取值范围为a-z,A-Z,0-9，密码必须是6-16位
     */
    String REGEX_USER_PASS      = "^\\w{6,16}$";
    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    String REGEX_DATE          = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * 正则：IP地址
     */
    String REGEX_IP            = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    /**
     * 正则：双字节字符(包括汉字在内)
     */
    String REGEX_DOUBLE_BYTE_CHAR     = "[^\\x00-\\xff]";
    /**
     * 正则：空白行
     */
    String REGEX_BLANK_LINE           = "\\n\\s*\\r";
    /**
     * 正则：QQ号
     */
    String REGEX_TENCENT_NUM          = "[1-9][0-9]{4,}";
    /**
     * 正则：中国邮政编码
     */
    String REGEX_ZIP_CODE             = "[1-9]\\d{5}(?!\\d)";
    /**
     * 正则：正整数
     */
    String REGEX_POSITIVE_INTEGER     = "^[1-9]\\d*$";
    /**
     * 正则：负整数
     */
    String REGEX_NEGATIVE_INTEGER     = "^-[1-9]\\d*$";
    /**
     * 正则：整数
     */
    String REGEX_INTEGER              = "^-?[1-9]\\d*$";
    /**
     * 正则：非负整数(正整数 + 0)
     */
    String REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$";
    /**
     * 正则：非正整数（负整数 + 0）
     */
    String REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$";
    /**
     * 正则：正浮点数
     */
    String REGEX_POSITIVE_FLOAT       = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /**
     * 正则：负浮点数
     */
    String REGEX_NEGATIVE_FLOAT       = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";
    /**
     * 正则：纯数字
     */
    String REGEX_ONLY_NUMBER          = "^\\d+$";
    /**
     * 所有都是UTF-8编码
     */
    Charset UTF_8 = Charset.forName("UTF-8");

    String WX_APP_ID = "wxc6fbeded8b899a3b";
    String WX_TRADE_TYPE = "APP";
    String WX_PARTNER_ID = "1495980282";
    String WX_PARTNER_KEY = "BeijingDitingpingxingren20180108";
}
