package com.diting.pingxingren.videomanage.playermessages;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.diting.pingxingren.videomanage.manager.VideoPlayerManagerCallback;
import com.diting.pingxingren.videomanage.ui.VideoPlayerView;

import java.io.FileDescriptor;

/**
 * This PlayerMessage calls {@link MediaPlayer#setDataSource(FileDescriptor)} on the instance that is used inside {@link VideoPlayerView}
 */
public class SetAssetsDataSourceMessage extends SetDataSourceMessage{

    private final AssetFileDescriptor mAssetFileDescriptor;

    public SetAssetsDataSourceMessage(VideoPlayerView videoPlayerView, AssetFileDescriptor assetFileDescriptor, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
        mAssetFileDescriptor = assetFileDescriptor;
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.setDataSource(mAssetFileDescriptor);
    }
}
