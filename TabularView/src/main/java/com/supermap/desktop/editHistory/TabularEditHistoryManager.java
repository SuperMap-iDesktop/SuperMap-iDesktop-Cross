package com.supermap.desktop.editHistory;


import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.Interface.ITabularEditHistory;
import com.supermap.desktop.Interface.ITabularEditHistoryManager;
import com.supermap.desktop.beans.EditHistoryBean;
import com.supermap.desktop.enums.TabularChangedType;
import com.supermap.desktop.event.TabularChangedEvent;
import com.supermap.desktop.event.TabularValueChangedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class TabularEditHistoryManager implements ITabularEditHistoryManager {
	private List<ITabularEditHistory> historyList = new ArrayList<>();
	private int currentIndex = -1;
	private IFormTabular tabular;
	private TabularValueChangedListener tabularValueChangedListener;
	private boolean isMultiRecord = false;
	private ITabularEditHistory tempTabularEditHistory;

	public TabularEditHistoryManager(IFormTabular tabular) {
		this.tabular = tabular;
		tabularValueChangedListener = new TabularValueChangedListener() {
			@Override
			public void valueChanged(TabularChangedEvent tabularChangedEvent) {
				if (tabularChangedEvent.getTabularChangedType() == TabularChangedType.UPDATED) {
					EditHistoryBean editHistoryBean = tabularChangedEvent.getEditHistoryBean();
					if (tempTabularEditHistory == null) {
						tempTabularEditHistory = new TabularEditHistory();
					}
					tempTabularEditHistory.addEditHistoryBean(editHistoryBean);
					if (!isMultiRecord) {
						push();
					}
				}
			}
		};
		tabular.addValueChangedListener(tabularValueChangedListener);
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void clear() {
		historyList.clear();
		currentIndex = -1;
	}

	@Override
	public void redo() {
		currentIndex++;
		historyList.get(currentIndex).redo(tabular);
	}

	@Override
	public void undo() {
		historyList.get(currentIndex).undo(tabular);
		pop();
	}

	@Override
	public boolean canRedo() {
		return currentIndex != historyList.size() - 1;
	}

	@Override
	public boolean canUndo() {
		return currentIndex != -1;
	}

	@Override
	public void startMultiRecord() {
		isMultiRecord = true;
	}

	@Override
	public void endMultiRecord() {
		isMultiRecord = false;
		push();
	}

	@Override
	public void addEditHistory(ITabularEditHistory tabularEditHistory) {
		tempTabularEditHistory = tabularEditHistory;
		push();
	}

	private void push() {
		if (tempTabularEditHistory != null) {
			for (int i = historyList.size() - 1; i > currentIndex; i--) {
				historyList.remove(i);
			}
			historyList.add(tempTabularEditHistory);
			tempTabularEditHistory = null;
			currentIndex = historyList.size() - 1;
		}
	}

	private void pop() {
		currentIndex--;
	}

}
