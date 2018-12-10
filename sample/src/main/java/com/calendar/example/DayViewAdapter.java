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

package com.calendar.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sha.calendar.lib.interfaces.DayView;
import com.sha.calendar.lib.interfaces.IDayData;
import com.sha.calendar.lib.interfaces.IDayView;
import com.sha.calendar.lib.interfaces.IDayViewAdapterCallback;

/**
 * Created by Shahul Hameed Shaik  on 06,December,2018
 * Email: android.shahul@gmail.com
 */
public class DayViewAdapter implements IDayViewAdapterCallback {

    private Context context;

    public DayViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public IDayView onCreateDayView() {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View mainView = inflater.inflate(R.layout.sample_custom_day_view, null, false);
        TextView dayOfMonthTextView = mainView.findViewById(R.id.day_value);

        IDayView dayView = new DayView();
        dayView.setDayView(mainView);
        dayView.setDateValueTextView(dayOfMonthTextView);

        return dayView;
    }

    @Override
    public void onUpDateDayView(View view, IDayData dayData) {
        TextView sampleTextView = view.findViewById(R.id.sampleText);
        if (sampleTextView != null) {
            sampleTextView.setText("hi hello");
            sampleTextView.setTextColor(view.getContext().getResources().getColor(R.color._light_green));
        }
    }
}

