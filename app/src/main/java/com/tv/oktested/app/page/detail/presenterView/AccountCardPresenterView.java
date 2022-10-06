package com.tv.oktested.app.page.detail.presenterView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.leanback.widget.Presenter;

import com.tv.oktested.R;
import com.tv.oktested.header.MainHeaderActivity;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;
import com.tv.oktested.utils.Helper;

public class AccountCardPresenterView extends Presenter {
    Context context;
    Button fav;
    Activity activity;

    public AccountCardPresenterView(Activity activity) {
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
     /*   final BaseCardView cardView = new BaseCardView(mContext, null, R.style.SideInfoCardStyle);
        cardView.setFocusable(true);*/
        context = parent.getContext();
        View view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.terms_condition_layout, null));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Object item) {
        Button back = viewHolder.view.findViewById(R.id.back);
        Button logOut = viewHolder.view.findViewById(R.id.logOut);
        TextView emailId = viewHolder.view.findViewById(R.id.emailId);
        TextView versionANme = viewHolder.view.findViewById(R.id.versionANme);
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

        versionANme.setText("App Version : "+version);
        final LinearLayout detailsUser = viewHolder.view.findViewById(R.id.detailsUser);
        TextView emailHeader = viewHolder.view.findViewById(R.id.emailHeader);
        TextView displayName = viewHolder.view.findViewById(R.id.displayName);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainHeaderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(sure.getVisibility()==View.VISIBLE){
                       }else{
                    sure.setVisibility(View.VISIBLE);
                    detailsUser.setVisibility(View.GONE);
                }*/

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
                builder.setTitle("LogOut");
                builder.setMessage("Are you sure you want to logout from OKTESTED APP ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog progressDialog = new ProgressDialog(activity);
                        progressDialog.setMessage("Loging out");
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        AppPreferences.getInstance(context).savePreferencesString(AppConstants.ACCESS_TOKEN, null);
                        activity.finishAffinity();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        if (Helper.isContainValue(AppPreferences.getInstance(context).getPreferencesString(AppConstants.DISPLAY_NAME))) {
            displayName.setText(AppPreferences.getInstance(context).getPreferencesString(AppConstants.DISPLAY_NAME));
        } else {
            displayName.setText(AppPreferences.getInstance(context).getPreferencesString(AppConstants.NAME));
        }

        if (Helper.isContainValue(AppPreferences.getInstance(context).getPreferencesString(AppConstants.EMAIL))) {
            emailId.setText(AppPreferences.getInstance(context).getPreferencesString(AppConstants.EMAIL));
        } else {
            emailId.setText(AppPreferences.getInstance(context).getPreferencesString(AppConstants.MOBILE));
        }

//        emailId.setText(AppPreferences.getInstance(context).getPreferencesString(AppConstants.EMAIL));
//        displayName.setText(AppPreferences.getInstance(context).getPreferencesString(AppConstants.ACCESS_TOKEN));


    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.e("backpressed", "onbackpressed");
        Intent intent = new Intent(context, MainHeaderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }


}
