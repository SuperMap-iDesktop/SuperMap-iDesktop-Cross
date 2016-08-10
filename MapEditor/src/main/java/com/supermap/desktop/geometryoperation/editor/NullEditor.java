package com.supermap.desktop.geometryoperation.editor;

import com.supermap.desktop.geometryoperation.EditEnvironment;

/**
 * 在对象编辑体系中，表示什么都不做，直白的说，它就是 IEditor版 null
 * 
 * @author highsad
 *
 */
public class NullEditor implements IEditor {

	public static final NullEditor INSTANCE = new NullEditor();

	private NullEditor() {
		// 不开放构造方法
	}

	@Override
	public void activate(EditEnvironment environment) {
		// 什么都不做
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		// 什么都不做
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return false;
	}
}
