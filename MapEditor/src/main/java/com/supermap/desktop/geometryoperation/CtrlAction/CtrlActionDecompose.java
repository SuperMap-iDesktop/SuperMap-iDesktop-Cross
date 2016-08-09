package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.DecomposeEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;

public class CtrlActionDecompose extends CtrlActionEditorBase {

	private DecomposeEditor editor = new DecomposeEditor();

	public CtrlActionDecompose(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
