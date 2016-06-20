package com.supermap.desktop.ui.controls.comboBox;

/**
 * @author XiaJT
 */
public class SearchItemValueGetter<T> {
	/**
	 * 获得搜索时的字符串
	 *
	 * @param item 搜索的对象
	 * @return 搜索对象参与搜索的信息
	 */
	public String getSearchString(T item) {
		return item.toString();
	}
}
