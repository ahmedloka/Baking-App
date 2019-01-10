package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakingapp.ViewModel.ViewModelMain;
import com.example.android.bakingapp.database.AppDatabase;
import com.example.android.bakingapp.database.AppExecutor;
import com.example.android.bakingapp.fragments.PositionFragment;
import com.example.android.bakingapp.fragments.StepsFragment;
import com.example.android.bakingapp.fragments.VideoFragment;
import com.example.android.bakingapp.retrofit.Ingredients;
import com.example.android.bakingapp.retrofit.Post;
import com.example.android.bakingapp.retrofit.Steps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.bakingapp.fragments.MainFragment.EXTRA_ITEM_NAME;

public class IngredientsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "tag";

    private static final String SCROLL_POSITION = "scrollPosition";
    private static final String KEY_LIST_DESCRIPTION = "keyDesc";
    private static final String KEY_LIST_SHORT_DESCRIPTION = "keyShortDes";
    private static final String KEY_LIST_URLS = "keyUrls";
    private static final String KEY_LIST_THUMBNAILS = "keyThumbnails";
    private static final String KEY_LIST_POSITION = "listIndex";
    private static final int DEFAULT_VALUE = 0;
    public static final String EXTRA_ID = "extraId";

    private int position;

    private ImageView mIVError;

    private List<String> mListGetShortDescription = new ArrayList<>();
    private List<String> mListGetDescription = new ArrayList<>();
    private List<String> mListVideoUrl = new ArrayList<>();
    private List<String> mListThumbnailUrl = new ArrayList<>();

    private int listIndex = 0;

    private NestedScrollView mNestedScrollView;

    private Button mBtnNext;
    private Button mBtnBack;

    private ProgressBar mProgressBar;

    int index;

    public void addDataToList(List<String> mListGetData, String data) {
        mListGetData.add(data);
    }

    private boolean isTwoPane;

    private List<String> mListIngredients = new ArrayList<>();
    private List<String> mListMeasure = new ArrayList<>();
    private List<Double> mListQuantity = new ArrayList<>();
    private List<String> mListNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            position = intent.getIntExtra(EXTRA_ID, DEFAULT_VALUE);
        }
        if (intent.hasExtra(EXTRA_ITEM_NAME)) {
            Toast.makeText(this, intent.getStringExtra(EXTRA_ITEM_NAME), Toast.LENGTH_SHORT).show();
        }


        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

        mIVError = findViewById(R.id.iv_pane);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);


        if (findViewById(R.id.linear_steps) != null) {
            isTwoPane = true;


            index = ++position;

            mBtnBack = findViewById(R.id.btn_back);
            mBtnBack.setOnClickListener(this);

            mBtnNext = findViewById(R.id.btn_forward);
            mBtnNext.setOnClickListener(this);

            mNestedScrollView = findViewById(R.id.nested_scroll_view);

            if (savedInstanceState != null) {
                listIndex = (int) savedInstanceState.getSerializable(KEY_LIST_POSITION);
                mListGetDescription = (List<String>) savedInstanceState.getSerializable(KEY_LIST_DESCRIPTION);
                mListGetShortDescription = (List<String>) savedInstanceState.getSerializable(KEY_LIST_SHORT_DESCRIPTION);
                mListVideoUrl = (List<String>) savedInstanceState.getSerializable(KEY_LIST_URLS);
                mListThumbnailUrl = (List<String>) savedInstanceState.getSerializable(KEY_LIST_THUMBNAILS);
            }

            databaseHanlding();
            setUpUi();
        } else {
            isTwoPane = false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (findViewById(R.id.linear_steps) != null) {
            isTwoPane = true;
            updateAllWidgets();
        }
    }

    private void setUpUi() {
        if (isNetWorkAvailable()) {
            mProgressBar.setVisibility(View.INVISIBLE);
            ViewModelMain viewModelMain = ViewModelProviders.of(this)
                    .get(ViewModelMain.class);

            viewModelMain.getPostNames().observe(this, new Observer<List<Post>>() {
                @Override
                public void onChanged(@Nullable List<Post> posts) {

                    mListGetDescription.clear();
                    mListGetShortDescription.clear();
                    mListThumbnailUrl.clear();
                    mListVideoUrl.clear();

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


                    setUpFragments();
                }

            });
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            handlingErrorUi();
        }

    }

    private void databaseHanlding() {
        try {
            final Post postDB = new Post(mListNames);
            postDB.setNameList(Collections.singletonList(mListNames.get(mListNames.size() - 1)));
            AppExecutor.getInstance(getApplicationContext()).diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppDatabase.getInstance(getApplicationContext()).taskDao().getName() != null) {
                        AppDatabase.getInstance(getApplicationContext()).taskDao().updateName(postDB);
                    }
                    AppDatabase.getInstance(getApplicationContext()).taskDao().insertName(postDB);
                }
            });

            final List<String> quantities = new ArrayList<>();
            for (double item : mListQuantity) {
                quantities.add(String.valueOf(item));
            }

            final Ingredients ingredientsDB = new Ingredients(mListIngredients, mListMeasure, quantities);

            AppExecutor.getInstance(getApplicationContext()).diskIO().execute(new Runnable() {
                @Override
                public void run() {

                    AppDatabase.getInstance(getApplicationContext()).taskDao().insertIngredients(ingredientsDB);
                }
            });

        } catch (ArrayIndexOutOfBoundsException ignored) {

        }
    }

    private void setUpFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (mListThumbnailUrl != null) {
            Log.d(TAG, "onResponse: " + mListThumbnailUrl);
            Log.d(TAG, "onResponse: " + mListThumbnailUrl.get(listIndex));
        }


        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setListThumbnailUrl(mListThumbnailUrl);
        videoFragment.setListVideoUrl(mListVideoUrl);

        videoFragment.setIndex(listIndex);

        fragmentManager.beginTransaction()
                .add(R.id.video_container, videoFragment)
                .commit();


        StepsFragment fragmentShortDescription = new StepsFragment();
        fragmentShortDescription.setListData(mListGetShortDescription);
        fragmentShortDescription.setIndex(listIndex);

        fragmentManager.beginTransaction()
                .add(R.id.short_description_container, fragmentShortDescription)
                .commit();

        StepsFragment fragmentDescription = new StepsFragment();
        fragmentDescription.setListData(mListGetDescription);
        fragmentDescription.setIndex(listIndex);
        fragmentManager.beginTransaction()
                .add(R.id.description_container, fragmentDescription)
                .commit();

        PositionFragment positionFragment = new PositionFragment();
        positionFragment.setPosition(listIndex);

        fragmentManager.beginTransaction()
                .add(R.id.text_position_container, positionFragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int mTextPosition = Integer.valueOf(PositionFragment.mTextViewPosition.getText().toString());

        if (id == R.id.btn_back) {

            int errorPosition = 2 - mTextPosition;
            if (errorPosition == 1) {
                Toast.makeText(this, R.string.error_back, Toast.LENGTH_SHORT).show();
                return;
            }

            listIndex--;
            handleButtonClicks();
        }

        if (id == R.id.btn_forward)

        {

            if (mTextPosition > mListGetDescription.size()) {
                Toast.makeText(this, R.string.error_next, Toast.LENGTH_SHORT).show();
                return;
            }

            listIndex++;
            handleButtonClicks();
        }

    }

    private void handleButtonClicks() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        VideoFragment videoFragment = new VideoFragment();

        videoFragment.setListVideoUrl(mListVideoUrl);
        videoFragment.setListThumbnailUrl(mListThumbnailUrl);
        videoFragment.setIndex(listIndex);
        Log.d(TAG, "handleButtonClicks: " + mListVideoUrl.get(listIndex));

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

    public int getPosition() {
        return ++position;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        try {
            if (findViewById(R.id.linear_steps) != null) {
                outState.putIntArray(SCROLL_POSITION, new int[]{mNestedScrollView.getScrollX(), mNestedScrollView.getScrollY()});
            }
            outState.putSerializable(KEY_LIST_POSITION, listIndex);
            outState.putStringArrayList(KEY_LIST_DESCRIPTION, (ArrayList<String>) mListGetDescription);
            outState.putStringArrayList(KEY_LIST_SHORT_DESCRIPTION, (ArrayList<String>) mListGetShortDescription);
            outState.putStringArrayList(KEY_LIST_URLS, (ArrayList<String>) mListVideoUrl);
            outState.putStringArrayList(KEY_LIST_THUMBNAILS, (ArrayList<String>) mListThumbnailUrl);
        } catch (NullPointerException e) {

        }
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (findViewById(R.id.linear_steps) != null) {

            mNestedScrollView.post(new Runnable() {
                @Override
                public void run() {
                    int[] scrollToPosition = savedInstanceState.getIntArray(SCROLL_POSITION);
                    mNestedScrollView.scrollTo(scrollToPosition[0], scrollToPosition[1]);
                }
            });
        }
    }

    private void updateAllWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        if (appWidgetIds.length > 0) {
            new BakingWidgetProvider().onUpdate(getApplicationContext(), appWidgetManager, appWidgetIds);
        }
    }

    private boolean isNetWorkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
