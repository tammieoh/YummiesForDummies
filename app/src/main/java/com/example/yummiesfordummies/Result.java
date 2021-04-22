package com.example.yummiesfordummies;

import android.widget.ImageView;

public class Result {
    String resultTitle;
    String imageUrl;

    public Result(String resultTitle, ImageView resultImage) {
        this.resultTitle = resultTitle;
        this.imageUrl = imageUrl;
    }
    public String getTitle() {
        return resultTitle; }

    public void setTitle(String resultTitle) {
        this.resultTitle = resultTitle;
    }

    public String getResultImage() {
        return imageUrl;
    }
    public void setImage(ImageView resultImage) {
        this.imageUrl = imageUrl;
    }
}
