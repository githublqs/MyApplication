package com.example.administrator.myapplication;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lqs on 2016/8/12.
 * 测试  materia design TabLayout 结合ViewPager 和 Fragment
 */

public class Test_0_TabLayout_Activity extends AppCompatActivity {
    //private TabLayout tab_FindFragment_title;
    private List<String> list_title;
    private List<Fragment> list_fragments;
    private static int[] imageResId = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };
    private TabLayout tabCustomTab_FindFragment_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Android5.0全透明状态栏效果，发现当 theme设置成 ThemeOverlay.AppCompat 的时候 不起作用
        /*getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }*/
        //沉浸式状态栏 配合android:fitsSystemWindows="true"
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/

        setContentView(R.layout.activity_test_0_tablayout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("CollapsingToolbarLayout");
//通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色



        list_title = new ArrayList<String>();
        list_title.add("pager0");
        list_title.add("pager1");
        list_title.add("pager2");

        list_fragments = new ArrayList<Fragment>();

        list_fragments.add(MDWidgetFragment.newInstance());
        list_fragments.add(MyFragment.newInstance(1, (ArrayList<String>) list_title));
        list_fragments.add(MyFragment.newInstance(2, (ArrayList<String>) list_title));

        tabCustomTab_FindFragment_title=(TabLayout)findViewById(R.id.tabCustomTab_FindFragment_title);

        FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list_fragments.get(position);
            }

            @Override
            public int getCount() {
                return list_title.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                //return list_title.get(position);
              /*  Drawable image = getResources().getDrawable(imageResId[position]);
                image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                // Replace blank spaces with image icon
                SpannableString sb = new SpannableString("   " + list_title.get(position));

                ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
                sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return sb;*/
                return null;

            }
            public View getTabView(int position){
                View view = LayoutInflater.from(Test_0_TabLayout_Activity.this).inflate(R.layout.tab_item, null);
                TextView tv= (TextView) view.findViewById(R.id.textView);
                tv.setText(list_title.get(position));
                ImageView img = (ImageView) view.findViewById(R.id.imageView);
                img.setImageResource(imageResId[position]);
                return view;
            }
        };
        ViewPager viewPager= (ViewPager) findViewById(R.id.vp_FindFragment_pager);
        viewPager.setAdapter(fragmentPagerAdapter);
       // tabLayout.setOnTabSelectedListener


        //tab_FindFragment_title.setupWithViewPager(viewPager);
        tabCustomTab_FindFragment_title.setupWithViewPager(viewPager);

        for (int i=0;i<tabCustomTab_FindFragment_title.getTabCount();i++){
            View view=getLayoutInflater().inflate(R.layout.tab_item,null);
            TextView  tv= (TextView) view.findViewById(R.id.textView);
            tv.setText(list_title.get(i));
            ImageView iv= (ImageView) view.findViewById(R.id.imageView);
            iv.setImageResource(imageResId[i]);
            tabCustomTab_FindFragment_title.getTabAt(i).setCustomView(view);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
