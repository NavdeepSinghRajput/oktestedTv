package com.tv.oktested.app.onBoarded;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.tv.oktested.R;
import com.tv.oktested.header.MainHeaderActivity;
import com.tv.oktested.model.GenerateCodeModel;
import com.tv.oktested.model.VerifyCodeModel;
import com.tv.oktested.network.ApiClient;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnboardSupportFragment extends Fragment {
    private static final String TAG = "OnboardingF";
    private static final boolean DEBUG = false;
    private static final int SLIDE_DISTANCE = 60;
    private static int sSlideDistance;
    // Keys used to save and restore the states.
    private static final String KEY_CURRENT_PAGE_INDEX = "leanback.onboarding.current_page_index";
    private static final String KEY_LOGO_ANIMATION_FINISHED =
            "leanback.onboarding.logo_animation_finished";
    private static final String KEY_ENTER_ANIMATION_FINISHED =
            "leanback.onboarding.enter_animation_finished";

    private ContextThemeWrapper mThemeWrapper;
    TextView generateCode;
    String android_id;
    String code;
    TextView login_url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        resolveTheme();
        LayoutInflater localInflater = getThemeInflater(inflater);
        final ViewGroup view = (ViewGroup) localInflater.inflate(R.layout.onboarded, container, false);
        generateCode = (TextView) view.findViewById(R.id.generateCode);
        login_url = (TextView) view.findViewById(R.id.login_url);
        android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        view.requestFocus();
        generatedCode();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public int onProvideTheme() {
        return -1;
    }

    private void resolveTheme() {
        final Context context = getContext();
        int theme = onProvideTheme();
        if (theme == -1) {
            // Look up the onboardingTheme in the activity's currently specified theme. If it
            // exists, wrap the theme with its value.
            int resId = R.attr.onboardingTheme;
            TypedValue typedValue = new TypedValue();
            boolean found = context.getTheme().resolveAttribute(resId, typedValue, true);
            if (DEBUG) Log.v(TAG, "Found onboarding theme reference? " + found);
            if (found) {
                mThemeWrapper = new ContextThemeWrapper(context, typedValue.resourceId);
            }
        } else {
            mThemeWrapper = new ContextThemeWrapper(context, theme);
        }
    }

    private LayoutInflater getThemeInflater(LayoutInflater inflater) {
        return mThemeWrapper == null ? inflater : inflater.cloneInContext(mThemeWrapper);
    }

    public class GenerateCode {
        String unique_id;
        String code;
    }

    void generatedCode() {

        final GenerateCode generateCodes = new GenerateCode();
        generateCodes.unique_id = android_id;
        Log.e("android_id", android_id);

        Call<GenerateCodeModel> call = ApiClient.getGenerateCode().generateCode(generateCodes);
        call.enqueue(new Callback<GenerateCodeModel>() {
            @Override
            public void onResponse(Call<GenerateCodeModel> call, Response<GenerateCodeModel> response) {
                if (response.isSuccessful()) {
                    generateCode.setText(response.body().getData().getCode());
                    login_url.setText(response.body().getData().getLogin_show_url());
                    code = generateCode.getText().toString();
//                    startActivity(new Intent(getContext(),MainActivity.class));
                    int interval =Integer.parseInt(response.body().getData().getInterval())*1000;
                    verifycode(response.body().getData().getVerification_url(),interval);

                }
            }

            @Override
            public void onFailure(Call<GenerateCodeModel> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.getMessage());
            }
        });
    }

    Handler handler;

    void verifycode(final String verification_url, final int interval) {
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GenerateCode generateCodes = new GenerateCode();
                generateCodes.unique_id = android_id;
                generateCodes.code = code;
               /* startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();*/

                Call<VerifyCodeModel> call = ApiClient.getVerifyGenerateCode(verification_url).verifyCode(generateCodes);
                call.enqueue(new Callback<VerifyCodeModel>() {
                    @Override
                    public void onResponse(Call<VerifyCodeModel> call, Response<VerifyCodeModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("success")) {
                                Log.e("success", "sucess");
                                AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.ACCESS_TOKEN, response.body().getAccess_token());
                                AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.UID, response.body().getUser().getId());
                                AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.NAME, response.body().getUser().getName());
                                AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.DISPLAY_NAME, response.body().getUser().getDisplay_name());
                                AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.MOBILE, response.body().getUser().getMobile());
                                AppPreferences.getInstance(getContext()).savePreferencesString(AppConstants.EMAIL, response.body().getUser().getEmail());
                                startActivity(new Intent(getContext(), MainHeaderActivity.class));
                                getActivity().finish();
                            } else if (response.body().getStatus().equalsIgnoreCase("pending")) {
                                verifycode(verification_url,interval);
                            } else if (response.body().getStatus().equalsIgnoreCase("fail")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
//                                builder.setTitle("Note");
                                builder.setMessage(response.body().getMessage());
                                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setMessage("Exiting....");
                                        progressDialog.setCancelable(true);
                                        progressDialog.show();
                                        getActivity().finish();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyCodeModel> call, Throwable t) {
                        Toast.makeText(getActivity().getBaseContext(), "Network error occured", Toast.LENGTH_SHORT).show();
                        Log.e("Failure", t.getMessage());

                    }
                });
            }
        }, interval);

    }

}
