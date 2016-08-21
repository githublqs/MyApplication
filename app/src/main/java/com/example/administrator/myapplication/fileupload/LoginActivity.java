package com.example.administrator.myapplication.fileupload;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.myapplication.R;

import org.json.JSONObject;

/**
 * Created by lqs on 2016/8/21.
 * 用户登录页
 */

public class LoginActivity extends BaseAppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_login);
    }
    /**
     * 发送登录请求
     */
    private void sendLoginRequest() {
        String url=null;
        JSONObject jsonRequest =new JSONObject();//用户登录信息
        SessionJsonObjectRequest request = new SessionJsonObjectRequest(Request.Method.POST, url,jsonRequest ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        VolleyController.getInstance(this).getRequestQueue().add(request);
    }
}
