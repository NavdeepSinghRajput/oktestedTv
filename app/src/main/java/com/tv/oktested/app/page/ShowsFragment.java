package com.tv.oktested.app.page;

import android.content.Intent;
import android.os.Bundle;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.OnItemViewClickedListener;
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

public class ShowsFragment extends GridFragment implements BrowseShowsView {
    private static final int COLUMNS = 4;
    private final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL;
    private ArrayObjectAdapter mAdapter;
    BrowseShowsApiHandling browseShowsApiHandling;
    String offset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAdapter();
        browseShowsApiHandling = new BrowseShowsApiHandling(getContext(), this);
        callShowsApiData();
        getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
    }


    private void setupAdapter() {
        VerticalGridPresenter presenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        presenter.setNumberOfColumns(COLUMNS);
        setGridPresenter(presenter);

        ShowCardsPresenter cardPresenter = new ShowCardsPresenter();
        mAdapter = new ArrayObjectAdapter(cardPresenter);
        setAdapter(mAdapter);

        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof ShowsListModel) {
                    startActivity(new Intent(getActivity().getBaseContext(), DetailActivity.class).putExtra("type", "Shows").putExtra("showName", ((ShowsListModel) item).topic_name).putExtra("slug", ((ShowsListModel) item).topic_slug));
                }
            }
        });
    }


    private void callShowsApiData() {
        if (Helper.isNetworkAvailable(getActivity())) {
            showLoader();
            browseShowsApiHandling.callShowsApi("0");
        } else {
            showMessage(getString(R.string.please_check_internet_connection));
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
