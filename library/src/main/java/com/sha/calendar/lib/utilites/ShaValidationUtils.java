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

package com.sha.calendar.lib.utilites;

import android.support.v7.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
public final class ShaValidationUtils {

    public static void checkDateNull(Date date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "From: or To: dates should not be null");
        }
    }

    public static void checkCalendarNull(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException(
                    "From: or To: calendar should not be null");
        }
    }

    public static void checkFromToDates(Date from, Date to) {
        if (!ShaDateUtils.isBeforeDay(from, to)) {
            if (!ShaDateUtils.isSameDay(from, to)) {
                throw new IllegalArgumentException(
                        "From: date should be before To: date, please check from, to dates");
            }
        }
    }

    public static void checkFromToCalendar(Calendar from, Calendar to) {
        if (!ShaDateUtils.isBeforeDay(from, to)) {
            if (!ShaDateUtils.isSameDay(from, to)) {
                throw new IllegalArgumentException(
                        "From: calendar should be before To: calendar, please check From, To dates ");
            }

        }
    }

    public static void checkHighlightDate(Date date, Calendar from, Calendar to) {
        Calendar highlightCal = Calendar.getInstance();
        highlightCal.setTime(date);

        if (ShaDateUtils.isSameDay(date, from.getTime()) || ShaDateUtils.isSameDay(date, to.getTime())) {
            return;
        }

        if (!(date.after(from.getTime()) && date.before(to.getTime()))) {
            throw new IllegalArgumentException(
                    "Highlight date is not valid, its not within calendar range. Please check given highlight date");
        }
    }

    public static void checkMonthValidation(int month) {
        if (!(month >= 1 && month <=12)) {
            throw new IllegalArgumentException("Month value will be allowed from 1 to 12, but the value given: "+month);
        }
    }

    public static void checkSelectDates(Date from, Date to, SelectionMode selectionMode) {

        if (selectionMode != SelectionMode.RANGE) {
            throw new IllegalArgumentException(
                    "Please set selection mode with RANGE in Calendar Settings");
        }

        if (from.after(to)) {
            throw new IllegalArgumentException(
                    "From: date should be before To: date in Range");
        }
    }

    public static void checkSelectDateWithinCalendarRange(Date date, Calendar from, Calendar to) {
        if (ShaDateUtils.isSameDay(date, from.getTime()) || ShaDateUtils.isSameDay(date, to.getTime())) {
            return;
        }

        if (!(date.after(from.getTime()) && date.before(to.getTime()))) {
            throw new IllegalArgumentException(
                    "Selected date is not valid, its not within calendar range. Please check given select date");
        }
    }

    public static void checkInfiniteLoad(RecyclerView.OnScrollListener iCalendarScrollListener, boolean isLoadInfinite) {
        if (iCalendarScrollListener != null) {
            if (!isLoadInfinite) {
                throw new IllegalArgumentException(
                        "please initialize setting setLoadInfinite to be true");
            }
        }
    }
}
