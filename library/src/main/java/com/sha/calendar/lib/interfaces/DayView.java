/*
 * Copyright 2018 Shahul Hameed Shaik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sha.calendar.lib.interfaces;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
public final class DayView implements IDayView {
    private View view;
    private TextView dayValueTextView;

    public DayView() {
    }

    public View getView() {
        return view;
    }

    @Override
    public void setDayView(View view) {
        this.view = view;
    }

    public TextView getDayValueTextView() {
        return dayValueTextView;
    }

    @Override
    public void setDateValueTextView(TextView textView) {
        this.dayValueTextView = textView;
    }
}
