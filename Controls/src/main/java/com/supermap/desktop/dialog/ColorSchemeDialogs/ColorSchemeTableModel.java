package com.supermap.desktop.dialog.ColorSchemeDialogs;

import com.supermap.data.Colors;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorScheme;
import com.supermap.desktop.ui.controls.SortTable.SortableTableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class ColorSchemeTableModel extends SortableTableModel {

	private List<ColorScheme> colorSchemes;
	private final String[] columnNames = new String[]{
			ControlsProperties.getString("String_identifier"),
			ControlsProperties.getString("String_ColumnColorRamp"),
			ControlsProperties.getString("String_ColumnRampName"),
			ControlsProperties.getString("String_ColumnRampAuthor"),
			ControlsProperties.getString("String_Description")
	};

	public static final int COLUMN_INDEX = 0;
	public static final int COLUMN_COLOR_RAMP = 1;
	public static final int COLUMN_RAMP_NAME = 2;
	public static final int COLUMN_AUTHOR = 3;
	public static final int COLUMN_DESCRIPTION = 4;

	public void setColorSchemes(List<ColorScheme> colorSchemes) {
		this.colorSchemes = colorSchemes;
		indexes = null;
		fireTableDataChanged();
	}

	public List<ColorScheme> getColorSchemes() {
		return colorSchemes;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		row = getIndexRow(row)[0];
		switch (col) {
			case COLUMN_INDEX:
				throw new UnsupportedOperationException("cant set Index");
			case COLUMN_COLOR_RAMP:
				if (value == null || value instanceof List) {
					colorSchemes.get(row).setColorList(((List) value));
				}
				break;
			case COLUMN_RAMP_NAME:
				if (value == null || value instanceof String) {
					colorSchemes.get(row).setName(((String) value));
				}
				break;
			case COLUMN_AUTHOR:
				if (value == null || value instanceof String) {
					colorSchemes.get(row).setAuthor(((String) value));
				}
				break;
			case COLUMN_DESCRIPTION:
				if (value == null || value instanceof String) {
					colorSchemes.get(row).setDescription(((String) value));
				}
				break;
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		int realRow = getIndexRow(row)[0];
		switch (col) {
			case COLUMN_INDEX:
				return row + 1;
			case COLUMN_COLOR_RAMP:
				return colorSchemes.get(realRow).getColors();
			case COLUMN_RAMP_NAME:
				return colorSchemes.get(realRow).getName();
			case COLUMN_AUTHOR:
				return colorSchemes.get(realRow).getAuthor();
			case COLUMN_DESCRIPTION:
				return colorSchemes.get(realRow).getDescription();
		}
		return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if (colorSchemes == null) {
			return 0;
		}
		return colorSchemes.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case COLUMN_INDEX:
				return String.class;
			case COLUMN_COLOR_RAMP:
				return Colors.class;
			case COLUMN_RAMP_NAME:
				return String.class;
			case COLUMN_AUTHOR:
				return String.class;
			case COLUMN_DESCRIPTION:
				return String.class;
		}
		return String.class;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return !(column == COLUMN_INDEX || column == COLUMN_COLOR_RAMP);
	}

	public void addColorScheme(ColorScheme colorScheme) {
		if (colorScheme == null) {
			return;
		}
		if (this.colorSchemes == null) {
			this.colorSchemes = new ArrayList<>();
		}
		this.colorSchemes.add(colorScheme);
		addIndexRow(this.colorSchemes.size() - 1);
		fireTableDataChanged();
	}

	public ColorScheme getColorScheme(int row) {
		row = getIndexRow(row)[0];
		return colorSchemes.get(row);
	}

	@Override
	protected void removeRowsHook(int... selectedRows) {
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			ColorScheme colorScheme = colorSchemes.get(selectedRows[i]);
			colorSchemes.remove(colorScheme);
		}
	}

	public void setColorSchemeAtRow(int row, ColorScheme colorScheme) {
		colorSchemes.get(row).copy(colorScheme);
		fireTableDataChanged();
	}

	@Override
	protected boolean isSortColumn(int column) {
		return column != COLUMN_INDEX;
	}

}
