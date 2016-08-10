package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.AddNodeEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;

public class CtrlActionAddNode extends CtrlActionEditorBase {

	private AddNodeEditor editor = new AddNodeEditor();

	public CtrlActionAddNode(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
