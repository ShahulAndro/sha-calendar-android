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

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.Toast;

import com.sha.calendar.lib.interfaces.IDayData;
import com.sha.calendar.lib.utilites.SelectionMode;
import com.sha.calendar.lib.utilites.ShaCalendarUtils;
import com.sha.calendar.lib.view.CalendarPickerRecyclerView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity {

    private CalendarPickerRecyclerView calendarPickerRecylerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.calendarPickerRecylerView = findViewById(R.id.calendar_recyclerview);
        this.calendarPickerRecylerView.setLayoutManager(new LinearLayoutManager(this));
        this.calendarPickerRecylerView.
                initSettings().
                setDisplayPreviousNextMonthDaysByMonth(false).
                setDisplayWeekDaysByMonth(false).
                setSelectionMode(SelectionMode.SINGLE).
                apply().
                loadContent();

        this.calendarPickerRecylerView.setCalendarCallback(new CalendarPickerRecyclerView.CalendarCallback() {
            @Override
            public void onDateSelected(IDayData dayData) {
                Toast.makeText(getApplicationContext(), "Date selected:"+dayData.getDate(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.single_selection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPickerRecylerView.
                        initSettings().
                        setDisplayPreviousNextMonthDaysByMonth(false).
                        setDisplayWeekDaysByMonth(true).
                        setSelectionMode(SelectionMode.SINGLE).
                        apply().
                        loadContent();
            }
        });

        findViewById(R.id.multiple_selection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPickerRecylerView.
                        initSettings().
                        setDisplayPreviousNextMonthDaysByMonth(false).
                        setDisplayWeekDaysByMonth(true).
                        setSelectionMode(SelectionMode.MULTIPLE).
                        apply().
                        loadContent();
            }
        });


        findViewById(R.id.range_selection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPickerRecylerView.
                        initSettings().
                        setDisplayPreviousNextMonthDaysByMonth(false).
                        setDisplayWeekDaysByMonth(true).
                        setSelectionMode(SelectionMode.RANGE).
                        apply().
                        loadContent();
            }
        });

        findViewById(R.id.rtl_selection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPickerRecylerView.
                        initSettings().
                        setDisplayPreviousNextMonthDaysByMonth(false).
                        setDisplayWeekDaysByMonth(true).
                        setLocale(new Locale("ar")).
                        setForceDisplayRTL(true).
                        setSelectionMode(SelectionMode.RANGE).
                        apply().
                        loadContent();
            }
        });

        findViewById(R.id.custom_dayview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPickerRecylerView.
                        initSettings().
                        setDisplayPreviousNextMonthDaysByMonth(false).
                        setDisplayWeekDaysByMonth(true).
                        setDayViewAdapterCallback(new DayViewAdapter(getApplicationContext())).
                        apply().
                        loadContent();
            }
        });

        findViewById(R.id.highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPickerRecylerView.
                        initSettings().
                        setDisplayPreviousNextMonthDaysByMonth(false).
                        setDisplayWeekDaysByMonth(true).
                        apply().
                        loadContent();

                calendarPickerRecylerView.highlightDates(ShaCalendarUtils.calendarFor(2018, 12, 31).getTime());
            }
        });

        findViewById(R.id.select_dates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPickerRecylerView.
                        initSettings().
                        setSelectionMode(SelectionMode.RANGE).
                        setDisplayPreviousNextMonthDaysByMonth(false).
                        setDisplayWeekDaysByMonth(true).
                        apply().
                        loadContent();

                Calendar from1 = ShaCalendarUtils.calendarFor(2019, 1, 1);
                Calendar to1 = ShaCalendarUtils.calendarFor(2019, 1, 15);
                calendarPickerRecylerView.selectDates(from1, to1);

                Calendar from2 = ShaCalendarUtils.calendarFor(2019, 2, 15);
                Calendar to2 = ShaCalendarUtils.calendarFor(2019, 2, 28);
                calendarPickerRecylerView.selectDates(from2, to2);
            }
        });

        findViewById(R.id.infinite_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPickerRecylerView.
                        initSettings().
                        setDisplayPreviousNextMonthDaysByMonth(false).
                        setDisplayWeekDaysByMonth(true).
                        setLoadInfinite(true).
                        apply().
                        loadContent();
            }
        });
    }
}
