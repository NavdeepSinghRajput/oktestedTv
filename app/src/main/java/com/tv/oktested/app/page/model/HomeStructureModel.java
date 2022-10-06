package com.tv.oktested.app.page.model;

import com.google.gson.annotations.SerializedName;
import com.tv.oktested.model.BaseResponse;

import java.util.ArrayList;

public class HomeStructureModel extends BaseResponse {

   /* @SerializedName("status")
    public int status;*/

    @SerializedName("data")
    public ArrayList<HomeStructuteListModel> data;



}
