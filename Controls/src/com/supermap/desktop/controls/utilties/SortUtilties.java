package com.supermap.desktop.controls.utilties;

import com.supermap.data.Colors;
import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.ui.controls.DataCell;

import java.awt.*;
import java.util.Date;

/**
 * @author XiaJt
 */
public class SortUtilties {
	private SortUtilties() {

	}

	/**
	 * 数组排序
	 *
	 * @param sort 需要排序的数组
	 */
	public static <T> void sortList(T[] sort) {
		quickSort(sort, 0, sort.length - 1);
	}

	private static void quickSort(Object[] array, int beg, int end) {
		if (beg >= end || array == null)
			return;
		int p = partition(array, beg, end);
		quickSort(array, beg, p - 1);
		quickSort(array, p + 1, end);
	}

	private static int partition(Object[] array, int beg, int end) {
		Object last = array[end];
		int i = beg - 1;
		Object temp;
		for (int j = beg; j <= end - 1; j++) {
			if (compareObject(array[j], last) <= 0) {
				i++;
				if (i != j) {
					temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
		if ((i + 1) != end) {
			temp = array[i + 1];
			array[i + 1] = array[end];
			array[end] = temp;
		}
		return i + 1;
	}

	/**
	 * 比较2个对象
	 *
	 * @param o1 对象1
	 * @param o2 对象2
	 * @return 负 对象2>对象1；0 相等；正 对象1大于对象2
	 */
	public static int compareObject(Object o1, Object o2) {
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
			} else if (o1 instanceof Colors) {
				return compare((Colors) o1, (Colors) o2);
			} else {
				return String.valueOf(o1).compareTo(String.valueOf(o2));
			}
		}
	}

	private static int compare(Colors o1, Colors o2) {
		for (int i = 0; i < o1.getCount() && i < o2.getCount(); i++) {
			if (compare(o1.get(i), o2.get(i)) != 0) {
				return compare(o1.get(i), o2.get(i));
			}
		}
		if (o1.getCount() > o2.getCount()) {
			return 1;
		}
		if (o1.getCount() < o2.getCount()) {
			return -1;
		}
		return 0;
	}

	private static int compare(Color color, Color color1) {
		if (color.getRed() != color1.getRed()) {
			return color.getRed() - color1.getRed();
		}
		if (color.getGreen() != color1.getGreen()) {
			return color.getGreen() - color1.getGreen();
		}
		if (color.getBlue() != color1.getBlue()) {
			return color.getBlue() - color1.getBlue();
		}
		if (color.getAlpha() != color1.getAlpha()) {
			return color.getAlpha() - color1.getAlpha();
		}
		return 0;
	}

	private static int compare(DataCell o1, DataCell o2) {
		if (o1.getData() == null) {
			return String.valueOf(o1).compareTo(String.valueOf(o2));
		} else if (o1.getData() instanceof Datasource) {
			return compare(((Datasource) o1.getData()), ((Datasource) o2.getData()));
		} else if (o1.getData() instanceof Dataset) {
			return compare(((Dataset) o1.getData()), ((Dataset) o2.getData()));
		}
		return 0;
	}

	private static int compare(Datasource data, Datasource data1) {
		if (data.getEngineType() != data1.getEngineType()) {
			return data.getEngineType().value() - data1.getEngineType().value();
		} else {
			return data.getAlias().compareTo(data1.getAlias());
		}
	}

	private static int compare(Dataset data, Dataset data1) {
		if (data.getType() != data1.getType()) {
			return data1.getType().value() - data.getType().value();
		} else {
			return data.getName().compareTo(data1.getName());
		}
	}

	private static int compare(Number o1, Number o2) {
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

	private static int compare(Date o1, Date o2) {
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

	private static int compare(Boolean b1, Boolean b2) {
		if (b1 == b2) {
			return 0;
		} else if (b1) {
			return 1;
		} else {
			return -1;
		}
	}
}
