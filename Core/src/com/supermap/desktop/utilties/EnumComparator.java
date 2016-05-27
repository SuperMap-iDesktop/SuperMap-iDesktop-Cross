package com.supermap.desktop.utilties;

import com.supermap.data.Enum;

import java.util.Comparator;

/**
 * @author XiaJT
 */
public class EnumComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		return ((Enum) o1).name().compareTo(((Enum) o2).name());
	}
}
