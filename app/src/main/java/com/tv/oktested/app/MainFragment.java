package com.tv.oktested.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.tv.oktested.R;
import com.tv.oktested.app.page.PageRowFragmentFactory;
import com.tv.oktested.app.search.SearchActivity;
import com.tv.oktested.card.HeaderItemPresenter;
import com.tv.oktested.model.HeaderItemModel;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;
import com.tv.oktested.utils.GetUserData;

import org.json.JSONException;
import org.json.JSONObject;

public class MainFragment extends BrowseFragment {
    BackgroundManager mBackgroundManager;
//    String id= "5eb1565187c0a3374fc066df";
//    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1ODg5MjczNTksIm5iZiI6MTU4ODkyNzM1OSwianRpIjoiOWRkNGYwMTEtZmU1ZS00YzA3LTg0ZGYtYzUxNjQxOGQzMGNhIiwiaWRlbnRpdHkiOiI1ZWIxNTY1MTg3YzBhMzM3NGZjMDY2ZGYiLCJmcmVzaCI6ZmFsc2UsInR5cGUiOiJhY2Nlc3MifQ.tme6STyMz6vOggTSaesA7-PwVc4bI6QBbDyubUfxL7Q @Override";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.ACCESS_TOKEN, token);
//        AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.UID, id);
//        AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.NAME, "Navdeep Singh");
//        AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.DISPLAY_NAME, "Navdeep Singh");
//        AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.MOBILE, "98765433467");
//        AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.EMAIL, "rajputnaviratore@gmail.com");
        setupUIElements();
        createRows();
        callUserData();
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(getResources().getColor(R.color.background_gradient_start,null));
        getMainFragmentRegistry().registerFragment(PageRow.class, new PageRowFragmentFactory(getContext(),mBackgroundManager));
        startHeadersTransition(true);
    }

    private void setupUIElements() {
//        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.pink));
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.pink));
        setTitle("OK tested");
        setBadgeDrawable(getResources().getDrawable(R.drawable.oktested_logo));// Badge, when set, takes precedent
         showTitle(false);
//        showTitle(0);
        getHeadersState();
        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new HeaderItemPresenter();
            }
        });
//        prepareEntranceTransition();
        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getBaseContext(), SearchActivity.class));

            }
        });

    }

    public void callUserData() {
        GetUserData getUserData = new GetUserData(getActivity().getBaseContext());
        getUserData.callUserData();

    }


    private void createRows() {

        ArrayObjectAdapter  mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        HeaderItemModel headerItem1 = new HeaderItemModel(1, "Home", R.drawable.ic_home_white_24dp);
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);

        HeaderItemModel headerItem3 = new HeaderItemModel(3, "Shows", R.drawable.ic_eye_white_24dp);
        PageRow pageRow3 = new PageRow(headerItem3);
        mRowsAdapter.add(pageRow3);
        HeaderItemModel headerItem4 = new HeaderItemModel(4, "Anchors", R.drawable.ic_star_white_24dp);
        PageRow pageRow4 = new PageRow(headerItem4);
        mRowsAdapter.add(pageRow4);

        HeaderItemModel headerItem5 = new HeaderItemModel(5, "Favourite", R.drawable.ic_heart_white_24dp);
        PageRow pageRow5 = new PageRow(headerItem5);
        mRowsAdapter.add(pageRow5);

        HeaderItemModel headerItem6 = new HeaderItemModel(6, "Account", R.drawable.ic_person_black_24dp);
        PageRow pageRow6 = new PageRow(headerItem6);
        mRowsAdapter.add(pageRow6);

        setAdapter(mRowsAdapter);

    }

}
