package com.tv.oktested.app.page.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tv.oktested.R;
import com.tv.oktested.app.page.model.AnchorsListModel;
import com.tv.oktested.header.MainHeaderActivity;

public class DetailActivity extends Activity {
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            type = getIntent().getExtras().getString("type");
            if (type.equalsIgnoreCase("Anchor")) {
                AnchorsListModel anchorsListModel = (AnchorsListModel) getIntent().getSerializableExtra("anchorModel");
                Bundle bundle = new Bundle();
                bundle.putString("username", getIntent().getExtras().getString("username"));
                bundle.putSerializable("anchorModel", anchorsListModel);
                AnchorDetailFragment fragment = new AnchorDetailFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.details_fragment, fragment)
                        .commit();
            } else if (type.equalsIgnoreCase("Shows")) {
                String slug = getIntent().getExtras().getString("slug");
                String showName = getIntent().getExtras().getString("showName");
                Bundle bundle = new Bundle();
                bundle.putString("slug", slug);
                bundle.putString("showName", showName);
                DetailShowLayoutFragment fragment = new DetailShowLayoutFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.details_fragment, fragment)
                        .commit();
            } else if (type.equalsIgnoreCase("Account")) {
                AccountDetailFragment fragment = new AccountDetailFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.details_fragment, fragment)
                        .commit();

            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("backpressed", "onbackpressed");
        if (type.equalsIgnoreCase("Account")) {
            Intent intent = new Intent(getApplicationContext(), MainHeaderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finishAffinity();
        } else {
            super.onBackPressed();

        }

    }
}
