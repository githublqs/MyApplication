package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.fileupload.UploadFaceListFragment;

import java.util.ArrayList;

import as2.lqs.com.mylibrary.JNITest;


public class MainActivity extends AppCompatActivity/* implements View.OnClickListener*/ {
    private  NavigationView navigationView;
    private  DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private  Toolbar toolbar;
    private ArrayList<String> list_title;
    private static int[] imageResId = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };
    private TabLayout tabCustomTab_FindFragment_title;
    private ArrayList<Fragment> list_fragments ;
    private void  setUpAppBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("CollapsingToolbarLayout");
        mCollapsingToolbarLayout.setActivated(false);
//通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_drawer_layout);
       /* Button button_test_tab_layout_activity= (Button) findViewById(R.id.button_test_tab_layout_activity);
        button_test_tab_layout_activity.setOnClickListener(this);
        Button button_test_CollapsingToolbarLayout_activity= (Button) findViewById(R.id.button_test_CollapsingToolbarLayout_activity);
        button_test_CollapsingToolbarLayout_activity.setOnClickListener(this);*/
        setUpAppBar();
    initNavigationView();
    setUpViewPager();
       /* JNITest jniTest=new JNITest();
        Toast.makeText(getApplicationContext(),jniTest.getStrFromJni(),Toast.LENGTH_SHORT).show();*/
        //setUpListeners();

    }

    /*private void setUpListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/

    private void setUpViewPager() {
        list_title = new ArrayList<String>();
        list_title.add("pager0");
        list_title.add("上传图片列表");
        list_title.add("pager2");

        list_fragments = new ArrayList<Fragment>();

        //list_fragments.add(MDWidgetFragment.newInstance());

        list_fragments.add(RecycleViewFragment.newInstance());
        list_fragments.add(UploadFaceListFragment.newInstance());
        list_fragments.add(ChildViewPagerFragment.newInstance());

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
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item, null);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


       /* if (item.getItemId() == android.R.id.home){
            //打开抽屉侧滑菜单
            drawerLayout.openDrawer(GravityCompat.START);
        }*/
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initNavigationView(){
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // set a custom shadow that overlays the main content when the drawer
        // opens
       /* drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);*/

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
               toolbar, R.string.drawer_open,
                R.string.drawer_close)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {

                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {

                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };
        //设置侧滑菜单选择监听事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                //关闭抽屉侧滑菜单
                drawerLayout.closeDrawers();

                setTitle(menuItem.getTitle());
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    public void  startTabLayoutTestActivity(View view){
        startActivity(new Intent(this,Test_0_TabLayout_Activity.class));
    }

   /* @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_test_tab_layout_activity:
                startTabLayoutTestActivity(view);
                break;
            case R.id.button_test_CollapsingToolbarLayout_activity:
                startActivity(new Intent(this,CollapsingToolbarLayoutActivity.class));

                break;
        }
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
