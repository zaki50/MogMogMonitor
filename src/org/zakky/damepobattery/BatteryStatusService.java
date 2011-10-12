/*
 * Copyright 2011 YAMAZAKI Makoto<makoto1975@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
