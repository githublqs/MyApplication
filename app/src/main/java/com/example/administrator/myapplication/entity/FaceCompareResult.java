package com.example.administrator.myapplication.entity;

import com.example.administrator.myapplication.fileupload.Face_Rect;

import java.util.List;

public class FaceCompareResult {
    public String getMatching() {
        return matching;
    }
    public void setMatching(String matching) {
        this.matching = matching;
    }
    public float getSimilarity() {
        return similarity;
    }
    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }
    public int getErrorcode() {
        return errorcode;
    }
    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }
    public List<Face_Rect> getFace_rect() {
        return face_rect;
    }
    public void setFace_rect(List<Face_Rect> face_rect) {
        this.face_rect = face_rect;
    }
    private String matching;
    private float similarity;
    private int errorcode;
    private List<Face_Rect> face_rect;
    public FaceCompareResult() {
        super();
        // TODO Auto-generated constructor stub
    }
    public FaceCompareResult(String matching, float similarity, int errorcode,
                             List<Face_Rect> face_rect) {
        super();
        this.matching = matching;
        this.similarity = similarity;
        this.errorcode = errorcode;
        this.face_rect = face_rect;
    }

}