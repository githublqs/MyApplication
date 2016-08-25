package com.example.administrator.myapplication.fileupload;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.custom.view.PageStateListener;
import com.mobsandgeeks.saripaar.annotation.Url;

import net.mobctrl.views.SuperSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by lqs on 2016/8/23.
 */
public class UploadFaceListFragment extends Fragment implements PageStateListener {
    private static final String TAG = "UploadFaceListFragment";
    ProgressBar progressBar ;
    TextView textView ;
    ImageView imageView;

    ProgressBar footerProgressBar;
    ImageView footerImageView;
    TextView footerTextView;

    private int mCurrentPos;
    @BindView(R.id.listview_uploadface)
    SwipeMenuListView listviewUploadface;
    @BindView(R.id.refresh_view)
    SuperSwipeRefreshLayout refreshView;
    private UploadFaceListFragmentInteractionListener mListener;
    private PageInfo pageInfo = new PageInfo(1, Constant.PageSize, true);
    private ArrayList<UploadFace> uploadFaces;
    private UploadFaceAdapter adapter;
    private Dialog pDialog;
    private boolean fetchingUploadFacesFlag;
    private UploadFace currentUploadFace;

    private  void  showLoadingDialog(boolean show){
        if(pDialog==null){
            pDialog=CustomProgressDialog.createLoadingDialog(getContext(),"Loading...");

        }
        if(show)
            pDialog.show();
        else
            pDialog.hide();
    }


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
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


// set creator
        listviewUploadface.setMenuCreator(creator);
        listviewUploadface.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
        // step 2. listener item click event
        listviewUploadface.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                UploadFace item = uploadFaces.get(position);
                switch (index) {
                    case 0:
                        prepareToEditItem(UploadFaceListFragment.this, position);
                        break;
                    case 1:
                        if (!fetchingUploadFacesFlag&&mListener!=null){
                            //请求服务器删除该条记录
                            String url=Constant.DeleteUploadFaceUrl;
                            JSONObject jsonRequest=new JSONObject();
                            try {
                                currentUploadFace=adapter.getUploadFaces().get(position);
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
                                        fetchingUploadFacesFlag=false;
                                        showLoadingDialog(false);
                                        if(jsonObject.getBoolean("deleteSuccess")){
                                            showSnackBar(listviewUploadface,"删除成功");
                                            uploadFaces.remove(currentUploadFace) ;
                                            adapter.notifyDataSetChanged();
                                        }else {
                                            showSnackBar(listviewUploadface,"删除失败");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    fetchingUploadFacesFlag=false;
                                    showLoadingDialog(false);
                                     showSnackBar(listviewUploadface,"删除失败");
                                    VolleyController.getInstance(getContext()).cancelAll("deleteItemUploadFaceListFragment");

                                }
                            });
                            jsonObjectRequest.setTag("deleteItemUploadFaceListFragment");
                            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//还是会重复提交
                            VolleyController.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
                            fetchingUploadFacesFlag=true;
                            showLoadingDialog(true);
                        }

                        break;
                }
                return false;
            }
        });

        // set MenuStateChangeListener
        listviewUploadface.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });


        return view;
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle =getArguments();
        if(bundle!=null){
        }
        //将 getArguments() 中的取出赋给成员
        uploadFaces = new ArrayList<UploadFace>();


    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    private View createHeaderView() {
        View headerView = LayoutInflater.from(refreshView.getContext())
                .inflate(R.layout.layout_head, null);
        progressBar = (ProgressBar) headerView.findViewById(R.id.pb_view);
        textView = (TextView) headerView.findViewById(R.id.text_view);
        textView.setText("下拉刷新");
        imageView = (ImageView) headerView.findViewById(R.id.image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.down_arrow);
        progressBar.setVisibility(View.GONE);
        return headerView;
    }
    private View createFooterView() {
        View footerView = LayoutInflater.from(refreshView.getContext())
                .inflate(R.layout.layout_footer, null);
        footerProgressBar = (ProgressBar) footerView
                .findViewById(R.id.footer_pb_view);
        footerImageView = (ImageView) footerView
                .findViewById(R.id.footer_image_view);
        footerTextView = (TextView) footerView
                .findViewById(R.id.footer_text_view);
        footerProgressBar.setVisibility(View.GONE);
        footerImageView.setVisibility(View.VISIBLE);
        footerImageView.setImageResource(R.drawable.down_arrow);
        footerTextView.setText("上拉加载更多...");
        return footerView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new UploadFaceAdapter(getActivity(), uploadFaces);
        listviewUploadface.setAdapter(adapter);
        refreshView.setHeaderView(createHeaderView());// add headerView

        //etTargetScrollWithLayout(false/true);//default true
        refreshView.setFooterView(createFooterView());
        //设置下拉时，被包含的View是否随手指的移动而移动
        refreshView.setTargetScrollWithLayout(true);
        //setHeaderViewBackgroundColor
        //        设置下拉刷新头部背景色
        refreshView.setHeaderViewBackgroundColor(0xff888888);
        /*setDefaultCircleProgressColor
                设置默认圆形进度条颜色

        setDefaultCircleBackgroundColor
                设置默认圆形背景色

        setDefaultCircleShadowColor
                设置默认圆形的阴影颜色*/

        refreshView
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
                    @Override
                    public void onRefresh() {
                        textView.setText("正在刷新");
                        imageView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        fetchUploadFaces(new PageInfo(1,Constant.PageSize,true));
                    }

                    @Override
                    public void onPullDistance(int distance) {
                        // pull distance
                    }

                    @Override
                    public void onPullEnable(boolean enable) {
                        textView.setText(enable ? "松开刷新" : "下拉刷新");
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setRotation(enable ? 180 : 0);
                    }
                });

        refreshView
                .setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        footerTextView.setText("正在加载...");
                        footerImageView.setVisibility(View.GONE);
                        footerProgressBar.setVisibility(View.VISIBLE);
                        fetchUploadFaces(new PageInfo(pageInfo.getPageNo()+1,Constant.PageSize,true));
                    }
                    @Override
                    public void onPushEnable(boolean enable) {
                        footerTextView.setText(enable ? "松开加载" : "上拉加载");
                        footerImageView.setVisibility(View.VISIBLE);
                        footerImageView.setRotation(enable ? 0 : 180);
                    }

                    @Override
                    public void onPushDistance(int distance) {
                        // TODO Auto-generated method stub

                    }

                });

        fetchUploadFaces(new PageInfo(1,Constant.PageSize,true));
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UploadFaceListFragmentInteractionListener) {
            mListener = (UploadFaceListFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement UploadFaceListFragment.OnFragmentInteractionListener");
        }
        super.onAttach(context);
    }


    private void prepareToEditItem(UploadFaceListFragment fragment, int position){
        if(!fetchingUploadFacesFlag&&mListener!=null){
                mCurrentPos = position ;
                mListener.onListViewItemClick(fragment,position);

        }
    }

    @Override
    public boolean isExecuting() {
        return fetchingUploadFacesFlag;
    }

    public interface UploadFaceListFragmentInteractionListener {
        public void onListViewItemClick(UploadFaceListFragment fragment, int position);
    }

    public void fetchUploadFaces(final PageInfo pageInfo) {
        String requestBody=pageInfo.toJsonString();
        CustomJsonArrayRequest req=new CustomJsonArrayRequest(
                Request.Method.POST,
                Constant.UploadFaceListUrl,
                requestBody,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
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
                                uploadFace.setId(Long.parseLong(object.getString("id")));
                                uploadFace.setiMgFileName(object.getString("imgFileiName"));
                                uploadFace.setThumbnailImgFileName(object.getString("thumbnailImgFileName"));
                                uploadFaces.add(uploadFace);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(pageInfo.getPageNo()<=1){
                            refreshView.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                        }else{
                            footerImageView.setVisibility(View.VISIBLE);
                            footerProgressBar.setVisibility(View.GONE);
                            refreshView.setLoadMore(false);
                        }
                        fetchingUploadFacesFlag=false;
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyController.getInstance(getActivity()).cancelAll("fetchUploadFaces");
                fetchingUploadFacesFlag=false;
            }
        });
        req.setTag("fetchUploadFaces");
        req.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//还是会重复提交
        fetchingUploadFacesFlag=true;
        VolleyController.getInstance(getActivity()).addToRequestQueue(req);
    }
    public UploadFaceAdapter getAdapter(){
        return  adapter;
    }

}

