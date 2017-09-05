package com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass;

import com.supermap.analyst.spatialanalyst.ReclassSegment;
import com.supermap.analyst.spatialanalyst.ReclassSegmentType;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.StringUtilities;

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
	private ReclassSegmentType reclassSegmentType = ReclassSegmentType.CLOSEOPEN;

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
				return row + 1;
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
		if (isLegitNumber(aValue.toString())) {
			double tempValue = Double.valueOf(aValue.toString());
			if (column == COLUMN_INDEX_VALUE_LOWER) {
				if (this.tableDatas != null) {
					if (row == 0) {
						if (Double.compare(tempValue, this.tableDatas.get(row).segment.getEndValue()) == -1) {
							this.tableDatas.get(row).segment.setStartValue(tempValue);
						}
					} else {
						if (Double.compare(tempValue, this.tableDatas.get(row).segment.getEndValue()) == -1 &&
								Double.compare(tempValue, this.tableDatas.get(row - 1).segment.getStartValue()) == 1) {
							this.tableDatas.get(row).segment.setStartValue(tempValue);
							this.tableDatas.get(row - 1).segment.setEndValue(tempValue);
						}
					}
				}
			} else if (column == COLUMN_INDEX_VALUE_UPPER) {
				if (this.tableDatas != null) {
					if (row == this.tableDatas.size() - 1) {
						if (Double.compare(tempValue, this.tableDatas.get(row).segment.getStartValue()) == 1) {
							this.tableDatas.get(row).segment.setEndValue(tempValue);
						}
					} else {
						if (Double.compare(tempValue, this.tableDatas.get(row).segment.getStartValue()) == 1 &&
								Double.compare(tempValue, this.tableDatas.get(row + 1).segment.getEndValue()) == -1) {
							this.tableDatas.get(row).segment.setEndValue(tempValue);
							this.tableDatas.get(row + 1).segment.setStartValue(tempValue);
						}
					}
				}
			} else if (column == COLUMN_INDEX_VALUE_TARGET) {
				this.tableDatas.get(row).segment.setNewValue(tempValue);
			}
			fireTableDataChanged();
			//fireTableCellUpdated(row, column);
		}
	}

	private boolean isLegitNumber(String string) {
		if (StringUtilities.isNullOrEmpty(string)) {
			return false;
		}
		try {
			Double num = Double.valueOf(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void setDataset(DatasetGrid dataset) {
		this.tableDatas.clear();
		if (dataset != null) {
			DatasetGrid datasetGrid = dataset;
			double minValue = datasetGrid.getMinValue();
			double maxValue = datasetGrid.getMaxValue();
			if (Double.compare(minValue, maxValue) == 0) {
				ReclassSegment reclassSegment = new ReclassSegment();
				reclassSegment.setStartValue(minValue);
				reclassSegment.setEndValue(maxValue);
				reclassSegment.setNewValue(1);
				reclassSegment.setSegmentType(reclassSegmentType);
				this.tableDatas.add(new TableData(reclassSegment));
			} else {
				double interval = (maxValue - minValue) / 10.0;
				for (int i = 0; i < 10; i++) {
					ReclassSegment reclassSegment = new ReclassSegment();
					reclassSegment.setStartValue(Double.valueOf(df.format(minValue + interval * i)));
					reclassSegment.setEndValue(Double.valueOf(df.format(minValue + interval * (i + 1))));
					reclassSegment.setNewValue(i + 1);
					reclassSegment.setSegmentType(reclassSegmentType);
					this.tableDatas.add(new TableData(reclassSegment));
				}
			}
			fireTableDataChanged();
		}
	}

	public void setData(double[] newValues){
		if (newValues!=null){
			this.tableDatas.clear();
			for(int i=0;i<newValues.length-1;i++){
				ReclassSegment reclassSegment = new ReclassSegment();
				reclassSegment.setStartValue(Double.valueOf(df.format(newValues[i])));
				reclassSegment.setEndValue(Double.valueOf(df.format(newValues[i+1])));
				reclassSegment.setNewValue(i + 1);
				reclassSegment.setSegmentType(reclassSegmentType);
				this.tableDatas.add(new TableData(reclassSegment));
			}
			fireTableDataChanged();
		}

	}

	public void combine(int[] rows) {
		if (rows != null && rows.length > 1) {
			int startRow = rows[0];
			int endRow = rows[rows.length - 1];
			this.tableDatas.get(startRow).segment.setEndValue(this.tableDatas.get(endRow).segment.getEndValue());
			for (int i = startRow + 1; i <= endRow; i++) {
				this.tableDatas.remove(startRow + 1);
			}
			fireTableDataChanged();
		}
	}

	public void split(int row) {
		if (row != -1) {
			double newValue = this.tableDatas.get(row).segment.getStartValue() + this.tableDatas.get(row).segment.getEndValue();
			newValue = Double.valueOf(df.format(newValue / 2.0));

			ReclassSegment reclassSegment = new ReclassSegment();
			reclassSegment.setStartValue(newValue);
			reclassSegment.setEndValue(this.tableDatas.get(row).segment.getEndValue());
			reclassSegment.setNewValue(this.tableDatas.get(row).segment.getNewValue());
			reclassSegment.setSegmentType(reclassSegmentType);
			this.tableDatas.get(row).segment.setEndValue(newValue);
			TableData tableData = new TableData(reclassSegment);
			this.tableDatas.add(row + 1, tableData);
			fireTableDataChanged();
		}
	}

	public void inverse() {
		double[] newValue = new double[this.tableDatas.size()];
		for (int i = 0; i < newValue.length; i++) {
			newValue[i] = this.tableDatas.get(i).segment.getNewValue();
		}
		for (int i = 0; i < newValue.length; i++) {
			this.tableDatas.get(i).segment.setNewValue(newValue[newValue.length - 1 - i]);
		}
		fireTableDataChanged();
	}

	public ReclassSegment[] getSegmentData() {
		ReclassSegment[] reclassSegment = new ReclassSegment[this.tableDatas.size()];
		for (int i = 0; i < this.tableDatas.size(); i++) {
			reclassSegment[i] = this.tableDatas.get(i).segment;
		}
		return reclassSegment;
	}

	public void setSegmentData(ReclassSegment[] reclassSegments) {
		this.tableDatas.clear();
		if (reclassSegments!=null){
			for(int i=0;i<reclassSegments.length;i++){
				this.tableDatas.add(new TableData(reclassSegments[i]));
			}
		}
		fireTableDataChanged();
	}

	private class TableData {
		ReclassSegment segment;

		TableData(ReclassSegment segment) {
			this.segment = segment;
		}
	}

	public void setReclassSegmentType(ReclassSegmentType reclassSegmentType) {
		this.reclassSegmentType = reclassSegmentType;
		for (int i = 0; i < this.tableDatas.size(); i++) {
			this.tableDatas.get(i).segment.setSegmentType(this.reclassSegmentType);
		}
	}

}
