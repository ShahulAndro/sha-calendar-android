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

package com.sha.calendar.lib.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calendar.lib.R;
import com.sha.calendar.lib.adapter.DayDataAdapter;
import com.sha.calendar.lib.model.DayData;
import com.sha.calendar.lib.utilites.RangeState;
import com.sha.calendar.lib.utilites.ShaLocaleUtils;

import java.util.Locale;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public final class DayViewHolder implements View.OnClickListener {

    private boolean canDisplayPreviousNextMonthDays;
    private boolean isRTL;
    private boolean isForceDisplayRTL;

    private DayData dayData;
    private DayDataAdapter.DayAdapterCallback dayAdapterCallback;
    private DayDataAdapter.DayViewOnClickListener dayViewOnClickListener;

    private TextView dayDataTextView;
    private ViewGroup rootView;
    private Context context;

    public DayViewHolder(View view) {
        this.context = view.getContext();
        this.rootView = (ViewGroup) view;
        this.dayDataTextView = view.findViewById(R.id.day_value);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.dayAdapterCallback .onDateClicked(this.dayData);
    }

    public void displayDataInView() {
        applyRTLOnViews();

        if (!this.dayData.isCurrentMonth()) {
            if (this.canDisplayPreviousNextMonthDays) {
                this.rootView.setEnabled(false);
                for (int i = 0; i < this.rootView.getChildCount(); i++) {
                    this.rootView.getChildAt(i).setEnabled(false);
                }
            } else {
                this.rootView.setVisibility(View.INVISIBLE);
            }
        }

        this.dayDataTextView.setText(this.dayData.getValue());

        if (this.dayData.isToday()) {
            this.rootView.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
            this.dayDataTextView.setTextColor(context.getResources().getColor(R.color.white));
        }

        if (this.dayData.isHighlighted()) {
            this.rootView.setBackgroundColor(context.getResources().getColor(R.color.highlighted_day_background));
            this.dayDataTextView.setTextColor(context.getResources().getColor(R.color.day_text_highlighted));
        }

        if (this.dayData.isSelected()) {
            if (this.dayData.getRangeState() == RangeState.MIDDLE ) {
                this.rootView.setBackgroundColor(context.getResources().getColor(R.color.light_blue5));
                this.dayDataTextView.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                this.rootView.setBackgroundColor(context.getResources().getColor(R.color.blue));
                this.dayDataTextView.setTextColor(context.getResources().getColor(R.color.white));
            }
        }
    }

    public void setDayAdapterCallback(DayDataAdapter.DayAdapterCallback dayAdapterCallback) {
        this.dayAdapterCallback = dayAdapterCallback;
    }

    public void setDayViewOnClickListener(DayDataAdapter.DayViewOnClickListener dayViewOnClickListener) {
        this.dayViewOnClickListener = dayViewOnClickListener;
    }

    public void setDayData(DayData dayData) {
        this.dayData = dayData;
    }

    public void setDayOfMonthTextView(TextView textView) {
        this.dayDataTextView = textView;
    }

    public void setDisplayPreviousNextMonthDays(boolean canDisplayPreviousNextMonthDays) {
        this.canDisplayPreviousNextMonthDays = canDisplayPreviousNextMonthDays;
    }

    public void isRTL(boolean isRTL) {
        this.isRTL = isRTL;
    }

    public void setForceDisplayRTL(boolean forceDisplayRTL) {
        isForceDisplayRTL = forceDisplayRTL;
    }

    private void applyRTLOnViews() {
        if(!ShaLocaleUtils.isRTL(Locale.getDefault()) && this.isForceDisplayRTL) {
            for (int i = 0; i < this.rootView.getChildCount(); i++) {
                this.rootView.getChildAt(i).setRotationY(ShaLocaleUtils.rotateYForView());
            }
        }
    }
}

