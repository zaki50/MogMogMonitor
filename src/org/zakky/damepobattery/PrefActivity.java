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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

/**
 * アプリの設定を表示/変更するためのアクティビティです。
 */
public class PrefActivity extends Activity {
    private final PrefActivity self = this;

    private CheckBox mEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window w = getWindow();
        w.requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.pref);
        w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);

        mEnabled = (CheckBox) findViewById(R.id.monitor_charging);
        mEnabled.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean enabled = mEnabled.isChecked();
                Config.setEnabled(self, enabled);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 設定をUIに反映させる
        final boolean autoStartEnabled = Config.isEnabled(this);
        mEnabled.setChecked(autoStartEnabled);
    }
}
