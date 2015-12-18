package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.CursorUtilties;

import javax.swing.*;
import java.text.MessageFormat;

public class CtrlActionDeleteDataset extends CtrlAction {

    public CtrlActionDeleteDataset(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        try {
            boolean isDataset = false;
	        final Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
	        if (datasets != null && datasets.length > 0) {
                isDataset = true;
            }

            if (isDataset) {
                if (Application.getActiveApplication().getActiveDatasources()[0].isReadOnly()) {
                    UICommonToolkit.showErrorMessageDialog(DataEditorProperties.getString("String_DelectReadonlyDataset"));
                    return;
                }
                String message = "";
                if (datasets.length == 1) {
                    message = MessageFormat.format(DataEditorProperties.getString("String_DelectOneDataset"), Application.getActiveApplication()
                            .getActiveDatasources()[0].getAlias(), datasets[0].getName());
                } else {
                    message = MessageFormat.format(DataEditorProperties.getString("String_DelectManyDataset"), datasets.length);
                }
                if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(message)) {
                    try {
                        CursorUtilties.setWaitCursor();
                        CommonToolkit.DatasetWrap.CloseDataset(datasets);
	                    new Thread() {
		                    @Override
		                    public void run() {
			                    for (int i = 0; i < datasets.length; i++) {
				                    String resultInfo = MessageFormat.format(DataEditorProperties.getString("String_DelectDatasetSuccessfulInfo"), datasets[i]
						                    .getDatasource().getAlias(), datasets[i].getName());
				                    datasets[i].getDatasource().getDatasets().delete(datasets[i].getName());
				                    Application.getActiveApplication().getOutput().output(resultInfo);
			                    }
		                    }
	                    }.run();
                        Application.getActiveApplication().setActiveDatasets(null);
                    } finally {
                        CursorUtilties.setDefaultCursor();
                    }
                }
            } else {
                Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
                JFrame frame = (JFrame) Application.getActiveApplication().getMainFrame();
                DatasetChooserDataEditor dataSetChooser = new DatasetChooserDataEditor(frame, datasource, true);
                dataSetChooser.setVisible(true);
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    @Override
    public boolean enable() {
        boolean enable = false;
        Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();
        if (datasources != null && datasources.length > 0) {
            for (Datasource datasource : datasources) {
                if (null != datasource && !datasource.isReadOnly()) {
                    enable = true;
                    break;
                }
            }
            if (datasources[0].getDatasets().getCount() <= 0) {
                enable = false;
            }
        }
        Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
        if (datasets != null && datasets.length>0 &&datasets[0] instanceof DatasetVector && ((DatasetVector) datasets[0]).getParentDataset() != null) {
            enable = false;
        }
        return enable;
    }
}