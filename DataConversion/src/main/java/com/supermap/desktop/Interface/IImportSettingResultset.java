package com.supermap.desktop.Interface;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.conversion.ImportMode;

/**
 * Created by xie on 2016/9/29.
 * 结果设置接口
 */
public interface IImportSettingResultset extends IPanelModel {

    /**
     * 获取目标数据源
     *
     * @return
     */
    Datasource getTargetDatasouce();

    /**
     * 获取数据集名称
     *
     * @return
     */
    String getTargetDatasetName();

    /**
     * 获取编码类型
     *
     * @return
     */
    EncodeType getEncodeType();

    /**
     * 获取导入模式，
     *
     * @return
     */
    ImportMode getImportMode();

    /**
     * 获取数据集类型，非父类方法
     * 只有特殊的数据类型导入时需要设置（tab等）
     *
     * @return
     */
    DatasetType getDatasetType();

    /**
     * 是否创建字段索引，非父类方法
     * 只有特殊的数据类型导入后为矢量数据集
     * 此时才需要创建字段索引（tab等）
     *
     * @return
     */
    boolean getFieldIndex();

    /**
     * 是否创建空间索引，非父类方法
     * 只有特殊的数据类型导入后为矢量数据集
     * 此时才需要创建字段索引（tab等）
     *
     * @return
     */
    boolean getSpatialIndex();

    /**
     * 实例化类时是否需要编码类型控件
     *
     * @param visible
     */
    void needEncodeType(boolean visible);

    /**
     * 实例化类时是否需要数据集类型控件
     *
     * @param visible
     */
    void needDatasetType(boolean visible);

    /**
     * 实例化类时是否需要创建字段索引控件
     *
     * @param visible
     */
    void needFieldIndex(boolean visible);

    /**
     * 实例化类时是否需要创建空间索引控件
     *
     * @param visible
     */
    void needSpatialIndex(boolean visible);

//    /**
//     * 获取ImportSetting
//     *
//     * @return
//     */
//    ImportSetting getImportSetting();
}
