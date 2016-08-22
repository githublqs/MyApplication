package com.example.administrator.myapplication.fileupload;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mobsandgeeks.saripaar.Validator;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/21.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity implements Validator.ValidationListener{
    protected Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }
   @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
       injectViews(this);
    }
    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     */
    protected void injectViews(@NonNull Activity target) {
        ButterKnife.bind(target);
    }
    protected void showSnackbar(@NonNull CharSequence text, View view) {
        final Snackbar snackbar = Snackbar.make(
                view,
                text,
                Snackbar.LENGTH_SHORT);

        snackbar.setAction("隐藏",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
        snackbar.show();
    }

}
