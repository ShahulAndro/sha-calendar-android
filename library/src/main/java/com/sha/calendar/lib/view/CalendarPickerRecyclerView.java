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

package com.sha.calendar.lib.view;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.calendar.lib.R;
import com.sha.calendar.lib.adapter.DefaultDayDataView;
import com.sha.calendar.lib.adapter.MonthRecyclerViewAdapter;
import com.sha.calendar.lib.interfaces.IDayData;
import com.sha.calendar.lib.interfaces.IDayViewAdapterCallback;
import com.sha.calendar.lib.model.DateRange;
import com.sha.calendar.lib.model.DayData;
import com.sha.calendar.lib.model.MonthData;
import com.sha.calendar.lib.utilites.RangeState;
import com.sha.calendar.lib.utilites.SelectionMode;
import com.sha.calendar.lib.utilites.ShaCalendarUtils;
import com.sha.calendar.lib.utilites.ShaDateUtils;
import com.sha.calendar.lib.utilites.ShaValidationUtils;
import com.sha.calendar.lib.viewlistener.RecyclerViewEndlessScrollListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class CalendarPickerRecyclerView extends RecyclerView implements MonthRecyclerViewAdapter.MonthRecyclerViewAdapterCallback {

    private Locale locale = Locale.getDefault();
    private TimeZone timeZone = TimeZone.getDefault();

    private boolean canDisplayWeekDays = true;
    private boolean canDisplayPreviousNextMonthDays = false;
    private boolean canDisplayBorders = false;
    private boolean isForceDisplayRTL = false;
    private boolean isLoadInfinite = false;
    private SelectionMode selectionMode = SelectionMode.SINGLE;
    private int defaultLoadMonthsWithInfiniteLoad = 6;

    private Calendar fromCal;
    private Calendar toCal;
    private Calendar monthCounter;

    private List<MonthData> monthDataList = new ArrayList<>();
    private List<DayData> selectedDates = new ArrayList<>();
    private List<DateRange> selectedDateRanges = new ArrayList<>();

    private Map<String, MonthData> monthDataMap = new HashMap<>();

    private DayData fromDayData;
    private DayData toDayData;
    private IDayViewAdapterCallback dayViewAdapterCallback;
    private MonthRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Context context;

    private RecyclerView.OnScrollListener customScrollListener;
    private CalendarSettings calendarSettings;
    private CalendarCallback calendarCallback;

    public CalendarPickerRecyclerView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CalendarPickerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CalendarPickerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    @Override
    public MonthRecyclerViewAdapter getAdapter() {
        return this.adapter;
    }


    private void reset() {
        monthDataList.clear();
        selectedDates.clear();
        selectedDateRanges.clear();
        monthDataMap.clear();

        removeAllViewsInLayout();
    }

    @Override
    public void onDaySelectedInMonth(DayData dayData) {
        Log.d("Selected date", ""+dayData.getDate());

        if (this.selectionMode == SelectionMode.SINGLE) {
            updateSelectionWithSingleMode(dayData);
        } else if (this.selectionMode == SelectionMode.MULTIPLE) {
            updateSelectionWithMultipleMode(dayData);
        } else if (this.selectionMode == SelectionMode.RANGE) {
            updateSelectionWithRangeMode(dayData);
        }

        if (this.calendarCallback != null) {
            this.calendarCallback.onDateSelected(dayData);
        }
    }

    @Override
    public void setLayoutManager(@Nullable RecyclerView.LayoutManager layout) {
        super.setLayoutManager(layout);
        this.layoutManager = (LinearLayoutManager) layout;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void setCalendarCallback(CalendarCallback calendarCallback) {
        this.calendarCallback = calendarCallback;
    }

    public CalendarPickerRecyclerView init() {
        this.adapter = new MonthRecyclerViewAdapter(getContext());
        this.adapter.setHasStableIds(true);
        this.adapter.setCallback(this);
        setAdapter(this.adapter);

        Calendar nextYear = Calendar.getInstance(timeZone, locale);
//        nextYear.add(Calendar.YEAR, 1);
        nextYear.add(MONTH, 6);

        this.fromCal = ShaCalendarUtils.getCalendarFromDate(new Date(), timeZone, locale);
        this.fromCal.add(MONTH, -6);

        this.toCal = ShaCalendarUtils.getCalendarFromDate(nextYear.getTime(), timeZone, locale);

        return this;
    }

    public CalendarPickerRecyclerView loadContent() {
        reset();
        this.fromCal.set(Calendar.DAY_OF_MONTH, this.fromCal.getActualMinimum(Calendar.DAY_OF_MONTH));
        this.toCal.set(Calendar.DAY_OF_MONTH, this.toCal.getActualMaximum(Calendar.DAY_OF_MONTH));

        this.monthCounter = Calendar.getInstance(timeZone, locale);
        monthCounter.setTime(this.fromCal.getTime());

        ShaDateUtils.clearHoursMinSecMillsFromCalendar(this.fromCal);
        ShaDateUtils.clearHoursMinSecMillsFromCalendar(this.toCal);

        final int maxMonth = this.toCal.get(MONTH);
        final int maxYear = this.toCal.get(YEAR);
        while ((monthCounter.get(MONTH) <= maxMonth || monthCounter.get(YEAR) < maxYear) && monthCounter.get(YEAR) < maxYear + 1) {
            Date date = monthCounter.getTime();
            MonthData monthData = new MonthData(monthCounter.get(MONTH), monthCounter.get(YEAR), date, formatMonthDate(date), getDaysFromMonth(date));
            monthData.setDisplayWeekDays(this.canDisplayWeekDays);
            monthData.setDisplayPreviousNextMonthDays(this.canDisplayPreviousNextMonthDays);
            this.monthDataList.add(monthData);
            this.monthCounter.add(MONTH, 1);
        }

        this.adapter.setMonthDataList(monthDataList);
        if (this.dayViewAdapterCallback == null) {
            this.dayViewAdapterCallback = new DefaultDayDataView(this.context);
        }

        this.adapter.setDayViewAdapterCallback(this.dayViewAdapterCallback);
        this.adapter.setLocale(this.locale);
        this.adapter.setForceDisplayRTL(this.isForceDisplayRTL);
        this.updateMonthsMap(this.monthDataList);

        setScrollListener();

        this.adapter.notifyDataSetChanged();
        this.scrollToMonthWithDate(new Date());

        return this;
    }

    private void setScrollListener() {
        ShaValidationUtils.checkInfiniteLoad(this.customScrollListener, this.isLoadInfinite);
        if (this.isLoadInfinite) {
            if (this.customScrollListener == null) {
                this.addOnScrollListener(new RecyclerViewEndlessScrollListener(this.layoutManager) {

                    @Override
                    public void onLoadMorePrevious() {
                        loadPreviousMonths();
                    }

                    @Override
                    public void onLoadMoreNext() {
                        loadNextMonths();
                    }
                });
            } else {
                this.addOnScrollListener(this.customScrollListener);
            }
        }
    }

    public CalendarPickerRecyclerView load(Calendar from, Calendar to) {
        ShaValidationUtils.checkCalendarNull(from);
        ShaValidationUtils.checkCalendarNull(to);
        ShaValidationUtils.checkFromToCalendar(from, to);

        this.fromCal = from;
        this.toCal = to;
        loadContent();
        return this;
    }

    public CalendarPickerRecyclerView load(Date from) {
        Calendar to = Calendar.getInstance();
        to.setTime(from);
        load(from, to.getTime());
        return this;
    }

    public CalendarPickerRecyclerView load(Calendar from) {
        Calendar to = Calendar.getInstance();
        to.setTime(from.getTime());
        load(from, to);
        return this;
    }

    public CalendarPickerRecyclerView load(Date from, Date to) {
        ShaValidationUtils.checkDateNull(from);
        ShaValidationUtils.checkDateNull(to);
        ShaValidationUtils.checkFromToDates(from, to);

        this.fromCal = ShaCalendarUtils.getCalendarFromDate(from, timeZone, locale);
        this.toCal = ShaCalendarUtils.getCalendarFromDate(to, timeZone, locale);
        loadContent();
        return this;
    }

    private List<DayData> getDaysFromMonth(Date date) {
        Calendar calMax = Calendar.getInstance(timeZone, locale);
        calMax.setTime(date);
        calMax.set(DAY_OF_MONTH, calMax.getActualMinimum(DAY_OF_MONTH));

        Calendar cal = Calendar.getInstance(timeZone, locale);
        cal.setTime(date);
        cal.set(DAY_OF_MONTH, cal.getActualMinimum(DAY_OF_MONTH));

        List<DayData> dayDataList = new ArrayList<>();

        for (int i = 0; i < calMax.getActualMaximum(DAY_OF_MONTH); i++) {
            DayData dayData = new DayData();
            dayData.setDate(cal.getTime());
            dayData.setToday(ShaDateUtils.isSameDay(cal.getTime(), Calendar.getInstance(timeZone, locale).getTime()));
            dayData.setValue(cal.get(DAY_OF_MONTH));
            dayData.setCurrentMonth(true);

            dayDataList.add(dayData);

            cal.add(DATE, 1);
        }

        return dayDataList;
    }

    private void updateSelectionWithSingleMode(DayData dayData) {
        if(this.selectedDates.size() == 0) {
            this.selectedDates.add(0, dayData);
            updateDayDataListIntoMonthData(dayData, true);

        } else {
            DayData alreadySelectedDayData = this.selectedDates.get(0);
            if (ShaDateUtils.isSameDay(dayData.getDate(), alreadySelectedDayData.getDate())) {
                updateDayDataListIntoMonthData(dayData, false);
                this.selectedDates.clear();
            } else {
                updateDayDataListIntoMonthData(alreadySelectedDayData, false);
                this.selectedDates.add(0, dayData);
                updateDayDataListIntoMonthData(dayData, true);
            }
        }
    }

    private void updateSelectionWithMultipleMode(DayData dayData) {
        if (this.selectedDates.size() > 0) {
            int alreadySelectedDayDataPosition = this.selectedDates.indexOf(dayData);
            if (alreadySelectedDayDataPosition != -1 &&
                    ShaDateUtils.isSameDay(this.selectedDates.get(alreadySelectedDayDataPosition).getDate(), dayData.getDate())) {

                this.selectedDates.remove(alreadySelectedDayDataPosition);
                updateDayDataListIntoMonthData(dayData, false);
                return;
            }
        }

        this.selectedDates.add(dayData);
        updateDayDataListIntoMonthData(dayData, true);
    }

    private void updateSelectionWithRangeMode(DayData dayData) {
        if (this.fromDayData == null) {
            this.fromDayData = dayData;
            updateDayDataListIntoMonthData(dayData, true);
            return;
        }

        if (ShaDateUtils.isSameDay(this.fromDayData.getDate(), dayData.getDate())) {
            if (this.selectedDateRanges.size() > 0) {
                updateDayDataRange(this.selectedDateRanges.get(0).getFromDate().getDate(), this.selectedDateRanges.get(0).getToDate().getDate(), false);
            }
            updateDayDataListIntoMonthData(dayData, true);
            this.toDayData = null;
            return;
        }

        if (ShaDateUtils.isAfterDay(this.fromDayData.getDate(), dayData.getDate())) {
            if (this.selectedDateRanges.size() > 0) {
                updateDayDataRange(this.selectedDateRanges.get(0).getFromDate().getDate(), this.selectedDateRanges.get(0).getToDate().getDate(), false);
            }

            updateDayDataListIntoMonthData(this.fromDayData, false);
            updateDayDataListIntoMonthData(dayData, true);
            this.fromDayData = dayData;
            this.toDayData = null;
            return;
        }

        if (this.toDayData == null){
            this.toDayData = dayData;
            updateDayDataRange(this.fromDayData.getDate(), this.toDayData.getDate(), true);
            this.selectedDateRanges.add(new DateRange(this.fromDayData, this.toDayData));
            return;
        }

        if (this.fromDayData != null && this.toDayData != null) {
            this.selectedDateRanges.clear();
            updateDayDataRange(this.fromDayData.getDate(), this.toDayData.getDate(), false);
            updateDayDataListIntoMonthData(dayData, true);
            this.fromDayData = dayData;
            this.toDayData = null;
        }
    }

    private void updateDayDataRange(Date from, Date to, boolean selection) {
        this.selectedDateRanges.clear();
        List<Integer> updatedMonthPositions = new ArrayList<>();

        Calendar dayDataCalendarFrom = Calendar.getInstance();
        dayDataCalendarFrom.setTime(from);

        while (!ShaDateUtils.isAfterDay(dayDataCalendarFrom.getTime(), to)) {
            MonthData monthDataFrom = this.monthDataMap.get(ShaCalendarUtils.getMonthKey(dayDataCalendarFrom.getTime()));
            DayData dayData = monthDataFrom.getDayDataMap().get(ShaCalendarUtils.getDayOfMonth(dayDataCalendarFrom.getTime()));
            int dayDataIndexInMonth = monthDataFrom.getDayDataList().indexOf(dayData);
            if (dayDataIndexInMonth != -1) {
                for (int i = dayDataIndexInMonth; i < monthDataFrom.getDayDataList().size(); i++) {

                    DayData updateDayData = monthDataFrom.getDayDataList().get(i);
                    updateDayData.setSelected(selection);
                    updateDayData.setRangeState(getRangeState(updateDayData.getDate(), from, to, selection));
                    monthDataFrom.getDayDataList().set(i, updateDayData);
                    this.selectedDates.add(updateDayData);
                    dayDataCalendarFrom.add(DAY_OF_MONTH, 1);

                    if (ShaCalendarUtils.isLastDayOfMonth(updateDayData.getDate()) ||
                            ShaDateUtils.isSameDay(updateDayData.getDate(), to)) {
                        updatedMonthPositions.add(this.monthDataList.indexOf(monthDataFrom));
                        break;
                    }
                }
            }
        }

        for (Integer position : updatedMonthPositions) {
            this.adapter.notifyItemChanged(position);
        }
    }

    private RangeState getRangeState(Date updatedDate, Date from, Date to, boolean selection) {
        if (!selection) {
            return RangeState.NONE;
        }

        if (ShaDateUtils.isSameDay(updatedDate, from)) {
            return RangeState.FIRST;
        } else if (ShaDateUtils.isSameDay(updatedDate, to)) {
            return RangeState.LAST;
        } else if (ShaDateUtils.isAfterDay(updatedDate, from) && ShaDateUtils.isBeforeDay(updatedDate, to)) {
            return RangeState.MIDDLE;
        } else {
            return RangeState.NONE;
        }
    }

    private void updateDateRangeState(boolean selection) {
        if (selection) {
            if (this.selectedDates.size() > 2) {
                this.selectedDates.get(0).setRangeState(RangeState.FIRST);
                this.selectedDates.get(this.selectedDates.size() - 1).setRangeState(RangeState.LAST);

                for (int i = 1; i < this.selectedDates.size() - 1; i++) {
                    this.selectedDates.get(i).setRangeState(RangeState.MIDDLE);
                }
            }
        } else {
            if (this.selectedDates.size() > 0) {
                for (DayData dayData: this.selectedDates) {
                    dayData.setRangeState(RangeState.NONE);
                }

                this.selectedDates.clear();
            }
        }
    }

    private void updateDayDataListIntoMonthData(DayData dayData, boolean selection) {
        dayData.setSelected(selection);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dayData.getDate());
        MonthData monthData = this.monthDataMap.get(ShaCalendarUtils.getMonthKey(dayData.getDate()));
        if (monthData.getDayDataList() != null && monthData.getDayDataList().indexOf(dayData) != -1) {
            monthData.getDayDataList().set(monthData.getDayDataList().indexOf(dayData), dayData);
            this.adapter.notifyItemChanged(monthDataList.indexOf(monthData));
        }
    }

    private void updateMonthsMap(List<MonthData> monthsData) {
        if (!monthsData.isEmpty()) {
            for (int i = 0; i < monthsData.size(); i++) {
                this.monthDataMap.put(ShaCalendarUtils.getMonthKey(monthsData.get(i).getDate()), monthsData.get(i));
            }
        }
    }

    public void selectDate(Date date) {
        selectDateInMonth(date, true);
    }

    public void clearDateSelection(Date date) {
        selectDateInMonth(date, false);
    }

    public List<DayData> getSelectedDays() {
        return this.selectedDates;
    }

    public void selectDates(Date from, Date to) {
        ShaValidationUtils.checkDateNull(from);
        ShaValidationUtils.checkDateNull(to);
        ShaValidationUtils.checkSelectDates(from, to, this.selectionMode);
        ShaValidationUtils.checkSelectDateWithinCalendarRange(from, fromCal, toCal);
        ShaValidationUtils.checkSelectDateWithinCalendarRange(to, fromCal, toCal);

        if (ShaDateUtils.isSameDay(from, to)) {
            selectDate(from);
            return;
        }


        updateDayDataRange(from, to, true);
    }

    public void selectDates(Calendar from, Calendar to) {
        ShaValidationUtils.checkCalendarNull(from);
        ShaValidationUtils.checkCalendarNull(to);
        ShaValidationUtils.checkSelectDates(from.getTime(), to.getTime(), this.selectionMode);

        if (ShaDateUtils.isSameDay(from.getTime(), to.getTime())) {
            selectDate(from.getTime());
            return;
        }

        updateDayDataRange(from.getTime(), to.getTime(), true);
    }

    public void highlightDates(Date date) {
        highlightDates(date, true);
    }

    public void clearHighlightDate(Date date) {
        highlightDates(date, false);
    }

    public void highlightDates(Collection<Date> dates) {
        for (Date date : dates) {
            highlightDates(date);
        }
    }

    public void clearHighlightDates(Collection<Date> dates) {
        for (Date date : dates) {
            clearHighlightDate(date);
        }
    }

    private void highlightDates(Date date, boolean isHighLighted) {
        ShaValidationUtils.checkHighlightDate(date, fromCal, toCal);

        updateMonthByDate(date, isHighLighted);
    }

    public void scrollToMonthWithDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        MonthData monthData = this.monthDataMap.get(ShaCalendarUtils.getMonthKey(date));
        if (monthData != null) {
            int monthIndex = this.monthDataList.indexOf(monthData);
            if (monthIndex != -1) {
                scrollToPosition(monthIndex);
            }
        }
    }

    private void updateMonthByDate(Date date, boolean isHighLighted) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        MonthData monthData = this.monthDataMap.get(ShaCalendarUtils.getMonthKey(date));
        DayData dayData = monthData.getDayDataMap().get(ShaCalendarUtils.getDayOfMonth(date));
        int indexOfDayInMonth = monthData.getDayDataList().indexOf(dayData);
        if (indexOfDayInMonth == -1) {
            return;
        }

        dayData.setHighlighted(isHighLighted);
        monthData.getDayDataList().set(monthData.getDayDataList().indexOf(dayData), dayData);
        this.adapter.notifyItemChanged(monthDataList.indexOf(monthData));
    }

    private void selectDateInMonth(Date date, boolean selection) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        MonthData monthData = this.monthDataMap.get(ShaCalendarUtils.getMonthKey(date));
        DayData dayData = monthData.getDayDataMap().get(ShaCalendarUtils.getDayOfMonth(date));
        int indexOfDayInMonth = monthData.getDayDataList().indexOf(dayData);
        if (indexOfDayInMonth == -1) {
            return;
        }

        dayData.setSelected(selection);
        monthData.getDayDataList().set(monthData.getDayDataList().indexOf(dayData), dayData);
        this.adapter.notifyItemChanged(monthDataList.indexOf(monthData));
    }

    private String formatMonthDate(Date date) {
        String dateFormatted;
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdfMonth = new SimpleDateFormat(getContext()
                .getString(R.string.month_only_name_format), locale);
        SimpleDateFormat sdfYear = new SimpleDateFormat(getContext()
                .getString(R.string.year_only_format), Locale.ENGLISH);
        dateFormatted = sb.append(sdfMonth.format(date.getTime())).append(" ")
                .append(sdfYear.format(date.getTime())).toString();
        return dateFormatted;
    }

    private void clearData() {
        monthDataList.clear();
        selectedDates.clear();
        selectedDateRanges.clear();
        monthDataMap.clear();
    }

    private synchronized void loadMoreMonths(Calendar from, Calendar to, int addIndexPosition) {
        List<MonthData> loadMoreMonthDataList = new ArrayList<>();
        Calendar monthCounter = Calendar.getInstance(timeZone, locale);
        monthCounter.setTime(from.getTime());

        ShaDateUtils.clearHoursMinSecMillsFromCalendar(from);
        ShaDateUtils.clearHoursMinSecMillsFromCalendar(to);

        final int maxMonth = to.get(MONTH);
        final int maxYear = to.get(YEAR);
        while ((monthCounter.get(MONTH) <= maxMonth || monthCounter.get(YEAR) < maxYear)
                && monthCounter.get(YEAR) < maxYear + 1) {
            Date date = monthCounter.getTime();
            MonthData monthData =
                    new MonthData(monthCounter.get(MONTH), monthCounter.get(YEAR),
                            date, formatMonthDate(date), getDaysFromMonth(date));
            monthData.setDisplayWeekDays(this.canDisplayWeekDays);
            monthData.setDisplayPreviousNextMonthDays(this.canDisplayPreviousNextMonthDays);
            loadMoreMonthDataList.add(monthData);
            monthCounter.add(MONTH, 1);
        }

        if (loadMoreMonthDataList.size() > 0) {
            this.monthDataList.addAll(addIndexPosition, loadMoreMonthDataList);
        }

        this.updateMonthsMap(loadMoreMonthDataList);
    }

    private void loadPreviousMonths() {
        final Calendar from = Calendar.getInstance();
        from.setTime(this.fromCal.getTime());
        from.add(MONTH, -defaultLoadMonthsWithInfiniteLoad);
        from.set(DAY_OF_MONTH, from.getActualMinimum(DAY_OF_MONTH));

        Calendar to = Calendar.getInstance();
        to.setTime(this.fromCal.getTime());
        to.add(MONTH, -1);
        to.set(DAY_OF_MONTH, to.getActualMaximum(DAY_OF_MONTH));

        this.fromCal = from;

        loadMoreMonths(from, to,0);
        this.adapter.notifyItemRangeInserted(0, defaultLoadMonthsWithInfiniteLoad);
    }

    private void loadNextMonths() {
        Calendar from = Calendar.getInstance();
        from.setTime(this.toCal.getTime());
        from.add(MONTH, 1);
        from.set(DAY_OF_MONTH, from.getActualMinimum(DAY_OF_MONTH));

        Calendar to = Calendar.getInstance();
        to.setTime(this.toCal.getTime());
        to.add(DAY_OF_MONTH, defaultLoadMonthsWithInfiniteLoad);
        to.set(DAY_OF_MONTH, to.getActualMaximum(DAY_OF_MONTH));

        this.toCal = to;

        int currentScrollPosition = this.monthDataList.size();
        loadMoreMonths(from, to, this.monthDataList.size());

        this.adapter.notifyItemRangeInserted(currentScrollPosition, defaultLoadMonthsWithInfiniteLoad);
    }


    public CalendarSettings initSettings() {
        return new CalendarSettings();
    }

    public class CalendarSettings {
        private boolean canDisplayWeekDays = true;
        private boolean canDisplayPreviousNextMonthDays = false;
        private boolean canDisplayBorders = false;
        private boolean isForceDisplayRTL = false;
        private boolean isLoadInfinite = false;
        private SelectionMode selectionMode = SelectionMode.SINGLE;
        private  IDayViewAdapterCallback dayViewAdapterCallback = null;
        private Locale locale = Locale.getDefault();
        private TimeZone timeZone = TimeZone.getDefault();
        private RecyclerView.OnScrollListener customScrollListener;

        public CalendarSettings() {
        }

        public CalendarSettings setDisplayWeekDaysByMonth(boolean canDisplayWeekDays) {
            this.canDisplayWeekDays = canDisplayWeekDays;
            return this;
        }

        public CalendarSettings setDisplayPreviousNextMonthDaysByMonth(boolean canDisplayPreviousNextMonthDays) {
            this.canDisplayPreviousNextMonthDays = canDisplayPreviousNextMonthDays;
            return this;
        }

        public CalendarSettings setSelectionMode(SelectionMode selectionMode) {
            this.selectionMode = selectionMode;
            return this;
        }

        public CalendarSettings setCanDisplayBorders(boolean canDisplayBorders) {
            this.canDisplayBorders = canDisplayBorders;
            return this;
        }

        public CalendarSettings setDayViewAdapterCallback(IDayViewAdapterCallback dayViewAdapterCallback) {
            this.dayViewAdapterCallback = dayViewAdapterCallback;
            return this;
        }

        public CalendarSettings setForceDisplayRTL(boolean isForceDisplayRTL) {
            this.isForceDisplayRTL = isForceDisplayRTL;
            return this;
        }

        public CalendarSettings setLoadInfinite(boolean loadInfinite) {
            isLoadInfinite = loadInfinite;
            return this;
        }

        public CalendarSettings setCustomScrollListener(OnScrollListener customScrollListener) {
            this.customScrollListener = customScrollListener;
            return this;
        }

        public CalendarSettings setLocale(Locale locale) {
            if (locale == null) {
                this.locale = Locale.getDefault();
                return this;
            }

            this.locale = locale;

            return this;
        }

        public CalendarSettings setTimeZone(TimeZone timeZone) {
            if (timeZone == null) {
                this.timeZone = TimeZone.getDefault();
                return this;
            }

            this.timeZone = timeZone;
            return this;
        }

        public CalendarPickerRecyclerView apply() {
            CalendarPickerRecyclerView.this.canDisplayWeekDays = this.canDisplayWeekDays;
            CalendarPickerRecyclerView.this.canDisplayPreviousNextMonthDays = this.canDisplayPreviousNextMonthDays;
            CalendarPickerRecyclerView.this.selectionMode = this.selectionMode;
            CalendarPickerRecyclerView.this.canDisplayBorders = this.canDisplayBorders;
            CalendarPickerRecyclerView.this.isForceDisplayRTL = this.isForceDisplayRTL;
            CalendarPickerRecyclerView.this.isLoadInfinite = this.isLoadInfinite;
            CalendarPickerRecyclerView.this.locale = this.locale;
            CalendarPickerRecyclerView.this.timeZone = this.timeZone;
            CalendarPickerRecyclerView.this.dayViewAdapterCallback = this.dayViewAdapterCallback;
            CalendarPickerRecyclerView.this.customScrollListener = this.customScrollListener;
            return CalendarPickerRecyclerView.this;
        }
    }

    public interface CalendarCallback {
        void onDateSelected(IDayData dayData);
    }
}
