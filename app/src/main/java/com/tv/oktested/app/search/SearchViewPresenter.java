package com.tv.oktested.app.search;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.tv.oktested.R;

public class SearchViewPresenter extends Presenter {
    int CARD_WIDTH = 313;
    int CARD_HEIGHT = 176;
    int sSelectedBackgroundColor;
    int sDefaultBackgroundColor;
    Drawable mDefaultCardImage;


    void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.selected_background);
        /*
         * This template uses a default image in res/drawable, but the general case for Android TV
         * will require your resources in xhdpi. For more information, see
         * https://developer.android.com/training/tv/start/layouts.html#density-resources
         */
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);

        ImageCardView cardView =
                new ImageCardView(parent.getContext()) {
                    @Override
                    public void setSelected(boolean selected) {
                        updateCardBackgroundColor(this, selected);
                        super.setSelected(selected);
                    }
                };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
