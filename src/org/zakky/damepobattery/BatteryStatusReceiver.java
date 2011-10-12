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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 電源に関するブロードキャストインテントを受信して、必要があればモニターを開始終了するクラスです。
 */
public class BatteryStatusReceiver extends BroadcastReceiver {
    @SuppressWarnings("unused")
    private final BatteryStatusReceiver self = this;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Log.i("mogu", "ACTION_POWER_CONNECTED");
            if (Config.isEnabled(context)) {
                controlService(context, true);
            }
            return;
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Log.i("mogu", "ACTION_POWER_DISCONNECTED");
            controlService(context, false);
            return;
        } else {
            Log.i("mogu", "unexpected intent: " + intent.getAction());
        }
    }

    private void controlService(Context context, boolean starting) {
        Intent intent = new Intent();
        intent.setClass(context, BatteryStatusService.class);
        if (starting)
            context.startService(intent);
        else
            context.stopService(intent);
    }
}
