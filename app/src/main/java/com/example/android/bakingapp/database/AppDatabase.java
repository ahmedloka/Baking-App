package com.example.android.bakingapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.android.bakingapp.retrofit.Ingredients;
import com.example.android.bakingapp.retrofit.Post;

@Database(entities = {Post.class , Ingredients.class} ,
version = 6 ,
exportSchema = false)
@TypeConverters(DataConverot.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "dbInstance";
    private static final Object LOCK = new Object();
    private static AppDatabase mInstance ;

    public static AppDatabase getInstance (Context context){
        if (mInstance == null){
            synchronized (LOCK){
                mInstance = Room.databaseBuilder(context,AppDatabase.class
                        ,DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return mInstance ;
    }
    public abstract TaskDao taskDao ();

}
