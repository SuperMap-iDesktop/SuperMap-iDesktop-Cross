package com.supermap.desktop.ui.controls;

import java.util.EventObject;

import com.supermap.mapping.Theme;
/**
 * 对话框返回结果事件
 * @author zhaosy
 *
 */
public class DialogResultEvent extends EventObject {
	private  transient  DialogResult dialogResult;

	public DialogResult getDialogResult() {
		return dialogResult;
	}

	public DialogResultEvent(Object source, DialogResult result) {
		super(source);
		dialogResult = result;
	}

}
