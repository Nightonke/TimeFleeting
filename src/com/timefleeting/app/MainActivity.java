package com.timefleeting.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.timefleeting.app.JazzyViewPager;
import com.timefleeting.app.JazzyViewPager.TransitionEffect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.capricorn.RayMenu;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class MainActivity extends FragmentActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	public static final String DATEPICKER_TAG = "选择日期";
    public static final String TIMEPICKER_TAG = "选择时间";
	
	private JazzyViewPager mJazzy;
	private LayoutInflater layoutInflater;	
	
	private TextView resultTextView;
	private Button addButton;
	private Button deleteButton;
	private Button queryButton;
	private int titleTestId;
	
	private TimeFleetingData timeFleetingData;
	
	private Context mContext;
	
	private ListView listView;
	private ListViewAdapter mAdapter;
	
	private RayMenu rayMenu;
	
	private boolean isSortedByCreateTimeReversely = false;
	private boolean isSortedByRemindTime = false;
	private boolean isSortedByTitle = false;
	private boolean isSortedByStar = false;
	
	// record whether the listview is scrolling
	private boolean scrollFlag = false;
	private int lastVisibleItemPosition = 0;
	private boolean lastIsScrollDown = false;
	
	private View v;
	
	private int statusBarHeight;
	
	private static final int[] ITEM_DRAWABLES_FUTURE = {
		R.drawable.create,
		R.drawable.sort_by_title,
		R.drawable.sort_by_remind_time,
		R.drawable.sort_by_create_time,
		R.drawable.sort_by_star,
		R.drawable.search,
		R.drawable.settings};
	
	ImageView setTimeImageView;
	ImageView setStarImageView;
	ImageView deleteImageView;
	
	private int clickPosition;
	
	private String setStarString;
	private Record setStarRecord;
	
	private String newRemindTimeString;
	private Record setTimeRecord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mContext = this;
		titleTestId = 0;
		statusBarHeight = getStatusBarHeight();
		
		try {
			timeFleetingData = TimeFleetingData.getInstance(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		mAdapter = new ListViewAdapter(timeFleetingData.futureRecords, mContext);
		mAdapter.setMode(Attributes.Mode.Single);
		
		layoutInflater = getLayoutInflater().from(this);
		setupJazziness(TransitionEffect.Tablet);
	}

	private void show() {
		 String resultString = "PAST: \n";
		 for (Record c : timeFleetingData.pastRecords) {
			 resultString += c.toString() + "\n";
		 }
		 resultString += "FUTURE: \n";
		 for (Record c : timeFleetingData.futureRecords) {
			 resultString += c.toString() + "\n";
		 }
		 resultTextView.setText(resultString);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Toggle Fade");
		String[] effects = this.getResources().getStringArray(R.array.jazzy_effects);
		for (String effect : effects)
			menu.add(effect);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().equals("Toggle Fade")) {
			mJazzy.setFadeEnabled(!mJazzy.getFadeEnabled());
		} else {
			TransitionEffect effect = TransitionEffect.valueOf(item.getTitle().toString());
			setupJazziness(effect);
		}
		return true;
	}
	
	
	// listener to listen whether the EditActivity is finished
	// if finished, notifyDataSetChanged
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				boolean isEditActivityFinished = data.getBooleanExtra("isEditActivityFinished", false);
				if (isEditActivityFinished) {
					timeFleetingData.sortFutureRecordByCreateTimeReversely();
					isSortedByCreateTimeReversely = true;
					mAdapter.notifyDataSetChanged();
					if (data.getBooleanExtra("isOld", false)) {
						// don't scroll
					} else {
						listView.smoothScrollToPosition(0);
					}
				} else {
					Log.d("TimeFleeting", "isEditActivityFinished is false");
				}
			}
			break;

		default:
			break;
		}
	}

	private void setupJazziness(TransitionEffect effect) {

		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);

		mJazzy.setTransitionEffect(TransitionEffect.valueOf("CubeOut".toString()));
		mJazzy.setAdapter(new MainAdapter());
		mJazzy.setPageMargin(0);
	}

	private class MainAdapter extends PagerAdapter {
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = new View(MainActivity.this);
			
			if (position == 0) {
				v = layoutInflater.inflate(R.layout.layout1, null);
				listView = (ListView)v.findViewById(R.id.listview);
				listView.setAdapter(mAdapter);

				// sort by the create time default
				isSortedByCreateTimeReversely = true;
				timeFleetingData.sortFutureRecordByCreateTimeReversely();
				
				// on list item click listener
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		            	if (mAdapter.isOpen(position)) {
		            		clickPosition = position;
		                    SwipeLayout swipeLayout = (SwipeLayout)view.findViewById(mAdapter.getSwipeLayoutResourceId(position));
		                    setTimeImageView = (ImageView)swipeLayout.findViewById(R.id.set_time);
		                    setTimeImageView.setClickable(false);
		                    LinearLayout setTimeLinearLayout = (LinearLayout)swipeLayout.findViewById(R.id.set_time_ly);
		                    setTimeLinearLayout.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									YoYo.with(Techniques.Shake).duration(1000).delay(500).playOn(setTimeImageView);
									setRemindTime(clickPosition);
								}
							});
		                    setStarImageView = (ImageView)swipeLayout.findViewById(R.id.set_star);
		                    setStarImageView.setClickable(false);
		                    LinearLayout setStarLinearLayout = (LinearLayout)swipeLayout.findViewById(R.id.set_star_ly);
		                    setStarLinearLayout.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									YoYo.with(Techniques.Shake).duration(1000).delay(500).playOn(setStarImageView);
									setStar(clickPosition);
								}
							});
		                    deleteImageView = (ImageView)swipeLayout.findViewById(R.id.delete);
		                    deleteImageView.setClickable(false);
		                    LinearLayout deleteLinearLayout = (LinearLayout)swipeLayout.findViewById(R.id.delete_ly);
		                    deleteLinearLayout.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									YoYo.with(Techniques.Shake).duration(1000).delay(500).playOn(deleteImageView);
									int idToBeDeleted = timeFleetingData.futureRecords.get(clickPosition).getId();
									whetherDelete(idToBeDeleted, clickPosition);
								}
							});
		                    return;
		            	} else {
		            		Intent intent = new Intent(mContext, EditActivity.class);
			            	intent.putExtra("isOld", true);
			            	intent.putExtra("Title", timeFleetingData.futureRecords.get(position).getTitle());
			            	intent.putExtra("Content", timeFleetingData.futureRecords.get(position).getText());
			            	intent.putExtra("CreateTime", timeFleetingData.futureRecords.get(position).getCreateTime());
			            	intent.putExtra("RemindTime", timeFleetingData.futureRecords.get(position).getRemindTime());
			            	intent.putExtra("Star", timeFleetingData.futureRecords.get(position).getStar());
			            	intent.putExtra("ID", timeFleetingData.futureRecords.get(position).getId());
							startActivityForResult(intent, 1);
		            	}
		            }
		        });			

				rayMenu = (RayMenu)v.findViewById(R.id.past_layout_ray_menu);
				for (int i = 0; i < ITEM_DRAWABLES_FUTURE.length; i++) {
					ImageView imageView = new ImageView(mContext);
					imageView.setImageResource(ITEM_DRAWABLES_FUTURE[i]);
					final int menuPosition = i;
					rayMenu.addItem(imageView, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, "Click " + menuPosition, Toast.LENGTH_SHORT).show();
							if (menuPosition == 0) {
								Intent intent = new Intent(mContext, EditActivity.class);
								intent.putExtra("isOld", false);
								startActivityForResult(intent, 1);
							} else if (menuPosition == 1) {
								if (isSortedByTitle) {
									timeFleetingData.sortFutureRecordByTitleReversely();
									isSortedByTitle = false;
								} else {
									timeFleetingData.sortFutureRecordByTitle();
									isSortedByTitle = true;
								}
								mAdapter.notifyDataSetChanged();
							} else if (menuPosition == 2) {
								if (isSortedByCreateTimeReversely) {
									timeFleetingData.sortFutureRecordByCreateTime();
									isSortedByCreateTimeReversely = false;
								} else {
									timeFleetingData.sortFutureRecordByCreateTimeReversely();
									isSortedByCreateTimeReversely = true;
								}
								mAdapter.notifyDataSetChanged();
							} else if (menuPosition == 3) {
								if (isSortedByRemindTime) {
									timeFleetingData.sortFutureRecordByRemindTimeReversely();
									isSortedByRemindTime = false;
								} else {
									timeFleetingData.sortFutureRecordByRemindTime();
									isSortedByRemindTime = true;
								}
								mAdapter.notifyDataSetChanged();
							}
						}
					});
				}
				
				listView.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						// TODO Auto-generated method stub
						switch (scrollState) {
						case OnScrollListener.SCROLL_STATE_IDLE:
							scrollFlag = false;
							if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
//								rayMenu.setVisibility(View.GONE);
							}
							if (listView.getFirstVisiblePosition() == 0) {
//								rayMenu.setVisibility(View.VISIBLE);
							}
							break;
						case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
							Log.d("DEBUG", "scrolling");
							scrollFlag = true;
							break;
						case OnScrollListener.SCROLL_STATE_FLING:
							scrollFlag = false;
							break;
						default:
							break;
						}
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						rayMenu.closeMenu();
						if (scrollFlag && 
								ScreenUtil.getScreenViewBottomHeight(listView) 
								>= ScreenUtil.getScreenHeight(MainActivity.this) - statusBarHeight) {
							if (firstVisibleItem > lastVisibleItemPosition) {
								// scroll down
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
	;								rayMenu.startAnimation(animationSet);
								} else {
									
								}
								lastIsScrollDown = true;
							} else if (firstVisibleItem < lastVisibleItemPosition) {
								// scroll up
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
	;								rayMenu.startAnimation(animationSet);
								} else {
									
								}
								lastIsScrollDown = false;
							} else {
								return;
							}
							lastVisibleItemPosition = firstVisibleItem;
						}
					}
				});
				
			} else if (position == 1) {
				v = layoutInflater.inflate(R.layout.layout2, null);
				
			} else if (position == 2) {
				v = layoutInflater.inflate(R.layout.layout3, null);
			} else {
				v = layoutInflater.inflate(R.layout.layout4, null);
			}
			
			mJazzy.setObjectForPosition(v, position);
			container.addView(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
			return v;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			container.removeView(mJazzy.findViewFromObject(position));
		}
		
		@Override
		public int getCount() {
			return 4;
		}
		
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}		
	}
	
	public int getStatusBarHeight() {
	  int result = 0;
	  int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	  if (resourceId > 0) {
	      result = getResources().getDimensionPixelSize(resourceId);
	  }
	  return result;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		return;
	}
	
	private void setRemindTime(int position) {
		
		setTimeRecord = timeFleetingData.futureRecords.get(position);
		
		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		datePickerDialog.setYearRange(2015, 2036);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		newRemindTimeString += (hourOfDay < 10 ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay)) + ":";
		newRemindTimeString += (minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute)) + ":";
		newRemindTimeString += "00";

		setTimeRecord.setRemindTime(newRemindTimeString);
		timeFleetingData.saveRecord(setTimeRecord);
		mAdapter.notifyDataSetChanged();
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
        timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
	}
	
	private void setStar(int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.save_without_circle);
		builder.setTitle("优先级");
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View view = layoutInflater.inflate(R.layout.set_star, null);
		builder.setView(view);
		setStarString = "0";
		setStarRecord= timeFleetingData.futureRecords.get(position);
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
				setStarString = "1";
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
				setStarString = "2";
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
				setStarString = "3";
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
				setStarString = "4";
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
				setStarString = "5";
			}
		});
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setStarRecord.setStar(setStarString);
				timeFleetingData.saveRecord(setStarRecord);
				mAdapter.notifyDataSetChanged();
			}
		});
		
		builder.show();
	}
	
	private void whetherDelete(final int id, final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		String messageString = "Are u sure to delete this record?\n";
		messageString += timeFleetingData.futureRecords.get(position).getTitle() + "\n";
		messageString += "Create time: " + timeFleetingData.futureRecords.get(position).getCreateTime().substring(5, 16);
		builder.setMessage(messageString);
		builder.setPositiveButton("Yeah~", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int returnId = timeFleetingData.deleteRecord(id);
				if (returnId != -1) {
					// delete successfully
					Log.d("TimeFleeting", "Delete successfully");
				} else {
					// delete failed
					Log.d("TimeFleeting", "Delete failed");
				}
				mAdapter.closeItem(position);
				mAdapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton("Oh no", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		builder.show();
	}
}
