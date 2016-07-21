package com.supermap.desktop.dialog;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Vector;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.JTableHeader;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.http.*;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.*;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.desktop.ui.docking.*;
import com.supermap.desktop.ui.docking.event.WindowClosingEvent;
import com.supermap.desktop.utilities.*;

public class FormLBSControl extends FormBaseChild implements IFormLBSControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_INDEX_Permission = 5;
	private static final int COLUMN_INDEX_Owner = 3;
	private static final int COLUMN_INDEX_Group = 4;
	private static final int COLUMN_INDEX_Size = 1;
	private static final int COLUMN_INDEX_Replication = 6;
	private static final int COLUMN_INDEX_BlockSize = 2;
	private static final int COLUMN_INDEX_Name = 0;
	private static final int ROW_HEADER_WIDTH = 70;

	private JPopupMenu contextPopuMenu;
	private JLabel labelServerURL;
	private JTextField textServerURL;
	private JList rowHeader;
	private JScrollPane scrollPaneFormLBSControl;
	private JTable table;
	private Boolean isOutputFolder = false;
	private KeyListener textServerURLKeyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				buttonBrowserActionPerformed();
			}
		}
	};
	private MouseAdapter tableMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			table_MouseClicked(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			table_MouseReleased(e);
		}
	};

	public FormLBSControl(String title, Icon icon, Component component) {
		super(title, icon, component);
		initializeComponents();
		registEvents();
		this.setTitle(("Browse Directory"));
	}

	public FormLBSControl(String title) {
		this(title, null, null);
	}

	public FormLBSControl() {
		this("");
	}

	private void registEvents() {
		removeEvents();
		this.textServerURL.addKeyListener(this.textServerURLKeyListener);
		this.table.addMouseListener(this.tableMouseListener);
		this.addListener(new DockingWindowAdapter() {
			@Override
			public void windowClosing(WindowClosingEvent evt) throws OperationAbortedException {
				if (evt.getSource().equals(FormLBSControl.this)) {
					removeListener(this);
					removeEvents();
				}
			}
		});
	}

	private void removeEvents() {
		this.textServerURL.removeKeyListener(this.textServerURLKeyListener);
		this.table.removeMouseListener(this.tableMouseListener);
	}

	public void initializeComponents() {
		this.setSize(900, 600);
		this.scrollPaneFormLBSControl = new JScrollPane();
		this.labelServerURL = new JLabel();
		this.textServerURL = new JTextField(WebHDFS.webURL);
		this.table = new JTable();
		HDFSTableModel tableModel = new HDFSTableModel();
		this.table.setModel(tableModel);
		this.table.putClientProperty("terminateEditOnFocusLost", true);
		this.table.setRowHeight(23);
		ListModel<?> listModel = new LeftTableHeaderListModel(table);
		this.rowHeader = new JList(listModel);
		this.rowHeader.setFixedCellWidth(ROW_HEADER_WIDTH);
		this.rowHeader.setFixedCellHeight(table.getRowHeight());
		this.rowHeader.setCellRenderer(new RowHeaderRenderer(table));
		this.scrollPaneFormLBSControl.setRowHeaderView(rowHeader);
		if (Application.getActiveApplication().getMainFrame() != null) {
			IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
			this.contextPopuMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LBSControlManager.ContextMenuLBSControl");
		}
		initializeLayout();
		listDirectory(this.textServerURL.getText(), "", this.getIsOutputFolder());
	}

	private void initializeLayout() {
		GroupLayout gLayout = new GroupLayout(this);
		gLayout.setAutoCreateContainerGaps(true);
		gLayout.setAutoCreateGaps(true);
		// @formatter:off
		gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(gLayout.createSequentialGroup().addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addComponent(scrollPaneFormLBSControl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		gLayout.setVerticalGroup(gLayout.createSequentialGroup()
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPaneFormLBSControl, 100, 200, Short.MAX_VALUE));
		scrollPaneFormLBSControl.setViewportView(table);
		this.setLayout(gLayout);
		// @formatter:on
	}

	/**
	 * 添加一行记录
	 *
	 * @param rowIndex
	 * 
	 */
	private void addFileInfo(WebHDFS.HDFSDefine hdfsDefine) {
		try {
			HDFSTableModel tableModel = (HDFSTableModel) this.table.getModel();
			tableModel.addRow(hdfsDefine);
			this.table.updateUI();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void table_MouseClicked(MouseEvent e) {
		//
		if (table.getSelectedRow() != -1 && e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
			HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
			if (define != null) {
				// if mouse double click foler, list folder files
				if (define.isDir()) {
					String name = (String) this.table.getModel().getValueAt(table.getSelectedRow(), COLUMN_INDEX_Name);
					String url = this.listDirectory(this.textServerURL.getText(), name, this.getIsOutputFolder());
					this.textServerURL.setText(url);
				} else {
					this.buttonOKActionPerformed();
				}
			}
		} else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
			showContextMenu(e);
		}
	}

	private void showContextMenu(MouseEvent e) {
		this.contextPopuMenu.show(table, e.getPoint().x, e.getPoint().y);
	}

	public void table_MouseReleased(MouseEvent e) {

	}

	private String listDirectory(String urlPath, String childFolder, Boolean isFolderOnly) {
		// 删除后设置第0行被选中
		if (0 < table.getRowCount()) {
			HDFSTableModel tableModel = (HDFSTableModel) table.getModel();
			tableModel.removeRows(0, tableModel.getRowCount());
			table.updateUI();
		}

		if (!"".equals(childFolder)) {
			if (!childFolder.endsWith("/")) {
				childFolder += "/";
			}

			if (childFolder.startsWith("/")) {
				childFolder.substring(1, childFolder.length() - 1);
			}

			urlPath += childFolder;
		}

		WebHDFS.HDFSDefine[] defines = WebHDFS.listDirectory(urlPath, "", isFolderOnly);
		for (WebHDFS.HDFSDefine define : defines) {
			this.addFileInfo(define);
		}

		if (0 < table.getRowCount()) {
			table.setRowSelectionInterval(0, 0);
		}

		return urlPath;
	}

	/**
	 * 浏览按钮点击事件
	 */
	private void buttonBrowserActionPerformed() {
		try {
			listDirectory(this.textServerURL.getText(), "", this.getIsOutputFolder());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

	/**
	 * 确定按钮点击事件
	 */
	private void buttonOKActionPerformed() {
		try {
			if (this.textServerURL.isFocusOwner()) {
				return;
			}
			Boolean fileSelected = false;
			if (table.getSelectedRow() != -1) {
				HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
				if (define != null) {
					String root = this.textServerURL.getText();
					if (!root.endsWith("/")) {
						root += "/";
					}

					if (define.isDir()) {
						if (this.getIsOutputFolder()) {
							WebHDFS.outputURL = root + define.getName() + "/";
						} else {
							WebHDFS.webFile = "";
							WebHDFS.webURL = root + define.getName() + "/";
						}
					} else {
						WebHDFS.webURL = this.textServerURL.getText();
						if (define.getName().endsWith(".idx")) {
							WebHDFS.webFile = "";
						}
					}
					fileSelected = true;
				}
			}

			if (!fileSelected) {
				UICommonToolkit.showMessageDialog("please select a file");
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

	public Boolean getIsOutputFolder() {
		return isOutputFolder;
	}

	public void setIsOutputFolder(Boolean isOutputFolder) {
		this.isOutputFolder = isOutputFolder;
	}

	/**
	 * describe a HDFS file DataModel
	 *
	 * @author
	 */
	private class HDFSTableModel extends MutiTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private String[] title = new String[]{"Name","Size","BlockSize","Owner","Group","Permission","Replication"};
//		private String[] title = new String[] { "Permission", "Owner", "Group", "Size", "Replication", "BlockSize", "Name" };

		/**
		 * 构造函数。
		 * 
		 * @param columnNames
		 */
		public HDFSTableModel() {
		}

		@Override
		public String getColumnName(int column) {
			return title[column];
		}

		@Override
		public int getColumnCount() {
			return title.length;
		}

		/**
		 * 添加指定数据的一行。<br>
		 * 
		 * @param define
		 *            　数据
		 * @throws Exception
		 *             抛出数据数不正确的异常
		 */
		public void addRow(WebHDFS.HDFSDefine define) {
			if (null == define) {
				return;
			}

			// 初始化内容存储
			Vector<Object> content = new Vector<Object>(this.columnNames.size());
			content.add(define.getName());
			content.add(define.getSize());
			content.add(define.getBlockSize());
			content.add(define.getOwner());
			content.add(define.getGroup());
			content.add(define.getPermission());
			content.add(define.getReplication());
			

			// 追加内容
			contents.add(content);
			this.setRowTagAt(define, this.getRowCount() - 1);
		}
	}

	class LeftTableHeaderListModel extends AbstractListModel {
		private static final long serialVersionUID = 1L;

		JTable table;

		public LeftTableHeaderListModel(JTable table) {
			super();
			this.table = table;
		}

		@Override
		public int getSize() {
			return table.getRowCount();
		}

		@Override
		public Object getElementAt(int index) {
			return index;
		}
	}

	/**
	 * 自定义JTable头部渲染器
	 * 
	 * @author
	 *
	 */
	public class RowHeaderRenderer extends JLabel implements ListCellRenderer {
		JTable table;

		RowHeaderRenderer(JTable table) {
			this.table = table;
			JTableHeader header = table.getTableHeader();
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			this.setText(String.valueOf(index + 1));
			this.setPreferredSize(new Dimension(100, 50));
			return this;
		}

	}

	@Override
	public int getSelectRow() {
		return table.getSelectedRow();
	}

	@Override
	public int[] getSelectRows() {
		return table.getSelectedRows();
	}

	@Override
	public String getURL() {
		return this.textServerURL.getText();
	}

	@Override
	public JTable getTable() {
		return this.table;
	}

	@Override
	public void delete() {
		// 从hdfs文件系统中删除文件
		try {
			try {
				Boolean fileSelected = false;
				String webFile = "";
				String webURL = "";

				if (table.getSelectedRowCount() == 1) {
					HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
					if (define != null) {
						webFile = define.getName();
						webURL = this.textServerURL.getText();

						if (define.isDir()) {
							if (UICommonToolkit.showConfirmDialog(MessageFormat.format(LBSClientProperties.getString("String_DeleteDir"), webFile)) == JOptionPane.OK_OPTION) {
								fileSelected = true;
							}
						} else {
							if (UICommonToolkit.showConfirmDialog(MessageFormat.format(LBSClientProperties.getString("String_DeleteFile"), webFile)) == JOptionPane.OK_OPTION) {
								fileSelected = true;
							}
						}
						if (fileSelected) {
							DeleteFile deleteFile = new DeleteFile(webURL, webFile);
							deleteFile.start();
						}
					}
				} else if (table.getSelectedRowCount() > 1) {
					int[] indexs = table.getSelectedRows();
					if (UICommonToolkit.showConfirmDialog(MessageFormat.format(LBSClientProperties.getString("String_DeleteFile"), indexs.length)) == JOptionPane.OK_OPTION) {
						for (int index : indexs) {
							HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(index);
							if (define != null) {
								webFile = define.getName();
								webURL = this.textServerURL.getText();
								DeleteFile deleteFile = new DeleteFile(webURL, webFile);
								deleteFile.start();
							}
						}
					}
				} else {
				}

				if (!fileSelected) {
					UICommonToolkit.showMessageDialog("please select a file to delete");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

	@Override
	public void downLoad() {
		// 下载文件
		try {
			Boolean fileSelected = false;
			if (table.getSelectedRow() != -1) {
				HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
				if (define != null && !define.isDir()) {
					fileSelected = true;
					// show save file dialog
					JDialogFileSaveAs dialogFileSaveAs = new JDialogFileSaveAs();
					dialogFileSaveAs.setWebURL(this.textServerURL.getText());
					dialogFileSaveAs.setWebFile(define.getName());
					dialogFileSaveAs.setFileSize(Long.parseLong(define.getSize()));
					dialogFileSaveAs.setLocalPath("F:/temp/" + define.getName());
					dialogFileSaveAs.showDialog();
				}
			}

			if (!fileSelected) {
				UICommonToolkit.showMessageDialog("please select a file");
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}
}
