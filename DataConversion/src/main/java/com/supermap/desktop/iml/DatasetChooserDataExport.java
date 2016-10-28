package com.supermap.desktop.iml;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.exportUI.DataExportDialog;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.utilities.DatasourceUtilities;

import javax.swing.*;
import java.util.List;

/**
 * Created by xie on 2016/10/28.
 * 导出数据集选择界面
 */
public class DatasetChooserDataExport {
    private final DatasetChooser datasetChooser;
    private MutiTable exportTable;
    private DataExportDialog owner;

    public DatasetChooserDataExport(JDialog owner, final MutiTable exportTable) {
        this.exportTable = exportTable;
        this.owner = (DataExportDialog) owner;
        datasetChooser = new DatasetChooser(owner) {
            @Override
            protected boolean isSupportDatasource(Datasource datasource) {
                return !DatasourceUtilities.isWebDatasource(datasource.getEngineType()) && super.isSupportDatasource(datasource);
            }
        };
        initSelectDatasource();
        if (datasetChooser.showDialog() == DialogResult.OK) {
            addInfoToMainTable();
        }
    }

    private void initSelectDatasource() {
        if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
            this.datasetChooser.getWorkspaceTree().setSelectedDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
        } else if (null != Application.getActiveApplication().getWorkspace().getDatasources()) {
            this.datasetChooser.getWorkspaceTree().setSelectedDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
        }
    }

    private void addInfoToMainTable() {
        try {
            List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();
            owner.addTableInfo(selectedDatasets.toArray(new Dataset[selectedDatasets.size()]));
            if (0 < exportTable.getRowCount()) {
                exportTable.setRowSelectionInterval(exportTable.getRowCount() - 1, exportTable.getRowCount() - 1);
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }
}
