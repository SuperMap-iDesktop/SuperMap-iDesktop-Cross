package com.supermap.desktop.geometryoperation.CtrlAction;

import javax.swing.Timer;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.EraseEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;

public class CtrlActionErase extends CtrlActionEditorBase {

	private EraseEditor editor = new EraseEditor();
	Timer timer;

	public CtrlActionErase(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

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
