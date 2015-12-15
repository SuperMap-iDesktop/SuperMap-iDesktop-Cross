package com.supermap.desktop.CtrlAction;

import javax.swing.JFrame;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;

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
				String[] datasetType = new String[] { CommonProperties.getString("String_DatasetType_All"),
						CommonProperties.getString("String_DatasetType_Point"), CommonProperties.getString("String_DatasetType_Line"),
						CommonProperties.getString("String_DatasetType_Region"), CommonProperties.getString("String_DatasetType_Text"),
						CommonProperties.getString("String_DatasetType_CAD"), CommonProperties.getString("String_DatasetType_Network"),
						CommonProperties.getString("String_DatasetType_LineM"), CommonProperties.getString("String_DatasetType_Grid"),
						CommonProperties.getString("String_DatasetType_Image"), CommonProperties.getString("String_DatasetType_Point3D"),
						CommonProperties.getString("String_DatasetType_Line3D"), CommonProperties.getString("String_DatasetType_Region3D"),
						// CommonProperties.getString("String_DatasetType_Topology"),
						CommonProperties.getString("String_DatasetType_GridCollection"), CommonProperties.getString("String_DatasetType_ImageCollection"),
						CommonProperties.getString("String_DatasetType_ParametricLine"), CommonProperties.getString("String_DatasetType_ParametricRegion"),
				// CommonProperties.getString("String_DatasetType_WCS"),CommonProperties.getString("String_DatasetType_WMS")
				};
				DatasetChooserNewMap datasetChooser = new DatasetChooserNewMap(frame, formMap, datasetType);
				datasetChooser.setVisible(true);
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
