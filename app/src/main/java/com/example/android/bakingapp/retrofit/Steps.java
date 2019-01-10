package com.example.android.bakingapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Steps {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("shortDescription")
    private String shortDescription;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("videoURL")
    private String videoURL;

    @Expose
    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public Steps() {
    }
}
