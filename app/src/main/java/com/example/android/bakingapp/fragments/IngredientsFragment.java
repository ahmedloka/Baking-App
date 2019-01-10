package com.example.android.bakingapp.fragments;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakingapp.BakingWidgetProvider;
import com.example.android.bakingapp.IngredientsActivity;
import com.example.android.bakingapp.R;

import com.example.android.bakingapp.ViewModel.ViewModelMain;
import com.example.android.bakingapp.adapters.IngredientsAdapter;
import com.example.android.bakingapp.database.AppDatabase;
import com.example.android.bakingapp.database.AppExecutor;
import com.example.android.bakingapp.retrofit.Ingredients;
import com.example.android.bakingapp.retrofit.Post;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IngredientsFragment extends Fragment implements IngredientsAdapter.ClickHandler {

    private static final String TAG = "tag";
    private static final String SCROLL_STATE = "scrollState";
    private static final String KEY_LIST_INGREDIENTS = "keyIngredients";
    private static final String KEY_LIST_MEASURE = "keyMeasure";
    private static final String KEY_LIST_QUANTITY = "keyQuantity";

    private List<String> mListIngredients = new ArrayList<>();
    private List<String> mListMeasure = new ArrayList<>();
    private List<Double> mListQuantity = new ArrayList<>();
    private List<String> mListNames = new ArrayList<>();

    private RecyclerView mRecyclerViewIngredients;
    private ImageView mIVErrorRetrofit;
    private ProgressBar mProgressBar;
    private int id;

    Parcelable scrollState;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_igredients, container, false);

        mRecyclerViewIngredients = view.findViewById(R.id.rv_ingredients);
        mIVErrorRetrofit = view.findViewById(R.id.iv_error_retrofit);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        IngredientsActivity ingredientsActivity = (IngredientsActivity) getActivity();
        if (ingredientsActivity != null) {
            id = ingredientsActivity.getPosition();
        }

        if (savedInstanceState != null) {

            scrollState = savedInstanceState.getParcelable(SCROLL_STATE);
            mListMeasure = (List<String>) savedInstanceState.getSerializable(KEY_LIST_MEASURE);
            mListIngredients = (List<String>) savedInstanceState.getSerializable(KEY_LIST_INGREDIENTS);
            mListQuantity = (List<Double>) savedInstanceState.getSerializable(KEY_LIST_QUANTITY);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        setUpUi();

        AppExecutor.getInstance(getContext()).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<String> quantityList = AppDatabase.getInstance(getContext()).taskDao().getQua();
                if (quantityList != null) {
                    if (quantityList.size() - 1 == -1) {
                        return;
                    }
                    int index = quantityList.size() - 1;
                    String quantity = quantityList.get(index).replace('[', ' ')
                            .replace(']', ' ')
                            .replace('"', ' ');
                    List<String> quantities = Arrays.asList(quantity.trim().split("\\s*,\\s*"));
                    Log.d(TAG, "onChangedQuantity:" + quantities);
                }
            }
        });

    }

    private void setUpUi() {

        ViewModelMain viewModelMain = ViewModelProviders.of(this).get(ViewModelMain.class);
        viewModelMain.getPostNames().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {

                if (isNetWorkAvailable()) {
                    mIVErrorRetrofit.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);

                    mListIngredients.clear();
                    mListMeasure.clear();
                    mListMeasure.clear();
                    for (final Post post : posts) {
                        List<Ingredients> ingredientsList = post.getIngredients();

                        for (Ingredients ingredients : ingredientsList) {

                            if (post.getId() == id) {

                                mListQuantity.add(ingredients.getQuantity());
                                mListIngredients.add(ingredients.getIngredient());
                                mListMeasure.add(ingredients.getMeasure());

                                mListNames.add(post.getName());

                            }
                        }

                    }

                    databaseHanlding();

                    mRecyclerViewIngredients.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    mRecyclerViewIngredients.setLayoutManager(layoutManager);
                    mRecyclerViewIngredients.getLayoutManager().onRestoreInstanceState(scrollState);
                    IngredientsAdapter adapter = new IngredientsAdapter(mListIngredients, mListMeasure, mListQuantity, IngredientsFragment.this);
                    mRecyclerViewIngredients.setAdapter(adapter);
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    handlingErrorUi();
                }

            }
        });
    }

    private void databaseHanlding() {
        try {
            final Post postDB = new Post(mListNames);
            postDB.setNameList(Collections.singletonList(mListNames.get(mListNames.size() - 1)));
            AppExecutor.getInstance(getContext()).diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppDatabase.getInstance(getContext()).taskDao().getName() != null) {
                        AppDatabase.getInstance(getContext()).taskDao().updateName(postDB);
                    }
                    AppDatabase.getInstance(getContext()).taskDao().insertName(postDB);
                }
            });

            final List<String> quantities = new ArrayList<>();
            for (double item : mListQuantity) {
                quantities.add(String.valueOf(item));
            }

            final Ingredients ingredientsDB = new Ingredients(mListIngredients, mListMeasure, quantities);

            AppExecutor.getInstance(getContext()).diskIO().execute(new Runnable() {
                @Override
                public void run() {

                    AppDatabase.getInstance(getContext()).taskDao().insertIngredients(ingredientsDB);
                }
            });

        } catch (ArrayIndexOutOfBoundsException ignored) {

        }
    }

    private void handlingErrorUi() {
        Picasso.get()
                .load(R.drawable.error_place_holder)
                .fit()
                .centerCrop()
                .into(mIVErrorRetrofit);

        mIVErrorRetrofit.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), R.string.error_retrofit, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        scrollState = mRecyclerViewIngredients.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SCROLL_STATE, scrollState);
        outState.putSerializable(KEY_LIST_INGREDIENTS, (Serializable) mListIngredients);
        outState.putSerializable(KEY_LIST_MEASURE, (Serializable) mListMeasure);
        outState.putSerializable(KEY_LIST_QUANTITY, (Serializable) mListQuantity);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            try {
                scrollState = savedInstanceState.getParcelable(SCROLL_STATE);
            } catch (NullPointerException e) {

            }
        }
    }

    private boolean isNetWorkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    private void updateAllWidgets(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), BakingWidgetProvider.class));
        if (appWidgetIds.length > 0) {
            new BakingWidgetProvider().onUpdate(getContext(), appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (scrollState != null) {
            mRecyclerViewIngredients.getLayoutManager().onRestoreInstanceState(scrollState);
        }
        updateAllWidgets();
    }
}
