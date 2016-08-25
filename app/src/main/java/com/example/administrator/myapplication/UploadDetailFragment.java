package com.example.administrator.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.administrator.myapplication.custom.view.PageStateListener;
import com.example.administrator.myapplication.fileupload.Constant;
import com.example.administrator.myapplication.fileupload.CustomProgressDialog;
import com.example.administrator.myapplication.fileupload.UploadFace;
import com.example.administrator.myapplication.fileupload.UploadFaceAdapter;
import com.example.administrator.myapplication.fileupload.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lqs on 2016/8/25.
 */
public class UploadDetailFragment extends Fragment implements PageStateListener {
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.btnEdit)
    Button btnEdit;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.tvDate)
    TextView tvDate;
    private UploadDetailFragmentListener mListener;
    private UploadFace uploadFaceAdapter;
    private UploadFace uploadFace;
    private boolean executingFlag = false;//正在执行异步服务器操作
    private Dialog pDialog;


    /*public static final String ARGUMENT = "argument";
    public static final String RESPONSE = "response";*/
    public static UploadDetailFragment newInstance() {
        Bundle bundle = new Bundle();
        /*bundle.putString(ARGUMENT, argument);*/
        UploadDetailFragment contentFragment = new UploadDetailFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploaddetail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UploadDetailFragmentListener) {
            mListener = (UploadDetailFragmentListener) context;

        } else {
            throw new ClassCastException("must implements UploadDetailFragment.UploadDetailFragmentListener");
        }
        super.onAttach(context);
    }
    private  void  showLoadingDialog(boolean show){
        if(pDialog==null){
            pDialog= CustomProgressDialog.createLoadingDialog(getContext(),"Loading...");

        }
        if(show)
            pDialog.show();
        else
            pDialog.hide();
    }
    public void showCurrentItem(UploadFaceAdapter uploadFaceAdapter, final UploadFace uploadFace) {

        this.uploadFaceAdapter = uploadFace;
        this.uploadFace = uploadFace;
        tvDate.setText(uploadFace.getDate());

        ImageRequest imageRequest = new ImageRequest(
                Constant.UploadFaceDirUrl + uploadFace.getImgFileiName(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        executingFlag=false;
                        ivImage.setImageBitmap(response);
                        if (mListener != null) {
                            mListener.onItemLoaded(uploadFace);
                        }
                        showLoadingDialog(false);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                executingFlag=false;
                VolleyController.getInstance(getContext()).cancelAll("getDetailImage");
                showLoadingDialog(false);
            }
        });
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//还是会重复提交
        imageRequest.setTag("getDetailImage");
        VolleyController.getInstance(getContext()).getRequestQueue().add(imageRequest);
        executingFlag=true;
        showLoadingDialog(true);
    }

    private void deleteItem() {



        if (!isExecuting()&&mListener!=null&& uploadFace!=null){
            //请求服务器删除该条记录
            String url=Constant.DeleteUploadFaceUrl;
            JSONObject jsonRequest=new JSONObject();
            UploadFace currentUploadFace=null;
            try {
                currentUploadFace=uploadFace;
                jsonRequest.put("id",currentUploadFace.getId());
                jsonRequest.put("uploadimg",currentUploadFace.getImgFileiName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, jsonRequest
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if(jsonObject.getBoolean("deleteSuccess")){
                            if (mListener != null) {
                                btnDelete.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showSnackBar(btnDelete,"删除成功");
                                        mListener.onItemDeleted(uploadFace,true);
                                    }
                                }, 1500);
                            }
                        }else {
                            showSnackBar(btnDelete,"删除失败");
                            if (mListener != null) {
                                mListener.onItemDeleted(uploadFace,false);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setExecuting(false);
                    showLoadingDialog(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    setExecuting(false);
                    showLoadingDialog(false);
                    showSnackBar(btnDelete,"删除失败");
                    VolleyController.getInstance(getContext()).cancelAll("deleteItemUploadFaceDetal");
                    if (mListener != null) {
                        mListener.onItemDeleted(uploadFace,false);
                    }

                }
            });
            jsonObjectRequest.setTag("deleteItemUploadFaceDetal");
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//还是会重复提交
            VolleyController.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
            setExecuting(true);
            showLoadingDialog(true);
        }


    }
    private void  showSnackBar(View view ,CharSequence msg){

        final Snackbar snackBar = Snackbar.make(view,msg,Snackbar.LENGTH_SHORT);
        snackBar.setAction("隐藏", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }
    private void editItem() {



    }
    @OnClick({R.id.btnEdit, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEdit:
                editItem();
                break;
            case R.id.btnDelete:
                deleteItem();
                break;
        }
    }

    public void setDefaultState() {
        tvDate.setText("");
        ivImage.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public boolean isExecuting() {
        return executingFlag;
    }
    private void setExecuting(boolean excuting) {
         executingFlag=excuting;
    }

    public interface UploadDetailFragmentListener {
        void onItemDeleted(UploadFace uploadFace,boolean deleteSuccess);
        void onItemEdited();
        void onItemLoaded(UploadFace uploadFace);
    }
}
