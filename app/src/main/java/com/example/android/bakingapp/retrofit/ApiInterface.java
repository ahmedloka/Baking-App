package com.example.android.bakingapp.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Post>> getData();

}