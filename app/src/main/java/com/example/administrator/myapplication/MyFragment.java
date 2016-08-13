package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class MyFragment extends  Fragment{

    private static final String ARG_POSITION="ARG_POSITION";
    private static final String ARG_list_title="ARG_list_title";
    private int position;
    private ArrayList<String> list_title;

    public static MyFragment newInstance(int position, ArrayList<String> list_title) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
        args.putStringArrayList(ARG_list_title,list_title);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=null;
        if(position==1){
            view=inflater.inflate(R.layout.fragment_mdwidget_layout,container,false);
            ArrayList<String>mData = new ArrayList<>();
            for (int i = 1; i < 50; i++) {
                mData.add("pager" + 1 + " 第" + i + "个item");
            }
            //NestedScrollView下的LinearLayout
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_sc_content);
            layout.removeAllViews();
            for (int i = 0; i < mData.size(); i++) {
                View viewItem = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
                ((TextView) viewItem.findViewById(android.R.id.text1)).setText(mData.get(i));
                //动态添加 子View
                layout.addView(viewItem, i);
            }
        }else{
            view=inflater.inflate(R.layout.fragment_layout,container,false);

            TextView textView= (TextView) view.findViewById(R.id.textView);
            textView.setText(list_title.get(position));
        }

        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position =getArguments().getInt(ARG_POSITION,0);
        list_title=getArguments().getStringArrayList(ARG_list_title);

    }
}