package com.tv.oktested.app.page.detail.presenter;

import android.content.Context;

import com.tv.oktested.app.page.detail.apihandling.AnchorDetailView;
import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.model.BaseResponse;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.network.NetworkHandler;
import com.tv.oktested.utils.Helper;

import okhttp3.Request;

public class AnchorDetailPresenter extends NetworkHandler {

    private Context context;
    private AnchorDetailView anchorDetailView;
    String offset;

    public AnchorDetailPresenter(Context context, AnchorDetailView anchorDetailView) {
        super(context,anchorDetailView);
        this.context = context;
        this.anchorDetailView = anchorDetailView;
    }

    public void callActorsVideoListApi(String username,String offset ) {
        this.offset =offset;
        ApiClient.getScoopWhoopApi().getActorsVideoList(username,offset).enqueue(this);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (anchorDetailView != null) {
            if (response instanceof VideoListResponse) {
                VideoListResponse videoListResponse = (VideoListResponse) response;
                anchorDetailView.getVideoListResponse(videoListResponse,offset);
            }
        }
        return true;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (anchorDetailView != null) {
            if (Helper.isContainValue(message)) {
                anchorDetailView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (anchorDetailView != null) {
            anchorDetailView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    public void onDestroyedView() {
        anchorDetailView = null;
    }
}