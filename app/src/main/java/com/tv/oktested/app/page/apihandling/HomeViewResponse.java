package com.tv.oktested.app.page.apihandling;

import com.tv.oktested.app.page.model.AnchorsResponse;
import com.tv.oktested.app.page.model.AppExclusiveResponse;
import com.tv.oktested.app.page.model.EditorPickModel;
import com.tv.oktested.app.page.model.HomeStructureModel;
import com.tv.oktested.app.page.model.ShowsResponse;
import com.tv.oktested.app.page.model.ShowsVideoResponse;
import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.network.MasterView;

public interface HomeViewResponse extends MasterView {
    void setHomeStructureResponse(HomeStructureModel homeStructureResponse);

/*    void setEditorPickResponse(EditorPickModel editorPickResponse);
    void setAppExclusiveResponse(AppExclusiveResponse appExclusiveResponse);

    void setRecentlyAddedResponse(VideoListResponse videoListResponse);

    void setShowsResponse(ShowsResponse showsResponse);

    void setAnchorsResponse(AnchorsResponse anchorsResponse);

    void setShowsVideoResponse(ShowsVideoResponse showsVideoResponse,String slug);*/
}