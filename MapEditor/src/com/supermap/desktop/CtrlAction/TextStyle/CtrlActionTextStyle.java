package com.supermap.desktop.CtrlAction.TextStyle;

import java.util.*;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.CtrlAction.CtrlActionEditorBase;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.ui.controls.TextStyleDialog;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.mapping.*;

public class CtrlActionTextStyle extends CtrlActionEditorBase {

	IEditor editor = new TextStyleEditor();

	public CtrlActionTextStyle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		List<Object> geometryTexts = getGeometryTexts();
		GeoText geoText = null;
		EditEnvironment environment = EditEnvironment.createInstance((IFormMap) Application.getActiveApplication().getActiveForm());
		if (ListUtilties.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT)) {
			geoText = ((GeoText) getGeometryTexts().get(geometryTexts.size() - 1));
			GeoText newGeoText = TextStyleDialog.showDialog(geoText, false, false);
			if (null != newGeoText) {
				Recordset recordset = getActiveRecordset();
				recordset.moveFirst();
				while (!recordset.isEOF()) {
					recordset.edit();
					recordset.setGeometry(newGeoText);
					recordset.update();
					recordset.moveNext();
				}
				environment.getMap().refresh();
			}
		}
		GeoText3D geoText3D = null;
		if (ListUtilties.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT3D)) {
			geoText3D = ((GeoText3D) getGeometryTexts().get(geometryTexts.size() - 1));
			TextStyle newTextStyle = TextStyleDialog.showDialog(geoText3D.getTextStyle(), false, false, geoText3D.getText());
			if (null != newTextStyle) {
				Recordset recordset = getActiveRecordset();
				recordset.moveFirst();
				while (!recordset.isEOF()) {
					Geometry temp = recordset.getGeometry().clone();
					((GeoText3D)temp).setTextStyle(newTextStyle);
					recordset.edit();
					recordset.setGeometry(temp);
					recordset.update();
					recordset.moveNext();
				}
				environment.getMap().refresh();
			}
			
		}

	}

	public IEditor getEditor() {
		return this.editor;
	}

	public List<Object> getGeometryTexts() {
		// 获取geoText对象数组
		List<Object> result = new ArrayList<Object>();
		Recordset recordset = getActiveRecordset();
		recordset.moveFirst();
		while (!recordset.isEOF()) {
			result.add((GeoText)recordset.getGeometry());
			recordset.moveNext();
		}
		return result;
	}

	private Recordset getActiveRecordset() {
		Layer activeLayer = ((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers()[0];
		Selection selection = activeLayer.getSelection();
		Recordset recordset = selection.toRecordset();
		return recordset;
	}
}
