package com.supermap.desktop.ui.controls.CollectionDataset;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ChooseTable.MultipleCheckboxTableModel;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by xie on 2017/8/9.
 * 抽象父类,用于添加显示数据集,需要子类实现抽象方法后才能实现具体的功能
 */
public abstract class JPanelDatasetChoose extends JPanel {
	private JScrollPane scrollPane;
	protected JTable tableDatasetDisplay;
	protected JToolBar toolBar;
	private JButton buttonAddDataset;
	private JButton buttonSelectAll;
	private JButton buttonInvertSelect;
	private JButton buttonDelete;
	private JButton buttonMoveFirst;
	private JButton buttonMoveUp;
	private JButton buttonMoveDown;
	private JButton buttonMoveLast;
	//子类中实现
//	private JButton buttonRefresh;
	private ArrayList<Dataset> datasets;
	private String[] columnName;
	private boolean[] enableColumn;
	protected MultipleCheckboxTableModel tableModel;
	private DatasetChooser datasetChooser;
	protected DatasetType[] supportDatasetTypes;

	private ActionListener addDatasetListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//添加数据集
			datasetChooser = new DatasetChooser(null) {
				@Override
				protected boolean isSupportDatasource(Datasource datasource) {
					return !DatasourceUtilities.isWebDatasource(datasource.getEngineType()) && super.isSupportDatasource(datasource);
				}
			};
			datasetChooser.setSupportDatasetTypes(supportDatasetTypes);
			datasetChooser.setSelectedDatasource(DatasourceUtilities.getDefaultResultDatasource());
			if (datasetChooser.showDialog() == DialogResult.OK) {
				addInfoToMainTable();
			}
			setButtonState();
		}
	};

	private ActionListener selectAllListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			selectAll();
			setButtonState();
		}
	};
	private ActionListener invertSelectListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			TableUtilities.invertSelection(tableDatasetDisplay);
			setButtonState();
		}
	};
	private ActionListener deleteListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRows = tableDatasetDisplay.getSelectedRows();
			for (int length = selectedRows.length, i = length; i >= 0; i--) {
				tableModel.removeRow(selectedRows[i]);
				//删除数据集集合内的数据集
				datasets.remove(selectedRows[i]);
			}
		}
	};
	private ActionListener moveFirstListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveFirst();
		}
	};

	private ActionListener moveUpListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveUp();
		}
	};
	private ActionListener moveDownListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveDown();
		}
	};
	private ActionListener moveLastListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveLast();
		}
	};

	public JPanelDatasetChoose(ArrayList<Dataset> datasets, String[] columnName, boolean[] enableColumn) {
		this.datasets = datasets;
		this.columnName = columnName;
		this.enableColumn = enableColumn;
		init();
	}

	public void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
	}

	protected void registEvents() {
		removeEvents();
		this.buttonAddDataset.addActionListener(addDatasetListener);
		this.buttonSelectAll.addActionListener(selectAllListener);
		this.buttonInvertSelect.addActionListener(invertSelectListener);
		this.buttonDelete.addActionListener(deleteListener);
		this.buttonMoveFirst.addActionListener(moveFirstListener);
		this.buttonMoveUp.addActionListener(moveUpListener);
		this.buttonMoveDown.addActionListener(moveDownListener);
		this.buttonMoveLast.addActionListener(moveLastListener);
//		this.buttonRefresh.addActionListener(refreshListener);
	}

	//提供给外部调用,方便释放资源,同时可供子类重载
	public void removeEvents() {
		this.buttonAddDataset.removeActionListener(addDatasetListener);
		this.buttonSelectAll.removeActionListener(selectAllListener);
		this.buttonInvertSelect.removeActionListener(invertSelectListener);
		this.buttonDelete.removeActionListener(deleteListener);
		this.buttonMoveFirst.removeActionListener(moveFirstListener);
		this.buttonMoveUp.removeActionListener(moveUpListener);
		this.buttonMoveDown.removeActionListener(moveDownListener);
		this.buttonMoveLast.removeActionListener(moveLastListener);
	}

	protected void initResources() {
		this.buttonAddDataset.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonMoveFirst.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveFirst.png"));
		this.buttonMoveUp.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveUp.png"));
		this.buttonMoveDown.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveDown.png"));
		this.buttonMoveLast.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveLast.png"));
//		this.buttonRefresh.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Refresh.png"));
		this.buttonAddDataset.setToolTipText(ControlsProperties.getString("String_AddColor"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		this.buttonInvertSelect.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
		this.buttonDelete.setToolTipText(CommonProperties.getString("String_ToolBar_Remove"));
		this.buttonMoveFirst.setToolTipText(CommonProperties.getString("String_ToolBar_MoveFirst"));
		this.buttonMoveUp.setToolTipText(CommonProperties.getString("String_ToolBar_MoveUp"));
		this.buttonMoveDown.setToolTipText(CommonProperties.getString("String_ToolBar_MoveDown"));
		this.buttonMoveLast.setToolTipText(CommonProperties.getString("String_ToolBar_MoveLast"));
//		this.buttonRefresh.setToolTipText(CommonProperties.getString("String_Tooltip_RefreshStatus"));
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(this.toolBar, new GridBagConstraintsHelper(0, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 3, 4).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.scrollPane.setViewportView(this.tableDatasetDisplay);
	}

	protected void initComponents() {
		this.datasets = new ArrayList<>();
		this.scrollPane = new JScrollPane();
		this.buttonAddDataset = new JButton();
		this.buttonSelectAll = new JButton();
		this.buttonInvertSelect = new JButton();
		this.buttonMoveFirst = new JButton();
		this.buttonMoveUp = new JButton();
		this.buttonMoveDown = new JButton();
		this.buttonMoveLast = new JButton();
//		this.buttonRefresh = new JButton();
		this.buttonDelete = new JButton();
		this.tableDatasetDisplay = new JTable();
		this.tableModel = new MultipleCheckboxTableModel(getData(datasets), columnName, enableColumn);
		this.tableDatasetDisplay.getTableHeader().setReorderingAllowed(false);
		this.tableDatasetDisplay.setModel(this.tableModel);
		this.tableDatasetDisplay.setRowHeight(23);
		this.toolBar = new JToolBar();
		this.scrollPane.setPreferredSize(new Dimension(300, 200));
		initToolBar();
	}

	protected void initToolBar() {
		this.toolBar.setFloatable(false);
		this.toolBar.add(buttonAddDataset);
		this.toolBar.addSeparator();
		this.toolBar.add(buttonSelectAll);
		this.toolBar.add(buttonInvertSelect);
		this.toolBar.addSeparator();
		this.toolBar.add(buttonDelete);
		this.toolBar.addSeparator();
		this.toolBar.add(buttonMoveFirst);
		this.toolBar.add(buttonMoveUp);
		this.toolBar.add(buttonMoveDown);
		this.toolBar.add(buttonMoveLast);
//		this.toolBar.add(buttonRefresh);
	}

	private void moveLast() {
		int[] selectRows = tableDatasetDisplay.getSelectedRows();
		int size = selectRows.length;
		if (0 == size) {
			return;
		} else if (1 <= size && tableDatasetDisplay.getRowCount() - 1 == selectRows[size - 1]) {
			return;
		} else {
			//交换到最后一个时需要进行双层遍历，且时时修改selectRows才能实现整体位移,
			//方法和移动到第一个相似，逆序
			int exchangeSize = selectRows[size - 1] + 1;
			int rowCount = tableDatasetDisplay.getRowCount() - 1;
			for (int i = exchangeSize; i <= rowCount; i++) {
				int row = i;
				for (int j = size - 1; j >= 0; j--) {
					exchangeItem(selectRows[j], row);
					selectRows[j] = row;
					row--;
				}
			}
			tableDatasetDisplay.clearSelection();
			for (int i = 0; i < size; i++) {
				tableDatasetDisplay.addRowSelectionInterval(rowCount - i, rowCount - i);
			}
			scrollToLast();
		}
	}

	private void scrollToLast() {
		Rectangle treeVisibleRectangle = new Rectangle(tableDatasetDisplay.getVisibleRect().x, tableDatasetDisplay.getVisibleRect().height, tableDatasetDisplay.getVisibleRect().width, tableDatasetDisplay.getVisibleRect().height);
		tableDatasetDisplay.scrollRectToVisible(treeVisibleRectangle);
	}

	private void moveDown() {
		int[] selectRows = tableDatasetDisplay.getSelectedRows();
		int size = selectRows.length;
		if (0 == size) {
			return;
		} else if (1 <= size && tableDatasetDisplay.getRowCount() - 1 == selectRows[size - 1]) {
			return;
		} else {
			int index = selectRows[size - 1];
			int rowLocation = index + 1;
			int newSize = index - selectRows[0] + 1;
			for (int i = 0; i < newSize; i++) {
				exchangeItem(index, rowLocation);
				index--;
				rowLocation--;
			}

			tableDatasetDisplay.clearSelection();
			for (int i = 0; i < size; i++) {
				tableDatasetDisplay.addRowSelectionInterval(selectRows[i] + 1, selectRows[i] + 1);
			}
			int length = tableDatasetDisplay.getSelectedRows().length;
			if (length > 0 && tableDatasetDisplay.getSelectedRows()[length - 1] == tableDatasetDisplay.getRowCount() - 1) {
				scrollToLast();
			}
		}
	}

	private void moveFirst() {
		int[] selectRows = tableDatasetDisplay.getSelectedRows();
		int size = selectRows.length;
		if (0 == size) {
			return;
		} else if (1 <= size && 0 == selectRows[0]) {
			return;
		} else {
			//交换到第一个时需要进行双层遍历，且时时修改selectRows才能实现整体位移
			int exchangeSize = selectRows[0] - 1;
			for (int i = exchangeSize; i >= 0; i--) {
				int row = i;
				for (int j = 0; j < size; j++) {
					exchangeItem(selectRows[j], row);
					selectRows[j] = row;
					row++;
				}
			}
			tableDatasetDisplay.clearSelection();
			for (int i = 0; i < size; i++) {
				tableDatasetDisplay.addRowSelectionInterval(i, i);
			}
			scrollToFrist();
		}
	}

	private void moveUp() {
		int[] selectRows = tableDatasetDisplay.getSelectedRows();
		int size = selectRows.length;
		if (0 == size || (1 <= size && 0 == selectRows[0])) {
			return;
		} else {
			int index = selectRows[0];
			int rowLocation = index - 1;
			int newSize = selectRows[size - 1] - index + 1;
			for (int i = 0; i < newSize; i++) {
				exchangeItem(index, rowLocation);
				index++;
				rowLocation++;
			}
			tableDatasetDisplay.clearSelection();
			for (int i = 0; i < size; i++) {
				tableDatasetDisplay.addRowSelectionInterval(selectRows[i] - 1, selectRows[i] - 1);
			}
			if (tableDatasetDisplay.getSelectedRows().length > 0 && tableDatasetDisplay.getSelectedRows()[0] == 0) {
				scrollToFrist();
			}
		}

	}

	private void scrollToFrist() {
		Rectangle treeVisibleRectangle = new Rectangle(tableDatasetDisplay.getVisibleRect().x, 0, tableDatasetDisplay.getVisibleRect().width, tableDatasetDisplay.getVisibleRect().height);
		tableDatasetDisplay.scrollRectToVisible(treeVisibleRectangle);
	}

	/**
	 * 设置按键状态,子类重载后填充自己的需求
	 */
	protected void setButtonState() {
		if (0 < tableDatasetDisplay.getRowCount()) {
			this.buttonSelectAll.setEnabled(true);
			this.buttonInvertSelect.setEnabled(true);
			this.buttonMoveUp.setEnabled(true);
			this.buttonMoveFirst.setEnabled(true);
			this.buttonMoveDown.setEnabled(true);
			this.buttonMoveLast.setEnabled(true);
//			this.buttonRefresh.setEnabled(true);

		} else {
			this.buttonSelectAll.setEnabled(false);
			this.buttonInvertSelect.setEnabled(false);
			this.buttonMoveUp.setEnabled(false);
			this.buttonMoveFirst.setEnabled(false);
			this.buttonMoveDown.setEnabled(false);
			this.buttonMoveLast.setEnabled(false);
//			this.buttonRefresh.setEnabled(false);
		}
		if (tableDatasetDisplay.getSelectedRows().length > 0) {
			this.buttonDelete.setEnabled(true);
		} else {
			this.buttonDelete.setEnabled(false);
		}
	}

	protected void addInfoToMainTable() {
		//将数据添加到展示表中
		try {
			java.util.List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();
			for (int i = 0; i < selectedDatasets.size(); i++) {
				//添加行和数据集集合的添加顺序一致,保证两个集合内部的索引一致
				tableModel.addRow(transFormData(selectedDatasets.get(i)));
				datasets.add(selectedDatasets.get(i));
			}
			if (0 < tableDatasetDisplay.getRowCount()) {
				tableDatasetDisplay.setRowSelectionInterval(tableDatasetDisplay.getRowCount() - 1, tableDatasetDisplay.getRowCount() - 1);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public DatasetType[] getSupportDatasetTypes() {
		return supportDatasetTypes;
	}

	public void setSupportDatasetTypes(DatasetType[] supportDatasetTypes) {
		this.supportDatasetTypes = supportDatasetTypes;
	}

	//全选
	private void selectAll() {
		if (tableDatasetDisplay.getRowCount() < 1) {
			tableDatasetDisplay.setRowSelectionAllowed(true);
		} else {
			tableDatasetDisplay.setRowSelectionAllowed(true);
			// 设置所有项全部选中
			tableDatasetDisplay.setRowSelectionInterval(0, tableDatasetDisplay.getRowCount() - 1);
		}
	}

	//行与行之间的数据交换,子类实现其具体功能
	protected abstract void exchangeItem(int sourceRow, int targetRow);

	//将选中的数据集转换为对象数组类型
	protected abstract Object[] transFormData(Dataset dataset);

	//初始化时将传入的数据集集合转换为对象数组
	protected abstract Object[][] getData(ArrayList<Dataset> datasets);
}
