package com.tv.oktested.card;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

import com.tv.oktested.app.page.model.ShowsResponse;

public class ShowListRow extends ListRow {

    private ShowsResponse mCardRow;

    public ShowListRow(HeaderItem header, ObjectAdapter adapter, ShowsResponse cardRow) {
        super(header, adapter);
        setCardRow(cardRow);
    }

    public ShowsResponse getCardRow() {
        return mCardRow;
    }

    public void setCardRow(ShowsResponse cardRow) {
        this.mCardRow = cardRow;
    }
}