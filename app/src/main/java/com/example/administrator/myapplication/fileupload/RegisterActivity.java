package com.example.administrator.myapplication.fileupload;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.administrator.myapplication.R;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 用户注册页
 */
public class RegisterActivity extends BaseAppCompatActivity {
    @BindView(R.id.buttonChooseUserFaceImg) Button buttonChooseUserFaceImg;//选择用户照片
    @BindView(R.id.buttonRegister) Button buttonRegister;//注册按钮
    private Uri imgFileUri;
    private String mNickName;
    private String mPassword;
    private String mUserName;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_register);
        sendRegisterRequest();
    }

    /**
     * 检查录入用户注册信息是否合法有效
     * @return true ok
     */
    private boolean checkUserInfoVal(){
        if(imgFileUri==null){
            showSnackbar("请选择相片",buttonRegister);
            return  false;
        }
        //用户名
        mUserName="";
        //密码
        mPassword="";
        //照片

        //昵称 以上存入 Activity的成员变量
        mNickName="";
        return  false;
    }
    /**
     * 发送注册请求
     */
    @OnClick(R.id.buttonRegister)
    public void sendRegisterRequest() {
        if(!checkUserInfoVal()){
            return;
        }


        String url=null;
        JSONObject jsonRequest =new JSONObject();//用户注册信息

        SessionJsonObjectRequest request = new SessionJsonObjectRequest(Request.Method.POST, url,jsonRequest ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        showSnackbar("注册成功",buttonRegister);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //发送登陆请求
                                //登陆成功，跳转到内部主页面
                            }
                        }, 2000);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showSnackbar("注册失败",buttonRegister);
                    }
                }
        );
        VolleyController.getInstance(this).getRequestQueue().add(request);
    }
    /**
     * 选择照片
     */
    @OnClick(R.id.buttonChooseUserFaceImg)
    private void showChoosePicWhereDialog() {
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == Constant.SELECT_PICTURE) {
                            startActivityForResult(Intent.createChooser(UriResolver.getSmallScaledImageFrompPotoAlbum(),
                                    "选择图片"), Constant.SELECT_PICTURE);
                        } else {
                            startActivityForResult(UriResolver.getImageOrCaptureIntent(RegisterActivity.this,
                                    new UriResolver.ExtraOutputUriCallBack() {
                                        @Override
                                        public void onPutExtra(Uri imageUri) {

                                            imgFileUri = imageUri;

                                        }
                                    }, Constant.SELECT_CAMER), Constant.SELECT_CAMER);
                        }
                    }
                })
                .create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.SELECT_PICTURE:
                imgFileUri=getImageUri(data,true);
                break;
            case Constant.SELECT_CAMER:
                startActivityForResult(UriResolver.getCropImageUri( imgFileUri,  100,  200), Constant.SELECT_Crop);
                break;
            case Constant.SELECT_Crop:
                imgFileUri=getImageUri(data,false);
                //处理裁剪的结果图片
                break;
        }

    }
    private Uri getImageUri(Intent data,boolean useParcelableExtra) {
        Parcelable pdata = data.getParcelableExtra("data");
        Bitmap bitmap =null;
        if(useParcelableExtra){

            try{
                bitmap=(Bitmap)pdata;
            }catch (Exception e){

            }
            if(bitmap!=null){
                File parent=new File(Environment.getExternalStorageDirectory(),"uploadface") ;
                String fileName="temp.jpg";
                boolean saveOk=BitmapUtil.saveBitmap(bitmap,parent,fileName,100);
                bitmap.recycle();
                if(saveOk) {
                    return Uri.parse(new File(parent, fileName).getAbsolutePath());
                }
            }
        }
        Uri uri = null;
        if (data != null && data.getData() != null) {
            uri = data.getData();//bitmap中的
        }


// 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
        if (uri == null) {
            if (imgFileUri != null) {
                uri = imgFileUri;
            }
        }
        return uri;

    }

}
