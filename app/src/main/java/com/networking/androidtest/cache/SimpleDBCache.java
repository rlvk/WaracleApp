package com.networking.androidtest.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Created by rafalwesolowski on 20/07/2016.
 */
public class SimpleDBCache {

    protected static int DEFAULT_CACHE_LIFESPAN = 60; //1 minute

    private final DBHelper mDBHelper;

    private Object mutexLock = new Object();

    public SimpleDBCache(DBHelper dbHelper) {
        mDBHelper = dbHelper;
    }

    public void clearCache() {
        synchronized (mutexLock) {
            SQLiteDatabase db = mDBHelper.getWritableDB();
            db.execSQL("DROP TABLE IF EXISTS " + DBHelper.TABLE_CACHE);
        }
    }

    public CacheData get(CacheData cacheData) {

        synchronized (mutexLock) {

            SQLiteDatabase db = mDBHelper.getWritableDB();

            String[] columns = new String[]{DBHelper.COLUMN_REQUEST_ID, DBHelper.COLUMN_RESPONSE, DBHelper.COLUMN_TIMESTAMP};

            Integer requestIdentifier = cacheData.getRequestId();

            String whereClause = DBHelper.COLUMN_REQUEST_ID + " = '" + requestIdentifier + "'";

            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

            queryBuilder.setTables(DBHelper.TABLE_CACHE);
            Cursor cursor = queryBuilder.query(db, columns, whereClause, null, null, null, null);

            while (cursor.moveToNext()) {
                Integer requestId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_REQUEST_ID));
                String requestString = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_REQUEST_ID));
                String timestamp = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TIMESTAMP));
                String response = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RESPONSE));

                cacheData.setResponse(response);
                cacheData.setTimestamp(Long.valueOf(timestamp));
                cacheData.setRequestString(requestString);

                cacheData.setRetrievedItemFromCache(true);
                break;
            }

            db.close();
        }
        return cacheData;
    }

    public void put(CacheData data) {

        synchronized (mutexLock) {

            SQLiteDatabase db = mDBHelper.getWritableDB();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.COLUMN_REQUEST_ID, data.getRequestId());
            contentValues.put(DBHelper.COLUMN_REQUEST_STRING, data.getRequestString());
            contentValues.put(DBHelper.COLUMN_TIMESTAMP, data.getTimestamp());
            contentValues.put(DBHelper.COLUMN_RESPONSE, data.getResponse());

            String whereClause = DBHelper.COLUMN_REQUEST_ID + " = '" + data.getRequestId() + "'";

            if (db.update(DBHelper.TABLE_CACHE, contentValues, whereClause, null) == 0) {
                db.insert(DBHelper.TABLE_CACHE, null, contentValues);
            }

            db.close();
        }

    }
}
