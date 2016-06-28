package com.zonsim.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.zonsim.sqlite.helper.MyDataBaseHelper;

/**
 * CopyRight
 * Created by tang-jw on 2016/6/27.
 */
public class JobDao {
	private MyDataBaseHelper mHelper;
	
	public JobDao(Context context) {
		mHelper = new MyDataBaseHelper(context);
		
		
	}
	
	public long insert(String code, String name, int id) {
		
		SQLiteDatabase db = mHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("professionCode", code);
		values.put("professionName", name);
		values.put("id", id);
		
		long insert = db.insert("jobs", null, values);
//		long replace = db.replace("jobs", null, values);
//		long insertWithOnConflict = db.insertWithOnConflict("jobs", "NULL", values, 1);
		db.close();
		return insert;
	}
	
	/*public long insert(String code, String name, int id) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("professionCode", code);
		values.put("professionName", name);
		values.put("id", id);
		
		return db.insertWithOnConflict("jobs", null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}*/
	
	public int delete() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int delete = db.delete("jobs", null, null);
		db.close();
		Logger.i("db delete ... " + delete);
		return delete;
	}
}
