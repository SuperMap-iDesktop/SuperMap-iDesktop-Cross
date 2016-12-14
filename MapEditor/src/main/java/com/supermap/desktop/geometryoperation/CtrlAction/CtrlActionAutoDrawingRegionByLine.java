package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.AutoDrawingRegionByLineEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;

/**
 * @author lixiaoyao
 */
public class CtrlActionAutoDrawingRegionByLine extends CtrlActionEditorBase {
	private AutoDrawingRegionByLineEditor editor = new AutoDrawingRegionByLineEditor ();

	public CtrlActionAutoDrawingRegionByLine(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
