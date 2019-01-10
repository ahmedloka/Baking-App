package com.example.android.bakingapp.Respositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bakingapp.retrofit.ApiClient;
import com.example.android.bakingapp.retrofit.ApiInterface;
import com.example.android.bakingapp.retrofit.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.bakingapp.IngredientsActivity.TAG;

public class MainRepository {
    private ApiInterface apiInterface ;
    private static class SingletonHelper {

        private static final MainRepository INSTANCE =
                new MainRepository();
    }
    public static MainRepository getInstance  (){
        return SingletonHelper.INSTANCE ;
    }
    MainRepository(){
        apiInterface = ApiClient.getData().create(ApiInterface.class);
    }

    public LiveData<List<Post>> getData (){
        final MutableLiveData<List<Post>> data = new MutableLiveData<>();
        apiInterface.getData().
                enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {

                        if (response.isSuccessful()){
                            data.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: ",t.getCause() );
                        try {
                            data.setValue(null);
                        }catch (NullPointerException ignored){
                        }
                    }
                });
        return data ;
    }

}
