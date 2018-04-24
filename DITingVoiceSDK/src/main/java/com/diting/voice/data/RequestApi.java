package com.diting.voice.data;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/10, 17:52.
 * Description: 请求Api接口.
 */

public interface RequestApi {

    long TIME_OUT = 10;

    //String BASE_HOST = "192.168.3.73:3100";
    String BASE_HOST = "http://www.ditingai.com/";
    String HEADERS_JSON = "Content-Type: application/json";

    String TURNED_CHAT_INFO = "remote/chat/info";
    String CALL_INFO_SAVE = "call/save";


    String QR_CODE_HOST = "http://m.ditingai.com/robot-company/";
    String QR_CODE_REGISTER = BASE_HOST + "m/register";

}
