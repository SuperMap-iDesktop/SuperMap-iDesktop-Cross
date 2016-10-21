package com.supermap.desktop.Interface;

import com.supermap.data.conversion.ImportSetting;

/**
 * Created by xie on 2016/10/14.
 * importsetting制造工厂
 */
public interface IImportSettingFactory {
    /**
     * 根据文件路径实例化ImportSetting
     *
     * @param filePath
     * @return
     */
    ImportSetting createImportSetting(String filePath, String fileFilter);
}
