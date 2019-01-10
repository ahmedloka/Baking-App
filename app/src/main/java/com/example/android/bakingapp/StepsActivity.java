package com.example.android.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.ViewModel.ViewModelMain;
import com.example.android.bakingapp.fragments.PositionFragment;
import com.example.android.bakingapp.fragments.StepsFragment;
import com.example.android.bakingapp.fragments.VideoFragment;
import com.example.android.bakingapp.retrofit.Post;
import com.example.android.bakingapp.retrofit.Steps;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;


public class StepsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SCROLL_POSITION = "scrollPosition";

    public static final String TAG = "tag";
    private static final int DEFAULT_VALUE = 0;
    public static final String EXTRA_ID = "extraId";
    private static final String KEY_LIST_POSITION = "listIndex";
    private static final String KEY_LIST_DESCRIPTION = "keyDesc";
    private static final String KEY_LIST_SHORT_DESCRIPTION = "keyShortDes";
    private static final String KEY_LIST_URLS = "keyUrls";
    private static final String KEY_LIST_THUMBNAILS = "keyThumbnails";
    private int position;

    private List<String> mListGetShortDescription = new ArrayList<>();
    private List<String> mListGetDescription = new ArrayList<>();
    private List<String> mListVideoUrl = new ArrayList<>();
    private List<String> mListThumbnailUrl = new ArrayList<>();

    private Button mBtnNext;
    private Button mBtnBack;

    private ProgressBar mProgressBar;

    private ImageView mIVError;

    private NestedScrollView mNestedScrollView;

    private LinearLayout buttonsLinear;
    private LinearLayout positionLinear;
    private CardView cardView;
    private FrameLayout frameLayoutVideo;
    private ScrollView mScrollView;
    private TextView mTVDescription;
    private TextView mTVShortDescription;
    private LinearLayout mLinearDesContainer;
    private LinearLayout mLinearShortDescContainer;

    public void addDataToList(List<String> mListGetData, String data) {
        mListGetData.add(data);
    }

    int index;
    int listIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        if (initinalizeUiComponent()) return;


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            position = intent.getIntExtra(EXTRA_ID, DEFAULT_VALUE);
        }

        index = ++position;
        listIndex = 0;

        mBtnNext.setVisibility(View.INVISIBLE);
        mBtnBack.setVisibility(View.INVISIBLE);

        setUpUi();

        if (savedInstanceState != null) {
            listIndex = savedInstanceState.getInt(KEY_LIST_POSITION);
            mListGetDescription = savedInstanceState.getStringArrayList(KEY_LIST_DESCRIPTION);
            mListGetShortDescription = savedInstanceState.getStringArrayList(KEY_LIST_SHORT_DESCRIPTION);
            mListVideoUrl = savedInstanceState.getStringArrayList(KEY_LIST_URLS);
            mListThumbnailUrl = savedInstanceState.getStringArrayList(KEY_LIST_THUMBNAILS);
        }

    }

    private boolean initinalizeUiComponent() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return true;
        }


        actionBar.setDisplayHomeAsUpEnabled(true);

        mBtnBack = findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mBtnNext = findViewById(R.id.btn_forward);
        mBtnNext.setOnClickListener(this);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mIVError = findViewById(R.id.iv_error_retrofit);

        mNestedScrollView = findViewById(R.id.nested_scroll_view);

        buttonsLinear = findViewById(R.id.buttons_linear);
        positionLinear = findViewById(R.id.text_position_container);
        frameLayoutVideo = findViewById(R.id.video_container);
        cardView = findViewById(R.id.card_view_steps);

        mScrollView = findViewById(R.id.scrollView);

        mTVDescription = findViewById(R.id.tv_desc);
        mTVShortDescription = findViewById(R.id.tv_short_desc);
        mLinearDesContainer = findViewById(R.id.description_container);
        mLinearShortDescContainer = findViewById(R.id.short_description_container);

        return false;
    }

    private void setUpUi() {
        if (isNetWorkAvailable()) {
            ViewModelMain mainViewModel = ViewModelProviders.of(this)
                    .get(ViewModelMain.class);

            mProgressBar.setVisibility(View.INVISIBLE);
            mBtnBack.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);

            mainViewModel.getPostNames().observe(this, new Observer<List<Post>>() {
                @Override
                public void onChanged(@Nullable List<Post> posts) {

                    mListVideoUrl.clear();
                    mListThumbnailUrl.clear();
                    mListGetShortDescription.clear();
                    mListGetDescription.clear();
                    for (Post post : posts) {

                        List<Steps> listSteps = post.getSteps();
                        for (Steps steps : listSteps) {
                            if (post.getId() == index) {

                                addDataToList(mListGetShortDescription, steps.getShortDescription());
                                addDataToList(mListGetDescription, steps.getDescription());
                                addDataToList(mListVideoUrl, steps.getVideoURL());
                                addDataToList(mListThumbnailUrl, steps.getThumbnailURL());
                            }
                        }
                    }

                    setUpFragments(listIndex);

                }
            });
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            handlingErrorUi();
        }

    }

    private void setUpFragments(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Log.d(TAG, "onResponse: " + mListVideoUrl);
        Log.d(TAG, "onResponse: " + mListVideoUrl.get(index));

        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setListThumbnailUrl(mListThumbnailUrl);
        videoFragment.setListVideoUrl(mListVideoUrl);
        videoFragment.setIndex(index);

        fragmentManager.beginTransaction()
                .add(R.id.video_container, videoFragment)
                .commit();

        StepsFragment fragmentShortDescription = new StepsFragment();
        fragmentShortDescription.setListData(mListGetShortDescription);
        fragmentShortDescription.setIndex(index);

        fragmentManager.beginTransaction()
                .add(R.id.short_description_container, fragmentShortDescription)
                .commit();

        StepsFragment fragmentDescription = new StepsFragment();
        fragmentDescription.setListData(mListGetDescription);
        fragmentDescription.setIndex(index);
        fragmentManager.beginTransaction()
                .add(R.id.description_container, fragmentDescription)
                .commit();

        PositionFragment positionFragment = new PositionFragment();
        positionFragment.setPosition(index);

        fragmentManager.beginTransaction()
                .add(R.id.text_position_container, positionFragment)
                .commit();
    }

    @Override
    public void onClick(View v) throws IndexOutOfBoundsException {
        int id = v.getId();

        try {
            int mTextPosition = Integer.valueOf(PositionFragment.mTextViewPosition.getText().toString());

            if (id == R.id.btn_back) {

                int errorPosition = 2 - mTextPosition;
                Log.i(TAG, "Position: " + errorPosition);
                if (errorPosition == 1) {
                    Toast.makeText(this, R.string.error_back, Toast.LENGTH_SHORT).show();
                    return;
                }

                listIndex--;
                handleButtonClicks();
            }

            if (id == R.id.btn_forward)

            {

                Log.d(TAG, "onClickSize: " + mListGetDescription.size());
                Log.i(TAG, "Position: " + ++mTextPosition);
                if (mTextPosition > mListGetDescription.size()) {
                    Toast.makeText(this, R.string.error_next, Toast.LENGTH_SHORT).show();
                    return;
                }

                listIndex++;
                handleButtonClicks();
            }
        } catch (NullPointerException e) {

        }

    }

    private void handleButtonClicks() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setListThumbnailUrl(mListThumbnailUrl);
        videoFragment.setListVideoUrl(mListVideoUrl);
        videoFragment.setIndex(listIndex);

        fragmentManager.beginTransaction()
                .replace(R.id.video_container, videoFragment)
                .commit();


        StepsFragment fragmentShortDescription = new StepsFragment();
        fragmentShortDescription.setListData(mListGetShortDescription);
        fragmentShortDescription.setIndex(listIndex);

        fragmentManager.beginTransaction()
                .replace(R.id.short_description_container, fragmentShortDescription)
                .commit();

        StepsFragment fragmentDescription = new StepsFragment();
        fragmentDescription.setListData(mListGetDescription);
        fragmentDescription.setIndex(listIndex);
        fragmentManager.beginTransaction()
                .replace(R.id.description_container, fragmentDescription)
                .commit();

        PositionFragment positionFragment = new PositionFragment();
        positionFragment.setPosition(listIndex);

        fragmentManager.beginTransaction()
                .replace(R.id.text_position_container, positionFragment)
                .commit();
    }


    private void handlingErrorUi() {
        Picasso.get()
                .load(R.drawable.error_place_holder)
                .fit()
                .centerCrop()
                .into(mIVError);

        mIVError.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.error_retrofit, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            positionLinear.setVisibility(View.INVISIBLE);
            buttonsLinear.setVisibility(View.INVISIBLE);
            mBtnNext.setVisibility(View.INVISIBLE);
            mBtnBack.setVisibility(View.INVISIBLE);
            mIVError.setVisibility(View.INVISIBLE);
            mLinearShortDescContainer.setVisibility(View.INVISIBLE);
            mLinearDesContainer.setVisibility(View.INVISIBLE);
            mTVShortDescription.setVisibility(View.INVISIBLE);
            mTVDescription.setVisibility(View.INVISIBLE);


            mScrollView.setBackgroundColor(getResources().getColor(R.color.colorBlack));

            cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            frameLayoutVideo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);

            if (getSupportActionBar() != null)
                getSupportActionBar().hide();


        } else {


            if (getSupportActionBar() != null)
                getSupportActionBar().show();

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }
        final int[] scrollToPosition = savedInstanceState.getIntArray(SCROLL_POSITION);
        if (mNestedScrollView != null) {
            mNestedScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mNestedScrollView.scrollTo(scrollToPosition[0], scrollToPosition[1]);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putIntArray(SCROLL_POSITION, new int[]{mNestedScrollView.getScrollX(), mNestedScrollView.getScrollY()});
        outState.putInt(KEY_LIST_POSITION, listIndex);
        outState.putStringArrayList(KEY_LIST_DESCRIPTION, (ArrayList<String>) mListGetDescription);
        outState.putStringArrayList(KEY_LIST_SHORT_DESCRIPTION, (ArrayList<String>) mListGetShortDescription);
        outState.putStringArrayList(KEY_LIST_URLS, (ArrayList<String>) mListVideoUrl);
        outState.putStringArrayList(KEY_LIST_THUMBNAILS, (ArrayList<String>) mListThumbnailUrl);

        super.onSaveInstanceState(outState);
    }

    private boolean isNetWorkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
