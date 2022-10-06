package com.tv.oktested.app.page.presenterView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tv.oktested.R;
import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.EditorPickModel;
import com.tv.oktested.app.videoConsumption.VideoActivity;

import java.util.ArrayList;

public class EditorCardPresenterView extends Presenter {
    ImageView editor_view;
    Button fav;
    RelativeLayout editorlayout;
    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
     /*   final BaseCardView cardView = new BaseCardView(mContext, null, R.style.SideInfoCardStyle);
        cardView.setFocusable(true);*/
        context = parent.getContext();
        View view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_detail_layout, null));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Object item) {
        editor_view = viewHolder.view.findViewById(R.id.editor_view);
        editorlayout = viewHolder.view.findViewById(R.id.editorlayout);
       TextView editorShowType = viewHolder.view.findViewById(R.id.editorShowType);
        TextView editorShowDes = viewHolder.view.findViewById(R.id.editorShowDes);
        TextView editorShowCreatedOn = viewHolder.view.findViewById(R.id.editorShowCreatedOn);
        TextView editorShowTitle = viewHolder.view.findViewById(R.id.editorShowTitle);
        Button watchNow = viewHolder.view.findViewById(R.id.watchNow);
        final EditorPickModel dataItem = (EditorPickModel) item;
        fav = viewHolder.view.findViewById(R.id.fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sdsdf", "Dsds");
            }
        });
        editorShowTitle.setText(dataItem.data.title);
        editorShowDes.setText(dataItem.data.duration + " minutes");
        editorShowType.setText(dataItem.data.show.topic);
        String[] strings = dataItem.data.pubDate.split("at");
        editorShowCreatedOn.setText(strings[0]);
        watchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<DataItem> dataItems = new ArrayList<>();
                dataItems.add(dataItem.data);
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putParcelableArrayListExtra("dataItemList", dataItems);
                intent.putExtra("position", 0);
                context.startActivity(intent);
//                }
            }
        });

        RequestOptions options = new RequestOptions()
//               / .fitCenter()
                .centerInside()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);
    /*    Glide.with(context)
                .load(dataItem.data.onexone_img)
                .apply(options)
                .into(coverPic);*/
//        editor_view.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
        Glide.with(context)
                .load(dataItem.data.onexone_img)
                .apply(options)
                .into(editor_view);

        /*Glide.with(context)
                .asBitmap()
                .load(dataItem.data)
                .apply(options)
                .into(new SimpleTarget<Bitmap>(200,200) {
                    @Override
                    public void onResourceReady(
                            Bitmap resource,
                            Transition<? super Bitmap> transition) {
                        BitmapDrawable background = new BitmapDrawable(resource);
                        coverPic.setBackgroundDrawable(background);
                    }
                });*/
    }


    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
