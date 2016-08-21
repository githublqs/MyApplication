package com.example.administrator.myapplication.fileupload;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LQS on 2016/8/21.
 * 扩展JsonObjectRequest支持Session处理
 */

public class SessionJsonObjectRequest extends JsonObjectRequest {
    public SessionJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public SessionJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        //将本次服务器响应头重的Session值存入Constant.localCookie;
        Response<JSONObject> superResponse = super.parseNetworkResponse(response);
        Map<String, String> responseHeaders = response.headers;
        String rawCookies = responseHeaders.get("Set-Cookie");
        if (rawCookies!=null) {
            Constant.localCookie = rawCookies.substring(0, rawCookies.indexOf(";"));
        }
        return superResponse;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        //本地cookie一并发送到服务器
        if(Constant.localCookie!=null&&Constant.localCookie.length()>0){
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("cookie", Constant.localCookie);
            return headers;
        }else {
            return super.getHeaders();
        }
    }
}
