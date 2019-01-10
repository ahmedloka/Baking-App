package com.example.android.bakingapp.retrofit;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "TABLE_NAME")
public class Post {

    @Expose
    @SerializedName("id")
    private int id;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "COL_ID")
    private int dbID;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("ingredients")
    @Ignore
    private List<Ingredients> ingredients = null;

    @Expose
    @SerializedName("steps")
    @Ignore
    private List<Steps> steps = null;

    @Expose
    @SerializedName("servings")
    @Ignore
    private int servings;

    @Expose
    @SerializedName("image")
    @Ignore
    private String image;


    public Post() {

    }

    public Post(int dbID, String names) {
        this.dbID = dbID;
        this.name = names;
    }


    public int getDbID() {
        return dbID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }


    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    //FOR database
    @ColumnInfo(name = "COL_NAME")
    private List<String> nameList ;

    @Ignore
    public Post(List<String> nameList) {
        this.nameList = nameList;
    }

}
