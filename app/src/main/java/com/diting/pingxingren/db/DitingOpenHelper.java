package com.diting.pingxingren.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus on 2016/4/14.
 */
public class DitingOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_MESSAGE = "create table message ("
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
            + "type integer)";
    private static final String CREATE_TABLE_INVALID = "create table invalid ("
            + "id integer primary key autoincrement, "
            + "question text,"
            + "deleted integer default 0,"
            + "answer text,"
            + "img_url text,"
            + "time text,"
            + "robot_name text,"
            + "username text)";

    public DitingOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_INVALID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("ALTER TABLE message RENAME TO message_temp");
            db.execSQL("create table message ("
                    + "id integer primary key autoincrement, "
                    + "content text,"
                    + "img_url text,"
                    + "username text,"
                    + "from_username text,"
                    + "time text,"
                    + "head_portrait text,"
                    + "voice_time REAL,"
                    + "voice_path text,"
                    + "type integer)");
            db.execSQL("insert into message(id, content, img_url, username, from_username, time,head_portrait, voice_time, voice_path,type) "
                    + "select id, content, img_url, username,' ', time, head_portrait,' ', ' ', type from message_temp");
            db.execSQL("DROP TABLE message_temp");
        } else if (oldVersion == 2) {
            db.execSQL("ALTER TABLE message RENAME TO message_temp");
            db.execSQL("create table message ("
                    + "id integer primary key autoincrement, "
                    + "content text,"
                    + "img_url text,"
                    + "username text,"
                    + "from_username text,"
                    + "time text,"
                    + "head_portrait text,"
                    + "voice_time REAL,"
                    + "voice_path text,"
                    + "type integer)");
            db.execSQL("insert into message(id, content, img_url, username, from_username, time, head_portrait, voice_time, voice_path, type) "
                    + "select id, content, img_url, username,' ', time, head_portrait, voice_time, voice_path, type from message_temp");
            db.execSQL("DROP TABLE message_temp");
        } else if (oldVersion == 3) {
            db.execSQL("ALTER TABLE message RENAME TO message_temp");
            db.execSQL("create table message ("
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
                    + "type integer)");
            db.execSQL("insert into message(id, content, img_url, username, from_username, time, head_portrait, voice_time, voice_path, type) "
                    + "select id, content, img_url, username,' ', time, head_portrait, voice_time, voice_path, type from message_temp");
            db.execSQL("DROP TABLE message_temp");
        }
    }
}