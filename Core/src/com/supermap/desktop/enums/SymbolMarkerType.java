package com.supermap.desktop.enums;

import com.supermap.data.StrokeType;
import com.supermap.data.SymbolMarker;

/**
 * 点符号类型
 *
 * @author xiajt
 */
public enum SymbolMarkerType {
	/**
	 * 空
	 */
	Null,
	/**
	 * 矢量
	 */
	Vector,
	/**
	 * 栅格
	 */
	Raster,
	/**
	 * 混合
	 */
	Mix;

	/**
	 * 获取点符号的符号类型
	 *
	 * @param symbolMarker 点符号
	 * @return 点符号类型
	 */
	public SymbolMarkerType getSymbolMarkerType(SymbolMarker symbolMarker) {
		SymbolMarkerType result = SymbolMarkerType.Vector;
		if (symbolMarker != null) {
			int count = symbolMarker.getCount();
			if (count < 1) {
				result = SymbolMarkerType.Null; // 笔划为空则是空符号
			} else {
				for (int i = 0; i < count; i++) {
					if (symbolMarker.get(i).getType() == StrokeType.ST_BITMAP || symbolMarker.get(i).getType() == StrokeType.ST_ICON) {
						// 这两种代表栅格符号
						result = SymbolMarkerType.Raster; // 只要有栅格的笔划就暂定为栅格
						break;
					}
				}

				if (result == SymbolMarkerType.Raster && count > 1) {
					// 只有一笔的是栅格符号，超过一笔的就是混合符号
					result = SymbolMarkerType.Mix;
				}
			}
		}
		return result;
	}

}
