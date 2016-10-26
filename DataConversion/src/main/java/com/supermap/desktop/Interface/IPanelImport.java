package com.supermap.desktop.Interface;

import com.supermap.desktop.iml.ImportInfo;

/**
 * Created by xie on 2016/9/30.
 * 导入设置界面接口
 */
public interface IPanelImport extends IPanelModel {
    /**
     * 获取导入信息ImportInfo
     *
     * @return
     */
    ImportInfo getImportInfo();

    /**
     * 自定义dispose方法
     */
    void dispose();

    /**
     * 获取导入界面类型
     *
     * @return
     */
    int getPanelImportType();
}
