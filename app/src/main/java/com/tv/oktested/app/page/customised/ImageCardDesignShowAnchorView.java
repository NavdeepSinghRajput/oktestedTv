/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tv.oktested.app.page.customised;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.leanback.widget.BaseCardView;

import com.tv.oktested.R;

public class ImageCardDesignShowAnchorView extends BaseCardView {

    public static final int CARD_TYPE_FLAG_IMAGE_ONLY = 0;
    public static final int CARD_TYPE_FLAG_TITLE = 1;
    public static final int CARD_TYPE_FLAG_CONTENT = 2;
    public static final int CARD_TYPE_FLAG_ICON_RIGHT = 4;
    public static final int CARD_TYPE_FLAG_ICON_LEFT = 8;

    private static final String ALPHA = "alpha";

    private ImageView mImageView;
    private ViewGroup mInfoArea;
    private TextView mTitleView;
    private TextView mContentView;
    private TextView exclusiveDuration;
    private LinearLayout selectorView;
    private ImageView mBadgeImage;
    private boolean mAttachedToWindow;
    ObjectAnimator mFadeInAnimator;

    /**
     * Create an ImageCardView using a given theme for customization.
     *
     * @param context
     *            The Context the view is running in, through which it can
     *            access the current theme, resources, etc.
     * @param themeResId
     *            The resourceId of the theme you want to apply to the ImageCardView. The theme
     *            includes attributes "imageCardViewStyle", "imageCardViewTitleStyle",
     *            "imageCardViewContentStyle" etc. to customize individual part of ImageCardView.
     * @deprecated Calling this constructor inefficiently creates one ContextThemeWrapper per card,
     * you should share it in card Presenter: wrapper = new ContextThemeWrapper(context, themResId);
     * return new ImageCardView(wrapper);
     */
    @Deprecated
    public ImageCardDesignShowAnchorView(Context context, int themeResId) {
        this(new ContextThemeWrapper(context, themeResId));
    }

    /**
     * @see View#View(Context, AttributeSet, int)
     */
    public ImageCardDesignShowAnchorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildImageCardView(attrs, defStyleAttr, R.style.Widget_Leanback_ImageCardView);
    }

    private void buildImageCardView(AttributeSet attrs, int defStyleAttr, int defStyle) {
        // Make sure the ImageCardView is focusable.
        setFocusable(true);
        setFocusableInTouchMode(true);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.anchor_detail_layout, this);
        TypedArray cardAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.lbImageCardView, defStyleAttr, defStyle);

        mImageView = findViewById(R.id.anchorView);
        if (mImageView.getDrawable() == null) {
            mImageView.setVisibility(View.INVISIBLE);
        }
        // Set Object Animator for image view.
        mFadeInAnimator = ObjectAnimator.ofFloat(mImageView, ALPHA, 1f);
        mFadeInAnimator.setDuration(
                mImageView.getResources().getInteger(android.R.integer.config_shortAnimTime));

        mInfoArea = findViewById(R.id.selectorInfo);
        mTitleView = findViewById(R.id.anchorName);
//        mContentView = findViewById(R.id.exclusiveType);
//        exclusiveDuration = findViewById(R.id.exclusiveDuration);
//        selectorView = findViewById(R.id.selectorView);

        Drawable background = cardAttrs.getDrawable(R.styleable.lbImageCardView_infoAreaBackground);
        if (null != background) {
            setInfoAreaBackground(background);
        }
        // Backward compatibility: There has to be an icon in the default
        // version. If there is one, we have to set its visibility to 'GONE'.
        // Disabling 'adjustIconVisibility' allows the user to set the icon's
        // visibility state in XML rather than code.
        if (mBadgeImage != null && mBadgeImage.getDrawable() == null) {
            mBadgeImage.setVisibility(View.GONE);
        }
        cardAttrs.recycle();
    }

    void setSelectorView(){
        if(selectorView.getVisibility()==VISIBLE){
            selectorView.setVisibility(GONE);
        }else{
            selectorView.setVisibility(VISIBLE);
        }


    }
    /**
     * @see View#View(Context)
     */
    public ImageCardDesignShowAnchorView(Context context) {
        this(context, null);
    }

    /**
     * @see View#View(Context, AttributeSet)
     */
    public ImageCardDesignShowAnchorView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.imageCardViewStyle);
    }

    /**
     * Returns the main image view.
     */
    public final ImageView getMainImageView() {
        return mImageView;
    }

    /**
     * Enables or disables adjustment of view bounds on the main image.
     */
    public void setMainImageAdjustViewBounds(boolean adjustViewBounds) {
        if (mImageView != null) {
            mImageView.setAdjustViewBounds(adjustViewBounds);
        }
    }

    /**
     * Sets the ScaleType of the main image.
     */
    public void setMainImageScaleType(ScaleType scaleType) {
        if (mImageView != null) {
            mImageView.setScaleType(scaleType);
        }
    }

    /**
     * Sets the image drawable with fade-in animation.
     */
    public void setMainImage(Drawable drawable) {
        setMainImage(drawable, true);
    }

    /**
     * Sets the image drawable with optional fade-in animation.
     */
    public void setMainImage(Drawable drawable, boolean fade) {
        if (mImageView == null) {
            return;
        }

        mImageView.setImageDrawable(drawable);
        if (drawable == null) {
            mFadeInAnimator.cancel();
            mImageView.setAlpha(1f);
            mImageView.setVisibility(View.INVISIBLE);
        } else {
            mImageView.setVisibility(View.VISIBLE);
            if (fade) {
                fadeIn();
            } else {
                mFadeInAnimator.cancel();
                mImageView.setAlpha(1f);
            }
        }
    }

    /**
     * Sets the layout dimensions of the ImageView.
     */
    public void setMainImageDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mImageView.setLayoutParams(lp);
    }

    /**
     * Returns the ImageView drawable.
     */
    public Drawable getMainImage() {
        if (mImageView == null) {
            return null;
        }

        return mImageView.getDrawable();
    }

    /**
     * Returns the info area background drawable.
     */
    public Drawable getInfoAreaBackground() {
        if (mInfoArea != null) {
            return mInfoArea.getBackground();
        }
        return null;
    }

    /**
     * Sets the info area background drawable.
     */
    public void setInfoAreaBackground(Drawable drawable) {
        if (mInfoArea != null) {
            mInfoArea.setBackground(drawable);
        }
    }

    /**
     * Sets the info area background color.
     */
    public void setInfoAreaBackgroundColor(@ColorInt int color) {
        if (mInfoArea != null) {
            mInfoArea.setBackgroundColor(color);
        }
    }

    /**
     * Sets the title text.
     */
    public void setTitleText(CharSequence text) {
        if (mTitleView == null) {
            return;
        }
        mTitleView.setText(text);
    }

    /**
     * Returns the title text.
     */
    public CharSequence getTitleText() {
        if (mTitleView == null) {
            return null;
        }

        return mTitleView.getText();
    }

    /**
     * Sets the content text.
     */
    public void setContentText(CharSequence text) {
        if (mContentView == null) {
            return;
        }
        mContentView.setText(text);
    }
    public void setDurationText(CharSequence text) {
        if (exclusiveDuration == null) {
            return;
        }
        exclusiveDuration.setText(text);
    }

    /**
     * Returns the content text.
     */
    public CharSequence getContentText() {
        if (mContentView == null) {
            return null;
        }

        return mContentView.getText();
    }
    public CharSequence getDurationText() {
        if (exclusiveDuration == null) {
            return null;
        }

        return exclusiveDuration.getText();
    }

    /**
     * Sets the badge image drawable.
     */
    public void setBadgeImage(Drawable drawable) {
        if (mBadgeImage == null) {
            return;
        }
        mBadgeImage.setImageDrawable(drawable);
        if (drawable != null) {
            mBadgeImage.setVisibility(View.VISIBLE);
        } else {
            mBadgeImage.setVisibility(View.GONE);
        }
    }

    /**
     * Returns the badge image drawable.
     */
    public Drawable getBadgeImage() {
        if (mBadgeImage == null) {
            return null;
        }

        return mBadgeImage.getDrawable();
    }

    private void fadeIn() {
        mImageView.setAlpha(0f);
        if (mAttachedToWindow) {
            mFadeInAnimator.start();
        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        if (mImageView.getAlpha() == 0) {
            fadeIn();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttachedToWindow = false;
        mFadeInAnimator.cancel();
        mImageView.setAlpha(1f);
        super.onDetachedFromWindow();
    }
}
