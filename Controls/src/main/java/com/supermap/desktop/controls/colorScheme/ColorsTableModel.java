package com.supermap.desktop.controls.colorScheme;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author XiaJT
 */
public class ColorsTableModel extends DefaultTableModel {

	private static final String[] columnNames = new String[]{
			CommonProperties.getString(CommonProperties.Index),
			ControlsProperties.getString("String_Column_Color")
	};
	private final Color defaultColor = new Color(255, 128, 128);
	private java.util.List<Color> colors;

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 0) {
			return row + 1;
		} else {
			return colors.get(row);
		}
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == 0) {
			throw new UnsupportedOperationException();
		} else {
			colors.set(row, (Color) aValue);
		}
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 1;
	}


	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return String.class;
		} else {
			return Color.class;
		}
	}

	@Override
	public int getRowCount() {
		if (colors == null) {
			return 0;
		}
		return colors.size();
	}

	public void moveToTop(int... rows) {
		for (int i = 0; i < rows.length; i++) {
			moveTo(rows[i], i);
		}
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

	public void moveToBottom(int... rows) {
		int count = colors.size();
		for (int i = rows.length - 1; i >= 0; i--) {
			moveTo(rows[i], i + count - rows.length);
		}
		fireTableDataChanged();
	}

	public void add() {
		colors.add(defaultColor);
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
		Color color = colors.get(i);
		colors.set(i, colors.get(i1));
		colors.set(i1, color);
	}

	public void removeRow(int[] selectedRow) {
		for (int i = selectedRow.length - 1; i >= 0; i--) {
			colors.remove(selectedRow[i]);
		}
		fireTableDataChanged();
	}

	public void colorInvert() {
		for (int i = 0; i < colors.size() / 2; i++) {
			swap(i, colors.size() - 1 - i);
		}
		fireTableDataChanged();
	}
}
