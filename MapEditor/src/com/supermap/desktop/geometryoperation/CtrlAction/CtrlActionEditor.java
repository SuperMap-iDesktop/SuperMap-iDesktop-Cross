package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.IEditor;
import com.supermap.desktop.geometryoperation.NullEditor;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapeditor.MapEditorEnv;

public class CtrlActionEditor extends CtrlAction {

	public CtrlActionEditor(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	/**
	 * 子类必须重写这个
	 * 
	 * @return
	 */
	public IEditor getEditor() {
		return NullEditor.INSTANCE;
	}

	@Override
	public void run() {
		MapEditorEnv.getGeometryEditManager().instance().activateEditor(getEditor());
	}

	@Override
	public boolean enable() {
		return getEditor().enble();
	}

	@Override
	public boolean check() {
		return this.getEditor().check();
	}
}
