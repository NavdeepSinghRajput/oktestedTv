/*
 * Copyright (c) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tv.oktested.app.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.leanback.app.SearchSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.CursorObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.gson.Gson;
import com.tv.oktested.R;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.app.page.presenter.ExclusiveCardsPresenter;
import com.tv.oktested.app.videoConsumption.VideoActivity;
import com.tv.oktested.card.RecentlyAddedListRow;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;
import com.tv.oktested.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
 * This class demonstrates how to do in-app search
 */
public class SearchFragment extends SearchSupportFragment
        implements SearchSupportFragment.SearchResultProvider,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "SearchFragment";
    private static final boolean FINISH_ON_RECOGNIZER_CANCELED = true;
    private static final int REQUEST_SPEECH = 0x00000010;
    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private String mQuery;
    private final CursorObjectAdapter mVideoCursorAdapter = new CursorObjectAdapter(new SearchViewPresenter());
    ArrayObjectAdapter recentAddedAdapter;
    VideoListResponse videoListResponse;

    private int mSearchLoaderId = 1;
    private boolean mResultsFound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL));
//        mVideoCursorAdapter.setMapper(new VideoCursorMapper());

        setSearchResultProvider(this);
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void callUserSearchApi(String searchString, String offset) {
        if (Helper.isNetworkAvailable(getContext())) {
           /* if (!oldSearchString.equalsIgnoreCase(searchString)) {
                firstTime = true;
                showLoader();*/
            getUserSearchResult(searchString, offset);
           /* } else {
                searchResultRV.setVisibility(View.VISIBLE);
                searchTitleTV.setVisibility(View.VISIBLE);
                searchSuggestionRV.setVisibility(View.GONE);
            }
            oldSearchString = searchString;*/
        } else {
            Toast.makeText(getContext(), "no internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserSearchResult(final String searchString, final String offset) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(getActivity().getBaseContext()).getPreferencesString(AppConstants.ACCESS_TOKEN));
        Call<VideoListResponse> call = ApiClient.getScoopWhoopApi().getUserSearchResult(headerMap, searchString,offset);
        call.enqueue(new Callback<VideoListResponse>() {
            @Override
            public void onResponse(Call<VideoListResponse> call, Response<VideoListResponse> response) {
                new Gson().toJson(response.body());
                videoListResponse = response.body();
                if (videoListResponse != null && videoListResponse.data != null && videoListResponse.data.size() > 0) {
                    if (videoListResponse != null && videoListResponse.data != null && videoListResponse.data.size() > 0) {
                        if (offset.equalsIgnoreCase("1")) {
                            PresenterSelector presenterSelector = new ExclusiveCardsPresenter();
                            recentAddedAdapter = new ArrayObjectAdapter(presenterSelector);
                            Log.e("videoList", "fresh");

                            for (int i = 0; i < videoListResponse.data.size(); i++) {
                                videoListResponse.data.get(i).itemPosition = i;
                                recentAddedAdapter.add(videoListResponse.data.get(i));
                            }
                            mRowsAdapter.clear();
                            HeaderItem headerItem = new HeaderItem(0, searchString);
                            mRowsAdapter.add(new RecentlyAddedListRow(headerItem, recentAddedAdapter, videoListResponse));

                        } else {
                            Log.e("videoList", "pagination");
                            int count = recentAddedAdapter.size();
                            for (int i = 0; i < videoListResponse.data.size(); i++) {
                                videoListResponse.data.get(i).itemPosition = count + i;
                                recentAddedAdapter.add(videoListResponse.data.get(i));
                            }
                            synchronized (recentAddedAdapter) {

                                recentAddedAdapter.notify();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoListResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.getMessage());
            }
        });
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SPEECH:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        setSearchQuery(data, true);
                        break;
                    default:
                        // If recognizer is canceled or failed, keep focus on the search orb
                        if (FINISH_ON_RECOGNIZER_CANCELED) {
                            if (!hasResults()) {

                                getView().findViewById(R.id.lb_search_bar_speech_orb).requestFocus();
                            }
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        loadQuery(newQuery);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadQuery(query);

        return true;
    }

    public boolean hasResults() {
        return mRowsAdapter.size() > 0 && mResultsFound;
    }

    private boolean hasPermission(final String permission) {
        final Context context = getActivity();
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                permission, context.getPackageName());
    }

    private void loadQuery(String query) {
        if (!TextUtils.isEmpty(query) && !query.equals("nil")) {
            mQuery = query;
            callUserSearchApi(query, "1");

//            getLoaderManager().initLoader(mSearchLoaderId++, null, this);
        }
    }

    public void focusOnSearch() {
        getView().findViewById(R.id.lb_search_bar).requestFocus();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String query = mQuery;
        return new CursorLoader(
                getActivity()/*,
                VideoContract.VideoEntry.CONTENT_URI,
                null, // Return all fields.
                VideoContract.VideoEntry.COLUMN_NAME + " LIKE ? OR " +
                        VideoContract.VideoEntry.COLUMN_DESC + " LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"},
                null*/ // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int titleRes;
        if (cursor != null && cursor.moveToFirst()) {
            mResultsFound = true;
            titleRes = R.string.search_results;
        } else {
            mResultsFound = false;
            titleRes = R.string.no_search_results;
        }
        mVideoCursorAdapter.changeCursor(cursor);
        HeaderItem header = new HeaderItem(getString(titleRes, mQuery));
        mRowsAdapter.clear();
        ListRow row = new ListRow(header, mVideoCursorAdapter);
        mRowsAdapter.add(row);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mVideoCursorAdapter.changeCursor(null);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {


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
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
//            Log.e("video", "video" + recentAddedAdapter.size() + "   " + ((DataItem)item).itemPosition + "    " + videoListResponse.next_offset);
            if(item instanceof DataItem){
                Log.e("video", "video" + recentAddedAdapter.size() + "   " + ((DataItem)item).itemPosition + "    " + videoListResponse.next_offset);
                if (!videoListResponse.next_offset.equalsIgnoreCase("-1") && ((recentAddedAdapter.size() - 5) < ((DataItem) item).itemPosition)) {
                    callUserSearchApi(mQuery,videoListResponse.next_offset );
                }
            }
        }
         }

}
