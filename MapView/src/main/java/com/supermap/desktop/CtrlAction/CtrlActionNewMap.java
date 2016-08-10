package com.supermap.desktop.CtrlAction;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;

import javax.swing.*;

public class CtrlActionNewMap extends CtrlAction {

	public CtrlActionNewMap(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {

			IFormMap formMap = null;
			if ((Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap)
					&& (((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap().getLayers() == null || ((IFormMap) Application
							.getActiveApplication().getActiveForm()).getMapControl().getMap().getLayers().getCount() <= 0)) {

				formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			}

			if (formMap == null) {
				formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP);
			}
			if (formMap != null) {
				JFrame frame = (JFrame) Application.getActiveApplication().getMainFrame();
				DatasetType[] datasetTypes = new DatasetType[]{
						DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.TEXT, DatasetType.CAD, DatasetType.NETWORK,
						DatasetType.LINEM, DatasetType.GRID, DatasetType.IMAGE, DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D,
						DatasetType.GRIDCOLLECTION, DatasetType.IMAGECOLLECTION, DatasetType.PARAMETRICLINE, DatasetType.PARAMETRICREGION,
						DatasetType.NETWORK3D
				};
				DatasetChooserNewMap datasetChooser = new DatasetChooserNewMap(frame, formMap, datasetTypes);
				datasetChooser = null;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {

		boolean enable = false;

		try {
			if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}
}
