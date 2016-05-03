package com.major.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



/**
 * ������
 * @author Administrator
 *
 */
public class BlackNumberDao {
	
	public BlackNumberOpenHelper helper;
	
	public BlackNumberDao(Context context) {
		 helper = new BlackNumberOpenHelper(context);
	}
	
	/**
     * @param number ����������
     * @param mode   ����ģʽ
     */
	public  boolean add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("number", number);
		contentValues.put("mode", mode);
		long rowid = db.insert("blacknumber", null, contentValues);
		if (rowid == -1) {
			return false;
		} else {
			return true;
		}
		
	}
	
	 /**
     * ͨ���绰����ɾ��
     *
     * @param number �绰����
     */
    public boolean delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete("blacknumber", "number=?", new String[]{number});
        if (rowNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * ͨ���绰�����޸����ص�ģʽ
     *
     * @param number
     */
    public boolean changeNumberMode(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        int rownumber =db.update("blacknumber", values, "number=?", new String[]{number});
        if (rownumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * ����һ����������������ģʽ
     *
     * @return
     */
    public String findNumber(String number) {
        String mode = "";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }	
}
