package com.example.administrator.myapplication.fileupload;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon.zhong on 2015/3/3.
 */
public class UploadApi {

    /**
     * 上传图片接口
     * @param bitmap 需要上传的图片
     * @param listener 请求回调
     */
    public static void uploadImg(Context context, Bitmap bitmap, String filename,Response.Listener<String> listener, Response.ErrorListener errorListener){
        List<FormImage> imageList = new ArrayList<FormImage>() ;
        imageList.add(new FormImage(bitmap,filename)) ;
        Request request = new PostUploadRequest(Constant.UploadHost,imageList,listener,errorListener) ;
        Volley.newRequestQueue(context).add(request) ;
    }
}