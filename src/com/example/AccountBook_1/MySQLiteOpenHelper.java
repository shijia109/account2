package com.example.AccountBook_1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	private final static String DBNAME = "db_account.db";
	private final static int VERSION = 1;
	private SQLiteDatabase db = null;

	public MySQLiteOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		db = getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("", "创建数据库");
	//	db.execSQL("CREATE TABLE IF NOT EXISTS tb_income_type(_id PRIMARY KEY AUTOINCREMENT , type)");
	//	db.execSQL("CREATE TABLE IF NOT EXISTS tb_outlay_type(_id PRIMARY KEY AUTOINCREMENT , type)");
		db.execSQL("CREATE TABLE IF NOT EXISTS tb_income(_id INTEGER PRIMARY KEY AUTOINCREMENT , date, title, money, type )");
		db.execSQL("CREATE TABLE IF NOT EXISTS tb_outlay(_id INTEGER PRIMARY KEY AUTOINCREMENT , date, title, money, type_big, type_small)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS tb_collection");
			onCreate(db);
		}
	}

	public Cursor selectCursor(String sql, String[] selectionArgs) {
		return db.rawQuery(sql, selectionArgs);
	}

	public List<Map<String, Object>> selectList(String sql,
			String[] selectionArgs) {
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursorToList(cursor);
	}

	public int selectCount(String sql, String[] selectionArgs) {
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		int count = cursor.getCount();
		if (cursor != null) {
			cursor.close();
		}
		return count;
	}

	public boolean execData(String sql, Object[] bindArgs) {
		try {
			db.execSQL(sql, bindArgs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<Map<String, Object>> cursorToList(Cursor cursor) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] arrColumnName = cursor.getColumnNames();
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < arrColumnName.length; i++) {
				map.put(arrColumnName[i], cursor.getString(i));
			}
			list.add(map);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

}
