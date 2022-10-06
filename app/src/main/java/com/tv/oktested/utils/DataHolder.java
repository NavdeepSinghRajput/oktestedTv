package com.tv.oktested.utils;

import com.tv.oktested.app.page.model.AnchorsListModel;
import com.tv.oktested.app.page.model.GetUserDataresponse;

import java.util.ArrayList;

public class DataHolder {

    private static volatile DataHolder instance;
    public ArrayList<AnchorsListModel> anchorList = new ArrayList<>();
    public GetUserDataresponse getUserDataresponse;

    private DataHolder() {
    }

    public synchronized static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }
}