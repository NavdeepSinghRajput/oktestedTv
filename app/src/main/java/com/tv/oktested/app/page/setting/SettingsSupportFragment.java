package com.tv.oktested.app.page.setting;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.PagingIndicator;

import com.tv.oktested.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsSupportFragment extends Fragment {
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

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, final ViewGroup container,
           Bundle savedInstanceState) {
       resolveTheme();
       LayoutInflater localInflater = getThemeInflater(inflater);
       final ViewGroup view = (ViewGroup) localInflater.inflate(R.layout.setting_details,
               container, false);

       view.requestFocus();
       return view;
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
       Button lkjh = (Button)view.findViewById(R.id.lkjh);
       lkjh.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(mThemeWrapper, "sadfsghjhk", Toast.LENGTH_SHORT).show();
           }
       });
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


}
