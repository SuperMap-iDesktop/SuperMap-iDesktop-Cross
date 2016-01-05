package com.supermap.desktop.utilties;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoRegion;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Map;

public class LayerUtilties {
	private LayerUtilties() {
		// 工具类不提供构造函数
	}

	/**
	 * 获取指定地图上指定图层的 Bounds
	 * 
	 * @param map 指定的地图，主要用来做动态投影的判断处理
	 * @param layer
	 * @return
	 */
	public static Rectangle2D getLayerBounds(Map map, Layer layer) {
		Rectangle2D rectangle2D = Rectangle2D.getEMPTY();
		try {
			// 有过滤条件的情况要区分对待 added by zengwh 2012-1-6
			Recordset recordset = null;
			if ("".equals(layer.getDisplayFilter().getAttributeFilter())) {
				rectangle2D = layer.getDataset().getBounds();
			} else {
				recordset = ((DatasetVector) layer.getDataset()).query(layer.getDisplayFilter());
				if (recordset != null) {
					rectangle2D = recordset.getBounds();
					recordset.dispose();
				}
			}

			Point2Ds pts = new Point2Ds();
			pts.add(new Point2D(rectangle2D.getLeft(), rectangle2D.getTop()));
			pts.add(new Point2D(rectangle2D.getRight(), rectangle2D.getTop()));
			pts.add(new Point2D(rectangle2D.getRight(), rectangle2D.getBottom()));
			pts.add(new Point2D(rectangle2D.getLeft(), rectangle2D.getBottom()));
			pts.add(new Point2D(rectangle2D.getLeft(), rectangle2D.getTop()));
			GeoRegion region = new GeoRegion(pts);
			if (map.isDynamicProjection()) {
				CoordSysTranslator.convert(region, layer.getDataset().getPrjCoordSys(), map.getPrjCoordSys(), new CoordSysTransParameter(),
						CoordSysTransMethod.MTH_POSITION_VECTOR);
			}
			rectangle2D = region.getBounds();
			region.dispose();
			// 废弃原来获取显示范围的方法，采用新的方法，使全幅显示图层的效果与地图中只有一个图层时全幅显示地图的效果一样
			// 其中 26 是经验值
			Double dX = rectangle2D.getWidth() / 26;
			Double dY = rectangle2D.getHeight() / 26;
			rectangle2D = new Rectangle2D(rectangle2D.getLeft() - dX, rectangle2D.getBottom() - dY, rectangle2D.getRight() + dX, rectangle2D.getTop() + dY);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return rectangle2D;
	}

	/**
	 * 获取指定地图上指定图层分组的 Bounds
	 * 
	 * @param map
	 * @param layerGroup
	 * @return
	 */
	public static Rectangle2D getLayerBounds(Map map, LayerGroup layerGroup) {
		Rectangle2D rectangle2D = Rectangle2D.getEMPTY();
		try {
			for (int i = 0; i < layerGroup.getCount(); i++) {
				Layer layer = layerGroup.get(i);

				if (layer instanceof LayerGroup) {
					if (i == 0) {
						rectangle2D = getLayerBounds(map, (LayerGroup) layer);
					} else {
						rectangle2D.union(getLayerBounds(map, (LayerGroup) layer));
					}
				} else {
					if (i == 0) {
						rectangle2D = getLayerBounds(map, layer);
					} else {
						rectangle2D.union(getLayerBounds(map, layer));
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return rectangle2D;
	}

	public static int getSelectionSize(Layer layer) {
		int size = 0;

		try {
			if (layer instanceof LayerGroup) {
				for (int i = 0; i < ((LayerGroup) layer).getCount(); i++) {
					size += getSelectionSize(((LayerGroup) layer).get(i));
				}
			} else {
				if (layer.isSelectable() && layer.getSelection() != null) {
					size = layer.getSelection().getCount();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return size;
	}
}
