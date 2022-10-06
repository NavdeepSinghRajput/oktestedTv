package com.tv.oktested.app.videoConsumption;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.leanback.app.VideoFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.ControlButtonPresenterSelector;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackControlsRowPresenter;

import com.tv.oktested.DetailsDescriptionPresenter;
import com.tv.oktested.app.page.entity.DataItem;

import java.util.ArrayList;

public class PlaybackOverlayFragments extends VideoFragment {
 
    private static final String TAG = PlaybackOverlayFragments.class.getSimpleName();
 
//    private Movie mSelectedMovie;
    private PlaybackControlsRow mPlaybackControlsRow;
    private ArrayObjectAdapter mPrimaryActionsAdapter;
    private ArrayObjectAdapter mSecondaryActionsAdapter;
 
    private PlaybackControlsRow.PlayPauseAction mPlayPauseAction;
    private PlaybackControlsRow.RepeatAction mRepeatAction;
    private PlaybackControlsRow.ThumbsUpAction mThumbsUpAction;
    private PlaybackControlsRow.ThumbsDownAction mThumbsDownAction;
    private PlaybackControlsRow.ShuffleAction mShuffleAction;
    private PlaybackControlsRow.SkipNextAction mSkipNextAction;
    private PlaybackControlsRow.SkipPreviousAction mSkipPreviousAction;
    private PlaybackControlsRow.FastForwardAction mFastForwardAction;
    private PlaybackControlsRow.RewindAction mRewindAction;
    private PlaybackControlsRow.HighQualityAction mHighQualityAction;
    private PlaybackControlsRow.ClosedCaptioningAction mClosedCaptioningAction;
    private PlaybackControlsRow.MoreActions mMoreActions;
    ArrayList<DataItem> dataItemList;


    public PlaybackOverlayFragments(ArrayList<DataItem> dataItemList, int currentItem) {
        this.dataItemList = dataItemList;
//        this.currentItem = currentItem;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
 
//        mSelectedMovie = (Movie) getActivity().getIntent().getSerializableExtra(DetailsActivity.MOVIE);
 
        setBackgroundType(PlaybackOverlayFragments.BG_LIGHT);
        setFadingEnabled(true);
 
        setUpRows();
    }
 
    private ArrayObjectAdapter mRowsAdapter;
 
    private void setUpRows() {
        ClassPresenterSelector ps = new ClassPresenterSelector();
 
        PlaybackControlsRowPresenter playbackControlsRowPresenter;
        playbackControlsRowPresenter = new PlaybackControlsRowPresenter(new DetailsDescriptionPresenter());
 
        ps.addClassPresenter(PlaybackControlsRow.class, playbackControlsRowPresenter);
        ps.addClassPresenter(ListRow.class, new ListRowPresenter());
        mRowsAdapter = new ArrayObjectAdapter(ps);


        /*
         * Add PlaybackControlsRow to mRowsAdapter, which makes video control UI.
         * PlaybackControlsRow is supposed to be first Row of mRowsAdapter.
         */
        addPlaybackControlsRow();
        /* add ListRow to second row of mRowsAdapter */
//        addOtherRows();

        setAdapter(mRowsAdapter);
 
    }
 
    private void addPlaybackControlsRow() {
        mPlaybackControlsRow = new PlaybackControlsRow(dataItemList);
        mRowsAdapter.add(mPlaybackControlsRow);
 
        ControlButtonPresenterSelector presenterSelector = new ControlButtonPresenterSelector();
        mPrimaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        mSecondaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        mPlaybackControlsRow.setPrimaryActionsAdapter(mPrimaryActionsAdapter);
        mPlaybackControlsRow.setSecondaryActionsAdapter(mSecondaryActionsAdapter);
        
        Activity activity = getActivity();
        mPlayPauseAction = new PlaybackControlsRow.PlayPauseAction(activity);
        mRepeatAction = new PlaybackControlsRow.RepeatAction(activity);
        mThumbsUpAction = new PlaybackControlsRow.ThumbsUpAction(activity);
        mThumbsDownAction = new PlaybackControlsRow.ThumbsDownAction(activity);
        mShuffleAction = new PlaybackControlsRow.ShuffleAction(activity);
        mSkipNextAction = new PlaybackControlsRow.SkipNextAction(activity);
        mSkipPreviousAction = new PlaybackControlsRow.SkipPreviousAction(activity);
        mFastForwardAction = new PlaybackControlsRow.FastForwardAction(activity);
        mRewindAction = new PlaybackControlsRow.RewindAction(activity);
        mHighQualityAction = new PlaybackControlsRow.HighQualityAction(activity);
        mClosedCaptioningAction = new PlaybackControlsRow.ClosedCaptioningAction(activity);
        mMoreActions = new PlaybackControlsRow.MoreActions(activity);
 
        /* PrimaryAction setting */
        mPrimaryActionsAdapter.add(mSkipPreviousAction);
        mPrimaryActionsAdapter.add(mRewindAction);
        mPrimaryActionsAdapter.add(mPlayPauseAction);
        mPrimaryActionsAdapter.add(mFastForwardAction);
        mPrimaryActionsAdapter.add(mSkipNextAction);
 
        /* SecondaryAction setting */
        mSecondaryActionsAdapter.add(mThumbsUpAction);
        mSecondaryActionsAdapter.add(mThumbsDownAction);
        mSecondaryActionsAdapter.add(mRepeatAction);
        mSecondaryActionsAdapter.add(mShuffleAction);
        mSecondaryActionsAdapter.add(mHighQualityAction);
        mSecondaryActionsAdapter.add(mClosedCaptioningAction);
        mSecondaryActionsAdapter.add(mMoreActions);
    }
/*
    private void addOtherRows() {
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        Movie movie = new Movie();
        movie.setTitle("Title");
        movie.setStudio("studio");
        movie.setDescription("description");
        movie.setCardImageUrl("http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02580.jpg");
        listRowAdapter.add(movie);
        listRowAdapter.add(movie);
 
        HeaderItem header = new HeaderItem(0, "OtherRows");
        mRowsAdapter.add(new ListRow(header, listRowAdapter));
    }*/
 
}