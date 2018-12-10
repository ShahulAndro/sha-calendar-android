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

import com.sha.calendar.lib.interfaces.IDayData;
import com.sha.calendar.lib.utilites.RangeState;

import java.util.Date;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class DayData implements IDayData {

    private RangeState rangeState = RangeState.NONE;
    private boolean isToday;
    private boolean isCurrentMonth;
    private boolean isSelected;
    private boolean isHighlighted;
    private int value;
    private Date date;

    public DayData() {
    }

    public DayData(Date date, boolean today, int value) {
        this.date = date;
        isToday = today;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public String getValue() {
        if (value == 0) {
            return "";
        }
        return String.valueOf(value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public RangeState getRangeState() {
        return rangeState;
    }

    public void setRangeState(RangeState rangeState) {
        this.rangeState = rangeState;
    }
}
