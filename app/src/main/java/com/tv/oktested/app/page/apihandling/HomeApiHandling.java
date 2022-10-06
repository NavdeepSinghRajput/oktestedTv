package com.tv.oktested.app.page.apihandling;

import android.content.Context;

import com.tv.oktested.app.page.model.AnchorsResponse;
import com.tv.oktested.app.page.model.AppExclusiveResponse;
import com.tv.oktested.app.page.model.EditorPickModel;
import com.tv.oktested.app.page.model.HomeStructureModel;
import com.tv.oktested.app.page.model.ShowsResponse;
import com.tv.oktested.app.page.model.ShowsVideoResponse;
import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.model.BaseResponse;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.network.NetworkHandler;
import com.tv.oktested.utils.Helper;

import okhttp3.Request;

public class HomeApiHandling extends NetworkHandler {

    private HomeViewResponse homeView;
    private Context context;
    String slug;

    public HomeApiHandling(Context context, HomeViewResponse homeView) {
        super(context, homeView);
        this.homeView = homeView;
        this.context = context;
    }

    public  void callHomeStructure() {
       ApiClient.getDynamicHomeApi().getHomeStructure("androidtv").enqueue(this);
    }

    public void callEditorPickApi() {
        ApiClient.getScoopWhoopApi().getEditorPickVideo().enqueue(this);
    }

    public void callAppExclusiveApi(String offset) {
        ApiClient.getScoopWhoopApi().getAppExclusiveVideoList(offset).enqueue(this);
    }

    public void callRecentlyAddedApi() {
        ApiClient.getScoopWhoopApi().getRecentlyAddedVideoList("0").enqueue(this);
    }

    public void callShowsApi(String offset) {
        ApiClient.getScoopWhoopOldApi().getShowsList(offset).enqueue(this);
    }

    public void callAnchorsApi() {
        ApiClient.getScoopWhoopOldApi().getAnchorsList().enqueue(this);
    }

    public void callShowsVideoApi(String slug) {
        this.slug =slug;
        ApiClient.getScoopWhoopApi().getShowsVideoList(slug,"0").enqueue(this);
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (homeView != null) {
            homeView.hideLoader();
            if (Helper.isContainValue(message)) {
                homeView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (homeView != null) {
            homeView.hideLoader();
            homeView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (homeView != null) {
            if (response instanceof HomeStructureModel) {
                HomeStructureModel homeStructureResponse = (HomeStructureModel) response;
                homeView.setHomeStructureResponse(homeStructureResponse);
            }/*else if (response instanceof EditorPickModel) {
                EditorPickModel editorPickResponse = (EditorPickModel) response;
                homeView.setEditorPickResponse(editorPickResponse);
            }  else if (response instanceof AppExclusiveResponse) {
                AppExclusiveResponse appExclusiveResponse = (AppExclusiveResponse) response;
                homeView.setAppExclusiveResponse(appExclusiveResponse);
            } else if (response instanceof VideoListResponse) {
                VideoListResponse videoListResponse = (VideoListResponse) response;
                homeView.setRecentlyAddedResponse(videoListResponse);
            } else if (response instanceof ShowsResponse) {
                ShowsResponse showsResponse = (ShowsResponse) response;
                homeView.setShowsResponse(showsResponse);
            } else if (response instanceof AnchorsResponse) {
                AnchorsResponse anchorsResponse = (AnchorsResponse) response;
                homeView.setAnchorsResponse(anchorsResponse);
            } else if (response instanceof ShowsVideoResponse) {
                ShowsVideoResponse showsVideoResponse = (ShowsVideoResponse) response;
                homeView.setShowsVideoResponse(showsVideoResponse,slug);
            }*/
        }
        return true;
    }

    public void onDestroyedView() {
        homeView = null;
    }
}