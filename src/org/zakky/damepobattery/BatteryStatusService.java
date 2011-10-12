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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.content.BroadcastReceiver;

/**
 * バッテリー残量をモニターして、必要があればユーザーに対して通知を行うクラスです。
 */
public class BatteryStatusService extends Service {
    @SuppressWarnings("unused")
    private final BatteryStatusService self = this;

    /** Battery level from previous change, < 0 when invalid */
    private static int sPrevLevel = -1;

    @Override
    public IBinder onBind(Intent intent) {
        // Will not be bound
        return null;
    }

    private static BroadcastReceiver sBatteryChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent broadcast) {
            if (broadcast.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = broadcast.getIntExtra("level", -1);
                Log.i("mogu", "ACTION_BATTERY_CHANGED:" + Integer.valueOf(level) + " previous:"
                        + sPrevLevel);
                if (level < 0)
                    return;
                updateBatteryLevel(context, level);
                return;
            }
        }
    };

    private static void updateBatteryLevel(Context context, int level) {
        if (sPrevLevel >= 0) {
            if (level < sPrevLevel) {
                controlNotification(context);
            }
        }
        sPrevLevel = level;
    }

    /*
     * shows the notification when context is given, clears the notification
     * when context is null
     */
    private static void controlNotification(Context context) {
        final NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
        if (context != null) {
            final Notification n = buildNotification(context);
            nm.notify(R.string.app_name, n);
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(sBatteryChangedReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(sBatteryChangedReceiver);
    }

    /**
     * Settings アプリのステータス画面を表示するインテントを構築します。
     * 
     * @return インテント。
     */
    private static Intent buildIntentForStatusSettings() {
        final Intent i = new Intent("android.intent.action.MAIN");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setClassName("com.android.settings", "com.android.settings.deviceinfo.Status");
        return i;
    }

    /**
     * ノティフィケーションオブジェクトを構築して返します。
     * 
     * @param context コンテキスト。
     * @return ノティフィケーション。
     */
    private static Notification buildNotification(Context context) {
        final Notification n = new Notification(R.drawable.icon, "充電中のバッテリー残量低下を検出しました。",
                System.currentTimeMillis());

        final Intent i = buildIntentForStatusSettings();
        final PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

        n.setLatestEventInfo(context, context.getString(R.string.app_name), "もぐもぐされています", pi);
        return n;
    }
}
