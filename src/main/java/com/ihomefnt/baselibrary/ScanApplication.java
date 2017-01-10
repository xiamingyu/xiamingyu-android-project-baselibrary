package com.ihomefnt.baselibrary;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by XiaMingYu on 2016/10/27.
 */
public class ScanApplication extends Application {
    public static ScanApplication mApplication;
    private RequestQueue mQueue;

    public ScanApplication() {
        super();
        mApplication = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public RequestQueue getRequestQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mQueue;
    }

    public static ScanApplication getInstance() {
        return mApplication;
    }

}
