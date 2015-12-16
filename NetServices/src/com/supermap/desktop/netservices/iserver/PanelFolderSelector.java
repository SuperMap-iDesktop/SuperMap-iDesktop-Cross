package com.supermap.desktop.netservices.iserver;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.supermap.desktop.Application;
import com.supermap.desktop.core.FileSize;
import com.supermap.desktop.core.FileSizeType;

public class PanelFolderSelector extends JPanel {
	private static final int CONTENT_NONE = 0;
	private static final int CONTENT_FILE = 1;
	private static final int CONTENT_DIRECTORY = 2;
	private static final int CONTENT_FILE_AND_DIRECTORY = 3;

	private int displayContent = CONTENT_FILE_AND_DIRECTORY;
	private String rootDirectory;
	private ArrayList<String> selectedDirectories;
	private ArrayList<String> selectedFiles;
	private ArrayList<String> hideExtensions;
	private boolean isInitial = false;

	/**
	 * 显示隐藏文件。true，在列表中列出，并以 是/否 显示；false，过滤掉，并且不在列表中显示。
	 */
	private boolean isShowHide = true;

	private JTable table;

	public PanelFolderSelector() {
		initializeComponent();
		// initializeListView();
		this.hideExtensions = new ArrayList<String>();
		this.selectedDirectories = new ArrayList<String>();
		this.selectedFiles = new ArrayList<String>();
	}

	public PanelFolderSelector(String rootDirectory) {
		this();
		InitializeSelector(rootDirectory);
	}

	public PanelFolderSelector(String rootDirectory, ArrayList<String> selectedDirectories, ArrayList<String> selectedFiles) {
		this();
		initializeSelector(rootDirectory, selectedDirectories, selectedFiles);
	}

	public boolean isShowHide() {
		return isShowHide;
	}

	public void setShowHide(boolean isShowHide) {
		this.isShowHide = isShowHide;
	}

	public ArrayList<String> getSelectedDirectories() {
		return selectedDirectories;
	}

	public ArrayList<String> getSelectedFiles() {
		return selectedFiles;
	}

	public ArrayList<String> getHideExtensions() {
		return hideExtensions;
	}

	void InitializeSelector(String rootDirectory) {
		this.isInitial = false;
		// setIsShowHide();
		this.rootDirectory = rootDirectory;
		this.selectedDirectories.clear();
		this.selectedFiles.clear();
		// fillListView();
		// resizeColumnHeader();
	}

	void initializeSelector(String rootDirectory, ArrayList<String> selectedDirectories, ArrayList<String> selectedFiles) {
		this.isInitial = false;
		// setIsShowHide();
		this.rootDirectory = rootDirectory;
		this.selectedDirectories.clear();
		if (selectedDirectories != null) {
			this.selectedDirectories.addAll(selectedDirectories);
		}
		this.selectedFiles.clear();
		if (selectedFiles != null) {
			this.selectedFiles.addAll(selectedFiles);
		}
		// fillListView();
		// resizeColumnHeader();
	}

	private void initializeComponent() {
		this.table = new JTable();
	}

	private class FolderSelectorTableModel extends AbstractTableModel {

		public static final int SELECTED = 0;
		public static final int NAME = 1; // 名称列
		public static final int SIZE = 2; //
		public static final int LASTMODIFIED = 3;
		public static final int HIDEN = 4;

		private boolean isShowHidden = true;
		private ArrayList<File> files = new ArrayList<>();
		private ArrayList<Boolean> isSelectedValues = new ArrayList<>();

		public FolderSelectorTableModel(ArrayList<File> files) {
			this.files.addAll(files);
			for (int i = 0; i < this.files.size(); i++) {
				this.isSelectedValues.add(false);
			}
		}

		@Override
		public int getRowCount() {
			return this.files.size();
		}

		@Override
		public int getColumnCount() {
			return this.isShowHidden ? 5 : 4;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object result = null;

			try {
				if (rowIndex >= 0 && rowIndex < files.size()) {
					File file = this.files.get(rowIndex);

					if (columnIndex == SELECTED) {
						result = this.isSelectedValues.get(rowIndex);
					} else if (columnIndex == NAME) {
						result = file.getName();
					} else if (columnIndex == SIZE) {
						FileSize fileSize = new FileSize(file.length(), FileSizeType.BYTE);
						result = fileSize.ToStringClever();
					} else if (columnIndex == LASTMODIFIED) {
						result = new Date(file.lastModified());
					} else if (columnIndex == HIDEN && this.isShowHidden) {
						result = file.isHidden();
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			return result;
		}

		/**
		 * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
		 *
		 * @param columnIndex
		 *            the column being queried
		 * @return the Object.class
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return columnIndex == SELECTED ? Boolean.class : Object.class;
		}

		/**
		 * Returns false. This is the default implementation for all cells.
		 *
		 * @param rowIndex
		 *            the row being queried
		 * @param columnIndex
		 *            the column being queried
		 * @return false
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == SELECTED ? true : false;
		}

		public boolean isShowHidden() {
			return isShowHidden;
		}

		public void setShowHidden(boolean isShowHidden) {
			this.isShowHidden = isShowHidden;
			fireTableStructureChanged();
		}

	}
}
