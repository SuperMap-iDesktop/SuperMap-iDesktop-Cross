package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.GeometrySplitByDrawingRegionEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;

/**
 * @author lixiaoyao
 */
public class CtrlActionGeometrySplitByDrawingRegion extends CtrlActionEditorBase {
	private GeometrySplitByDrawingRegionEditor editor = new GeometrySplitByDrawingRegionEditor();

	public CtrlActionGeometrySplitByDrawingRegion(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
