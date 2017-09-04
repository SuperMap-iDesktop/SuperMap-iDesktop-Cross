package com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass;

import com.supermap.analyst.spatialanalyst.ReclassSegment;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/31.
 */
public class RasterReclassModel extends DefaultTableModel {

	private final String[] columnHeaders = new String[]{
			CommonProperties.getString("String_ColumnHeader_Index"),
			ControlsProperties.getString("String_ValueLowerLimit"),
			ControlsProperties.getString("String_ValueUpperLimit"),
			ControlsProperties.getString("String_TargetValue")
	};
	private ArrayList<TableData> tableDatas = new ArrayList<>();

	public static final int COLUMN_INDEX_INDEX = 0;
	public static final int COLUMN_INDEX_VALUE_LOWER = 1;
	public static final int COLUMN_INDEX_VALUE_UPPER = 2;
	public static final int COLUMN_INDEX_VALUE_TARGET = 3;
	DecimalFormat df = new DecimalFormat("#.0");

	public RasterReclassModel() {
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column != COLUMN_INDEX_INDEX;
	}

	@Override
	public int getRowCount() {
		return tableDatas == null ? 0 : tableDatas.size();
	}

	@Override
	public int getColumnCount() {
		return columnHeaders == null ? 0 : columnHeaders.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnHeaders[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case COLUMN_INDEX_INDEX:
			case COLUMN_INDEX_VALUE_LOWER:
			case COLUMN_INDEX_VALUE_UPPER:
			case COLUMN_INDEX_VALUE_TARGET:
				return String.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch (column) {
			case COLUMN_INDEX_INDEX:
				return row+1;
			case COLUMN_INDEX_VALUE_LOWER:
				return tableDatas.get(row).segment.getStartValue();
			case COLUMN_INDEX_VALUE_UPPER:
				return tableDatas.get(row).segment.getEndValue();
			case COLUMN_INDEX_VALUE_TARGET:
				return tableDatas.get(row).segment.getNewValue();
		}
		return super.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
//		if (column == 0) {
//			this.tableDatas.get(row).isSelected = (boolean) aValue;
			fireTableCellUpdated(row, column);
		//}
	}

	public void setDataset(DatasetGrid dataset){
		this.tableDatas.clear();
		if (dataset!=null) {
			DatasetGrid datasetGrid =dataset;
			double minValue = datasetGrid.getMinValue();
			double maxValue = datasetGrid.getMaxValue();
			if (Double.compare(minValue, maxValue) == 0) {
				ReclassSegment reclassSegment = new ReclassSegment();
				reclassSegment.setStartValue(minValue);
				reclassSegment.setEndValue(maxValue);
				reclassSegment.setNewValue(1);
				this.tableDatas.add(new TableData(reclassSegment));
			} else {
				double interval = (maxValue - minValue) / 10.0;
				for (int i = 0; i < 10; i++) {
					ReclassSegment reclassSegment = new ReclassSegment();
					reclassSegment.setStartValue(Double.valueOf(df.format(minValue + interval * i)));
					reclassSegment.setEndValue(Double.valueOf(df.format(minValue + interval * (i + 1))));
					reclassSegment.setNewValue(i + 1);
					this.tableDatas.add(new TableData(reclassSegment));
				}
			}
			fireTableDataChanged();
		}
	}

	public void setData(double keys[]){
		for (int i=0;i<keys.length;i++){

		}
	}

	private class TableData {
		ReclassSegment segment;

		TableData(ReclassSegment segment) {
			this.segment = segment;
		}
	}
}
