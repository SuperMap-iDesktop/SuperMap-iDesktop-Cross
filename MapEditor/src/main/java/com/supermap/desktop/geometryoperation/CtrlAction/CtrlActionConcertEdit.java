package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.ConcertEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;

/**
 * @author lixiaoyao
 */
public class CtrlActionConcertEdit extends CtrlActionEditorBase {
	private ConcertEditor editor = new ConcertEditor();

	public CtrlActionConcertEdit(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
