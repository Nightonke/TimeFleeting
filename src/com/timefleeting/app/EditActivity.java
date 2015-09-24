package com.timefleeting.app;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.Object;

import com.capricorn.RayMenu;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ScrollingView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class EditActivity extends FragmentActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener, ScrollViewListener  {

	private final String DEF_TITLE_STRING = "Unnaming";
	private final String DEFAULT_STAR = "3";
	
	public static final String DATEPICKER_TAG = "选择日期";
    public static final String TIMEPICKER_TAG = "选择时间";
	
	private Context mContext;
	private TimeFleetingData timeFleetingData;
	private boolean isOld = false;
	private String oldTitleString;
	private String oldContentString;
	private String createTimeString;
	private String remindTimeString;
	private String starString = "0";
	private String wordNumberString;
	
	private TextView createTimeTextView;
	
	private RayMenu rayMenu;
	private boolean lastIsScrollDown = false;
	
	private static final int[] ITEM_DRAWABLES_FUTURE = {
		R.drawable.save,
		R.drawable.sort_by_remind_time,
		R.drawable.sort_by_star,
		R.drawable.copy_c,
		R.drawable.copy_v};
	
	private boolean isSaved = false;
	private boolean isRemind = false;
	private boolean isStared = false;
	private boolean afterSetTimeBack = false;
	
	private TextView wordNumberTextView;
	private EditText titleEditText;
	private EditText contentEditText;
	private int saveId = -1;
	
	private ObservableScrollView observableScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_layout);
		
		mContext = this;
		
		titleEditText = (EditText)findViewById(R.id.edit_layout_title);
		wordNumberTextView = (TextView)findViewById(R.id.word_number_textview);
		createTimeTextView = (TextView)findViewById(R.id.create_time_textview);
		contentEditText = (EditText)findViewById(R.id.edit_layout_content);
		
		observableScrollView = (ObservableScrollView)findViewById(R.id.editview_scrollview);
		observableScrollView.setScrollViewListener(this);
		
		Intent intent = getIntent();
		isOld = intent.getBooleanExtra("isOld", false);

		if (isOld) {
			oldTitleString = intent.getStringExtra("Title");
			titleEditText.setText(oldTitleString);
			oldContentString = intent.getStringExtra("Content");
			contentEditText.setText(oldContentString);
			createTimeString = intent.getStringExtra("CreateTime");
			remindTimeString = intent.getStringExtra("RemindTime");
			starString = intent.getStringExtra("Star");
			isSaved = true;
			isRemind = true;
			isStared = true;
			saveId = intent.getIntExtra("ID", -1);
			
			wordNumberString = String.valueOf(oldContentString.length()) + "words---";
			wordNumberTextView.setText(wordNumberString);
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
			Date curDate = new Date(System.currentTimeMillis());
			createTimeString = formatter.format(curDate); 
			remindTimeString = calculateDefaultRemindTime();
			starString = DEFAULT_STAR;
			isSaved = false;
			isRemind = false;
			isStared = false;
			wordNumberTextView.setText("0 words---");
		}

		createTimeTextView.setText("---" + createTimeString);
		
		titleEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				isSaved = false;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		contentEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				wordNumberString = String.valueOf(contentEditText.getText().toString().length()) + "---";
				wordNumberTextView.setText(wordNumberString);
				isSaved = false;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		if (isOld) {
			if (!titleEditText.getText().toString().equals("")) {
				contentEditText.setFocusable(true);
				contentEditText.setFocusableInTouchMode(true);
				contentEditText.requestFocus();
			} else {
				titleEditText.setFocusable(true);
				titleEditText.setFocusableInTouchMode(true);
				titleEditText.requestFocus();
			}
		}

		rayMenu = (RayMenu)findViewById(R.id.edit_layout_ray_menu);
		for (int i = 0; i < ITEM_DRAWABLES_FUTURE.length; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageResource(ITEM_DRAWABLES_FUTURE[i]);
			final int menuPosition = i;
			rayMenu.addItem(imageView, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (menuPosition == 0) {
						// save
						save(false);
					} else if (menuPosition == 1) {
						// set remind time
						setRemindTime();
					} else if (menuPosition == 2) {
						// set star
						setStar();
					} else if (menuPosition == 3) {
						// copy all
						ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
						cmb.setText(contentEditText.getText().toString());
						Toast.makeText(mContext, "Copied", Toast.LENGTH_SHORT).show();
					} else if (menuPosition == 4) {
						// statis
						if (contentEditText.isFocused()) {
							// if focused
							ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							int cursorPosition = contentEditText.getSelectionEnd();
							String newString = contentEditText.getText().toString().substring(0, cursorPosition)
									+ cmb.getText().toString()
									+ contentEditText.getText().toString().substring(cursorPosition, contentEditText.getText().toString().length());
							contentEditText.setText(newString);
							contentEditText.setSelection(cursorPosition + cmb.getText().toString().length());
							wordNumberString = String.valueOf(contentEditText.getText().toString().length()) + "---";
							wordNumberTextView.setText(wordNumberString);
							isSaved = false;
						} else if (titleEditText.isFocused()) {
							// if focused
							ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							int cursorPosition = titleEditText.getSelectionEnd();
							String newString = titleEditText.getText().toString().substring(0, cursorPosition)
									+ cmb.getText().toString()
									+ titleEditText.getText().toString().substring(cursorPosition, titleEditText.getText().toString().length());
							titleEditText.setText(newString);
							titleEditText.setSelection(cursorPosition + cmb.getText().toString().length());
							isSaved = false;
						}
					}
				}
			});
		}
		
	}
	
	@Override
	public void onBackPressed() {
		String titleString = titleEditText.getText().toString();
		String contentString = contentEditText.getText().toString();
		
		if (("".equals(titleString) && "".equals(contentString)) || isSaved) {
			returnHome();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			View view = layoutInflater.inflate(R.layout.whether_save, null);
			builder.setView(view);
			final AlertDialog dialog = builder.show();
			dialog.setCanceledOnTouchOutside(true);
			
			LinearLayout whetherSaveLinearLayout = (LinearLayout)view.findViewById(R.id.whether_save_logo);
			
			TextView tipTextView = (TextView)view.findViewById(R.id.whether_save_tip);
			TextView cancelTextView = (TextView)view.findViewById(R.id.whether_save_cancel);
			TextView noTextView = (TextView)view.findViewById(R.id.whether_save_no);
			TextView sureTextView = (TextView)view.findViewById(R.id.whether_save_sure);
			
			if (isRemind && isStared) {
				tipTextView.setText(GlobalSettings.TIP_WHETHER_SAVE_ISREMIND_ISSTARED);
			} else if (!isRemind && isStared) {
				tipTextView.setText(GlobalSettings.TIP_WHETHER_SAVE_ISNOTREMIND_ISSTARED);
			} else if (isRemind && !isStared) {
				tipTextView.setText(GlobalSettings.TIP_WHETHER_SAVE_ISREMIND_ISNOTSTARED);
			} else {
				tipTextView.setText(GlobalSettings.TIP_WHETHER_SAVE_ISNOTREMIND_ISNOTSTARED);
			}
			
			YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
			.duration(GlobalSettings.TIP_ANIMATION_DURATION)
			.delay(GlobalSettings.TIP_ANIMATION_DELAY)
			.playOn(whetherSaveLinearLayout);
			YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
			.duration(GlobalSettings.TIP_ANIMATION_DURATION)
			.delay(GlobalSettings.TIP_ANIMATION_DELAY)
			.playOn(tipTextView);
			YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
			.duration(GlobalSettings.TIP_ANIMATION_DURATION)
			.delay(GlobalSettings.TIP_ANIMATION_DELAY)
			.playOn(cancelTextView);
			YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
			.duration(GlobalSettings.TIP_ANIMATION_DURATION)
			.delay(GlobalSettings.TIP_ANIMATION_DELAY)
			.playOn(noTextView);
			YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
			.duration(GlobalSettings.TIP_ANIMATION_DURATION)
			.delay(GlobalSettings.TIP_ANIMATION_DELAY)
			.playOn(sureTextView);
			
			cancelTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					return;
				}
			});
			
			noTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					returnHome();
				}
			});
			
			sureTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					save(true);
				}
			});
		}
	}
	
	// call this function when:
	// click the save button
	// back button
	private void save(boolean isBack) {
		
		try {
			timeFleetingData = TimeFleetingData.getInstance(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		String titleString = titleEditText.getText().toString();
		String contentString = contentEditText.getText().toString();

		if ("".equals(titleString)) {
			titleString = DEF_TITLE_STRING; 
		}
		
		if (isSaved) {
			// have click the save button
			timeFleetingData.saveRecord(new Record(
					saveId,
					titleString,
					contentString,
					remindTimeString,
					createTimeString,
					starString,
					"FUTURE"));
			if (isBack) {
				returnHome();
			}
		} else {
			// haven't click the save button
			if (saveId != -1) {
				// but is from old
				saveId = timeFleetingData.saveRecord(new Record(
						saveId,
						titleString,
						contentString,
						remindTimeString,
						createTimeString,
						starString,
						"FUTURE"));
				isSaved = true;
				if (isBack) {
					returnHome();
				}
			} else {
				saveId = timeFleetingData.saveRecord(new Record(
						-1,
						titleString,
						contentString,
						remindTimeString,
						createTimeString,
						starString,
						"FUTURE"));
				isSaved = true;
				if (isBack) {
					returnHome();
				}
			}
		}
	}
	
	private void setRemindTime() {
		isRemind = false;
		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		datePickerDialog.setYearRange(2015, 2036);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		remindTimeString += (hourOfDay < 10 ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay)) + ":";
		remindTimeString += (minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute)) + ":";
		remindTimeString += "00";
		
		isRemind = true;
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub
		
		isRemind = false;
		
		// a bug in the datetimepicker-library
		// which will cause the month is month - 1
		month++;
		
		remindTimeString = String.valueOf(year) + "-";
		remindTimeString += (month < 10 ? "0" + String.valueOf(month) : String.valueOf(month)) + "-";
		remindTimeString += (day < 10 ? "0" + String.valueOf(day) : String.valueOf(day)) + "-";
		
		final Calendar calendar = Calendar.getInstance();
		
	    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
		
		timePickerDialog.setVibrate(false);
        timePickerDialog.setCloseOnSingleTapMinute(false);
        timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
	}
	
	private void returnHome() {
		Intent intent = new Intent();
		intent.putExtra("isEditActivityFinished", true);
		if (isOld) {
			intent.putExtra("isOld", true);
		}
		setResult(RESULT_OK, intent);

		finish();
	}

	private String calculateDefaultRemindTime() {
		SimpleDateFormat formatter = new SimpleDateFormat (GlobalSettings.FULL_DATE_FORMAT);     
		Date curDate = new Date(System.currentTimeMillis());
		Date createDate = new Date(System.currentTimeMillis());
		try {
			createDate = formatter.parse(createTimeString);
		} catch (ParseException p) {
			p.printStackTrace();
		}
		createDate.setTime(createDate.getTime() + GlobalSettings.REMIND_TIME);
		return formatter.format(createDate);
	}
	
	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// TODO Auto-generated method stub
		rayMenu.closeMenu();
		if (oldy < y) {
			// down
			// should disappear
			if (!lastIsScrollDown) {
				AnimationSet animationSet = new AnimationSet(true);
				TranslateAnimation translateAnimation = 
						new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 1f);
				translateAnimation.setDuration(1000);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
;				rayMenu.startAnimation(animationSet);
			} else {
				
			}
			lastIsScrollDown = true;
		} else if (oldy > y) {
			// up
			// should appear
			if (lastIsScrollDown) {
				AnimationSet animationSet = new AnimationSet(true);
				TranslateAnimation translateAnimation = 
						new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, 1f,
								Animation.RELATIVE_TO_SELF, 0f);
				translateAnimation.setDuration(1000);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
				rayMenu.startAnimation(animationSet);
			} else {
				
			}
			lastIsScrollDown = false;
		}
	}
	
	private void setStar() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View view = layoutInflater.inflate(R.layout.set_star, null);
		builder.setView(view);
		final AlertDialog dialog = builder.show();
		dialog.setCanceledOnTouchOutside(true);
		
		final TextView okTextView = (TextView)view.findViewById(R.id.set_star_ok);

		final ImageView star1 = (ImageView)view.findViewById(R.id.star_1);
		final ImageView star2 = (ImageView)view.findViewById(R.id.star_2);
		final ImageView star3 = (ImageView)view.findViewById(R.id.star_3);
		final ImageView star4 = (ImageView)view.findViewById(R.id.star_4);
		final ImageView star5 = (ImageView)view.findViewById(R.id.star_5);
		star4.setVisibility(View.INVISIBLE);
		star5.setVisibility(View.INVISIBLE);
		final SeekBar seekBar = (SeekBar)view.findViewById(R.id.star_seekbar);
		seekBar.setProgress(2);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (progress == 0) {
					star1.setVisibility(View.VISIBLE);
					star2.setVisibility(View.INVISIBLE);
					star3.setVisibility(View.INVISIBLE);
					star4.setVisibility(View.INVISIBLE);
					star5.setVisibility(View.INVISIBLE);
				} else if (progress == 1) {
					star1.setVisibility(View.VISIBLE);
					star2.setVisibility(View.VISIBLE);
					star3.setVisibility(View.INVISIBLE);
					star4.setVisibility(View.INVISIBLE);
					star5.setVisibility(View.INVISIBLE);
				} else if (progress == 2) {
					star1.setVisibility(View.VISIBLE);
					star2.setVisibility(View.VISIBLE);
					star3.setVisibility(View.VISIBLE);
					star4.setVisibility(View.INVISIBLE);
					star5.setVisibility(View.INVISIBLE);
				} else if (progress == 3) {
					star1.setVisibility(View.VISIBLE);
					star2.setVisibility(View.VISIBLE);
					star3.setVisibility(View.VISIBLE);
					star4.setVisibility(View.VISIBLE);
					star5.setVisibility(View.INVISIBLE);
				} else if (progress == 4) {
					star1.setVisibility(View.VISIBLE);
					star2.setVisibility(View.VISIBLE);
					star3.setVisibility(View.VISIBLE);
					star4.setVisibility(View.VISIBLE);
					star5.setVisibility(View.VISIBLE);
				}
				YoYo.with(Techniques.Tada).duration(1000).delay(500).playOn(star1);
				YoYo.with(Techniques.Tada).duration(1000).delay(500).playOn(star2);
				YoYo.with(Techniques.Tada).duration(1000).delay(500).playOn(star3);
				YoYo.with(Techniques.Tada).duration(1000).delay(500).playOn(star4);
				YoYo.with(Techniques.Tada).duration(1000).delay(500).playOn(star5);
				YoYo.with(Techniques.Tada).duration(1000).delay(500).playOn(okTextView);
			}
		});
		
		okTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				starString = String.valueOf(seekBar.getProgress() + 1);
				isStared = true;
			}
		});
	}

}
