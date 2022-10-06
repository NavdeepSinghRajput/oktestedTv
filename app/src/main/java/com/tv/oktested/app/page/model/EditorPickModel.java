package com.tv.oktested.app.page.model;


import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.model.BaseResponse;

public class EditorPickModel extends BaseResponse {
    public DataItem data;

    public DataItem getData() {
        return data;
    }

    public void setData(DataItem data) {
        this.data = data;
    }
}