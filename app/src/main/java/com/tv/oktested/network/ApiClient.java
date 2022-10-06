package com.tv.oktested.network;

public class ApiClient {
    private static final String OK_TESTED_DYNAMIC_HOME = "https://www.scoopwhoop.com/api/oktestedapp/";
    private static final String SCOOPWHOOP_URL_API_NEW = "https://www.scoopwhoop.com/api/v2/video_app/extPlateform/ok_tested_app/";
    private static final String SCOOPWHOOP_URL_API = "https://www.scoopwhoop.com/api/v2/";
    private static final String BASE_URL_API_LIVE = "https://oktapi.scoopwhoop.com";
    private static final String BASE_URL_API_STAGING = "https://stageoktapi.scoopwhoop.com";

    private static final String VIDEO_TRACKING_LIVE_URL = "https://videoapi.scwp.in/";
    private static final String VIDEO_TRACKING_STAGE_URL = "https://stage.scwp.in/";
    private static final String SCOOPWHOOP_ACTOR_URL_API = "https://www.scoopwhoop.com/api/v2/video_app/";
    public static ApiInterface getDynamicHomeApi() {
        return RetrofitClient.getClient(OK_TESTED_DYNAMIC_HOME)
                .create(ApiInterface.class);
    }
    public static ApiInterface getScoopWhoopApi() {
        return RetrofitClient.getClient(SCOOPWHOOP_URL_API_NEW)
                .create(ApiInterface.class);
    }
    public static ApiInterface getScoopWhoopOldApi() {
        return RetrofitClient.getClient(SCOOPWHOOP_URL_API)
                .create(ApiInterface.class);
    }
    public static ApiInterface getApi() {
        return RetrofitClient.getClient(BASE_URL_API_LIVE)
                .create(ApiInterface.class);
    }
    public static ApiInterface getScoopWhoopActorApi() {
        return RetrofitClient.getClient(SCOOPWHOOP_ACTOR_URL_API)
                .create(ApiInterface.class);
    }

    public static ApiInterface getScoopWhoopVideoTrackingApi() {
        return RetrofitClient.getClient(VIDEO_TRACKING_LIVE_URL)
                .create(ApiInterface.class);
    }
    public static ApiInterface getVerifyGenerateCode(String verification_url) {
        return RetrofitClient.getClient(verification_url)
                .create(ApiInterface.class);
    } public static ApiInterface getGenerateCode() {
        return RetrofitClient.getClient(BASE_URL_API_LIVE)
                .create(ApiInterface.class);
    }
}
