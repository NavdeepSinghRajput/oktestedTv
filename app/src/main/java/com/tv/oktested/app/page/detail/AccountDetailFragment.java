/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tv.oktested.app.page.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.leanback.app.DetailsFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.tv.oktested.R;
import com.tv.oktested.app.page.customised.DetailsFragmentCustomizatioon;
import com.tv.oktested.app.page.detail.presenterView.AccountCardPresenterView;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.ShowsVideoResponse;
import com.tv.oktested.app.page.presenter.SettingRowPresenter;
import com.tv.oktested.app.videoConsumption.VideoActivity;

import java.util.ArrayList;

/**
 * Displays a card with more details using a {@link DetailsFragment}.
 */
public class AccountDetailFragment extends DetailsFragmentCustomizatioon implements OnItemViewClickedListener,
        OnItemViewSelectedListener {

    ArrayObjectAdapter listRowAdapter;
    boolean isFollow = false;
    private ArrayObjectAdapter mRowsAdapter;
    ArrayList<DataItem> showsVideoResponsess = new ArrayList<>();
    ShowsVideoResponse showsVideoResponses;
    DisplayMetrics mMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        setupEventListeners();
        setupUi();

    }



    private void setupUi() {
        SettingRowPresenter detailsOverviewRowPresenter = new SettingRowPresenter(new AccountCardPresenterView(getActivity()));
        ClassPresenterSelector rowPresenterSelector = new ClassPresenterSelector();
        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsOverviewRowPresenter);
        rowPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL));
        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow("s");
        mRowsAdapter.add(detailsOverview);
        setAdapter(mRowsAdapter);
    }

    private void setupEventListeners() {
        setOnItemViewSelectedListener(this);
        setOnItemViewClickedListener(this);
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

        if (item instanceof DataItem) {
            DataItem dataItem = (DataItem) item;
            ArrayList<DataItem> dataItems = new ArrayList<>();
            dataItems.add(dataItem);
            Intent intent = new Intent(getActivity().getBaseContext(), VideoActivity.class);
            intent.putParcelableArrayListExtra("dataItemList", dataItems);
            intent.putExtra("position", 0);
            startActivity(intent);

        }
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (mRowsAdapter.indexOf(row) > 0) {
            int backgroundColor = getResources().getColor(R.color.detail_view_related_background);
            getView().setBackgroundColor(backgroundColor);
        } else {
            getView().setBackground(null);
        }
    }



}
