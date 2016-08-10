package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.JointLineDirectionEditor;

/**
 * 首尾连接线
 * @author highsad
 *
 */
public class CtrlActionJointLineDirection extends CtrlActionEditorBase {

	private JointLineDirectionEditor editor = new JointLineDirectionEditor();

	public CtrlActionJointLineDirection(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
