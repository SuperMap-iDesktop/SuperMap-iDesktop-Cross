package com.supermap.desktop.ui.controls.SortTable;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.ui.controls.DataCell;

import java.util.Date;
import java.util.HashMap;

/**
 * 表排序类，根据类型匹配对应的函数
 */
public class TableSorter {
	private SortableTableModel model;

	public TableSorter(SortableTableModel model) {
		this.model = model;
	}


	//n2 selection
	public void sort(int column, boolean isAscent) {
		int[] rows = model.getSortRows();
		HashMap indexes = model.getSortIndexes();

		int row1;
		int row2;
		for (int i = 0; i < rows.length - 1; i++) {
			row1 = rows[i];
			int k = row1;
			for (int j = i + 1; j < rows.length; j++) {
				row2 = rows[j];
				if (isAscent) {
					if (compare(column, k, row2) < 0) {
						k = row2;
					}
				} else {
					if (compare(column, k, row2) > 0) {
						k = row2;
					}
				}
			}
			if (k != row1) {
				int tmp = (int) indexes.get(row1);
				indexes.put(row1, indexes.get(k));
				indexes.put(k, tmp);
			}
		}
	}


	// comparaters

	private int compare(int column, int row1, int row2) {
		Object o1 = model.getValueAt(row1, column);
		Object o2 = model.getValueAt(row2, column);
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		} else {
			if (o1 instanceof Number) {
				return compare((Number) o1, (Number) o2);
			} else if (o1 instanceof String) {
				return ((String) o1).compareTo((String) o2);
			} else if (o1 instanceof Date) {
				return compare((Date) o1, (Date) o2);
			} else if (o1 instanceof Boolean) {
				return compare((Boolean) o1, (Boolean) o2);
			} else if (o1 instanceof DataCell) {
				return compare((DataCell) o1, (DataCell) o2);
			} else if (o1 instanceof Dataset) {
				return compare((Dataset) o1, (Dataset) o2);
			} else if (o1 instanceof Datasource) {
				return compare((Datasource) o1, (Datasource) o2);
			} else {
				return String.valueOf(o1).compareTo(String.valueOf(o2));
			}
		}
	}

	private int compare(DataCell o1, DataCell o2) {
		if (o1.getData() == null) {
			return String.valueOf(o1).compareTo(String.valueOf(o2));
		} else if (o1.getData() instanceof Datasource) {
			return compare(((Datasource) o1.getData()), ((Datasource) o2.getData()));
		} else if (o1.getData() instanceof Dataset) {
			return compare(((Dataset) o1.getData()), ((Dataset) o2.getData()));
		}
		return 0;
	}

	private int compare(Datasource data, Datasource data1) {
		if (data.getEngineType() != data1.getEngineType()) {
			return data.getEngineType().value() - data1.getEngineType().value();
		} else {
			return data.getAlias().compareTo(data1.getAlias());
		}
	}

	private int compare(Dataset data, Dataset data1) {
		if (data.getType() != data1.getType()) {
			return data.getType().value() - data1.getType().value();
		} else {
			return data.getName().compareTo(data1.getName());
		}
	}

	private int compare(Number o1, Number o2) {
		double n1 = o1.doubleValue();
		double n2 = o2.doubleValue();
		if (n1 < n2) {
			return -1;
		} else if (n1 > n2) {
			return 1;
		} else {
			return 0;
		}
	}

	private int compare(Date o1, Date o2) {
		long n1 = o1.getTime();
		long n2 = o2.getTime();
		if (n1 < n2) {
			return -1;
		} else if (n1 > n2) {
			return 1;
		} else {
			return 0;
		}
	}

	private int compare(Boolean b1, Boolean b2) {
		if (b1 == b2) {
			return 0;
		} else if (b1) {
			return 1;
		} else {
			return -1;
		}
	}

}