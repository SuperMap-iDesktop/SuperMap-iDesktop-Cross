package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.RegionSplitByLineEditor;
/**
 * @author lixiaoyao
 */
public class CtrlActionRegionSplitByLine extends CtrlActionEditorBase {
	private RegionSplitByLineEditor editor=new RegionSplitByLineEditor();

	public CtrlActionRegionSplitByLine(IBaseItem caller, IForm formClass)
	{
		super(caller,formClass);
	}
	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
