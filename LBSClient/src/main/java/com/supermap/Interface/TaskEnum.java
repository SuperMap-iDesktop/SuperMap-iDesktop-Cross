package com.supermap.Interface;

public enum TaskEnum {
	DOWNLOADTASK, // 下载任务
	UPLOADTASK, // 上传任务
	BOUNDSQUERYTASK, // 范围查询任务
	CREATESPATIALINDEXTASK, // 空间索引任务
	FINDTRACKTASK, // 轨迹查询任务
	KERNELDENSITYTASK, // (计算热度图任务)核密度分析
	KERNELDENSITYREALTIMETASK, // 实时热度图
	SPATIALQUERY, // 空间查询
	ATTRIBUTEQUERY,// 属性查询
	HEATMAP//热度图
}
