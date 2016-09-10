package com.supermap.desktop.ui.controls.datasetChoose;

import java.util.List;

/**
 * @author XiaJT
 */
public interface IDatasetChoose {

	void initTable();

	void initializeTableInfo(Object currentNode);

	List<Object> getSelectedValues(int[] selectedRows);

	void dispose();
}
