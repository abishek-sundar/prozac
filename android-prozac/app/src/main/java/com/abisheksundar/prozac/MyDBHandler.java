package com.abisheksundar.prozac;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Vector;

public class MyDBHandler extends SQLiteOpenHelper {

    //information of database

    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_NAME = "phoneListDB.db";

    public static final String TABLE_NAME = "PhoneList";

    //public SQLiteDatabase db = getWritableDatabase();

    public static final String COLUMN_PHONE = "PhoneNum";

    public static final String COLUMN_NAME = "Name";

    //initialize the database

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    @Override

    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_PHONE +
                " TEXT, " + COLUMN_NAME + " TEXT )";

        db.execSQL(CREATE_TABLE);
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public String loadHandler() {
        String result = "";
        String query = "Select* FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            result += String.valueOf(result_0) + " " + result_1 +
                    System.getProperty("line.separator");
        }
        cursor.close();
        //db.close();
        return result;
    }

    public void addHandler(PhoneList list) {
        //if(findHandler(list.getName())!=null) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, list.getPhoneNum());
        values.put(COLUMN_NAME, list.getName());
        SQLiteDatabase db = this.getWritableDatabase();
        long ma = db.insert(TABLE_NAME, null, values);
        db.close();
        //}
    }


    public PhoneList findHandler(String name) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = " + "'" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        PhoneList friend = new PhoneList();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            friend.setName(cursor.getString(0));
            friend.setPhoneNum(cursor.getString(1));
            cursor.close();
        } else {
            friend = null;
        }
        //db.close();
        return friend;
    }

    public boolean deleteHandler(String phone) {
        boolean result = false;
        String query = "Select *FROM " + TABLE_NAME + " WHERE " + COLUMN_PHONE + " = '" + phone + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        PhoneList frined = new PhoneList();
        if (cursor.moveToFirst()) {
            frined.setPhoneNum(cursor.getString(0));
            db.delete(TABLE_NAME, COLUMN_NAME + "=?",
                    new String[]{
                            String.valueOf(frined.getPhoneNum())
                    });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public boolean updateHandler(String phone, String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put(COLUMN_PHONE, phone);
        args.put(COLUMN_NAME, name);
        return db.update(TABLE_NAME, args, COLUMN_PHONE + "=" + phone, null) > 0;
    }

    public Vector<PhoneList> printAll() {

        String query = "Select * FROM " + TABLE_NAME;
        Vector<PhoneList> listName = new Vector<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        while (cursor.moveToNext()) {
            PhoneList friend = new PhoneList();
            friend.setName(cursor.getString(0));
            friend.setPhoneNum(cursor.getString(1));
            listName.add(friend);
        }
        cursor.close();

        for (int i = 0; i < listName.size(); i++) {
            Log.d("re:", listName.get(i).getName());
        }

        Log.d("re:", "END");
        db.close();
        return listName;
    }
}

