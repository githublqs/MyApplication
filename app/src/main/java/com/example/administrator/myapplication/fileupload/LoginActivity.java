package com.example.administrator.myapplication.fileupload;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lqs on 2016/8/21.
 * 用户登录页
 */

/*在程序中加入以下代码时，软键盘会出现:
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
        如果要让软键盘消失，则为以下代码：
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        很多应用中对于一个界面比如进入搜索界面或者修改信息等等情况，为了用户体验应该自动弹出软键盘而不是让用户主动点击输入框才弹出（因为用户进入该界面必然是为了更改信息）。具体实现这种效果如下：
        [代码]java代码

        EditText  editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        首先要对指定的输入框请求焦点。然后调用输入管理器弹出软键盘。*/
public class LoginActivity extends BaseAppCompatActivity {
    @NotEmpty(message="请输入用户名")
    @Length(min=2,max = 30,trim =true,message = "字数在2 到 30之间")
    @BindView(R.id.editTextUserName)
    EditText editTextUserName;//用户名

    @Password(message="非法密码",min = 6, scheme = Password.Scheme.ALPHA_NUMERIC)
    @NotEmpty(message="请输入密码")
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;//密码



    @BindView(R.id.checkBoxRemberMe)
    CheckBox checkBoxRemberMe;
    @BindView(R.id.checkBoxAutoLogin)
    CheckBox checkBoxAutoLogin;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    private String mUserName;
    private String mPassword;
    private boolean mRemberMe;
    private boolean mAutoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences=getSharedPreferences("settings",MODE_PRIVATE);
        mUserName=sharedPreferences.getString("username","");
        mPassword=sharedPreferences.getString("password","");
        if ("".equals(mUserName)){
            mRemberMe=sharedPreferences.getBoolean("remberme",true);
            mAutoLogin=sharedPreferences.getBoolean("autologin",true);
        }else{
            mRemberMe=sharedPreferences.getBoolean("remberme",false);
            mAutoLogin=sharedPreferences.getBoolean("autologin",false);
        }

        editTextUserName.setText(mUserName);
        editTextPassword.setText(mPassword);
        checkBoxRemberMe.setChecked(mRemberMe);
        checkBoxAutoLogin.setChecked(mAutoLogin);

       Intent intent=getIntent();
        if (intent!=null){
            if (intent.hasExtra("username")){
                mUserName=intent.getStringExtra("username");
                editTextUserName.setText(mUserName);
            }
        }
    }
    /**
     * 发送登录请求
     */
    private void sendLoginRequest() {
        String url = Constant.UserLoginUrl;
        JSONObject jsonRequest = new JSONObject();//用户登录信息
        prepareJsonRequest(jsonRequest);
        SessionJsonObjectRequest request = new SessionJsonObjectRequest(Request.Method.POST, url, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if(jsonObject.getBoolean("loginSuccess")){
                                String username=jsonObject.getString("username");
                                mUserName=username ;
                                SharedPreferences sp=getSharedPreferences("settings",MODE_PRIVATE);
                                //保存Session
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("autologin",mAutoLogin);
                                editor.putBoolean("remberme",mRemberMe);
                                editor.putString("username",mUserName);
                                editor.putString("password",mPassword);
                                editor.commit();
                                showSnackbar("登录成功",btnLogin);
                                btnLogin.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 1000);
                            }else {
                                int errorcode=jsonObject.getInt("errorcode");
                                //2 用户没有注册
                                if(errorcode==2){
                                    showSnackbar("用户没有注册",btnLogin);

                                }else if (errorcode==1){//用户已经登陆
                                    showSnackbar("用户已经登陆",btnLogin);

                                }else if(errorcode==-9){
                                    showSnackbar("登录失败",btnLogin);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showSnackbar("登录失败",btnLogin);
                    }
                }
        );
        VolleyController.getInstance(this).getRequestQueue().add(request);
    }

    private void prepareJsonRequest(JSONObject jsonRequest) {
        try {
            jsonRequest.put("username",mUserName);
            jsonRequest.put("password",mPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onValidationSucceeded() {
        if(editTextUserName.getError()!=null||editTextPassword.getError()!=null){
            return;
        }
        mUserName=editTextUserName.getText().toString().trim();
        mPassword=editTextPassword.getText().toString().trim();
        mRemberMe=checkBoxRemberMe.isChecked();
        mAutoLogin=checkBoxAutoLogin.isChecked();
        sendLoginRequest();
    }


    @OnClick(R.id.btnLogin)
    public void onClick() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        validator.validate();
    }
}
