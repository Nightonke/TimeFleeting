package com.timefleeting.app;

import android.R.string;

public class Language {

	public static boolean isChinese = false;
	
	public static String[] advancedTimeDataText;
	
	public static String[] colorDataText;
	
	public static String[] repeatDataText;
	
	private static String[] eSpinnerStrings = {"1 day",
											   "3 days",
											   "1 week",
											   "2 weeks",
											   "1 month"};
	
	private static String[] cSpinnerStrings = {"1 ��",
											   "3 ��",
											   "1 ��",
											   "2 ��",
											   "1 ��"};
	
	private static String[] eSetColorSpinnerStrings = {"Title background color",
													   "Title text color",
													   "Item background color",
													   "Body background color",
												       "Item title text color",
												       "Item content text color",
												 	   "Item remain days text color"};
	
	private static String[] cSetColorSpinnerStrings = {"���ⱳ��",
													   "һ������",
													   "ÿ���",
													   "���ⱳ��",
												       "ÿ�����",
												       "ÿ������",
												 	   "ÿ���"};
	
	private static String[] eDayOfWeekStrings = {"Sunday",
										         "Monday",
										         "Tuesday",
										         "Wednesday",
										         "Thursday",
										         "Friday",
										         "Saturday"};
	
	private static String[] cDayOfWeekStrings = {"������",
										         "����һ",
										         "���ڶ�",
										         "������",
										         "������",
										         "������",
										         "������"};
	
	
	
	private static String[] eRepeatStrings = {"Every year", 
											  "Every month",
											  "Every week",
											  "Every hundred days",
											  "Every thousand days",
											  "Don't repeat"};
	
	private static String[] cRepeatStrings = {"ÿ��", 
											  "ÿ��",
											  "ÿ��",
											  "ÿ����",
											  "ÿǧ��",
											  "���ظ�"};
	
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
	
	public static void setAdvancedTimeDataText() {
		advancedTimeDataText = isChinese ? cSpinnerStrings : eSpinnerStrings;
	}
	
	public static void setColorDataText() {
		colorDataText = isChinese ? cSetColorSpinnerStrings : eSetColorSpinnerStrings;
	}
	
	public static void setRepeatDataText() {
		repeatDataText = isChinese ? cRepeatStrings : eRepeatStrings;
	}
	
	public static String getTitleText(int position) {
		return position == 0 ? (isChinese ? "��" : "Memory") : (isChinese ? "׷" : "Future");
	}
	
	public static String getDayOfWeekText(int i) {
		return isChinese ? cDayOfWeekStrings[i] : eDayOfWeekStrings[i];
	}
	
	public static String getEditPastTitleLayoutText(boolean isOld) {
		return isOld ? (isChinese ? "��" : "Change Memory") : (isChinese ? "��" : "New Memory");
	}
	
	public static String getEditPastTitleText() {
		return isChinese ? "����" : "Title";
	}
	
	public static String getEditPastTitleHintText() {
		return isChinese ? "����������" : "Click to input title";
	}
	
	public static String getEditPastDateText() {
		return isChinese ? "����" : "Date";
	}
	
	public static String getEditPastRepeatText() {
		return isChinese ? "�ظ�" : "Repeat";
	}
	
	public static String getEditPastRemarksText() {
		return isChinese ? "��ע" : "Remarks";
	}
	
	public static String getEditPastRemarksHintText() {
		return isChinese ? "������뱸ע" : "Click to input remarks";
	}
	
	public static String getToastGetTitle() {
		return isChinese ? "���Ը����ұ�����" : "May I get a title?";
	}
	
	public static String getEditFutureTitleHintText() {
		return isChinese ? "����" : "Title";
	}
	
	public static String getEditFutureContentHintText() {
		return isChinese ? "����" : "Content";
	}
	
	public static String getEditFutureWordText() {
		return isChinese ? " ��---" : " word---";
				
	}
	
	public static String getToastSaveSuccessfullyText() {
		return isChinese ? "������" : "Save successfully";
	}
	
	public static String getToastCopiedText() {
		return isChinese ? "������" : "Copied";
	}
	
	public static String getToastPastedText() {
		return isChinese ? "ճ��" : "Pasted";
	}
	
	public static String getOKText() {
		return isChinese ? "��" : "OK";
	}
	
	public static String getDeleteText() {
		return isChinese ? "ɾ�����" : "Delete?";
	}
	
	public static String getDeleteYesText() {
		return isChinese ? "��" : "Yeah";
	}
	
	public static String getDeleteNoText() {
		return isChinese ? "������" : "Oh no";
	}
	
	public static String getToastDeleteText() {
		return isChinese ? "ɾ��" : "Delete successfully";
	}
	
	public static String getSaveText() {
		return isChinese ? "������" : "Save?";
	}
	
	public static String getSaveCancelText() {
		return isChinese ? "�ȵ�" : "Cancel";
	}
	
	public static String getSaveNoText() {
		return isChinese ? "��" : "No";
	}
	
	public static String getSaveYesText() {
		return isChinese ? "��" : "Sure";
	}
		
	public static String getSaveTip(boolean isRemind, boolean isStared) {
		if (isRemind && isStared) {
			return "";
		} else if (!isRemind && isStared) {
			return isChinese ? "Ĭ��7�������" : "Remind after 7 days defaultly";
		} else if (isRemind && !isStared) {
			return isChinese ? "Ĭ�����ȼ�3" : "Set level 3 defaultly";
		} else {
			return isChinese ? "Ĭ��7������ѡ����ȼ�3" : "Remind after 7 days and set level 3 defaultly";
		}
	}
	
	public static String getToastSortByTitleText(boolean reverse) {
		return reverse ? (isChinese ? "�����⵹��������" : "Sort by title reversely") : (isChinese ? "����������" : "Sort by title");
	}
	
	public static String getToastSortByRemindTimeText(boolean reverse) {
		return reverse ? (isChinese ? "������ʱ�䵹��������" : "Sort by remind time reversely") : (isChinese ? "������ʱ������" : "Sort by remind time");
	}
	
	public static String getToastSortByCreateTimeText(boolean reverse) {
		return reverse ? (isChinese ? "������ʱ�䵹��������" : "Sort by create time reversely") : (isChinese ? "������ʱ������" : "Sort by create time");
	}
	
	public static String getToastSortByRemainTimeText(boolean reverse) {
		return reverse ? (isChinese ? "��ʣ��ʱ�䵹��������" : "Sort by remain time reversely") : (isChinese ? "��ʣ��ʱ������" : "Sort by remain time");
	}
	
	public static String getToastSortByStarText(boolean reverse) {
		return reverse ? (isChinese ? "�����ȼ�����������" : "Sort by level reversely") : (isChinese ? "�����ȼ�����" : "Sort by level");
	}
	
}
