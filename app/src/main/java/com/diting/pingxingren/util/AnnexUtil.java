package com.diting.pingxingren.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import com.diting.pingxingren.BuildConfig;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.AddKnowledgeActivity;
import com.diting.pingxingren.easypermissions.EasyPermissions;
import com.diting.pingxingren.smarteditor.listener.ClickListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 06, 11:35.
 * Description: 附件选择Util.
 */

public class AnnexUtil {
    public static final int COPY_LINK = 0;
    public static final int CHOOSE_PICTURE = 1;
    public static final int CHOOSE_VIDEO = 2;
    public static final int TAKE_PICTURE = 3;
    public static final int CHOOSE_AUDIO = 4;
    public static final int CHOOSE_FILE = 50;
    public static final int CROP_SMALL_PICTURE = 6;
    public static final int TEXT = 7;


    public static final int PICTURE = 0;
    public static final int VIDEO = 1;
    public static final int AUDIO = 2;
    public static final int FILE = 3;
    public static final int TYPE_TAKE_PICTURE = 4;
    public static final int HYPERLINK = 5;

    public static String sTempPath;
    public static Uri sTempUri;

    public static void showChooseAnnexDialog(final Object object) {
        final Activity activity;
        final AddKnowledgeActivity addKnowledgeActivity;
        if (object instanceof Activity) {
            if (object instanceof AddKnowledgeActivity) {
                activity = (Activity) object;
                addKnowledgeActivity = (AddKnowledgeActivity) object;
            } else {
                addKnowledgeActivity = null;
                activity = (Activity) object;
            }
        } else if (object instanceof Fragment) {
            addKnowledgeActivity = null;
            activity = ((Fragment) object).getActivity();
        } else {
            throw new UnsupportedOperationException("Can not be empty...");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("选择附件");
        String[] items = {"复制链接", "选择本地照片", "选择本地视频", "拍照", /*"拍视频",*/ "音频", "文件"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case COPY_LINK:
                        copyLink(addKnowledgeActivity);
                        break;
                    case CHOOSE_PICTURE:
                        choosePhoto(activity);
                        break;
                    case CHOOSE_VIDEO:
                        chooseVideo(activity);
                        break;
                    case TAKE_PICTURE:
                        takePhoto(activity);
                        break;
                    case CHOOSE_AUDIO:
                        chooseAudio(activity);
                        break;
                    case 5:
                        chooseFile(activity);
                        break;
                }
            }
        });
        builder.create().show();
    }

    public static void showChooseImgDialog(String title, final Object object) {
        final Activity activity;
        if (object instanceof Activity) {
            activity = (Activity) object;
        } else if (object instanceof Fragment) {
            activity = ((Fragment) object).getActivity();
        } else {
            throw new UnsupportedOperationException("Can not be empty...");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        String[] items = {"选择本地照片", "拍照",};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://选照片
                        choosePhoto(activity);
                        break;
                    case 1://拍照
                        takePhoto(activity);
                        break;
                }
            }
        });
        builder.create().show();
    }

    public static void showAddContentDialog(String title, Activity activity, final ClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        String[] items = {"选择本地照片", "选择本地视频", "拍照", "文字"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickListener.onClick(null, which);
            }
        });
        builder.create().show();
    }

    //剪切板管理工具类
    private static ClipboardManager mClipboardManager;
    //剪切板Data对象
    private static ClipData mClipData;

    public static void copyLink(AddKnowledgeActivity activity) {
        if (activity != null) {
            mClipboardManager = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
            if (mClipboardManager.hasPrimaryClip()) {
                //GET贴板是否有内容
                mClipData = mClipboardManager.getPrimaryClip();
                //获取到内容
                ClipData.Item item = mClipData.getItemAt(0);
                String text = item.getText().toString();
                activity.copyLinkResult(text);
            } else {
                activity.copyLinkResult("");
            }
        }
    }

    public static void choosePhoto(Activity activity) {
        if (Utils.hasStoragePermission(activity)) {
            Intent openAlbumIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
        } else {
            EasyPermissions.requestPermissions(activity,
                    activity.getString(R.string.rationale_photo),
                    Constants.REQUEST_CODE_STORAGE_PHOTO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    public static void chooseVideo(Activity activity) {
        if (Utils.hasStoragePermission(activity)) {
            Intent openAlbumIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(openAlbumIntent, CHOOSE_VIDEO);
        } else {
            EasyPermissions.requestPermissions(activity,
                    activity.getString(R.string.rationale_photo),
                    Constants.REQUEST_CODE_STORAGE_VIDEO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    public static void takePhoto(Activity activity) {
        if (Utils.hasCameraAndStoragePermission(activity)) {
            Intent openCameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            FileUtil.createOrExistsDir(Constants.FILE_PATH_IMAGES);
            String photoPath = Constants.FILE_PATH_IMAGES + "/image-" + TimeUtil.getNowTimeMills() + ".jpg";
            File tempFile = FileUtil.getFileByPath(photoPath);
            if (tempFile != null) {
                sTempPath = tempFile.getAbsolutePath();
            }
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            if (Utils.hasN()) {
                sTempUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
                /*openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);*/
            } else {
                sTempUri = Uri.fromFile(tempFile);
            }
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, sTempUri);
            activity.startActivityForResult(openCameraIntent, TAKE_PICTURE);
        } else {
            EasyPermissions.requestPermissions(activity,
                    activity.getString(R.string.rationale_camera_and_storage),
                    Constants.REQUEST_CODE_CAMERA_AND_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public static void startPhotoZoom(Uri uri, Activity activity) {
        if (uri == null) {
            LogUtils.i("The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    public static void chooseAudio(Activity activity) {
        if (Utils.hasStoragePermission(activity)) {
            Intent openAlbumIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(openAlbumIntent, CHOOSE_AUDIO);
        } else {
            EasyPermissions.requestPermissions(activity,
                    activity.getString(R.string.rationale_audio),
                    Constants.REQUEST_CODE_STORAGE_VIDEO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    public static void chooseFile(Activity activity) {
        if (Utils.hasStoragePermission(activity)) {
            Intent openFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
            String phoneBrand = Utils.getPhoneBrand();
            if ("Meizu".equals(phoneBrand) || "GIONEE".equals(phoneBrand) || "Meitu".equals(phoneBrand)) {
                openFileIntent.setType("*/*");
            } else {
                openFileIntent.setType("application/pdf,application/msword," +
                        "application/vnd.ms-excel,application/vnd.ms-powerpoint");
            }
            openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(openFileIntent, CHOOSE_FILE);
        } else {
            EasyPermissions.requestPermissions(activity,
                    activity.getString(R.string.rationale_file),
                    Constants.REQUEST_CODE_STORAGE_VIDEO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    public static Bitmap lessenUriImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // Bitmap bitmap; //此时返回 bm 为空
        options.inJustDecodeBounds = false; //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        /*int be = (int) (options.outHeight / (float) 320);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be; //重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false 了
        bitmap = BitmapFactory.decodeFile(path, options);*/
        return BitmapFactory.decodeFile(path, options);
    }

    //图片压缩，防止oom
    public static String getImagePath(ContentResolver cr, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = cr.query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    public static String getVideoPath(ContentResolver cr, Uri uri) {
        String videoPath = null;
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if (cursor != null) {
            int pathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            videoPath = cursor.getString(pathColumnIndex);
            cursor.close();
        }
        return videoPath;
    }

    public static String getAudioPath(ContentResolver cr, Uri uri) {
        String audioPath = null;
        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID};
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if (cursor != null) {
            // int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int pathColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            audioPath = cursor.getString(pathColumnIndex);
            // int videoId = cursor.getInt(idColumnIndex);
            cursor.close();
        }
        return audioPath;
    }

    public static String getFilePath(Context context, Uri uri) {
        String filePath = uri.getPath();
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        String externalStorage = String.valueOf(Environment.getExternalStorageDirectory());
        String realPath = null;

        if ("media".equals(authority)) {
            realPath = getFilePathForUri(context.getContentResolver(), uri);
        } else if ("com.android.externalstorage.documents".equals(authority)) {
            if (Utils.hasKitkat()) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                realPath = externalStorage + "/" + split[1];
            }
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = filePath;
        } else if (filePath.contains(externalStorage)) {
            realPath = externalStorage + filePath.substring(filePath.indexOf(externalStorage), filePath.length());
        } else if (filePath.contains("/external_files")) {
            realPath = externalStorage + filePath.replace("/external_files", "");
        } else {
            realPath = null;
        }

        if (realPath != null && (realPath.endsWith(".doc")
                || realPath.endsWith(".pdf") || realPath.endsWith(".txt")
                || realPath.endsWith(".ppt") || realPath.endsWith(".xlsx")
                || realPath.endsWith(".pptx"))) {
            return realPath;
        }
        return null;
    }

    /**
     * 根据Uri获取文件的绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param fileUri
     */
    @TargetApi(19)
    public static String getFileAbsolutePath(Activity context, Uri fileUri) {
        if (context == null || fileUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, fileUri)) {
            if (isExternalStorageDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(fileUri)) {
                String id = DocumentsContract.getDocumentId(fileUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(fileUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(fileUri))
                return fileUri.getLastPathSegment();
            return getDataColumn(context, fileUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(fileUri.getScheme())) {
            return fileUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Bitmap getNetWorkVideoThumb(String videoPath) {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
        return metadataRetriever.getFrameAtTime();
    }

    private static String getFilePathForUri(ContentResolver contentResolver, Uri uri) {
        if (Utils.hasJellyBean()) {
            Cursor cursor = contentResolver.query(uri, null,
                    null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            }
        }

        return null;
    }

    public static Bitmap getLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

}
