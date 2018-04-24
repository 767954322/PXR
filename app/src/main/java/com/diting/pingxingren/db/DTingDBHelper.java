package com.diting.pingxingren.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.diting.pingxingren.util.Constants;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 25, 11:55.
 * Description: .
 */

public class DTingDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE " + Constants.TABLE_CHAT_NAME + " ("
            + "id integer primary key autoincrement, "
            + "content text,"
            + "img_url text,"
            + "video_url text,"
            + "audio_url text,"
            + "file_url text,"
            + "username text,"
            + "from_username text,"
            + "time text,"
            + "head_portrait text,"
            + "voice_time REAL,"
            + "voice_path text,"
            + "type integer,"
            + "robotName text,"
            + "hyperlink text)";

    public DTingDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("ALTER TABLE chat RENAME TO chat_temp");
            db.execSQL(CREATE_TABLE_MESSAGE);
            db.execSQL("insert into chat (id, content, img_url, video_url, audio_url, file_url, username," +
                    "from_username, time, head_portrait, voice_time, voice_path, type) select id," +
                    "content, img_url, video_url, audio_url, file_url, username, " +
                    "from_username, time, head_portrait, voice_time, voice_path, type from chat_temp");
            db.execSQL("DROP TABLE chat_temp");
        } else if (oldVersion == 2) {
            db.execSQL("ALTER TABLE chat RENAME TO chat_temp");
            db.execSQL(CREATE_TABLE_MESSAGE);
            db.execSQL("insert into chat (id, content, img_url, video_url, audio_url, file_url, username," +
                    "from_username, time, head_portrait, voice_time, voice_path, type, robotName) select id," +
                    "content, img_url, video_url, audio_url, file_url, username, " +
                    "from_username, time, head_portrait, voice_time, voice_path, type, robotName from chat_temp");
            db.execSQL("DROP TABLE chat_temp");
        }
    }
}
