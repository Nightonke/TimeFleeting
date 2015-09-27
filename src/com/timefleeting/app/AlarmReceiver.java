package com.timefleeting.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String titleString = intent.getStringExtra("TITLE");
		Log.d("TimeFleeting", "Get " + titleString);
		Toast.makeText(context, "Get " + titleString, Toast.LENGTH_LONG).show();
	}

}
