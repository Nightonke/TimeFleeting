package com.timefleeting.app;

public class Language {

	public static boolean isChinese = false;
	
	public static String[] advancedTimeDataText;
	
	public static String getAppNameText() {
		return isChinese ? "ʱ���" : "Time Fleeting";
	}
	
	public static String getTodoNotificationText() {
		return isChinese ? "׷������" : "Todo Notification";
	}
	
	public static String getMemoryNotificationText() {
		return isChinese ? "�䡤����" : "Memory Notification";
	}
	
	public static String getVibrateText() {
		return isChinese ? "��" : "Vibrate";
	}
	
	public static String getSoundText() {
		return isChinese ? "����" : "Sound";
	}
	
	public static String getMemoryRemindTimeText() {
		return isChinese ? "��ʱ����" : "Memory remind time";
	}
	
	public static String getAdvancedTimeText() {
		return isChinese ? "��ǰ����" : "Advanced time";
	}
	
	public static String getDrawMeText() {
		return isChinese ? "Ⱦɫ" : "Dye me";
	}
	
	public static String getOriginalMeText() {
		return isChinese ? "ԭɫ" : "Original me";
	}
	
	public static String getLanguageText() {
		return isChinese ? "Speak English" : "˵����";
	}
	
	public static String[] getAdvancedTimeDataText() {
		String[] eSpinnerStrings = {"1 day",
				   "3 days",
				   "1 week",
				   "2 weeks",
				   "1 month"};
		String[] cSpinnerStrings = {"1 ��",
				   "3 ��",
				   "1 ��",
				   "2 ��",
				   "1 ��"};
		advancedTimeDataText = isChinese ? cSpinnerStrings : eSpinnerStrings;
		return advancedTimeDataText;
	}
	
	public static String getTitleText(int position) {
		return position == 0 ? (isChinese ? "��" : "Memory") : (isChinese ? "׷" : "Future");
	}
	
}
