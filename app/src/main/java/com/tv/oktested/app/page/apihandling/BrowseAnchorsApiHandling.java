package com.tv.oktested.app.page.apihandling;

import android.content.Context;

import com.tv.oktested.app.page.model.AnchorsResponse;
import com.tv.oktested.header.CustomAnchorFragment;
import com.tv.oktested.model.BaseResponse;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.network.NetworkHandler;
import com.tv.oktested.utils.Helper;

import okhttp3.Request;

public class BrowseAnchorsApiHandling extends NetworkHandler {

    private CustomAnchorFragment browseAnchorView;
    private Context context;

    public BrowseAnchorsApiHandling(Context context, CustomAnchorFragment browseAnchorView) {
        super(context, browseAnchorView);
        this.context = context;
        this.browseAnchorView = browseAnchorView;
    }

    public void callAnchorsApi() {
        ApiClient.getScoopWhoopOldApi().getAnchorsList().enqueue(this);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (browseAnchorView != null) {
            browseAnchorView.hideLoader();
            if (response instanceof AnchorsResponse) {
                AnchorsResponse anchorsResponse = (AnchorsResponse) response;
                browseAnchorView.setAnchorsResponse(anchorsResponse);
            }
        }
        return true;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (browseAnchorView != null) {
            browseAnchorView.hideLoader();
            if (Helper.isContainValue(message)) {
                browseAnchorView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (browseAnchorView != null) {
            browseAnchorView.hideLoader();
            browseAnchorView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    public void onDestroyedView() {
        browseAnchorView = null;
    }
}