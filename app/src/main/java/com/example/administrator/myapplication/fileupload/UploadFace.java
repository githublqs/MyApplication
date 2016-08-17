package com.example.administrator.myapplication.fileupload;

/**
 * Created by Administrator on 2016/8/17.
 */

public class UploadFace {
    private String thumbnailImgFileName;
    private String imgFileiName;
    private  String date;

    public UploadFace() {
    }

    public UploadFace(String thumbnailImgFileName, String imgFileiName, String date) {

        this.thumbnailImgFileName = thumbnailImgFileName;
        this.imgFileiName = imgFileiName;
        this.date = date;
    }

    public String getThumbnailImgFileName() {
        return thumbnailImgFileName;
    }

    public void setThumbnailImgFileName(String thumbnailImgFileName) {
        this.thumbnailImgFileName = thumbnailImgFileName;
    }

    public String getImgFileiName() {
        return imgFileiName;
    }

    public void setiMgFileName(String imgFileiName) {
        this.imgFileiName = imgFileiName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
