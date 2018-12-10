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

package com.sha.calendar.lib.viewlistener;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
public abstract class RecyclerViewEndlessScrollListener extends RecyclerView.OnScrollListener {

    private boolean loading = false;
    private boolean isUserScrolling = false;

    private int previousTotal = 0;
    private int totalItemCount;

    private LinearLayoutManager mLinearLayoutManager;

    public RecyclerViewEndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState ==  RecyclerView.SCROLL_STATE_DRAGGING) {
            this.isUserScrolling = true;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = mLinearLayoutManager.getItemCount();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (isUserScrolling ) {
            int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (pastVisibleItems  == 0) {
                onLoadMorePrevious();
                isUserScrolling = false;
                return;
            }
        }

        int pos = mLinearLayoutManager.findFirstVisibleItemPosition();
        if ((mLinearLayoutManager.getItemCount() - pos) <= 2) {
            onLoadMoreNext();
            loading = true;
        }
    }

    public abstract void onLoadMoreNext();
    public abstract void onLoadMorePrevious();
}
