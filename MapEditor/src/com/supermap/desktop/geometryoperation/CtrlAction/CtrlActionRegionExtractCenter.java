package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.RegionExtractCenterEditor;

public class CtrlActionRegionExtractCenter extends CtrlActionEditorBase {

	private RegionExtractCenterEditor editor = new RegionExtractCenterEditor();

	public CtrlActionRegionExtractCenter(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
