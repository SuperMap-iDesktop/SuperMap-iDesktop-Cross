package com.supermap.desktop.Interface;

import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.exportUI.DataExportDialog;
import com.supermap.desktop.iml.ExportFileInfo;

import java.util.ArrayList;

/**
 * Created by xie on 2016/10/31.
 */
public interface IExportPanelFactory {
    PanelExportTransform createExportPanel(DataExportDialog owner, ExportFileInfo exportsFileInfo);

    PanelExportTransform createExportPanel(DataExportDialog owner, ArrayList<PanelExportTransform> panelExports);
}
