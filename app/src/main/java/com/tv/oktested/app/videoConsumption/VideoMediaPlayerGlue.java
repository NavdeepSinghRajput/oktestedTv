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
 *
 */

package com.tv.oktested.app.videoConsumption;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;

import com.google.gson.JsonObject;
import com.tv.oktested.R;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.GetUserDataresponse;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;
import com.tv.oktested.utils.DataHolder;
import com.tv.oktested.utils.GetUserData;
import com.tv.oktested.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PlayerGlue for video playback
 *
 * @param <T>
 */
public class VideoMediaPlayerGlue<T extends PlayerAdapter> extends PlaybackTransportControlGlue<T> {
    private static final long TEN_SECONDS = TimeUnit.SECONDS.toMillis(10);

    private PlaybackControlsRow.RepeatAction mRepeatAction;
    private PlaybackControlsRow.ThumbsUpAction mThumbsUpAction;
    private PlaybackControlsRow.ThumbsDownAction mThumbsDownAction;
    private PlaybackControlsRow.PictureInPictureAction mPipAction;
    private PlaybackControlsRow.ClosedCaptioningAction mClosedCaptioningAction;
    //    private PlaybackControlsRow.MoreActions moreActions;
    Context context;
    private ArrayList<DataItem> dataItemList;
    private int currentItem;
    boolean isFavourite = false;


    private PlaybackControlsRow.SkipPreviousAction mSkipPreviousAction;
    private PlaybackControlsRow.SkipNextAction mSkipNextAction;
    private PlaybackControlsRow.FastForwardAction mFastForwardAction;
    private PlaybackControlsRow.RewindAction mRewindAction;

    private void setLikeIcon(GetUserDataresponse response) {
        if (response != null && response.favourite != null && response.favourite.size() > 0) {
            for (int i = 0; i < response.favourite.size(); i++) {
                if (response.favourite.get(i).equalsIgnoreCase(dataItemList.get(currentItem).id)) {
                    isFavourite = true;
                    mThumbsDownAction.setIcon(getContext().getResources().getDrawable(R.drawable.ic_heart_white_24dp));
                    break;
                }
            }
        }
    }

    private void callVideoFollowApi() {
        if (Helper.isNetworkAvailable(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.ACCESS_TOKEN));
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("video", dataItemList.get(currentItem).id);
            Call<ResponseBody> call;
            if (isFavourite) {
                call = ApiClient.getApi().unfavourite(headerMap, jsonObject);
            } else {
                call = ApiClient.getApi().favourite(headerMap, jsonObject);
            }
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    GetUserData getUserData = new GetUserData(context);
                    getUserData.callUserData();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Network error occured", Toast.LENGTH_SHORT).show();
                    Log.e("favouriteFailure", t.getMessage());
                }
            });

        }
    }

    public VideoMediaPlayerGlue(Activity context, T impl, ArrayList<DataItem> dataItemList, int currentItem) {
        super(context, impl);
        this.context = context;
        this.dataItemList = dataItemList;
        this.currentItem = currentItem;
        mClosedCaptioningAction = new PlaybackControlsRow.ClosedCaptioningAction(context);
        mThumbsUpAction = new PlaybackControlsRow.ThumbsUpAction(context);
        mThumbsUpAction.setIndex(PlaybackControlsRow.ThumbsUpAction.OUTLINE);
        mThumbsDownAction = new PlaybackControlsRow.ThumbsDownAction(context);
        Drawable[] drawables = new Drawable[]{getContext().getResources().getDrawable(R.drawable.ic_unavourite_24dp), getContext().getResources().getDrawable(R.drawable.ic_heart_white_24dp)};
        mThumbsDownAction.setDrawables(drawables);
        mRepeatAction = new PlaybackControlsRow.RepeatAction(context);
        mPipAction = new PlaybackControlsRow.PictureInPictureAction(context);
        mSkipPreviousAction = new PlaybackControlsRow.SkipPreviousAction(context);
        mSkipNextAction = new PlaybackControlsRow.SkipNextAction(context);
        mFastForwardAction = new PlaybackControlsRow.FastForwardAction(context);
        mRewindAction = new PlaybackControlsRow.RewindAction(context);

        setLikeIcon(DataHolder.getInstance().getUserDataresponse);


    }

    @Override
    protected void onCreateSecondaryActions(ArrayObjectAdapter adapter) {
        //  adapter.add(mThumbsUpAction);

//        adapter.add(moreActions);
        if (android.os.Build.VERSION.SDK_INT > 23) {
            //   adapter.add(mPipAction);
        }
    }

    @Override
    protected void onCreatePrimaryActions(ArrayObjectAdapter adapter) {
        super.onCreatePrimaryActions(adapter);
        //        adapter.add(mSkipPreviousAction);
        adapter.add(mRewindAction);
        adapter.add(mFastForwardAction);
//        adapter.add(mSkipNextAction);
//        adapter.add(mRepeatAction);
        adapter.add(mThumbsDownAction);
//        adapter.add(mClosedCaptioningAction);
    }

    @Override
    public void onActionClicked(Action action) {
        if (action == mRewindAction) {
            rewind();
        } else if (action == mFastForwardAction) {
            fastForward();
        } else if (shouldDispatchAction(action)) {
            dispatchAction(action);
            return;
        }
        super.onActionClicked(action);
    }

    private boolean shouldDispatchAction(Action action) {
        return action == mRewindAction
                || action == mFastForwardAction || action == mRepeatAction || action == mThumbsUpAction || action == mThumbsDownAction
                || action == mPipAction || action == mClosedCaptioningAction/* || action == moreActions*/;
    }

    private void dispatchAction(Action action) {
        if (action == mPipAction) {
            ((Activity) getContext()).enterPictureInPictureMode();
        } else if (action == mThumbsDownAction) {
            callVideoFollowApi();
            PlaybackControlsRow.MultiAction multiAction = (PlaybackControlsRow.MultiAction) action;
            multiAction.nextIndex();
            notifyActionChanged(multiAction);
        } else {
            PlaybackControlsRow.MultiAction multiAction = (PlaybackControlsRow.MultiAction) action;
            multiAction.nextIndex();
            notifyActionChanged(multiAction);
        }
    }


    private void notifyActionChanged(PlaybackControlsRow.MultiAction action) {
        int index = -1;
        if (getPrimaryActionsAdapter() != null) {
            index = getPrimaryActionsAdapter().indexOf(action);
        }
        if (index >= 0) {
            getPrimaryActionsAdapter().notifyArrayItemRangeChanged(index, 1);
        } else {
            if (getSecondaryActionsAdapter() != null) {
                index = getSecondaryActionsAdapter().indexOf(action);
                if (index >= 0) {
                    getSecondaryActionsAdapter().notifyArrayItemRangeChanged(index, 1);
                }
            }
        }
    }

    private ArrayObjectAdapter getPrimaryActionsAdapter() {
        if (getControlsRow() == null) {
            return null;
        }
        return (ArrayObjectAdapter) getControlsRow().getPrimaryActionsAdapter();
    }

    private ArrayObjectAdapter getSecondaryActionsAdapter() {
        if (getControlsRow() == null) {
            return null;
        }
        return (ArrayObjectAdapter) getControlsRow().getSecondaryActionsAdapter();
    }

    Handler mHandler = new Handler();

    @Override
    protected void onPlayCompleted() {
        super.onPlayCompleted();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mRepeatAction.getIndex() != PlaybackControlsRow.RepeatAction.NONE) {
                    play();
                }
            }
        });
    }

    public void setMode(int mode) {
        mRepeatAction.setIndex(mode);
        if (getPrimaryActionsAdapter() == null) {
            return;
        }
        notifyActionChanged(mRepeatAction);
    }

    public void rewind() {
        long newPosition = getCurrentPosition() - TEN_SECONDS;
        newPosition = (newPosition < 0) ? 0 : newPosition;
        Toast.makeText(context, "-10", Toast.LENGTH_SHORT).show();
        getPlayerAdapter().seekTo(newPosition);
    }

    /**
     * Skips forward 10 seconds.
     */
    public void fastForward() {
        if (getDuration() > -1) {
            long newPosition = getCurrentPosition() + TEN_SECONDS;
            newPosition = (newPosition > getDuration()) ? getDuration() : newPosition;
            Toast.makeText(context, "+10", Toast.LENGTH_SHORT).show();
            getPlayerAdapter().seekTo(newPosition);
        }
    }
}