package com.timefleeting.app;

import java.util.List;

import android.graphics.Color;

import com.daimajia.androidanimations.library.Techniques;

public class GlobalSettings {

	//
	public static int REMIND_DAYS = 7;
	
	// the remind time in milliseconds
	// the default value is a week(7 days)
	public static long REMIND_TIME = 7 * 1000 * 60 * 60 * 24;
	
	// the hour time of a day to remind
	public static int REMIND_HOUR = 19;
	
	// the minute time of a day to remind
	public static int REMIND_MINUTE = 28;
	
	// a week
	public static long A_WEEK = 7 * 24 * 60 * 60 * 1000;
	
	// a day
	public static long A_DAY = 24 * 60 * 60 * 1000;
	
	// the max number of integer
	public static int MAX_INT = 99999999;
	
	// the default be top number
	public static int BE_TOP = 0;
	
	// the default status
	public static String STATUS = "DOING";
	
	// i even don't know what the hell it is
	public static String DATEPICKER_TAG = "Choose date";
	
	// i even don't know what the hell it is
	public static String TIMEPICKER_TAG = "Choose time";
	
	// tip animation in milliseconds
	public static int TIP_ANIMATION_DURATION = 1000;
	
	// tip animation style
	public static Techniques TIP_ANIMATION_STYLE = Techniques.Tada;
	
	// tip animation delay
	public static int TIP_ANIMATION_DELAY = 0;
	
	// full date format
	public static String FULL_DATE_FORMAT = "yyyy-MM-dd-HH:mm:ss";
	
	// the origin height of the wave, in progress
	public static int DEFAULT_WAVE_HEIGHT = 10;
	
	// the tips in whether_save
	public static String TIP_WHETHER_SAVE_ISREMIND_ISSTARED = "";
	public static String TIP_WHETHER_SAVE_ISNOTREMIND_ISSTARED = "Use default remind time";
	public static String TIP_WHETHER_SAVE_ISREMIND_ISNOTSTARED = "Use default star";
	public static String TIP_WHETHER_SAVE_ISNOTREMIND_ISNOTSTARED = "Use default remind time and star";
	
	// the default title for the record that don't get a title
	public static String DEFAULT_TITLE = "Unnamed record";
	
	// the default star for the record that don't get a star
	public static String DEFAULT_STAR = "3";
	
	// the word counter's postfix
	public static String WORD_COUNTER_POSTFIX = " word---";
	
	// the title of the future
	public static String FUTURE_TITLE = "Future";
	
	// the title of the past
	public static String PAST_TITLE = "Memory";
	
	// the remind time
	public static int ALARM_TIME = 0;
	
	// the ahead days
	public static int AHEAD_DAYS = 3;
	
	// whether open reminder
	public static boolean REMIND_ENABLE = true;
	
	// whether open memory reminder
	public static boolean REMIND_PAST_ENABLE = true;
	
	// the global remind list
	public static List<Remind> REMIND_LIST;
	
	// the global remind past list
	public static List<Remind> REMIND_PAST_LIST;
	
	// whether vibrate
	public static boolean VIBRATE_ENABLE = true;
	
	// whether sound
	public static boolean SOUND_ENABLE = true;
	
	// default title background color
	public static int DEFAULT_TITLE_BACKGROUND_COLOR = Color.parseColor("#66CC69");
	// title background color
	public static int TITLE_BACKGROUND_COLOR;
	
	// default title title color
	public static int DEFAULT_TITLE_TEXT_COLOR = Color.parseColor("#004000");
	// title background color
	public static int TITLE_TEXT_COLOR;
	
	// default item background color
	public static int DEFAULT_ITEM_BACKGROUND_COLOR = Color.parseColor("#3FFF00");
	// item background color
	public static int ITEM_BACKGROUND_COLOR;
	
	// default item wave color
	public static int DEFAULT_ITEM_WAVE_COLOR = Color.parseColor("#009E10");
	// item wave color
	public static int ITEM_WAVE_COLOR;
	
	// default body background color
	public static int DEFAULT_BODY_BACKGROUND_COLOR = Color.parseColor("#f4f4f4");
	// body background color
	public static int BODY_BACKGROUND_COLOR;
}
