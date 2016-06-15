package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.NullEditor;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapeditor.PluginEnvironment;

/**
 * 对象编辑基类 CtrlAction
 * 
 * @author highsad
 *
 */
public class CtrlActionEditorBase extends CtrlAction {

	public CtrlActionEditorBase(IBaseItem caller, IForm formClass) {
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
		PluginEnvironment.getGeometryEditManager().instance().activateEditor(getEditor());
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().requestFocus();
		}
	}

	@Override
	public boolean enable() {
		boolean flag = false;
		if (null!=PluginEnvironment.getGeometryEditManager().instance()) {
			flag = this.getEditor().enble(PluginEnvironment.getGeometryEditManager().instance());
		}
		return flag;
		
	}

	@Override
	public boolean check() {
		boolean flag = false;
		if (null!=PluginEnvironment.getGeometryEditManager().instance()) {
			flag = this.getEditor().enble(PluginEnvironment.getGeometryEditManager().instance());
		}
		return flag;
	}
}
