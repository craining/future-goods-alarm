package com.zgy.goldmonitor.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
/**
 * 数据库帮助类
 * @Author zhuanggy
 * @Date:2014-1-7
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version 
 * @since
 */
public abstract class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	/**
	 * 插入数据（替换或忽略）
	 * @Description:
	 * @param tableName
	 * @param values
	 * @param conflictFlag
	 * @return
	 */
	private synchronized long insertWithConflict(String tableName, ContentValues values, int conflictFlag) {
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		try {
			db.beginTransaction();
			result = db.insertWithOnConflict(tableName, null, values, conflictFlag);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}

	/**
	 * 执行插入操作, 如果已经存在相关记录(插入时发生约束冲突), 则进行整行更新(替换)
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	public long insertOrReplace(String tableName, ContentValues values) {
		return insertWithConflict(tableName, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	/**
	 * 执行插入操作, 如果插入过程中发生了数据库约束冲突, 则不做任何事情
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	public long insertOrIgnore(String tableName, ContentValues values) {
		return insertWithConflict(tableName, values, SQLiteDatabase.CONFLICT_IGNORE);
	}

	/**
	 * 更新, 捕获异常并关闭数据库
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 */
	public synchronized int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		int result = 0;
		try {
			db.beginTransaction();
			result = db.update(table, values, whereClause, whereArgs);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}
	
	/**
	 * 执行sql
	 * @param 
	 * @author zhuanggy
	 * @date 2014-1-6
	 */
	public synchronized void excuteSQL(String sql) throws Exception{
		SQLiteDatabase db = getReadableDatabase();
		try {
			db.beginTransaction();
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			db.endTransaction();
		}
	}
	
	/**
	 * 通过行号删除数据
	 * @Description:
	 * @param tableName
	 * @param rowid
	 * @return
	 */
	public synchronized int deleteByRowID(String tableName, long rowid) {
		SQLiteDatabase db = getWritableDatabase();
		int result = 0;
		try {
			db.beginTransaction();
			result = db.delete(tableName, "_ROWID_=?", new String[] {String.valueOf(rowid)});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}
	/**
	 * 执行删除操作
	 * 
	 * @param tableName
	 * @param where
	 * @param args
	 * @return
	 */
	public synchronized int delete(String tableName, String where, String[] args) {
		SQLiteDatabase db = getWritableDatabase();
		int result = 0;
		try {
			db.beginTransaction();
			result = db.delete(tableName, where, args);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}

	/**
	 * 统计记录数
	 * @Description:
	 * @param table
	 * @return
	 */
	public long count(String table) {
		SQLiteDatabase db = getReadableDatabase();
		try {
			return DatabaseUtils.queryNumEntries(db, table);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return 0;
	}

	/**
	 * 统计记录数
	 * @Description:
	 * @param table
	 * @param where
	 * @param whereArgs
	 */
	public long count(String table, String where, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		try {
			String s = (!TextUtils.isEmpty(where)) ? " where " + where : "";
			return DatabaseUtils.longForQuery(db, "select count(*) from " + table + s, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return 0;
	}
	/**
	 * 查询
	 * @Description:
	 * @param table
	 * @return
	 */
	public Cursor query(String table) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, null, null, null, null, null, null);
	}
	/**
	 * 查询指定列
	 * @Description:
	 * @param table
	 * @param columns
	 * @return
	 */
	public Cursor query(String table, String[] columns) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, null, null, null, null, null);
	}
	/**
	 * 条件查询指定列
	 * @Description:
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, null);
	}
	/**
	 * 条件查询指定列并排序
	 * @Description:
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @return
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, orderBy);
	}
	/**
	 * 条件查询指定列的几条数据并排序
	 * @Description:
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @param limit
	 * @return
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy, String limit) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, orderBy, limit);
	}

}