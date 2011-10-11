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

/**
 * アプリの設定を管理するクラスです。
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Config {
    private static final String PREF_NAME = "config";

    private static final String KEY_ENABLED = "enabled";

    private static Boolean mEnabled;

    public static synchronized boolean isEnabled(Context context) {
        if (mEnabled != null) {
            return mEnabled.booleanValue();
        }
        final SharedPreferences pref = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final boolean result = pref.getBoolean(KEY_ENABLED, false);
        mEnabled = Boolean.valueOf(result);
        return result;
    }

    public static synchronized void setEnabled(Context context, boolean enabled) {
        final SharedPreferences pref = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final Editor edit = pref.edit();
        try {
            edit.putBoolean(KEY_ENABLED, enabled);
        } finally {
            edit.commit();
            mEnabled = Boolean.valueOf(enabled);
        }
    }
}
