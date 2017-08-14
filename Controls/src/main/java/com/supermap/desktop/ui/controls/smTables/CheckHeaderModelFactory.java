package com.supermap.desktop.ui.controls.smTables;

import com.supermap.desktop.ui.controls.smTables.models.ModelFieldName;

import javax.swing.*;

/**
 * Created by lixiaoyao on 2017/8/10.
 */
public class CheckHeaderModelFactory {
	public static JCheckBox getHeaderCheck(IModel iModel) {
		JCheckBox jCheckBox = null;
		if (iModel instanceof ModelFieldName) {
			jCheckBox = new JCheckBox(((ModelFieldName) iModel).getColumnName(0));
		}
		return jCheckBox;
	}
}
