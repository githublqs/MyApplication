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
import android.text.Editable;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.administrator.myapplication.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
//http://blog.csdn.net/jdsjlzx/article/details/51354433
//ButterKnife 8.0.1不生效的问题
//如果使用gradle-experimental
//则根据如下说明配置
//https://bitbucket.org/pvoid/android-apt-experemental/overview
//http://jakewharton.github.io/butterknife/
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 用户注册页
 */
public class RegisterActivity extends BaseAppCompatActivity   {
    @BindView(R.id.ivUserfaceImg)
    ImageView ivUserfaceImg;
   @BindView(R.id.spinnerGender)
    public Spinner spinnerGender;
   @BindView(R.id.buttonChooseUserFaceImg)
    Button buttonChooseUserFaceImg;//选择用户照片

    @BindView(R.id.buttonRegister)
     Button buttonRegister;//注册按钮
    @BindView(R.id.buttonLogin_page)
    Button buttonLogin_page;//转到登陆页

   @NotEmpty(message="请输入用户名")
    @Length(min=2,max = 30,trim =true,message = "字数在2 到 30之间")
    @BindView(R.id.editTextUserName)
     EditText editTextUserName;//用户名

    @Password(message="非法密码",min = 6, scheme = Password.Scheme.ALPHA_NUMERIC)
    @NotEmpty(message="请输入密码")
    @BindView(R.id.editTextPassword)
   EditText editTextPassword;//密码

    @ConfirmPassword(message="两次输入的密码不一致")
    @NotEmpty(message="请输入密码")
    @BindView(R.id.confirmPasswordEditText)
     EditText confirmPasswordEditText;

    @BindView(R.id.editTextNickName)
     EditText editTextNickName;//昵称
    private Uri imgFileUri;
    private String mNickName;
    private String mPassword;
    private String mconformPassword;
    private String mUserName;
    private String mGender;
    private String imgBase64Str;
    private  static  final String TAG="RegisterActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    /**
     * 转到登陆页
     */
    @OnClick(R.id.buttonLogin_page)
    void goToLoginPage(){
        Intent inttent=new Intent(RegisterActivity.this,LoginActivity.class);
        inttent.putExtra("username",mUserName);
        startActivity(inttent);
    }

    /**
     * 发送注册请求
     */
    @OnClick(R.id.buttonRegister)
    public void prepareSendrRegisterRequest(){
        //Android InputFilters()理解
        //http://blog.sina.com.cn/s/blog_78806ae901018mxz.html
        //android TextView setEms 方法名字 em是什么,暂时不知其与 maxEms 还有MaxLength，Layout_width 的最佳时间
        //new LoginFilter.UsernameFilterGeneric() 与 android:inputType="textPersonName" 有什么区别 与汉字的关系？
        validator.validate();
    }
    private  void setJsonRequest(JSONObject jsonRequest){
        try {
            jsonRequest.put("username",mUserName);
            jsonRequest.put("password",mPassword);
            jsonRequest.put("nickname",mNickName);
            jsonRequest.put("faceimg",imgBase64Str);
            jsonRequest.put("gender",mGender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void sendRegisterRequest() {
        String url=Constant.UserRegisterUrl;
        JSONObject jsonRequest =new JSONObject();//用户注册信息
        setJsonRequest(jsonRequest);
        SessionJsonObjectRequest request = new SessionJsonObjectRequest(Request.Method.POST, url,jsonRequest ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        responseRegister(jsonObject);
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
    private void responseRegister(JSONObject jsonObject){
        try {
            if(jsonObject.getBoolean("registerSuccess")){
                final String username=jsonObject.getString("username");
                //发送登陆请求
                showSnackbar("注册成功",buttonRegister);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //跳转到登录页面
                        mUserName=username;
                        goToLoginPage();
                    }
                }, 2000);
            }else{
                int errorcode=jsonObject.getInt("errorcode");
                if(errorcode==1){
                    showSnackbar("注册失败,用户名已经被注册过",buttonRegister);
                }else if(errorcode==-9){
                    showSnackbar("注册失败",buttonRegister);
                }else if(errorcode==2){
                    showSnackbar("注册失败",buttonRegister);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    /**
     * 选择照片
     */
    @OnClick(R.id.buttonChooseUserFaceImg)
    void showChoosePicWhereDialog() {
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == Constant.SELECT_PICTURE) {
                            /*File parent=new File(Environment.getExternalStorageDirectory(),"uploadface") ;
                            String fileName="temp.jpg";
                            Uri uri=Uri.fromFile(new File(parent,fileName));
                            imgFileUri=uri;*/
                            //测试发现，直接裁切会卡死在选择完图片的后一阶段

                            /*startActivityForResult(Intent.createChooser(UriResolver.getScaledImageFrompPotoAlbum(uri,3,4,150,200),
                                    "选择图片"), Constant.SELECT_PICTURE);*/
                            startActivityForResult(UriResolver.getImageOrCaptureIntent(RegisterActivity.this,null, Constant.SELECT_PICTURE), Constant.SELECT_PICTURE);

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
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case Constant.SELECT_PICTURE:
                    File parent=new File(Environment.getExternalStorageDirectory(),"uploadface") ;
                    String fileName="temp.jpg";
                    imgFileUri=getImageUri(data,false);
                    FileUtil.copyFile(UriResolver.getPath(this,imgFileUri),new File(parent,fileName).getAbsolutePath());
                    Uri uri=Uri.fromFile(new File(parent,fileName));
                    imgFileUri=uri;

                    /*showSelectedPic( imgFileUri);*/
                    startActivityForResult(UriResolver.getCropImageUri( imgFileUri,3,4,  150,  200), Constant.SELECT_Crop);
                    break;
                case Constant.SELECT_CAMER:
                    startActivityForResult(UriResolver.getCropImageUri( imgFileUri,3,4,  150,  200), Constant.SELECT_Crop);
                    break;
                case Constant.SELECT_Crop:
                    imgFileUri=getImageUri(data,false);
                    showSelectedPic( imgFileUri);
                    break;
            }
        }
    }

    /**
     * 显示选择的照片
     * @param imgFileUri 图片的uri
     */
    private void showSelectedPic(Uri imgFileUri) {
        ivUserfaceImg.setImageURI(imgFileUri);
    }

    private Uri getImageUri(Intent data,boolean useParcelableExtra) {

        Bitmap bitmap =null;
        if(useParcelableExtra){
            try{
                if(data!=null){
                    Parcelable pdata = data.getParcelableExtra("data");
                    bitmap=(Bitmap)pdata;
                }

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

    @Override
    public void onValidationSucceeded() {
        if(editTextUserName.getError()!=null||editTextPassword.getError()!=null||
                confirmPasswordEditText.getError()!=null||editTextNickName.getError()!=null){
            return;
        }
        //照片
        if(imgFileUri==null){
            showSnackbar("请选择相片",buttonRegister);
            return;
        }
        imgBase64Str=Base64Helper.bitmapToBase64(this,imgFileUri);
        if(imgBase64Str==null){
            showSnackbar("获取照片流失败",buttonRegister);
            return;
        }

        //用户名
        mUserName=editTextUserName.getText().toString().trim();
        //密码
        mPassword=editTextPassword.getText().toString().trim();
        //昵称
        mNickName=editTextNickName.getText().toString().trim();

        mGender=spinnerGender.getSelectedItemPosition()==0?"1":spinnerGender.getSelectedItemPosition()==1?"0":"2";
        sendRegisterRequest();
    }



    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
