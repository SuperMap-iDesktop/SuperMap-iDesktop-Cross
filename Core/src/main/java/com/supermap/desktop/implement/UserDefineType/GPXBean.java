package com.supermap.desktop.implement.UserDefineType;

/**
 * Created by xie on 2017/3/28.
 * Bean for gpx(GPS file) import
 */
public class GPXBean {
    private double lat = 0.0d;//纬度 y
    private double lon = 0.0d;//经度 x
    private float ele = 0.0f;//海拔
    private String time = null;//时间
    /**
     * 获取纬度
     * @return
     */
    public double getLat() {
        return lat;
    }
    /**
     * 设置纬度
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }
    /**
     * 获取经度
     * @return
     */
    public double getLon() {
        return lon;
    }
    /**
     * 设置经度
     * @param lon
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
    /**
     * 获取海拔
     * @return
     */
    public float getEle() {
        return ele;
    }
    /**
     * 设置海拔
     * @param ele
     */
    public void setEle(float ele) {
        this.ele = ele;
    }
    /**
     * 获取时间
     * @return
     */
    public String getTime() {
        return time;
    }
    /**
     * 设置时间
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }
}
