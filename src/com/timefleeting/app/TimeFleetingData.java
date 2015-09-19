package com.timefleeting.app;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class TimeFleetingData {

	private static TimeFleetingData timeFleetingData;
	
	private static DB db;
	public static List<Record> pastRecords;
	public static List<Record> futureRecords;
	
	
	private TimeFleetingData(Context context) {
		try {
			db = db.getInstance(context);
			Log.d("TimeFleeting", "Loading database successfully.");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static TimeFleetingData getInstance(Context context) 
			throws IOException {
		if (timeFleetingData == null) {
			timeFleetingData = new TimeFleetingData(context);
			pastRecords = db.loadPastRecords();
			futureRecords = db.loadFutureRecords();
			Log.d("TimeFleeting", "The timeFlletingData is NULL.");
			Log.d("TimeFleeting", "Loading " + 
				String.valueOf(pastRecords.size()) + 
				" PAST records successfully.");
			Log.d("TimeFleeting", "Loading " + 
					String.valueOf(futureRecords.size()) + 
					" FUTURE records successfully.");
		}
		return timeFleetingData;
	}
	
	// return weather the new record is saved successfully
	public boolean saveRecord(Record record) {
		int insertId = db.saveRecord(record);
		if (insertId == -1) {
			return false;
		}
		record.setId(insertId);
		if (record.getType() == "PAST") {
			pastRecords.add(record);
		} else if (record.getType() == "FUTURE") {
			futureRecords.add(record);
		}
		return true;
	}
	
	// sort the past Records by id
	// this is also the default sort in the database
	public void sortPastRecordById() {
		Collections.sort(pastRecords, new Comparator<Record>() {

			@Override
			public int compare(Record lhs, Record rhs) {
				// TODO Auto-generated method stub
				return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
			}
		});
	}

}
