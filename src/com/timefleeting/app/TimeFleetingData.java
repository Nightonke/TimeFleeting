package com.timefleeting.app;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class TimeFleetingData {

	private static TimeFleetingData timeFleetingData;
	
	private static DB db;
	public static List<Record> pastRecords;
	public static List<Record> futureRecords;
	
	// according which to sort
	public static boolean isSortByCreateTime = true;
	public static boolean isSortedByRemindTime = false;
	public static boolean isSortedByTitle = false;
	public static boolean isSortedByStar = false;
	
	// the direction of the sort
	public static boolean isSortedByCreateTimeReversely = true;
	public static boolean isSortedByRemindTimeReversely = false;
	public static boolean isSortedByTitleReversely = false;
	public static boolean isSortedByStarReversely = false;
	
	// according which to sort
	public static boolean pastIsSortByRemindTime = true;
	public static boolean pastIsSortByRemainTime = false;
	
	// the direction of the sort
	public static boolean pastIsSortByRemindTimeReversely = false;
	public static boolean pastIsSortByRemainTimeReversely = false;
	
	
	private static boolean overdueSort = false;
	public static int futureBeTopNumber;
	
	public static int pastBeTopNumber;
	
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
			Log.d("TimeFleeting", "The timeFleetingData is NULL.");
			Log.d("TimeFleeting", "The pastBeTopNumber is " + pastBeTopNumber);
			Log.d("TimeFleeting", "The futureBeTopNumber is " + futureBeTopNumber);
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
	// if successfully, return the new id
	// else return -1
	public static int saveRecord(Record record) {
		boolean isUpdate = false;
		if (record.getId() != -1) {
			isUpdate = true;
		}
		int insertId = db.saveRecord(record);
		if (insertId == -1) {
			return -1;
		}
		record.setId(insertId);
		if (record.getType().substring(0, 4).equals("PAST")) {
			if (isUpdate) {
				for (int i = 0; i < pastRecords.size(); i++) {
					if (pastRecords.get(i).getId() == record.getId()) {
						pastRecords.get(i).set(record);
					}
				}
			} else {
				pastRecords.add(record);
			}
		} else if (record.getType().equals("FUTURE")) {
			if (isUpdate) {
				for (int i = 0; i < futureRecords.size(); i++) {
					if (futureRecords.get(i).getId() == record.getId()) {
						futureRecords.get(i).set(record);
					}
				}
			} else {
				futureRecords.add(record);
			}
		}

		return insertId;
	}
	
	public static int deleteRecord(int id) {
		int deletePositionInFuture = -1;
		int deletePositionInPast = -1;
		for (int i = 0; i < futureRecords.size(); i++) {
			if (futureRecords.get(i).getId() == id) {
				if (deletePositionInFuture == -1) {
					// first found
					deletePositionInFuture = i;
				} else {
					// second or more found
					// it means error
					Log.d("TimeFleeting", "Found more than one record to be deleted in the futureRecords");
					return -1;
				}
			}
		}
		for (int i = 0; i < pastRecords.size(); i++) {
			if (pastRecords.get(i).getId() == id) {
				if (deletePositionInPast == -1) {
					// first found
					deletePositionInPast = i;
				} else {
					// second or more found
					// it means error
					Log.d("TimeFleeting", "Found more than one record to be deleted in the pastRecords");
					return -1;
				}
			}
		}
		if ((deletePositionInFuture != -1 && deletePositionInPast != -1) 
				|| (deletePositionInFuture == -1 && deletePositionInPast == -1)) {
			// not found or found more than one
			Log.d("TimeFleeting", "Not found or found more than one");
			return -1;
		} else {
			if (deletePositionInFuture != -1) {
				// the record to be deleted is a future record
				futureRecords.remove(deletePositionInFuture);
				int idToBeDeleted = db.deleteRecord(id);
				if (idToBeDeleted == -1) {
					return -1;
				} else {
					return idToBeDeleted;
				}
			} else if (deletePositionInPast != -1) {
				// the record to be deleted is a past record
				pastRecords.remove(deletePositionInPast);
				int idToBeDeleted = db.deleteRecord(id);
				if (idToBeDeleted == -1) {
					return -1;
				} else {
					return idToBeDeleted;
				}
			}
		}
		return -1;
	}
	
	public boolean getOverdueSort() {
		return overdueSort;
	}
	
	public void setOverdueSortTrue() {
		overdueSort = true;
	}
	
	public void setOverdueSortFalse() {
		overdueSort = false;
	}
	
	// sort the past Records by id
	// this is also the default sort in the database
	public void sortPastRecordById() {
		Collections.sort(pastRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
			}
		});
	}
	
	// sort the future Records by id
	// this is also the default sort in the database
	public void sortFutureRecordById() {
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if (lhsIsOverdue < 0 && rhsIsOverdue < 0) {
						// both are overdue
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else if (lhsIsOverdue >= 0 && rhsIsOverdue < 0) {
						return -1;
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	public void sortFutureRecordByIdReversely() {
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if (lhsIsOverdue < 0 && rhsIsOverdue < 0) {
						// both are overdue
						return Integer.valueOf(rhs.getId()).compareTo(Integer.valueOf(lhs.getId()));
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else if (lhsIsOverdue >= 0 && rhsIsOverdue < 0) {
						return -1;
					} else {
						return Integer.valueOf(rhs.getId()).compareTo(Integer.valueOf(lhs.getId()));
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	// sort the future records by title
	// if the title is same, sort by create time
	// if the create time is same, sort by remind time
	// if the remind time is same, sort by star
	// is the star is same, sort by id
	public static void sortFutureRecordByTitle() {
		setSortParameter(true, false, false, false, false, false, false, false);
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if ((lhsIsOverdue < 0 && rhsIsOverdue < 0) || (lhsIsOverdue >= 0 && rhsIsOverdue >= 0)) {
						// both are overdue or both are not overdue
						if (!lhs.getTitle().equals(rhs.getTitle())) {
							return lhs.getTitle().compareTo(rhs.getTitle());
						} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
							return lhs.getCreateTime().compareTo(rhs.getCreateTime());
						} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
							return lhs.getRemindTime().compareTo(rhs.getRemindTime());
						} else if (!lhs.getStar().equals(rhs.getStar())) {
							return lhs.getStar().compareTo(rhs.getStar());
						} else {
							return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
						}
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else {
						// lhsIsOverdue >= 0 && rhsIsOverdue < 0
						return -1;
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else if (!lhs.getTitle().equals(rhs.getTitle())) {
						return lhs.getTitle().compareTo(rhs.getTitle());
					} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
						return lhs.getCreateTime().compareTo(rhs.getCreateTime());
					} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
						return lhs.getRemindTime().compareTo(rhs.getRemindTime());
					} else if (!lhs.getStar().equals(rhs.getStar())) {
						return lhs.getStar().compareTo(rhs.getStar());
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	// sort the future records by title reversely
	// if the title is same, sort by create time
	// if the create time is same, sort by remind time
	// if the remind time is same, sort by star
	// is the star is same, sort by id
	public static void sortFutureRecordByTitleReversely() {
		setSortParameter(true, true, false, false, false, false, false, false);
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if ((lhsIsOverdue < 0 && rhsIsOverdue < 0) || (lhsIsOverdue >= 0 && rhsIsOverdue >= 0)) {
						// both are overdue or both are not overdue
						if (!lhs.getTitle().equals(rhs.getTitle())) {
							return rhs.getTitle().compareTo(lhs.getTitle());
						} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
							return lhs.getCreateTime().compareTo(rhs.getCreateTime());
						} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
							return lhs.getRemindTime().compareTo(rhs.getRemindTime());
						} else if (!lhs.getStar().equals(rhs.getStar())) {
							return lhs.getStar().compareTo(rhs.getStar());
						} else {
							return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
						}
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else {
						// lhsIsOverdue >= 0 && rhsIsOverdue < 0
						return -1;
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else if (!lhs.getTitle().equals(rhs.getTitle())) {
						return rhs.getTitle().compareTo(lhs.getTitle());
					} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
						return lhs.getCreateTime().compareTo(rhs.getCreateTime());
					} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
						return lhs.getRemindTime().compareTo(rhs.getRemindTime());
					} else if (!lhs.getStar().equals(rhs.getStar())) {
						return lhs.getStar().compareTo(rhs.getStar());
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	// sort the future records by create time
	// if the create time is same, sort by title
	// if the title is same, sort by remind time
	// if the remind time is same, sort by star
	// is the star is same, sort by id
	public static void sortFutureRecordByCreateTime() {
		setSortParameter(false, false, true, false, false, false, false, false);
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if ((lhsIsOverdue < 0 && rhsIsOverdue < 0) || (lhsIsOverdue >= 0 && rhsIsOverdue >= 0)) {
						// both are overdue or both are not overdue
						if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
							return lhs.getCreateTime().compareTo(rhs.getCreateTime());
						} else if (!lhs.getTitle().equals(rhs.getTitle())) {
							return rhs.getTitle().compareTo(lhs.getTitle());
						} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
							return lhs.getRemindTime().compareTo(rhs.getRemindTime());
						} else if (!lhs.getStar().equals(rhs.getStar())) {
							return lhs.getStar().compareTo(rhs.getStar());
						} else {
							return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
						}
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else {
						// lhsIsOverdue >= 0 && rhsIsOverdue < 0
						return -1;
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
						return lhs.getCreateTime().compareTo(rhs.getCreateTime());
					} else if (!lhs.getTitle().equals(rhs.getTitle())) {
						return rhs.getTitle().compareTo(lhs.getTitle());
					} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
						return lhs.getRemindTime().compareTo(rhs.getRemindTime());
					} else if (!lhs.getStar().equals(rhs.getStar())) {
						return lhs.getStar().compareTo(rhs.getStar());
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	// sort the future records by create time reversely
	// if the create time is same, sort by title
	// if the title is same, sort by remind time
	// if the remind time is same, sort by star
	// is the star is same, sort by id
	public static void sortFutureRecordByCreateTimeReversely() {
		setSortParameter(false, false, true, true, false, false, false, false);
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if ((lhsIsOverdue < 0 && rhsIsOverdue < 0) || (lhsIsOverdue >= 0 && rhsIsOverdue >= 0)) {
						// both are overdue or both are not overdue
						if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
							return rhs.getCreateTime().compareTo(lhs.getCreateTime());
						} else if (!lhs.getTitle().equals(rhs.getTitle())) {
							return rhs.getTitle().compareTo(lhs.getTitle());
						} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
							return lhs.getRemindTime().compareTo(rhs.getRemindTime());
						} else if (!lhs.getStar().equals(rhs.getStar())) {
							return lhs.getStar().compareTo(rhs.getStar());
						} else {
							return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
						}
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else {
						// lhsIsOverdue >= 0 && rhsIsOverdue < 0
						return -1;
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
						return rhs.getCreateTime().compareTo(lhs.getCreateTime());
					} else if (!lhs.getTitle().equals(rhs.getTitle())) {
						return rhs.getTitle().compareTo(lhs.getTitle());
					} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
						return lhs.getRemindTime().compareTo(rhs.getRemindTime());
					} else if (!lhs.getStar().equals(rhs.getStar())) {
						return lhs.getStar().compareTo(rhs.getStar());
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	// sort the future records by remind time
	// if the remind time is same, sort by create time
	// if the create time is same, sort by title 
	// if the title is same, sort by star
	// is the star is same, sort by id
	public static void sortFutureRecordByRemindTime() {
		setSortParameter(false, false, false, false, true, false, false, false);
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if ((lhsIsOverdue < 0 && rhsIsOverdue < 0) || (lhsIsOverdue >= 0 && rhsIsOverdue >= 0)) {
						// both are overdue or both are not overdue
						if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
							return lhs.getRemindTime().compareTo(rhs.getRemindTime());
						} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
							return rhs.getCreateTime().compareTo(lhs.getCreateTime());
						} else if (!lhs.getTitle().equals(rhs.getTitle())) {
							return lhs.getTitle().compareTo(rhs.getTitle());
						} else if (!lhs.getStar().equals(rhs.getStar())) {
							return lhs.getStar().compareTo(rhs.getStar());
						} else {
							return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
						}
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else {
						// lhsIsOverdue >= 0 && rhsIsOverdue < 0
						return -1;
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
						return lhs.getRemindTime().compareTo(rhs.getRemindTime());
					} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
						return rhs.getCreateTime().compareTo(lhs.getCreateTime());
					} else if (!lhs.getTitle().equals(rhs.getTitle())) {
						return lhs.getTitle().compareTo(rhs.getTitle());
					} else if (!lhs.getStar().equals(rhs.getStar())) {
						return lhs.getStar().compareTo(rhs.getStar());
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	// sort the future records by remind time
	// if the remind time is same, sort by create time
	// if the create time is same, sort by title 
	// if the title is same, sort by star
	// is the star is same, sort by id
	public static void sortFutureRecordByRemindTimeReversely() {
		setSortParameter(false, false, false, false, true, true, false, false);
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if ((lhsIsOverdue < 0 && rhsIsOverdue < 0) || (lhsIsOverdue >= 0 && rhsIsOverdue >= 0)) {
						// both are overdue or both are not overdue
						if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
							return rhs.getRemindTime().compareTo(lhs.getRemindTime());
						} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
							return rhs.getCreateTime().compareTo(lhs.getCreateTime());
						} else if (!lhs.getTitle().equals(rhs.getTitle())) {
							return lhs.getTitle().compareTo(rhs.getTitle());
						} else if (!lhs.getStar().equals(rhs.getStar())) {
							return lhs.getStar().compareTo(rhs.getStar());
						} else {
							return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
						}
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else {
						// lhsIsOverdue >= 0 && rhsIsOverdue < 0
						return -1;
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
						return rhs.getRemindTime().compareTo(lhs.getRemindTime());
					} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
						return rhs.getCreateTime().compareTo(lhs.getCreateTime());
					} else if (!lhs.getTitle().equals(rhs.getTitle())) {
						return lhs.getTitle().compareTo(rhs.getTitle());
					} else if (!lhs.getStar().equals(rhs.getStar())) {
						return lhs.getStar().compareTo(rhs.getStar());
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}

	// sort the future records by star
	// if the star is same, sort by create time
	// if the create time is same, sort by title 
	// if the title is same, sort by remind time
	// is the remind time is same, sort by id
	public static void sortFutureRecordByStar() {
		setSortParameter(false, false, false, false, false, false, true, false);
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if ((lhsIsOverdue < 0 && rhsIsOverdue < 0) || (lhsIsOverdue >= 0 && rhsIsOverdue >= 0)) {
						// both are overdue or both are not overdue
						if (!lhs.getStar().equals(rhs.getStar())) {
							return rhs.getStar().compareTo(lhs.getStar());
						} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
							return rhs.getCreateTime().compareTo(lhs.getCreateTime());
						} else if (!lhs.getTitle().equals(rhs.getTitle())) {
							return lhs.getTitle().compareTo(rhs.getTitle());
						} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
							return lhs.getRemindTime().compareTo(rhs.getRemindTime());
						} else {
							return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
						}
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else {
						// lhsIsOverdue >= 0 && rhsIsOverdue < 0
						return -1;
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else if (!lhs.getStar().equals(rhs.getStar())) {
						return lhs.getStar().compareTo(rhs.getStar());
					} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
						return rhs.getCreateTime().compareTo(lhs.getCreateTime());
					} else if (!lhs.getTitle().equals(rhs.getTitle())) {
						return lhs.getTitle().compareTo(rhs.getTitle());
					} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
						return lhs.getRemindTime().compareTo(rhs.getRemindTime());
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	// sort the future records by star reversely
	// if the star is same, sort by create time
	// if the create time is same, sort by title 
	// if the title is same, sort by remind time
	// is the remind time is same, sort by id
	public static void sortFutureRecordByStarReversely() {
		setSortParameter(false, false, false, false, false, false, true, true);
		final String currentTimeString;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		currentTimeString = formatter.format(curDate);
		Collections.sort(futureRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (overdueSort) {
					// lhs is negative means the lhs is overdue
					int lhsIsOverdue = lhs.getRemindTime().compareTo(currentTimeString);
					// rhs is negative means the rhs is overdue
					int rhsIsOverdue = rhs.getRemindTime().compareTo(currentTimeString);
					if ((lhsIsOverdue < 0 && rhsIsOverdue < 0) || (lhsIsOverdue >= 0 && rhsIsOverdue >= 0)) {
						// both are overdue or both are not overdue
						if (!lhs.getStar().equals(rhs.getStar())) {
							return lhs.getStar().compareTo(rhs.getStar());
						} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
							return rhs.getCreateTime().compareTo(lhs.getCreateTime());
						} else if (!lhs.getTitle().equals(rhs.getTitle())) {
							return lhs.getTitle().compareTo(rhs.getTitle());
						} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
							return lhs.getRemindTime().compareTo(rhs.getRemindTime());
						} else {
							return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
						}
					} else if (lhsIsOverdue < 0 && rhsIsOverdue >= 0) {
						return 1;
					} else {
						// lhsIsOverdue >= 0 && rhsIsOverdue < 0
						return -1;
					}
				} else {
					if (lhs.getBeTop() != rhs.getBeTop()) {
						return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
					} else if (!lhs.getStar().equals(rhs.getStar())) {
						return rhs.getStar().compareTo(lhs.getStar());
					} else if (!lhs.getCreateTime().equals(rhs.getCreateTime())) {
						return rhs.getCreateTime().compareTo(lhs.getCreateTime());
					} else if (!lhs.getTitle().equals(rhs.getTitle())) {
						return lhs.getTitle().compareTo(rhs.getTitle());
					} else if (!lhs.getRemindTime().equals(rhs.getRemindTime())) {
						return lhs.getRemindTime().compareTo(rhs.getRemindTime());
					} else {
						return Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId()));
					}
				}
			}
		});
	}
	
	private static void setSortParameter(
			boolean _isSortedByTitle,
			boolean _isSortedByTitleReversely,
			boolean _isSortByCreateTime,
			boolean _isSortedByCreateTimeReversely,
			boolean _isSortedByRemindTime,
			boolean _isSortedByRemindTimeReversely,
			boolean _isSortedByStar,
			boolean _isSortedByStarReversely) {
		isSortedByTitle = _isSortedByTitle;
		isSortedByTitleReversely = _isSortedByTitleReversely;
		isSortByCreateTime = _isSortByCreateTime;
		isSortedByCreateTimeReversely = _isSortedByCreateTimeReversely;
		isSortedByRemindTime = _isSortedByRemindTime;
		isSortedByRemindTimeReversely = _isSortedByRemindTimeReversely;
		isSortedByStar = _isSortedByStar;
		isSortedByStarReversely = _isSortedByStarReversely;
	}
	
	public static void sortFutureRecordsByLastSort() {
		if (isSortByCreateTime) {
			if (isSortedByCreateTimeReversely) {
				sortFutureRecordByCreateTimeReversely();
			} else {
				sortFutureRecordByCreateTime();
			}
		} else if (isSortedByRemindTime) {
			if (isSortedByRemindTimeReversely) {
				sortFutureRecordByRemindTimeReversely();
			} else {
				sortFutureRecordByRemindTime();
			}
		} else if (isSortedByStar) {
			if (isSortedByStarReversely) {
				sortFutureRecordByStarReversely();
			} else {
				sortFutureRecordByStar();
			}
		} else if (isSortedByTitle) {
			if (isSortedByTitleReversely) {
				sortFutureRecordByTitleReversely();
			} else {
				sortFutureRecordByTitle();
			}
		}
	}
	
	public static void sortPastRecordsByRemindTime() {
		setPastSortParameter(true, false, false, false);
		Collections.sort(pastRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (lhs.getBeTop() != rhs.getBeTop()) {
					return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
				} else {
					return lhs.getRemindTime().compareTo(rhs.getRemindTime());
				}
			}
		});
	}
	
	public static void sortPastRecordsByRemindTimeReversely() {
		setPastSortParameter(true, true, false, false);
		Collections.sort(pastRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (lhs.getBeTop() != rhs.getBeTop()) {
					return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
				} else {
					return rhs.getRemindTime().compareTo(lhs.getRemindTime());
				}
			}
		});
	}
	
	public static void sortPastRecordsByRemainTime() {
		setPastSortParameter(false, false, true, false);
		Collections.sort(pastRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (lhs.getBeTop() != rhs.getBeTop()) {
					return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
				} else {
					return Integer.valueOf(calculateRemainDays(lhs)).compareTo(Integer.valueOf(calculateRemainDays(rhs)));
				}
			}
		});
	}
	
	public static void sortPastRecordsByRemainTimeReversely() {
		setPastSortParameter(false, false, true, true);
		Collections.sort(pastRecords, new Comparator<Record>() {
			@Override
			public int compare(Record lhs, Record rhs) {
				if (lhs.getBeTop() != rhs.getBeTop()) {
					return Integer.valueOf(rhs.getBeTop()).compareTo(Integer.valueOf(lhs.getBeTop()));
				} else {
					return Integer.valueOf(calculateRemainDays(rhs)).compareTo(Integer.valueOf(calculateRemainDays(lhs)));
				}
			}
		});
	}
	
	private static void setPastSortParameter(
			boolean _pastIsSortByRemindTime,
			boolean _pastIsSortByRemindTimeReversely,
			boolean _pastIsSortByRemainInTime,
			boolean _pastIsSortByRemainInTimeReversely) {
		pastIsSortByRemindTime = _pastIsSortByRemindTime;
		pastIsSortByRemindTimeReversely = _pastIsSortByRemindTimeReversely;
		pastIsSortByRemainTime = _pastIsSortByRemainInTime;
		pastIsSortByRemainTimeReversely = _pastIsSortByRemainInTimeReversely;
		
	}
	
	private static void sortPastRecordsByLastSort() {
		if (pastIsSortByRemindTime) {
			if (pastIsSortByRemindTimeReversely) {
				sortPastRecordsByRemindTime();
			} else {
				sortPastRecordsByRemindTimeReversely();
			}
		} else if (pastIsSortByRemainTime) {
			if (pastIsSortByRemainTimeReversely) {
				sortPastRecordsByRemainTime();
			} else {
				sortPastRecordsByRemainTimeReversely();
			}
		}
	}
	
	// according to the remind time and the type to calculate the remain time
	public static int calculateRemainDays(Record record) {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date remindDate = new Date();
		try {
			remindDate = formatter.parse(record.getRemindTime());
			remindDate.setHours(GlobalSettings.REMIND_HOUR);
			remindDate.setMinutes(GlobalSettings.REMIND_MINUTE);
		} catch (ParseException p) {
			p.printStackTrace();
		}
		
		Date curDate = new Date(System.currentTimeMillis());
		
		if (record.getType().equals("PAST_W")) {
			// every week
			while (remindDate.before(curDate)) {
				remindDate.setTime(remindDate.getTime() + GlobalSettings.A_WEEK);
			}
			return (int)((remindDate.getTime() - curDate.getTime()) / GlobalSettings.A_DAY);
		} else if (record.getType().equals("PAST_M")) {
			// every month
			while (remindDate.before(curDate)) {
				int month = remindDate.getMonth() + 1;
				if (month == 13) {
					remindDate.setYear(remindDate.getYear() + 1);
					month = 1;
				}
				remindDate.setMonth(month);
			}
			return (int)((remindDate.getTime() - curDate.getTime()) / GlobalSettings.A_DAY);
		} else if (record.getType().equals("PAST_Y")) {
			// every year
			while (remindDate.before(curDate)) {
				remindDate.setYear(remindDate.getYear() + 1);
			}
			return (int)((remindDate.getTime() - curDate.getTime()) / GlobalSettings.A_DAY);
		} else if (record.getType().equals("PAST_N")) {
			return GlobalSettings.MAX_INT;
		} else if (record.getType().equals("PAST_H")) {
			// every 100 days
			while (remindDate.before(curDate)) {
				remindDate.setTime(remindDate.getTime() + 100 * GlobalSettings.A_DAY);
			}
			return (int)((remindDate.getTime() - curDate.getTime()) / GlobalSettings.A_DAY);
		} else if (record.getType().equals("PAST_T")) {
			// every 1000 days
			while (remindDate.before(curDate)) {
				remindDate.setTime(remindDate.getTime() + 1000 * GlobalSettings.A_DAY);
			}
			return (int)((remindDate.getTime() - curDate.getTime()) / GlobalSettings.A_DAY);
		} else {
			Log.d("TimeFleeting", "Error type in calculateRemainDays function");
			return -1;
		}
	}
	
}
