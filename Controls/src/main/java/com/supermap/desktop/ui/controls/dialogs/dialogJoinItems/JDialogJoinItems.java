package com.supermap.desktop.ui.controls.dialogs.dialogJoinItems;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.List;

public class JDialogJoinItems extends SmDialog {
	private static final int COLUMN_INDEX = 0;
	private static final int COLUMN_NAME = 1;
	private static final int COLUMN_TABLE = 2;
	private static final int COLUMN_THIS_FIELD = 3;
	private static final int COLUMN_JOIN_FIELD = 4;
	private static final int COLUMN_EXPRESSION = 5;
	private static final int COLUMN_TYPE = 6;

	private int[] tableWeight = new int[]{60, 110, 125, 125, 125, 350, 75};

	private JoinItems joinItems = null;
	private Dataset currentDataset = null;

	private JToolBar toolBar = new JToolBar();

	private JPanel panelButton = new JPanel();
	private SmButton buttonAdd = new SmButton();
	private SmButton buttonDel = new SmButton();
	private SmButton buttonSelectAll = new SmButton();
	private SmButton buttonReverse = new SmButton();
	private SmButton buttonOk = new SmButton("Ok");
	private SmButton buttonCancel = new SmButton("cancle");
	private JScrollPane scrollPane = new JScrollPane();
	private JoinItemsTable joinItemsTable = new JoinItemsTable();
	protected static final DatasetType[] datasetTypes = new DatasetType[]{DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.CAD, DatasetType.TABULAR,
			DatasetType.TEXT, DatasetType.NETWORK, DatasetType.POINT3D, DatasetType.LINKTABLE, DatasetType.LINE3D, DatasetType.REGION3D};
	/*
	 * private final DefaultTableCellRenderer columnTableCellRenderer = new DefaultTableCellRenderer() {
	 * 
	 * @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { Dataset
	 * dataset = joinItemsTable.getCurrentDataset().getDatasource().getDatasets().get(value.toString()); DataCell dataCell = new DataCell();
	 * dataCell.initDatasetType(dataset); if (isSelected) { dataCell.setBackground(table.getSelectionBackground()); } else {
	 * dataCell.setBackground(table.getBackground()); } return dataCell; } };
	 */
	private final DefaultCellEditor columnTableCellEditor = new DefaultCellEditor(new JComboBox()) {
		DatasetComboBox datasetComboBox = null;
		int currentRow = -1;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			currentRow = row;
			getDatasetComboBox(((Dataset) value).getName());
			datasetComboBox.setSelectedDataset(((Dataset) value));
			return datasetComboBox;
		}

		private DatasetComboBox getDatasetComboBox(String name) {
			datasetComboBox = new DatasetComboBox(currentDataset.getDatasource().getDatasets());

			datasetComboBox.setSupportedDatasetTypes(datasetTypes);
			datasetComboBox.removeDataset(((JoinItemsTableModel) joinItemsTable.getModel()).getCurrentDataset());
			for (int i = 0; i < joinItems.getCount(); i++) {
				if (!name.equals(joinItems.get(i).getForeignTable())) {
					datasetComboBox.removeDataset(joinItems.get(i).getForeignTable());
				}
			}
			datasetComboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						datasetComboBox.setSelectedDataset(((Dataset) (e.getItem())));
						stopCellEditing();
						joinItemsTable.setRowSelectionInterval(currentRow, currentRow);
					}
				}
			});
			return datasetComboBox;
		}

		@Override
		public Object getCellEditorValue() {
			return datasetComboBox.getSelectItem();
		}
	};
	private final DefaultCellEditor columnFieldCelleEditor = new DefaultCellEditor(new JComboBox()) {
		private JComboBox<String> comboBoxField;
		int currentRow = -1;

		@Override
		public Object getCellEditorValue() {
			return comboBoxField.getSelectedItem();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			currentRow = row;
			getComboBoxField();
			comboBoxField.setSelectedItem(value);
			return comboBoxField;
		}

		private void getComboBoxField() {
			if (comboBoxField == null) {
				comboBoxField = new JComboBox<>();
				FieldInfos fieldInfos = ((DatasetVector) currentDataset).getFieldInfos();
				for (int i = 0; i < fieldInfos.getCount(); i++) {
					comboBoxField.addItem(fieldInfos.get(i).getName());
				}
				comboBoxField.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							comboBoxField.setSelectedItem(e.getItem());
							stopCellEditing();
							joinItemsTable.setRowSelectionInterval(currentRow, currentRow);
						}
					}
				});
			}
		}
	};
	private final DefaultCellEditor columnJoinFieldCelleEditor = new DefaultCellEditor(new JComboBox()) {
		JComboBox<String> comboBoxJoinField;
		int currentRow = -1;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			currentRow = row;
			if (comboBoxJoinField == null) {
				comboBoxJoinField = new JComboBox<>();
				comboBoxJoinField.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							comboBoxJoinField.setSelectedItem(e.getItem());
							stopCellEditing();
							joinItemsTable.setRowSelectionInterval(currentRow, currentRow);
						}
					}
				});
			}
			Object joinDataset = joinItemsTable.getModel().getValueAt(row, COLUMN_TABLE);
			if (joinDataset != null) {
				FieldInfos fieldInfos = ((DatasetVector) joinDataset).getFieldInfos();
				String[] models = new String[fieldInfos.getCount()];
				for (int i = 0; i < fieldInfos.getCount(); i++) {
					models[i] = fieldInfos.get(i).getName();
				}
				comboBoxJoinField.setModel(new DefaultComboBoxModel<>(models));
				comboBoxJoinField.setSelectedItem(value);
			} else {
				comboBoxJoinField = new JComboBox<>();
			}
			return comboBoxJoinField;
		}

		@Override
		public Object getCellEditorValue() {
			return comboBoxJoinField.getSelectedItem();
		}
	};
	private final DefaultCellEditor columnTypeCellEditor = new DefaultCellEditor(new JComboBox()) {
		public int currentRow;
		private JComboBox<String> comboBoxJoinType;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			currentRow = row;
			getComboBoxJoinType();
			comboBoxJoinType.setSelectedItem(value);
			return comboBoxJoinType;
		}

		private void getComboBoxJoinType() {
			if (comboBoxJoinType == null) {
				comboBoxJoinType = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{CoreProperties.getString("String_JoinItem_Left"),
						CoreProperties.getString("String_JoinItem_Inner")}));
				comboBoxJoinType.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							comboBoxJoinType.setSelectedItem(e.getItem());
							stopCellEditing();
							joinItemsTable.setRowSelectionInterval(currentRow, currentRow);
						}
					}
				});
			}
		}

		@Override
		public Object getCellEditorValue() {
			return comboBoxJoinType.getSelectedItem();
		}
	};

	public JDialogJoinItems(JoinItems joinItems) {
		super();
		this.joinItems = joinItems.clone();
		this.setTitle(CoreProperties.getString("String_FormJoinItems_Title"));
		initComponents();
		initResources();
		initLayout();
		registListeners();
		initComponentStates();
		setTableStates();
		this.componentList.add(buttonOk);
		this.componentList.add(buttonCancel);
		this.setFocusTraversalPolicy(policy);
	}

	private void rememberTableWeight() {
		tableWeight[COLUMN_INDEX] = joinItemsTable.getColumnModel().getColumn(0).getWidth();
		tableWeight[COLUMN_NAME] = joinItemsTable.getColumnModel().getColumn(1).getWidth();
		tableWeight[COLUMN_TABLE] = joinItemsTable.getColumnModel().getColumn(2).getWidth();
		tableWeight[COLUMN_THIS_FIELD] = joinItemsTable.getColumnModel().getColumn(3).getWidth();
		tableWeight[COLUMN_JOIN_FIELD] = joinItemsTable.getColumnModel().getColumn(4).getWidth();
		tableWeight[COLUMN_EXPRESSION] = joinItemsTable.getColumnModel().getColumn(5).getWidth();
	}

	private void setTableStates() {
		joinItemsTable.setRowHeight(23);

		joinItemsTable.getColumnModel().getColumn(COLUMN_INDEX).setPreferredWidth(tableWeight[0]);
		joinItemsTable.getColumnModel().getColumn(COLUMN_NAME).setPreferredWidth(tableWeight[1]);
		joinItemsTable.getColumnModel().getColumn(COLUMN_TABLE).setPreferredWidth(tableWeight[2]);
		joinItemsTable.getColumnModel().getColumn(COLUMN_THIS_FIELD).setPreferredWidth(tableWeight[3]);
		joinItemsTable.getColumnModel().getColumn(COLUMN_JOIN_FIELD).setPreferredWidth(tableWeight[4]);
		joinItemsTable.getColumnModel().getColumn(COLUMN_EXPRESSION).setPreferredWidth(tableWeight[5]);

		// 外接表
		joinItemsTable.getColumnModel().getColumn(COLUMN_TABLE).setCellRenderer(new TableDataCellRender());
		this.columnTableCellEditor.setClickCountToStart(2);
		joinItemsTable.getColumnModel().getColumn(COLUMN_TABLE).setCellEditor(this.columnTableCellEditor);
		this.columnFieldCelleEditor.setClickCountToStart(2);
		joinItemsTable.getColumnModel().getColumn(COLUMN_THIS_FIELD).setCellEditor(this.columnFieldCelleEditor);
		this.columnJoinFieldCelleEditor.setClickCountToStart(2);
		joinItemsTable.getColumnModel().getColumn(COLUMN_JOIN_FIELD).setCellEditor(this.columnJoinFieldCelleEditor);
		this.columnTypeCellEditor.setClickCountToStart(2);
		joinItemsTable.getColumnModel().getColumn(COLUMN_TYPE).setCellEditor(this.columnTypeCellEditor);
	}

	private void initComponentStates() {
		if (joinItems != null && joinItems.getCount() > 0) {
			this.joinItemsTable.setRowSelectionInterval(0, 0);
		} else {
			checkButtonStates();
		}
	}

	private void registListeners() {
		this.scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					buttonAddClicked();
				}
			}
		});

		this.joinItemsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 && isPointAtTableUneditable(e.getPoint())) {
					buttonAddClicked();
				}
			}
		});
		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogJoinItems.this.setDialogResult(DialogResult.OK);
				JDialogJoinItems.this.dispose();
			}
		});

		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogJoinItems.this.setDialogResult(DialogResult.CANCEL);
				JDialogJoinItems.this.dispose();
			}
		});

		this.buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAddClicked();
			}
		});

		this.buttonDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rememberTableWeight();
				JDialogJoinItems.this.delete();
				setTableStates();
			}
		});

		this.buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogJoinItems.this.joinItemsTable.selectAll();
			}
		});

		this.buttonReverse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogJoinItems.this.reverse();
			}
		});

		this.joinItemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonStates();
			}
		});
	}

	private boolean isPointAtTableUneditable(Point point) {
		int column = joinItemsTable.columnAtPoint(point);
		return column == 0 || column == 5;
	}

	private void buttonAddClicked() {
		rememberTableWeight();
		DatasetChooser datasetChooser = new DatasetChooser(JDialogJoinItems.this) {
			@Override
			protected boolean isSupportDatasource(Datasource datasource) {
				return datasource == currentDataset.getDatasource();
			}

			@Override
			protected boolean isAllowedDatasetShown(Dataset dataset) {
				return ((JoinItemsTableModel) JDialogJoinItems.this.joinItemsTable.getModel()).isAddableDataset(dataset);
			}
		};
		datasetChooser.setSupportDatasetTypes(datasetTypes);
		DialogResult result = datasetChooser.showDialog();
		if (result == DialogResult.OK) {
			JDialogJoinItems.this.addNewJoinItem(datasetChooser.getSelectedDatasets());
		}
		setTableStates();
	}

	private void checkButtonStates() {
		boolean enable = this.joinItemsTable.getRowCount() > 0;
		this.buttonSelectAll.setEnabled(enable);
		this.buttonReverse.setEnabled(enable);
		if ((this.joinItemsTable.getSelectedRows() != null && this.joinItemsTable.getSelectedRows().length > 0) != buttonDel.isEnabled()) {
			this.buttonDel.setEnabled(this.joinItemsTable.getSelectedRows() != null && this.joinItemsTable.getSelectedRows().length > 0);
		}
	}

	private void reverse() {
		TableUtilities.invertSelection(joinItemsTable);
	}

	private void delete() {
		int[] selectRows = this.joinItemsTable.getSelectedRows();
		if (selectRows != null && selectRows.length > 0) {
			((JoinItemsTableModel) this.joinItemsTable.getModel()).deleteRows(selectRows);
			if (this.joinItemsTable.getRowCount() > 0 && selectRows[0] < this.joinItemsTable.getRowCount()) {
				this.joinItemsTable.setRowSelectionInterval(selectRows[0], selectRows[0]);
			} else if (this.joinItemsTable.getRowCount() > 0) {
				this.joinItemsTable.setRowSelectionInterval(this.joinItemsTable.getRowCount() - 1, this.joinItemsTable.getRowCount() - 1);
			}
		}
	}

	private void addNewJoinItem(List<Dataset> selectedDatasets) {
		((JoinItemsTableModel) this.joinItemsTable.getModel()).addNewJoinItem(selectedDatasets);
		this.joinItemsTable.addRowSelectionInterval(this.joinItemsTable.getRowCount() - 1, this.joinItemsTable.getRowCount() - 1);
		this.joinItemsTable.scrollRectToVisible(this.joinItemsTable.getCellRect(this.joinItemsTable.getRowCount() - 1, 0, true));
	}

	private void initComponents() {
		this.setSize(1000, 500);
		this.toolBar.setFloatable(false);
		scrollPane.setViewportView(joinItemsTable);
		joinItemsTable.setJoinItems(joinItems);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


	}

	private void initResources() {
//		this.buttonAdd.setText(CommonProperties.getString(CommonProperties.Add));
//		this.buttonDel.setText(CommonProperties.getString(CommonProperties.Delete));
//		this.buttonSelectAll.setText(ControlsProperties.getString("String_SelectAll"));
//		this.buttonReverse.setText(ControlsProperties.getString("String_SelectReverse"));
		this.buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));

		this.buttonAdd.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonReverse.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonDel.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
	}

	public JoinItems getJoinItems() {
		JoinItems joinItems = this.joinItemsTable.getJoinItems();
		for (int i = joinItems.getCount() - 1; i >= 0; i--) {
			if (StringUtilities.isNullOrEmpty(joinItems.get(i).getForeignTable())) {
				joinItems.remove(i);
			}
		}
		return joinItems;
	}

	private void initLayout() {
		initToolbar();
		initPanelButton();
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 0));
		centerPanel.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 100));
		centerPanel.add(panelButton, new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(5, 0, 0, 0));

		this.setLayout(new GridBagLayout());
		this.add(centerPanel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraintsHelper.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(10));
	}

	private void initToolbar() {
		this.toolBar.add(buttonAdd);
		this.toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		this.toolBar.add(buttonSelectAll);
		this.toolBar.add(buttonReverse);
		this.toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		this.toolBar.add(buttonDel);
	}

	private void initPanelButton() {
		panelButton.setLayout(new GridBagLayout());
//		panelButton.add(buttonAdd, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(0, 10, 0, 0));
//		panelButton.add(buttonDel, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(0, 10, 0, 0));
//		panelButton.add(buttonSelectAll, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(0, 10, 0, 0));
//		panelButton.add(buttonReverse, new GridBagConstraintsHelper(3, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(0, 10, 0, 0));
		panelButton.add(buttonOk, new GridBagConstraintsHelper(4, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(1, 1).setInsets(0, 0, 0, 10));
		panelButton.add(buttonCancel, new GridBagConstraintsHelper(5, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(0, 1).setInsets(0, 0, 0, 10));
	}

	public Dataset getCurrentDataset() {
		return currentDataset;
	}

	public void setCurrentDataset(Dataset currentDataset) {
		this.currentDataset = currentDataset;
		this.joinItemsTable.setCurrentDataset(this.currentDataset);
	}

	class JoinItemsTable extends JTable {
		private JoinItems joinItems = null;
		private Dataset currentDataset;

		public JoinItemsTable() {
			super();
			this.setModel(new JoinItemsTableModel());
		}

		public JoinItems getJoinItems() {
			this.joinItems = ((JoinItemsTableModel) this.getModel()).getJoinItems();
			return this.joinItems;
		}

		public void setJoinItems(JoinItems joinItems) {
			this.joinItems = joinItems;
			((JoinItemsTableModel) this.getModel()).setJoinItems(joinItems);
		}

		public void setCurrentDataset(Dataset correntDataset) {
			this.currentDataset = correntDataset;
			((JoinItemsTableModel) this.getModel()).setCurrentDataset(correntDataset);
		}

		public Dataset getCurrentDataset() {
			return currentDataset;
		}
	}

	class JoinItemsTableModel extends DefaultTableModel {
		private boolean[] editable = {false, true, true, true, true, false, true};
		private String[] headers = new String[]{CommonProperties.getString("String_ColumnHeader_Index"), CoreProperties.getString("String_Name"),
				CoreProperties.getString("String_JoinItem_ForeignTable"), CoreProperties.getString("String_JoinItem_Field"),
				CoreProperties.getString("String_JoinItem_ForeignField"), CoreProperties.getString("String_JoinItem_Filter"),
				CoreProperties.getString("String_JoinItem_JoinType")};

		private Dataset currentDataset = null;

		private JoinItems joinItems = null;

		public JoinItems getJoinItems() {
			return joinItems;
		}

		public void setJoinItems(JoinItems joinItems) {
			this.joinItems = joinItems;
		}

		@Override
		public int getRowCount() {
			if (joinItems == null) {
				return 0;
			} else {
				return joinItems.getCount();
			}
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (row != -1 && row < joinItems.getCount()) {
				switch (column) {
					case 0:
						// 序号
						return row + 1;
					case 1:
						// 名称
						return joinItems.get(row).getName();
					case 2:
						// 外接表
						return DatasetUIUtilities.getDatasetFromDatasource(joinItems.get(row).getForeignTable(), currentDataset.getDatasource());
					case 3:
						// 本表字段
						return StringUtilities.isNullOrEmpty(joinItems.get(row).getJoinFilter()) ? "" : joinItems.get(row).getJoinFilter().split("=")[0]
								.split("\\.")[1];
					case 4:
						// 外接表字段
						return StringUtilities.isNullOrEmpty(joinItems.get(row).getJoinFilter()) ? "" : joinItems.get(row).getJoinFilter().split("=")[1]
								.split("\\.")[1];
					case 5:
						// 关联表达式
						return joinItems.get(row).getJoinFilter();
					case 6:
						return JoinTypeUtilties.getJoinTypeName(joinItems.get(row).getJoinType());
					default:
						return "";
				}
			}
			return "";
		}

		@Override
		public int getColumnCount() {
			return headers.length;
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (aValue == null && column != 1) {
				return;
			}
			String value = String.valueOf(aValue);
			if (aValue == null) {
				value = "";
			}
			if (StringUtilities.isNullOrEmpty(value) && column != 1) {
				return;
			}
			JoinItem joinItem = joinItems.get(row);
			if (StringUtilities.isNullOrEmpty(joinItem.getForeignTable()) && column != 1) {
				return;
			}
			switch (column) {
				case 1:
					joinItem.setName(value);
					break;
				case 2:
					joinItem.setForeignTable(value);
					int startIndex0 = joinItem.getJoinFilter().indexOf("=");
					int endIndex0 = joinItem.getJoinFilter().lastIndexOf(".");
					int filterLength = joinItem.getJoinFilter().length();
					StringBuilder builder0 = new StringBuilder(joinItem.getJoinFilter());

					String joinField = joinItem.getJoinFilter().substring(endIndex0 + 1, filterLength);
					FieldInfos fieldInfos = ((DatasetVector) currentDataset.getDatasource().getDatasets().get(value)).getFieldInfos();
					boolean isExists = false;
					for (int i = 0; i < fieldInfos.getCount(); i++) {
						if (fieldInfos.get(i).getName().equals(joinField)) {
							isExists = true;
							break;
						}
					}
					if (isExists) {
						builder0.replace(startIndex0 + 1, endIndex0, value);
					} else {
						builder0.replace(startIndex0 + 1, filterLength, value + "." + fieldInfos.get(0).getName());
					}
					joinItem.setJoinFilter(builder0.toString());
					fireTableDataChanged();
					break;
				case 3:
					int startIndex = joinItem.getJoinFilter().indexOf(".");
					int endIndex = joinItem.getJoinFilter().indexOf("=");
					StringBuilder builder = new StringBuilder(joinItem.getJoinFilter());
					builder.replace(startIndex + 1, endIndex, value);
					joinItem.setJoinFilter(builder.toString());
					fireTableDataChanged();
					break;
				case 4:
					int startIndex1 = joinItem.getJoinFilter().lastIndexOf(".");
					int endIndex1 = joinItem.getJoinFilter().length();
					StringBuilder builder1 = new StringBuilder(joinItem.getJoinFilter());
					builder1.replace(startIndex1 + 1, endIndex1, value);
					joinItem.setJoinFilter(builder1.toString());
					fireTableDataChanged();
					break;
				case 6:
					JoinType joinTypeValue = JoinTypeUtilties.getJoinTypeValue(value);
					if (joinTypeValue != null) {
						joinItem.setJoinType(joinTypeValue);
					}
					break;
				default:
					Application.getActiveApplication().getOutput().output("How did you do it?");
			}
		}

		@Override
		public String getColumnName(int column) {
			return headers[column];
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return editable[column];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 6) {
				return JoinType.class;
			} else if (columnIndex == 2) {
				return Dataset.class;
			} else {
				return String.class;
			}
		}

		public Dataset getCurrentDataset() {
			return currentDataset;
		}

		public void setCurrentDataset(Dataset currentDataset) {
			this.currentDataset = currentDataset;
		}

		public void addNewJoinItem(List<Dataset> selectedDatasets) {
			for (Dataset joinDataset : selectedDatasets) {
				if (isAddableDataset(joinDataset)) {
					JoinItem joinItem = new JoinItem();
					joinItem.setName(MessageFormat.format("{0}{1}", "JoinItem", String.valueOf(joinItems.getCount())));
					joinItem.setJoinType(JoinType.LEFTJOIN);

					if (joinDataset != null) {
						String builder = currentDataset.getName() + "." + ((DatasetVector) currentDataset).getFieldInfos().get(0).getName() + "="
								+ joinDataset.getName() + "." + ((DatasetVector) joinDataset).getFieldInfos().get(0).getName();
						joinItem.setForeignTable(joinDataset.getName());
						joinItem.setJoinFilter(builder);
					} else {
						joinItem.setForeignTable("");
						joinItem.setJoinFilter("");
					}
					this.joinItems.add(joinItem);
				}
			}
			fireTableStructureChanged();
		}

		private boolean isAddableDataset(Dataset dataset) {
			if (dataset == currentDataset) {
				return false;
			} else if (joinItems == null) {
				// 基本不会运行到这，以防万一还是加上
				return true;
			} else {
				for (int i = 0; i < joinItems.getCount(); i++) {
					if (joinItems.get(i).getForeignTable().equals(dataset.getName())) {
						return false;
					}
				}
			}
			return true;
		}

//		private Dataset getForeignDataset(Dataset currentDataset) {
//			Datasource datasource = currentDataset.getDatasource();
//			for (int i = 0; i < datasource.getDatasets().getCount(); i++) {
//				if (datasource.getDatasets().get(i) instanceof DatasetVector && datasource.getDatasets().get(i) != currentDataset) {
//					return datasource.getDatasets().get(i);
//				}
//			}
//			return null;
//		}

		public void deleteRows(int[] selectRows) {
			for (int i = selectRows.length - 1; i >= 0; i--) {
				this.deleteRow(selectRows[i]);
			}
			fireTableStructureChanged();
		}

		private boolean deleteRow(int selectRow) {
			return this.joinItems.remove(selectRow);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
