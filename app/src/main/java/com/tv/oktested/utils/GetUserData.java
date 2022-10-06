package com.tv.oktested.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tv.oktested.app.page.model.GetUserResponse;
import com.tv.oktested.network.ApiClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserData {

    Context context;
    public GetUserData(Context context) {
        this.context =context;
    }

    public void callUserData() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.ACCESS_TOKEN));
        String id = AppPreferences.getInstance(context).getPreferencesString(AppConstants.UID);
        Call<GetUserResponse> call = ApiClient.getApi().getUserData(headerMap, id);
        call.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                GetUserResponse getUserResponse = response.body();
                if (getUserResponse != null && getUserResponse.data != null) {
                    DataHolder.getInstance().getUserDataresponse = getUserResponse.data;
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                Toast.makeText(context, "Network error occured", Toast.LENGTH_SHORT).show();
                callUserData();
                Log.e("Failure", t.getMessage());
            }
        });
    }

}
