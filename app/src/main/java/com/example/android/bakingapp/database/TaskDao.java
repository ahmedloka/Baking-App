package com.example.android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.bakingapp.retrofit.Ingredients;
import com.example.android.bakingapp.retrofit.Post;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM table_name ORDER BY COL_ID ")
    List<Post> loadAllNames ();

    @Query("SELECT * FROM table_ing ORDER BY COL_ID")
    List<Ingredients> loadAllIngredients();

    @Query("SELECT COL_NAME FROM TABLE_NAME ")
    List<String> getName();

    @Query("SELECT COL_ING FROM TABLE_ING")
    List<String> getIngredient();

    @Query("SELECT COL_MEA FROM TABLE_ING")
    List<String> getMeasure();

    @Query("SELECT COL_QUN FROM TABLE_ING")
    List<String> getQua();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertName(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredients(Ingredients ingredients);

    @Delete
    void deleteIngredients (Ingredients ingredients);

    @Update
    void updateIngredients(Ingredients ingredients);

    @Update
    void updateName(Post post);


}
