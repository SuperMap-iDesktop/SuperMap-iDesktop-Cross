package com.supermap.desktop.utilties;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/23.
 */
public class TabularCache {

	private int cacheSize = 30000;
	private int currentSize;
	private HashMap<Integer, TabularValueItem> tabularValuesMap;
	private TabularValueItem firstTabularValue;
	private TabularValueItem lastTabularValue;

	public TabularCache() {
		this.currentSize = 0;
		tabularValuesMap = new HashMap<>();
	}

	/**
	 * 根据key得到结果，不存在时返回null
	 *
	 * @param key key
	 * @return
	 */
	public Object getValue(int key) {
		Object result = null;
		TabularValueItem value = tabularValuesMap.get(key);
		if (value != null) {
			result = value.getValue();
			moveToLast(value);
		}
		return result;
	}

	/**
	 * 更新缓存
	 *
	 * @param key   key
	 * @param value value
	 */
	public void updateValue(int key, Object value) {

		long start = System.currentTimeMillis();
		TabularValueItem valueItem = tabularValuesMap.get(key);
		if (valueItem != null) {
			// 修改
			valueItem.setValue(value);
			moveToLast(valueItem);
		} else {
			// 添加
			if (currentSize < cacheSize) {
				valueItem = new TabularValueItem();
				valueItem.setKey(key);
				valueItem.setValue(value);
				if (lastTabularValue != null) {
					valueItem.setPerItem(lastTabularValue);
					lastTabularValue.setNextItem(valueItem);
				}
				if (firstTabularValue == null) {
					firstTabularValue = valueItem;
				}
				lastTabularValue = valueItem;
				tabularValuesMap.put(key, valueItem);
			} else {
				// 超过范围，直接修改第一个对象
				tabularValuesMap.remove(firstTabularValue.getKey());
				firstTabularValue.setKey(key);
				firstTabularValue.setValue(value);
				tabularValuesMap.put(key, firstTabularValue);
				moveToLast(firstTabularValue);
			}
		}
	}

	private void moveToLast(TabularValueItem value) {
		if (lastTabularValue != value) {
			// 不是最后一个时移到最后位置
			TabularValueItem perItem = value.getPerItem();
			TabularValueItem nextItem = value.getNextItem();
			if (perItem != null) {
				// 中间移到最后
				perItem.setNextItem(nextItem);
				nextItem.setPerItem(perItem);
			} else {
				// 第一个移动到最后
				nextItem.setPerItem(null);// 前面有判断，不为最后一个，所以不需要判断下一个是否为空
				firstTabularValue = firstTabularValue.getNextItem();
			}
			value.setPerItem(this.lastTabularValue);
			this.lastTabularValue.setNextItem(value);
			lastTabularValue = value;
		}
	}


	public void setCacheSize(int size) {
		// 如果当前的大小已经超过的设置的大小不处理
		// 既然已经开了那么大空间就不浪费了
		this.cacheSize = size;

	}

	private class TabularValueItem {
		private TabularValueItem perItem;
		private TabularValueItem nextItem;
		private Object value;
		private int key;

		public TabularValueItem() {
			this.perItem = null;
			this.nextItem = null;
			this.value = null;
			this.key = -1;
		}

		public TabularValueItem getPerItem() {
			return perItem;
		}

		public void setPerItem(TabularValueItem perItem) {
			this.perItem = perItem;
		}

		public TabularValueItem getNextItem() {
			return nextItem;
		}

		public void setNextItem(TabularValueItem nextItem) {
			this.nextItem = nextItem;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}
	}
}
