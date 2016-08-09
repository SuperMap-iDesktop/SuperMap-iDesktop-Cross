package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.IntersectEditor;

public class CtrlActionIntersect extends CtrlActionEditorBase {

	private IntersectEditor editor = new IntersectEditor();

	public CtrlActionIntersect(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 子类必须重写这个
	 * 
	 * @return
	 */
	public IEditor getEditor() {
		return this.editor;
	}
}
