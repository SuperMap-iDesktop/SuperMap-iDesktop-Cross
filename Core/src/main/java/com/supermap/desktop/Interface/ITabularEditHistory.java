package com.supermap.desktop.Interface;

import com.supermap.desktop.beans.EditHistoryBean;

/**
 * @author XiaJT
 */
public interface ITabularEditHistory {
	void addEditHistoryBean(EditHistoryBean editHistoryBean);

	void redo(IFormTabular tabular);

	void undo(IFormTabular tabular);

	int getCount();
}
