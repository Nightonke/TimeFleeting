package com.timefleeting.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.SwipeListener;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class ListViewAdapter extends BaseSwipeAdapter {

	private final int ALL_TIME = 7 * 1000 * 60 * 60 * 24;
	
    private Context mContext;
    private List<Record> list;
    private int CurrentPosition;
    
    SwipeLayout swipeLayout;

    public ListViewAdapter(List<Record> list, Context mContext) {
    	this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
    	CurrentPosition = position;
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SwipeListener() {

			@Override
			public void onUpdate(SwipeLayout arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				Log.d("TimeFleeting", "onUpdate");
			}
			
			@Override
			public void onStartOpen(SwipeLayout arg0) {
				// TODO Auto-generated method stub
				Log.d("TimeFleeting", "onStartOpen");
			}
			
			@Override
			public void onStartClose(SwipeLayout arg0) {
				// TODO Auto-generated method stub
				Log.d("TimeFleeting", "onStartClose");
			}
			
			@Override
			public void onOpen(SwipeLayout arg0) {
				// TODO Auto-generated method stub
				Log.d("TimeFleeting", "onOpen");
				YoYo.with(Techniques.StandUp).duration(1000).delay(500).playOn(arg0.findViewById(R.id.set_time));
				YoYo.with(Techniques.StandUp).duration(1000).delay(500).playOn(arg0.findViewById(R.id.set_star));
				YoYo.with(Techniques.StandUp).duration(1000).delay(500).playOn(arg0.findViewById(R.id.delete));
			}
			
			@Override
			public void onHandRelease(SwipeLayout arg0, float arg1, float arg2) {
				// TODO Auto-generated method stub
				Log.d("TimeFleeting", "onHandRelease");
			}
			
			@Override
			public void onClose(SwipeLayout arg0) {
				// TODO Auto-generated method stub
				Log.d("TimeFleeting", "onClose");
			}
		});

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
    	TextView idTextView = (TextView)convertView.findViewById(R.id.listview_id);
    	idTextView.setText(list.get(position).getId() + "");
    	TextView titleTextView = (TextView)convertView.findViewById(R.id.listview_item_title);
    	titleTextView.setText(list.get(position).getTitle());
    	TextView remindTimeTextView = (TextView)convertView.findViewById(R.id.listview_item_remind_time);
    	remindTimeTextView.setText(list.get(position).getRemindTime().substring(5, 16));
    	TextView createTimeTextView = (TextView)convertView.findViewById(R.id.listview_item_create_time);
    	createTimeTextView.setText(list.get(position).getCreateTime().substring(5, 16));
    	String starString = list.get(position).getStar();
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
    	
    	SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		Date remindDate = new Date(System.currentTimeMillis());;
		try {
			remindDate = formatter.parse(list.get(position).getRemindTime());
		} catch (ParseException p) {
			p.printStackTrace();
		}
		long diff = remindDate.getTime() - curDate.getTime();
		if (diff < 0) {
			diff = 0;
		} else if (diff > ALL_TIME) {
			diff = ALL_TIME;
		}
		diff = ALL_TIME - diff;
		int progress = (int)(diff * 1.0 / ALL_TIME * 100) + 10;
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
}
