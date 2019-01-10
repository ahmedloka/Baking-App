package com.example.android.bakingapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public final static String BaseUrl = "https://d17h27t6h515a5.cloudfront.net/";
    public static Retrofit retrofit;

    public static Retrofit getData() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
