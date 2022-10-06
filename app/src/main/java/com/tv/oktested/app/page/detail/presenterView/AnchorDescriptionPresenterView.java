/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.tv.oktested.app.page.detail.presenterView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonObject;
import com.tv.oktested.R;
import com.tv.oktested.app.page.model.AnchorsListModel;
import com.tv.oktested.app.page.model.GetUserDataresponse;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;
import com.tv.oktested.utils.DataHolder;
import com.tv.oktested.utils.GetUserData;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnchorDescriptionPresenterView extends Presenter {

    private Context mContext;
    Button fav;
    String username;
    ImageView favIcon;
    public AnchorDescriptionPresenterView(Context context, String username) {
        mContext = context;
        this.username =username;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_show_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
       ImageView detailImageView = viewHolder.view.findViewById(R.id.detailImageView);
       final RelativeLayout detailShowBackground = viewHolder.view.findViewById(R.id.detailShowBackground);
        TextView detailShowTitle = viewHolder.view.findViewById(R.id.detailShowTitle);
         favIcon = viewHolder.view.findViewById(R.id.favIcon);
        TextView detailShowType = viewHolder.view.findViewById(R.id.detailShowType);
        fav = viewHolder.view.findViewById(R.id.fav);
        final AnchorsListModel dataItem = (AnchorsListModel) item;
        setAnchorFollowIcon(DataHolder.getInstance().getUserDataresponse);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav.getText().toString().trim().equalsIgnoreCase("Following")) {
                    fav.setText("Follow");
                    favIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                 //   favIcon.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext,R.drawable.ic_unavourite_24dp), null);
                    callFollowApi(true);
                } else {
                    fav.setText("Following");
                    favIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_clicked));
                  //  fav.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext,R.drawable.ic_heart_white_24dp), null);
                    callFollowApi(false);
                }

            }
        });
        detailShowTitle.setText(dataItem.about_me);
        detailShowType.setText(dataItem.display_name);
        RequestOptions options = new RequestOptions()
//               / .fitCenter()
                .centerInside()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);    Glide.with(mContext)
                .load(dataItem.profile_pic)
                .apply(options)
                .into(detailImageView);

  /*      Glide.with(mContext)
                .asBitmap()
                .load(dataItem.profile_pic)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(
                            Bitmap resource,
                            Transition<? super Bitmap> transition) {

                        BitmapDrawable background = new BitmapDrawable(resource);
                        detailShowBackground.setBackgroundDrawable(background);
                    }
                });*/

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Nothing to do here.
    }

    private void setAnchorFollowIcon(GetUserDataresponse response) {
        if (response != null && response.follow != null && response.follow.size() > 0) {
            for (int i = 0; i < response.follow.size(); i++) {
                if (response.follow.get(i).equalsIgnoreCase(username)) {
                    fav.setText("Following");
                    favIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_clicked));
               //     fav.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext,R.drawable.ic_heart_white_24dp), null);
                    break;
                }
            }
        }
    }

    public void callFollowApi( boolean isFollow) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(mContext).getPreferencesString(AppConstants.ACCESS_TOKEN));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("actor", username);
        Call<ResponseBody> call;
        if (isFollow) {
            call = ApiClient.getApi().unfollow(headerMap, jsonObject);

        } else {
            call = ApiClient.getApi().follow(headerMap, jsonObject);

        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                GetUserData getUserData = new GetUserData(mContext);
                getUserData.callUserData();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Network error occured", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.getMessage());
            }
        });
    }


}
