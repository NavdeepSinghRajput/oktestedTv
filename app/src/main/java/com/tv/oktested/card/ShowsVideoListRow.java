package com.tv.oktested.card;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

import com.tv.oktested.app.page.model.ShowsVideoResponse;

public class ShowsVideoListRow extends ListRow {

    private ShowsVideoResponse mCardRow;

    public ShowsVideoListRow(HeaderItem header, ObjectAdapter adapter, ShowsVideoResponse cardRow) {
        super(header, adapter);
        setCardRow(cardRow);
    }

    public ShowsVideoResponse getCardRow() {
        return mCardRow;
    }

    public void setCardRow(ShowsVideoResponse cardRow) {
        this.mCardRow = cardRow;
    }
}