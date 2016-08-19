package com.example.administrator.myapplication.fileupload;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.myapplication.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import com.example.administrator.myapplication.pullableview.PullToRefreshLayout;
import com.example.administrator.myapplication.pullableview.PullableListView;

public class UploadFaceListActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG=UploadFaceListActivity.class.getSimpleName();
    private ArrayList<UploadFace> uploadFaces;
    private UploadFaceAdapter adapter;
    private PullableListView uploadFaceListView;
    private Dialog pDialog;
    private boolean isFirstIn;
    private PullToRefreshLayout ptrl;

   /* @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        // 第一次进入自动刷新

        if (isFirstIn)
        {
            ptrl.autoRefresh();
            isFirstIn = false;
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfacelist);
        ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        uploadFaceListView= (PullableListView) findViewById(R.id.listview_uploadface);
        uploadFaces=new ArrayList<UploadFace>();
        adapter=new UploadFaceAdapter(this,uploadFaces);
        uploadFaceListView.setAdapter(adapter);
        ptrl.autoRefresh();
        //fetchUploadFaces(1,Constant.PageSize);
        showPDialog();
    }
    private  PageInfo pageInfo=new PageInfo(1,Constant.PageSize,true);

    private void fetchUploadFaces(final PageInfo pageInfo) {
        String requestBody=pageInfo.toJsonString();
        CustomJsonArrayRequest req=new CustomJsonArrayRequest(
                Request.Method.POST,
                Constant.UploadFaceListUrl,
                requestBody,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            hidePDialog();
                        }
                    });
                    if(pageInfo.getPageNo()<=1){
                        uploadFaces.clear();
                    }
                    if(pageInfo.getPageNo()>=2){
                        if(jsonArray.length()>=1){
                            UploadFaceListActivity.this.pageInfo.setPageNo(pageInfo.getPageNo());
                        }
                    }else{
                        UploadFaceListActivity.this.pageInfo.setPageNo(pageInfo.getPageNo());
                    }
                    UploadFaceListActivity.this.pageInfo.setPageSize(pageInfo.getPageSize());
                    UploadFaceListActivity.this.pageInfo.setRetListDirect(pageInfo.isRetListDirect());

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            UploadFace uploadFace = new UploadFace();
                            uploadFace.setDate(object.getString("date"));
                            uploadFace.setiMgFileName(object.getString("imgFileiName"));
                            uploadFace.setThumbnailImgFileName(object.getString("thumbnailImgFileName"));
                            uploadFaces.add(uploadFace);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(pageInfo.getPageNo()<=1){
                        //注意刷新数据
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }else{
                        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                    adapter.notifyDataSetChanged();
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,"error:"+volleyError.getMessage());
                hidePDialog();
            }
        });

        VolleyController.getInstance(this).addToRequestQueue(req);
    }

    public void hidePDialog(){
        if(pDialog!=null)
            pDialog.dismiss();
        pDialog=null;
    }
    public void showPDialog() {
        if (pDialog == null){
            pDialog = CustomProgressDialog.createLoadingDialog(this,"Loading...");
        }
        //pDialog.setMessage("Loading...");
        pDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        fetchUploadFaces(new PageInfo(1,Constant.PageSize,true));
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        fetchUploadFaces(new PageInfo(pageInfo.getPageNo()+1,Constant.PageSize,true));
    }
}
