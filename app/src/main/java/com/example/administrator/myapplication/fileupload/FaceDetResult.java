package com.example.administrator.myapplication.fileupload;

import java.io.Serializable;
import java.util.List;
/**
 * Created by Administrator on 2016/8/15.
 */
//{“ID”:”123456789”,“face_num”:2,”face_rect”:[{”left”:12,”top”:34,”width”:120,”height”:120},{”left”:12,”top”:34,”width”:120,”height”:120}],”errorcode”:0}
public class FaceDetResult implements Serializable {
    private static final long serialVersionUID = 136902301452537722L;
    private  String ID;
    private  int face_num;

    public String getID() {
        return ID;
    }

    public int getFace_num() {
        return face_num;
    }

    public List<Face_Rect>  getFace_rectList() {
        return face_rectList;
    }

    public int getErrorcode() {
        return errorcode;
    }

    private List<Face_Rect> face_rectList;
    private  int errorcode;

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setFace_num(int face_num) {
        this.face_num = face_num;
    }

    public void setFace_rect(List<Face_Rect> face_rectList) {
        this.face_rectList = face_rectList;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public FaceDetResult() {

    }

    public FaceDetResult(String ID, int face_num, List<Face_Rect>  face_rectList, int errorcode) {
        this.ID = ID;
        this.face_num = face_num;
        this.face_rectList = face_rectList;
        this.errorcode = errorcode;
    }
}
