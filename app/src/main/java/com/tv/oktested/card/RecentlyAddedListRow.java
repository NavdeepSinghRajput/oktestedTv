package com.tv.oktested.card;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

import com.tv.oktested.app.page.model.VideoListResponse;

public class RecentlyAddedListRow extends ListRow {

    private VideoListResponse mCardRow;

    public RecentlyAddedListRow(HeaderItem header, ObjectAdapter adapter, VideoListResponse cardRow) {
        super(header, adapter);
        setCardRow(cardRow);
    }

    public VideoListResponse getCardRow() {
        return mCardRow;
    }

    public void setCardRow(VideoListResponse cardRow) {
        this.mCardRow = cardRow;
    }
}