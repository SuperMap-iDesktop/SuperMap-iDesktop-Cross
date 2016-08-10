package com.supermap.desktop.CtrlAction.spatialQuery;

import com.supermap.data.DatasetType;
import com.supermap.data.SpatialQueryMode;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.mapping.Layer;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class TableModelSpatialQuery extends DefaultTableModel {
	private ArrayList<TableRowData> rowDatas;

	private String[] columns = new String[]{
			"",
			DataViewProperties.getString("String_Type"),
			DataViewProperties.getString("String__SearchedLayerName"),
			DataViewProperties.getString("String_SpatialQueryMode"),
			DataViewProperties.getString("String_TabularQueryCondition"),
	};
	private ArrayList<DatasetType> supportDatasetTypes;

	public TableModelSpatialQuery() {
		super();
		supportDatasetTypes = new ArrayList<>();
		supportDatasetTypes.add(DatasetType.POINT);
		supportDatasetTypes.add(DatasetType.LINE);
		supportDatasetTypes.add(DatasetType.REGION);
		supportDatasetTypes.add(DatasetType.NETWORK);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return Boolean.class;
		} else if (columnIndex == 1) {
			return DatasetType.class;
		} else if (columnIndex == 2) {
			return String.class;
		} else if (columnIndex == 3) {
			return SpatialQueryMode.class;
		} else if (columnIndex == 4) {
			return String.class;
		}
		return String.class;
	}

	@Override
	public int getRowCount() {
		if (rowDatas == null) {
			return 0;
		}
		return rowDatas.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column != 1 && column != 2;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return super.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		super.setValueAt(aValue, row, column);
	}

	public void setLayers(ArrayList<Layer> layers) {
		if (rowDatas != null) {
			rowDatas.clear();
		}
		if (layers != null && layers.size() > 0) {
			for (Layer layer : layers) {
				if (layer.getDataset() != null && supportDatasetTypes.contains(layer.getDataset().getType())) {
					rowDatas.add(new TableRowData(layer));
				}
			}
		}
	}
}
