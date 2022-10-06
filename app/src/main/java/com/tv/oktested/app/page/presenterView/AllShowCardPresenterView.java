package com.tv.oktested.app.page.presenterView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.tv.oktested.R;
import com.tv.oktested.app.page.customised.ImageCardDesignView;
import com.tv.oktested.app.page.entity.DataItem;

public class AllShowCardPresenterView extends Presenter {
    Context mContext;
    ImageView exclusiveView;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        sDefaultBackgroundColor = ContextCompat.getColor(context, R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(context, R.color.pink);

        ImageCardDesignView cardView = new ImageCardDesignView(parent.getContext()) {
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

    private static void updateCardBackgroundColor(ImageCardDesignView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        view.setBackgroundColor(sDefaultBackgroundColor);
        view.findViewById(R.id.selectorInfo).setBackgroundColor(color);
        if (color == sSelectedBackgroundColor) {
            view.findViewById(R.id.selectorView).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.selectorView).setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
     DataItem clip = (DataItem) item;
        if (clip.featureImg != null) {
            ImageCardDesignView cardView = (ImageCardDesignView) viewHolder.view;
            cardView.setTitleText(clip.title);
            cardView.setContentText(clip.show.topic);
            cardView.setDurationText(clip.duration);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(10, 0, 10, 0);

            Glide.with(viewHolder.view.getContext())
                    .load(clip.featureImg)
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.e("unbind", "unbind");

    }
}
