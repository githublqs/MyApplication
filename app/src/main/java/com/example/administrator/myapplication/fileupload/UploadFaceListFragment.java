package com.example.administrator.myapplication.fileupload;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.pullableview.PullToRefreshLayout;
import com.example.administrator.myapplication.pullableview.PullableListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by lqs on 2016/8/23.
 */
public class UploadFaceListFragment extends Fragment implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "UploadFaceListFragment";
    @BindView(R.id.listview_uploadface)
    PullableListView listviewUploadface;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    private OnFragmentInteractionListener mListener;
    private PageInfo pageInfo = new PageInfo(1, Constant.PageSize, true);
    private ArrayList<UploadFace> uploadFaces;
    private UploadFaceAdapter adapter;
    private Dialog pDialog;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        UploadFaceListFragment fragment = new UploadFaceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_uploadfacelist, container, false);
        ButterKnife.bind(this, view);
        refreshView.setOnRefreshListener(this);
        listviewUploadface.setAdapter(adapter);
        refreshView.autoRefresh();
        showPDialog();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将 getArguments() 中的取出赋给成员
        uploadFaces = new ArrayList<UploadFace>();
        adapter = new UploadFaceAdapter(getActivity(), uploadFaces);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        fetchUploadFaces(new PageInfo(1,Constant.PageSize,true));
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        fetchUploadFaces(new PageInfo(pageInfo.getPageNo()+1,Constant.PageSize,true));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }
    private void showPDialog() {
        if (pDialog == null){
            pDialog = CustomProgressDialog.createLoadingDialog(getActivity(),"Loading...");
        }
        //pDialog.setMessage("Loading...");
        pDialog.show();
    }
    private void hidePDialog(){
        if(pDialog!=null)
            pDialog.dismiss();
        pDialog=null;
    }
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
                                UploadFaceListFragment.this.pageInfo.setPageNo(pageInfo.getPageNo());
                            }
                        }else{
                            UploadFaceListFragment.this.pageInfo.setPageNo(pageInfo.getPageNo());
                        }
                        UploadFaceListFragment.this.pageInfo.setPageSize(pageInfo.getPageSize());
                        UploadFaceListFragment.this.pageInfo.setRetListDirect(pageInfo.isRetListDirect());

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
                            refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }else{
                            refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log2.e(TAG,"error:"+volleyError.getMessage());
                hidePDialog();
            }
        });

        VolleyController.getInstance(getActivity()).addToRequestQueue(req);
    }
}
