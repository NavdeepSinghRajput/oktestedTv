package com.tv.oktested.app.page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.RowsFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tv.oktested.app.page.detail.DetailActivity;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.AnchorsListModel;
import com.tv.oktested.app.page.model.AnchorsResponse;
import com.tv.oktested.app.page.model.GetUserDataresponse;
import com.tv.oktested.app.page.model.ShowsListModel;
import com.tv.oktested.app.page.model.ShowsResponse;
import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.app.page.presenter.AnchorCardsPresenter;
import com.tv.oktested.app.page.presenter.ExclusiveCardsPresenter;
import com.tv.oktested.app.page.presenterView.NoDataPresenterView;
import com.tv.oktested.app.page.presenter.NoDataRowPresenter;
import com.tv.oktested.app.page.presenter.ShowCardsPresenter;
import com.tv.oktested.app.videoConsumption.VideoActivity;
import com.tv.oktested.card.AnchorListRow;
import com.tv.oktested.card.RecentlyAddedListRow;
import com.tv.oktested.card.ShowListRow;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.utils.DataHolder;
import com.tv.oktested.utils.Helper;
import com.tv.oktested.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends RowsFragment implements OnItemViewClickedListener,
        OnItemViewSelectedListener {

    ArrayObjectAdapter mRowsAdapter;
    ClassPresenterSelector rowPresenterSelector;

    public FavouriteFragment() {

        /*rowPresenterSelector = new ClassPresenterSelector();
        listRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        rowPresenterSelector.addClassPresenter(Row.class, listRowPresenter);
        */
        rowPresenterSelector = new ClassPresenterSelector();
        ListRowPresenter listRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL, true);
        listRowPresenter.setKeepChildForeground(true);
        rowPresenterSelector.addClassPresenter(Row.class, listRowPresenter);
        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);
        setAdapter(mRowsAdapter);
        setupEventListeners();

    }

    private void setupEventListeners() {
        setOnItemViewSelectedListener(this);
        setOnItemViewClickedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        //    fragment.showTitleView(false);
        callUserDataApi();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void callUserDataApi() {
        if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
            getUserData(DataHolder.getInstance().getUserDataresponse);
        } else {
            /*showMessage(getString(R.string.please_check_internet_connection));*/
        }
    }


    private void getUserData(GetUserDataresponse getUserResponse) {
        if (getUserResponse != null) {
            mRowsAdapter.clear();
            if (getUserResponse.favourite != null && getUserResponse.favourite.size() > 0) {
                callVideoListApi(getUserResponse.favourite);
            }
            if (getUserResponse.favourite_shows != null && getUserResponse.favourite_shows.size() > 0) {
                callShowsListApi(getUserResponse.favourite_shows);
            }
            if (getUserResponse.follow != null && getUserResponse.follow.size() > 0) {
                callAnchorListApi(getUserResponse.follow);
            }
            if(getUserResponse.follow.size()==0&&getUserResponse.favourite_shows.size()==0&&getUserResponse.favourite.size()==0){
                noData();
            }
        }else{
            noData();
        }
    }

    void callAnchorListApi(ArrayList<String> actorList) {
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(actorList).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("castcrews", myCustomArray);
        Call<AnchorsResponse> anchorsResponseCall = ApiClient.getScoopWhoopActorApi().getFavouriteAnchorList(jsonObject.toString());
        anchorsResponseCall.enqueue(new Callback<AnchorsResponse>() {
            @Override
            public void onResponse(Call<AnchorsResponse> call, Response<AnchorsResponse> response) {
                AnchorsResponse anchorDetailResponse = response.body();
                if (anchorDetailResponse != null && anchorDetailResponse.data != null && anchorDetailResponse.data.size() > 0) {
                    PresenterSelector presenterSelector = new AnchorCardsPresenter();
                    ArrayObjectAdapter anchorsResponseAdapter = new ArrayObjectAdapter(presenterSelector);
                    for (int i = 0; i < anchorDetailResponse.data.size(); i++) {
//                        anchorsResponse.data.get(i).itemPosition = i;
                        anchorsResponseAdapter.add(anchorDetailResponse.data.get(i));
                    }
                    HeaderItem headerItem = new HeaderItem(0, "Favourite Anchor");
                    mRowsAdapter.add(new AnchorListRow(headerItem, anchorsResponseAdapter, anchorDetailResponse));

                }
            }

            @Override
            public void onFailure(Call<AnchorsResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();
                Log.e("Failure", "callAnchorListApi" + t.getMessage());

            }
        });
    }

    void callVideoListApi(ArrayList<String> videoList) {
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(videoList).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("videos", myCustomArray);
        Call<VideoListResponse> call = ApiClient.getScoopWhoopApi().getFavouriteVideoList(jsonObject.toString());
        call.enqueue(new Callback<VideoListResponse>() {
            @Override
            public void onResponse(Call<VideoListResponse> call, Response<VideoListResponse> response) {
                VideoListResponse videoListResponse = response.body();
                if (videoListResponse != null && videoListResponse.data != null && videoListResponse.data.size() > 0) {
                    PresenterSelector presenterSelector = new ExclusiveCardsPresenter();
                    ArrayObjectAdapter recentAddedAdapter = new ArrayObjectAdapter(presenterSelector);
                    Log.e("videoList", "fresh");

                    for (int i = 0; i < videoListResponse.data.size(); i++) {
                        videoListResponse.data.get(i).itemPosition = i;
                        recentAddedAdapter.add(videoListResponse.data.get(i));
                    }
                    HeaderItem headerItem = new HeaderItem(2, "Favourite Video");
                    mRowsAdapter.add(new RecentlyAddedListRow(headerItem, recentAddedAdapter, videoListResponse));
                }
            }

            @Override
            public void onFailure(Call<VideoListResponse> call, Throwable t) {
                Log.e("Failure", "callVideoListApi" + t.getMessage());
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();

            }
        });
    }


    void callShowsListApi(ArrayList<String> favourite_shows) {
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(favourite_shows).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("shows", myCustomArray);
        Call<ShowsResponse> showsResponseCall = ApiClient.getScoopWhoopApi().getFavouriteShowsList(jsonObject.toString());
        showsResponseCall.enqueue(new Callback<ShowsResponse>() {
            @Override
            public void onResponse(Call<ShowsResponse> call, Response<ShowsResponse> response) {
                ShowsResponse showsResponse = response.body();
                if (showsResponse != null && showsResponse.data != null && showsResponse.data.size() > 0) {

                    PresenterSelector presenterSelector = new ShowCardsPresenter();
                    ArrayObjectAdapter showResponseAdapter = new ArrayObjectAdapter(presenterSelector);
                    for (int i = 0; i < showsResponse.data.size(); i++) {
//                    showsResponse.data.get(i).itemPosition = i;
                        showResponseAdapter.add(showsResponse.data.get(i));
                    }
                    HeaderItem headerItem = new HeaderItem(1, "Favourite Shows");
                    mRowsAdapter.add(new ShowListRow(headerItem, showResponseAdapter, showsResponse));
                }
            }

            @Override
            public void onFailure(Call<ShowsResponse> call, Throwable t) {
                Log.e("Failure", "callShowsListApi" + t.getMessage());
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCustomPadding();
    }

    public void noData() {
        mRowsAdapter.clear();
        DetailsOverviewRow row = new DetailsOverviewRow("editorPickResponse");
        NoDataRowPresenter detailsOverviewRowPresenter = new NoDataRowPresenter(new NoDataPresenterView());
        mRowsAdapter.add(row);
        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsOverviewRowPresenter);
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof ShowsListModel) {
            if (item instanceof ShowsListModel) {
                startActivity(new Intent(getActivity().getBaseContext(), DetailActivity.class).putExtra("type", "Shows").putExtra("slug", ((ShowsListModel) item).topic_slug));
                Toast.makeText(getActivity(), "Implement click handler", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (item instanceof DataItem) {
            DataItem dataItem = (DataItem) item;
            ArrayList<DataItem> dataItems = new ArrayList<>();
            dataItems.add(dataItem);
            Intent intent = new Intent(getActivity().getBaseContext(), VideoActivity.class);
            intent.putParcelableArrayListExtra("dataItemList", dataItems);
            intent.putExtra("position", 0);
            startActivity(intent);
        } else if (item instanceof AnchorsListModel) {

            startActivity(new Intent(getActivity().getBaseContext(), DetailActivity.class).putExtra("anchorModel", ((AnchorsListModel) item)).putExtra("type", "Anchor").putExtra("username", ((AnchorsListModel) item).username));

        }
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

    }


    private void setCustomPadding() {
        getView().setPadding(Utils.convertDpToPixel(getActivity(), -24), Utils.convertDpToPixel(getActivity(), 200), Utils.convertDpToPixel(getActivity(),48), 0);
    }

    public void refresh() {
        getView().setPadding(Utils.convertDpToPixel(getActivity(), -24), Utils.convertDpToPixel(getActivity(),200), Utils.convertDpToPixel(getActivity(), 48), 0);
    }
}

