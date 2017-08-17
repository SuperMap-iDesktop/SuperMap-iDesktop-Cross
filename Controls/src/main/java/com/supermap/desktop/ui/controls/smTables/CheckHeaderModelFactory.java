package com.supermap.desktop.ui.controls.smTables;

import com.supermap.desktop.ui.controls.smTables.models.ModelFieldName;
import com.supermap.desktop.ui.controls.smTables.models.ModelRarefyPoints;

import javax.swing.*;

/**
 * Created by lixiaoyao on 2017/8/10.
 */
public class CheckHeaderModelFactory {
	public static JCheckBox getHeaderCheck(IModel iModel) {
		JCheckBox jCheckBox = null;
		if (iModel instanceof ModelFieldName) {
			jCheckBox = new JCheckBox(((ModelFieldName) iModel).getColumnName(0));
		}else if (iModel instanceof ModelRarefyPoints){
			jCheckBox=new JCheckBox(((ModelRarefyPoints)iModel).getColumnName(0));
		}
		return jCheckBox;
	}
}
