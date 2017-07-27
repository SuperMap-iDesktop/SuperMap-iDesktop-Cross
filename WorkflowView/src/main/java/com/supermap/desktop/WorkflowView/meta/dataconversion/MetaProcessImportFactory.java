package com.supermap.desktop.WorkflowView.meta.dataconversion;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessImport;

/**
 * Created by xie on 2017/4/10.
 */
public class MetaProcessImportFactory {
    public static MetaProcessImport createMetaProcessImport(String importType) {
        IImportSettingCreator importSettingCreator = new ImportSettingCreator();
        ImportSetting importSetting = importSettingCreator.create(importType);
        return new MetaProcessImport(importSetting,importType);
    }
}
