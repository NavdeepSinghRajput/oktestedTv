package com.tv.oktested.app.page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import com.tv.oktested.R;
import com.tv.oktested.app.page.apihandling.HomeApiHandling;
import com.tv.oktested.app.page.apihandling.HomeViewResponse;
import com.tv.oktested.app.page.detail.DetailActivity;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.AnchorsListModel;
import com.tv.oktested.app.page.model.AnchorsResponse;
import com.tv.oktested.app.page.model.AppExclusiveResponse;
import com.tv.oktested.app.page.model.EditorPickModel;
import com.tv.oktested.app.page.model.HomeStructureModel;
import com.tv.oktested.app.page.model.HomeStructuteListModel;
import com.tv.oktested.app.page.model.ShowsListModel;
import com.tv.oktested.app.page.model.ShowsResponse;
import com.tv.oktested.app.page.model.ShowsVideoResponse;
import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.app.page.presenter.AnchorCardsPresenter;
import com.tv.oktested.app.page.presenterView.EditorCardPresenterView;
import com.tv.oktested.app.page.presenter.EditorRowPresenter;
import com.tv.oktested.app.page.presenter.ExclusiveCardsPresenter;
import com.tv.oktested.app.page.presenter.ShowCardsPresenter;
import com.tv.oktested.app.videoConsumption.VideoActivity;
import com.tv.oktested.card.AnchorListRow;
import com.tv.oktested.card.ExclusiveListRow;
import com.tv.oktested.card.RecentlyAddedListRow;
import com.tv.oktested.card.ShowListRow;
import com.tv.oktested.card.ShowsVideoListRow;
import com.tv.oktested.model.HeaderItemModel;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.Helper;
import com.tv.oktested.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends RowsFragment implements HomeViewResponse {
    //    private final ArrayObjectAdapter mRowsAdapter;
    HomeApiHandling homeApiHandling;
    private ArrayList<HomeStructuteListModel> homeStructureAL;
    private Map<Integer, Object> sectionMap = new HashMap<>();
    ArrayObjectAdapter showResponseAdapter, appExclusiveAdapter, recentAddedAdapter, anchorsResponseAdapter;
    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;
    int homeStructureSize;
    ArrayObjectAdapter mRowsAdapter;
    ClassPresenterSelector rowPresenterSelector;
    AppExclusiveResponse appExclusiveResponse;
    VideoListResponse videoListResponse;
    ShowsResponse showsResponse;
    AnchorsResponse anchorsResponse;
    ShowsVideoResponse showVideoResponse;
    Map<String, ArrayObjectAdapter> moreShows = new HashMap<>();
    Map<String, ShowsVideoResponse> moreShowsResponse = new HashMap<>();
    ArrayList<String> slugShowsNames = new ArrayList<>();

    ArrayObjectAdapter arrayObjectAdapter;

    public HomeFragment() {
        rowPresenterSelector = new ClassPresenterSelector();
        ListRowPresenter listRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL, true);
        listRowPresenter.setKeepChildForeground(true);
        rowPresenterSelector.addClassPresenter(Row.class, listRowPresenter);
        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);
        setAdapter(mRowsAdapter);

        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                try {
                    rowViewHolder.getRowObject();
                    Log.e("navi", "navi-------------" + rowViewHolder.getRow().getHeaderItem().getName() + "  " + row.getHeaderItem().getId() + "      " + getMainFragmentRowsAdapter().getSelectedPosition());
                    if (item instanceof AnchorsListModel) {
                        Log.e("Anchors", "anchors" + anchorsResponseAdapter.size() + "   " + ((AnchorsListModel) item).itemPosition + "    " + anchorsResponse.next_offset);
                        if (!anchorsResponse.next_offset.equalsIgnoreCase("-1") && ((anchorsResponseAdapter.size() - 5) < ((AnchorsListModel) item).itemPosition)) {
                            callAnchorsApi(anchorsResponse.next_offset, 0);
                        }

                    } else if (item instanceof ShowsListModel) {
                        Log.e("Shows", "shows" + showResponseAdapter.size() + "   " + ((ShowsListModel) item).itemPosition + "    " + showsResponse.next_offset);
                        if (!showsResponse.next_offset.equalsIgnoreCase("-1") && ((showResponseAdapter.size() - 5) < ((ShowsListModel) item).itemPosition)) {
                            callShowsApi(appExclusiveResponse.next_offset, 0);
                        }
                    } else if (item instanceof DataItem) {
                        if (row.getHeaderItem().getName().equalsIgnoreCase("Exclusive")) {
                            Log.e("exclusive", "Exclusive" + appExclusiveAdapter.size() + "   " + ((DataItem) item).itemPosition + "    " + appExclusiveResponse.next_offset);
                            if (!appExclusiveResponse.next_offset.equalsIgnoreCase("-1") && ((appExclusiveAdapter.size() - 5) < ((DataItem) item).itemPosition)) {
                                callAppExclusiveApi(appExclusiveResponse.next_offset, 0);
                            }
                        } else if (row.getHeaderItem().getName().equalsIgnoreCase("Videos")) {
                            Log.e("video", "video" + recentAddedAdapter.size() + "   " + ((DataItem) item).itemPosition + "    " + videoListResponse.next_offset);
                            if (!videoListResponse.next_offset.equalsIgnoreCase("-1") && ((recentAddedAdapter.size() - 5) < ((DataItem) item).itemPosition)) {
                                callRecentlyAddedApi(videoListResponse.next_offset, 0);
                            }
                        } else {
                            for (int i = 0; i < slugShowsNames.size(); i++) {
                                String rowheader = row.getHeaderItem().getName();
                                if (slugShowsNames.get(i).equalsIgnoreCase(rowheader)) {

                                    Log.e("moreShows", rowheader + "           " + moreShowsResponse.get(rowheader).next_offset);
                                    arrayObjectAdapter = moreShows.get(row.getHeaderItem().getName());
                                    if (!moreShowsResponse.get(rowheader).next_offset.equalsIgnoreCase("-1") && ((arrayObjectAdapter.size() - 5) < ((DataItem) item).itemPosition)) {
                                        callShowsVideoApi(moreShowsResponse.get(rowheader).next_offset, 0, moreShowsResponse.get(rowheader).show_details.topic_slug);
                                    }
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("selectionFailure", e.getMessage());
                }
            }
        });
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(
                    Presenter.ViewHolder itemViewHolder,
                    Object item,
                    RowPresenter.ViewHolder rowViewHolder,
                    Row row) {

                if (item instanceof ShowsListModel) {
                    if (item instanceof ShowsListModel) {
                        startActivity(new Intent(getActivity().getBaseContext(), DetailActivity.class).putExtra("type", "Shows").putExtra("showName", ((ShowsListModel) item).topic_name).putExtra("slug", ((ShowsListModel) item).topic_slug));
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
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeApiHandling = new HomeApiHandling(getActivity().getBaseContext(), this);
        /*  BrowseFragment.FragmentHost fragment =*/
//        getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        //    fragment.showTitleView(false);
         callHomeStructureApi();

    }

    private void callHomeStructureApi() {
        if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
            homeApiHandling.callHomeStructure();
        } else {
            Toast.makeText(getActivity().getBaseContext(), "no internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCustomPadding();

    }

    @Override
    public void setHomeStructureResponse(HomeStructureModel homeStructureResponse) {
        if (homeStructureResponse != null && homeStructureResponse.data != null && homeStructureResponse.data.size() > 0) {
            Log.e("size", homeStructureResponse.data.size() + "dd");
            HomeStructuteListModel homeStructuteListModel = new HomeStructuteListModel();
            homeStructuteListModel.section_type = AppConstants.SECTION_EDITOR_PICK_LAYOUT;
            homeStructureResponse.data.add(0, homeStructuteListModel);
            homeStructureAL = homeStructureResponse.data;
            for (int i = 0; i < homeStructureResponse.data.size(); i++) {
                if (homeStructureResponse.data.get(i).section_type.equalsIgnoreCase(AppConstants.SECTION_EDITOR_PICK_LAYOUT)) {
                    showLoader();
                    callEditorPickApi();
                } else if (homeStructureResponse.data.get(i).section_type.equalsIgnoreCase(AppConstants.SECTION_BANNER_LAYOUT)) {
                    sectionMap.put(AppConstants.SECTION_BANNER, homeStructureResponse.data.get(i).value);
                } else if (homeStructureResponse.data.get(i).section_type.equalsIgnoreCase(AppConstants.SECTION_APP_EXCLUSIVE_LAYOUT)) {
                    showLoader();
                    callAppExclusiveApi("0", i - 1);
                } else if (homeStructureResponse.data.get(i).section_type.equalsIgnoreCase(AppConstants.SECTION_RECENTLY_ADDED_LAYOUT)) {
                    showLoader();
                    callRecentlyAddedApi("0", i - 1);
                } else if (homeStructureResponse.data.get(i).section_type.equalsIgnoreCase(AppConstants.SECTION_SHOWS_LAYOUT)) {
                    showLoader();
                    callShowsApi("0", i - 1);
                } else if (homeStructureResponse.data.get(i).section_type.equalsIgnoreCase(AppConstants.SECTION_ANCHORS_LAYOUT)) {
                    showLoader();
                    callAnchorsApi("0", i - 1);
                } else if (homeStructureResponse.data.get(i).section_type.equalsIgnoreCase(AppConstants.SECTION_MORE_SHOWS_LAYOUT)) {
                    showLoader();
                    callShowsVideoApi("0", i - 1, homeStructureResponse.data.get(i).value.slug);
                }
            }
        }
    }

    private void callEditorPickApi() {
        Thread t = new Thread() {
            public void run() {
                Call<EditorPickModel> call = ApiClient.getScoopWhoopApi().getEditorPickVideo();
                try {
                    Response<EditorPickModel> modelResponse = call.execute();
                    EditorPickModel editorPickResponse = modelResponse.body();
                    if (editorPickResponse != null && editorPickResponse.data != null) {
                        sectionMap.put(AppConstants.SECTION_EDITOR_PICK, editorPickResponse.data);
                        DetailsOverviewRow row = new DetailsOverviewRow(editorPickResponse);
                        EditorRowPresenter detailsOverviewRowPresenter = new EditorRowPresenter(new EditorCardPresenterView());
                        mRowsAdapter.add(row);
                        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsOverviewRowPresenter);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("error", e.getMessage());
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }

    }

    public void callAppExclusiveApi(final String offset, final int j) {
//        Thread t = new Thread() {
//            public void run() {
        Call<AppExclusiveResponse> call = ApiClient.getScoopWhoopApi().getAppExclusiveVideoList(offset);
        call.enqueue(new Callback<AppExclusiveResponse>() {
            @Override
            public void onResponse(Call<AppExclusiveResponse> call, Response<AppExclusiveResponse> response) {
                if (appExclusiveResponse != null && appExclusiveResponse.data != null && appExclusiveResponse.data.size() > 0) {
                    if (offset.equalsIgnoreCase("0")) {
                        PresenterSelector presenterSelector = new ExclusiveCardsPresenter();
                        appExclusiveAdapter = new ArrayObjectAdapter(presenterSelector);
                        for (int i = 0; i < appExclusiveResponse.data.size(); i++) {
                            appExclusiveResponse.data.get(i).itemPosition = i;
                            appExclusiveAdapter.add(appExclusiveResponse.data.get(i));
                        }
                        HeaderItem headerItem = new HeaderItem(j, "Exclusive");
                        mRowsAdapter.add(new ExclusiveListRow(headerItem, appExclusiveAdapter, appExclusiveResponse));

                    } else {
                        int count = appExclusiveAdapter.size();
                        for (int i = 0; i < appExclusiveResponse.data.size(); i++) {
                            appExclusiveResponse.data.get(i).itemPosition = count + i;
                            appExclusiveAdapter.add(appExclusiveResponse.data.get(i));
                        }
                        synchronized (appExclusiveAdapter) {

                            appExclusiveAdapter.notify();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<AppExclusiveResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callRecentlyAddedApi(final String offset, final int j) {
//        Thread t = new Thread() {
//            public void run() {
        Call<VideoListResponse> call = ApiClient.getScoopWhoopApi().getRecentlyAddedVideoList(offset);
        call.enqueue(new Callback<VideoListResponse>() {
            @Override
            public void onResponse(Call<VideoListResponse> call, Response<VideoListResponse> response) {
                videoListResponse = response.body();
                sectionMap.put(AppConstants.SECTION_RECENTLY_ADDED, videoListResponse);
                if (videoListResponse != null && videoListResponse.data != null && videoListResponse.data.size() > 0) {
                    sectionMap.put(AppConstants.SECTION_APP_EXCLUSIVE, videoListResponse);
                    if (offset.equalsIgnoreCase("0")) {
                        PresenterSelector presenterSelector = new ExclusiveCardsPresenter();
                        recentAddedAdapter = new ArrayObjectAdapter(presenterSelector);
                        Log.e("videoList", "fresh");

                        for (int i = 0; i < videoListResponse.data.size(); i++) {
                            videoListResponse.data.get(i).itemPosition = i;
                            recentAddedAdapter.add(videoListResponse.data.get(i));
                        }
                        HeaderItem headerItem = new HeaderItem(j, "Latest Videos");
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

            @Override
            public void onFailure(Call<VideoListResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void callShowsApi(final String offset, final int j) {

        Call<ShowsResponse> call = ApiClient.getScoopWhoopOldApi().getShowsList(offset);
        call.enqueue(new Callback<ShowsResponse>() {
            @Override
            public void onResponse(Call<ShowsResponse> call, Response<ShowsResponse> response) {
                showsResponse = response.body();
                if (showsResponse != null && showsResponse.data != null && showsResponse.data.size() > 0) {
                    if (offset.equalsIgnoreCase("0")) {
                        PresenterSelector presenterSelector = new ShowCardsPresenter();
                        showResponseAdapter = new ArrayObjectAdapter(presenterSelector);
                        for (int i = 0; i < showsResponse.data.size(); i++) {
                            showsResponse.data.get(i).itemPosition = i;
                            showResponseAdapter.add(showsResponse.data.get(i));
                        }
                        HeaderItem headerItem = new HeaderItem(j, "Shows");
                        mRowsAdapter.add(new ShowListRow(headerItem, showResponseAdapter, showsResponse));
                    } else {
                        int count = recentAddedAdapter.size();
                        for (int i = 0; i < showsResponse.data.size(); i++) {
                            showsResponse.data.get(i).itemPosition = count + i;
                            showResponseAdapter.add(showsResponse.data.get(i));
                        }
                        synchronized (showResponseAdapter) {

                            showResponseAdapter.notify();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ShowsResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callAnchorsApi(final String offset, final int j) {
//        Thread t = new Thread() {
//            public void run() {

        Call<AnchorsResponse> call = ApiClient.getScoopWhoopOldApi().getAnchorsList();
        call.enqueue(new Callback<AnchorsResponse>() {
            @Override
            public void onResponse(Call<AnchorsResponse> call, Response<AnchorsResponse> response) {
                anchorsResponse = response.body();
                if (anchorsResponse != null && anchorsResponse.data != null && anchorsResponse.data.size() > 0) {
                    if (offset.equalsIgnoreCase("0")) {
                        PresenterSelector presenterSelector = new AnchorCardsPresenter();
                        anchorsResponseAdapter = new ArrayObjectAdapter(presenterSelector);
                        for (int i = 0; i < anchorsResponse.data.size(); i++) {
                            anchorsResponse.data.get(i).itemPosition = i;
                            anchorsResponseAdapter.add(anchorsResponse.data.get(i));
                        }
                        HeaderItem headerItem = new HeaderItem(j, "Anchor");
                        mRowsAdapter.add(new AnchorListRow(headerItem, anchorsResponseAdapter, anchorsResponse));
                    } else {
                        int count = anchorsResponseAdapter.size();
                        for (int i = 0; i < anchorsResponse.data.size(); i++) {
                            anchorsResponse.data.get(i).itemPosition = count + i;
                            anchorsResponseAdapter.add(anchorsResponse.data.get(i));
                        }
                        synchronized (anchorsResponseAdapter) {

                            anchorsResponseAdapter.notify();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<AnchorsResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();

            }
        });

             /*   try {
                    Response<AnchorsResponse> modelResponse = call.execute();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("error", e.getMessage());
                }
            }*/
      /*  };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
     }*/
    }

    public void callShowsVideoApi(final String offset, final int j, final String slug) {
//        Log.e("slug", slug);
//        Thread t = new Thread() {
//            public void run() {
        Call<ShowsVideoResponse> call = ApiClient.getScoopWhoopApi().getShowsVideoList(slug, offset);
        call.enqueue(new Callback<ShowsVideoResponse>() {
            @Override
            public void onResponse(Call<ShowsVideoResponse> call, Response<ShowsVideoResponse> response) {
                showVideoResponse = response.body();
                if (showVideoResponse != null && showVideoResponse.data != null && showVideoResponse.data.size() > 0) {
                    if (offset.equalsIgnoreCase("0")) {
                        PresenterSelector presenterSelector = new ExclusiveCardsPresenter();
                        arrayObjectAdapter = new ArrayObjectAdapter(presenterSelector);
                        for (int i = 0; i < showVideoResponse.data.size(); i++) {
                            showVideoResponse.data.get(i).itemPosition = i;
                            arrayObjectAdapter.add(showVideoResponse.data.get(i));
                        }
                        moreShows.put(showVideoResponse.show_details.topic_name, arrayObjectAdapter);
                        moreShowsResponse.put(showVideoResponse.show_details.topic_name, showVideoResponse);
                        slugShowsNames.add(showVideoResponse.show_details.topic_name);
                        HeaderItem headerItem = new HeaderItem(j, showVideoResponse.show_details.topic_name);
                        HeaderItemModel headerItem1 = new HeaderItemModel(1, "Home", R.drawable.ic_home_white_24dp);
                        mRowsAdapter.add(new ShowsVideoListRow(headerItem, arrayObjectAdapter, showVideoResponse));
                    } else {
                        int count = arrayObjectAdapter.size();
                        for (int i = 0; i < showVideoResponse.data.size(); i++) {
                            showVideoResponse.data.get(i).itemPosition = count + i;
                            arrayObjectAdapter.add(showVideoResponse.data.get(i));
                        }
                        moreShowsResponse.put(showVideoResponse.show_details.topic_name, showVideoResponse);

                        synchronized (arrayObjectAdapter) {

                            arrayObjectAdapter.notify();
                        }
//            }
//        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ShowsVideoResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();

            }
        });
      /*  try {
            Response<ShowsVideoResponse> modelResponse = call.execute();
            new Gson().toJson(modelResponse.body());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }*/

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

    private void setCustomPadding() {
        getView().setPadding(Utils.convertDpToPixel(getActivity(), -28), Utils.convertDpToPixel(getActivity(), 500), Utils.convertDpToPixel(getActivity(),0), 0);
    }

    public void refresh() {
        getView().setPadding(Utils.convertDpToPixel(getActivity(), -24), Utils.convertDpToPixel(getActivity(),500), Utils.convertDpToPixel(getActivity(), 48), 0);
    }
}
