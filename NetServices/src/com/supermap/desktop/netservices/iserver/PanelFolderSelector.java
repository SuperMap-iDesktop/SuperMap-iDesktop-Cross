package com.supermap.desktop.netservices.iserver;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.supermap.desktop.Application;
import com.supermap.desktop.core.FileSize;
import com.supermap.desktop.core.FileSizeType;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.StringUtilties;

public class PanelFolderSelector extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new PanelFolderSelector());
		frame.setVisible(true);
	}

	private static final int CONTENT_FILE = 1;
	private static final int CONTENT_DIRECTORY = 2;
	private static final int CONTENT_FILE_AND_DIRECTORY = 3;

	private int displayContent = CONTENT_FILE_AND_DIRECTORY;
	private String rootDirectory;
	private ArrayList<String> selectedDirectories;
	private ArrayList<String> selectedFiles;

	/**
	 * 显示隐藏文件。true，在列表中列出，并以 是/否 显示；false，过滤掉，并且不在列表中显示。
	 */
	private boolean isShowHidden = true;

	private JTable table;

	private PanelFolderSelector() {
		initializeComponent();
		this.selectedDirectories = new ArrayList<String>();
		this.selectedFiles = new ArrayList<String>();
	}

	public PanelFolderSelector(String rootDirectory) {
		this();
		initializeSelector(rootDirectory);
	}

	public PanelFolderSelector(String rootDirectory, ArrayList<String> selectedDirectories, ArrayList<String> selectedFiles) {
		this();
		initializeSelector(rootDirectory, selectedDirectories, selectedFiles);
	}

	public boolean isShowHidden() {
		return isShowHidden;
	}

	public void setShowHidden(boolean isShowHidden) {
		this.isShowHidden = isShowHidden;
		((FolderSelectorTableModel) this.table.getModel()).setShowHidden(isShowHidden);
	}

	public ArrayList<String> getSelectedDirectories() {
		return selectedDirectories;
	}

	public ArrayList<String> getSelectedFiles() {
		return selectedFiles;
	}

	void initializeSelector(String rootDirectory) {
		this.rootDirectory = rootDirectory;
		this.selectedDirectories.clear();
		this.selectedFiles.clear();

	}

	void initializeSelector(String rootDirectory, ArrayList<String> selectedDirectories, ArrayList<String> selectedFiles) {
		this.rootDirectory = rootDirectory;
		this.selectedDirectories.clear();
		if (selectedDirectories != null) {
			this.selectedDirectories.addAll(selectedDirectories);
		}
		this.selectedFiles.clear();
		if (selectedFiles != null) {
			this.selectedFiles.addAll(selectedFiles);
		}
	}

	private void initializeComponent() {
		this.table = new JTable();
		FolderSelectorTableModel tableModel = new FolderSelectorTableModel();
		this.table.setModel(tableModel);
	}

	/**
	 * 更新 TableModel 的数据
	 * 
	 * @param rootFile
	 */
	private void updateTableModel(File rootFile) {
		if (rootFile == null || !rootFile.exists() || !rootFile.isDirectory()) {
			return;
		}

		ArrayList<File> modelData = new ArrayList<>(); // 用来初始化 Model 的集合
		File[] subFiles = rootFile.listFiles(new DirectoryFilter(CONTENT_FILE)); // 获取根目录下的所有文件
		File[] subDirectories = rootFile.listFiles(new DirectoryFilter(CONTENT_DIRECTORY)); // 获取根目录下的所有目录

		if (this.displayContent == CONTENT_FILE) {
			ListUtilties.addArray(modelData, subFiles);
		} else if (this.displayContent == CONTENT_DIRECTORY) {
			ListUtilties.addArray(modelData, subDirectories);
		} else if (this.displayContent == CONTENT_FILE_AND_DIRECTORY) {
			ListUtilties.addArray(modelData, subFiles);
			ListUtilties.addArray(modelData, subDirectories);
		}
		((FolderSelectorTableModel) this.table.getModel()).setFiles(modelData);
	}

	private class DirectoryFilter implements FileFilter {

		/**
		 * 可以返回的文件类型 1 -- 文件，2 -- 目录，其他 -- 文件和目录
		 */
		private int acceptType = PanelFolderSelector.CONTENT_FILE_AND_DIRECTORY;

		public DirectoryFilter(int acceptType) {
			this.acceptType = acceptType;
		}

		@Override
		public boolean accept(File pathname) {
			boolean accept = false;

			if (this.acceptType == PanelFolderSelector.CONTENT_FILE) {
				if (pathname.exists() && !pathname.isDirectory()) {
					accept = true;
				}
			} else if (this.acceptType == PanelFolderSelector.CONTENT_DIRECTORY) {
				if (pathname.exists() && pathname.isDirectory()) {
					accept = true;
				}
			}
			return accept;
		}

		public int getAcceptType() {
			return this.acceptType;
		}

		public void setAcceptType(int acceptType) {
			this.acceptType = acceptType;
		}
	}

	private class FolderSelectorTableModel extends AbstractTableModel {

		public static final int SELECTED = 0;
		public static final int NAME = 1; // 名称列
		public static final int SIZE = 2; // 文件大小列
		public static final int LASTMODIFIED = 3; // 最后修改时间列
		public static final int HIDEN = 4; // 是否显示隐藏列

		private boolean isShowHidden = true;
		private ArrayList<File> files = new ArrayList<>();
		private ArrayList<Boolean> isSelectedValues = new ArrayList<>();

		public FolderSelectorTableModel() {

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

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			try {

			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
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

		public void clear() {
			this.files.clear();
			this.isSelectedValues.clear();
			fireTableDataChanged();
		}

		public void setFiles(ArrayList<File> files) {
			this.files.addAll(files);
			for (int i = 0; i < this.files.size(); i++) {
				this.isSelectedValues.add(false);
			}
			fireTableDataChanged();
		}
	}
}
