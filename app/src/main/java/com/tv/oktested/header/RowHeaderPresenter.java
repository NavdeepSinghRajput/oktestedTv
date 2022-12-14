/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tv.oktested.header;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;

import com.tv.oktested.R;
import com.tv.oktested.model.HeaderItemModel;


public class RowHeaderPresenter extends Presenter {

    private final int mLayoutResourceId;
    private final Paint mFontMeasurePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mNullItemVisibilityGone;

    public RowHeaderPresenter() {
        this(R.layout.headerdesign);
    }

    /**
     * @hide
     */
    public RowHeaderPresenter(int layoutResourceId) {
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Optionally sets the view visibility to {@link View#GONE} when bound to null.
     */
    public void setNullItemVisibilityGone(boolean nullItemVisibilityGone) {
        mNullItemVisibilityGone = nullItemVisibilityGone;
    }

    /**
     * Returns true if the view visibility is set to {@link View#GONE} when bound to null.
     */
    public boolean isNullItemVisibilityGone() {
        return mNullItemVisibilityGone;
    }

    /**
     * A ViewHolder for the RowHeaderPresenter.
     */
    public static class ViewHolder extends Presenter.ViewHolder {
        float mSelectLevel;
        int mOriginalTextColor;
        float mUnselectAlpha;

        public ViewHolder(View view) {
            super(view);
        }
        public final float getSelectLevel() {
            return mSelectLevel;
        }
    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        LinearLayout headerView = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(mLayoutResourceId, parent, false);

        ViewHolder viewHolder = new ViewHolder(headerView);
//        viewHolder.mOriginalTextColor = headerView.getCurrentTextColor();
        viewHolder.mUnselectAlpha = parent.getResources().getFraction(
                R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        setSelectLevel((ViewHolder) viewHolder, 0);
        HeaderItemModel headerItem = (HeaderItemModel) ((Row) item).getHeaderItem();
        ImageView headerImage = viewHolder.view.findViewById(R.id.headerImage);
        TextView headerText = viewHolder.view.findViewById(R.id.headerText);
        headerText.setText(headerItem.getName());
        View rootView = viewHolder.view;
        rootView.setFocusable(true);
        rootView.setFocusableInTouchMode(true);
        int iconResId = headerItem.getIconResId();
        if( iconResId != HeaderItemModel.ICON_NONE) { // Show icon only when it is set.
            Drawable icon = rootView.getResources().getDrawable(iconResId, null);
            headerImage.setImageDrawable(icon);
        }
//        if (headerItem == null) {
//            ((RowHeaderView) viewHolder.view).setText(null);
//            if (mNullItemVisibilityGone) {
//                viewHolder.view.setVisibility(View.GONE);
//            }
//        } else {
//            viewHolder.view.setVisibility(View.VISIBLE);
//            ((RowHeaderView) viewHolder.view).setText(headerItem.getName());
//        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
//        ((RowHeaderView) viewHolder.view).setText(null);
    }

    /**
     * Sets the select level.
     */
    public final void setSelectLevel(ViewHolder holder, float selectLevel) {
        holder.mSelectLevel = selectLevel;
        onSelectLevelChanged(holder);
    }

    /**
     * Called when the select level changes.  The default implementation sets the alpha on the view.
     */
    protected void onSelectLevelChanged(ViewHolder holder) {
        holder.view.setAlpha(holder.mUnselectAlpha + holder.mSelectLevel *
                (1f - holder.mUnselectAlpha));
    }

    /**
     * Returns the space (distance in pixels) below the baseline of the
     * text view, if one exists; otherwise, returns 0.
     */
    public int getSpaceUnderBaseline(ViewHolder holder) {
        int space = holder.view.getPaddingBottom();
        if (holder.view instanceof TextView) {
            space += (int) getFontDescent((TextView) holder.view, mFontMeasurePaint);
        }
        return space;
    }

    protected static float getFontDescent(TextView textView, Paint fontMeasurePaint) {
        if (fontMeasurePaint.getTextSize() != textView.getTextSize()) {
            fontMeasurePaint.setTextSize(textView.getTextSize());
        }
        if (fontMeasurePaint.getTypeface() != textView.getTypeface()) {
            fontMeasurePaint.setTypeface(textView.getTypeface());
        }
        return fontMeasurePaint.descent();
    }
}
