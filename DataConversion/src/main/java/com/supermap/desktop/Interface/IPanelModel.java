package com.supermap.desktop.Interface;

/**
 * Created by xie on 2016/9/29.
 * 界面模板接口,所有的界面创建是都必须按照此接口规范创建，
 * 统一了标准，但是接口内的某些方法不会被其他类
 * 调用，此处只是为了为实现它的类有统一的方法
 */
public interface IPanelModel {
    /**
     * 统一的控件初始化方法
     */
    void initComponents();

    /**
     * 统一的界面布局方法
     */
    void initLayerout();

    /**
     * 统一的控件事件注册方法
     */
    void registEvents();

    /**
     * 统一的控件事件销毁方法
     */
    void removeEvents();

}
