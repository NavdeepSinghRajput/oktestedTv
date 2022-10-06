package com.tv.oktested.app.page.detail.apihandling;

import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.network.MasterView;

public interface AnchorDetailView extends MasterView {
    void getVideoListResponse(VideoListResponse videoListResponse,String offset);

  /*  void getUserData(GetUserResponse getUserResponse);

    void setFollowResponse(FollowResponse followResponse);

    void setUnFollowResponse(UnFollowResponse unFollowResponse);*/
}