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

package com.sha.calendar.lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.calendar.lib.R;
import com.sha.calendar.lib.interfaces.DayView;
import com.sha.calendar.lib.interfaces.IDayData;
import com.sha.calendar.lib.interfaces.IDayView;
import com.sha.calendar.lib.interfaces.IDayViewAdapterCallback;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class DefaultDayDataView implements IDayViewAdapterCallback {

    private Context context;

    public DefaultDayDataView(Context context) {
        this.context = context;
    }

    @Override
    public IDayView onCreateDayView() {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View mainView = inflater.inflate(R.layout.day_view, null, false);
        TextView dayOfMonthTextView = mainView.findViewById(R.id.day_value);

        IDayView dayView = new DayView();
        dayView.setDayView(mainView);
        dayView.setDateValueTextView(dayOfMonthTextView);

        return dayView;
    }

    @Override
    public void onUpDateDayView(View view, IDayData dayData) {

    }

}
