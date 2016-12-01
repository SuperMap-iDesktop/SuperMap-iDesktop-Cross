package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.GeometrySplitByDrawingLineEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;

/**
 * @author lixiaoyao
 */
public class CtrlActionGeometrySplitByDrawingLine extends CtrlActionEditorBase {
	private GeometrySplitByDrawingLineEditor editor = new GeometrySplitByDrawingLineEditor();

	public CtrlActionGeometrySplitByDrawingLine(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
