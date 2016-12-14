package com.supermap.desktop.Interface;

/**
 * @author XiaJT
 */
public interface ITabularEditHistoryManager {
	void start();

	void stop();

	void clear();

	void redo();

	void undo();

	boolean canRedo();

	boolean canUndo();

	void startMultiRecord();

	void endMultiRecord();

	void addEditHistory(ITabularEditHistory tabularEditHistory);
}
