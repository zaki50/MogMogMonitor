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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * バッテリーに関するブロードキャストインテントを受信して、必要があればユーザーに対して
 * 通知を行うクラスです。
 */
public class BatteryStatusReceiver extends BroadcastReceiver {
    @SuppressWarnings("unused")
    private final BatteryStatusReceiver self = this;

    private static Integer sPrevLevel;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        final boolean enabled = Config.isEnabled(context);
        if (!enabled) {
            sPrevLevel = null;
            // TODO on-going notification の非表示
            return;
        }

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            return;
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            sPrevLevel = null;
            return;
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
            final int level = intent.getIntExtra("level", -1);
            if (level < 0) {
                return;
            }
            if (sPrevLevel != null && level < sPrevLevel.intValue()) {
                // とりあえずテスト用に増えた時にログを出す
                Log.i("mogu", "incresed to " + level);

                final NotificationManager nm = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancelAll();
                final Notification n = buildNotification(context);
                nm.notify(R.string.app_name, n);
            }
            sPrevLevel = Integer.valueOf(level);
            return;
        } else {
            Log.i("mogu", "unexpected intent: " + intent.getAction());
        }
    }

    /**
     * Settings アプリのステータス画面を表示するインテントを構築します。
     * @return インテント。
     */
    private Intent buildIntentForStatusSettings() {
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
    private Notification buildNotification(Context context) {
        final Notification n = new Notification(R.drawable.icon, "充電中のバッテリー残量低下を検出しました。",
                System.currentTimeMillis());

        final Intent i = buildIntentForStatusSettings();
        final PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

        n.setLatestEventInfo(context, context.getString(R.string.app_name), "もぐもぐされています", pi);
        return n;
    }

}
