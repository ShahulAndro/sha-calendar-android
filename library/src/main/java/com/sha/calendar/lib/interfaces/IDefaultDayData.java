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

package com.sha.calendar.lib.interfaces;

import com.sha.calendar.lib.utilites.RangeState;

import java.util.Date;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
 interface IDefaultDayData extends IDayData {
     void setDate(Date date);
     void setToday(boolean today);
     void setValue(int value);
     void setCurrentMonth(boolean currentMonth);
     void setSelected(boolean selected);
     void setHighlighted(boolean highlighted);
     void setRangeState(RangeState rangeState);
}
