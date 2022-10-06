package com.tv.oktested.app.page.presenterView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.leanback.widget.Presenter;

import com.tv.oktested.R;

public class NoDataPresenterView extends Presenter {
    ImageView editor_view;
    Button fav;
    RelativeLayout editorlayout;
    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
     /*   final BaseCardView cardView = new BaseCardView(mContext, null, R.style.SideInfoCardStyle);
        cardView.setFocusable(true);*/
        context = parent.getContext();
        View view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.no_data_layout, null));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Object item) {

    }


    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
