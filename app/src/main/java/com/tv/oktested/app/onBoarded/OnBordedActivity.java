package com.tv.oktested.app.onBoarded;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.tv.oktested.R;

public class OnBordedActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_borded);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
