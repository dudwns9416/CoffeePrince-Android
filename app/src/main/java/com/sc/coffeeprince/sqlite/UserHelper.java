package com.sc.coffeeprince.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sc.coffeeprince.model.User;


public class UserHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "UserDB";
    public static final int DATABASE_VERSION = 5;

    private static final String TABLE_USER = "User";

    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_AUTHORITY = "authority";
    private static final String KEY_FACEBOOK = "facebook";

    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_AUTHORITY + " INT,"
                + KEY_FACEBOOK + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 전에 있던 DB 삭제
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // DB 재생성
        onCreate(db);
    }

    public long insertUser(User user, String facebook) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_AUTHORITY, user.getAuthority());
        values.put(KEY_FACEBOOK, facebook);

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        return result;
    }

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_AUTHORITY, user.getAuthority());

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        return result;
    }


    public User selectUser() {
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                String id = cursor.getString(cursor.getColumnIndex(KEY_ID));
                String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
                Integer authority = cursor.getInt(cursor.getColumnIndex(KEY_AUTHORITY));
                String facebook = cursor.getString(cursor.getColumnIndex(KEY_FACEBOOK));

                User user = new User();
                user.setId(id);
                //user.setPassword(password);
                user.setAuthority(authority);
                user.setFacebook(facebook);

                cursor.close();
                db.close();

                return user;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        cursor.close();
        db.close();

        return null;
    }

    public boolean isLogin() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(countQuery, null);

        int rowCount = cursor.getCount();
        boolean result = rowCount > 0 ? true : false;

        cursor.close();
        db.close();
        return result;
    }

    public int deleteUser() {
        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(TABLE_USER, null, null);
        db.close();

        return result;
    }
}
