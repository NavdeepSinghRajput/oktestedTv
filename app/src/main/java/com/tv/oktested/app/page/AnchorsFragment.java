package com.tv.oktested.app.page;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.OnItemViewClickedListener;
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

public  class AnchorsFragment extends GridFragment implements BrowseAnchorView {
        private static final int COLUMNS = 3;
        private final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL;
        private ArrayObjectAdapter mAdapter;
    BrowseAnchorsApiHandling browseAnchorsApiHandling;
    String offset;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setupAdapter();
//            browseAnchorsApiHandling = new BrowseAnchorsApiHandling(getContext(), this);
            callAnchorsApiData();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }


        private void setupAdapter() {
            VerticalGridPresenter presenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_SMALL);
            presenter.setNumberOfColumns(COLUMNS);
            setGridPresenter(presenter);

            AnchorCardsPresenter cardPresenter = new AnchorCardsPresenter();
            mAdapter = new ArrayObjectAdapter(cardPresenter);
            setAdapter(mAdapter);

            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                    if (item instanceof AnchorsListModel) {

                        startActivity(new Intent(getActivity().getBaseContext(), DetailActivity.class).putExtra("anchorModel", ((AnchorsListModel) item)).putExtra("type", "Anchor").putExtra("username", ((AnchorsListModel) item).username));

                    }
                }


            });
        }

   /*     private void loadData() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.grid_example));
            CardRow cardRow = new Gson().fromJson(json, CardRow.class);
            mAdapter.addAll(0, cardRow.getCards());
        }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callAnchorsApiData() {
        if (Helper.isNetworkAvailable(getContext())) {
            showLoader();
            browseAnchorsApiHandling.callAnchorsApi();
        } else {
            showMessage(getString(R.string.please_check_internet_connection));
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
            mAdapter.addAll(0,anchorsResponse.data);
        }
    }
}
