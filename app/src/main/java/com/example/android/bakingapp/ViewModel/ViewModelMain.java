package com.example.android.bakingapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.Respositories.MainRepository;
import com.example.android.bakingapp.retrofit.Post;

import java.util.List;

public class ViewModelMain extends AndroidViewModel {

    private MutableLiveData MUTABLE_LIVE_DATA = new MutableLiveData();
    {
        MUTABLE_LIVE_DATA.setValue(null);
    }
    public static LiveData<List<Post>> mainRepository ;
    private Context context ;

    public ViewModelMain(@NonNull Application application) {
        super(application);

        mainRepository = MainRepository.getInstance()
                .getData();

    }

    public LiveData<List<Post>> getPostNames (){
        return mainRepository ;
    }
}
