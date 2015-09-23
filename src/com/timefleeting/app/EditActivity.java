package com.timefleeting.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.Object;

import com.capricorn.RayMenu;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends FragmentActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener, ScrollViewListener  {

	private final String dEF_TITLE_STRING = "Unnaming";
	
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
	
	private TextView createTimeTextView;
	
	private RayMenu rayMenu;
	private boolean lastIsScrollDown = false;
	
	private static final int[] ITEM_DRAWABLES_FUTURE = {
		R.drawable.save,
		R.drawable.sort_by_remind_time,
		R.drawable.sort_by_star,
		R.drawable.copy,
		R.drawable.statistics,
		R.drawable.home};
	
	private boolean isSaved = false;
	private boolean isRemind = false;
	private boolean isStared = false;
	private boolean afterSetTimeBack = false;
	
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
		
		Intent intent = getIntent();
		isOld = intent.getBooleanExtra("isOld", false);
		
		if (isOld) {
			oldTitleString = intent.getStringExtra("Title");
			oldContentString = intent.getStringExtra("Content");
			createTimeString = intent.getStringExtra("CreateTime");
			remindTimeString = intent.getStringExtra("RemindTime");
			starString = intent.getStringExtra("Star");
			isSaved = true;
			isRemind = true;
			isStared = true;
			saveId = intent.getIntExtra("ID", -1);
		}

		observableScrollView = (ObservableScrollView)findViewById(R.id.editview_scrollview);
		observableScrollView.setScrollViewListener(this);
		
		if (!isOld) {
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
			Date curDate = new Date(System.currentTimeMillis());
			createTimeString = formatter.format(curDate); 
		} 
		
		createTimeTextView = (TextView)findViewById(R.id.create_time_textview);
		createTimeTextView.setText("---" + createTimeString);
		
		titleEditText = (EditText)findViewById(R.id.edit_layout_title);
		
		if (isOld) {
			titleEditText.setText(oldTitleString);
		}
		
		titleEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				isSaved = false;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		contentEditText = (EditText)findViewById(R.id.edit_layout_content);
		
		if (isOld) {
			contentEditText.setText(oldContentString);
		}
		
		contentEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				isSaved = false;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
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
						// set the remind time
						setRemindTime();
					} else if (menuPosition == 2) {
						setStar(false);
					} else if (menuPosition == 5) {
						goBack(true);
					}
				}
			});
		}
		
	}
	
	@Override
	public void onBackPressed() {
		goBack(true);
	}
	
	private void goBack(boolean isBack) {

		String titleString = titleEditText.getText().toString();
		String contentString = contentEditText.getText().toString();
		
		if (("".equals(titleString) && "".equals(contentString)) || isSaved) {
			returnHome();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(R.drawable.save_without_circle);
			builder.setTitle("等等！");
			builder.setMessage("请问保存吗？");
			builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// save
					saveAndReturnHome();
				}
			});
			builder.setNeutralButton("不保存", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					returnHome();
				}
			});
			builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
			builder.show();
		}
	}
	
	private void saveAndReturnHome() {
		save(true);
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
			titleString = dEF_TITLE_STRING; 
		}
		
		if (isSaved) {
			// have click the save button
			Log.d("TimeFleeting", saveId + "");
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
				if (!isRemind) {
					// haven't clicked the remind button
					// set the time then go back
					afterSetTimeBack = isBack;
					isSaved = true;
					setRemindTime();
				} else {
					// have clicked the remind button
					if (!isStared) {
						setStar(isBack);
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

		if (afterSetTimeBack) {
			if (!isStared) {
				setStar(true);
			} else {
				save(true);
			}
		} else {
			
		}
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
	
	private void setStar(final boolean isBack) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.save_without_circle);
		builder.setTitle("优先级");
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View view = layoutInflater.inflate(R.layout.set_star, null);
		builder.setView(view);
		starString = "0";
		final ImageView star1 = (ImageView)view.findViewById(R.id.star_1);
		final ImageView star2 = (ImageView)view.findViewById(R.id.star_2);
		final ImageView star3 = (ImageView)view.findViewById(R.id.star_3);
		final ImageView star4 = (ImageView)view.findViewById(R.id.star_4);
		final ImageView star5 = (ImageView)view.findViewById(R.id.star_5);
		star1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				star1.setImageResource(R.drawable.star_blue);
				star2.setImageResource(R.drawable.star_blank);
				star3.setImageResource(R.drawable.star_blank);
				star4.setImageResource(R.drawable.star_blank);
				star5.setImageResource(R.drawable.star_blank);
				starString = "1";
			}
		});
		star2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				star1.setImageResource(R.drawable.star_blue);
				star2.setImageResource(R.drawable.star_blue);
				star3.setImageResource(R.drawable.star_blank);
				star4.setImageResource(R.drawable.star_blank);
				star5.setImageResource(R.drawable.star_blank);
				starString = "2";
			}
		});
		star3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				star1.setImageResource(R.drawable.star_blue);
				star2.setImageResource(R.drawable.star_blue);
				star3.setImageResource(R.drawable.star_blue);
				star4.setImageResource(R.drawable.star_blank);
				star5.setImageResource(R.drawable.star_blank);
				starString = "3";
			}
		});
		star4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				star1.setImageResource(R.drawable.star_blue);
				star2.setImageResource(R.drawable.star_blue);
				star3.setImageResource(R.drawable.star_blue);
				star4.setImageResource(R.drawable.star_blue);
				star5.setImageResource(R.drawable.star_blank);
				starString = "4";
			}
		});
		star5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				star1.setImageResource(R.drawable.star_blue);
				star2.setImageResource(R.drawable.star_blue);
				star3.setImageResource(R.drawable.star_blue);
				star4.setImageResource(R.drawable.star_blue);
				star5.setImageResource(R.drawable.star_blue);
				starString = "5";
			}
		});
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				isStared = true;
				if (isBack) {
					save(true);
				}
			}
		});
		
		builder.show();
	}
	
	
	
}
