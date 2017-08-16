package com.supermap.desktop.ui.controls.smTables;

import com.supermap.desktop.ui.controls.smTables.tables.TableFieldName;
import com.supermap.desktop.ui.controls.smTables.tables.TableFieldNameCaptionType;
import com.supermap.desktop.ui.controls.smTables.tables.TableRarefyPoints;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public class TableFactory {
	public static ITable getTable(String tableKeys) {
		ITable iTable = null;
		if (tableKeys.equals(TableKeys.FIELD_NAME_CAPTION_TYPE)) {
			iTable = new TableFieldNameCaptionType();
		} else if (tableKeys.equals(TableKeys.FIELD_NAME)) {
			iTable = new TableFieldName();
		}else if (tableKeys.equals(TableKeys.STATISTICS_FIELD_FOR_RAREFY_POINTS)){
			iTable=new TableRarefyPoints();
		}

		return iTable;
	}
}
