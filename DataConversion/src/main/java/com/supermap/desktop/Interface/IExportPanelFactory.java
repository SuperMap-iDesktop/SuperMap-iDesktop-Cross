package com.supermap.desktop.Interface;

import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.iml.ExportFileInfo;

import java.util.ArrayList;

/**
 * Created by xie on 2016/10/31.
 */
public interface IExportPanelFactory {
    PanelExportTransform createExportPanel(ExportFileInfo exportsFileInfo);

    PanelExportTransform createExportPanel(ArrayList<PanelExportTransform> panelExports);
}
