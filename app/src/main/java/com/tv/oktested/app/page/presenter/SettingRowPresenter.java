package com.tv.oktested.app.page.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;

import com.tv.oktested.R;


public class SettingRowPresenter extends RowPresenter {

    Presenter mDetailsPresenter;
    Context context;

    public SettingRowPresenter(Presenter presenter) {
        mDetailsPresenter = presenter;
    }

    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vh_details, parent, false);
        ViewHolder vh = new ViewHolder(v, mDetailsPresenter);
//        initDetailsOverview(vh);

        return vh;
    }

    public final class ViewHolder extends RowPresenter.ViewHolder {
        final FrameLayout mOverviewFrame;
        final FrameLayout mDetailsDescriptionFrame;
        public final Presenter.ViewHolder mDetailsDescriptionViewHolder;
        final Handler mHandler = new Handler();

        public ViewHolder(View rootView, Presenter detailsPresenter) {
            super(rootView);
            mOverviewFrame = (FrameLayout) rootView.findViewById(R.id.details_frame);
            mDetailsDescriptionFrame = (FrameLayout) rootView.findViewById(R.id.details_overview_description);
            mDetailsDescriptionViewHolder = detailsPresenter.onCreateViewHolder(mDetailsDescriptionFrame);
            mDetailsDescriptionFrame.addView(mDetailsDescriptionViewHolder.view);
        }
    }

    private void initDetailsOverview(final ViewHolder vh) {
        final View overview = vh.mOverviewFrame;
        ViewGroup.LayoutParams lp = overview.getLayoutParams();
//        lp.height = getCardHeight(overview.getContext());
        lp.height = 500;
        overview.setLayoutParams(lp);

        if (!getSelectEffectEnabled()) {
            vh.mOverviewFrame.setForeground(context.getResources().getDrawable(R.drawable.editor_gradient,null));
        }
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);

        DetailsOverviewRow row = (DetailsOverviewRow) item;
        ViewHolder vh = (ViewHolder) holder;

        //   bindImageDrawable(vh);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        ViewGroup.LayoutParams layoutParams = vh.mDetailsDescriptionViewHolder.view.getLayoutParams();
        layoutParams.width = width ;
        layoutParams.height = height;
        vh.mDetailsDescriptionViewHolder.view.setLayoutParams(layoutParams);

        //   vh.mDetailsDescriptionViewHolder.view.requestLayout();

        mDetailsPresenter.onBindViewHolder(vh.mDetailsDescriptionViewHolder, row.getItem());
//        vh.bindActions(row.getActionsAdapter());
//        row.addListener(vh.mListener);
    }

    @Override
    protected void onUnbindRowViewHolder(RowPresenter.ViewHolder holder) {
        ViewHolder vh = (ViewHolder) holder;
        DetailsOverviewRow dor = (DetailsOverviewRow) vh.getRow();
//        dor.removeListener(vh.mListener);
        if (vh.mDetailsDescriptionViewHolder != null) {
            mDetailsPresenter.onUnbindViewHolder(vh.mDetailsDescriptionViewHolder);
        }
        super.onUnbindRowViewHolder(holder);
    }

    @Override
    public final boolean isUsingDefaultSelectEffect() {
        return false;
    }

    @Override
    protected void onSelectLevelChanged(RowPresenter.ViewHolder holder) {
        super.onSelectLevelChanged(holder);
        if (getSelectEffectEnabled()) {
            ViewHolder vh = (ViewHolder) holder;
//            int dimmedColor = vh.mColorDimmer.getPaint().getColor();
//            ((ColorDrawable) vh.mOverviewFrame.getForeground().mutate()).setColor(dimmedColor);
        }
    }

    @Override
    protected void onRowViewAttachedToWindow(RowPresenter.ViewHolder vh) {
        super.onRowViewAttachedToWindow(vh);
        if (mDetailsPresenter != null) {
            mDetailsPresenter.onViewAttachedToWindow(
                    ((ViewHolder) vh).mDetailsDescriptionViewHolder);
        }
    }

    @Override
    protected void onRowViewDetachedFromWindow(RowPresenter.ViewHolder vh) {
        super.onRowViewDetachedFromWindow(vh);
        if (mDetailsPresenter != null) {
            mDetailsPresenter.onViewDetachedFromWindow(
                    ((ViewHolder) vh).mDetailsDescriptionViewHolder);
        }
    }
}
