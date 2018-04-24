package com.diting.pingxingren.util;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.diting.pingxingren.util.AudioManager.MSG_ERROR_AUDIO_RECORD;
import static com.diting.pingxingren.util.Const.VOICE_PATH;

/**
 * Created by asus on 2017/3/23.
 */

public class MyAudioManager {
    private static final String TAG = "MyAudioManager";
    private boolean isRecording = false;
    private String fileName;
    private Handler handler;
    private AudioRecord audioRecord;

    private static MyAudioManager mInstance;
    public static MyAudioManager getInstance() {
        if (mInstance == null) {
            synchronized (MyAudioManager.class) {
                if (mInstance == null) {
                    mInstance = new MyAudioManager();

                }
            }
        }
        return mInstance;
    }

    public void StartRecord() {
        Log.i(TAG,"开始录音");
//16K采集率
        final int frequency = 16000;
//格式
        final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
//16Bit
        final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
//生成PCM文件
        File dir = new File(VOICE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName + ".pcm");
        Log.i(TAG,"生成文件");
//如果存在，就先删除再创建
        if (file.exists())
            file.delete();
        Log.i(TAG,"删除文件");
        try {
            file.createNewFile();
            Log.i(TAG,"创建文件");
        } catch (IOException e) {
            Log.i(TAG,"未能创建");
            e.printStackTrace();
            throw new IllegalStateException("未能创建" + file.toString());
        }
        try {
//输出流
            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            final DataOutputStream dos = new DataOutputStream(bos);
            final int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);
            final  short[] buffer = new short[bufferSize];
//                    if(audioRecord == null) {
//                        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);
//                    }
                    audioRecord.startRecording();
                    Log.i(TAG, "开始录音");
                    if (mListener != null) {
                        mListener.wellPrepared();
                    }
                    isRecording = true;
                    while (isRecording) {
                        int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                        for (int i = 0; i < bufferReadResult; i++) {
                            try {
                                dos.writeShort(buffer[i]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    audioRecord.stop();
                    try {
                        dos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        } catch (Throwable t) {
            Log.e(TAG, "录音失败");
            if (handler != null) {
                handler.sendEmptyMessage(MSG_ERROR_AUDIO_RECORD);
            }
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String dir) {
        this.fileName = dir;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    // 释放资源
    public void release() {
        // 严格按照api流程进行
        if (null != audioRecord) {
            audioRecord = null;
        }
    }

    public void cancel() {
        release();
        if (fileName != null) {
            File file = new File(fileName);
            file.delete();
            fileName = null;
        }

    }

    public interface AudioStageListener {
        void wellPrepared();
    }

    public AudioStageListener mListener;

    public AudioStageListener getmListener() {
        return mListener;
    }

    public void setmListener(AudioStageListener mListener) {
        this.mListener = mListener;
    }

    public String getFilePath(){
        return Const.VOICE_PATH + fileName + ".pcm";
    }
}
