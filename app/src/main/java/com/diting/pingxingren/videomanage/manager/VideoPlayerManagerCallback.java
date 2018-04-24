package com.diting.pingxingren.videomanage.manager;

import com.diting.pingxingren.videomanage.PlayerMessageState;
import com.diting.pingxingren.videomanage.meta.MetaData;
import com.diting.pingxingren.videomanage.ui.VideoPlayerView;

/**
 * This callback is used by {@link }
 * to get and set data it needs
 */
public interface VideoPlayerManagerCallback {

    void setCurrentItem(MetaData currentItemMetaData, VideoPlayerView newPlayerView);

    void setVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
