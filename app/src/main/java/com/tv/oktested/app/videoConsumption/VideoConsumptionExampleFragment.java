/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tv.oktested.app.videoConsumption;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.VideoFragment;
import androidx.leanback.app.VideoFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackGlue;
import androidx.leanback.widget.PlaybackControlsRow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tv.oktested.BuildConfig;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.GetUserResponse;
import com.tv.oktested.app.videoConsumption.model.EventTrackingResponse;
import com.tv.oktested.app.videoConsumption.model.EventTrackingUpdateResponse;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.DataHolder;
import com.tv.oktested.utils.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressLint("ValidFragment")
public class VideoConsumptionExampleFragment extends VideoFragment implements VideoPlayerView{

    private static final String URL = "https://storage.googleapis.com/android-tv/Sample videos/"
            + "April Fool's 2013/Explore Treasure Mode with Google Maps.mp4";
    public static final String TAG = "VideoConsumption";
    private VideoMediaPlayerGlue<MediaPlayerAdapter> mMediaPlayerGlue;
    final VideoFragmentGlueHost mHost = new VideoFragmentGlueHost(this);
    private ArrayList<DataItem> dataItemList;
    private int currentItem;
    private String eventId = "", cityName, stateName, countryName;
    private long twentyFivePercent, twentySixPercent, fiftyPercent, fiftyOnePercent, seventyFivePercent,
            seventySixPercent, ninetyFivePercent, ninetySixPercent;
    private boolean isFive = true, isTen = true, isTwentyFive = true, isFifty = true, isSeventyFive = true, isNinetyFive = true;

    private FusedLocationProviderClient fusedLocationClient;
    private Handler mVideoPositionTrackingHandler;
    private Runnable mVideoPositionTrackingRunnable;
    VideoPlayerApiPresenter videoPlayerApiPresenter;
    WifiManager wifiManager ;
    @SuppressLint("ValidFragment")
    public VideoConsumptionExampleFragment(ArrayList<DataItem> dataItemList, int currentItem) {
        this.dataItemList = dataItemList;
        this.currentItem = currentItem;
    }

    @Override
    public void onStart() {
        super.onStart();
        mVideoPositionTrackingHandler.post(mVideoPositionTrackingRunnable);

    }



    static void playWhenReady(PlaybackGlue glue) {
        if (glue.isPrepared()) {
            glue.play();
        } else {
            glue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
                @Override
                public void onPreparedStateChanged(PlaybackGlue glue) {
                    if (glue.isPrepared()) {
                        glue.removePlayerCallback(this);
                        glue.play();
                    }
                }
            });
        }
    }

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener
            = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int state) {
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVideoPositionTrackingHandler.removeCallbacks(mVideoPositionTrackingRunnable);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoPlayerApiPresenter = new VideoPlayerApiPresenter(getActivity().getBaseContext(), this);
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mMediaPlayerGlue = new VideoMediaPlayerGlue(getActivity(), new MediaPlayerAdapter(getActivity()),dataItemList,currentItem);
        mMediaPlayerGlue.setHost(mHost);

        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN) != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.w(TAG, "video player cannot obtain audio focus!");
        }
//        wifi();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity().getBaseContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    getLocationFromLatLong(latitude, longitude);
                }
            }
        });

        /*new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    getLocationFromLatLong(latitude, longitude);
                }
            }
        });*/

        mMediaPlayerGlue.setMode(PlaybackControlsRow.RepeatAction.NONE);
        String s = dataItemList.get(0).altContent;
        mMediaPlayerGlue.setTitle(dataItemList.get(0).title);
        mMediaPlayerGlue.setSubtitle(dataItemList.get(0).topic_desc);
        mMediaPlayerGlue.getPlayerAdapter().setDataSource(Uri.parse(s));


//        PlaybackSeekDiskDataProvider.setDemoSeekProvider(mMediaPlayerGlue);
        playWhenReady(mMediaPlayerGlue);
        setBackgroundType(BG_LIGHT);
        callVideoTrackingApi();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVideoPositionTrackingHandler = new Handler();
        mVideoPositionTrackingRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // If player exists and playing then set video position to tracking implementation.
                    if (mMediaPlayerGlue != null && mMediaPlayerGlue.isPlaying()) {
                        if (Helper.isContainValue(eventId)) {
                            callVideoTrackingUpdateApi(mMediaPlayerGlue.getCurrentPosition());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TrackingFailed", "Video position tracking failed.");
                }
                mVideoPositionTrackingHandler.postDelayed(this, AppConstants.VIDEO_POSITION_TRACKING_POLL_TIME_MS);
            }
        };
    }

    private void getLocationFromLatLong(double latitude, double longitude) {
        Geocoder geoCoder = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
        List<Address> address = null;
        try {
            address = geoCoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address != null && address.size() > 0) {
            cityName = address.get(0).getLocality();
            stateName = address.get(0).getAdminArea();
            countryName = address.get(0).getCountryName();
        }
/*
        LocationManager locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation("static");

// Attempt to get postal or zip code from the static location object
        Geocoder geocoder = new Geocoder(this);
        Address address = null;
        try {
            address = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1).get(0);
            Log.d("Zip code", address.getPostalCode());

        } catch (IOException e) {
            Log.e(TAG, "Geocoder error", e);
        }*/

    }

    @Override
    public void onPause() {
        if (mMediaPlayerGlue != null) {
            mMediaPlayerGlue.pause();
        }
        super.onPause();
    }

    private void callVideoTrackingApi() {
        long videoDurationInMilliSeconds = mMediaPlayerGlue.getDuration();
        long videoDurationInSeconds = TimeUnit.MILLISECONDS.toSeconds(videoDurationInMilliSeconds);
        twentyFivePercent = videoDurationInMilliSeconds * 25 / 100;
        twentySixPercent = videoDurationInMilliSeconds * 26 / 100;
        fiftyPercent = videoDurationInMilliSeconds * 50 / 100;
        fiftyOnePercent = videoDurationInMilliSeconds * 51 / 100;
        seventyFivePercent = videoDurationInMilliSeconds * 75 / 100;
        seventySixPercent = videoDurationInMilliSeconds * 76 / 100;
        ninetyFivePercent = videoDurationInMilliSeconds * 95 / 100;
        ninetySixPercent = videoDurationInMilliSeconds * 96 / 100;

        Calendar calendar = Calendar.getInstance();

        ArrayList<String> mediaTag = new ArrayList<>();
        mediaTag.addAll(dataItemList.get(currentItem).tags);
        VideoEventTrackingRequest videoEventTrackingRequest = new VideoEventTrackingRequest();
        videoEventTrackingRequest.city = "";
        videoEventTrackingRequest.autoplay = "true";
        videoEventTrackingRequest.state = "";
        videoEventTrackingRequest.country = "";
        videoEventTrackingRequest.date = Helper.getCurrentDates();
        videoEventTrackingRequest.duration = String.valueOf(videoDurationInSeconds);
        videoEventTrackingRequest.event = "play";
        videoEventTrackingRequest.quartile = "0";
        videoEventTrackingRequest.watchDuration = "0";
        videoEventTrackingRequest.hours = String.valueOf(calendar.get(Calendar.HOUR));
        videoEventTrackingRequest.minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        videoEventTrackingRequest.mediaTags = mediaTag;
        videoEventTrackingRequest.image = dataItemList.get(currentItem).featureImg;
        videoEventTrackingRequest.path = dataItemList.get(currentItem).altContent;
        videoEventTrackingRequest.playerUID = DataHolder.getInstance().getUserDataresponse.id;
        videoEventTrackingRequest.title = dataItemList.get(currentItem).title;
        videoEventTrackingRequest.videoId = dataItemList.get(currentItem).id;
        videoEventTrackingRequest.videoPlatform = "APP";
        videoEventTrackingRequest.pageDomain = Build.VERSION.SDK;
        videoEventTrackingRequest.os = "AndroidTV";
        videoEventTrackingRequest.deviceType = "oktestedtv";
        videoEventTrackingRequest.osVersion = BuildConfig.VERSION_NAME;
        videoEventTrackingRequest.browser = Build.BRAND;
        videoEventTrackingRequest.browserVersion = Build.MODEL;
        videoEventTrackingRequest.sw_cookie = Settings.Secure.getString(getActivity().getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(videoEventTrackingRequest);
            JsonParser parser = new JsonParser();
            JsonObject  jsonObjects = parser.parse(jsonStr).getAsJsonObject();

            if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
                videoPlayerApiPresenter.callVideoTrackingApi(jsonObjects);
            } else {
                Log.e("Internet","Internet Not Available");
//            showMessage(getString(R.string.please_check_internet_connection));
            }  } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void wifi(){
        List<ScanResult> results;
         results = wifiManager.getScanResults();

        String message = "No results found.Please Check your wireless is on";
        if (results != null)
        {
            final int size = results.size();
            if (size == 0)
                message = "No access points in range";
            else
            {
                ScanResult bestSignal = results.get(0);
                int count = 1;
                for (ScanResult result : results)
                {
                    if (WifiManager.compareSignalLevel(bestSignal.level, result.level) < 0)
                    {
                        bestSignal = result;
                    }
                }
                Log.e("bestSignal",bestSignal.BSSID+"       "+bestSignal.level+"        ss"   );
            }
        }
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void callVideoTrackingUpdateApi(long duration) {
        if (duration >= 5000 && duration <= 6000) {
            if (isFive) {
                isFive = false;
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("quartile", "5");
                jsonObject.addProperty("watchDuration", "5");
                jsonObject.addProperty("id", eventId);
                if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
                    videoPlayerApiPresenter.callVideoTrackingUpdateApi(jsonObject);
                }
            }
        } else if (duration >= 10000 && duration <= 11000) {
            if (isTen) {
                isTen = false;
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("quartile", "10");
                jsonObject.addProperty("watchDuration", "10");
                jsonObject.addProperty("id", eventId);
                if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
                    videoPlayerApiPresenter.callVideoTrackingUpdateApi(jsonObject);
                }
            }
        } else if (duration >= twentyFivePercent && duration <= twentySixPercent && twentyFivePercent != 0L && twentySixPercent != 0L) {
            if (isTwentyFive) {
                isTwentyFive = false;
                Log.d("25Percentage", String.valueOf(twentyFivePercent));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("quartile", "25");
                jsonObject.addProperty("watchDuration", TimeUnit.MILLISECONDS.toSeconds(duration));
                jsonObject.addProperty("id", eventId);
                if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
                    videoPlayerApiPresenter.callVideoTrackingUpdateApi(jsonObject);
                }
            }
        } else if (duration >= fiftyPercent && fiftyPercent != 0L && duration <= fiftyOnePercent && fiftyOnePercent != 0L) {
            if (isFifty) {
                isFifty = false;
                Log.d("50Percentage", String.valueOf(fiftyPercent));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("quartile", "50");
                jsonObject.addProperty("watchDuration", TimeUnit.MILLISECONDS.toSeconds(duration));
                jsonObject.addProperty("id", eventId);
                if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
                    videoPlayerApiPresenter.callVideoTrackingUpdateApi(jsonObject);
                }
            }
        } else if (duration >= seventyFivePercent && seventyFivePercent != 0L && duration <= seventySixPercent && seventySixPercent != 0L) {
            if (isSeventyFive) {
                isSeventyFive = false;
                Log.d("75Percentage", String.valueOf(seventyFivePercent));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("quartile", "75");
                jsonObject.addProperty("watchDuration", TimeUnit.MILLISECONDS.toSeconds(duration));
                jsonObject.addProperty("id", eventId);
                if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
                    videoPlayerApiPresenter.callVideoTrackingUpdateApi(jsonObject);
                }
            }
        } else if (duration >= ninetyFivePercent && ninetyFivePercent != 0L && duration <= ninetySixPercent && ninetySixPercent != 0L) {
            if (isNinetyFive) {
                isNinetyFive = false;
                Log.d("95Percentage", String.valueOf(ninetyFivePercent));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("quartile", "100");
                jsonObject.addProperty("watchDuration", TimeUnit.MILLISECONDS.toSeconds(duration));
                jsonObject.addProperty("id", eventId);
                if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
                    videoPlayerApiPresenter.callVideoTrackingUpdateApi(jsonObject);
                }
            }
        }
    }

    @Override
    public void getUserData(GetUserResponse getUserResponse) {

    }

    @Override
    public void setEventTrackingUpdateResponse(EventTrackingUpdateResponse eventTrackingUpdateResponse) {
        if (eventTrackingUpdateResponse != null) {
            Log.d("success", "success");
        }
    }

    @Override
    public void setEventTrackingResponse(EventTrackingResponse eventTrackingResponse) {
        if (eventTrackingResponse != null) {
            eventId = eventTrackingResponse.event_id;
        }
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMessage(String message) {

    }
}
