package com.supermap.desktop.ui.controls.CollectionDataset;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2017/7/13.
 * 创建数据集集合主界面(后面影像数据集集合也用该类创建,避免代码大量重复)
 */
public class JDialogCreateCollectionDataset extends SmDialog {
	private JTable tableDatasetDisplay;
	private JLabel labelDatasource;
	private DatasourceComboBox datasourceComboBox;
	private JLabel labelDatasetName;
	private JTextField textFieldDatasetName;
	//新建数据集集合控件
	private JLabel labelCharset;
	private CharsetComboBox charsetComboBox;
	//新建影像集合控件

	private JToolBar toolBar;
	private JButton buttonAddDataset;
	private JButton buttonSelectAll;
	private JButton buttonInvertSelect;
	private JButton buttonDelete;
	private JButton buttonMoveFirst;
	private JButton buttonMoveUp;
	private JButton buttonMoveDown;
	private JButton buttonMoveLast;
	private JButton buttonRefresh;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JPanel panelTableInfo;
	private JPanel panelBasicInfo;
	private JScrollPane scrollPane;
	private CollectionDatasetTableModel tableModel;
	//修改数据集集合时用到
	private DatasetVector datasetVector;

	private int collectionType;
	//矢量数据集集合添加子数据集
	private boolean isSetDatasetCollectionCount;
	//影像集合添/删子集
	private boolean isSetImageCollectinCount;


	private DatasetChooser datasetChooser;

	private ActionListener addDatasetListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			addDataset();
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
			deleteDatasetInfo();
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
	private ActionListener refreshListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			refreshState();
		}
	};
	private ActionListener createListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (collectionType == tableModel.VECTOR_COLLECTION_TYPE) {
				if (isSetDatasetCollectionCount) {
					addDatasetToCollection();
				} else {
					createDatasetCollection();
				}
			} else {

			}
		}
	};

	private void addDatasetToCollection() {
		if (null != this.datasetVector) {
			ArrayList<DatasetInfo> datasetInfos = tableModel.getDatasetInfos();
			for (int i = 0; i < datasetInfos.size(); i++) {
				if (((datasetVector.GetSubCollectionDatasetType() == DatasetType.UNKNOWN)
						|| (datasetVector.GetSubCollectionDatasetType() == datasetInfos.get(i).getDataset().getType()))
						&& !(datasetVector.getName().equals(datasetInfos.get(i).getDataset().getName()))) {
					boolean result = datasetVector.addCollectionDataset((DatasetVector) datasetInfos.get(i).getDataset());
					if (result) {
						Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddSuccess"), datasetVector.getName(), datasetInfos.get(i).getName()));
					} else {
						Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddFailed"), datasetVector.getName(), datasetInfos.get(i).getName()));
					}
				}
			}
			JDialogCreateCollectionDataset.this.dispose();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					UICommonToolkit.refreshSelectedDatasourceNode(datasetVector.getDatasource().getAlias());
				}
			});
		}
	}

	private void createDatasetCollection() {
		final Datasource datasource = datasourceComboBox.getSelectedDatasource();
		DatasetVectorInfo info = new DatasetVectorInfo();
		String datasetName = datasource.getDatasets().getAvailableDatasetName(textFieldDatasetName.getText());
		info.setName(datasetName);
		info.setType(DatasetType.VECTORCOLLECTION);

		DatasetVector vector = datasource.getDatasets().create(info);
		vector.setCharset(CharsetUtilities.valueOf(charsetComboBox.getSelectedItem().toString()));
		ArrayList<DatasetInfo> datasetInfos = tableModel.getDatasetInfos();
		for (int i = 0; i < datasetInfos.size(); i++) {
			if ((vector.GetSubCollectionDatasetType() == DatasetType.UNKNOWN) || (vector.GetSubCollectionDatasetType() == datasetInfos.get(i).getDataset().getType())) {
				boolean result = vector.addCollectionDataset((DatasetVector) datasetInfos.get(i).getDataset());
				if (result) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddSuccess"), vector.getName(), datasetInfos.get(i).getName()));
				} else {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddFailed"), vector.getName(), datasetInfos.get(i).getName()));
				}
			}
		}
		JDialogCreateCollectionDataset.this.dispose();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
			}
		});
	}

	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialogCreateCollectionDataset.this.dispose();
		}
	};

	private void refreshState() {
		ArrayList<DatasetInfo> datasetInfos = tableModel.getDatasetInfos();
		for (int i = 0; i < datasetInfos.size(); i++) {
			Dataset tempDataset = null;
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int j = 0; j < datasources.getCount(); j++) {
				tempDataset = DatasourceUtilities.getDataset(datasetInfos.get(i).getName(), datasources.get(j));
			}
			int stateColumn = collectionType == tableModel.VECTOR_COLLECTION_TYPE ? tableModel.COLUMN_VECTOR_STATE : tableModel.COLUMN_IMAGE_STATE;
			if (null == tempDataset) {
				tableModel.setValueAt(CommonProperties.getString("String_Status_NotExist"), i, stateColumn);
			} else {
				tableModel.setValueAt(CommonProperties.getString("String_Status_Exist"), i, stateColumn);
			}
		}
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

	private void exchangeItem(int source, int target) {
		if (collectionType == tableModel.IMAGE_COLLECTION_TYPE) {
			String sourceCaption = (String) tableModel.getValueAt(source, tableModel.COLUMN_IMAGE_CAPTION);
			String targetCaption = (String) tableModel.getValueAt(target, tableModel.COLUMN_IMAGE_CAPTION);
			String sourceName = (String) tableModel.getValueAt(source, tableModel.COLUMN_IMAGE_NAME);
			String targetName = (String) tableModel.getValueAt(target, tableModel.COLUMN_IMAGE_NAME);
			String sourceState = (String) tableModel.getValueAt(source, tableModel.COLUMN_IMAGE_STATE);
			String targetState = (String) tableModel.getValueAt(target, tableModel.COLUMN_IMAGE_STATE);
			tableModel.setValueAt(targetCaption, source, tableModel.COLUMN_IMAGE_CAPTION);
			tableModel.setValueAt(sourceCaption, target, tableModel.COLUMN_IMAGE_CAPTION);
			tableModel.setValueAt(targetName, source, tableModel.COLUMN_IMAGE_NAME);
			tableModel.setValueAt(sourceName, target, tableModel.COLUMN_IMAGE_NAME);
			tableModel.setValueAt(targetState, source, tableModel.COLUMN_IMAGE_STATE);
			tableModel.setValueAt(sourceState, target, tableModel.COLUMN_IMAGE_STATE);
		} else {
			String sourceName = (String) tableModel.getValueAt(source, tableModel.COLUMN_VECTOR_NAME);
			String targetName = (String) tableModel.getValueAt(target, tableModel.COLUMN_VECTOR_NAME);
			String sourceState = (String) tableModel.getValueAt(source, tableModel.COLUMN_VECTOR_STATE);
			String targetState = (String) tableModel.getValueAt(target, tableModel.COLUMN_VECTOR_STATE);
			tableModel.setValueAt(targetName, source, tableModel.COLUMN_VECTOR_NAME);
			tableModel.setValueAt(sourceName, target, tableModel.COLUMN_VECTOR_NAME);
			tableModel.setValueAt(targetState, source, tableModel.COLUMN_VECTOR_STATE);
			tableModel.setValueAt(sourceState, target, tableModel.COLUMN_VECTOR_STATE);
		}
	}

	private void scrollToFrist() {
		Rectangle treeVisibleRectangle = new Rectangle(tableDatasetDisplay.getVisibleRect().x, 0, tableDatasetDisplay.getVisibleRect().width, tableDatasetDisplay.getVisibleRect().height);
		tableDatasetDisplay.scrollRectToVisible(treeVisibleRectangle);
	}

	private void scrollToLast() {
		Rectangle treeVisibleRectangle = new Rectangle(tableDatasetDisplay.getVisibleRect().x, tableDatasetDisplay.getVisibleRect().height, tableDatasetDisplay.getVisibleRect().width, tableDatasetDisplay.getVisibleRect().height);
		tableDatasetDisplay.scrollRectToVisible(treeVisibleRectangle);
	}

	private void deleteDatasetInfo() {
		int[] selectRows = tableDatasetDisplay.getSelectedRows();
		tableModel.removeRows(selectRows);
		if (tableDatasetDisplay.getRowCount() > 0) {
			tableDatasetDisplay.setRowSelectionInterval(tableDatasetDisplay.getRowCount() - 1, tableDatasetDisplay.getRowCount() - 1);
		}
		setButtonState();
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

	/**
	 * 设置按键状态
	 */
	protected void setButtonState() {
		if (0 < tableDatasetDisplay.getRowCount()) {
			this.buttonOK.setEnabled(true);
			this.buttonSelectAll.setEnabled(true);
			this.buttonInvertSelect.setEnabled(true);
			this.buttonMoveUp.setEnabled(true);
			this.buttonMoveFirst.setEnabled(true);
			this.buttonMoveDown.setEnabled(true);
			this.buttonMoveLast.setEnabled(true);
			this.buttonRefresh.setEnabled(true);

		} else {
			this.buttonOK.setEnabled(false);
			this.buttonSelectAll.setEnabled(false);
			this.buttonInvertSelect.setEnabled(false);
			this.buttonMoveUp.setEnabled(false);
			this.buttonMoveFirst.setEnabled(false);
			this.buttonMoveDown.setEnabled(false);
			this.buttonMoveLast.setEnabled(false);
			this.buttonRefresh.setEnabled(false);
		}
		if (isSetDatasetCollectionCount) {
			//添加数据集集合时不用判断删除按钮是否可用
			this.buttonDelete.setEnabled(false);
			return;
		}
		if (tableDatasetDisplay.getSelectedRows().length > 0) {
			this.buttonDelete.setEnabled(true);
		} else {
			this.buttonDelete.setEnabled(false);
		}
	}

	private void addDataset() {
		//添加数据集
		datasetChooser = new DatasetChooser(JDialogCreateCollectionDataset.this) {
			@Override
			protected boolean isSupportDatasource(Datasource datasource) {
				return !DatasourceUtilities.isWebDatasource(datasource.getEngineType()) && super.isSupportDatasource(datasource);
			}
		};
		datasetChooser.setSupportDatasetTypes(DatasetTypeUtilities.getDatasetTypeVector());
		datasetChooser.setSelectedDatasource(DatasourceUtilities.getDefaultResultDatasource());
		if (datasetChooser.showDialog() == DialogResult.OK) {
			addInfoToMainTable();
		}
		setButtonState();
	}


	private void addInfoToMainTable() {
		//将数据添加到展示表中
		try {
			java.util.List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();
			for (int i = 0; i < selectedDatasets.size(); i++) {
				DatasetInfo datasetInfo = new DatasetInfo();
				datasetInfo.setDataset(selectedDatasets.get(i));
				datasetInfo.setCapiton(selectedDatasets.get(i).getName());
				datasetInfo.setName(selectedDatasets.get(i).getName());
				datasetInfo.setState(CommonProperties.getString("String_Status_Exist"));
				tableModel.addRow(datasetInfo);
			}
			if (0 < tableDatasetDisplay.getRowCount()) {
				tableDatasetDisplay.setRowSelectionInterval(tableDatasetDisplay.getRowCount() - 1, tableDatasetDisplay.getRowCount() - 1);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public JDialogCreateCollectionDataset(int collectionType) {
		this(collectionType, null);
	}

	public JDialogCreateCollectionDataset(int collectionType, DatasetVector[] datasetVectors) {
		super();
		this.collectionType = collectionType;
		init(datasetVectors);
	}

	public void init(DatasetVector[] datasetVectors) {
		initComponents();
		initResources();
		initLayout();
		registEvents();
		this.setSize(new Dimension(800, 450));
		this.setLocationRelativeTo(null);
		this.componentList.add(this.buttonOK);
		this.componentList.add(this.buttonCancel);
		this.componentList.add(this.buttonAddDataset);
		this.componentList.add(this.buttonSelectAll);
		this.componentList.add(this.buttonInvertSelect);
		this.componentList.add(this.buttonDelete);
		this.componentList.add(this.buttonMoveFirst);
		this.componentList.add(this.buttonMoveUp);
		this.componentList.add(this.buttonMoveDown);
		this.componentList.add(this.buttonMoveLast);
		this.componentList.add(this.buttonRefresh);
		this.componentList.add(this.datasourceComboBox);
		this.componentList.add(this.textFieldDatasetName);
		this.componentList.add(this.charsetComboBox);
		this.setFocusTraversalPolicy(this.policy);
		Dataset[] datasets = null;
		if (null != datasetVectors) {
			datasets = datasetVectors;
		} else if (null != Application.getActiveApplication().getActiveDatasets()
				&& Application.getActiveApplication().getActiveDatasets().length > 0) {
			datasets = Application.getActiveApplication().getActiveDatasets();
		}
		if (null != datasets) {
			//有默认数据集数组则更新显示
			ArrayList<DatasetInfo> datasetInfos = new ArrayList<>();
			for (int i = 0; i < datasets.length; i++) {
				DatasetInfo datasetInfo = new DatasetInfo();
				datasetInfo.setDataset(datasets[i]);
				datasetInfo.setCapiton(datasets[i].getName());
				datasetInfo.setName(datasets[i].getName());
				datasetInfo.setState(CommonProperties.getString("String_Status_Exist"));
				datasetInfos.add(datasetInfo);
			}
			tableModel.updateRows(datasetInfos);
		}
	}

	private void initComponents() {
		this.panelTableInfo = new JPanel();
		this.panelBasicInfo = new JPanel();
		this.scrollPane = new JScrollPane();
		this.tableDatasetDisplay = new JTable();
		this.labelDatasource = new JLabel();
		ArrayList<Datasource> datasourceArray = new ArrayList<>();
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (null != datasources) {
			for (int i = 0; i < datasources.getCount(); i++) {
				//当前只支持POSTGRESQL类型的数据源,后续修改
				if (datasources.get(i).getEngineType().equals(EngineType.POSTGRESQL)
						|| datasources.get(i).getEngineType().equals(EngineType.UDB)
						|| datasources.get(i).getEngineType().equals(EngineType.ORACLEPLUS)) {
					datasourceArray.add(datasources.get(i));
				}
			}
		}
		this.datasourceComboBox = new DatasourceComboBox(datasourceArray);
		this.labelDatasetName = new JLabel();
		this.textFieldDatasetName = new JTextField();
		this.toolBar = new JToolBar();
		this.buttonDelete = new JButton();
		this.buttonAddDataset = new JButton();
		this.buttonSelectAll = new JButton();
		this.buttonInvertSelect = new JButton();
		this.buttonMoveFirst = new JButton();
		this.buttonMoveUp = new JButton();
		this.buttonMoveDown = new JButton();
		this.buttonMoveLast = new JButton();
		this.buttonRefresh = new JButton();
		this.tableModel = new CollectionDatasetTableModel(collectionType);
		if (collectionType == tableModel.VECTOR_COLLECTION_TYPE) {
			Datasource datasource = this.datasourceComboBox.getSelectedDatasource();
			if (null != datasource) {
				this.textFieldDatasetName.setText(datasource.getDatasets().getAvailableDatasetName("NewDatasetVectorCollection"));
			}
			this.labelCharset = new JLabel();
			this.charsetComboBox = new CharsetComboBox();
		} else {

		}
		this.tableDatasetDisplay.getTableHeader().setReorderingAllowed(false);
		this.tableDatasetDisplay.setModel(this.tableModel);
		this.tableDatasetDisplay.setRowHeight(23);
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		initToolBar();
		setButtonState();
	}

	private void initToolBar() {
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
		this.toolBar.add(buttonRefresh);
	}

	public void setComponentName() {
		ComponentUIUtilities.setName(this.buttonAddDataset, "JDialogCreateCollectionDataset_buttonAddDataset");
		ComponentUIUtilities.setName(this.buttonDelete, "JDialogCreateCollectionDataset_buttonDelete");
		ComponentUIUtilities.setName(this.buttonSelectAll, "JDialogCreateCollectionDataset_buttonSelectAll");
		ComponentUIUtilities.setName(this.buttonInvertSelect, "JDialogCreateCollectionDataset_buttonInvertSelect");
		ComponentUIUtilities.setName(this.buttonRefresh, "JDialogCreateCollectionDataset_buttonRefresh");
		ComponentUIUtilities.setName(this.buttonMoveFirst, "JDialogCreateCollectionDataset_buttonMoveToFirst");
		ComponentUIUtilities.setName(this.buttonMoveUp, "JDialogCreateCollectionDataset_buttonMoveToForeword");
		ComponentUIUtilities.setName(this.buttonMoveDown, "JDialogCreateCollectionDataset_buttonMoveToNext");
		ComponentUIUtilities.setName(this.buttonMoveLast, "JDialogCreateCollectionDataset_buttonMoveToLast");
		ComponentUIUtilities.setName(this.buttonOK, "JDialogCreateCollectionDataset_buttonOK");
		ComponentUIUtilities.setName(this.buttonCancel, "JDialogCreateCollectionDataset_buttonCancel");
		ComponentUIUtilities.setName(this.toolBar, "JDialogCreateCollectionDataset_toolBar");
		ComponentUIUtilities.setName(this.scrollPane, "JDialogCreateCollectionDataset_scrollPane");
	}

	private void initResources() {
		this.setTitle(ControlsProperties.getString("String_CreateCollectionDataset"));
		this.labelDatasource.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
		this.labelDatasetName.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
		this.labelCharset.setText(ControlsProperties.getString("String_LabelCharset"));
		this.buttonAddDataset.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonMoveFirst.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveFirst.png"));
		this.buttonMoveUp.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveUp.png"));
		this.buttonMoveDown.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveDown.png"));
		this.buttonMoveLast.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveLast.png"));
		this.buttonRefresh.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Refresh.png"));
		this.buttonAddDataset.setToolTipText(ControlsProperties.getString("String_AddColor"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		this.buttonInvertSelect.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
		this.buttonDelete.setToolTipText(CommonProperties.getString("String_ToolBar_Remove"));
		this.buttonMoveFirst.setToolTipText(CommonProperties.getString("String_ToolBar_MoveFirst"));
		this.buttonMoveUp.setToolTipText(CommonProperties.getString("String_ToolBar_MoveUp"));
		this.buttonMoveDown.setToolTipText(CommonProperties.getString("String_ToolBar_MoveDown"));
		this.buttonMoveLast.setToolTipText(CommonProperties.getString("String_ToolBar_MoveLast"));
		this.buttonRefresh.setToolTipText(CommonProperties.getString("String_Tooltip_RefreshStatus"));
	}

	private void initLayout() {
		initPanelTableInfoLayout();
		initPanelBasicInfoLayout();
		this.getContentPane().setLayout(new GridBagLayout());
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 1, 1, 1).setInsets(0, 5, 10, 10));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 1, 1, 1).setInsets(0, 5, 10, 10));
		this.getContentPane().add(this.panelTableInfo, new GridBagConstraintsHelper(0, 0, 7, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(0.7, 1).setInsets(10, 10, 5, 0));
		this.getContentPane().add(this.panelBasicInfo, new GridBagConstraintsHelper(7, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(0.3, 1).setInsets(10));
		this.getContentPane().add(panelButton, new GridBagConstraintsHelper(0, 1, 10, 1).setAnchor(GridBagConstraints.EAST));
	}

	private void initPanelBasicInfoLayout() {
		this.panelBasicInfo.setLayout(new GridBagLayout());
		if (collectionType == tableModel.VECTOR_COLLECTION_TYPE) {
			this.panelBasicInfo.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.panelBasicInfo.add(this.datasourceComboBox, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.panelBasicInfo.add(this.labelDatasetName, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.panelBasicInfo.add(this.textFieldDatasetName, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.panelBasicInfo.add(this.labelCharset, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.panelBasicInfo.add(this.charsetComboBox, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.panelBasicInfo.add(new JPanel(), new GridBagConstraintsHelper(0, 4, 3, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		} else {

		}
		this.panelBasicInfo.setBorder(new LineBorder(Color.GRAY));
	}

	private void initPanelTableInfoLayout() {
		this.panelTableInfo.setLayout(new GridBagLayout());
		this.panelTableInfo.add(this.toolBar, new GridBagConstraintsHelper(0, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelTableInfo.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 3, 4).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.scrollPane.setViewportView(this.tableDatasetDisplay);
	}

	private void registEvents() {
		removeEvents();
		this.buttonAddDataset.addActionListener(addDatasetListener);
		this.buttonSelectAll.addActionListener(selectAllListener);
		this.buttonInvertSelect.addActionListener(invertSelectListener);
		this.buttonDelete.addActionListener(deleteListener);
		this.buttonMoveFirst.addActionListener(moveFirstListener);
		this.buttonMoveUp.addActionListener(moveUpListener);
		this.buttonMoveDown.addActionListener(moveDownListener);
		this.buttonMoveLast.addActionListener(moveLastListener);
		this.buttonRefresh.addActionListener(refreshListener);
		this.buttonOK.addActionListener(createListener);
		this.buttonCancel.addActionListener(cancelListener);
	}

	private void removeEvents() {
		this.buttonAddDataset.removeActionListener(addDatasetListener);
		this.buttonSelectAll.removeActionListener(selectAllListener);
		this.buttonInvertSelect.removeActionListener(invertSelectListener);
		this.buttonDelete.removeActionListener(deleteListener);
		this.buttonMoveFirst.removeActionListener(moveFirstListener);
		this.buttonMoveUp.removeActionListener(moveUpListener);
		this.buttonMoveDown.removeActionListener(moveDownListener);
		this.buttonMoveLast.removeActionListener(moveLastListener);
		this.buttonRefresh.removeActionListener(refreshListener);
		this.buttonOK.removeActionListener(createListener);
		this.buttonCancel.removeActionListener(cancelListener);
	}

	public void isSetDatasetCollectionCount(boolean setDatasetCollectionCount) {
		isSetDatasetCollectionCount = setDatasetCollectionCount;
		if (isSetDatasetCollectionCount) {
			this.panelBasicInfo.setVisible(false);
			this.setButtonState();
		}
	}

	public void setDatasetVector(DatasetVector datasetVector) {
		this.datasetVector = datasetVector;
	}
}
