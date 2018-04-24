//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Microsoft Cognitive Services (formerly Project Oxford): https://www.microsoft.com/cognitive-services
//
// Microsoft Cognitive Services (formerly Project Oxford) GitHub:
// https://github.com/Microsoft/Cognitive-Speech-TTS
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.diting.voice.tts;

import com.diting.voice.utils.Constants;
import com.diting.voice.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class TtsServiceClient {
    private Authentication m_auth;
    private byte[] m_result;

    public TtsServiceClient(String apiKey) {
        m_auth = new Authentication(apiKey);
    }

    protected void doWork(String ssml) {
        int code;
        synchronized(m_auth) {
            String accessToken = m_auth.GetAccessToken();
            try {
                URL url = new URL(Constants.MICROSOFT_SERVICE_URL);
                HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(Constants.MICROSOFT_CONNECT_TIME_OUT);
                urlConnection.setReadTimeout(Constants.MICROSOFT_READ_TIME_OUT);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", Constants.MICROSOFT_CONTENT_TYPE);
                urlConnection.setRequestProperty("X-MICROSOFT-OutputFormat", Constants.MICROSOFT_OUT_PUT_FORMAT);
                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                urlConnection.setRequestProperty("X-Search-AppId", Constants.MICROSOFT_SEARCH_APP_ID);
                urlConnection.setRequestProperty("X-Search-ClientID", Constants.MICROSOFT_SEARCH_CLIENT_ID);
                urlConnection.setRequestProperty("User-Agent", Constants.MICROSOFT_USER_AGENT);
                urlConnection.setRequestProperty("Accept", "*/*");
                byte[] ssmlBytes = ssml.getBytes();
                urlConnection.setRequestProperty("content-length", String.valueOf(ssmlBytes.length));
                urlConnection.connect();
                urlConnection.getOutputStream().write(ssmlBytes);
                code = urlConnection.getResponseCode();
                if (code == 200) {
                    InputStream in = urlConnection.getInputStream();
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int ret = in.read(bytes);
                    while (ret > 0) {
                        bout.write(bytes, 0, ret);
                        ret = in.read(bytes);
                    }
                    m_result = bout.toByteArray();
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                LogUtils.e("Exception error: " + e);
            }
        }
    }

    public byte[] SpeakSSML(final String ssml) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork(ssml);
            }
        });
        try {
            th.start();
            th.join();
        } catch (Exception e) {
            LogUtils.e("Exception error: " + e);
        }

        return m_result;
    }
}
