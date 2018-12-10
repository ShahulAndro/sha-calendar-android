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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calendar.lib.R;
import com.sha.calendar.lib.interfaces.IDayViewAdapterCallback;
import com.sha.calendar.lib.model.DayData;
import com.sha.calendar.lib.model.MonthData;
import com.sha.calendar.lib.utilites.ShaCalendarUtils;
import com.sha.calendar.lib.viewholder.MonthRecyclerViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class MonthRecyclerViewAdapter extends RecyclerView.Adapter<MonthRecyclerViewHolder> implements DayDataAdapter.DayAdapterCallback {

    private boolean isForceDisplayRTL;
    private Locale locale;
    private List<MonthData> monthDataList;
    private Map<String, MonthData> monthDataMap = new HashMap<>();

    private MonthRecyclerViewHolder viewHolder;
    private IDayViewAdapterCallback dayViewAdapterCallback;
    private MonthRecyclerViewAdapterCallback monthRecyclerViewAdapterCallback;

    private LayoutInflater inflater;
    private Context context;

    public MonthRecyclerViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(MonthRecyclerViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MonthRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = inflater.inflate(R.layout.month_days_grid_view, null, false);
        this.viewHolder = new MonthRecyclerViewHolder(this.context, itemView);
        this.viewHolder.setCallback(this);
        this.viewHolder.setLocale(this.locale);
        this.viewHolder.setForceDisplayRTL(this.isForceDisplayRTL);
        this.viewHolder.setDayViewAdapterCallback(this.dayViewAdapterCallback);
        return this.viewHolder;
    }

    @Override
    public void onBindViewHolder(MonthRecyclerViewHolder monthViewHolder, int position) {
        MonthData monthData = this.monthDataList.get(position);
        monthViewHolder.bindView(monthData);
    }

    @Override
    public int getItemCount() {
        if (this.monthDataList == null || this.monthDataList.size() < 1) {
            return 0;
        }

        return this.monthDataList.size();
    }

    @Override
    public void onDateClicked(DayData dayData) {
        MonthData monthData = this.monthDataMap.get(ShaCalendarUtils.getMonthKey(dayData.getDate()));
        monthRecyclerViewAdapterCallback.onDaySelectedInMonth(dayData);
    }

    public void setCallback(MonthRecyclerViewAdapterCallback monthRecyclerViewAdapterCallback) {
        this.monthRecyclerViewAdapterCallback = monthRecyclerViewAdapterCallback;
    }

    public List<MonthData> getMonthDataList() {
        return monthDataList;
    }

    public void setMonthDataList(List<MonthData> monthDataList) {
        this.monthDataList = monthDataList;
        updatedMonthsDataListWithMap();
    }

    public void setDayViewAdapterCallback(IDayViewAdapterCallback dayViewAdapterCallback) {
        this.dayViewAdapterCallback = dayViewAdapterCallback;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setForceDisplayRTL(boolean forceDisplayRTL) {
        isForceDisplayRTL = forceDisplayRTL;
    }

    private void updatedMonthsDataListWithMap() {
        for (int i = 0; i < this.monthDataList.size(); i++) {
            MonthData monthData = this.monthDataList.get(i);
            monthDataMap.put(ShaCalendarUtils.getMonthKey(monthData.getDate()), monthData);
        }
    }

    public interface MonthRecyclerViewAdapterCallback {
        void onDaySelectedInMonth(DayData dayData);
    }
}
