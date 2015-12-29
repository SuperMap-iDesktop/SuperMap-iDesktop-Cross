package com.supermap.desktop.popupmenus;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.DataExportFrame;

import javax.swing.*;

public class CtrlActionDataExport extends CtrlAction {


	public static EngineType[] UN_SUPPORT_TYPE = new EngineType[]{EngineType.OGC, EngineType.ISERVERREST,
			EngineType.SUPERMAPCLOUD, EngineType.GOOGLEMAPS, EngineType.BAIDUMAPS, EngineType.OPENSTREETMAPS, EngineType.MAPWORLD};

	public CtrlActionDataExport(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
				Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
				DataExportFrame dataImportFrame = new DataExportFrame(datasets, parent, true);
				dataImportFrame.setVisible(true);
			}
		});
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
		for (Datasource activeDatasource : activeDatasources) {
			if (isSupportEngineType(activeDatasource.getEngineType())) {
				enable = true;
				break;
			}
		}
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (activeDatasources.length <= 0 && datasources.getCount() > 0) {
			// 从上面选的时候在给一次机会
			for (int i = 0; i < datasources.getCount(); i++) {
				if (isSupportEngineType(datasources.get(i).getEngineType())) {
					enable = true;
					break;
				}
			}
		}
		return enable;
	}

	public static boolean isSupportEngineType(EngineType engineType) {
		boolean result = true;

		for (EngineType type : UN_SUPPORT_TYPE) {
			if (engineType == type) {
				result = false;
				break;
			}
		}
		return result;
	}
}
