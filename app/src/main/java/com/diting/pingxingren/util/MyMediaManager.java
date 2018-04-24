package com.diting.pingxingren.util;

import android.media.*;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by asus on 2017/3/23.
 */

public class MyMediaManager {
    private static final String TAG = "MyMediaManager";
    private static boolean isPause;

    public static void PlayRecord(String filepath, ICompleteListener listener) {
        if (filepath == null) {
            return;
        }
//        File file = new File("mnt/sdcard/diting/17-03-23-19-05.pcm");
        File file = new File(filepath);
//读取文件
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];
        byte[] sss = null;
        try {

            InputStream is = new FileInputStream(file);

//            BufferedInputStream bis = new BufferedInputStream(is);
//            DataInputStream dis = new DataInputStream(bis);
//            int i = 0;
//            while (dis.available() > 0) {
//                music[i] = dis.readShort();
//                i++;
//            }
//            dis.close();
            sss = new byte[is.available()];
            is.read(sss);
            is.close();
//            AudioTrack audioTrack = new AudioTrack(android.media.AudioManager.STREAM_MUSIC,
//                    16000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
//                    AudioFormat.ENCODING_PCM_16BIT,
//                    musicLength * 2,
//                    AudioTrack.MODE_STREAM);
            AudioTrack audioTrack = new AudioTrack(android.media.AudioManager.STREAM_MUSIC,
                    16000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    sss.length,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            //audioTrack.write(music, 0, musicLength);
            audioTrack.write(sss,0,sss.length);
            audioTrack.stop();
//            if(audioTrack.getPlayState()== AudioTrack.PLAYSTATE_STOPPED){
//                listener.onComplete();
//                audioTrack.release();
//            }
        } catch (Throwable t) {

            Log.e(TAG, "播放失败");
        }
    }

public interface ICompleteListener {
    void onComplete();

}

    //停止函数
    public static void pause() {
//        if (audioTrack!=null&&audioTrack.getPlayState()!=AudioTrack.PLAYSTATE_STOPPED) {
//            audioTrack.pause();
//            isPause=true;
//        }
    }

    public static void release() {
//        if (audioTrack!=null) {
//            audioTrack.release();
//            audioTrack=null;
//        }
    }
}
