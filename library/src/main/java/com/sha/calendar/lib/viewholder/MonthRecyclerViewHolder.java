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

package com.sha.calendar.lib.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calendar.lib.R;
import com.sha.calendar.lib.adapter.DayDataAdapter;
import com.sha.calendar.lib.interfaces.IDayViewAdapterCallback;
import com.sha.calendar.lib.model.MonthData;
import com.sha.calendar.lib.utilites.ShaLocaleUtils;
import com.sha.calendar.lib.utilites.WeekdayNames;

import java.util.Locale;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class MonthRecyclerViewHolder extends RecyclerView.ViewHolder {

    private boolean isForceDisplayRTL;
    private Locale locale;

    private WeekdayNames weekdayNames;
    private MonthData monthData;
    private DayDataAdapter.DayAdapterCallback callback;
    private IDayViewAdapterCallback dayViewAdapterCallback;

    private View view;
    private Context context;

    public MonthRecyclerViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public MonthRecyclerViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.view = itemView;
    }

    public void setCallback(DayDataAdapter.DayAdapterCallback dayAdapterCallback) {
        this.callback = dayAdapterCallback;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setForceDisplayRTL(boolean forceDisplayRTL) {
        this.isForceDisplayRTL = forceDisplayRTL;
    }

    public void setDayViewAdapterCallback(IDayViewAdapterCallback dayViewAdapterCallback) {
        this.dayViewAdapterCallback = dayViewAdapterCallback;
    }

    public void bindView(MonthData monthData) {
        applyRTLOnViews();

        this.monthData = monthData;

        TextView textView = this.view.findViewById(R.id.title);
        textView.setText(this.monthData.getLabel());

        displayWeekDays();

        GridView gridView = this.view.findViewById(R.id.month_days_gridview);
        DayDataAdapter dayDataAdapter = new DayDataAdapter(this.context);
        dayDataAdapter.setDayDataList(this.monthData.getDayDataList());
        dayDataAdapter.setCallback(this.callback);
        dayDataAdapter.setDayViewAdapterCallback(this.dayViewAdapterCallback);
        dayDataAdapter.isRTL(locale);
        dayDataAdapter.setForceDisplayRTL(this.isForceDisplayRTL);
        dayDataAdapter.setDisplayPreviousNextMonthDays(monthData.canDisplayPreviousNextMonthDays());
        gridView.setAdapter(dayDataAdapter);

    }

    private void displayWeekDays() {
        LinearLayout weekDaysView = this.view.findViewById(R.id.week_days);
        if (!this.monthData.canDisplayWeekDays()) {
            weekDaysView.setVisibility(View.GONE);
            return;
        }

        this.weekdayNames = new WeekdayNames(this.locale);

        setDayName(R.id.dayName1, 1);
        setDayName(R.id.dayName2, 2);
        setDayName(R.id.dayName3, 3);
        setDayName(R.id.dayName4, 4);
        setDayName(R.id.dayName5, 5);
        setDayName(R.id.dayName6, 6);
        setDayName(R.id.dayName7, 7);
    }

    private void setDayName(int dayTextId, int day) {
        TextView shortWeekDayName = this.view.findViewById(dayTextId);
        if (shortWeekDayName != null) {
            shortWeekDayName.setText(this.weekdayNames.getShortNameWeekdays()[day]);
            Log.d("WeekDayName", this.weekdayNames.getShortNameWeekdays()[day]);
        }
    }

    private void applyRTLOnViews() {
        if(!ShaLocaleUtils.isRTL(Locale.getDefault()) && this.isForceDisplayRTL) {
            applyRTLOnMonthTextView();
            applyRTLOnWeekDays();
            applyRTLOnGridViews();
        }
    }

    private void applyRTLOnMonthTextView() {
        this.view.findViewById(R.id.title).setRotationY(ShaLocaleUtils.rotateYForView());
    }

    private void applyRTLOnWeekDays() {
        ViewGroup viewGroup = this.view.findViewById(R.id.week_days);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setRotationY(ShaLocaleUtils.rotateYForView());
        }
    }

    private void applyRTLOnGridViews() {
        this.view.setRotationY(ShaLocaleUtils.rotateYForViewGroup());
    }
}
