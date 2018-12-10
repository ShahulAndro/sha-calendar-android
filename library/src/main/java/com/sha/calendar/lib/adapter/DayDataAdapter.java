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

package com.sha.calendar.lib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sha.calendar.lib.interfaces.IDayView;
import com.sha.calendar.lib.interfaces.IDayViewAdapterCallback;
import com.sha.calendar.lib.model.DayData;
import com.sha.calendar.lib.utilites.ShaLocaleUtils;
import com.sha.calendar.lib.viewholder.DayViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class DayDataAdapter extends BaseAdapter implements Cloneable{

    private boolean canDisplayPreviousNextMonthDays;
    private boolean isRTL;
    private boolean isForceDisplayRTL;

    private List<DayData> dayDataList = new ArrayList<>();
    private DayAdapterCallback dayAdapterCallback;
    private IDayViewAdapterCallback dayViewAdapterCallback;

    private Context context;

    private DayDataAdapter() {

    }

    public DayDataAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.dayDataList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0) {
            return null;
        }

        return this.dayDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        DayViewHolder viewHolder;

        if (convertView == null) {
            IDayView dayView = this.dayViewAdapterCallback.onCreateDayView();
            convertView = dayView.getView();
            viewHolder = new DayViewHolder(convertView);
            viewHolder.setDayOfMonthTextView(dayView.getDayValueTextView());
            viewHolder.setDayAdapterCallback(this.dayAdapterCallback);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DayViewHolder) convertView.getTag();
        }

        DayData dayData = this.dayDataList.get(position);
        viewHolder.setDayData(dayData);
        viewHolder.setDisplayPreviousNextMonthDays(this.canDisplayPreviousNextMonthDays);
        viewHolder.isRTL(this.isRTL);
        viewHolder.setForceDisplayRTL(this.isForceDisplayRTL);

        viewHolder.displayDataInView();

        this.dayViewAdapterCallback.onUpDateDayView(convertView, dayData);
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public DayDataAdapter clone() throws CloneNotSupportedException {
        DayDataAdapter dayDataAdapter = (DayDataAdapter) super.clone();
        return dayDataAdapter;
    }

    public void setCallback(DayAdapterCallback dayAdapterCallback) {
        this.dayAdapterCallback = dayAdapterCallback;
    }

    public void setDayViewAdapterCallback(IDayViewAdapterCallback dayViewAdapterCallback) {
        this.dayViewAdapterCallback = dayViewAdapterCallback;
    }

    public void isRTL(Locale locale) {
        this.isRTL = ShaLocaleUtils.isRTL(locale);
    }

    public void setForceDisplayRTL(boolean forceDisplayRTL) {
        this.isForceDisplayRTL = forceDisplayRTL;
    }

    public boolean canDisplayPreviousNextMonthDays() {
        return canDisplayPreviousNextMonthDays;
    }

    public void setDisplayPreviousNextMonthDays(boolean canDisplayPreviousNextMonthDays) {
        this.canDisplayPreviousNextMonthDays = canDisplayPreviousNextMonthDays;
    }

    public List<DayData> getDayDataList() {
        return dayDataList;
    }

    public void setDayDataList(List<DayData> dayDataList) {
        this.dayDataList = dayDataList;
    }

    public final class DayViewOnClickListener implements DayViewOnClickCallback {
        @Override
        public void onClick(View view, DayData dayData) {
            dayData.setSelected(true);
            dayAdapterCallback.onDateClicked(dayData);
        }
    }

    public interface DayAdapterCallback {
        void onDateClicked(DayData dayData);
    }

    public interface DayViewOnClickCallback {
        void onClick(View view, DayData dayData);
    }
}
