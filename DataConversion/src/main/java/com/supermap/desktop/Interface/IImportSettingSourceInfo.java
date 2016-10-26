package com.supermap.desktop.Interface;


import com.supermap.desktop.ui.controls.CharsetComboBox;

/**
 * Created by xie on 2016/9/29.
 * 源文件信息接口
 */
public interface IImportSettingSourceInfo extends IPanelModel {
    /**
     * 获取字符集类型控件
     *
     * @return
     */
    CharsetComboBox getComboBoxCharset();

}
