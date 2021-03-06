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

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
public final class DateRange {
    private DayData fromDate;
    private DayData toDate;

    public DateRange() {
    }

    public DateRange(DayData fromDate, DayData toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public DayData getFromDate() {
        return fromDate;
    }

    public void setFromDate(DayData fromDate) {
        this.fromDate = fromDate;
    }

    public DayData getToDate() {
        return toDate;
    }

    public void setToDate(DayData toDate) {
        this.toDate = toDate;
    }
}
