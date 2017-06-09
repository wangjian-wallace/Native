package com.wallace.tools;

import android.app.Application;

/**
 * Package com.wallace.tools
 * Created by Wallace.
 * on 2017/6/6.
 */

public class MyApp extends Application{

//    protected String userAgent;

    private static MyApp instance = null;

    public static MyApp getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        userAgent = Util.getUserAgent(this, "WallaceApp");
    }

//    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
//        return new DefaultDataSourceFactory(this, bandwidthMeter,
//                buildHttpDataSourceFactory(bandwidthMeter));
//    }
//
//    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
//        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
//    }
//
//    public boolean useExtensionRenderers() {
//        return BuildConfig.FLAVOR.equals("withExtensions");
//    }

}
