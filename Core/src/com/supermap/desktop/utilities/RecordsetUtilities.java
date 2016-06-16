package com.supermap.desktop.utilities;

import java.util.HashMap;
import java.util.Map;

import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.Recordset;
import com.supermap.data.StatisticMode;
import com.supermap.desktop.Application;

public class RecordsetUtilities {

	private RecordsetUtilities() {
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
			if (recordset != null && FieldTypeUtilities.isNumber(fieldType)) {

				// 组件不支持64位统计
				if (fieldType == FieldType.INT64) {
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

	/**
	 * 获取指定 recordset 当前记录的字段值（不含系统字段）
	 * 
	 * @param recordset
	 * @return
	 */
	public static Map<String, Object> getFieldValues(Recordset recordset) {
		Map<String, Object> fieldValues = new HashMap<>();
		FieldInfos fieldInfos = recordset.getFieldInfos();

		for (int i = 0; i < fieldInfos.getCount(); i++) {
			FieldInfo fieldInfo = fieldInfos.get(i);

			if (!fieldInfo.isSystemField()) {
				fieldValues.put(fieldInfo.getName(), recordset.getFieldValue(fieldInfo.getName()));
			}
		}
		return fieldValues;
	}
}
