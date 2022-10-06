package com.tv.oktested.card;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

import com.tv.oktested.app.page.model.AppExclusiveResponse;

public class ExclusiveListRow extends ListRow {

    private AppExclusiveResponse mCardRow;

    public ExclusiveListRow(HeaderItem header, ObjectAdapter adapter, AppExclusiveResponse cardRow) {
        super(header, adapter);
        setCardRow(cardRow);
    }

    public AppExclusiveResponse getCardRow() {
        return mCardRow;
    }

    public void setCardRow(AppExclusiveResponse cardRow) {
        this.mCardRow = cardRow;
    }
}