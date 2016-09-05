package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author XiaJT
 */
public class FormTransformationTableModel extends DefaultTableModel {

	private static final String[] columnNames = new String[]{
			CommonProperties.getString(CommonProperties.Index),
			DataEditorProperties.getString("String_TransformItem_OriginalX"),
			DataEditorProperties.getString("String_TransformItem_OriginalY"),
			DataEditorProperties.getString("String_TransformItem_ReferX"),
			DataEditorProperties.getString("String_TransformItem_ReferY"),
			DataEditorProperties.getString("String_TransformItem_ResidualX"),
			DataEditorProperties.getString("String_TransformItem_ResidualY"),
			DataEditorProperties.getString("String_TransformItem_ResidualTotal"),
	};

	public static final int COLUMN_IS_SELECTED = 0;
	public static final int COLUMN_INDEX = 1;
	public static final int COLUMN_OriginalX = 2;
	public static final int COLUMN_OriginalY = 3;
	public static final int COLUMN_ReferX = 4;
	public static final int COLUMN_ReferY = 5;
	public static final int COLUMN_ResidualX = 6;
	public static final int COLUMN_ResidualY = 7;
	public static final int COLUMN_ResidualTotal = 8;


	private List<TransformationTableDataBean> dataBeanList = new ArrayList<>();

	@Override
	public int getRowCount() {
		if (dataBeanList == null) {
			return 0;
		}
		return dataBeanList.size();
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
	public Object getValueAt(int row, int column) {
		Point point;
		switch (column) {
			case COLUMN_IS_SELECTED:
				return dataBeanList.get(row).getIsSelected();
			case COLUMN_INDEX:
				return row;
			case COLUMN_OriginalX:
				point = dataBeanList.get(row).getPointOriginal();
				if (point != null) {
					return point.getX();
				}
				return null;
			case COLUMN_OriginalY:
				point = dataBeanList.get(row).getPointOriginal();
				if (point != null) {
					return point.getY();
				}
				return null;
			case COLUMN_ReferX:
				point = dataBeanList.get(row).getPointRefer();
				if (point != null) {
					return point.getX();
				}
				return null;
			case COLUMN_ReferY:
				point = dataBeanList.get(row).getPointRefer();
				if (point != null) {
					return point.getX();
				}
				return null;
			case COLUMN_ResidualX:
				return dataBeanList.get(row).getResidualX();
			case COLUMN_ResidualY:
				return dataBeanList.get(row).getResidualY();
			case COLUMN_ResidualTotal:
				return dataBeanList.get(row).getResidualTotal();
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		Point point;
		if (aValue == null || row == -1) {
			return;
		}
		if (column == COLUMN_IS_SELECTED) {
			Boolean aBoolean = Boolean.valueOf(String.valueOf(aValue));
			dataBeanList.get(row).setIsSelected(aBoolean);
			return;
		}
		String value = String.valueOf(aValue);
		double doubleValue = 0;
		if (value.length() != 0) {
			doubleValue = Double.valueOf(value);
		}
		switch (column) {
			case COLUMN_OriginalX:
				point = dataBeanList.get(row).getPointOriginal();
				if (point != null) {
					point.setLocation(doubleValue, point.getY());
				} else {
					Point pointOriginal = new Point();
					pointOriginal.setLocation(doubleValue, 0);
					dataBeanList.get(row).setPointOriginal(pointOriginal);
				}
				break;
			case COLUMN_OriginalY:
				point = dataBeanList.get(row).getPointOriginal();
				if (point != null) {
					point.setLocation(point.getX(), doubleValue);
				} else {
					Point pointOriginal = new Point();
					pointOriginal.setLocation(0, doubleValue);
					dataBeanList.get(row).setPointOriginal(pointOriginal);
				}
				break;
			case COLUMN_ReferX:
				point = dataBeanList.get(row).getPointRefer();
				if (point != null) {
					point.setLocation(doubleValue, point.getY());
				} else {
					Point pointOriginal = new Point();
					pointOriginal.setLocation(doubleValue, 0);
					dataBeanList.get(row).setPointRefer(pointOriginal);
				}
				break;
			case COLUMN_ReferY:
				point = dataBeanList.get(row).getPointRefer();
				if (point != null) {
					point.setLocation(point.getX(), doubleValue);
				} else {
					Point pointOriginal = new Point();
					pointOriginal.setLocation(0, doubleValue);
					dataBeanList.get(row).setPointRefer(pointOriginal);
				}
				break;
		}
		fireTableCellUpdated(row, column);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == COLUMN_INDEX || column == COLUMN_ResidualX || column == COLUMN_ResidualY || column == COLUMN_ResidualTotal) {
			return false;
		}
		return super.isCellEditable(row, column);
	}

	public void remove(int... rows) {
		Arrays.sort(rows);
		for (int i = rows.length - 1; i >= 0; i--) {
			dataBeanList.remove(rows[i]);
			fireTableRowsDeleted(rows[i], rows[i]);
		}
	}

	public void addPoint(boolean isOriginal, Point point) {
		if (isOriginal) {
			for (int i = 0; i < dataBeanList.size(); i++) {
				TransformationTableDataBean bean = dataBeanList.get(i);
				if (bean.getPointOriginal() == null) {
					bean.setPointOriginal(point);
					fireTableRowsUpdated(i, i);
					return;
				}
			}
			TransformationTableDataBean bean = new TransformationTableDataBean();
			bean.setPointOriginal(point);
		} else {
			for (int i = 0; i < dataBeanList.size(); i++) {
				TransformationTableDataBean bean = dataBeanList.get(i);
				if (bean.getPointRefer() == null) {
					bean.setPointRefer(point);
					fireTableRowsUpdated(i, i);
					return;
				}
			}
			TransformationTableDataBean bean = new TransformationTableDataBean();
			bean.setPointRefer(point);
		}
		fireTableRowsInserted(dataBeanList.size() - 1, dataBeanList.size() - 1);
	}
}
