package com.supermap.desktop.mapview.geometry.operation;

public enum EditAction {
	NONE, // 空
	SPLIT_BY_LINE, // 画线分割
	SPLIT_BY_REGION, // 画面分割
	SPLIT_BY_GEOMETRY, // 选择对象分割
	CLIP_REGION_BY_LINE, // 画线裁剪
	CLIP_REGION_BY_REGION, // 画面裁剪
	CLIP_SELECTED_REGION_BY_LINE, // 画线裁剪选中面
	DELETE_GEOMETRY, // 删除对象
	GEOMETRY_MIRROR, // 镜像
	TRIM, // 修剪
	STYLEBRUSH, // 风格刷
	PROPERTYBRUSH, // 属性刷
	BREAK, // 打断线
	EXTEND, // 延伸
	OFFSET, // 线对象偏移
	MOVEOBJ, // 对象移动
	COPYOBJ, // 对象定位复制
	POINTS_AVERAGEERROR, // 结点平差
	ERASE, // 擦除
	ERASEOUTPART, // 擦除外部
	PARTIALUPDATE, // 局部更新
	EXPLODE, // 炸碎
	JOINT, // 连接线对象
	HOLEYREGION, // 岛洞多边形
}
