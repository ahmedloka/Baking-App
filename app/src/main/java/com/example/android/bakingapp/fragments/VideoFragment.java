package com.example.android.bakingapp.fragments;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.List;

public class VideoFragment extends Fragment {

    public static final String TAG = "tag";

    private List<String> mListThumbnailUrl;
    private List<String> mListVideoUrl;
    private int index;

    private ImageView mImgThumbnail;

    private PlayerView mPlayerView;
    private SimpleExoPlayer player;

    ComponentListener componentListener;

    private int mResumeWindow;
    private long mResumePosition;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps_video, container, false);
        mPlayerView = view.findViewById(R.id.exoplayer);
        mImgThumbnail = view.findViewById(R.id.iv_thumbnail);

        componentListener = new ComponentListener();

        return view;
    }

    private void initializeThumbnail(String path) {
        mPlayerView.setVisibility(View.INVISIBLE);

        if (getActivity() != null) {
            long microSecond = 1000000;
            RequestOptions requestOptions = new RequestOptions()
                    .frame(microSecond)
                    .override(600, 200)
                    .fitCenter()
                    .placeholder(R.drawable.loading_place_holder)
                    .error(R.drawable.error_place_holder);


            Glide.with(getContext())
                    .load(path)
                    .apply(requestOptions)
                    .thumbnail(Glide.with(getContext())
                            .load(path))
                    .into(mImgThumbnail);
        }
    }

    private void initializePlayer(String uri) {
        mImgThumbnail.setVisibility(View.INVISIBLE);

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());


        mPlayerView.setPlayer(player);

        player.setPlayWhenReady(true);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("ExoPlayer")).createMediaSource(Uri.parse(uri));

        player.seekTo(mResumeWindow, mResumePosition);
        player.addListener(componentListener);
        player.prepare(mediaSource);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (mListThumbnailUrl != null && mListThumbnailUrl.get(index) != null && !mListThumbnailUrl.get(index).equals(" ")
                    && !mListThumbnailUrl.get(index).equals("") && mListVideoUrl.get(index).equals(" ") || mListVideoUrl.get(index).equals("")) {
                mPlayerView.setVisibility(View.INVISIBLE);
                mImgThumbnail.setVisibility(View.VISIBLE);
                String path = mListThumbnailUrl.get(index);
                Log.d(TAG, "onStart: " + String.valueOf(path));
                initializeThumbnail(path);

            } else {
                try {
                    if (mPlayerView == null) {

                        mPlayerView = getView().findViewById(R.id.exoplayer);
                    }
                    Log.d(TAG, "onStart: " + mListThumbnailUrl);

                    if (mListVideoUrl != null) {
                        String uri = mListVideoUrl.get(index);

                        if (mListVideoUrl.get(index) != null) {
                            mPlayerView.setVisibility(View.VISIBLE);
                            mImgThumbnail.setVisibility(View.INVISIBLE);
                            initializePlayer(uri);
                        }
                    }

                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, "onResume: ", e);
                }
            }

        } catch (NullPointerException ignore) {

        }
    }


    private void releasePlayer() {
        if (player != null) {
            mResumePosition = player.getCurrentPosition();
            mResumeWindow = player.getCurrentWindowIndex();
            player.release();
            player.removeListener(componentListener);
            player = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPlayerView != null && mPlayerView.getPlayer() != null) {
            mResumeWindow = mPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mPlayerView.getPlayer().getContentPosition());

            releasePlayer();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    private class ComponentListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);

            if (playWhenReady && playbackState == Player.STATE_READY) {
                Log.d(TAG, "onPlayerStateChanged: " + "Playing");
            } else if (playWhenReady) {
                Log.d(TAG, "onPlayerStateChanged: " + "Paused");
                mPlayerView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            super.onPlayerError(error);
            mPlayerView.setVisibility(View.VISIBLE);
        }

    }

    public void setListThumbnailUrl(List<String> mListThumbnailUrl) {
        this.mListThumbnailUrl = mListThumbnailUrl;
    }

    public void setListVideoUrl(List<String> mListVideoUrl) {
        this.mListVideoUrl = mListVideoUrl;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            releasePlayer();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);

        super.onSaveInstanceState(outState);
    }

}
