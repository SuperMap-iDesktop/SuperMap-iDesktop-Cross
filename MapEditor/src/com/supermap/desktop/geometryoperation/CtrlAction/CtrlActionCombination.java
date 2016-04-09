package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.CombinationEditor;
import com.supermap.desktop.geometryoperation.IEditor;
import com.supermap.desktop.mapeditor.MapEditorEnv;

// @formatter:off
/**
 * 1.组合之后更改 Selection，无法触发 GeometrySelected
 * 2.暂时没有实现刷新打开的属性窗口
 * 3.需要想办法解决编辑功能 enable() 频繁读写 recordset 的问题
 * 
 * @author highsad
 *
 */
// @formatter:on
public class CtrlActionCombination extends CtrlActionEditor {

	private CombinationEditor editor;

	public CtrlActionCombination(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public IEditor getEditor() {
		if (this.editor == null) {
			this.editor = new CombinationEditor(MapEditorEnv.getGeometryEditManager().instance());
		}
		return this.editor;
	}
}
