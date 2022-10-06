package com.tv.oktested.app.page.model;

import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.entity.ShowDetails;
import com.tv.oktested.model.BaseResponse;

import java.util.ArrayList;

public class VideoListResponse extends BaseResponse {
    public ArrayList<DataItem> data;

    public ShowDetails show_details;
}