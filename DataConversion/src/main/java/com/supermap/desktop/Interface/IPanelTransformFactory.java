package com.supermap.desktop.Interface;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.importUI.PanelImport;

import java.util.ArrayList;

/**
 * Created by xie on 2016/10/13.
 * 转换参数工厂接口
 */
public interface IPanelTransformFactory {
    /**
     * 创建转换参数界面
     *
     * @param importSetting
     * @return
     */
    IImportSetttingTransform createPanelTransform(ImportSetting importSetting);

    IImportSetttingTransform createPanelTransform(ArrayList<PanelImport> panelImports);

    int getImportSettingsType(ArrayList<PanelImport> panelImports);

}
