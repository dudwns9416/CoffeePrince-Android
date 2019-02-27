package com.sc.coffeeprince.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sc.coffeeprince.model.Cafe;

import java.util.ArrayList;

/**
 * Created by fopa on 2017-10-09.
 */

public class CafeHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CafeDB";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_CAFE = "Cafe";
    private static final String KEY_PCID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMG = "img";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_BIRTH = "birth";
    private static final String KEY_ADDRESS = "address";

    public CafeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CAFE_TABLE = "CREATE TABLE " + TABLE_CAFE + "("
                + KEY_PCID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_IMG + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_BIRTH + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + "PRIMARY KEY (" + KEY_PCID + ")"
                + ")";
        db.execSQL(CREATE_CAFE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAFE);

        // DB 재생성
        onCreate(db);
    }

    public long insertCafe(Cafe cafe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PCID, cafe.getId());
        values.put(KEY_NAME, cafe.getName());
        values.put(KEY_IMG, cafe.getImage());
        values.put(KEY_CONTENT, cafe.getContent());
        values.put(KEY_BIRTH, cafe.getBirth());
        values.put(KEY_ADDRESS, cafe.getAddress());

        long result = db.insert(TABLE_CAFE, null, values);
        db.close();

        return result;
    }

    public ArrayList<Cafe> selectCafe() {
        ArrayList<Cafe> cafeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CAFE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_PCID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String img = cursor.getString(cursor.getColumnIndex(KEY_IMG));
                String content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
                String birth = cursor.getString(cursor.getColumnIndex(KEY_BIRTH));
                String address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));

                Cafe cafe = new Cafe();
                cafe.setId(id);
                cafe.setName(name);
                cafe.setImage(img);
                cafe.setContent(content);
                cafe.setCafeImportant(true);
                cafe.setBirth(birth);
                cafe.setAddress(address);

                cafeList.add(cafe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cafeList;

    }

    public int deleteCafeWhereId(int id) {
        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(TABLE_CAFE, KEY_PCID + "= ?", new String[]{String.valueOf(id)});
        db.close();

        return result;
    }
}
