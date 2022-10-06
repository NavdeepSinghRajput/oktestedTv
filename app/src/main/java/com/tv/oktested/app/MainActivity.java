package com.tv.oktested.app;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.tv.oktested.R;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* if (savedInstanceState == null) {
            Fragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment)
                    .commit();
        }*/
    }
}
