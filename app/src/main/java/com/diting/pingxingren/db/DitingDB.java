package com.diting.pingxingren.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.diting.pingxingren.entity.Msg;
import com.diting.pingxingren.util.Const;

import java.util.ArrayList;
import java.util.List;

import static com.diting.pingxingren.util.Const.DB_NAME;
import static com.diting.pingxingren.util.Const.DB_VERSION;

/**
 * Created by asus on 2016/4/14.
 */
public class DitingDB {
    private static DitingDB ditingDB;
    private SQLiteDatabase db;

    private DitingDB(Context context) {
        DitingOpenHelper dbHelper = new DitingOpenHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static DitingDB getInstance(Context context) {
        if (ditingDB == null) {
            ditingDB = new DitingDB(context);
        }
        return ditingDB;
    }

    public void saveMessage(Msg msg) {
        if (msg != null) {
            ContentValues values = new ContentValues();
            values.put("content", msg.getContent());
            values.put("img_url", msg.getImgUrl());
            values.put("video_url", msg.getVideoUrl());
            values.put("audio_url", msg.getAudioUrl());
            values.put("file_url", msg.getFileUrl());
            values.put("username", msg.getUserName());
            values.put("type", msg.getType());
            values.put("time", msg.getTime());
            values.put("from_username", msg.getFromUserName());
            values.put("head_portrait", msg.getHeadPortrait());
            values.put("voice_time", msg.getVoiceTime());
            values.put("voice_path", msg.getVoicePath());
            db.insert(Const.TABLE_MESSAGE, null, values);
        }
    }

    public List<Msg> listMessage(int offset, String username) {
        if (offset < 0) {
            return null;
        }
        List<Msg> list = new ArrayList<Msg>();
        Cursor cursor = db.query(Const.TABLE_MESSAGE, null, "username=?", new String[]{username}, null, null, "id desc", offset + "," + (10 + offset));
        if (cursor.moveToFirst()) {
            do {
                Msg msg = new Msg();
                msg.setId(cursor.getInt(cursor.getColumnIndex("id")));
                msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
                msg.setImgUrl(cursor.getString(cursor.getColumnIndex("img_url")));
                msg.setVideoUrl(cursor.getString(cursor.getColumnIndex("video_url")));
                msg.setAudioUrl(cursor.getString(cursor.getColumnIndex("audio_url")));
                msg.setFileUrl(cursor.getString(cursor.getColumnIndex("file_url")));
                msg.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                msg.setFromUserName(cursor.getString(cursor.getColumnIndex("from_username")));
                msg.setType(cursor.getInt(cursor.getColumnIndex("type")));
                msg.setTime(cursor.getLong(cursor.getColumnIndex("time")));
                msg.setHeadPortrait(cursor.getString(cursor.getColumnIndex("head_portrait")));
                msg.setVoiceTime(cursor.getFloat(cursor.getColumnIndex("voice_time")));
                msg.setVoicePath(cursor.getString(cursor.getColumnIndex("voice_path")));
                list.add(msg);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Msg> listOtherMessage(int offset, String username, String fromUserName) {
        if (offset < 0) {
            return null;
        }
        List<Msg> list = new ArrayList<Msg>();
        Cursor cursor = db.query(Const.TABLE_MESSAGE, null, "username=? and from_username=?", new String[]{username, fromUserName}, null, null, "id desc", offset + "," + (10 + offset));
        if (cursor.moveToFirst()) {
            do {
                Msg msg = new Msg();
                msg.setId(cursor.getInt(cursor.getColumnIndex("id")));
                msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
                msg.setImgUrl(cursor.getString(cursor.getColumnIndex("img_url")));
                msg.setVideoUrl(cursor.getString(cursor.getColumnIndex("video_url")));
                msg.setAudioUrl(cursor.getString(cursor.getColumnIndex("audio_url")));
                msg.setFileUrl(cursor.getString(cursor.getColumnIndex("file_url")));
                msg.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                msg.setFromUserName(cursor.getString(cursor.getColumnIndex("from_username")));
                msg.setType(cursor.getInt(cursor.getColumnIndex("type")));
                msg.setTime(cursor.getLong(cursor.getColumnIndex("time")));
                msg.setHeadPortrait(cursor.getString(cursor.getColumnIndex("head_portrait")));
                msg.setVoiceTime(cursor.getFloat(cursor.getColumnIndex("voice_time")));
                msg.setVoicePath(cursor.getString(cursor.getColumnIndex("voice_path")));
                list.add(msg);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
