package org.zakky.damepobattery;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.content.BroadcastReceiver;

public class BatteryStatusService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// Will not be bound
		return null;
	}

	private BroadcastReceiver mBatteryStatusReceiver = null;

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("BatteryStatusService", "onStart()");
		if (mBatteryStatusReceiver == null)
			mBatteryStatusReceiver = new BatteryStatusReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
		intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
		registerReceiver(mBatteryStatusReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		Log.i("BatteryStatusService", "onDestroy()");
		if (mBatteryStatusReceiver != null) {
			unregisterReceiver(mBatteryStatusReceiver);
			mBatteryStatusReceiver = null;
		}
	}
}
