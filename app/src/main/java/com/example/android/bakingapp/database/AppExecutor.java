package com.example.android.bakingapp.database;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static final Object LOCK = new Object();
    private static AppExecutor mInstance ;
    private final Executor diskIO ;

    public AppExecutor(Executor executor) {
        this.diskIO = executor ;
    }
    public static AppExecutor getInstance (Context context){
        if (mInstance == null){
            synchronized (LOCK){
                mInstance = new AppExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return mInstance ;
    }
    public Executor diskIO (){
        return diskIO ;
    }
}
