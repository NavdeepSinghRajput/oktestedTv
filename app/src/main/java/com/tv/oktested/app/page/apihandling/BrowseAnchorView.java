package com.tv.oktested.app.page.apihandling;

import com.tv.oktested.app.page.model.AnchorsResponse;
import com.tv.oktested.network.MasterView;

public interface BrowseAnchorView  extends  MasterView{
    void setAnchorsResponse(AnchorsResponse anchorsResponse);
}