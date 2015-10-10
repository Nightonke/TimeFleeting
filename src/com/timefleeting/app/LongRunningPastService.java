package com.timefleeting.app;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import java.util.List;

public class LongRunningPastService extends Service {

	static public List<Remind> remindList;
	
	@Override
	public void onCreate() {
		remindList = new ArrayList<Remind>();
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		remindList = new ArrayList<Remind>();
		remindList = GlobalSettings.REMIND_PAST_LIST;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("TimeFleeting", "Run LongRunningPastService");
			}
		}).start();
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, AlarmReceiver.class);
		if (remindList == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		for (int j = 0; j < remindList.size(); j++) {
			i.putExtra("ID", remindList.get(j).id);
			i.putExtra("TITLE", remindList.get(j).title);
			i.putExtra("CONTENT", remindList.get(j).content);
			i.putExtra("REMINDTIME", remindList.get(j).remindTime);
			i.putExtra("Type", remindList.get(j).type);
			i.putExtra("TAG", "MEMORY");
			PendingIntent pi = PendingIntent.getBroadcast(this, remindList.get(j).id + GlobalSettings.REMIND_LIST.size(), i, 0);
			manager.set(AlarmManager.RTC_WAKEUP, remindList.get(j).triggerAtTime, pi);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {  
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, AlarmReceiver.class);
		for (int j = 0; j < remindList.size(); j++) {
			i.putExtra("ID", remindList.get(j).id);
			i.putExtra("TITLE", remindList.get(j).title);
			i.putExtra("CONTENT", remindList.get(j).content);
			i.putExtra("REMINDTIME", remindList.get(j).remindTime);
			i.putExtra("Type", remindList.get(j).type);
			PendingIntent pi = PendingIntent.getBroadcast(this, remindList.get(j).id + GlobalSettings.REMIND_LIST.size(), i, 0);
			// you have to use both the cancel function to cancel the pendingIntent
			pi.cancel();
			manager.cancel(pi);
		}
		super.onDestroy();   
	}  
	
}
