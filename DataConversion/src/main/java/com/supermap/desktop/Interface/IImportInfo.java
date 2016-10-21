package com.supermap.desktop.Interface;

import com.supermap.data.conversion.ImportSetting;

/**
 * Created by xie on 2016/10/12.
 * 自己封装的导入类，其中包括导入的主类（ImportSetting）
 * 以及需要导入后处理的信息
 */
public interface IImportInfo {
    //
    //importsetting;
    //state;
    //isSpatialIndex;
    //isFieldIndex;
    //isSplitMore;
    //fileName;
    //fileType;

    /**
     * 设置导入文件名
     *
     * @param fileName
     */
    void setFileName(String fileName);

    /**
     * 获取导入文件名
     *
     * @return
     */
    String getFileName();

    /**
     * 设置导入文件的中文对应类型
     *
     * @param filetype
     */
    void setFiletype(String filetype);

    /**
     * 获取导入文件的中文对应类型
     *
     * @return
     */
    String getFileType();

    /**
     * 设置导入类
     *
     * @param importSetting
     */
    void setImportSetting(ImportSetting importSetting);

    /**
     * 获取导入类
     *
     * @return
     */
    ImportSetting getImportSetting();

    /**
     * 设置状态信息
     *
     * @param state
     */
    void setState(String state);

    /**
     * 获取状态
     *
     * @return
     */
    String getState();

    /**
     * 设置字段查询
     *
     * @param isFieldIndex
     */
    void setFieldIndex(boolean isFieldIndex);

    /**
     * 获取是否需要进行字段查询
     *
     * @param
     */
    boolean getFieldIndex();

    /**
     * 设置空间查询
     *
     * @param isSpatialIndex
     */
    void setSpatialIndex(boolean isSpatialIndex);

    /**
     * 获取是否要进行空间查询
     *
     * @return
     */
    boolean getSpatialIndex();

    /**
     * 设置是否需要将模型数据拆分为多个小对象
     *
     * @param isSplitMore
     */
    void setSplitMore(boolean isSplitMore);

    /**
     * 获取是否需要将模型数据拆分为多个小对象
     *
     * @param
     */
    boolean getSplitMore();
}
