package com.tv.oktested.app.page.detail.presenterView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
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
import com.tv.oktested.app.page.entity.ShowDetails;
import com.tv.oktested.app.page.model.GetUserDataresponse;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;
import com.tv.oktested.utils.DataHolder;
import com.tv.oktested.utils.GetUserData;
import com.tv.oktested.utils.Helper;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailShowCardPresenterView extends Presenter {
    Context context;
    Button fav;
    String slug;
    ImageView favIcon;

    public DetailShowCardPresenterView(String slug) {
        this.slug =slug;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
     /*   final BaseCardView cardView = new BaseCardView(mContext, null, R.style.SideInfoCardStyle);
        cardView.setFocusable(true);*/
        context = parent.getContext();
        View view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_show_layout, null));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Object item) {

       ImageView detailImageView = viewHolder.view.findViewById(R.id.detailImageView);
       final RelativeLayout detailShowBackground = viewHolder.view.findViewById(R.id.detailShowBackground);
        TextView detailShowTitle = viewHolder.view.findViewById(R.id.detailShowTitle);
        TextView detailShowType = viewHolder.view.findViewById(R.id.detailShowType);
        favIcon = viewHolder.view.findViewById(R.id.favIcon);
        fav = viewHolder.view.findViewById(R.id.fav);
        final ShowDetails dataItem = (ShowDetails) item;
        setShowFollowIcon(DataHolder.getInstance().getUserDataresponse, slug);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fav.getText().toString().trim().equalsIgnoreCase("Following")) {
                    fav.setText("Follow");
                    favIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
                    callShowFollowApi(true);
                } else {
                    fav.setText("Following");
                      callShowFollowApi(false);
                    favIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_clicked));
                }

            }
        });
        detailShowTitle.setText(dataItem.topic_desc);
        detailShowType.setText(dataItem.topic_name);
        RequestOptions options = new RequestOptions()
//               / .fitCenter()
                .centerInside()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);

        Glide.with(context)
                .load(dataItem.onexone_img)
                .apply(options)
                .into(detailImageView);

    /*    RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.placeholder);

        Glide.with(context)
                .asBitmap()
                .load(dataItem.onexone_img)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(
                            Bitmap resource,
                            Transition<? super Bitmap> transition) {

                        BitmapDrawable background = new BitmapDrawable(resource);
                        detailShowBackground.setBackgroundDrawable(background);
                    }
                });
*/
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    private void setShowFollowIcon(GetUserDataresponse response, String slug) {
        if (response != null && response.favourite_shows != null && response.favourite_shows.size() > 0) {
            for (int i = 0; i < response.favourite_shows.size(); i++) {
                if (response.favourite_shows.get(i).equalsIgnoreCase(slug)) {
                    fav.setText("Following");
                    favIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_clicked));
                    break;
                }
            }
        }
    }

    private void callShowFollowApi(boolean isFollow) {
        if (Helper.isNetworkAvailable(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.ACCESS_TOKEN));
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("show", slug);
            Call<ResponseBody> call;
            if (isFollow) {
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
                    Log.e("favouriteFailure", t.getMessage());
                    Toast.makeText(context, "Network error occured", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
//            showMessage(getString(R.string.please_check_internet_connection));
        }
    }

}
