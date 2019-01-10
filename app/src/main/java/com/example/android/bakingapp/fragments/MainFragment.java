package com.example.android.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakingapp.IngredientsActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.SecondaryMainActivity;
import com.example.android.bakingapp.ViewModel.ViewModelMain;
import com.example.android.bakingapp.adapters.MainAdapter;
import com.example.android.bakingapp.retrofit.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements MainAdapter.ClickHandler {

    private static final String TAG = "tag";
    public static final String EXTRA_POSITION = "position";
    private static final String SCROLL_STATE = "scrollState";
    private static final String KEY_LIST_NAMES = "keyListNames";
    private static final String KEY_IMG_RES = "keyImgRes";
    public static final String EXTRA_ITEM_NAME = "extraItemName";
    List<Integer> mImageRes = new ArrayList<>();

    List<String> mListNames = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ImageView mIngredientsImageView;
    private ProgressBar mProgressBar;

    private Parcelable scrollState;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mImageRes.add(R.drawable.nutella_pie);
        mImageRes.add(R.drawable.brownies);
        mImageRes.add(R.drawable.yellow_cake);
        mImageRes.add(R.drawable.cheese_cake);

        mIngredientsImageView = view.findViewById(R.id.iv_error_retrofit);
        mRecyclerView = view.findViewById(R.id.rv_main);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        setUpUi();
    }

    private void setUpUi() {
        if (isNetWorkAvailable()) {
            ViewModelMain viewModelMain = ViewModelProviders.of(this)
                    .get(ViewModelMain.class);

            viewModelMain.getPostNames()
                    .observe(this, new Observer<List<Post>>() {
                        @Override
                        public void onChanged(@Nullable List<Post> posts) {

                            mListNames.clear();

                            for (Post post : posts) {
                                mListNames.add(post.getName());
                            }
                            mIngredientsImageView.setVisibility(View.INVISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

                            boolean isTablet = getResources().getBoolean(R.bool.isTablet);
                            if (isTablet) {
                                mRecyclerView.setLayoutManager(gridLayoutManager);
                            } else {
                                mRecyclerView.setLayoutManager(mLayoutManager);
                            }
                            mRecyclerView.getLayoutManager().onRestoreInstanceState(scrollState);
                            MainAdapter mainAdapter = new MainAdapter(mImageRes, mListNames, MainFragment.this);
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setAdapter(mainAdapter);

                            Log.d(TAG, "onResponse: " + mListNames);
                        }
                    });
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            handlingErrorUi();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            try {

                scrollState = savedInstanceState.getParcelable(SCROLL_STATE);
                mListNames = savedInstanceState.getStringArrayList(KEY_LIST_NAMES);
                mImageRes = savedInstanceState.getIntegerArrayList(KEY_IMG_RES);
            } catch (NullPointerException e) {

            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            if (savedInstanceState != null) {
                mRecyclerView.getLayoutManager().onRestoreInstanceState(scrollState);
            }
        } catch (NullPointerException e) {

        }
    }


    private void handlingErrorUi() {
        Picasso.get()
                .load(R.drawable.error_place_holder)
                .fit()
                .centerCrop()
                .into(mIngredientsImageView);

        mIngredientsImageView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), R.string.error_retrofit, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int position) {
        Intent intent2 = new Intent(getActivity(), IngredientsActivity.class);
        Intent intent = new Intent(getActivity(), SecondaryMainActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_ITEM_NAME, mListNames.get(position));
        Log.d(TAG, "onClick: " + position);
        Log.d(TAG, "onClickName: " + mListNames.get(position));
        intent2.putExtra(EXTRA_POSITION, position);
        intent2.putExtra(EXTRA_ITEM_NAME, mListNames.get(position));


        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            startActivity(intent2);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        try {
            scrollState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(SCROLL_STATE, scrollState);
        } catch (NullPointerException e) {
            Log.e(TAG, "onSaveInstanceState: ", e);
        }
        outState.putStringArrayList(KEY_LIST_NAMES, (ArrayList<String>) mListNames);
        outState.putIntegerArrayList(KEY_IMG_RES, (ArrayList<Integer>) mImageRes);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (scrollState != null && mRecyclerView.getLayoutManager() != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(scrollState);
        }
    }

    private boolean isNetWorkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
