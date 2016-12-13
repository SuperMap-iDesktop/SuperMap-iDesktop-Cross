package com.supermap.desktop.editHistory;

import com.supermap.data.IDisposable;

/**
 * @author XiaJT
 */
public class TableEditHistoryMultiManager implements IDisposable {


	private TabularEditHistoryManager tableEditHistoryManager;

	public TableEditHistoryMultiManager(TabularEditHistoryManager tableEditHistoryManager) {
		this.tableEditHistoryManager = tableEditHistoryManager;
		tableEditHistoryManager.startMultiRecord();
	}

	@Override
	public void dispose() {
		tableEditHistoryManager.endMultiRecord();
	}
}
