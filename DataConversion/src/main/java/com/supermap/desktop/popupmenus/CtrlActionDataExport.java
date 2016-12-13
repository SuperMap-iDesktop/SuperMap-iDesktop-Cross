package com.supermap.desktop.popupmenus;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.exportUI.DataExportDialog;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.DatasourceUtilities;

public class CtrlActionDataExport extends CtrlAction {


    public static EngineType[] UN_SUPPORT_TYPE = new EngineType[]{EngineType.OGC, EngineType.ISERVERREST,
		    EngineType.SUPERMAPCLOUD, EngineType.GOOGLEMAPS, EngineType.BAIDUMAPS, EngineType.OPENSTREETMAPS};

    public CtrlActionDataExport(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
        DataExportDialog dataExportDialog = new DataExportDialog(datasets);
        dataExportDialog.setVisible(true);
    }

    @Override
    public boolean enable() {
        boolean enable = false;
        Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
        for (Datasource activeDatasource : activeDatasources) {
            if (!DatasourceUtilities.isWebDatasource(activeDatasource.getEngineType())) {
                enable = true;
                break;
            }
        }
        Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
        if (activeDatasources.length <= 0 && datasources.getCount() > 0) {
            // 从上面选的时候在给一次机会
            for (int i = 0; i < datasources.getCount(); i++) {
                if (!DatasourceUtilities.isWebDatasource(datasources.get(i).getEngineType())) {
                    enable = true;
                    break;
                }
            }
        }
        return enable;
    }

}
