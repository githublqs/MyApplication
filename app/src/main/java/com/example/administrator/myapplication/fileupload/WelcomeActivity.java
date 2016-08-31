package com.example.administrator.myapplication.fileupload;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.myapplication.FaceApplication;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseAppCompatActivity {
    @BindView(R.id.activity_welcome)
    RelativeLayout activityWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //如果已经登录
        boolean logedIn = FaceApplication.logedIn();
        if (logedIn) {
            SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
            boolean autologin = sharedPreferences.getBoolean("autologin",false);
            if (autologin) {
                //自动登陆
                sendLoginRequest();
            } else {
                //跳转到登录页
                goToPageDelayed(LoginActivity.class,0);
                finish();
            }
        } else {

            goToPageDelayed(RegisterActivity.class,0);
            finish();
        }
    }

    private void sendLoginRequest() {
        String url = Constant.UserLoginUrl;
        JSONObject jsonRequest = new JSONObject();//用户登录信息
        prepareJsonRequest(jsonRequest);
        SessionJsonObjectRequest request = new SessionJsonObjectRequest(Request.Method.POST, url, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getBoolean("loginSuccess")) {
                                String username = jsonObject.getString("username");
                                SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);
                                //保存Session
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username", username);
                                editor.commit();
                                //跳转到主页
                                goToPageDelayed(MainActivity.class,0);
                            } else {
                                int errorcode = jsonObject.getInt("errorcode");
                                //2 用户没有注册
                                if (errorcode == 2) {
                                    showSnackbar("用户没有注册", activityWelcome);
                                    //跳转到注册页
                                    goToPageDelayed(RegisterActivity.class,1000);

                                } else if (errorcode == 1) {//用户已经登陆
                                    //跳转到主页
                                    goToPageDelayed(MainActivity.class,0);

                                } else if (errorcode == -9) {
                                    //跳转到登录页
                                    showSnackbar("登陆失败",activityWelcome);
                                    goToPageDelayed(LoginActivity.class,1000);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showSnackbar("登陆失败",activityWelcome);
                        goToPageDelayed(LoginActivity.class,1000);
                    }
                }
        );
        VolleyController.getInstance(this).getRequestQueue().add(request);
    }

    private void goToPageDelayed(final Class<?> target, int ms) {
        //跳转到登录页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,target);
                startActivity(intent);
                finish();
            }
        }, ms);
    }
    private void prepareJsonRequest(JSONObject jsonRequest) {
        try {
            SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);
            String mUserName=sp.getString("username","");
            String mPassword=sp.getString("password","");
            jsonRequest.put("username",mUserName);
            jsonRequest.put("password",mPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onValidationSucceeded() {
    }
}
