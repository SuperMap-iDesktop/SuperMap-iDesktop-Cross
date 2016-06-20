package com.supermap.desktop.CtrlAction.TextStyle;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.CtrlAction.CtrlActionEditorBase;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.ui.controls.TextStyleDialog;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Selection;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;

public class CtrlActionTextStyle extends CtrlActionEditorBase {

	IEditor editor = new TextStyleEditor();

	public CtrlActionTextStyle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		EditEnvironment environment = EditEnvironment.createInstance((IFormMap) Application.getActiveApplication().getActiveForm());
		if (ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT)) {
			final TextStyleDialog dialog = TextStyleDialog.createInstance();
			if (null != getActiveRecordset()) {
				dialog.showDialog(getActiveRecordset());
			}
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().addGeometrySelectChangedListener(
					new GeometrySelectChangedListener() {

						@Override
						public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
							if (null != dialog && null != getActiveRecordset()) {
								dialog.showDialog(getActiveRecordset());
							} else if (null == getActiveRecordset()) {
								((JPanel) dialog.getContentPane()).removeAll();
								((JPanel) dialog.getContentPane()).updateUI();
							}
						}
					});
		}
	}

	public IEditor getEditor() {
		return this.editor;
	}

	public List<Geometry> getGeometryTexts() {
		// 获取geoText对象数组
		List<Geometry> result = new ArrayList<Geometry>();
		Recordset recordset = getActiveRecordset();
		recordset.moveFirst();
		while (!recordset.isEOF()) {
			result.add((GeoText) recordset.getGeometry());
			recordset.moveNext();
		}
		return result;
	}

	private Recordset getActiveRecordset() {
		Recordset recordset = null;
		if (MapUtilities.getActiveMap().findSelection(true).length > 0) {
			Selection selection = MapUtilities.getActiveMap().findSelection(true)[0];
			recordset = selection.toRecordset();
		}
		return recordset;
	}
}
