package com.diting.pingxingren.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.diting.pingxingren.entity.ChatMessageItem;
import com.diting.pingxingren.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 25, 11:56.
 * Description: .
 */

public class DTingDBUtil {

    private static DTingDBUtil sDTingDBUtil;
    private SQLiteDatabase mDatabase;

    public synchronized static DTingDBUtil getInstance(Context context) {
        if (null == sDTingDBUtil) {
            synchronized (DTingDBUtil.class) {
                sDTingDBUtil = new DTingDBUtil(context);
            }
        }
        return sDTingDBUtil;
    }

    public DTingDBUtil(Context context) {
        DTingDBHelper dTingDBHelper = new DTingDBHelper(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        mDatabase = dTingDBHelper.getWritableDatabase();
    }

    public void addMessage(ChatMessageItem msg) {
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
            values.put("robotName", msg.getRobotName());
            values.put("hyperlink", msg.getHyperlinkUrl());
            mDatabase.insert(Constants.TABLE_CHAT_NAME, null, values);
        }
    }

    public List<ChatMessageItem> queryMessage(int offset, String username, String robotName) {
        if (offset < 0) {
            return null;
        }
        List<ChatMessageItem> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(Constants.TABLE_CHAT_NAME, null,
                "username=? AND robotName=?", new String[]{username, robotName}, null, null,
                "id desc", offset + "," + (10 + offset));
        if (cursor.moveToFirst()) {
            do {
                ChatMessageItem msg = new ChatMessageItem();
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
                msg.setHyperlinkUrl(cursor.getString(cursor.getColumnIndex("hyperlink")));
                list.add(msg);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
