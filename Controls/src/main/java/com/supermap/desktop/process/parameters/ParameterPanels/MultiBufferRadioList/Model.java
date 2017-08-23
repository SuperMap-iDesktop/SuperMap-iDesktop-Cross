package com.supermap.desktop.process.parameters.ParameterPanels.MultiBufferRadioList;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/22 0022.
 */
public class Model extends DefaultTableModel {

	private final String[] columnNames = new String[]{
			"序号",
			"半径"
	};

	private final boolean[] isColumnEditable = new boolean[]{
			false, true
	};

	public static final int COLUMN_INDEX_INDEX = 0;
	public static final int COLUMN_INDEX_BUFFER_RADIO = 1;

	private ArrayList<Double> radioValues;

	public Model(ArrayList<Double> radioValues) {
		super();
		this.radioValues = radioValues;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == COLUMN_INDEX_INDEX) {
			return String.class;
		} else if (columnIndex == COLUMN_INDEX_BUFFER_RADIO) {
			return Double.class;
		}
		return String.class;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == COLUMN_INDEX_INDEX) {
			return row + 1;
		} else if (column == COLUMN_INDEX_BUFFER_RADIO) {
			return radioValues.get(row);
		}
		throw new UnsupportedOperationException("column out of index");
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return isColumnEditable[column];
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == COLUMN_INDEX_INDEX) {
			throw new UnsupportedOperationException("index can't change");
		} else if (column == COLUMN_INDEX_BUFFER_RADIO) {
			radioValues.set(row, (Double) aValue);
		}
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		if (radioValues == null) {
			return 0;
		}
		return radioValues.size();
	}

	@Override
	public void removeRow(int row) {
		if (row != this.getRowCount() - 1) {
			this.radioValues.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}

	public void insertRow(int row, double rowData) {
		this.radioValues.add(row, rowData);
		fireTableRowsUpdated(row, row);
	}

	public void setRadioValues(ArrayList<Double> radioValues) {
		this.radioValues = radioValues;
		fireTableDataChanged();
	}

	public void moveUp(int... rows) {
		for (int row : rows) {
			moveTo(row, row - 1);
		}
		fireTableDataChanged();
	}

	public void moveDown(int... rows) {
		for (int i = rows.length - 1; i >= 0; i--) {
			moveTo(rows[i], rows[i] + 1);
		}
		fireTableDataChanged();
	}

	private void moveTo(int beforeRow, int resultRow) {
		if (beforeRow > resultRow) {
			for (int i = beforeRow; i > resultRow; i--) {
				swap(i, i - 1);
			}
		} else if (beforeRow < resultRow) {
			for (int i = beforeRow; i < resultRow; i++) {
				swap(i, i + 1);
			}
		}
	}

	private void swap(int i, int i1) {
		double radioValue = radioValues.get(i);
		radioValues.set(i, radioValues.get(i1));
		radioValues.set(i1, radioValue);
	}
}
