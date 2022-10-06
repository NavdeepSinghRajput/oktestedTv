package com.tv.oktested.header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.leanback.app.VerticalGridFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;

import com.tv.oktested.R;
import com.tv.oktested.app.page.apihandling.BrowseShowsApiHandling;
import com.tv.oktested.app.page.apihandling.BrowseShowsView;
import com.tv.oktested.app.page.detail.DetailActivity;
import com.tv.oktested.app.page.model.ShowsListModel;
import com.tv.oktested.app.page.model.ShowsResponse;
import com.tv.oktested.app.page.presenter.ShowCardsPresenter;
import com.tv.oktested.utils.Helper;

public class CustomShowsFragment extends VerticalGridFragment implements BrowseShowsView {
    private static final String TAG = VerticalGridFragment.class.getSimpleName();
    private static final int NUM_COLUMNS = 4;
    private ArrayObjectAdapter mAdapter;
    String offset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //  setTitle("Shows");
        setupFragment();
        setupEventListeners();
    }

    private void setupFragment() {

        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
        BrowseShowsApiHandling browseShowsApiHandling = new BrowseShowsApiHandling(getActivity(), this);
        if (Helper.isNetworkAvailable(getActivity())) {
            showLoader();
            browseShowsApiHandling.callShowsApi("0");
        } else {
            showMessage(getString(R.string.please_check_internet_connection));
        }


        ShowCardsPresenter cardPresenter = new ShowCardsPresenter();
        mAdapter = new ArrayObjectAdapter(cardPresenter);
        setAdapter(mAdapter);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof ShowsListModel) {
                startActivity(new Intent(getActivity().getBaseContext(), DetailActivity.class).putExtra("type", "Shows").putExtra("showName", ((ShowsListModel) item).topic_name).putExtra("slug", ((ShowsListModel) item).topic_slug));
            }
        }
    }

    private class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
        }
    }

    @Override
    public void setShowsResponse(ShowsResponse showsResponse) {
        if (showsResponse != null && showsResponse.data != null && showsResponse.data.size() > 0) {
            mAdapter.addAll(0, showsResponse.data);
//            dataLoaded = true;
            offset = showsResponse.next_offset;
//            browseShowAdapter.setMoreShowsData(showsResponse.data);
        } else {

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

    }
}
