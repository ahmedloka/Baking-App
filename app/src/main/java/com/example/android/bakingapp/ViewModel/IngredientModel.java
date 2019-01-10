package com.example.android.bakingapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.database.AppDatabase;

import java.util.List;

public class IngredientModel {// extends AndroidViewModel {

    /*
    private LiveData<List<String>> ingredient;

    public IngredientModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(application);
        ingredient = database.taskDao().getIngredient();
    }

    public LiveData<List<String>> getIngredient() {
        return ingredient;
    }
    */
}
