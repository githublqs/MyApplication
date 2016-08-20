package com.example.administrator.myapplication.fileupload;

/**
 * Created by Administrator on 2016/8/15.
 */

public class Constant {
    //public static final  String UploadHost="http://http://100.0.2.2:8080/SSM/urlImg";
    public static final  String FaceHost="http://10.0.2.2:8080/SSM/";
    //public static final  String FaceHost="http://dojochinaextjs.imwork.net/SSM/";
    public static final  String ComputeSimUrl=FaceHost+"urlImg";
    public static final  String FaceDetUrl=FaceHost+"faceRec/faceDetBase64";
    public static final String UploadFaceDirUrl=FaceHost+"uploadface/";
    public static final String UploadFaceThumbnailDirUrl=UploadFaceDirUrl+"thumb/";
    public static final  String UploadFaceListUrl=FaceHost+"faceRec/uploadFaceList";
    public static final  String SocketHost="119.90.45.125";
    public static final  int SocketPort=9991;
    public static final int PageSize=10;

    public static final int SELECT_PICTURE=0;
    public static final int SELECT_CAMER=1;

}
