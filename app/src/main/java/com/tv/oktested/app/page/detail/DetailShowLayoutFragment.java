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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.DetailsFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.tv.oktested.R;
import com.tv.oktested.app.page.customised.DetailsFragmentCustomizatioon;
import com.tv.oktested.app.page.detail.apihandling.ShowDetailView;
import com.tv.oktested.app.page.detail.presenterView.DetailShowCardPresenterView;
import com.tv.oktested.app.page.detail.presenter.ShowDetailPresenter;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.GetUserDataresponse;
import com.tv.oktested.app.page.model.ShowsVideoResponse;
import com.tv.oktested.app.page.presenterView.AllShowCardPresenterView;
import com.tv.oktested.app.page.presenter.EditorRowPresenter;
import com.tv.oktested.app.videoConsumption.VideoActivity;
import com.tv.oktested.utils.DataHolder;
import com.tv.oktested.utils.Helper;

import java.util.ArrayList;

/**
 * Displays a card with more details using a {@link DetailsFragment}.
 */
public class DetailShowLayoutFragment extends DetailsFragmentCustomizatioon implements OnItemViewClickedListener,
        OnItemViewSelectedListener, ShowDetailView {

    public static final String TRANSITION_NAME = "t_for_transition";
    public static final String EXTRA_CARD = "card";
    ArrayObjectAdapter listRowAdapter;
  /*  private static final long ACTION_BUY = 1;
    private static final long ACTION_WISHLIST = 2;
    private static final long ACTION_RELATED = 3;
*/    boolean isFollow = false;
/*
    private Action mActionBuy;
    private Action mActionWishList;
    private Action mActionRelated;*/
    private ArrayObjectAdapter mRowsAdapter;
//    private final DetailsFragmentBackgroundController mDetailsBackground = new DetailsFragmentBackgroundController(this);
    Bitmap mbitmap;
    ArrayList<DataItem> showsVideoResponsess = new ArrayList<>();
    ShowsVideoResponse showsVideoResponses;
    String slug,showName;
    DisplayMetrics mMetrics;
//    ArrayObjectAdapter actionAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slug = getArguments().getString("slug");
        showName = getArguments().getString("showName");
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        setShowFollowIcon(DataHolder.getInstance().getUserDataresponse, slug);
        callShowVideoApi(slug, "0");
        setupEventListeners();
    }

    private void callShowVideoApi(String slug, String offset) {
        ShowDetailPresenter showDetailPresenter = new ShowDetailPresenter(getActivity().getBaseContext(), this);
        if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
            showLoader();
            showDetailPresenter.callShowsVideoApi(slug, offset);
        } else {
            showMessage(getString(R.string.please_check_internet_connection));
        }
    }


   /* int getBitmapFromURL(final String src) {
        //    Bitmap mbitmap;
        Thread t = new Thread() {
            public void run() {
                try {
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    mbitmap = bitmap;

                    // return myBitmap;
                } catch (IOException e) {

                }

            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }
  */ BackgroundManager mBackgroundManager;

    private void setupUi(ShowsVideoResponse showsVideoResponse) {
        showsVideoResponsess.addAll(showsVideoResponse.data);
        EditorRowPresenter detailsOverviewRowPresenter = new EditorRowPresenter(new DetailShowCardPresenterView(slug));
//        EditorRowPresenter detailsOverviewRowPresenter = new EditorRowPresenter(new EditorCardPresenterView());

        // Setup PresenterSelector to distinguish between the different rows.
        ClassPresenterSelector rowPresenterSelector = new ClassPresenterSelector();
        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsOverviewRowPresenter);
        rowPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL));
        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);

        // Setup action and detail row.
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(showsVideoResponse.show_details);
//        detailsOverview.setImageBitmap(getActivity(), mbitmap);
        mRowsAdapter.add(detailsOverview);

        listRowAdapter = new ArrayObjectAdapter(new AllShowCardPresenterView());
        for (int i = 0; i < showsVideoResponse.data.size(); i++) {
            listRowAdapter.add(showsVideoResponse.data.get(i));
        }

        HeaderItem header = new HeaderItem(1, "Videos from "+showName);
        mRowsAdapter.add(new ListRow(header, listRowAdapter));
        setAdapter(mRowsAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startEntranceTransition();
            }
        }, 500);
//        initializeBackground(showsVideoResponse.show_details.onexone_img);
    }

    private void setShowFollowIcon(GetUserDataresponse response, String slug) {
        if (response != null && response.favourite_shows != null && response.favourite_shows.size() > 0) {
            for (int i = 0; i < response.favourite_shows.size(); i++) {
                if (response.favourite_shows.get(i).equalsIgnoreCase(slug)) {
                    isFollow = true;
                    break;
                }
            }
        }
    }

 /*   private void initializeBackground(String imgUrl) {
        mDetailsBackground.enableParallax();
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        Drawable mDefaultBackground = getResources().getDrawable(R.color.background_gradient_start, null);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(mDefaultBackground);

        Glide.with(this)
                .asBitmap()
                .load(imgUrl)
                .apply(options)
                .into(new SimpleTarget<Bitmap>(mMetrics.widthPixels, mMetrics.heightPixels) {
                    @Override
                    public void onResourceReady(
                            Bitmap resource,
                            Transition<? super Bitmap> transition) {
                        mBackgroundManager.setBitmap(resource);
                    }
                });
//        mDetailsBackground.setCoverBitmap(mbitmap);

//        mDetailsBackground.setCoverBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.editor_pick_banner));
    }
*/
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
        if (item instanceof DataItem) {
            Log.e("exclusive", "Exclusive" + showsVideoResponses.data.size() + "   " + ((DataItem) item).itemPosition + "    " + showsVideoResponses.next_offset);
            if (!showsVideoResponses.next_offset.equalsIgnoreCase("-1") && ((showsVideoResponses.data.size() - 5) < ((DataItem) item).itemPosition)) {
                callShowVideoApi(slug, showsVideoResponses.next_offset);
            }
        }

    }

    @Override
    public void setShowsVideoResponse(ShowsVideoResponse showsVideoResponse, String offset) {
        if (showsVideoResponse != null && showsVideoResponse.show_details != null) {
            showsVideoResponses = showsVideoResponse;

            if (offset.equalsIgnoreCase("0")) {
                for (int i = 0; i < showsVideoResponse.data.size(); i++) {
                    showsVideoResponses.data.get(i).itemPosition = i;
                }
//                getBitmapFromURL(showsVideoResponse.show_details.onexone_img);

                setupUi(showsVideoResponses);
            } else {
                int count = listRowAdapter.size();
                for (int i = 0; i < showsVideoResponses.data.size(); i++) {
                    showsVideoResponses.data.get(i).itemPosition = count + i;
                    listRowAdapter.add(showsVideoResponses.data.get(i));
                }
                synchronized (listRowAdapter) {

                    listRowAdapter.notify();
                }
            }

        }
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();

    }
}
