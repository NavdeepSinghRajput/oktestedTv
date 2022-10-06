package com.tv.oktested.app.videoConsumption;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.tv.oktested.R;
import com.tv.oktested.app.page.entity.DataItem;

import java.util.ArrayList;

public class VideoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ArrayList<DataItem> dataItemList = getIntent().getParcelableArrayListExtra("dataItemList");
        int currentItem = getIntent().getExtras().getInt("position");

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.videoFragment, new VideoConsumptionExampleFragment(dataItemList,currentItem),
                    VideoConsumptionExampleFragment.TAG);
//            ft.add(R.id.videoFragment, new PlaybackOverlayFragments(dataItemList,currentItem),
//                    VideoConsumptionExampleFragment.TAG);
            ft.commit();
        }
    }


}
