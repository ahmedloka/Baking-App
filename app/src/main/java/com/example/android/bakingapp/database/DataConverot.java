package com.example.android.bakingapp.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataConverot {

    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromListString(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }


    @TypeConverter
    public static ArrayList<List<String>> fromListString(String value) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }


    @TypeConverter
    public static List<String> fromArrayListString(ArrayList<List<String>> lists) {
        Gson gson = new Gson();
        return Collections.singletonList(gson.toJson(lists));
    }


}
