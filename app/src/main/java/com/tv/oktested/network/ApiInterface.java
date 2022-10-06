package com.tv.oktested.network;

import com.google.gson.JsonObject;
import com.tv.oktested.app.onBoarded.OnboardSupportFragment;
import com.tv.oktested.app.page.model.AnchorsResponse;
import com.tv.oktested.app.page.model.AppExclusiveResponse;
import com.tv.oktested.app.page.model.EditorPickModel;
import com.tv.oktested.app.page.model.GetUserResponse;
import com.tv.oktested.app.page.model.HomeStructureModel;
import com.tv.oktested.app.page.model.ShowsResponse;
import com.tv.oktested.app.page.model.ShowsVideoResponse;
import com.tv.oktested.app.page.model.VideoListResponse;
import com.tv.oktested.app.videoConsumption.model.EventTrackingResponse;
import com.tv.oktested.app.videoConsumption.model.EventTrackingUpdateResponse;
import com.tv.oktested.model.GenerateCodeModel;
import com.tv.oktested.model.VerifyCodeModel;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    // For get the dynamic home structure
    @GET("dynamichome/")
    Call<HomeStructureModel> getHomeStructure(@Query("type") String type);

    // For get the editor pick video on top of home
    @GET("ed-picks/latest/")
    Call<EditorPickModel> getEditorPickVideo();

    // For App Exclusive Video list
    @GET("videos/?filter_type=app_only")
    Call<AppExclusiveResponse> getAppExclusiveVideoList(@Query("offset") String offset);

    // To get the recently added videos list
    @GET("videos/")
    Call<VideoListResponse> getRecentlyAddedVideoList(@Query("offset") String offset);


    // For get the list of shows
    @GET("video_app/ok_tested/get_all_channel_shows/")
    Call<ShowsResponse> getShowsList(@Query("offset") String offset);

    // For get the list of actors on actor listing page
    @GET("video_app/get_all_castcrew/?show_faces=true")
    Call<AnchorsResponse> getAnchorsList();

    // For get the list of videos for particular show
    @GET("videos/?filter_type=show")
    Call<ShowsVideoResponse> getShowsVideoList(@Query("filter_slug") String slug,@Query("offset") String offset);


    // For get the videos of particular actor on actor profile page
    @GET("videos/?filter_type=castcrew")
    Call<VideoListResponse> getActorsVideoList(@Query("filter_slug") String string,@Query("offset") String offset);

    // For get the user search result
    @GET("search_videos/{search}/")
    Call<VideoListResponse> getUserSearchResult(@HeaderMap HashMap<String, String> var1, @Path("search") String search,@Query("offset") String offset);

    // Use for click favourite video on video playing screen and for select favourite show
    @POST("/favourite")
    Call<ResponseBody> favourite(@HeaderMap HashMap<String, String> header, @Body JsonObject jsonObject);

    // Use for click unfavoured video on video playing screen and for select unfavoured show
    @POST("/unfavourite")
    Call<ResponseBody> unfavourite(@HeaderMap HashMap<String, String> header, @Body JsonObject jsonObject);

    // Used for follow actors on video playing screen and actors screen
    @POST("/follow")
    Call<ResponseBody> follow(@HeaderMap HashMap<String, String> header, @Body JsonObject jsonObject);

    // Used for unfollow actors on video playing screen and actors screen
    @POST("/unfollow")
    Call<ResponseBody> unfollow(@HeaderMap HashMap<String, String> header, @Body JsonObject jsonObject);

    // Used in multiple screens to get the user data like follow, favourite etc.
    @GET("/users/{uid}")
    Call<GetUserResponse> getUserData(@HeaderMap HashMap<String, String> var1, @Path("uid") String uid);


    // For get the list of favourite actors on my profile
    @GET("get_all_castcrew/?")
    Call<AnchorsResponse> getFavouriteAnchorList(@Query("data") String string);
    // For get list of favourite shows on my profile page
    @GET("video_app/ok_tested/get_all_channel_shows/?")
    Call<ShowsResponse> getFavouriteShowsList(@Query("data") String string);
    // For get list of favourite videos on my profile page
    @GET("videos/?")
    Call<VideoListResponse> getFavouriteVideoList(@Query("data") String string);

    // Video Tracking api for 1st time play video
    @POST("/events")
    Call<EventTrackingResponse> callVideoTrackingApi(@Body JsonObject jsonObject);

    // Video tracking api when video is playing in handler
    @PUT("/events")
    Call<EventTrackingUpdateResponse> callVideoTrackingUpdateApi(@Body JsonObject jsonObject);

    //For Generating Code
    @POST("/generatecode")
    Call<GenerateCodeModel>  generateCode(@Body OnboardSupportFragment.GenerateCode generateCode);

    //For Verifying Code
    @POST(".")
    Call<VerifyCodeModel>  verifyCode(@Body OnboardSupportFragment.GenerateCode generateCodes);
}
