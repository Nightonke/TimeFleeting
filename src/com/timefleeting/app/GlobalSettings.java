package com.timefleeting.app;

import com.daimajia.androidanimations.library.Techniques;

public class GlobalSettings {

	// the remind time in milliseconds
	// the default value is a week(7 days)
	public static int REMIND_TIME = 7 * 1000 * 60 * 60 * 24;
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	
}
