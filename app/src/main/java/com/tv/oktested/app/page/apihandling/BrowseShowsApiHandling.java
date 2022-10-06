package com.tv.oktested.app.page.apihandling;

import android.content.Context;

import com.tv.oktested.app.page.model.ShowsResponse;
import com.tv.oktested.model.BaseResponse;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.network.NetworkHandler;
import com.tv.oktested.utils.Helper;

import okhttp3.Request;

public class BrowseShowsApiHandling extends NetworkHandler {

    private BrowseShowsView browseShowsView;
    private Context context;

    public BrowseShowsApiHandling(Context context, BrowseShowsView browseShowsView) {
        super(context, browseShowsView);
        this.browseShowsView = browseShowsView;
        this.context = context;
    }

    public void callShowsApi(String offset) {
        ApiClient.getScoopWhoopOldApi().getShowsList(offset).enqueue(this);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (browseShowsView != null) {
            browseShowsView.hideLoader();
            browseShowsView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (browseShowsView != null) {
            browseShowsView.hideLoader();
            if (Helper.isContainValue(message)) {
                browseShowsView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (browseShowsView != null) {
            browseShowsView.hideLoader();
            if (response instanceof ShowsResponse) {
                ShowsResponse showsResponse = (ShowsResponse) response;
                browseShowsView.setShowsResponse(showsResponse);
            }
        }
        return true;
    }

    public void onDestroyedView() {
        browseShowsView = null;
    }
}