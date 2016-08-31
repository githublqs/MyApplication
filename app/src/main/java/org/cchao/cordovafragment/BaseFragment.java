package org.cchao.cordovafragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.administrator.myapplication.R;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.PluginEntry;
import org.crosswalk.engine.XWalkWebViewEngine;
import org.xwalk.core.XWalkView;
//https://github.com/crosswalk-project/cordova-plugin-crosswalk-webview
//http://blog.csdn.net/xuexiiphone/article/details/52170117
//ionic cli 2.0
//ionic browser add crosswalk 不能执行
//cordova-android-crosswalk
//http://stackoverflow.com/questions/23069156/crosswalk-cordova-does-not-build
import java.util.ArrayList;

/**
 * Created by chenchao on 15/12/2.
 */
public abstract class BaseFragment extends Fragment {

    private CordovaWebView webView;

    protected CordovaPreferences preferences;
    protected String launchUrl;
    protected ArrayList<PluginEntry> pluginEntries;
    protected CordovaInterfaceImpl cordovaInterface;


    protected void loadConfig() {
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(getActivity());
        preferences = parser.getPreferences();
        preferences.setPreferencesBundle(getActivity().getIntent().getExtras());
        launchUrl = parser.getLaunchUrl();
        pluginEntries = parser.getPluginEntries();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_fragment_cordova, container, false);
        cordovaInterface =  new CordovaInterfaceImpl(getActivity());
        if(savedInstanceState != null)
            cordovaInterface.restoreInstanceState(savedInstanceState);

        loadConfig();
        webView = new CordovaWebViewImpl(CordovaWebViewImpl.createEngine(getActivity(), preferences));
        //webView = new CordovaWebViewImpl(new XWalkWebViewEngine(getActivity(),preferences));
        RelativeLayout.LayoutParams wvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        webView.getView().setLayoutParams(wvlp);
        if (!webView.isInitialized()) {
            webView.init(cordovaInterface, pluginEntries, preferences);
        }
        cordovaInterface.onCordovaInit(webView.getPluginManager());
        ((RelativeLayout)rootView).addView(webView.getView());
        webView.loadUrl(getHtmlUri());
        return rootView;
    }

    @Override
    public void onDestroy() {
        webView.handleDestroy();
        super.onDestroy();
    }

    abstract protected String getHtmlUri();
}
