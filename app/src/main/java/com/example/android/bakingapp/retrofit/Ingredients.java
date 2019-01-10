package com.example.android.bakingapp.retrofit;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "TABLE_ING")
public class Ingredients {

    @ColumnInfo(name = "COL_ID")
    @PrimaryKey(autoGenerate = true)
    private int id ;

    @Expose
    @SerializedName("quantity")
    private double quantity;

    @Expose
    @SerializedName("measure")
    private String measure;

    @Expose
    @SerializedName("ingredient")
    private String ingredient;


    public Ingredients() {
    }

    @Ignore
    public Ingredients(int id, double quantity, String measure, String ingredient) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    /*
    public Ingredients(List<String> quantity, List<String> measure, List<String> ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
    */

    public int getId() {
        return id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setId(int id) {
        this.id = id;
    }

    //FOR database
    @ColumnInfo(name = "COL_ING")
    List<String> ingredientsList ;

    @ColumnInfo(name = "COL_MEA")
    List<String> measureList ;

    @ColumnInfo(name = "COL_QUN")
    List<String> quantityList ;

    public Ingredients(List<String> ingredientsList, List<String> measureList, List<String> quantityList) {
        this.ingredientsList = ingredientsList;
        this.measureList = measureList;
        this.quantityList = quantityList;
    }

    public List<String> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<String> getMeasureList() {
        return measureList;
    }

    public void setMeasureList(List<String> measureList) {
        this.measureList = measureList;
    }

    public List<String> getQuantityList() {
        return quantityList;
    }

    public void setQuantityList(List<String> quantityList) {
        this.quantityList = quantityList;
    }
}
