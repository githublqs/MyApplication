package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */

public class ChildViewPagerFragment extends  Fragment {
    private ArrayList<String> mData;
    private ViewPager vp_childfragment_pager;

    public static ChildViewPagerFragment newInstance() {
        Bundle args = new Bundle();

        ChildViewPagerFragment fragment = new ChildViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_childfragment_layout,container,false);
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
        vp_childfragment_pager = (ViewPager) view.findViewById(R.id.vp_childfragment_pager);
        setUpViewPager(view);
        DecoratorViewPager vp = (DecoratorViewPager) vp_childfragment_pager;
        vp.setNestedpParent((ViewGroup)vp.getParent());
    }
    private  ArrayList<Fragment> child_fragments;
    private void setUpViewPager(View view) {

        child_fragments = new ArrayList<Fragment>();

        final ArrayList<String> list_title = new ArrayList<String>();
        list_title.add("pager0");
        list_title.add("pager1");
        list_title.add("pager2");
        child_fragments.add(MyFragment.newInstance(0, list_title));
        child_fragments.add(MyFragment.newInstance(1, list_title));
        child_fragments.add(MyFragment.newInstance(2, list_title));



        FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return child_fragments.get(position);
            }
            @Override
            public int getCount() {
                return list_title.size();
            }

        };
        ViewPager viewPager= (ViewPager) view.findViewById(R.id.vp_childfragment_pager);
        viewPager.setAdapter(fragmentPagerAdapter);

    }
}