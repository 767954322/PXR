package com.diting.voice.utils;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/10, 11:19.
 * Description: 常量.
 */

public interface Constants {

    /** debug mode. */
    boolean DEBUG_MODE = true;
    boolean DEBUG_SYS_OUT = true;

    /** voice type. */
    String VOICE_TYPE_MICROSOFT = "microsoft"; // 微软语音合成.
    String VOICE_TYPE_TENCENT = "tencent"; // 腾讯语音合成.
    String VOICE_TYPE_XFYUN = "xfyun"; // 讯飞语音合成.
    String VOICE_TYPE_BAIDU = "baidu"; // 百度语音合成.

    /** Microsoft constants. */
    String MICROSOFT_API_KEY = "6861eb8e626d44e1a51e804fffdf548f"; // API KEY
    String MICROSOFT_SERVICE_URL = "https://speech.platform.bing.com/synthesize"; // SERVICE URL
    String MICROSOFT_OUT_PUT_FORMAT = "raw-16khz-16bit-mono-pcm"; // OUT PUT FORMAT
    String MICROSOFT_CONTENT_TYPE = "application/ssml+xml"; // CONTENT TYPE
    String MICROSOFT_SEARCH_APP_ID = "07D3234E49CE426DAA29772419F436CA"; // SEARCH APP ID
    String MICROSOFT_SEARCH_CLIENT_ID = "1ECFAE91408841A480F00935DC390960"; // SEARCH CLIENT ID
    String MICROSOFT_USER_AGENT = "TTSAndroid"; // USER AGENT
    int MICROSOFT_CONNECT_TIME_OUT = 5000; // CONNECT TIME OUT
    int MICROSOFT_READ_TIME_OUT = 15000; // READ TIME OUT

    /** xnufei constants. */
    String XFYUN_APP_ID = "57e87237"; // 科大讯飞APP ID.
    String XFYUN_TTS_VOICE = "aisbabyxu"; // 默认发音人.
    String XFYUN_TTS_SPEED = "50"; // 合成语音语速.
    String XFYUN_TTS_VOLUME = "100"; // 合成语音音量.
    String XFYUN_TTS_PITCH = "50"; // 合成语音音调.
    String XFYUN_TTS_TYPE = "3"; // 合成语音播放器音频流类型.
    String XFYUN_TTS_KEY_REQUEST_FOCUS = "true"; // 设置播放合成音频打断音乐播放，默认为false.
    String XFYUN_RESULT_TYPE_JSON = "json"; // 返回结果格式.
    String XFYUN_LANGUAGE_ZH_CN = "zh_cn"; // 语言-中文.
    String XFYUN_ACCENT_MANDARIN = "mandarin"; // 语言区域.
    String XFYUN_VAD_BOS_TIME = "10000"; // 静音超时时间，即用户多长时间不说话则当做超时处理.
    String XFYUN_VAD_EOS_TIME = "1500"; // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音.
    String XFYUN_ASR_PTT = "1"; // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点.
    String XFYUN_AUDIO_SOURCE = "-1"; // 音频来源, -1为外部文件.

    /** DITing constants. */
    String DITING_USERNAME_XIAODI = "4009003381"; // 小谛机器人用户名
    String DEFAULT_PASSWORD = "123456"; // 默认密码

    /** Time. */
    int MILLISECOND = 1000;
    int COMMON_COUNT_DOWN_TIME = 70 * MILLISECOND;

    /** 语音通话. */
    int HANDLER_VOICE_CALL_ANSWER = 0;
    int HANDLER_VOICE_CALL_REJECT = 1;
    int HANDLER_VOICE_CALL_END = 2;
    int HANDLER_VOICE_CALL_UPDATE_STATE = 3;
    int HANDLER_VOICE_CALL_ERROR = 4;
    int HANDLER_VOICE_CALL_PHONE = 5;
}
