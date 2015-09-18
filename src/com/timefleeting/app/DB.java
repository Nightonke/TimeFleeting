package com.timefleeting.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB {

	public static final String DB_NAME_STRING = "Record";
	
	public static final int VERSION = 1;
	
	private static DB db;
	private SQLiteDatabase sqliteDatabase;
	
	private DB(Context context) throws IOException {
		DBHelper dbHelper = new DBHelper(
				context, DB_NAME_STRING, null, VERSION);
		sqliteDatabase = dbHelper.getWritableDatabase();
	}
	
	public synchronized static DB getInstance(Context context) 
			throws IOException {
		if (db == null) {
			db = new DB(context);
		}
		return db;
	}
	
	// return the new id of the record if save successfully
	// otherwise return -1
	public int saveRecord(Record record) {
		if (record != null) {
			
			Cursor cursor = sqliteDatabase.query(
					DB_NAME_STRING, 
					null, 
					"id = ?", 
					new String[] {String.valueOf(record.getId())}, 
					null,
					null, 
					null);
			
			if (cursor.getCount() == 0) {
				ContentValues values = new ContentValues();
				
				// if the ID of record is -1
				// it means the record is a new record
				if (record.getId() == -1) {
					
				} else {
					values.put("id", record.getId());
				}
				
				values.put("title", record.getTitle());
				values.put("text", record.getText());
				values.put("remind_time", record.getRemindTime());
				values.put("create_time", record.getCreateTime());
				values.put("star", record.getStar());
				values.put("type", record.getType());
				sqliteDatabase.insert(DB_NAME_STRING, null, values);
				
				cursor = sqliteDatabase
						.query(DB_NAME_STRING, null, 
								null, null, null, null, null);
				cursor.moveToLast();
				return cursor.getInt(cursor.getColumnIndex("id"));
			}
		}
		return -1;
	}
	
	public List<Record> loadPastRecords() {
		List<Record> list = new ArrayList<Record>();
		Cursor cursor = sqliteDatabase
				.query(DB_NAME_STRING, null, 
						"type = ?",
						new String[] {"PAST"} ,
						null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Record record = new Record();
				record.setId(cursor.getInt(cursor.getColumnIndex("id")));
				record.setTitle(
						cursor.getString(cursor.getColumnIndex("title")));
				record.setText(
						cursor.getString(cursor.getColumnIndex("text")));
				record.setRemindTime(
						cursor.getString(cursor.getColumnIndex("remind_time")));
				record.setCreateTime(
						cursor.getString(cursor.getColumnIndex("create_time")));
				record.setStar(
						cursor.getString(cursor.getColumnIndex("star")));
				record.setType("PAST");
				list.add(record);
			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
	public List<Record> loadFutureRecords() {
		List<Record> list = new ArrayList<Record>();
		Cursor cursor = sqliteDatabase
				.query(DB_NAME_STRING, null, 
						"type = ?",
						new String[] {"FUTURE"} ,
						null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Record record = new Record();
				record.setId(cursor.getInt(cursor.getColumnIndex("id")));
				record.setTitle(
						cursor.getString(cursor.getColumnIndex("title")));
				record.setText(
						cursor.getString(cursor.getColumnIndex("text")));
				record.setRemindTime(
						cursor.getString(cursor.getColumnIndex("remind_time")));
				record.setCreateTime(
						cursor.getString(cursor.getColumnIndex("create_time")));
				record.setStar(
						cursor.getString(cursor.getColumnIndex("star")));
				record.setType("FUTURE");
				list.add(record);
			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
}
