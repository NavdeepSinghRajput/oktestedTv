package com.tv.oktested.app.page;

import android.content.Intent;
import android.os.Bundle;

import androidx.leanback.app.RowsFragment;

import com.tv.oktested.app.page.detail.DetailActivity;

public class SettingRowFragment extends RowsFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class).putExtra("type","Account");
        startActivity(intent);
    }
}
