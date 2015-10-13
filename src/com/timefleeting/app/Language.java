package com.timefleeting.app;

public class Language {

	public static boolean isChinese = false;
	
	public static String[] advancedTimeDataText;
	
	public static String getAppNameText() {
		return isChinese ? "时光机" : "Time Fleeting";
	}
	
	public static String getTodoNotificationText() {
		return isChinese ? "追·提醒" : "Todo Notification";
	}
	
	public static String getMemoryNotificationText() {
		return isChinese ? "忆·提醒" : "Memory Notification";
	}
	
	public static String getVibrateText() {
		return isChinese ? "震动" : "Vibrate";
	}
	
	public static String getSoundText() {
		return isChinese ? "声音" : "Sound";
	}
	
	public static String getMemoryRemindTimeText() {
		return isChinese ? "此时·忆" : "Memory remind time";
	}
	
	public static String getAdvancedTimeText() {
		return isChinese ? "提前·忆" : "Advanced time";
	}
	
	public static String getDrawMeText() {
		return isChinese ? "染色" : "Dye me";
	}
	
	public static String getOriginalMeText() {
		return isChinese ? "原色" : "Original me";
	}
	
	public static String getLanguageText() {
		return isChinese ? "Speak English" : "说中文";
	}
	
	public static String[] getAdvancedTimeDataText() {
		String[] eSpinnerStrings = {"1 day",
				   "3 days",
				   "1 week",
				   "2 weeks",
				   "1 month"};
		String[] cSpinnerStrings = {"1 天",
				   "3 天",
				   "1 周",
				   "2 周",
				   "1 月"};
		advancedTimeDataText = isChinese ? cSpinnerStrings : eSpinnerStrings;
		return advancedTimeDataText;
	}
	
	public static String getTitleText(int position) {
		return position == 0 ? (isChinese ? "忆" : "Memory") : (isChinese ? "追" : "Future");
	}
	
}
