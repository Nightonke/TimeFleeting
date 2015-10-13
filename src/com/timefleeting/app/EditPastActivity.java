package com.timefleeting.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.timefleeting.app.TopBottomScrollView.OnScrollToBottomListener;
import com.timefleeting.app.TopBottomScrollView.OnScrollToTopListener;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditPastActivity extends FragmentActivity 
	implements OnDateSetListener {

	private Context mContext;
	
	private LinearLayout topTitleLinearLayout;
	private LinearLayout infoLinearLayout;
	
	private LinearLayout titleLinearLayout;
	private LinearLayout dateLinearLayout;
	private LinearLayout repeatLinearLayout;
	private LinearLayout remarksLinearLayout;
	
	private EditText titleEditText;
	private EditText textEditText;
	
	private TextView dateTextView;
	private TextView infoTitleTextView;
	private TextView infoRemainTextView;
	private TextView infoDateTextView;
	private TextView infoContentTextView;
	
	private TextView editPastTextView0;
	private TextView editPastTextView1;
	private TextView editPastTextView2;
	private TextView editPastTextView3;
	
	private TopBottomScrollView scrollView = null;
	
	private ImageView check;
	private ImageView back;
	private ImageView editLogo;

	private String createTimeString;
	
	private String dateString;
	private String oldDateString;
	
	private String[] repeatType = {"PAST_Y",
								   "PAST_M",
								   "PAST_W",
								   "PAST_H",
								   "PAST_T",
								   "PAST_N"};
	
	private ArrayAdapter<String> arr_adapter;
	
	private Spinner spinner;
	
	private String repeatString;
	
	private boolean isOld;
	
	private int saveId;
	
	private int beTop = 0;
	private String status = "DOING";
	
	private WaveView pastEditWaveView;
	
	private TranslateAnimation upTranslateAnimation;
	private TranslateAnimation downTranslateAnimation;
	
	private LinearLayout pastEditWaveViewLinearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_past);
		
		mContext = this;
		
		pastEditWaveViewLinearLayout = (LinearLayout)findViewById(R.id.past_edit_wave_view_ly);
		pastEditWaveViewLinearLayout.setBackgroundColor(GlobalSettings.BODY_BACKGROUND_COLOR);
		
		pastEditWaveView = (WaveView)findViewById(R.id.past_edit_waveview);
		pastEditWaveView.setBackgroundColor(GlobalSettings.BODY_BACKGROUND_COLOR);

		upTranslateAnimation = 
				new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 1f,
						Animation.RELATIVE_TO_SELF, 0f);
		upTranslateAnimation.setDuration(10000);
		downTranslateAnimation = 
				new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 1f);
		downTranslateAnimation.setDuration(10000);
		
		upTranslateAnimation.setAnimationListener(new AnimationListener() {
		    @Override
		    public void onAnimationStart(Animation animation) {

		    }

		    @Override
		    public void onAnimationEnd(Animation animation) {
		    	pastEditWaveView.startAnimation(downTranslateAnimation);
		    }

		    @Override
		    public void onAnimationRepeat(Animation animation) {

		    }
		});
		
		downTranslateAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				pastEditWaveView.startAnimation(upTranslateAnimation);
			}
		});
		
		pastEditWaveView.startAnimation(upTranslateAnimation);
		
		topTitleLinearLayout = (LinearLayout)findViewById(R.id.past_edit_layout_title);
		topTitleLinearLayout.setBackgroundColor(GlobalSettings.TITLE_BACKGROUND_COLOR);
		infoLinearLayout = (LinearLayout)findViewById(R.id.info_ly);
		titleLinearLayout = (LinearLayout)findViewById(R.id.past_edit_title_ly);
		dateLinearLayout = (LinearLayout)findViewById(R.id.past_edit_date_ly);
		repeatLinearLayout = (LinearLayout)findViewById(R.id.past_edit_date_ly);
		remarksLinearLayout = (LinearLayout)findViewById(R.id.past_edit_text_ly);
		
		InputFilter[] titleFilters = { new LengthFilter(24) };  
		InputFilter[] textFilters = { new LengthFilter(22) };  
		
		titleEditText = (EditText)findViewById(R.id.past_edit_title);
		titleEditText.setTextColor(GlobalSettings.TITLE_TEXT_COLOR);
		titleEditText.setFilters(titleFilters);
		titleEditText.setHint(Language.getEditPastTitleHintText());
		textEditText = (EditText)findViewById(R.id.past_edit_text);
		textEditText.setTextColor(GlobalSettings.TITLE_TEXT_COLOR);
		textEditText.setFilters(textFilters);
		textEditText.setHint(Language.getEditPastRemarksHintText());
		
		dateTextView = (TextView)findViewById(R.id.past_edit_date);
		dateTextView.setTextColor(GlobalSettings.TITLE_TEXT_COLOR);
		infoTitleTextView = (TextView)findViewById(R.id.info_title);
		infoTitleTextView.setTextColor(GlobalSettings.TITLE_BACKGROUND_COLOR);
		infoRemainTextView = (TextView)findViewById(R.id.info_remain);
		infoRemainTextView.setTextColor(GlobalSettings.TITLE_BACKGROUND_COLOR);
		infoDateTextView = (TextView)findViewById(R.id.info_date);
		infoDateTextView.setTextColor(GlobalSettings.TITLE_BACKGROUND_COLOR);
		infoContentTextView = (TextView)findViewById(R.id.info_content);
		infoContentTextView.setTextColor(GlobalSettings.TITLE_BACKGROUND_COLOR);
		
		editPastTextView0 = (TextView)findViewById(R.id.edit_past_text_view_0);
		editPastTextView0.setTextColor(GlobalSettings.TITLE_TEXT_COLOR);
		editPastTextView0.setText(Language.getEditPastTitleText());
		editPastTextView1 = (TextView)findViewById(R.id.edit_past_text_view_1);
		editPastTextView1.setTextColor(GlobalSettings.TITLE_TEXT_COLOR);
		editPastTextView1.setText(Language.getEditPastDateText());
		editPastTextView2 = (TextView)findViewById(R.id.edit_past_text_view_2);
		editPastTextView2.setTextColor(GlobalSettings.TITLE_TEXT_COLOR);
		editPastTextView2.setText(Language.getEditPastRepeatText());
		editPastTextView3 = (TextView)findViewById(R.id.edit_past_text_view_3);
		editPastTextView3.setTextColor(GlobalSettings.TITLE_TEXT_COLOR);
		editPastTextView3.setText(Language.getEditPastRemarksText());
		
		((TextView)findViewById(R.id.past_edit_top_title)).setText(Language.getEditPastTitleLayoutText(isOld));
		
		scrollView = (TopBottomScrollView)findViewById(R.id.tb_scrollview);
		
		check = (ImageView)findViewById(R.id.past_edit_check);
		back = (ImageView)findViewById(R.id.past_edit_back);
		editLogo = (ImageView)findViewById(R.id.edit_logo);
		
		titleLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				titleEditText.requestFocus();
			}
		});
		
		titleEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				infoTitleTextView.setText(titleEditText.getText().toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		dateLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setTime();
			}
		});
		
		spinner = (Spinner)findViewById(R.id.past_edit_spinner);
        
		Language.setRepeatDataText();
		
        arr_adapter= new SpinnerArrayAdapter(mContext, Language.repeatDataText, 20);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);
        
        isOld = getIntent().getBooleanExtra("isOld", false);
        
        if (isOld) {
        	dateString = getIntent().getStringExtra("RemindTime");
        }
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				repeatString = repeatType[position];
				Record tempRecord = new Record();
				tempRecord.setRemindTime(dateString);
				tempRecord.setType(repeatString);
				
				infoRemainTextView.setText(String.valueOf(TimeFleetingData.calculateRemainDays(tempRecord)));
				
				dateTextView.setText(dateTextView.getText().toString().substring(0,  10) + " " + Language.getDayOfWeekText(calDayOfWeek(dateString)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
        	
		});

        remarksLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textEditText.requestFocus();
			}
		});
        
        textEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				infoContentTextView.setText(textEditText.getText().toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
        
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				returnHome();
			}
		});
        
        check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				save();
			}
		});
        
        SimpleDateFormat formatter = 
				new SimpleDateFormat (GlobalSettings.FULL_DATE_FORMAT);     
		Date curDate = new Date(System.currentTimeMillis());
		createTimeString = formatter.format(curDate); 
		

		if (isOld) {
			infoLinearLayout.getLayoutParams().height = 
					ScreenUtil.getScreenHeight(EditPastActivity.this) 
					- getStatusBarHeight() 
					- topTitleLinearLayout.getHeight();
			infoTitleTextView.setText(getIntent().getStringExtra("Title"));
			infoDateTextView.setText(getIntent().getStringExtra("RemindTime").substring(0, 10));
			infoContentTextView.setText(getIntent().getStringExtra("Content"));
			
			Record tempRecord = new Record();
			tempRecord.setRemindTime(getIntent().getStringExtra("RemindTime"));
			tempRecord.setType(getIntent().getStringExtra("Type"));
			
			Log.d("TimeFleeting", "Ans: " + TimeFleetingData.calculateRemainDays(tempRecord));
			infoRemainTextView.setText("What??");
			//infoRemainTextView.setText("" + TimeFleetingData.calculateRemainDays(tempRecord));
			
			titleEditText.setText(getIntent().getStringExtra("Title"));
			textEditText.setText(getIntent().getStringExtra("Content"));
			dateTextView.setText(getIntent().getStringExtra("RemindTime").substring(0,  10) + " " + calDayOfWeek(getIntent().getStringExtra("RemindTime")));
			for (int i = 0; i < repeatType.length; i++) {
				if (repeatType[i].equals(getIntent().getStringExtra("Type"))) {
					spinner.setSelection(i);
					break;
				}
			}
			
			saveId = getIntent().getIntExtra("ID", -1);
			
			dateTextView.setText(dateTextView.getText().toString().substring(0,  10) + " " + Language.getDayOfWeekText(calDayOfWeek(getIntent().getStringExtra("RemindTime"))));
			
			((TextView)findViewById(R.id.past_edit_top_title)).setText(Language.getEditPastTitleLayoutText(isOld));

			YoYo.with(Techniques.Shake)
			.duration(2000)
			.delay(GlobalSettings.TIP_ANIMATION_DELAY)
			.playOn(infoRemainTextView);
			
			YoYo.with(Techniques.Shake)
			.duration(2000)
			.delay(GlobalSettings.TIP_ANIMATION_DELAY)
			.playOn(editLogo);
			
		} else {
			dateString = createTimeString;
			dateTextView.setText(dateString.substring(0,  10) + " " + Language.getDayOfWeekText(calDayOfWeek(dateString)));
			infoLinearLayout.getLayoutParams().height = 0;
			saveId = -1;
		}
		
		
		if (scrollView == null) {
			Log.d("TimeFleeting", "ISNULL");
		} else {
			scrollView.setOnScrollToBottomLintener(new OnScrollToBottomListener() {
				
				@Override
				public void onScrollBottomListener(boolean isBottom) {
					YoYo.with(Techniques.Shake)
					.duration(2000)
					.delay(GlobalSettings.TIP_ANIMATION_DELAY)
					.playOn(check);
				}
			});
			scrollView.setOnScrollToTopLintener(new OnScrollToTopListener() {
				
				@Override
				public void onScrollTopListener(boolean isTop) {
					YoYo.with(Techniques.Shake)
					.duration(2000)
					.delay(GlobalSettings.TIP_ANIMATION_DELAY)
					.playOn(infoRemainTextView);
					
					YoYo.with(Techniques.Shake)
					.duration(2000)
					.delay(GlobalSettings.TIP_ANIMATION_DELAY)
					.playOn(editLogo);
				}
			});
		}

		
	}
	
	private void setTime() {
		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		datePickerDialog.setYearRange(1915, 2036);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.setCancelable(true);
        datePickerDialog.show(getSupportFragmentManager(), GlobalSettings.DATEPICKER_TAG);
	}
	
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		month++;
		dateString = String.valueOf(year) + "-";
		dateString += (month < 10 ? "0" + String.valueOf(month) : String.valueOf(month)) + "-";
		dateString += (day < 10 ? "0" + String.valueOf(day) : String.valueOf(day));
		dateString += "-00:00:00";
		dateTextView.setText(dateString.substring(0, 10));
		infoDateTextView.setText(dateString.substring(0, 10));
		
		Record tempRecord = new Record();
		tempRecord.setRemindTime(dateString);
		tempRecord.setType(repeatString);
		
		infoRemainTextView.setText(String.valueOf(TimeFleetingData.calculateRemainDays(tempRecord)));
		
		dateTextView.setText(dateTextView.getText().toString().substring(0,  10) + " " + Language.getDayOfWeekText(calDayOfWeek(dateString)));
	}
	
	private void save() {
		if (titleEditText.getText().toString().length() == 0) {
			YoYo.with(Techniques.Shake)
			.duration(2000)
			.delay(GlobalSettings.TIP_ANIMATION_DELAY)
			.playOn(titleLinearLayout);
			Toast.makeText(mContext, Language.getToastGetTitle(), Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(mContext, Language.getToastSaveSuccessfullyText(), Toast.LENGTH_SHORT).show();
		TimeFleetingData.saveRecord(new Record(
				saveId,
				titleEditText.getText().toString(),
				textEditText.getText().toString(),
				dateString,
				createTimeString,
				"PAST",
				repeatString,
				status,
				beTop
				));
		Log.d("TimeFleeting", "Save: " + dateString);
		returnHome();
	}

	private void returnHome() {
		Intent intent = new Intent();
		intent.putExtra("isEditActivityFinished", true);
		setResult(RESULT_OK, intent);

		finish();
	}
	
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		if (result == 0) {
			Log.d("TimeFleeting", "is zero");
		}
		return result;
	}
	
	private int calDayOfWeek(String ds) {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date date = new Date();
		try {
			date = formatter.parse(ds);
		} catch (ParseException p) {
			p.printStackTrace();
		}
		return date.getDay();
	}
	
}
