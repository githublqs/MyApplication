package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/13.
 */

public class MDWidgetFragment extends  Fragment implements View.OnClickListener {
    private ArrayList<String> mData;


    //private TextInputLayout tetInput;
    //private EditText et_til_test;

    public static MDWidgetFragment newInstance() {
        Bundle args = new Bundle();

        MDWidgetFragment fragment = new MDWidgetFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mdwidget_layout,container,false);
        setScrollViewContent(view);
       /* view.findViewById(R.id.buttonSnackBar).setOnClickListener(this);
        tetInput= (TextInputLayout) view.findViewById(R.id.textInput);
        et_til_test=(EditText) view.findViewById(R.id.et_til_test);
        et_til_test.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>4){
                    tetInput.setErrorEnabled(true);
                    tetInput.setError("长度大于4了");
                }else {
                    tetInput.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(0);


    }

    @Override
    public void onClick(View view) {
       /* switch (view.getId()){
            case  R.id.buttonSnackBar:
                final Snackbar snackbar=Snackbar.make(view,"SANCKBAR", Snackbar.LENGTH_SHORT);
                snackbar.setAction("click me", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                break;
        }*/
    }
    private void initData(int pager) {
        mData = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            mData.add("pager" + pager + " 第" + i + "个item");
        }
    }
    /**
     * 刷新ScrollView的内容
     */
    private void setScrollViewContent(View view) {
        //NestedScrollView下的LinearLayout
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_sc_content);
        layout.removeAllViews();
        for (int i = 0; i < mData.size(); i++) {
            View viewItem = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
            ((TextView) viewItem.findViewById(android.R.id.text1)).setText(mData.get(i));
            //动态添加 子View
            layout.addView(viewItem, i);
        }
    }
}