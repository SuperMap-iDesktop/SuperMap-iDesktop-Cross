package com.supermap.desktop.netservices.iserver;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.DateFormat;
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
	private ArrayList<SelectableFile> files;
	private ArrayList<SelectableFile> selectedFiles;

	/**
	 * 显示隐藏文件。true，在列表中列出，并以 是/否 显示；false，过滤掉，并且不在列表中显示。
	 */
	private boolean isShowHidden = true;

	private JTable table;

	private PanelFolderSelector() {
		initializeComponent();
		this.files = new ArrayList<>();
	}

	public PanelFolderSelector(ArrayList<SelectableFile> files) {
		this();
		updateTableModel(files);
	}

	public boolean isShowHidden() {
		return isShowHidden;
	}

	public void setShowHidden(boolean isShowHidden) {
		this.isShowHidden = isShowHidden;
		((FolderSelectorTableModel) this.table.getModel()).setShowHidden(isShowHidden);
	}

	public ArrayList<SelectableFile> getSelectedFiles() {
		return this.selectedFiles;
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
	private void updateTableModel(ArrayList<SelectableFile> files) {
		if (files == null || files.size() == 0) {
			return;
		}

		ArrayList<SelectableFile> modelData = new ArrayList<>(); // 用来初始化 Model 的集合

		if (this.displayContent == CONTENT_FILE) {
			modelData.addAll(getSingleFiles());
		} else if (this.displayContent == CONTENT_DIRECTORY) {
			modelData.addAll(getDirectories());
		} else if (this.displayContent == CONTENT_FILE_AND_DIRECTORY) {
			modelData.addAll(getSingleFiles());
			modelData.addAll(getDirectories());
		}
		((FolderSelectorTableModel) this.table.getModel()).setFiles(modelData);
	}

	/**
	 * 获取文件集合
	 * 
	 * @return
	 */
	private ArrayList<SelectableFile> getSingleFiles() {
		ArrayList<SelectableFile> singleFiles = new ArrayList<>();

		for (int i = 0; i < this.files.size(); i++) {
			if (!this.files.get(i).isDirectory()) {
				singleFiles.add(this.files.get(i));
			}
		}
		return singleFiles;
	}

	/**
	 * 获取目录集合
	 * 
	 * @return
	 */
	private ArrayList<SelectableFile> getDirectories() {
		ArrayList<SelectableFile> directories = new ArrayList<>();

		for (int i = 0; i < this.files.size(); i++) {
			if (this.files.get(i).isDirectory()) {
				directories.add(this.files.get(i));
			}
		}
		return directories;
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
		private ArrayList<SelectableFile> files;

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
					SelectableFile file = this.files.get(rowIndex);

					if (columnIndex == SELECTED) {
						result = file.isSelected();
					} else if (columnIndex == NAME) {
						result = file.getName();
					} else if (columnIndex == SIZE) {
						FileSize fileSize = new FileSize(file.length(), FileSizeType.BYTE);
						result = fileSize.ToStringClever();
					} else if (columnIndex == LASTMODIFIED) {
						result = new Date(file.lastModified());
						new Date(file.lastModified());
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
				if (columnIndex == SELECTED) {
					SelectableFile file = this.files.get(rowIndex);

					if (aValue == null) {
						file.setIsSelected(false);
					} else {
						file.setIsSelected(Boolean.valueOf(aValue.toString()));
					}
				}
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
			fireTableDataChanged();
		}

		/**
		 * 使用文件集合填充 Model
		 * 
		 * @param files
		 */
		public void setFiles(ArrayList<SelectableFile> files) {
			this.files.addAll(files);
			fireTableDataChanged();
		}
	}
}
