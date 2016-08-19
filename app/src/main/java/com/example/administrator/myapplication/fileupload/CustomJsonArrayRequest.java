package com.example.administrator.myapplication.fileupload;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lqs on 2016/8/19.
 */
public class CustomJsonArrayRequest extends JsonRequest<JSONArray> {

    public CustomJsonArrayRequest(int method,String url, String requestBody,Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }
    public CustomJsonArrayRequest(String url, String requestBody,Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(0, url, requestBody, listener, errorListener);
    }
    public CustomJsonArrayRequest(String url,Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(0, url, null, listener, errorListener);
    }
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String je = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(je), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }
}
