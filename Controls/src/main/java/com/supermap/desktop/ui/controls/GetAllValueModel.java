package com.supermap.desktop.ui.controls;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author XiaJT
 */
public class GetAllValueModel extends AbstractListModel {

	private ArrayList<Object> datas = new ArrayList<>();

	public GetAllValueModel() {

	}

	public GetAllValueModel(Object... datas) {
		Collections.addAll(this.datas, datas);
	}

	@Override
	public int getSize() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getElementAt(int index) {
		return datas.get(index);
	}

	public void setDatas(Object... datas) {
		removeAll();
		Collections.addAll(this.datas, datas);
		int newSize = this.datas.size();
		fireIntervalAdded(this, 0, newSize);
	}

	public void removeAll() {
		int size = this.datas.size();
		if (size != 0) {
			this.datas.clear();
			fireIntervalRemoved(this, 0, size);
		}
	}

}
