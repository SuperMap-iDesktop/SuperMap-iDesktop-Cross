package com.supermap.desktop.geometryoperation.editor;

import com.supermap.desktop.geometryoperation.EditEnvironment;

public class EraseEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof EraseEditor;
	}
}
