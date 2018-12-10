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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

final class YearData {
    private int year;
    private String label;
    private Date date;
    private List<MonthData> monthDataList = new ArrayList<>();

    public YearData() {
    }

    public YearData(int year, Date date, String label, List<MonthData> monthDataList) {
        this.year = year;
        this.date = date;
        this.label = label;
        this.monthDataList = monthDataList;
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

    public void setLabel(String label) {
        this.label = label;
    }

    public List<MonthData> getMonthDataList() {
        return monthDataList;
    }

    public void setMonthDataList(List<MonthData> monthDataList) {
        this.monthDataList = monthDataList;
    }
}
