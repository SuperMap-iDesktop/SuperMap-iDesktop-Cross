package com.supermap.desktop.Interface;

import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;

/**
 * Created by xie on 2016/9/29.
 * 结果设置接口
 */
public interface IImportSettingResultset extends IPanelModel {

    /**
     * 获取目标数据源控件
     *
     * @return
     */
    DatasourceComboBox getComboBoxDatasource();
    /**
     * 获取数据集名称控件
     *
     * @return
     */
    JTextField getTextFieldDatasetName();
    /**
     * 获取编码类型控件
     *
     * @return
     */
    JComboBox getComboBoxEncodeType();
    /**
     * 获取导入模式控件
     *
     * @return
     */
    JComboBox getComboBoxImportMode();
    /**
     * 获取数据集类型控件，非父类方法
     * 只有特殊的数据类型导入时需要设置（tab等）
     *
     * @return
     */
    DatasetComboBox getComboBoxDatasetType();
    /**
     * 是否创建字段索引控件，非父类方法
     * 只有特殊的数据类型导入后为矢量数据集
     * 此时才需要创建字段索引（tab等）
     *
     * @return
     */
    JCheckBox getCheckBoxFieldIndex();
    /**
     * 是否创建空间索引控件，非父类方法
     * 只有特殊的数据类型导入后为矢量数据集
     * 此时才需要创建字段索引（tab等）
     *
     * @return
     */
    JCheckBox getCheckBoxSpatialIndex();

}
