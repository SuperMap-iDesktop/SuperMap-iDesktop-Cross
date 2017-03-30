package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.mapping.Layer;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;


/**
 * 2017.3.28
 *
 * @author YuanR
 *         地图裁剪JTable@model
 */
public class MapClipTableModel extends AbstractTableModel {
	public Vector layerInfo;

	public static final int COLUMN_INDEX_ISCLIP = 0;
	public static final int COLUMN_INDEX_LAYERCAPTION = 1;
	public static final int COLUMN_INDEX_AIMDATASOURCE = 2;
	public static final int COLUMN_INDEX_AIMDATASET = 3;
	public static final int COLUMN_INDEX_CLIPTYPE = 4;
	public static final int COLUMN_INDEX_ERASE = 5;
	public static final int COLUMN_INDEX_EXACTCLIP = 6;
	public static final int COLUMNS_NUMBER = 7;

	public MapClipTableModel() {
		this.layerInfo = new Vector();
	}

	/**
	 * 增加一条记录
	 *
	 * @param clip             是否裁剪
	 * @param layer            图层
	 * @param targetDatasource 目标数据源
	 * @param targetDataset    目标数据集
	 * @param clipType         裁剪类型（区域内/区域外）
	 * @param erase            是否擦除
	 * @param exactClip        是否精确裁剪
	 */
	public void addRowLayerInfo(boolean clip, Layer layer, Datasource targetDatasource,
	                            String targetDataset, String clipType, String erase, boolean exactClip) {
		Vector v = new Vector(6);
		v.add(COLUMN_INDEX_ISCLIP, new Boolean(clip));
		v.add(COLUMN_INDEX_LAYERCAPTION, layer);
		v.add(COLUMN_INDEX_AIMDATASOURCE, targetDatasource);
		v.add(COLUMN_INDEX_AIMDATASET, targetDataset);
		v.add(COLUMN_INDEX_CLIPTYPE, clipType);
		v.add(COLUMN_INDEX_ERASE, erase);
		v.add(COLUMN_INDEX_EXACTCLIP, exactClip);
		this.layerInfo.add(v);
	}


	@Override
	public String getColumnName(int column) {
		if (column == COLUMN_INDEX_ISCLIP) {
			return MapViewProperties.getString("String_MapClip_IsClip");
		} else if (column == COLUMN_INDEX_LAYERCAPTION) {
			return MapViewProperties.getString("String_MapClip_LayerCaption");
		} else if (column == COLUMN_INDEX_AIMDATASOURCE) {
			return MapViewProperties.getString("String_MapClip_TargetDatasource");
		} else if (column == COLUMN_INDEX_AIMDATASET) {
			return MapViewProperties.getString("String_MapClip_TargetDataset");
		} else if (column == COLUMN_INDEX_CLIPTYPE) {
			return MapViewProperties.getString("String_MapClip_ClipType");
		} else if (column == COLUMN_INDEX_ERASE) {
			return MapViewProperties.getString("String_MapClip_Erase");
		}
		return "";
	}

	@Override
	public int getRowCount() {
		return this.layerInfo.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMNS_NUMBER - 1;
	}

	/**
	 * 设置单元格是否可修改
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == COLUMN_INDEX_LAYERCAPTION) {
			return false;
		}
		// 是否擦除列需要添加判断，当数据集类型是矢量数据集时才能进行是否擦除操作的设置
		if (columnIndex == COLUMN_INDEX_ERASE) {
			if (((Layer) (this.getValueAt(rowIndex, COLUMN_INDEX_LAYERCAPTION))).getDataset() instanceof DatasetVector) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 使修改的内容生效
	 *
	 * @param value
	 * @param row
	 * @param col
	 */
	public void setValueAt(Object value, int row, int col) {
		((Vector) layerInfo.get(row)).remove(col);
		((Vector) layerInfo.get(row)).add(col, value);
		this.fireTableCellUpdated(row, col);
	}

	@Override
	public Object getValueAt(int row, int col) {
		return ((Vector) this.layerInfo.get(row)).get(col);
	}


	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}

	public Vector getLayerInfo() {
		return layerInfo;
	}
}
