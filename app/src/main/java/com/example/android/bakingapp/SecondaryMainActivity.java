package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakingapp.adapters.IngredientsAdapter;
import com.example.android.bakingapp.fragments.IngredientsFragment;
import com.example.android.bakingapp.fragments.PositionFragment;
import com.example.android.bakingapp.fragments.StepsFragment;
import com.example.android.bakingapp.fragments.VideoFragment;
import com.example.android.bakingapp.retrofit.ApiClient;
import com.example.android.bakingapp.retrofit.ApiInterface;
import com.example.android.bakingapp.retrofit.Ingredients;
import com.example.android.bakingapp.retrofit.Post;
import com.example.android.bakingapp.retrofit.Steps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.bakingapp.fragments.MainFragment.EXTRA_POSITION;

public class SecondaryMainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int DEFAULT_VALUE = 0;
    public static final String EXTRA_ID = "extraId";
    public static final String TAG = "tag";

    private ImageView mImageSteps;
    private ImageView mImageIngredients;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_main);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        if (intent.hasExtra(EXTRA_POSITION)) {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_VALUE);
            Log.d(TAG, "onCreate: " + String.valueOf(position));
        }

        mImageIngredients = findViewById(R.id.iv_ingredients);
        mImageIngredients.setOnClickListener(this);

        mImageSteps = findViewById(R.id.iv_steps);
        mImageSteps.setOnClickListener(this);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

        setUpUi();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpUi() {
        Picasso.get()
                .load(R.drawable.ingredients)
                .resize(600, 200)
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .centerInside()
                .into(mImageIngredients);

        Picasso.get()
                .load(R.drawable.steps)
                .resize(600, 200)
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .centerInside()
                .into(mImageSteps);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.iv_ingredients) {
            Intent intentSendPosition = new Intent(this, IngredientsActivity.class);
            intentSendPosition.putExtra(EXTRA_ID, position);
            startActivity(intentSendPosition);
        } else if (id == R.id.iv_steps) {
            Intent intentSendPosition = new Intent(this, StepsActivity.class);
            intentSendPosition.putExtra(EXTRA_ID, position);
            startActivity(intentSendPosition);
        }
    }

}
