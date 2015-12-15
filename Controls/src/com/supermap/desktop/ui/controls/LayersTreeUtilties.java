package com.supermap.desktop.ui.controls;

import com.supermap.mapping.Layer;
import com.supermap.mapping.ThemeGridRangeItem;
import com.supermap.mapping.ThemeGridUniqueItem;
import com.supermap.mapping.ThemeLabelItem;
import com.supermap.mapping.ThemeRangeItem;
import com.supermap.mapping.ThemeUniqueItem;
import com.supermap.realspace.Feature3D;
import com.supermap.realspace.Feature3Ds;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.ScreenLayer3D;
import com.supermap.realspace.TerrainLayer;
import com.supermap.realspace.Theme3DRangeItem;
import com.supermap.realspace.Theme3DUniqueItem;

public class LayersTreeUtilties {

	private LayersTreeUtilties() {
		// 工具类，不需要构造函数
	}

	public static boolean isTreeNodeDataVisible(Object treeNodeData) {
		boolean isVisible = false;
		if (treeNodeData instanceof ThemeGridRangeItem) {
			ThemeGridRangeItem gridRangeItem = (ThemeGridRangeItem) treeNodeData;
			isVisible = gridRangeItem.isVisible();
		} else if (treeNodeData instanceof ThemeGridUniqueItem) {
			ThemeGridUniqueItem gridUniqueItem = (ThemeGridUniqueItem) treeNodeData;
			isVisible = gridUniqueItem.isVisible();
		} else if (treeNodeData instanceof ThemeLabelItem) {
			ThemeLabelItem labelItem = (ThemeLabelItem) treeNodeData;
			isVisible = labelItem.isVisible();
		} else if (treeNodeData instanceof ThemeRangeItem) {
			ThemeRangeItem rangeItem = (ThemeRangeItem) treeNodeData;
			isVisible = rangeItem.isVisible();
		} else if (treeNodeData instanceof ThemeUniqueItem) {
			ThemeUniqueItem uniqueItem = (ThemeUniqueItem) treeNodeData;
			isVisible = uniqueItem.isVisible();
		} else if (treeNodeData instanceof Layer) {
			Layer layer = (Layer) treeNodeData;
			isVisible = layer.isVisible();
		} else if (treeNodeData instanceof Layer3D) {
			Layer3D layer3D = (Layer3D) treeNodeData;
			isVisible = layer3D.isVisible();
		} else if (treeNodeData instanceof ScreenLayer3D) {
			ScreenLayer3D screenLayer3D = (ScreenLayer3D) treeNodeData;
			isVisible = screenLayer3D.isVisible();
		} else if (treeNodeData instanceof Theme3DRangeItem) {
			Theme3DRangeItem theme3DRangeItem = (Theme3DRangeItem) treeNodeData;
			isVisible = theme3DRangeItem.isVisible();
		} else if (treeNodeData instanceof Theme3DUniqueItem) {
			Theme3DUniqueItem item = (Theme3DUniqueItem) treeNodeData;
			isVisible = item.isVisible();
		} else if (treeNodeData instanceof TerrainLayer) {
			TerrainLayer terrainLayer = (TerrainLayer) treeNodeData;
			isVisible = terrainLayer.isVisible();
		} else if (treeNodeData instanceof Feature3D) {
			Feature3D feature3D = (Feature3D) treeNodeData;
			isVisible = feature3D.isVisible();
		} else if (treeNodeData instanceof Feature3Ds) {
			Feature3Ds feature3Ds = (Feature3Ds) treeNodeData;
			isVisible = feature3Ds.isVisible();
		}
		return isVisible;
	}
}
