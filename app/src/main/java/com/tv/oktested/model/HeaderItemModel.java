package com.tv.oktested.model;

import androidx.leanback.widget.HeaderItem;

public class HeaderItemModel extends HeaderItem {
    public static final int ICON_NONE = -1;

    /** Hold an icon resource id */
    private int mIconResId = ICON_NONE;

    public HeaderItemModel(long id, String name, int iconResId) {
        super(id, name);
        mIconResId = iconResId;
    }

    public HeaderItemModel(String name) {
        super(name);
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int iconResId) {
        this.mIconResId = iconResId;
    }
}
