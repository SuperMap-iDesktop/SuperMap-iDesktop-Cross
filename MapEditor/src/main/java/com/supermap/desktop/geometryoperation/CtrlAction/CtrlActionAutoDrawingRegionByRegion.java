package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.AutoDrawingRegionByRegionEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;
/**
 * @author lixiaoyao
 */
public class CtrlActionAutoDrawingRegionByRegion extends CtrlActionEditorBase {
	private AutoDrawingRegionByRegionEditor editor=new AutoDrawingRegionByRegionEditor();
	public CtrlActionAutoDrawingRegionByRegion(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
