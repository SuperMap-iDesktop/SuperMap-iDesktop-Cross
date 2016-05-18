package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.SmoothEditor;

public class CtrlActionSmooth extends CtrlActionEditorBase {

	private SmoothEditor editor = new SmoothEditor();

	public CtrlActionSmooth(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	public IEditor getEditor() {
		return this.editor;
	}
}
