package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */

public class RecycleViewFragment extends  Fragment {
    private ArrayList<String> mData;
    private RecyclerView mRecyclerView;

    public static RecycleViewFragment newInstance() {
        Bundle args = new Bundle();

        RecycleViewFragment fragment = new RecycleViewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recycleview_layout,container,false);
        setScrollViewContent(view);
        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 刷新ScrollView的内容
     */
    private void setScrollViewContent(View view) {


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
       // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,true));
        //RecyclerViewHeader必须在RecyclerView设置了LayoutManager之后调用。
        //如果你打算在RecyclerView中使用setOnScrollListener(...)方法，确保在setOnScrollListener(...)的attachTo(...)方法之前使用


        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(i);
        }
        mRecyclerView.setAdapter(new MyRecyclerViewAdapter(getContext(), datas));


    }
}