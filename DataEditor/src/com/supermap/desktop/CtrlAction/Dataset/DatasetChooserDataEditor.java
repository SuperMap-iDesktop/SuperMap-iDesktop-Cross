package com.supermap.desktop.CtrlAction.Dataset;

/**
 * @author Administrator 复制和删除数据集界面
 */

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetChooser;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.desktop.utilties.CharsetUtilties;
import com.supermap.desktop.utilties.CursorUtilties;

import javax.swing.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.Vector;

public class DatasetChooserDataEditor extends DatasetChooser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean DIALOG_TYPE_COPY = false;
	private boolean DIALOG_TYPE_DELECT = false;
	private int COLUMN_INDEX_SOURCEDATASET = 0;
	private int COLUMN_INDEX_SOURCEDATASOURCE = 1;
	private int COLUMN_INDEX_TARGETDATASOURCE = 2;
	private int COLUMN_INDEX_TARGETDATASET = 3;
	private int COLUMN_INDEX_CODINGTYPE = 4;
	private int COLUMN_INDEX_CHARSET = 5;
	private MutiTable datasetCopyTable;

	public DatasetChooserDataEditor(JDialog owner, Datasource datasource, MutiTable datasetCopyTable, boolean DIALOG_TYPE_COPY) {
		super(owner, true, datasource);
		this.datasetCopyTable = datasetCopyTable;
		this.DIALOG_TYPE_COPY = DIALOG_TYPE_COPY;
		setTitle(CoreProperties.getString("String_FormDatasetBrowse_FormText"));
		getDataset();
	}

	public DatasetChooserDataEditor(JFrame owner, Datasource datasource, boolean DIALOG_TYPE_DELECT) {
		super(owner, true, datasource);
		this.DIALOG_TYPE_DELECT = DIALOG_TYPE_DELECT;
		setTitle(CoreProperties.getString("String_FormDatasetBrowse_FormText"));
		getDataset();
	}

	private void getDataset() {
		buttonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (DIALOG_TYPE_COPY) {
					// 添加数据集到复制数据集窗口
					addInfoToMainTable();
				}
				if (DIALOG_TYPE_DELECT) {
					// 删除数据集
					// 删除数据集多线程操作会导致崩溃，原因未知，多半是因为另有线程同时访问了数据源，因此不使用 thread.start，使用 thread.run 单线程执行。
					DeleteThread thread = new DeleteThread();
					thread.run();
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (2 == e.getClickCount() && DIALOG_TYPE_COPY) {
					addInfoToMainTable();
				} else if (2 == e.getClickCount() && DIALOG_TYPE_DELECT) {

					// 删除数据集多线程操作会导致崩溃，原因未知，多半是因为另有线程同时访问了数据源，因此不使用 thread.start，使用 thread.run 单线程执行。
					DeleteThread thread = new DeleteThread();
					thread.run();
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER && DIALOG_TYPE_COPY) {
					addInfoToMainTable();
				} else if (e.getKeyChar() == KeyEvent.VK_ENTER && DIALOG_TYPE_DELECT) {

					// 删除数据集多线程操作会导致崩溃，原因未知，多半是因为另有线程同时访问了数据源，因此不使用 thread.start，使用 thread.run 单线程执行。
					DeleteThread thread = new DeleteThread();
					thread.run();
				}
			}
		});
	}

	class DeleteThread extends Thread {

		@Override
		public void run() {
			try {
				CursorUtilties.setWaitCursor();
				deleteFromDatasource();
			} finally {
				CursorUtilties.setDefaultCursor();
			}
		}
	}

	private void deleteFromDatasource() {
		MutiTableModel model = (MutiTableModel) table.getModel();
		int[] selectRows = table.getSelectedRows();
		if (0 < selectRows.length) {
			Datasource datasource = null;
			int count = selectRows.length;
			String datasourceName = model.getTagValue(selectRows[0]).get(1).toString();
			datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			// 只选择一条数据删除
			dispose();
			workspaceTree.removeMouseListener(mouseAdapter);
			if (1 == count) {
				String datasetName = model.getTagValue(selectRows[0]).get(0).toString();

				if (JOptionPane.OK_OPTION == UICommonToolkit
						.showConfirmDialog(MessageFormat.format(DataEditorProperties.getString("String_DelectOneDataset"), datasourceName, datasetName))) {
					Dataset deleteDataset = datasource.getDatasets().get(datasetName);
					boolean result = datasource.getDatasets().delete(deleteDataset.getName());
					workspaceTree.dispose();
					if (result) {
						deleteDataset = null;
						String successInfo = MessageFormat.format(DataEditorProperties.getString("String_Message_DelGroupSuccess"), datasourceName,
								datasetName);
						Application.getActiveApplication().getOutput().output(successInfo);
					} else {
						String failedInfo = MessageFormat.format(DataEditorProperties.getString("String_Message_DelGroupFailed"), datasourceName, datasetName);
						Application.getActiveApplication().getOutput().output(failedInfo);
					}
				}
			} else if (JOptionPane.OK_OPTION == UICommonToolkit
					.showConfirmDialog(MessageFormat.format(DataEditorProperties.getString("String_DelectMoreDataset"), count))) {
				// 删除选中的多条数据
				for (int i = 0; i < selectRows.length; i++) {
					String datasetName = model.getTagValue(selectRows[i]).get(0).toString();
					Dataset deleteDataset = datasource.getDatasets().get(datasetName);
					boolean result = datasource.getDatasets().delete(datasetName);
					if (result) {
						deleteDataset = null;
						String successInfo = MessageFormat.format(DataEditorProperties.getString("String_Message_DelGroupSuccess"), datasourceName,
								datasetName);
						Application.getActiveApplication().getOutput().output(successInfo);
					} else {
						String failedInfo = MessageFormat.format(DataEditorProperties.getString("String_Message_DelGroupFailed"), datasourceName, datasetName);
						Application.getActiveApplication().getOutput().output(failedInfo);
					}
				}
			}
		}
	}

	private void addInfoToMainTable() {
		try {
			int[] selectIndex = table.getSelectedRows();
			MutiTableModel model = (MutiTableModel) table.getModel();
			int rowCount = datasetCopyTable.getRowCount();
			for (int i = 0; i < selectIndex.length; i++) {
				Object[] temp = new Object[6];
				Vector<Object> selectVector = model.getTagValue(selectIndex[i]);
				temp[COLUMN_INDEX_SOURCEDATASET] = selectVector.get(0);
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(selectVector.get(1).toString());
				Dataset dataset = datasource.getDatasets().get(selectVector.get(0).toString());
				String imagePath = CommonToolkit.DatasourceImageWrap.getImageIconPath(datasource.getEngineType());
				DataCell datasoureCell = new DataCell(imagePath, datasource.getAlias());
				temp[COLUMN_INDEX_SOURCEDATASOURCE] = datasoureCell;
				temp[COLUMN_INDEX_TARGETDATASOURCE] = datasoureCell;
				temp[COLUMN_INDEX_TARGETDATASET] = datasource.getDatasets().getAvailableDatasetName(selectVector.get(0).toString());
				temp[COLUMN_INDEX_CODINGTYPE] = CommonToolkit.EncodeTypeWrap.findName(dataset.getEncodeType());
				if (dataset instanceof DatasetVector) {
					temp[COLUMN_INDEX_CHARSET] = CharsetUtilties.getCharsetName(((DatasetVector) dataset).getCharset());
				} else {
					temp[COLUMN_INDEX_CHARSET] = CharsetUtilties.getCharsetName(null);
				}
				datasetCopyTable.addRow(temp);
			}
			if (rowCount < datasetCopyTable.getRowCount()) {
				datasetCopyTable.setRowSelectionInterval(rowCount, datasetCopyTable.getRowCount() - 1);
			}
			dispose();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
