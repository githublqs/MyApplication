package com.example.administrator.myapplication.fileupload;

/**
 * Created by Administrator on 2016/8/17.
 */

public class UploadFace {
    private String thumbnailImgFileName;
    private String imgFileiName;
    private  String date;
    private long id;
    public UploadFace() {
    }

    public UploadFace(String thumbnailImgFileName, String imgFileiName, String date,int id) {
        this.thumbnailImgFileName = thumbnailImgFileName;
        this.imgFileiName = imgFileiName;
        this.date = date;
        this.id=id;
    }
    public UploadFace(String thumbnailImgFileName, String imgFileiName) {
        this(thumbnailImgFileName,imgFileiName,null,0);
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UploadFace that = (UploadFace) o;

        if (id != that.id) return false;
        if (thumbnailImgFileName != null ? !thumbnailImgFileName.equals(that.thumbnailImgFileName) : that.thumbnailImgFileName != null)
            return false;
        if (imgFileiName != null ? !imgFileiName.equals(that.imgFileiName) : that.imgFileiName != null)
            return false;
        return date != null ? date.equals(that.date) : that.date == null;

    }

    @Override
    public int hashCode() {
        int result = thumbnailImgFileName != null ? thumbnailImgFileName.hashCode() : 0;
        result = 31 * result + (imgFileiName != null ? imgFileiName.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
