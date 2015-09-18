package com.timefleeting.app;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NListViewAdatper extends BaseAdapter {

	private List<Record> list = null;
	private Context context = null;
	
	public NListViewAdatper(List<Record> list, Context context) {
		this.list = list;
		this.context = context;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (list == null) {
			return null;
		} else {
			return list.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(this.context);
		View view = layoutInflater.inflate(R.layout.list_item, null);
		TextView idTextView = (TextView)view.findViewById(R.id.list_item_id);
		idTextView.setText(list.get(position).getId() + "");
		TextView titleTextView = (TextView)view.findViewById(R.id.list_item_title);
		titleTextView.setText(list.get(position).getTitle());
		TextView remindTimeTextView = (TextView)view.findViewById(R.id.list_item_remind_time);
		remindTimeTextView.setText(list.get(position).getRemindTime());
		TextView createTimeTextView = (TextView)view.findViewById(R.id.list_item_create_time);
		createTimeTextView.setText(list.get(position).getCreateTime());
		TextView starTextView = (TextView)view.findViewById(R.id.list_item_star);
		starTextView.setText(list.get(position).getStar());
		return view;
		
	}

	
	
}
