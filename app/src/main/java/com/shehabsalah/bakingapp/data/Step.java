package com.shehabsalah.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 6/7/17.
 * This Class responsible on holding each step of the recipe
 */

public class Step implements Parcelable{
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    private Step(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        this.id                 = Integer.parseInt(data[0]);
        this.shortDescription   = data[1];
        this.description        = data[2];
        this.videoURL           = data[3];
        this.thumbnailURL       = data[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                Integer.toString(this.id),
                this.shortDescription,
                this.description,
                this.videoURL,
                this.thumbnailURL
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}
