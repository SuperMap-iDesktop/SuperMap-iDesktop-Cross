package com.supermap.desktop.ui.controls.comboBox;

/**
 * @author XiaJT
 */
public class SearchItemValueGetter {
	/**
	 * 获得搜索时的字符串
	 *
	 * @param item 搜索的对象
	 * @return 搜索对象参与搜索的信息
	 */
	public String getSearchString(Object item) {
		return item.toString();
	}
}
