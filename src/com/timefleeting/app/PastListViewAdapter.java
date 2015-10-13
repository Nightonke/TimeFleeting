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

public class PastListViewAdapter extends BaseSwipeAdapter {
	
    private Context mContext;
    private List<Record> list;
    private MainActivity mActivity;
    
    private int width;
    
    SwipeLayout swipeLayout;

    private Record setTimeRecord;
    private String newRemindTimeString;
    
    public PastListViewAdapter(List<Record> list, Context mContext) {
    	this.list = list;
        this.mContext = mContext;
        this.mActivity = (MainActivity)mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_past;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.past_listview_item, null);
        width = parent.getWidth();
        swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
			
			@Override
			public void onOpen(SwipeLayout arg0) {
				// while open, set the animations of the buttons
				YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
				.duration(GlobalSettings.TIP_ANIMATION_DURATION)
				.delay(GlobalSettings.TIP_ANIMATION_DELAY)
				.playOn(arg0.findViewById(R.id.past_be_top));
				
				YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
				.duration(GlobalSettings.TIP_ANIMATION_DURATION)
				.delay(GlobalSettings.TIP_ANIMATION_DELAY)
				.playOn(arg0.findViewById(R.id.past_right_arrow));
				
				YoYo.with(GlobalSettings.TIP_ANIMATION_STYLE)
				.duration(GlobalSettings.TIP_ANIMATION_DURATION)
				.delay(GlobalSettings.TIP_ANIMATION_DELAY)
				.playOn(arg0.findViewById(R.id.past_delete));
			}
		});
        
        return v;
    }

    @Override
    public void fillValues(final int position, View convertView) {
    	
    	LinearLayout buttonLinearLayout = (LinearLayout)convertView.findViewById(R.id.button_layout);
    	buttonLinearLayout.getLayoutParams().width = (int)(width * 0.6);
    	
    	LinearLayout backColorLinearLayout = (LinearLayout)convertView.findViewById(R.id.past_back_color_ly);
    	backColorLinearLayout.setBackgroundColor(Util.darkerColor(GlobalSettings.ITEM_BACKGROUND_COLOR, 1));
    	
    	LinearLayout backLinearLayout = (LinearLayout)convertView.findViewById(R.id.past_back_ly);
    	backLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeAllItems();
			}
		});
    	
    	LinearLayout beTopColorLinearLayout = (LinearLayout)convertView.findViewById(R.id.past_be_top_color_ly);
    	beTopColorLinearLayout.setBackgroundColor(Util.darkerColor(GlobalSettings.ITEM_BACKGROUND_COLOR, 2));
    	
    	LinearLayout beTopLinearLayout = (LinearLayout)convertView.findViewById(R.id.past_be_top_ly);
    	beTopLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeAllItems();
				beTop(position);
			}
		});
    	
    	LinearLayout deleteColorLinearLayout = (LinearLayout)convertView.findViewById(R.id.past_delete_color_ly);
    	deleteColorLinearLayout.setBackgroundColor(Util.darkerColor(GlobalSettings.ITEM_BACKGROUND_COLOR, 3));
    	
    	LinearLayout deleteLinearLayout = (LinearLayout)convertView.findViewById(R.id.past_delete_ly);
        deleteLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int idToBeDeleted = TimeFleetingData.pastRecords.get(position).getId();
				whetherDelete(idToBeDeleted, position);
			}
		});
    	
    	TextView dateTextView = (TextView)convertView.findViewById(R.id.date);
    	TextView titleTextView = (TextView)convertView.findViewById(R.id.past_listview_item_title);
    	titleTextView.setTextColor(GlobalSettings.ITEM_TITLE_TEXT_COLOR);
    	titleTextView.setText(list.get(position).getTitle());
    	TextView remindTimeTextView = (TextView)convertView.findViewById(R.id.past_listview_item_remind_time);
    	remindTimeTextView.setTextColor(GlobalSettings.ITEM_CONTENT_TEXT_COLOR);
    	remindTimeTextView.setText(list.get(position).getRemindTime().substring(0, 10) + " " + Language.getDayOfWeekText(calDayOfWeek(list.get(position).getRemindTime())));

    	ImageView beTop = (ImageView)convertView.findViewById(R.id.past_be_top);  // the button
    	ImageView beTopLogo = (ImageView)convertView.findViewById(R.id.past_listview_item_betop);
    	if (list.get(position).getBeTop() != 0) {
    		beTop.setImageResource(R.drawable.not_be_top);
    		beTopLogo.setVisibility(View.VISIBLE);
    	} else {
    		beTop.setImageResource(R.drawable.be_top);
    		beTopLogo.setVisibility(View.GONE);
    	}
    	
    	WaveView waveView = (WaveView)convertView.findViewById(R.id.past_wave_view);
    	waveView.setBackgroundColor(GlobalSettings.ITEM_BACKGROUND_COLOR);
    	int remainDays = TimeFleetingData.calculateRemainDays(list.get(position));
    	dateTextView.setTextColor(GlobalSettings.ITEM_REMAIN_TIME_TEXT_COLOR);
    	dateTextView.setText(String.valueOf(String.valueOf(remainDays)));
		
    	int diff = 0;
		if (remainDays > GlobalSettings.A_YEAR) {
			diff = 0;
		} else if (remainDays <= GlobalSettings.A_YEAR) {
			diff = (int)GlobalSettings.A_YEAR - remainDays;
		}
    	
		// add a default height to make the wave be able to be seen
		int progress = (int)(diff * 1.0 / GlobalSettings.A_YEAR * 100) + GlobalSettings.DEFAULT_WAVE_HEIGHT;
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
		
		Record record = TimeFleetingData.pastRecords.get(position);
		if (record.getBeTop() != 0) {
			// is top
			record.setBeTop(0);
			TimeFleetingData.saveRecord(record);
			TimeFleetingData.sortPastRecordsByLastSort();
			notifyDataSetChanged();
		} else {
			// is not top
			record.setBeTop(TimeFleetingData.pastBeTopNumber + 1);
			TimeFleetingData.saveRecord(record);
			TimeFleetingData.pastBeTopNumber++;
			TimeFleetingData.sortPastRecordsByLastSort();
			notifyDataSetChanged();
		}
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
