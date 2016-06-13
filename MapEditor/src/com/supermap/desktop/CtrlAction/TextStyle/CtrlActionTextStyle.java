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
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.*;

public class CtrlActionTextStyle extends CtrlActionEditorBase {

	IEditor editor = new TextStyleEditor();

	public CtrlActionTextStyle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		EditEnvironment environment = EditEnvironment.createInstance((IFormMap) Application.getActiveApplication().getActiveForm());
		if (ListUtilties.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT)) {
			final TextStyleDialog dialog = new TextStyleDialog();
			if (null!=getActiveRecordset()) {
				dialog.showDialog(getActiveRecordset(),getGeometryTexts());
			}
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
			result.add((GeoText)recordset.getGeometry());
			recordset.moveNext();
		}
		return result;
	}

	private Recordset getActiveRecordset() {
		Recordset recordset = null;
		if ( MapUtilties.getActiveMap().findSelection(true).length>0) {
			Selection selection = MapUtilties.getActiveMap().findSelection(true)[0];
			recordset = selection.toRecordset();
		}
		return recordset;
	}
}
