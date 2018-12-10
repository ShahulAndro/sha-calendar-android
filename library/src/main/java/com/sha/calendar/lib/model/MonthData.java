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

package com.sha.calendar.lib.model;

import com.sha.calendar.lib.utilites.ShaCalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class MonthData {
    private boolean canDisplayWeekDays = true;
    private boolean canDisplayPreviousNextMonthDays = false;
    private boolean isDayDataUpdated;
    private int month;
    private int year;
    private String label;
    private Date date;
    private List<DayData> dayDataList = new ArrayList<>();
    private Map<Integer, DayData> dayDataMap = new HashMap<>();

    public MonthData() {
    }

    public MonthData(int month, int year, Date date, String label, List<DayData> dataList) {
        this.month = month;
        this.year = year;
        this.date = date;
        this.label = label;
        this.dayDataList = dataList;
        updateDayDataMap(this.dayDataList);
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Date getDate() {
        return date;
    }

    public String getLabel() {
        return label;
    }

    void setLabel(String label) {
        this.label = label;
    }

    public List<DayData> getDayDataList() {
        if (canDisplayPreviousNextMonthDays) {
            adjustDayDataListWithPreviousNextMonthsDays();
        } else {
            adjustDayDataListWithEmptyWeekDays();
        }

        return dayDataList;
    }

    public void setDayDataList(List<DayData> dayDataList) {
        this.dayDataList = dayDataList;
        updateDayDataMap(this.dayDataList);
    }

    public Map<Integer, DayData> getDayDataMap() {
        return dayDataMap;
    }

    public void updateDayDataMap(List<DayData> dayDataList) {
        for (DayData dayData: dayDataList) {
            if (dayData.isCurrentMonth()) {
                this.dayDataMap.put(ShaCalendarUtils.getDayOfMonth(dayData.getDate()), dayData);
            }
        }
    }

    public boolean isDayDataUpdated() {
        return isDayDataUpdated;
    }

    public void setDayDataUpdated(boolean isDayDataUpdated) {
        this.isDayDataUpdated = isDayDataUpdated;
    }

    public boolean canDisplayWeekDays() {
        return canDisplayWeekDays;
    }

    public void setDisplayWeekDays(boolean canDisplayWeekDays) {
        this.canDisplayWeekDays = canDisplayWeekDays;
    }

    public boolean canDisplayPreviousNextMonthDays() {
        return canDisplayPreviousNextMonthDays;
    }

    public void setDisplayPreviousNextMonthDays(boolean canDisplayPreviousNextMonthDays) {
        this.canDisplayPreviousNextMonthDays = canDisplayPreviousNextMonthDays;
    }

    private void adjustDayDataListWithEmptyWeekDays() {
        if (canDisplayPreviousNextMonthDays) {
            return;
        }

        if (isDayDataUpdated) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        for(int i = 0; i < firstDayOfWeek - 1 ; i++) {
            this.dayDataList.add(i, new DayData());
        }

        this.setDayDataUpdated(true);
    }

    private void adjustDayDataListWithPreviousNextMonthsDays() {
        if (!canDisplayPreviousNextMonthDays) {
            return;
        }

        if (isDayDataUpdated) {
            return;
        }

        this.dayDataList.addAll(0, getPreviousMonthsDayData());
        this.dayDataList.addAll(getNextMonthsDayData());
    }

    private List<DayData> getPreviousMonthsDayData() {
        List<DayData> previousDayDataList = new ArrayList<>();

        Calendar currentMonth = Calendar.getInstance();
        currentMonth.setTime(this.date);
        currentMonth.getActualMinimum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK);

        Calendar previousMonth = Calendar.getInstance();
        previousMonth.setTime(this.date);
        previousMonth.add(Calendar.MONTH, -1);
        int maximumDayOfMonth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i = 0; i < firstDayOfWeek - 1 ; i++) {
            DayData dayData = new DayData();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(previousMonth.getTime());
            int previousMonthStartDay = maximumDayOfMonth - (firstDayOfWeek - 2) + i;
            calendar.set(DAY_OF_MONTH, previousMonthStartDay);
            dayData.setDate(calendar.getTime());
            dayData.setValue(calendar.get(DAY_OF_MONTH));
            dayData.setCurrentMonth(false);
            previousDayDataList.add(dayData);
        }

        this.setDayDataUpdated(true);
        return previousDayDataList;
    }

    private List<DayData> getNextMonthsDayData() {
        int totalWeekDays = 7;
        List<DayData> nextMonthDays = new ArrayList<>();

        Calendar currentMonth = Calendar.getInstance();
        currentMonth.setTime(this.date);
        currentMonth.set(DAY_OF_MONTH, currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK);

        Calendar nextMonth = Calendar.getInstance();
        nextMonth.setTime(this.date);
        nextMonth.add(Calendar.MONTH, 1);
        for(int i = 0; i < (totalWeekDays - lastDayOfWeek) ; i++) {
            DayData dayData = new DayData();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nextMonth.getTime());
            calendar.set(DAY_OF_MONTH, i + 1);
            dayData.setDate(calendar.getTime());
            dayData.setToday(sameDate(calendar, Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())));
            dayData.setValue(calendar.get(DAY_OF_MONTH));
            dayData.setCurrentMonth(false);
            nextMonthDays.add(dayData);
        }

        this.setDayDataUpdated(true);
        return nextMonthDays;
    }

    private boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(MONTH) == selectedDate.get(MONTH)
                && cal.get(YEAR) == selectedDate.get(YEAR)
                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
    }

}
