package com.timefleeting.app;

import java.io.IOException;
import java.util.List;

import com.timefleeting.app.JazzyViewPager;
import com.timefleeting.app.JazzyViewPager.TransitionEffect;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

	private JazzyViewPager mJazzy;
	private LayoutInflater layoutInflater;	
	
	private TextView resultTextView;
	private Button addButton;
	private Button deleteButton;
	private Button queryButton;
	private int titleTestId;
	
	private TimeFleetingData timeFleetingData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		titleTestId = 0;
		
		try {
			timeFleetingData = TimeFleetingData.getInstance(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
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

	private void setupJazziness(TransitionEffect effect) {

		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);

		mJazzy.setTransitionEffect(TransitionEffect.valueOf("CubeOut".toString()));
		mJazzy.setAdapter(new MainAdapter());
		mJazzy.setPageMargin(30);
	}

	private class MainAdapter extends PagerAdapter {
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = new View(MainActivity.this);
			View v;
			if (position == 0) {
				v = layoutInflater.inflate(R.layout.layout1, null);
			} else if (position == 1) {
				v = layoutInflater.inflate(R.layout.layout2, null);
				resultTextView = (TextView)v.findViewById(R.id.result_textview);
				addButton = (Button)v.findViewById(R.id.add_button);
				deleteButton = (Button)v.findViewById(R.id.delete_button);
				queryButton = (Button)v.findViewById(R.id.query_button);
				addButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String typeString = (titleTestId % 2 == 1 ? "PAST" : "FUTURE");
						timeFleetingData.saveRecord(new Record(-1, 
								"Title" + String.valueOf(titleTestId), "Text", 
								"2015-08-12", "2015-09-23", "5", typeString));
						titleTestId++;
						show();
					}
				});
				
				deleteButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
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
}
