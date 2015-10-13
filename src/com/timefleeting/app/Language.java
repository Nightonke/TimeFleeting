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
	
	private static String[] cSpinnerStrings = {"1 天",
											   "3 天",
											   "1 周",
											   "2 周",
											   "1 月"};
	
	private static String[] eSetColorSpinnerStrings = {"Title background color",
													   "Title text color",
													   "Item background color",
													   "Body background color",
												       "Item title text color",
												       "Item content text color",
												 	   "Item remain days text color"};
	
	private static String[] cSetColorSpinnerStrings = {"标题背景",
													   "一般文字",
													   "每项背景",
													   "主题背景",
												       "每项标题",
												       "每项文字",
												 	   "每项倒数"};
	
	private static String[] eDayOfWeekStrings = {"Sunday",
										         "Monday",
										         "Tuesday",
										         "Wednesday",
										         "Thursday",
										         "Friday",
										         "Saturday"};
	
	private static String[] cDayOfWeekStrings = {"星期天",
										         "星期一",
										         "星期二",
										         "星期三",
										         "星期四",
										         "星期五",
										         "星期六"};
	
	
	
	private static String[] eRepeatStrings = {"Every year", 
											  "Every month",
											  "Every week",
											  "Every hundred days",
											  "Every thousand days",
											  "Don't repeat"};
	
	private static String[] cRepeatStrings = {"每年", 
											  "每月",
											  "每周",
											  "每百日",
											  "每千日",
											  "不重复"};
	
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
		return position == 0 ? (isChinese ? "忆" : "Memory") : (isChinese ? "追" : "Future");
	}
	
	public static String getDayOfWeekText(int i) {
		return isChinese ? cDayOfWeekStrings[i] : eDayOfWeekStrings[i];
	}
	
	public static String getEditPastTitleLayoutText(boolean isOld) {
		return isOld ? (isChinese ? "忆" : "Change Memory") : (isChinese ? "忆" : "New Memory");
	}
	
	public static String getEditPastTitleText() {
		return isChinese ? "标题" : "Title";
	}
	
	public static String getEditPastTitleHintText() {
		return isChinese ? "点击输入标题" : "Click to input title";
	}
	
	public static String getEditPastDateText() {
		return isChinese ? "日期" : "Date";
	}
	
	public static String getEditPastRepeatText() {
		return isChinese ? "重复" : "Repeat";
	}
	
	public static String getEditPastRemarksText() {
		return isChinese ? "备注" : "Remarks";
	}
	
	public static String getEditPastRemarksHintText() {
		return isChinese ? "点击输入备注" : "Click to input remarks";
	}
	
	public static String getToastGetTitle() {
		return isChinese ? "可以告诉我标题吗？" : "May I get a title?";
	}
	
	public static String getEditFutureTitleHintText() {
		return isChinese ? "标题" : "Title";
	}
	
	public static String getEditFutureContentHintText() {
		return isChinese ? "正文" : "Content";
	}
	
	public static String getEditFutureWordText() {
		return isChinese ? " 字---" : " word---";
				
	}
	
	public static String getToastSaveSuccessfullyText() {
		return isChinese ? "保存了" : "Save successfully";
	}
	
	public static String getToastCopiedText() {
		return isChinese ? "复制了" : "Copied";
	}
	
	public static String getToastPastedText() {
		return isChinese ? "粘贴" : "Pasted";
	}
	
	public static String getOKText() {
		return isChinese ? "好" : "OK";
	}
	
	public static String getDeleteText() {
		return isChinese ? "删这个吗？" : "Delete?";
	}
	
	public static String getDeleteYesText() {
		return isChinese ? "嗯" : "Yeah";
	}
	
	public static String getDeleteNoText() {
		return isChinese ? "不不不" : "Oh no";
	}
	
	public static String getToastDeleteText() {
		return isChinese ? "删了" : "Delete successfully";
	}
	
	public static String getSaveText() {
		return isChinese ? "保存吗？" : "Save?";
	}
	
	public static String getSaveCancelText() {
		return isChinese ? "等等" : "Cancel";
	}
	
	public static String getSaveNoText() {
		return isChinese ? "不" : "No";
	}
	
	public static String getSaveYesText() {
		return isChinese ? "嗯" : "Sure";
	}
		
	public static String getSaveTip(boolean isRemind, boolean isStared) {
		if (isRemind && isStared) {
			return "";
		} else if (!isRemind && isStared) {
			return isChinese ? "默认7天后提醒" : "Remind after 7 days defaultly";
		} else if (isRemind && !isStared) {
			return isChinese ? "默认优先级3" : "Set level 3 defaultly";
		} else {
			return isChinese ? "默认7天后提醒、优先级3" : "Remind after 7 days and set level 3 defaultly";
		}
	}
	
	public static String getToastSortByTitleText(boolean reverse) {
		return reverse ? (isChinese ? "按标题倒着来排序" : "Sort by title reversely") : (isChinese ? "按标题排序" : "Sort by title");
	}
	
	public static String getToastSortByRemindTimeText(boolean reverse) {
		return reverse ? (isChinese ? "按提醒时间倒着来排序" : "Sort by remind time reversely") : (isChinese ? "按提醒时间排序" : "Sort by remind time");
	}
	
	public static String getToastSortByCreateTimeText(boolean reverse) {
		return reverse ? (isChinese ? "按创建时间倒着来排序" : "Sort by create time reversely") : (isChinese ? "按创建时间排序" : "Sort by create time");
	}
	
	public static String getToastSortByRemainTimeText(boolean reverse) {
		return reverse ? (isChinese ? "按剩余时间倒着来排序" : "Sort by remain time reversely") : (isChinese ? "按剩余时间排序" : "Sort by remain time");
	}
	
	public static String getToastSortByStarText(boolean reverse) {
		return reverse ? (isChinese ? "按优先级倒着来排序" : "Sort by level reversely") : (isChinese ? "按优先级排序" : "Sort by level");
	}
	
}
