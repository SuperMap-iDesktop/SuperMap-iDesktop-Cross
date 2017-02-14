package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.LineInterruptEditor;

/**
 * Created by lixiaoyao on 2017/1/12.
 */
public class CtrlActionLineInterrupt extends CtrlActionEditorBase {
	private LineInterruptEditor editor = new LineInterruptEditor();

	public CtrlActionLineInterrupt(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}