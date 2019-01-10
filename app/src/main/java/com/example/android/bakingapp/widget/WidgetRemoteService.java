package com.example.android.bakingapp.widget;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.IngredientsActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ViewModel.ViewModelMain;
import com.example.android.bakingapp.database.AppDatabase;
import com.example.android.bakingapp.database.AppExecutor;
import com.example.android.bakingapp.fragments.IngredientsFragment;
import com.example.android.bakingapp.fragments.MainFragment;
import com.example.android.bakingapp.retrofit.ApiClient;
import com.example.android.bakingapp.retrofit.ApiInterface;
import com.example.android.bakingapp.retrofit.Ingredients;
import com.example.android.bakingapp.retrofit.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WidgetRemoteService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new widgetFactory(getApplicationContext(), intent);
    }

    class widgetFactory implements RemoteViewsFactory {

        Context mContext;
        List<String> mListIngredients;

        public widgetFactory(Context Context, Intent intent) {
            this.mContext = Context;
        }

        @Override
        public void onCreate() {
            AppExecutor.getInstance(mContext).diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    List<String> listIngredient = AppDatabase.getInstance(mContext).taskDao().getIngredient();
                    if (listIngredient != null) {
                        if (listIngredient.size() - 1 == -1) {
                            return;
                        }
                        int index = listIngredient.size() - 1;
                        String ingredient = listIngredient.get(index).replace('[', ' ')
                                .replace(']', ' ')
                                .replace('"', ' ');

                        mListIngredients = Arrays.asList(ingredient.trim().split("\\s*,\\s*"));
                    }
                }
            });
        }


        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return mListIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_view_item_widget);

            if (mListIngredients != null) {
                views.setTextViewText(R.id.text_ingredient_item, "Ingredient : " + mListIngredients.get(position));
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
