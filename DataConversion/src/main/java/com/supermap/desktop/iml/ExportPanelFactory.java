package com.supermap.desktop.iml;

import com.supermap.data.conversion.FileType;
import com.supermap.desktop.Interface.IExportPanelFactory;
import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.exportUI.DataExportDialog;
import com.supermap.desktop.exportUI.PanelExportTransformForGrid;
import com.supermap.desktop.exportUI.PanelExportTransformForVector;
import com.supermap.desktop.localUtilities.FiletypeUtilities;

import java.util.ArrayList;

/**
 * Created by xie on 2016/10/31.
 */
public class ExportPanelFactory implements IExportPanelFactory {
    public static final int SAMETYPE = 0;
    public static final int GRIDTYPE = 1;
    public static final int VECTORTYPE = 2;
    public static final int GRID_AND_VECTORTYPE = 3;

    @Override
    public PanelExportTransform createExportPanel(DataExportDialog owner, ExportFileInfo exportsFileInfo) {
        PanelExportTransform result = new PanelExportTransform(exportsFileInfo);
        FileType fileType = exportsFileInfo.getFileType();
        if (fileType.equals(FileType.CSV)) {
            return new PanelExportTransformForVector(exportsFileInfo);
        }
        if (FiletypeUtilities.isGridType(fileType)) {
            result = new PanelExportTransformForGrid(owner, exportsFileInfo);
        } else if (FiletypeUtilities.isVectorType(fileType)) {
            result = new PanelExportTransformForVector(exportsFileInfo);
        }
        return result;
    }

    @Override
    public PanelExportTransform createExportPanel(DataExportDialog owner, ArrayList<PanelExportTransform> panelExports) {
        PanelExportTransform exportPanel = null;
        if (isSameType(panelExports)) {
            ExportFileInfo fileInfo = panelExports.get(0).getExportsFileInfo();
            if (FiletypeUtilities.isGridType(fileInfo.getFileType()) && fileInfo.getFileType() != FileType.CSV) {
                exportPanel = new PanelExportTransformForGrid(owner, panelExports, SAMETYPE);
            } else {
                exportPanel = new PanelExportTransformForVector(panelExports, SAMETYPE);
            }
        } else if (isGridTypes(panelExports)) {
            exportPanel = new PanelExportTransformForVector(panelExports, GRIDTYPE);
        } else if (isVectorTypes(panelExports)) {
            exportPanel = new PanelExportTransformForVector(panelExports, VECTORTYPE);
        } else {
            exportPanel = new PanelExportTransformForVector(panelExports, GRID_AND_VECTORTYPE);
        }
        return exportPanel;
    }


    private boolean isSameType(ArrayList<PanelExportTransform> panelExports) {
        boolean isSameType = true;
        ExportFileInfo exportsFileInfo = panelExports.get(0).getExportsFileInfo();
        int size = panelExports.size();
        for (int i = 0; i < size; i++) {
            if (!exportsFileInfo.getFileType().equals(panelExports.get(i).getExportsFileInfo().getFileType())) {
                isSameType = false;
            }
        }
        return isSameType;
    }

    private boolean isVectorTypes(ArrayList<PanelExportTransform> panelExports) {
        int count = 0;
        for (PanelExportTransform tempPanelExport : panelExports) {
            for (FileType tempFileType : FiletypeUtilities.getVectorValue()) {
                if (tempPanelExport.getExportsFileInfo().getFileType().equals(tempFileType)) {
                    count++;
                }
            }
        }

        return count == panelExports.size();
    }

    public boolean isGridTypes(ArrayList<PanelExportTransform> panelExports) {
        int count = 0;
        for (PanelExportTransform tempPanelExport : panelExports) {
            for (FileType tempFileType : FiletypeUtilities.getGridValue()) {
                if (tempPanelExport.getExportsFileInfo().getFileType().equals(tempFileType)) {
                    count++;
                }
            }
        }

        return count == panelExports.size();
    }
}
