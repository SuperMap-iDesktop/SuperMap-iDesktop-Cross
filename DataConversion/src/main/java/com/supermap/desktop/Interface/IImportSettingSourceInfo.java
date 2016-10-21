package com.supermap.desktop.Interface;


import com.supermap.data.Charset;

/**
 * Created by xie on 2016/9/29.
 * 源文件信息接口
 */
public interface IImportSettingSourceInfo extends IPanelModel {
    /**
     * 获取字符集类型
     *
     * @return
     */
    Charset getCharset();

    /**
     * 实例化类时是否需要创建字符集控件
     *
     * @param visible
     */
    void needCharset(boolean visible);

//    /**
//     * 获取ImportSetting
//     *
//     * @return
//     */
//    ImportSetting getImportSetting();
}
