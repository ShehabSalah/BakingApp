package com.shehabsalah.bakingapp.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.data.ingredient.Ingredient;
import com.shehabsalah.bakingapp.data.Step;
import com.shehabsalah.bakingapp.ui.adapters.IngredientAdapter;
import com.shehabsalah.bakingapp.util.Config;
import com.shehabsalah.bakingapp.util.ExoPlayer.AdaptiveTrackSelection;
import com.squareup.picasso.Picasso;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shehab Salah on 6/9/17.
 *
 */

public class StepFragment extends Fragment implements ExoPlayer.EventListener{
    private final String TAG = StepFragment.class.getSimpleName();
    private ArrayList<Ingredient> ingredients;
    private Step step;
    private String playerURL;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;
    private static boolean shouldRestorePosition = false;
    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }
    private static int resumeWindow;
    private static long resumePosition;


    @BindView(R.id.video_holder_layout) CardView video_holder_layout;
    @BindView(R.id.playerView) SimpleExoPlayerView playerView;
    @BindView(R.id.step_holder_layout) CardView step_holder_layout;
    @BindView(R.id.shortDescribe) TextView shortDescribe;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.ingredients_holder_layout) CardView ingredients_holder_layout;
    @BindView(R.id.ingredients_list) RecyclerView ingredients_list;
    @BindView(R.id.videoThumbnail) ImageView videoThumbnail;


    public StepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.step_details, container, false);
        ButterKnife.bind(this,mainView);
        if (savedInstanceState == null){
            clearResumePosition();
        }else{
            step = savedInstanceState.containsKey(Config.STEP_STATE)?
                    (Step) savedInstanceState.getParcelable(Config.STEP_STATE) :null;
            resumeWindow = savedInstanceState.getInt(Config.WINDOW_STATE);
            resumePosition = savedInstanceState.getLong(Config.POSITION_STATE);
            shouldRestorePosition = savedInstanceState.getBoolean(Config.RESTORE_STATE);
            if (savedInstanceState.containsKey(Config.BUNUEL_STATE)) {
                Bundle b = savedInstanceState.getBundle(Config.BUNUEL_STATE);
                if (b!=null&&b.containsKey(Config.INGR_STATE))
                    ingredients = b.getParcelableArrayList(Config.INGR_STATE);
            }
        }
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (step!=null){
            displayStep();
        }else if(ingredients!=null){
            displayIngredients();
        }
    }

    private void displayStep(){
        // Show step layout
        step_holder_layout.setVisibility(View.VISIBLE);

        // Display step details
        shortDescribe.setText(step.getShortDescription());
        description.setText(step.getDescription());

        // If step has video display the video
        if (step.getVideoURL()!=null && !step.getVideoURL().isEmpty()){
            if (step.getThumbnailURL()!=null && !step.getThumbnailURL().isEmpty()){
                videoThumbnail.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(step.getThumbnailURL())
                        .error(R.mipmap.holder)
                        .placeholder(R.mipmap.holder)
                        .into(videoThumbnail);
            }


            // Show the video layout
            video_holder_layout.setVisibility(View.VISIBLE);
            // Initialize the Media Session.
            initializeMediaSession();
            playerURL = step.getVideoURL();
            // Initialize the player.
            initializePlayer(Uri.parse(playerURL));
        }
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    private void displayIngredients(){
        // Show ingredient layout
        ingredients_holder_layout.setVisibility(View.VISIBLE);

        // Display the Ingredient list
        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredients);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ingredients_list.setLayoutManager(verticalLayoutManager);
        //add ItemDecoration
        ingredients_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        ingredients_list.setAdapter(ingredientAdapter);
    }


    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);
        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);
        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());
        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(mExoPlayer);
            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "VideoPlayer");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            if (shouldRestorePosition) {
                mExoPlayer.seekTo(resumeWindow, resumePosition);
            }
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            videoThumbnail.setVisibility(View.GONE);

        }
    }

    private void updateResumePosition() {
        if (mExoPlayer!=null){
            resumeWindow = mExoPlayer.getCurrentWindowIndex();
            resumePosition = mExoPlayer.isCurrentWindowSeekable() ? Math.max(0, mExoPlayer.getCurrentPosition())
                    : C.TIME_UNSET;
        }
    }

    private void clearResumePosition() {
        shouldRestorePosition = false;
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    /**
     * Release the player.
     */
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
        if (mMediaSession!=null){
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }


    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (mExoPlayer!=null){
            if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        mExoPlayer.getCurrentPosition(), 1f);
            } else if((playbackState == ExoPlayer.STATE_READY)){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        mExoPlayer.getCurrentPosition(), 1f);
            }
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }
    }
    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (isBehindLiveWindow(error)) {
            clearResumePosition();
            initializePlayer(Uri.parse(playerURL));
        } else {
            updateResumePosition();
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        updateResumePosition();
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mExoPlayer!=null){
            Timeline timeline = mExoPlayer.getCurrentTimeline();
            if (timeline != null) {
                resumeWindow = mExoPlayer.getCurrentWindowIndex();
                Timeline.Window window = timeline.getWindow(resumeWindow, new Timeline.Window());
                if (!window.isDynamic) {
                    shouldRestorePosition = true;
                    resumePosition = window.isSeekable ? mExoPlayer.getCurrentPosition() : C.TIME_UNSET;
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (step!=null){
            shouldRestorePosition = true;
            updateResumePosition();
            outState.putParcelable(Config.STEP_STATE,step);
            outState.putInt(Config.WINDOW_STATE,resumeWindow);
            outState.putLong(Config.POSITION_STATE,resumePosition);
            outState.putBoolean(Config.RESTORE_STATE,shouldRestorePosition);
        }
        if (ingredients!=null) {
            Bundle b = new Bundle();
            b.putParcelableArrayList(Config.INGR_STATE, ingredients);
            outState.putBundle(Config.BUNUEL_STATE,b);
        }
        super.onSaveInstanceState(outState);
    }
}