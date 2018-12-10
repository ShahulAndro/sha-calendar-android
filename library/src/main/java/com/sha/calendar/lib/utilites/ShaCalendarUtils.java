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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class ShaCalendarUtils {

    public static Calendar calendarFor(int year, int month, int day){
        ShaValidationUtils.checkMonthValidation(month);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal;
    }

    public static Calendar calendarFor(int year, int month) {
        ShaValidationUtils.checkMonthValidation(month);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return cal;
    }

    public static Calendar getCalendarFromDate(Date date, TimeZone timeZone, Locale locale) {
        Calendar calendar = Calendar.getInstance(timeZone, locale);
        calendar.setTime(date);
        return calendar;
    }

    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }

        return false;
    }

    public static String getMonthKey(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String key = String.format("%d-%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        return key;
    }
}
