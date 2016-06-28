package com.zonsim.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

/**
 * CopyRight
 * Created by tang-jw on 2016/6/27.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {
	
	public MyDataBaseHelper(Context context) {
		super(context, "yxjobs.db", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		Logger.i("creating the db ...");
		String createSql = "CREATE TABLE jobs (professionCode VARCHAR, professionName VARCHAR, id INTEGER)";
//		String createSql = "CREATE TABLE jobs (professionCode VARCHAR, professionName VARCHAR, id INTEGER UNIQUE)";
		//PRIMARY KEY AUTOINCREMENT
		sqLiteDatabase.execSQL(createSql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		Logger.i("updating the db ...");
	}
}
