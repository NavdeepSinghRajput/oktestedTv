package com.tv.oktested.card;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

import com.tv.oktested.app.page.model.AnchorsResponse;

public class AnchorListRow extends ListRow {

    private AnchorsResponse mCardRow;

    public AnchorListRow(HeaderItem header, ObjectAdapter adapter, AnchorsResponse cardRow) {
        super(header, adapter);
        setCardRow(cardRow);
    }

    public AnchorsResponse getCardRow() {
        return mCardRow;
    }

    public void setCardRow(AnchorsResponse cardRow) {
        this.mCardRow = cardRow;
    }
}