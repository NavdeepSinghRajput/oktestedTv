package com.tv.oktested.header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.tv.oktested.app.page.apihandling.BrowseAnchorView;
import com.tv.oktested.app.page.apihandling.BrowseAnchorsApiHandling;
import com.tv.oktested.app.page.detail.DetailActivity;
import com.tv.oktested.app.page.model.AnchorsListModel;
import com.tv.oktested.app.page.model.AnchorsResponse;
import com.tv.oktested.app.page.presenter.AnchorCardsPresenter;
import com.tv.oktested.utils.Helper;

public class CustomAnchorFragment extends VerticalGridFragment implements BrowseAnchorView {
    private static final String TAG = VerticalGridFragment.class.getSimpleName();
    private static final int NUM_COLUMNS = 4;
    private ArrayObjectAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

     //   setTitle("Anchors");
        setupFragment();
        setupEventListeners();
    }

    private void setupFragment() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
        BrowseAnchorsApiHandling browseAnchorsApiHandling = new BrowseAnchorsApiHandling(getActivity(), this);
        if (Helper.isNetworkAvailable(getActivity())) {
            showLoader();
            browseAnchorsApiHandling.callAnchorsApi();
        } else {
            showMessage(getString(R.string.please_check_internet_connection));
        }

        AnchorCardsPresenter cardPresenter = new AnchorCardsPresenter();
        mAdapter = new ArrayObjectAdapter(cardPresenter);
        setAdapter(mAdapter);


    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            Log.e("anchor", "anchors12");
            if (item instanceof AnchorsListModel) {

                startActivity(new Intent(getActivity().getBaseContext(), DetailActivity.class).putExtra("anchorModel", ((AnchorsListModel) item)).putExtra("type", "Anchor").putExtra("username", ((AnchorsListModel) item).username));

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
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setAnchorsResponse(AnchorsResponse anchorsResponse) {
        if (anchorsResponse != null && anchorsResponse.data != null && anchorsResponse.data.size() > 0) {
            mAdapter.addAll(0, anchorsResponse.data);
        }
    }
}