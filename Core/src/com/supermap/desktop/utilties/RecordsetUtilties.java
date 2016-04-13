package com.supermap.desktop.utilties;

import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.data.Recordset;
import com.supermap.data.StatisticMode;
import com.supermap.desktop.Application;

public class RecordsetUtilties {

	private RecordsetUtilties() {
		// 工具类，不提供构造方法
	}

	/**
	 * 字段求和
	 * 
	 * @param recordset
	 * @param fieldInfo
	 * @return
	 */
	public static Object sum(Recordset recordset, FieldInfo fieldInfo) {
		return sum(recordset, fieldInfo.getName(), fieldInfo.getType());
	}

	/**
	 * 字段求和
	 * 
	 * @param recordset
	 * @param fieldName
	 * @param fieldType
	 * @return
	 */
	public static Object sum(Recordset recordset, String fieldName, FieldType fieldType) {
		Object result = null;

		try {
			if (recordset != null && FieldTypeUtilties.isNumber(fieldType)) {

				// 组件不支持64位统计
				if (fieldType != FieldType.INT64) {
					Long sum = 0L;
					recordset.moveFirst();

					while (!recordset.isEOF()) {
						Object value = recordset.getFieldValue(fieldName);

						if (value instanceof Long) {
							sum += (Long) value;
						}
						recordset.moveNext();
					}
					result = sum;
				} else {
					result = recordset.statistic(fieldName, StatisticMode.SUM);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
