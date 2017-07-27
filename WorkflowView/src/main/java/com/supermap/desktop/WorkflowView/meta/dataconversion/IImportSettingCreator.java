package com.supermap.desktop.WorkflowView.meta.dataconversion;

import com.supermap.data.conversion.ImportSetting;

/**
 * Created by xie on 2017/3/31.
 */
public interface IImportSettingCreator<I> {
    /**
     * Use path or other type info to get a ImportSetting instance
     *
     * @param i
     * @return
     */
    ImportSetting create(I i);
}
