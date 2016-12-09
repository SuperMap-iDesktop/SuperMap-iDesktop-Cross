package com.supermap.desktop.editHistory;


import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class TableEditHistory {
	private List<EditHistoryBean> editHistoryBeanJList = new ArrayList<>();

	public void addEditHistoryBean(EditHistoryBean editHistoryBean) {
		editHistoryBeanJList.add(editHistoryBean);
	}

	public void redo() {

	}

	public void undo() {

	}
}
