package com.tv.oktested.app.page.detail.presenter;

import android.content.Context;

import com.tv.oktested.app.page.detail.apihandling.ShowDetailView;
import com.tv.oktested.app.page.model.ShowsVideoResponse;
import com.tv.oktested.model.BaseResponse;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.network.NetworkHandler;
import com.tv.oktested.utils.Helper;

import okhttp3.Request;

public class ShowDetailPresenter extends NetworkHandler {

    private Context context;
    private ShowDetailView showDetailView;
    private String offset;

    public ShowDetailPresenter(Context context, ShowDetailView showDetailView) {
        super(context, showDetailView);
        this.context = context;
        this.showDetailView = showDetailView;
    }

    public void callShowsVideoApi(String slug,String offset) {
        this.offset =offset;
        ApiClient.getScoopWhoopApi().getShowsVideoList(slug,offset).enqueue(this);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (showDetailView != null) {
            showDetailView.hideLoader();
            showDetailView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (showDetailView != null) {
            showDetailView.hideLoader();
            if (Helper.isContainValue(message)) {
                showDetailView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (showDetailView != null) {
            showDetailView.hideLoader();
            if (response instanceof ShowsVideoResponse) {
                ShowsVideoResponse showsVideoResponse = (ShowsVideoResponse) response;
                showDetailView.setShowsVideoResponse(showsVideoResponse,offset);
            }
        }
        return true;
    }

    public void onDestroyedView() {
        showDetailView = null;
    }
}