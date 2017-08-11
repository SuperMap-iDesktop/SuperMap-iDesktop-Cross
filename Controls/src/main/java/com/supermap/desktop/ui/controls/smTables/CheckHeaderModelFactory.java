package com.supermap.desktop.ui.controls.smTables;

import com.supermap.desktop.ui.controls.smTables.models.ModelFieldName;
import com.supermap.desktop.ui.controls.smTables.tables.TableFieldName;
import com.supermap.desktop.ui.controls.smTables.tables.TableFieldNameCaptionType;

import javax.swing.*;
import java.security.PrivateKey;

/**
 * Created by lixiaoyao on 2017/8/10.
 */
public class CheckHeaderModelFactory  {
	public static JCheckBox getHeaderCheck(IModel iModel){
		JCheckBox jCheckBox=null;
		if (iModel instanceof ModelFieldName){
			jCheckBox=new JCheckBox(((ModelFieldName)iModel).getColumnName(0));
		}
		return jCheckBox;
	}
}
