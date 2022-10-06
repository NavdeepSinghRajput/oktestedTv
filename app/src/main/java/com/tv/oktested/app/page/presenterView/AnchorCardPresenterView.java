package com.tv.oktested.app.page.presenterView;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.tv.oktested.R;
import com.tv.oktested.app.page.customised.ImageCardDesignShowAnchorView;
import com.tv.oktested.app.page.model.AnchorsListModel;

public class AnchorCardPresenterView extends Presenter {
    Context mContext;
    ImageView exclusiveView;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
      /*  mContext = parent.getContext();
        View view = (LayoutInflater.from(mContext).inflate(R.layout.exclusive_detail_layout, null));
        return new ViewHolder(view);*/

        Context context = parent.getContext();
        sDefaultBackgroundColor = ContextCompat.getColor(context, R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(context, R.color.pink);

        ImageCardDesignShowAnchorView cardView = new ImageCardDesignShowAnchorView(parent.getContext()) {
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

    private static void updateCardBackgroundColor(ImageCardDesignShowAnchorView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        view.setBackgroundColor(sDefaultBackgroundColor);
        view.findViewById(R.id.selectorInfo).setBackgroundColor(color);
      /*  if (color == sSelectedBackgroundColor) {
            view.findViewById(R.id.selectorView).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.selectorView).setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
/*        DataItem model = (DataItem) item;
        CardView cardView = viewHolder.view.findViewById(R.id.cardViewExclusive);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        layoutParams.setMargins(10, 0, 10, 0);
        cardView.requestLayout(); TextView exclusiveName = viewHolder.view.findViewById(R.id.exclusiveName);
        TextView exclusiveType = viewHolder.view.findViewById(R.id.exclusiveType);
        exclusiveView = viewHolder.view.findViewById(R.id.exclusiveView);
        exclusiveName.setText(model.title);
        exclusiveType.setText(model.show.topic);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);
        Glide.with(mContext)
                .load(model.featureImg)
                 .apply(options)
             .into(exclusiveView);*/
        AnchorsListModel clip = (AnchorsListModel) item;
        if (clip.profile_pic != null) {
            ImageCardDesignShowAnchorView cardView = (ImageCardDesignShowAnchorView) viewHolder.view;
            cardView.setTitleText(clip.display_name);
//            cardView.setContentText(clip.show.topic);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(10, 0, 10, 0);

            Glide.with(viewHolder.view.getContext())
                    .load(clip.profile_pic)
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.e("unbind", "unbind");

    }
}
