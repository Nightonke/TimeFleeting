package com.timefleeting.app;

import java.util.List;

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
import android.widget.TextView;
import android.widget.Toast;



public class ListViewAdapter extends BaseSwipeAdapter {

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
        
        swipeLayout.setOnClickListener(new OnClickListener() {
			
        	boolean wasClosed = true;
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("TimeFleeting", "CCCCCCCCCCCCc");
				if (SwipeLayout.Status.Close == swipeLayout.getOpenStatus()) {
					if (wasClosed) {
						Toast.makeText(mContext, "Click " + CurrentPosition, Toast.LENGTH_SHORT).show();
					} else {
						wasClosed = true;
					}
				} else {
					wasClosed = false;
				}
			}
		});
        
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
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
    	remindTimeTextView.setText(list.get(position).getRemindTime());
    	TextView createTimeTextView = (TextView)convertView.findViewById(R.id.listview_item_create_time);
    	createTimeTextView.setText(list.get(position).getCreateTime());
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
