package com.supermap.desktop.dialog;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.http.DeleteFile;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.callable.UploadPropressCallable;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.CursorUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * 下载,上传主界面
 * 
 * @author
 *
 */
public class JDialogHDFSFiles extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_INDEX_Permission = 0;
	private static final int COLUMN_INDEX_Owner = 1;
	private static final int COLUMN_INDEX_Group = 2;
	private static final int COLUMN_INDEX_Size = 3;
	private static final int COLUMN_INDEX_Replication = 4;
	private static final int COLUMN_INDEX_BlockSize = 5;
	private static final int COLUMN_INDEX_Name = 6;

	private JLabel labelServerURL;
	private JTextField textServerURL;
	private JButton buttonBrowser;
	private JTable table;
	private SmButton buttonUpload;
	private SmButton buttonDownload;
	private SmButton buttonDelete;
	private SmButton buttonOK;
	private SmButton buttonCancel;

	private Boolean isOutputFolder = false;
	private KeyListener textServerURLKeyListener;
	private ActionListener buttonBrowserListener;
	private ActionListener buttonUploadListener;
	private ActionListener buttonDownLoadListener;
	private ActionListener buttonDeleteListener;
	private ActionListener buttonOKListener;
	private ActionListener buttonCancelListener;

	/**
	 * Create the dialog.
	 */
	public JDialogHDFSFiles() {
		initializeComponents();
		initializeResources();
		registEvents();
	}

	private void registEvents() {
		this.textServerURLKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					buttonBrowserActionPerformed();
				}
			}
		};

		this.buttonBrowserListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonBrowserActionPerformed();
			}
		};

		this.buttonUploadListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonUploadActionPerformed();
			}
		};

		this.buttonDownLoadListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonDownloadActionPerformed();
			}
		};

		this.buttonDeleteListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonDeleteActionPerformed();
			}
		};

		this.buttonOKListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOKActionPerformed();
			}
		};

		this.buttonCancelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancelActionPerformed();
			}
		};
		removeEvents();
		this.buttonBrowser.addActionListener(this.buttonBrowserListener);
		this.buttonUpload.addActionListener(this.buttonUploadListener);
		this.buttonDownload.addActionListener(this.buttonDownLoadListener);
		this.buttonDelete.addActionListener(this.buttonDeleteListener);
		this.buttonOK.addActionListener(this.buttonOKListener);
		this.buttonCancel.addActionListener(this.buttonCancelListener);
		this.textServerURL.addKeyListener(this.textServerURLKeyListener);
		this.addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosed(WindowEvent e) {
				removeEvents();
			}
			
		});
		
		listDirectory(this.textServerURL.getText(), "", this.getIsOutputFolder());
	}

	private void removeEvents(){
		this.buttonBrowser.removeActionListener(this.buttonBrowserListener);
		this.buttonUpload.removeActionListener(this.buttonUploadListener);
		this.buttonDownload.removeActionListener(this.buttonDownLoadListener);
		this.buttonDelete.removeActionListener(this.buttonDeleteListener);
		this.buttonOK.removeActionListener(this.buttonOKListener);
		this.buttonCancel.removeActionListener(this.buttonCancelListener);
		this.textServerURL.removeKeyListener(this.textServerURLKeyListener);
	}
	
	public void initializeComponents() {
		this.setSize(900, 600);

		this.labelServerURL = new JLabel("服务器地址:");
		this.textServerURL = new JTextField(WebHDFS.webURL);
		this.buttonBrowser = new JButton("浏览");

		this.buttonUpload = new SmButton("上传");
		this.buttonDownload = new SmButton("下载");
		this.buttonOK = new SmButton("选择");
		this.buttonDelete = new SmButton("删除");
		this.buttonCancel = new SmButton(CommonProperties.getString("String_Button_Cancel"));
		this.getRootPane().setDefaultButton(this.buttonOK);

		GroupLayout gLayout = new GroupLayout(this.getContentPane());
		gLayout.setAutoCreateContainerGaps(true);
		gLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(gLayout);

		this.table = new JTable();
		this.table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		HDFSTableModel tableModel = new HDFSTableModel(//
				new String[] { "Permission", "Owner", "Group", "Size", "Replication", "BlockSize", "Name" }) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false };

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		this.table.setModel(tableModel);
		this.table.putClientProperty("terminateEditOnFocusLost", true);

		this.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				table_MouseClicked(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				table_MouseReleased(e);
			}
		});
		this.table.setRowHeight(23);
		JScrollPane scrollPaneTable = new JScrollPane(table);
		// @formatter:off
		gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(gLayout.createSequentialGroup().addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.buttonBrowser, 32, 32, 32))
				.addComponent(scrollPaneTable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonUpload, 75, 75, 75)
						.addComponent(this.buttonDownload, 75, 75, 75)
						.addComponent(this.buttonDelete, 75, 75, 75)
						.addComponent(this.buttonOK, 75, 75, 75)
						.addComponent(this.buttonCancel, 75, 75, 75)));

		gLayout.setVerticalGroup(gLayout.createSequentialGroup()
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonBrowser))
				.addComponent(scrollPaneTable, 100, 200, Short.MAX_VALUE)
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonUpload)
						.addComponent(this.buttonDownload)
						.addComponent(this.buttonDelete)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		this.setLocationRelativeTo(null);
	}

	public DialogResult showDialog() {
		this.setVisible(true);
		return this.dialogResult;
	}

	private void initializeResources() {
		if (table != null) {
			this.setTitle(("Browse Directory"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Close"));
			this.buttonOK.setText(CommonProperties.getString("String_Button_OK"));
			this.buttonOK.setText("选择");

			this.table.getColumnModel().getColumn(COLUMN_INDEX_Permission).setHeaderValue("Permission");
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Owner).setHeaderValue("Owner");
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Group).setHeaderValue(("Group"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Size).setHeaderValue("Size");
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Replication).setHeaderValue("Replication");
			this.table.getColumnModel().getColumn(COLUMN_INDEX_BlockSize).setHeaderValue(("BlockSize"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Name).setHeaderValue("Name");
		}
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

		if (table.getSelectedRow() != -1 && e.getClickCount() == 2) {
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
		}
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
					this.dispose();
					this.dialogResult = DialogResult.OK;

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

	private void buttonUploadActionPerformed() {
		try {
			String modelName = "HDFSFileUpload";
			if (!SmFileChoose.isModuleExist(modelName)) {
				SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), "选择文件", modelName, "OpenOne");
			}
			SmFileChoose smFileChoose = new SmFileChoose(modelName);
			smFileChoose.setAcceptAllFileFilterUsed(true);
			int state = smFileChoose.showDefaultDialog();
			if (state == JFileChooser.APPROVE_OPTION) {
				File file = new File(smFileChoose.getFilePath());
				
				FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();

				if (file.exists()&&fileManagerContainer != null) {
					String webPath = this.textServerURL.getText();
					FileInfo downloadInfo = new FileInfo(webPath);
					ITaskFactory taskFactory = TaskFactory.getInstance();
					ITask task = taskFactory.getTask(TaskEnum.UPLOADTASK, downloadInfo);
					UploadPropressCallable uploadProgressCallable = new UploadPropressCallable(downloadInfo,true);
					task.doWork(uploadProgressCallable);
					fileManagerContainer.addItem(task);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
			CursorUtilities.setDefaultCursor();
		}
	}

	private void buttonDownloadActionPerformed() {
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
					dialogFileSaveAs.setLocalPath("/home/huchenpu/demo/result/" + define.getName());
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

	private void buttonDeleteActionPerformed() {
		try {
			try {
				Boolean fileSelected = false;
				String webFile = "";
				String webURL = "";

				if (table.getSelectedRowCount() == 1) {
					HDFSDefine define = (HDFSDefine)((HDFSTableModel)this.table.getModel()).getRowTagAt(table.getSelectedRow());
					if (define != null) {
						webFile = define.getName();
						webURL = this.textServerURL.getText();

						if (define.isDir()) {
							if (UICommonToolkit.showConfirmDialog("是否确认要删除整个文件夹“" + webFile + "”？") == JOptionPane.OK_OPTION) {
								fileSelected = true;
							}
						} else {
							if (UICommonToolkit.showConfirmDialog("是否确认要删除文件“" + webFile + "”？") == JOptionPane.OK_OPTION) {
								fileSelected = true;
							}
						}
						
						DeleteFile deleteFile = new DeleteFile(webURL, webFile,false);
						deleteFile.deleteFile();
					}
				} else if (table.getSelectedRowCount() > 1) {
					int[] indexs = table.getSelectedRows();
					if (UICommonToolkit.showConfirmDialog(String.format("是否确认要删除所选的%d个文件/文件夹？", indexs.length)) == JOptionPane.OK_OPTION) {
						for (int index : indexs) {
							HDFSDefine define = (HDFSDefine)((HDFSTableModel)this.table.getModel()).getRowTagAt(index);
							if (define != null) {
								webFile = define.getName();
								webURL = this.textServerURL.getText();
								
								DeleteFile deleteFile = new DeleteFile(webURL, webFile,false);
								deleteFile.deleteFile();
							}
						}
					}					
				} else {
				}

				if (!fileSelected) {
					UICommonToolkit.showMessageDialog("please select a file");
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

	/**
	 * 关闭按钮点击事件
	 */
	private void buttonCancelActionPerformed() {
		this.dispose();
		this.dialogResult = DialogResult.CANCEL;
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
	 * @author huchenpu
	 */
	private class HDFSTableModel extends MutiTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 构造函数。
		 * 
		 * @param columnNames
		 */
		public HDFSTableModel(String[] columnNames) {
			super(columnNames);
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
			content.add(define.getPermission());
			content.add(define.getOwner());
			content.add(define.getGroup());
			content.add(define.getSize());
			content.add(define.getReplication());
			content.add(define.getBlockSize());
			content.add(define.getName());

			// 追加内容
			contents.add(content);
			this.setRowTagAt(define, this.getRowCount() - 1);
		}
	}
}
