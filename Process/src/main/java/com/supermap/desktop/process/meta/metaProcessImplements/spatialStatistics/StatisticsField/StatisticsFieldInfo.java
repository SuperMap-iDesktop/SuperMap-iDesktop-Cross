package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics.StatisticsField;

import com.supermap.analyst.spatialstatistics.StatisticsType;
import com.supermap.data.FieldType;

/**
 * Created by hanyz on 2017/5/3.
 * 字段信息与统计类型组合
 * 一个字段可以有多种统计方式，如SmUserID Max，SmUserID Min
 * <p>
 * 修改当前类，为实现统计字段可自行添加编辑功能，对StatisticsFieldInfo进行修改
 * 修改构造函数，使其通过（name，type，statisticsType）三个参数进行构造，这样就可以自行添加条目，而不影响当前数据的属性信息。
 * yuanR 2017.7.12
 */
public class StatisticsFieldInfo {
	private String fieldName;
	private FieldType fieldType;
	private StatisticsType statisticsType;


	// 废除此方法，此方法只能实现当前数据集属性信息的获取，并不能实现手动添加一行数据
//	StatisticsFieldInfo(FieldInfo fieldInfo, StatisticsType statisticsType) {
//		this.fieldInfo = fieldInfo;
//		this.statisticsType = statisticsType;
//	}

	// 新构造-yuanR 2017.7.12
	StatisticsFieldInfo(String fieldName, FieldType fieldType, StatisticsType statisticsType) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.statisticsType = statisticsType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public StatisticsType getStatisticsType() {
		return statisticsType;
	}

	public void setFieldName(String value) {
		this.fieldName = value;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public void setStatisticsType(StatisticsType statisticsType) {
		this.statisticsType = statisticsType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof StatisticsFieldInfo) {
			if (obj == this) {
				return true;
			}
			StatisticsFieldInfo statisticsFieldInfo = (StatisticsFieldInfo) obj;
			if (statisticsFieldInfo.getFieldName().equals(this.fieldName) &&
					statisticsFieldInfo.getFieldType().equals(this.fieldType) &&
					statisticsFieldInfo.getStatisticsType().equals(this.statisticsType)) {
				return true;
			}
		}
		return super.equals(obj);
	}
}
