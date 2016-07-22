package com.networking.androidtest.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rafalwesolowski on 20/07/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_CACHE = "cache";
    public static final String COLUMN_REQUEST_ID = "requestId";
    public static final String COLUMN_REQUEST_STRING = "requestString";
    public static final String COLUMN_RESPONSE = "cached_response";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CACHE + "("
            + COLUMN_REQUEST_ID + " INTEGER, "
            + COLUMN_REQUEST_STRING + " TEXT NOT NULL, "
            + COLUMN_RESPONSE + " TEXT NOT NULL, "
            + COLUMN_TIMESTAMP + " TEXT NOT NULL);";

    private static final String DB_NAME = "Database";
    private static final int DB_VERSION = 1;
    private boolean mIsDBOpened;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public boolean isDBOpened() {
        return mIsDBOpened;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        mIsDBOpened = true;
    }

    @Override
    public void close() {
        mIsDBOpened = false;
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public SQLiteDatabase getWritableDB() {
        return super.getWritableDatabase();
    }
}
