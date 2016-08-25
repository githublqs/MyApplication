package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.custom.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.administrator.myapplication.custom.view.CustomViewPager;
import com.example.administrator.myapplication.custom.view.PageStateListener;
import com.example.administrator.myapplication.fileupload.Constant;
import com.example.administrator.myapplication.fileupload.FileUploadFragment;
import com.example.administrator.myapplication.fileupload.Log2;
import com.example.administrator.myapplication.fileupload.PageInfo;
import com.example.administrator.myapplication.fileupload.UploadFace;
import com.example.administrator.myapplication.fileupload.UploadFaceAdapter;
import com.example.administrator.myapplication.fileupload.UploadFaceListFragment;
import com.example.administrator.myapplication.fileupload.VolleyController;

import java.util.ArrayList;

import as2.lqs.com.mylibrary.JNITest;

import static com.example.administrator.myapplication.R.id.imageView;
import static com.example.administrator.myapplication.R.id.tableLayout;


public class MainActivity extends AppCompatActivity/* implements View.OnClickListener*/
implements UploadFaceListFragment.UploadFaceListFragmentInteractionListener ,UploadDetailFragment.UploadDetailFragmentListener{
    private static final String TAG = "MainActivity";
    private  NavigationView navigationView;
    private  DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private  Toolbar toolbar;
    private ArrayList<String> list_title;
    private static Object[] imageResId = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };
    private TabLayout tabCustomTab_FindFragment_title;
    private ArrayList<Fragment> list_fragments ;
    private CustomViewPager viewPager;
    private MyFFragmentPagerAdapter fragmentPagerAdapter;

    public CustomViewPager getViewPager(){
        return viewPager;
    }

    private void  setUpAppBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*//使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("CollapsingToolbarLayout");
        mCollapsingToolbarLayout.setActivated(false);
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_drawer_layout);
      /*  Button button_test_tab_layout_activity= (Button) findViewById(R.id.button_test_tab_layout_activity);
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
        list_title.add("人脸接口");
        list_title.add("上传图片列表");
        list_title.add("上传详情");

        list_fragments = new ArrayList<Fragment>();

        //list_fragments.add(MDWidgetFragment.newInstance());

        //list_fragments.add(RecycleViewFragment.newInstance());
        list_fragments.add(FileUploadFragment.newInstance());
        list_fragments.add(UploadFaceListFragment.newInstance());
        //list_fragments.add(ChildViewPagerFragment.newInstance());
        list_fragments.add(UploadDetailFragment.newInstance());

        tabCustomTab_FindFragment_title=(TabLayout)findViewById(R.id.tabCustomTab_FindFragment_title);

        fragmentPagerAdapter=new MyFFragmentPagerAdapter(getSupportFragmentManager());
        viewPager= (CustomViewPager) findViewById(R.id.vp_FindFragment_pager);
        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Log2.i(TAG,"ViewPager--onPageSelected position:"+position);
                try {
                    if (position != 2) {
                        UploadDetailFragment uploadDetailFragment = (UploadDetailFragment) list_fragments.get(2);

                        uploadDetailFragment.setDefaultState();
                        imageResId[2] = R.mipmap.ic_launcher;
                        list_title.set(2, "上传详情");
                        initTabView();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       /* tabCustomTab_FindFragment_title.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                imageResId[2]= R.mipmap.ic_delete;
                list_title.set(2,"上传详情1");
                initTabView();
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                imageResId[2]= R.mipmap.ic_action_favorite;
                list_title.set(2,"上传详情2");
                initTabView();
            }
        });*/




        tabCustomTab_FindFragment_title.setupWithViewPager(viewPager);
        initTabView();
        //viewPager.setCurrentItem(1);
    }
    private void selectPage(View tabView){
        int pos = (int) tabView.getTag();
        boolean select=true;
        for (Fragment fragment :list_fragments){
            if (fragment instanceof PageStateListener) {
                if(((PageStateListener) fragment).isExecuting()){
                    select=false;
                    break;
                }
            }
        }
        if (select){
            TabLayout.Tab tab = tabCustomTab_FindFragment_title.getTabAt(pos);
            if (tab != null) {
                tab.select();
            }
        }
    }
    private void initTabView(){
        for (int i=0;i<tabCustomTab_FindFragment_title.getTabCount();i++){
            TabLayout.Tab tab = tabCustomTab_FindFragment_title.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(null);
                tab.setCustomView(fragmentPagerAdapter.getTabView(i));
                if (tab.getCustomView() != null) {
                    View tabView = (View) tab.getCustomView().getParent();
                    tabView.setTag(i);
                    tabView.setOnClickListener(mTabOnClickListener);
                }
            }
        }
    }


    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectPage(view);
        }
    };
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




    @Override
    public void onItemEdited() {
        list_title.set(2,"上传详情");
        imageResId[2]= R.mipmap.ic_launcher;
        initTabView();
        UploadFaceListFragment listFragment= (UploadFaceListFragment) list_fragments.get(1);
        listFragment.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemDeleted(UploadFace uploadFace,boolean deleteSuccess) {
        if(deleteSuccess){
            imageResId[2]= R.mipmap.ic_launcher;
            initTabView();
            View tabView= (View) tabCustomTab_FindFragment_title.getTabAt(1).getCustomView().getParent();
            selectPage(tabView);
            final UploadFaceListFragment listFragment= (UploadFaceListFragment) list_fragments.get(1);
            listFragment.getAdapter().remove(uploadFace);
            listFragment.getAdapter().notifyDataSetChanged();
            if(listFragment.getAdapter().getUploadFaces().size()==0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listFragment.fetchUploadFaces(new PageInfo(1,Constant.PageSize,true));
                    }
                }, 500);
            }
        }
    }
    @Override
    public void onItemLoaded(UploadFace uploadFace) {
        //标题显示item的 信息
        ImageRequest imageRequest = new ImageRequest(
                Constant.UploadFaceThumbnailDirUrl+uploadFace.getThumbnailImgFileName(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageResId[2]= response;
                        list_title.set(2,"编辑");
                        initTabView();
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyController.getInstance(MainActivity.this).cancelAll("getTabImage");
            }
        });
        imageRequest.setTag("getTabImage");
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//还是会重复提交
        VolleyController.getInstance(this).getRequestQueue().add(imageRequest);
    }
    @Override
    public void onListViewItemClick(UploadFaceListFragment fragment, int position) {
        UploadDetailFragment detailFragment = (UploadDetailFragment) list_fragments.get(2);
        if (detailFragment instanceof PageStateListener && !((PageStateListener) detailFragment).isExecuting()) {
            viewPager.setCurrentItem(2, true);
            UploadFaceAdapter uploadFaceAdapter = (UploadFaceAdapter) fragment.getAdapter();
            ArrayList<UploadFace> uploadFaces = uploadFaceAdapter.getUploadFaces();
            UploadFace uploadFace = uploadFaces.get(position);
            detailFragment.showCurrentItem(uploadFaceAdapter, uploadFace);
        }
    }
    class  MyFFragmentPagerAdapter extends  FragmentPagerAdapter{
        public MyFFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
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
            ImageView img = (ImageView) view.findViewById(imageView);
            if (imageResId[position] instanceof  Integer){
                img.setImageResource((Integer) imageResId[position]);
            }else if (imageResId[position] instanceof  Bitmap){
                img.setImageBitmap((Bitmap) imageResId[position] );
            }
            return view;
        }
    }

}
