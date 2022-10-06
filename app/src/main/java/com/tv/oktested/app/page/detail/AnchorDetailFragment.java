package com.tv.oktested.app.page.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.leanback.widget.Action;
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

import com.google.gson.JsonObject;
import com.tv.oktested.app.page.customised.DetailsFragmentCustomizatioon;
import com.tv.oktested.app.page.detail.apihandling.AnchorDetailView;
import com.tv.oktested.app.page.detail.presenterView.AnchorDescriptionPresenterView;
import com.tv.oktested.app.page.detail.presenter.AnchorDetailPresenter;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.AnchorsListModel;
import com.tv.oktested.app.page.model.GetUserDataresponse;
import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.app.page.presenterView.AllShowCardPresenterView;
import com.tv.oktested.app.page.presenter.EditorRowPresenter;
import com.tv.oktested.app.videoConsumption.VideoActivity;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;
import com.tv.oktested.utils.DataHolder;
import com.tv.oktested.utils.GetUserData;
import com.tv.oktested.utils.Helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnchorDetailFragment extends DetailsFragmentCustomizatioon implements OnItemViewClickedListener,
        OnItemViewSelectedListener, AnchorDetailView {
    AnchorDetailPresenter anchorDetailPresenter;
    public static final String TRANSITION_NAME = "t_for_transition";
    private ArrayObjectAdapter mRowsAdapter;
    Bitmap mbitmap;
    AnchorsListModel anchorsListModel;
    private Action mActionFavourite;
    private static final long ACTION_Favourite = 3;
    boolean isFollow = false;
    ArrayObjectAdapter actionAdapter, anchorRowAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anchorDetailPresenter = new AnchorDetailPresenter(getActivity().getBaseContext(), this);
        anchorsListModel = (AnchorsListModel) getArguments().getSerializable("anchorModel");
        setAnchorFollowIcon(DataHolder.getInstance().getUserDataresponse, anchorsListModel.username);
        callAnchorVideoApi(anchorsListModel.username, "0");
        setupEventListeners();

    }

    private void callAnchorVideoApi(String username, String offset) {
        if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
            anchorDetailPresenter.callActorsVideoListApi(username, offset);
        } else {
//            showMessage(getString(R.string.please_check_internet_connection));
        }
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

        } else if ((item instanceof Action)) {
            Action action = (Action) item;
            Log.e("ActionSelected", "Added");
            if (action.getId() == ACTION_Favourite) {
                String s = (String) action.getLabel1();
                Log.e("String", s + "ss");
                if (((String) action.getLabel1()).equalsIgnoreCase("unfollow")) {
                    callFollowApi(anchorsListModel.username);
                    action.setLabel1("follow");

                } else {
                    callFollowApi(anchorsListModel.username);
                    action.setLabel1("unfollow");
                }
                action.getLabel1();
                synchronized (actionAdapter) {

                    actionAdapter.notify();
                }
                synchronized (mRowsAdapter) {

                    mRowsAdapter.notify();
                }
//                actionAdapter.notify();
//                setSelectedPosition(1);
            } else {
         /*   Toast.makeText(getActivity(), getString(R.string.action_cicked), Toast.LENGTH_LONG)
                    .show();*/
            }
        }
    }

    public void callFollowApi(String username) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(getActivity().getBaseContext()).getPreferencesString(AppConstants.ACCESS_TOKEN));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("actor", username);
        Call<ResponseBody> call;
        if (isFollow) {
            isFollow = false;
            mActionFavourite = new Action(ACTION_Favourite, "Follow");
            call = ApiClient.getApi().unfollow(headerMap, jsonObject);
            synchronized (actionAdapter) {
                actionAdapter.notifyAll();
            }
        } else {
            isFollow = true;
            mActionFavourite = new Action(ACTION_Favourite, "unFollow");
            call = ApiClient.getApi().follow(headerMap, jsonObject);
            synchronized (actionAdapter) {
                actionAdapter.notifyAll();
            }
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                GetUserData getUserData = new GetUserData(getActivity().getBaseContext());
                getUserData.callUserData();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof DataItem) {
            Log.e("exclusive", "Exclusive" + anchorsVideoResponses.data.size() + "   " + ((DataItem) item).itemPosition + "    " + anchorsVideoResponses.next_offset);
            if (!anchorsVideoResponses.next_offset.equalsIgnoreCase("-1") && ((anchorsVideoResponses.data.size() - 5) < ((DataItem) item).itemPosition)) {
                callAnchorVideoApi(anchorsListModel.username, anchorsVideoResponses.next_offset);
            }
        }
    }

    VideoListResponse anchorsVideoResponses;

    @Override
    public void getVideoListResponse(VideoListResponse videoListResponse, String offset) {
        anchorsVideoResponses = videoListResponse;
        if (videoListResponse != null && videoListResponse.data != null && videoListResponse.data.size() > 0) {
            if (offset.equalsIgnoreCase("0")) {
                for (int i = 0; i < anchorsVideoResponses.data.size(); i++) {
                    anchorsVideoResponses.data.get(i).itemPosition = i;
                }
//                getBitmapFromURL(anchorsListModel.profile_pic);

                setupUi(videoListResponse);
            } else {
                int count = anchorRowAdapter.size();
                for (int i = 0; i < anchorsVideoResponses.data.size(); i++) {
                    anchorsVideoResponses.data.get(i).itemPosition = count + i;
                    anchorRowAdapter.add(anchorsVideoResponses.data.get(i));
                }
                synchronized (anchorRowAdapter) {

                    anchorRowAdapter.notify();
                }
            }
        }
    }

    private void setupUi(VideoListResponse videoListResponse) {

        EditorRowPresenter detailsOverviewRowPresenter = new EditorRowPresenter(new AnchorDescriptionPresenterView(getActivity(),anchorsListModel.username));
        ClassPresenterSelector rowPresenterSelector = new ClassPresenterSelector();
        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsOverviewRowPresenter);
        rowPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL));
        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);

        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(anchorsListModel);
        detailsOverview.setImageBitmap(getActivity(), mbitmap);
        mRowsAdapter.add(detailsOverview);

        anchorRowAdapter = new ArrayObjectAdapter(new AllShowCardPresenterView());
        for (int i = 0; i < videoListResponse.data.size(); i++) {
            anchorRowAdapter.add(videoListResponse.data.get(i));
        }
        HeaderItem header = new HeaderItem(1, "Videos Featuring "+anchorsListModel.display_name);
        mRowsAdapter.add(new ListRow(header, anchorRowAdapter));

        setAdapter(mRowsAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startEntranceTransition();
            }
        }, 500);
//        initializeBackground();
    }

   /* private void initializeBackground() {
        DetailsFragmentBackgroundController mDetailsBackground = new DetailsFragmentBackgroundController(this);
        mDetailsBackground.enableParallax();
        mDetailsBackground.setCoverBitmap(mbitmap);
    }
*/
    int getBitmapFromURL(final String src) {
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

    private void setAnchorFollowIcon(GetUserDataresponse response, String userName) {
        if (response != null && response.follow != null && response.follow.size() > 0) {
            for (int i = 0; i < response.follow.size(); i++) {
                if (response.follow.get(i).equalsIgnoreCase(userName)) {
                    isFollow = true;
                    break;
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
