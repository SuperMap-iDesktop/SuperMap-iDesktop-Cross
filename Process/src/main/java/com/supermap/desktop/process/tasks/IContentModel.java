package com.supermap.desktop.process.tasks;

/**
 * 控件显示接口，用于参数规范
 * Created by xie on 2016/11/26.
 */
public interface IContentModel {
    /**
     * 控件初始化
     */
    void initComponents();

    /**
     * 布局初始化
     */
    void initLayout();

    /**
     * 资源化
     */
    void initResouces();

    /**
     * 添加事件监听
     */
    void registEvents();

    /**
     * 移除事件监听
     */
    void removeEvents();
}
