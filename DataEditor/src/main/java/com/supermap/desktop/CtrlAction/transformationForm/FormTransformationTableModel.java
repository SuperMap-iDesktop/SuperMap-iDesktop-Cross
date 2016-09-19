package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.Point2D;
import com.supermap.desktop.CtrlAction.transformationForm.beans.FormTransformationSubFormType;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationTableDataBean;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author XiaJT
 */
public class FormTransformationTableModel extends DefaultTableModel {

	private static final String[] columnNames = new String[]{
			"",
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
		Point2D point;
		switch (column) {
			case COLUMN_IS_SELECTED:
				return dataBeanList.get(row).getIsSelected();
			case COLUMN_INDEX:
				return row + 1;
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
					return point.getY();
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
		Point2D point;
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
					point.setX(doubleValue);
				} else {
					Point2D pointOriginal = new Point2D(0, 0);
					pointOriginal.setX(doubleValue);
					dataBeanList.get(row).setPointOriginal(pointOriginal);
				}
				break;
			case COLUMN_OriginalY:
				point = dataBeanList.get(row).getPointOriginal();
				if (point != null) {
					point.setY(doubleValue);
				} else {
					Point2D pointOriginal = new Point2D(0, 0);
					pointOriginal.setY(doubleValue);
					dataBeanList.get(row).setPointOriginal(pointOriginal);
				}
				break;
			case COLUMN_ReferX:
				point = dataBeanList.get(row).getPointRefer();
				if (point != null) {
					point.setX(doubleValue);
				} else {
					Point2D pointOriginal = new Point2D(0, 0);
					pointOriginal.setX(doubleValue);
					dataBeanList.get(row).setPointRefer(pointOriginal);
				}
				break;
			case COLUMN_ReferY:
				point = dataBeanList.get(row).getPointRefer();
				if (point != null) {
					point.setY(doubleValue);
				} else {
					Point2D pointOriginal = new Point2D(0, 0);
					pointOriginal.setY(doubleValue);
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

	public void addPoint(FormTransformationSubFormType subFormType, Point2D point) {
		if (subFormType == FormTransformationSubFormType.Target) {
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
			dataBeanList.add(bean);
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
			dataBeanList.add(bean);
		}
		fireTableRowsInserted(dataBeanList.size() - 1, dataBeanList.size() - 1);
	}

	public void removePoint(int row, FormTransformationSubFormType subFormType) {
		if (subFormType == FormTransformationSubFormType.Target) {
			dataBeanList.get(row).setPointOriginal(null);
			fireTableCellUpdated(row, COLUMN_OriginalX);
//			fireTableCellUpdated(row,COLUMN_OriginalY);
		} else if (subFormType == FormTransformationSubFormType.Reference) {
			dataBeanList.get(row).setPointRefer(null);
			fireTableCellUpdated(row, COLUMN_ReferX);
//			fireTableCellUpdated(row, COLUMN_ReferY);
		}
	}

	public int getPointCount(FormTransformationSubFormType subFormTypeByForm) {
		int count = 0;
		if (subFormTypeByForm == FormTransformationSubFormType.Target) {
			for (TransformationTableDataBean bean : dataBeanList) {
				if (bean.getPointOriginal() != null) {
					count++;
				}
			}
		} else {
			for (TransformationTableDataBean bean : dataBeanList) {
				if (bean.getPointRefer() != null) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == COLUMN_IS_SELECTED) {
			return Boolean.class;
		} else if (columnIndex == COLUMN_INDEX) {
			return Integer.class;
		}
		return Double.class;
	}

	public Point2D getOriginalPoint(int row) {
		return dataBeanList.get(row).getPointOriginal();
	}

	public Point2D getReferPoint(int row) {
		return dataBeanList.get(row).getPointRefer();
	}

	public void setResidualX(int i, double value) {
		dataBeanList.get(i).setResidualX(value);
		fireTableCellUpdated(i, COLUMN_ResidualX);
	}

	public void setResidualY(int i, double value) {
		dataBeanList.get(i).setResidualY(value);
		fireTableCellUpdated(i, COLUMN_ResidualY);
	}

	public void setResidualTotal(int i, double value) {
		dataBeanList.get(i).setResidualTotal(value);
		fireTableCellUpdated(i, COLUMN_ResidualTotal);
	}
}
