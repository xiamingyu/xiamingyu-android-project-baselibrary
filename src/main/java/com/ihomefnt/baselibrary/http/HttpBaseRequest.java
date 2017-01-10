package com.ihomefnt.baselibrary.http;


import com.ihomefnt.baselibrary.ScanApplication;
import com.ihomefnt.baselibrary.baseutil.DeviceUtils;

/**
 * Created by shirely_geng on 15-2-1.
 */
public class HttpBaseRequest {
    private String deviceToken = DeviceUtils.getDeviceToken(ScanApplication.getInstance());
    private String osType = String.valueOf(0x02);     //0x01: ios; 0x02: android;
    private String appVersion = DeviceUtils.getAppVersion(ScanApplication.getInstance());
    private Integer width = DeviceUtils.getDisplayWidth(ScanApplication.getInstance());
    private String accessToken;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
