package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics.StatisticsField;

import com.supermap.analyst.spatialstatistics.StatisticsType;
import com.supermap.data.FieldInfo;

/**
 * Created by hanyz on 2017/5/3.
 * 字段信息与统计类型组合
 * 一个字段可以有多种统计方式，如SmUserID Max，SmUserID Min
 */
public class StatisticsFieldInfo {
	private FieldInfo fieldInfo;
	private StatisticsType statisticsType;

	StatisticsFieldInfo(FieldInfo fieldInfo, StatisticsType statisticsType) {
		this.fieldInfo = fieldInfo;
		this.statisticsType = statisticsType;
	}

	public FieldInfo getFieldInfo() {
		return fieldInfo;
	}

	public void setFieldInfo(FieldInfo fieldInfo) {
		this.fieldInfo = fieldInfo;
	}

	public StatisticsType getStatisticsType() {
		return statisticsType;
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
			if (statisticsFieldInfo.getFieldInfo().equals(this.fieldInfo) && statisticsFieldInfo.getStatisticsType().equals(this.statisticsType)) {
				return true;
			}
		}
		return super.equals(obj);
	}
}
