package com.tv.oktested.app.page;

import android.app.Fragment;
import android.content.Context;

import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.Row;

public class PageRowFragmentFactory extends BrowseFragment.FragmentFactory<Fragment> {
    BackgroundManager mBackgroundManager;
    Context context;

    public PageRowFragmentFactory(Context context, BackgroundManager backgroundManager) {
        this.mBackgroundManager = backgroundManager;
        this.context =context;
    }

    @Override
    public Fragment createFragment(Object rowObj) {
        Row row = (Row) rowObj;
        mBackgroundManager.setDrawable(null);
        if (row.getHeaderItem().getId() == 1) {
            return new HomeFragment();
        }/* else if (row.getHeaderItem().getId() == 2) {
            return new SearchRowFragment();
        } */else if (row.getHeaderItem().getId() == 3) {
            return new ShowsFragment();
        } else if (row.getHeaderItem().getId() == 4) {
            return new AnchorsFragment();
        } else if (row.getHeaderItem().getId() == 5) {
            return new FavouriteFragment();
        }else if (row.getHeaderItem().getId() == 6) {
            return new SettingRowFragment();
        }

//        throw new IllegalArgumentException(String.format("Invalid row %s", rowObj));
        return null;
    }
}
