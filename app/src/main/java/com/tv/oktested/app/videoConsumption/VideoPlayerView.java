package com.tv.oktested.app.videoConsumption;


import com.tv.oktested.app.page.model.GetUserResponse;
import com.tv.oktested.app.videoConsumption.model.EventTrackingResponse;
import com.tv.oktested.app.videoConsumption.model.EventTrackingUpdateResponse;
import com.tv.oktested.network.MasterView;

public interface VideoPlayerView extends MasterView {

/*    void setReactionResponse(ReactionResponse reactionResponse);

    void setUnFavResponse(UnFavouriteResponse unFavouriteResponse);

    void setFavResponse(FavouriteResponse favouriteResponse);

    void setFollowResponse(FollowResponse followResponse);

    void setUnFollowResponse(UnFollowResponse unFollowResponse);*/

    void getUserData(GetUserResponse getUserResponse);

  /*  void setVideoCommentResponse(VideoCommentResponse videoCommentResponse);

    void setVideoCommentPostResponse(VideoCommentPostResponse videoCommentPostResponse);

    void setCommentReactionResponse(CommentReactionResponse commentReactionResponse);

    void setCommentDeleteResponse(CommentDeleteResponse commentDeleteResponse);

    void setCommentReplyResponse(CommentReplyResponse commentReplyResponse);

    void setCommentReplyPostResponse(CommentReplyPostResponse commentReplyPostResponse);

    void setReplyReactionResponse(ReplyReactionResponse replyReactionResponse);

    void setReplyDeleteResponse(ReplyDeleteResponse replyDeleteResponse);*/

    void setEventTrackingUpdateResponse(EventTrackingUpdateResponse eventTrackingUpdateResponse);

    void setEventTrackingResponse(EventTrackingResponse eventTrackingResponse);
}