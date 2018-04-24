package com.diting.voice.data;

import com.diting.voice.data.body.ChatRequestBody;
import com.diting.voice.data.body.VoiceCallInfo;
import com.diting.voice.data.model.CommonInfoModel;
import com.diting.voice.data.model.PublicChatInfoModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 10:46.
 * Description: Api请求服务.
 */

public interface ApiService {

    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.TURNED_CHAT_INFO)
    Observable<PublicChatInfoModel> getPublicChatInfo(@Body ChatRequestBody chatRequestBody);

    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.CALL_INFO_SAVE)
    Observable<CommonInfoModel> saveCallInfo(@Body VoiceCallInfo voiceCallInfo);
}
