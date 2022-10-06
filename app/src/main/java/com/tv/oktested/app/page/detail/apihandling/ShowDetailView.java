package com.tv.oktested.app.page.detail.apihandling;

import com.tv.oktested.app.page.model.ShowsVideoResponse;
import com.tv.oktested.network.MasterView;

public interface ShowDetailView extends MasterView {
    void setShowsVideoResponse(ShowsVideoResponse showsVideoResponse,String offset);

  /*  void setUnFavResponse(UnFavouriteResponse unFavouriteResponse);

    void setFavResponse(FavouriteResponse favouriteResponse);

    void getUserData(GetUserResponse getUserResponse);*/
}