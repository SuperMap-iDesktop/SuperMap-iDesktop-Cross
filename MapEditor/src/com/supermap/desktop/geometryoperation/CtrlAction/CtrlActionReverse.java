package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.ReverseEditor;

public class CtrlActionReverse extends CtrlActionEditorBase {

	private ReverseEditor editor = new ReverseEditor();

	public CtrlActionReverse(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
