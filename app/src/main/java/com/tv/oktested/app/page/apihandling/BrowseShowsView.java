package com.tv.oktested.app.page.apihandling;

import com.tv.oktested.app.page.model.ShowsResponse;
import com.tv.oktested.network.MasterView;

public interface BrowseShowsView extends MasterView {

    void setShowsResponse(ShowsResponse showsResponse);
}