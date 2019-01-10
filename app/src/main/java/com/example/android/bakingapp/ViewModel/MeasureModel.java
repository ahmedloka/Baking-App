package com.example.android.bakingapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.database.AppDatabase;

import java.util.List;

public class MeasureModel{ //} extends AndroidViewModel {

    /*
    private LiveData<List<String>> measure ;

    public MeasureModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(application);
        measure = database.taskDao().getMeasure();
    }


    public LiveData<List<String>> getMeasure() {
        return measure;
    }
    */
}
