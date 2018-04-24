package com.diting.pingxingren.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import com.diting.pingxingren.R;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 02 - 02, 01:24.
 * Description: .
 */

public class VideoThumbLoaderUtil {

    private volatile static VideoThumbLoaderUtil sThumbLoaderUtil;

    public static VideoThumbLoaderUtil getInstance() {
        if (sThumbLoaderUtil == null) {
            synchronized (VideoThumbLoaderUtil.class) {
                if (sThumbLoaderUtil == null) {
                    sThumbLoaderUtil = new VideoThumbLoaderUtil();
                }
            }
        }

        return sThumbLoaderUtil;
    }

    private LruCache<String, Bitmap> mLruCache;

    public VideoThumbLoaderUtil() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void addVideoThumbToCache(String path, Bitmap bitmap) {
        if (getVideoThumbToCache(path) == null)
            mLruCache.put(path, bitmap);
    }

    public Bitmap getVideoThumbToCache(String path) {
        return mLruCache.get(path);
    }

    public void showThumbByAsyncTack(String path, ImageView imgView) {
        Bitmap bitmap = getVideoThumbToCache(path);
        if (bitmap == null) {
            imgView.setImageResource(R.drawable.ic_preloading);
            new VideoThumbLoaderTask(imgView, path).execute();
        } else {
            imgView.setImageBitmap(bitmap);
        }
    }

    class VideoThumbLoaderTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        private String mVideoPath;

        public VideoThumbLoaderTask(ImageView imageView, String path) {
            mImageView = imageView;
            mVideoPath = path;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = AnnexUtil.getNetWorkVideoThumb(mVideoPath);
            if (getVideoThumbToCache(mVideoPath) == null) {
                addVideoThumbToCache(mVideoPath, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mImageView.getTag().equals(mVideoPath)) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }
}
