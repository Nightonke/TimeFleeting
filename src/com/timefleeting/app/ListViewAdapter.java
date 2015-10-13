package com.timefleeting.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.SwipeListener;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings.Global;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ListViewAdapter extends BaseSwipeAdapter implements OnDateSetListener, OnTimeSetListener {
	
    private Context mContext;
    private List<Record> list;
    private MainActivity mActivity;
    
    SwipeLayout swipeLayout;

    private Record setTimeRecord;
    private String newRemindTimeString;
    
    public ListViewAdapter(List<Record> list, Context mContext) {
    	this.list = list;
        this.mContext = mContext;
        this.mActivity = (MainActivity)mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
			
			@Override
			public void onOpen(SwipeLayout arg0) {
				// while open, set the animations of the 3 buttons
				YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
				.duration(GlobalSettings.TIP_ANIMATION_DURATION)
				.delay(GlobalSettings.TIP_ANIMATION_DELAY)
				.playOn(arg0.findViewById(R.id.right_arrow));
				
				YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
				.duration(GlobalSettings.TIP_ANIMATION_DURATION)
				.delay(GlobalSettings.TIP_ANIMATION_DELAY)
				.playOn(arg0.findViewById(R.id.be_top));
				
				YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
				.duration(GlobalSettings.TIP_ANIMATION_DURATION)
				.delay(GlobalSettings.TIP_ANIMATION_DELAY)
				.playOn(arg0.findViewById(R.id.set_time));
				
				YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
				.duration(GlobalSettings.TIP_ANIMATION_DURATION)
				.delay(GlobalSettings.TIP_ANIMATION_DELAY)
				.playOn(arg0.findViewById(R.id.set_star));
				
				YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
				.duration(GlobalSettings.TIP_ANIMATION_DURATION)
				.delay(GlobalSettings.TIP_ANIMATION_DELAY)
				.playOn(arg0.findViewById(R.id.delete));
			}
		});
        
        return v;
    }

    @Override
    public void fillValues(final int position, View convertView) {
    	
    	LinearLayout backColorLinearLayout = (LinearLayout)convertView.findViewById(R.id.back_color_ly);
    	backColorLinearLayout.setBackgroundColor(Util.darkerColor(GlobalSettings.ITEM_BACKGROUND_COLOR, 1));
    	
    	LinearLayout backLinearLayout = (LinearLayout)convertView.findViewById(R.id.back_ly);
    	backLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeAllItems();
			}
		});
    	
    	LinearLayout beTopColorLinearLayout = (LinearLayout)convertView.findViewById(R.id.be_top_color_ly);
    	beTopColorLinearLayout.setBackgroundColor(Util.darkerColor(GlobalSettings.ITEM_BACKGROUND_COLOR, 2));
    	
    	LinearLayout beTopLinearLayout = (LinearLayout)convertView.findViewById(R.id.be_top_ly);
    	beTopLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeAllItems();
				beTop(position);
			}
		});
    	
    	LinearLayout setRemindTimeColorLinearLayout = (LinearLayout)convertView.findViewById(R.id.set_remind_time_color_ly);
    	setRemindTimeColorLinearLayout.setBackgroundColor(Util.darkerColor(GlobalSettings.ITEM_BACKGROUND_COLOR, 3));
    	
    	LinearLayout setTimeLinearLayout = (LinearLayout)convertView.findViewById(R.id.set_time_ly);
    	setTimeLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setRemindTime(position);
			}
		});
    	
    	LinearLayout setStarColorLinearLayout = (LinearLayout)convertView.findViewById(R.id.set_star_color_ly);
    	setStarColorLinearLayout.setBackgroundColor(Util.darkerColor(GlobalSettings.ITEM_BACKGROUND_COLOR, 4));
    	
    	LinearLayout setStarLinearLayout = (LinearLayout)convertView.findViewById(R.id.set_star_ly);
        setStarLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setStar(position);
			}
		});
        
        LinearLayout deleteColorLinearLayout = (LinearLayout)convertView.findViewById(R.id.delete_color_ly);
        deleteColorLinearLayout.setBackgroundColor(Util.darkerColor(GlobalSettings.ITEM_BACKGROUND_COLOR, 5));
        
        LinearLayout deleteLinearLayout = (LinearLayout)convertView.findViewById(R.id.delete_ly);
        deleteLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int idToBeDeleted = TimeFleetingData.futureRecords.get(position).getId();
				whetherDelete(idToBeDeleted, position);
			}
		});
    	
    	TextView titleTextView = (TextView)convertView.findViewById(R.id.listview_item_title);
    	titleTextView.setTextColor(GlobalSettings.ITEM_TITLE_TEXT_COLOR);
    	titleTextView.setText(list.get(position).getTitle());
    	TextView contentTextView = (TextView)convertView.findViewById(R.id.listview_item_content);
    	contentTextView.setTextColor(GlobalSettings.ITEM_CONTENT_TEXT_COLOR);
    	contentTextView.setText(list.get(position).getText());
    	TextView remindTimeTextView = (TextView)convertView.findViewById(R.id.listview_item_remind_time);
    	remindTimeTextView.setTextColor(GlobalSettings.ITEM_CONTENT_TEXT_COLOR);
    	remindTimeTextView.setText(list.get(position).getRemindTime().substring(5, 16));
    	TextView createTimeTextView = (TextView)convertView.findViewById(R.id.listview_item_create_time);
    	createTimeTextView.setTextColor(GlobalSettings.ITEM_CONTENT_TEXT_COLOR);
    	createTimeTextView.setText(list.get(position).getCreateTime().substring(5, 16));
    	String starString = list.get(position).getStar();
    	ImageView beTop = (ImageView)convertView.findViewById(R.id.be_top);
    	ImageView beTopLogo = (ImageView)convertView.findViewById(R.id.listview_item_betop);
    	if (list.get(position).getBeTop() != 0) {
    		beTop.setImageResource(R.drawable.not_be_top);
    		beTopLogo.setVisibility(View.VISIBLE);
    	} else {
    		beTop.setImageResource(R.drawable.be_top);
    		beTopLogo.setVisibility(View.GONE);
    	}
    	ImageView overdue = (ImageView)convertView.findViewById(R.id.listview_item_overdue);
    	overdue.setVisibility(View.GONE);
    	ImageView star1 = (ImageView)convertView.findViewById(R.id.listview_item_star_1);
    	ImageView star2 = (ImageView)convertView.findViewById(R.id.listview_item_star_2);
    	ImageView star3 = (ImageView)convertView.findViewById(R.id.listview_item_star_3);
    	ImageView star4 = (ImageView)convertView.findViewById(R.id.listview_item_star_4);
    	ImageView star5 = (ImageView)convertView.findViewById(R.id.listview_item_star_5);
    	star1.setImageResource(R.drawable.set_star);
		star2.setImageResource(R.drawable.set_star);
		star3.setImageResource(R.drawable.set_star);
		star4.setImageResource(R.drawable.set_star);
		star5.setImageResource(R.drawable.set_star);

    	if ("0".equals(starString)) {
    		star1.setVisibility(View.INVISIBLE);
    		star2.setVisibility(View.INVISIBLE);
    		star3.setVisibility(View.INVISIBLE);
    		star4.setVisibility(View.INVISIBLE);
    		star5.setVisibility(View.INVISIBLE);
    	} else if ("1".equals(starString)) {
    		star1.setVisibility(View.INVISIBLE);
    		star2.setVisibility(View.INVISIBLE);
    		star3.setVisibility(View.INVISIBLE);
    		star4.setVisibility(View.INVISIBLE);
    		star5.setVisibility(View.VISIBLE);
    	} else if ("2".equals(starString)) {
    		star1.setVisibility(View.INVISIBLE);
    		star2.setVisibility(View.INVISIBLE);
    		star3.setVisibility(View.INVISIBLE);
    		star4.setVisibility(View.VISIBLE);
    		star5.setVisibility(View.VISIBLE);
    	} else if ("3".equals(starString)) {
    		star1.setVisibility(View.INVISIBLE);
    		star2.setVisibility(View.INVISIBLE);
    		star3.setVisibility(View.VISIBLE);
    		star4.setVisibility(View.VISIBLE);
    		star5.setVisibility(View.VISIBLE);
    	} else if ("4".equals(starString)) {
    		star1.setVisibility(View.INVISIBLE);
    		star2.setVisibility(View.VISIBLE);
    		star3.setVisibility(View.VISIBLE);
    		star4.setVisibility(View.VISIBLE);
    		star5.setVisibility(View.VISIBLE);
    	} else if ("5".equals(starString)) {
    		star1.setVisibility(View.VISIBLE);
    		star2.setVisibility(View.VISIBLE);
    		star3.setVisibility(View.VISIBLE);
    		star4.setVisibility(View.VISIBLE);
    		star5.setVisibility(View.VISIBLE);
    	}
    	
    	WaveView waveView = (WaveView)convertView.findViewById(R.id.wave_view);
    	waveView.setBackgroundColor(GlobalSettings.ITEM_BACKGROUND_COLOR);
    	
    	SimpleDateFormat formatter = new SimpleDateFormat (GlobalSettings.FULL_DATE_FORMAT);     
		Date curDate = new Date(System.currentTimeMillis());
		Date remindDate = new Date(System.currentTimeMillis());;
		
		try {
			remindDate = formatter.parse(list.get(position).getRemindTime());
		} catch (ParseException p) {
			p.printStackTrace();
		}
		
		long diff = remindDate.getTime() - curDate.getTime();
		if (diff <= 0) {
			// this record is overdue
			overdue.setVisibility(View.VISIBLE);
			diff = 0;
		} else if (diff > GlobalSettings.REMIND_TIME) {
			// the record still gets a long time to be overdue
			diff = GlobalSettings.REMIND_TIME;
		}
		diff = GlobalSettings.REMIND_TIME - diff;
		// add a default height to make the wave be able to be seen
		int progress = (int)(diff * 1.0 / GlobalSettings.REMIND_TIME * 100) + GlobalSettings.DEFAULT_WAVE_HEIGHT;
		waveView.setProgress(progress);
    }

    @Override
    public int getCount() {
    	if (list == null) {
			return 0;
		} else {
			return list.size();
		}
    }

    @Override
    public Object getItem(int position) {
    	if (list == null) {
			return null;
		} else {
			return list.get(position);
		}
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
	private void beTop(int position) {
		
		Record record = TimeFleetingData.futureRecords.get(position);
		Log.d("TimeFleeting", "Click Position is " + position);
		Log.d("TimeFleeting", record.getTitle());
		if (record.getBeTop() != 0) {
			// is top
			record.setBeTop(0);
			TimeFleetingData.saveRecord(record);
			TimeFleetingData.sortFutureRecordsByLastSort();
			notifyDataSetChanged();
		} else {
			// is not top
			record.setBeTop(TimeFleetingData.futureBeTopNumber + 1);
			TimeFleetingData.saveRecord(record);
			TimeFleetingData.futureBeTopNumber++;
			TimeFleetingData.sortFutureRecordsByLastSort();
			notifyDataSetChanged();
		}
	}
    
	public void setRemindTime(int position) {
		
		setTimeRecord = TimeFleetingData.futureRecords.get(position);
		
		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		datePickerDialog.setYearRange(2015, 2036);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.setCancelable(false);
        datePickerDialog.show(mActivity.getSupportFragmentManager(), GlobalSettings.DATEPICKER_TAG);
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		newRemindTimeString += (hourOfDay < 10 ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay)) + ":";
		newRemindTimeString += (minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute)) + ":";
		newRemindTimeString += "00";

		if (newRemindTimeString.length() != GlobalSettings.FULL_DATE_FORMAT.length()) {
			return;
		}
		
		setTimeRecord.setRemindTime(newRemindTimeString);
		TimeFleetingData.saveRecord(setTimeRecord);
		notifyDataSetChanged();
		
		if (GlobalSettings.REMIND_ENABLE) {
			MainActivity.intentService = new Intent(mContext, LongRunningService.class);
			MainActivity.intentService.setAction("TimeFleeting Reminder");
	        MainActivity.initReminds();
			LongRunningService.remindList = GlobalSettings.REMIND_LIST;
			mContext.stopService(MainActivity.intentService);
			mContext.startService(MainActivity.intentService);
		}
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub

		// a bug in the datetimepicker-library
		// which will cause the month is month - 1
		month++;
		
		newRemindTimeString = String.valueOf(year) + "-";
		newRemindTimeString += (month < 10 ? "0" + String.valueOf(month) : String.valueOf(month)) + "-";
		newRemindTimeString += (day < 10 ? "0" + String.valueOf(day) : String.valueOf(day)) + "-";
		
		final Calendar calendar = Calendar.getInstance();
		
	    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
		
		timePickerDialog.setVibrate(false);
        timePickerDialog.setCloseOnSingleTapMinute(false);
        timePickerDialog.setCancelable(false);
        timePickerDialog.show(mActivity.getSupportFragmentManager(), GlobalSettings.TIMEPICKER_TAG);
	}
	
    private void setStar(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View view = layoutInflater.inflate(R.layout.set_star, null);
		builder.setView(view);
		final AlertDialog dialog = builder.show();
		dialog.setCanceledOnTouchOutside(true);
		
		LinearLayout setStarLinearLayout = (LinearLayout)view.findViewById(R.id.set_star_lyy);
		setStarLinearLayout.setBackgroundColor(GlobalSettings.TITLE_BACKGROUND_COLOR);
		
		final TextView okTextView = (TextView)view.findViewById(R.id.set_star_ok);
		okTextView.setText(Language.getOKText());

		final ImageView star1 = (ImageView)view.findViewById(R.id.star_1);
		final ImageView star2 = (ImageView)view.findViewById(R.id.star_2);
		final ImageView star3 = (ImageView)view.findViewById(R.id.star_3);
		final ImageView star4 = (ImageView)view.findViewById(R.id.star_4);
		final ImageView star5 = (ImageView)view.findViewById(R.id.star_5);
		star4.setVisibility(View.INVISIBLE);
		star5.setVisibility(View.INVISIBLE);
		final SeekBar seekBar = (SeekBar)view.findViewById(R.id.star_seekbar);
		seekBar.setBackgroundColor(GlobalSettings.TITLE_BACKGROUND_COLOR);
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
				YoYo.with(Techniques.Tada).duration(1000).playOn(star1);
				YoYo.with(Techniques.Tada).duration(1000).playOn(star2);
				YoYo.with(Techniques.Tada).duration(1000).playOn(star3);
				YoYo.with(Techniques.Tada).duration(1000).playOn(star4);
				YoYo.with(Techniques.Tada).duration(1000).playOn(star5);
				YoYo.with(Techniques.Tada).duration(1000).playOn(okTextView);
			}
		});
		
		okTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Record record = TimeFleetingData.futureRecords.get(position);
				record.setStar(String.valueOf(seekBar.getProgress() + 1));
				TimeFleetingData.saveRecord(record);
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
	}
    
    private void whetherDelete(final int id, final int position) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View view = layoutInflater.inflate(R.layout.whether_delete, null);
		builder.setView(view);
		final AlertDialog dialog = builder.show();
		dialog.setCanceledOnTouchOutside(true);
		
		LinearLayout whetherDeleteLinearLayout = (LinearLayout)view.findViewById(R.id.whether_delete_lyy);
		whetherDeleteLinearLayout.setBackgroundColor(GlobalSettings.TITLE_BACKGROUND_COLOR);
		
		TextView textView = (TextView)view.findViewById(R.id.delete_text);
		textView.setText(Language.getDeleteText());
		
		LinearLayout whetherSaveLinearLayout = (LinearLayout)view.findViewById(R.id.whether_delete_logo);
		YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
		.duration(GlobalSettings.TIP_ANIMATION_DURATION)
		.delay(GlobalSettings.TIP_ANIMATION_DELAY)
		.playOn(whetherSaveLinearLayout);
		
		TextView cancelTextView = (TextView)view.findViewById(R.id.whether_delete_cancel);
		cancelTextView.setText(Language.getDeleteNoText());
		TextView yesTextView = (TextView)view.findViewById(R.id.whether_delete_yes);
		yesTextView.setText(Language.getDeleteYesText());
		
		YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
		.duration(GlobalSettings.TIP_ANIMATION_DURATION)
		.delay(GlobalSettings.TIP_ANIMATION_DELAY)
		.playOn(cancelTextView);
		YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
		.duration(GlobalSettings.TIP_ANIMATION_DURATION)
		.delay(GlobalSettings.TIP_ANIMATION_DELAY)
		.playOn(yesTextView);
		
		cancelTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				return;
			}
		});
		
		yesTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int returnId = TimeFleetingData.deleteRecord(id);
				if (returnId != -1) {
					// delete successfully
					Log.d("TimeFleeting", "Delete successfully");
				} else {
					// delete failed
					Log.d("TimeFleeting", "Delete failed");
				}
				closeItem(position);
				notifyDataSetChanged();
				dialog.dismiss();
				Toast.makeText(mContext, Language.getToastDeleteText(), Toast.LENGTH_SHORT).show();
				
			}
		});
	}
    
}
