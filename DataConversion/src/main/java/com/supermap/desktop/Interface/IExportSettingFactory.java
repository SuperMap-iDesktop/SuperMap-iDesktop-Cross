package com.supermap.desktop.Interface;

import com.supermap.data.conversion.ExportSetting;
import com.supermap.data.conversion.FileType;

/**
 * Created by xie on 2016/10/28.
 * 导出类型实例化工厂
 */
public interface IExportSettingFactory {
    /**
     * 根据可导出的文件类型设置初始的ExportSetting
     *
     * @param fileType
     * @return
     */
    ExportSetting createExportSetting(FileType fileType);
}
