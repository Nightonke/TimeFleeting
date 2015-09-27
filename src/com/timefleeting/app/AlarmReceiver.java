package com.timefleeting.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int id = intent.getIntExtra("ID", -1);
		String titleString = intent.getStringExtra("TITLE");
		String contentString = intent.getStringExtra("CONTENT");
		
		Log.d("TimeFleeting", contentString);
		
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);  
		
		mBuilder.build().defaults = Notification.DEFAULT_ALL;
		if (GlobalSettings.VIBRATE_ENABLE) {
			mBuilder.setVibrate(new long[] {0, 300, 100, 300});
		} else {
		
		}
		mBuilder.setLights(0xff0000ff, 300, 0); 
		if (GlobalSettings.SOUND_ENABLE) {
			mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		} else {
			
		}
		mBuilder.setPriority(Notification.PRIORITY_HIGH);
		
		mBuilder.setContentTitle(titleString);
		mBuilder.setContentText(contentString);
		mBuilder.setWhen(System.currentTimeMillis());
		mBuilder.setTicker(titleString);
		mBuilder.setSmallIcon(R.drawable.timefleeting_logo_2);
		
		mNotificationManager.notify(id, mBuilder.build());
		
		Log.d("TimeFleeting", "Get " + titleString);
//		Toast.makeText(context, "Get " + titleString, Toast.LENGTH_LONG).show();
	}

}
