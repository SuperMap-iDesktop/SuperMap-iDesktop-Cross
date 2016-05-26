package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.GeometryCopyEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;

public class CtrlActionGeometryCopy extends CtrlActionEditorBase {

	private GeometryCopyEditor editor = new GeometryCopyEditor();

	public CtrlActionGeometryCopy(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
