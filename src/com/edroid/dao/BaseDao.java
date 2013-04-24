package com.edroid.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * BaseDao
 *
 * 子类只需要实现:将对象转换成ContentValues,将Cursor解析成单个对象。
 * 即可获得基本的增加改查模板，类似于Spring JDBCTemplate
 *
 * @param <T>
 */
public abstract class BaseDao<T> {
    protected static final String TAG = "BaseDao";
    /** DEBUG */
    protected static final boolean DEBUG = true;
    
    /**
     * 主键名
     */
    protected String primaryKey = BaseColumns._ID;
    
    /**
     * 表名 
     */
    protected String tableName;

    /**
     * SQLite Open Helper
     */
    protected SQLiteOpenHelper mOpenHelper;

    /**
     * @param mOpenHelper
     * @param tableName 表名
     */
    public BaseDao(SQLiteOpenHelper mOpenHelper, String tableName) {
        this.mOpenHelper = mOpenHelper;
        this.tableName = tableName;
    }
    
    /**
     * 打开数据库
     * 
     * @param writeable true为读写模式，false为只读
     * @return
     */
    public SQLiteDatabase getDb(boolean writeable) {
        if (writeable) {
            return mOpenHelper.getWritableDatabase();
        } else {
            return mOpenHelper.getReadableDatabase();
        }
    }

    /**
     * insert a row
     * 
     * @param row
     * @return
     */
    public long insert(T row) {
        if (DEBUG) Log.v(TAG, "insert: " + row.toString());
        SQLiteDatabase db = getDb(true);
        long id = db.insert(tableName, null, objectToValues(row));
        db.close();
        return id;
    }
    
    /**
     * delete row by primary key
     * 
     * @param id
     * @return affectedRows
     */
    public int delete(long id) {
        SQLiteDatabase db = getDb(true);
        return db.delete(tableName, primaryKey + "=" + id, null);
    }
    
    // alias
    public List<T> findAll()  {
        return queryForAll();
    }

    /**
     * query list for all
     * 
     * @return 永远不会返回null,只会返回空列表
     */
    public List<T> queryForAll() {
        SQLiteDatabase db = getDb(false);
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        List<T> list = cursorToList(cursor);
        cursor.close();
        db.close();
        return list;
    }
    
    /**
     * find one row by fields
     * 
     * @param selection
     * @param selectionArgs
     * @param orderBy
     * @return
     */
    public T findOneByFields(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        SQLiteDatabase db = getDb(false);
        Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, orderBy);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            T obj = cursorToObject(cursor);
            cursor.close();
            return obj;
        }
        return null;
    }
    
    public List<T> findListByFields(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        SQLiteDatabase db = getDb(false);
        Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, orderBy);
        List<T> list = cursorToList(cursor);
        cursor.close();
        db.close();
        return list;
        
    }
    
    /**
     * find one row by primary key
     * 
     * @param id
     * @return
     */
    public T findById(long id) {
        return findOneByFields(null, primaryKey + "=" + id, null, null);
    }
    
    /**
     * update a row by primary key
     * 
     * @param id
     * @param values
     * @return
     */
    public int update(long id, ContentValues values) {
        return updateByFields(values, primaryKey + "=" + id, null);
    }
    
    /**
     * update a row
     * 
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int updateByFields(ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getDb(true);
        return db.update(tableName, values, whereClause, whereArgs);
    }

    /**
     * Convert cursor object to list
     * 
     * @param cursor
     * @return
     */
    public List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<T>();
        while (cursor.moveToNext()) {
            list.add(cursorToObject(cursor));
        }
        return list;
    }
    
    
    /**
     * 将Cursor解析成指定的对象
     * Convert cursor to object
     * 
     * @param cursor 请不要手动关闭它,Cursor可能装的是个list
     * @return
     */
    abstract public T cursorToObject(Cursor cursor);
    
    /*
    public T cursorToObject(Cursor cursor) {
        List<T> list = cursorToList(cursor);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    */
    
    /**
     * 将对象的域转换成ContentValues, 用于入库
     * Convert object to ContentValues
     * for insert/update it into the database
     * 
     * @param row
     * @return
     */
    abstract public ContentValues objectToValues(T row);
}
