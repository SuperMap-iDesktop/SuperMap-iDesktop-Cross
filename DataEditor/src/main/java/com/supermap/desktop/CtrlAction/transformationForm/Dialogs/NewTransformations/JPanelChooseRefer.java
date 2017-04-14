package com.supermap.desktop.CtrlAction.transformationForm.Dialogs.NewTransformations;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooseMode;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.TableUtilities;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author XiaJT
 */
public class JPanelChooseRefer extends JPanelNewTransformationBase {

	private JPanel panelCenter;
	private JToolBar toolBar;
	private SmButton buttonAdd;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonDel;

	private JScrollPane scrollPane;
	private SmSortTable table;
	private ReferenceLayersTableModel tableModel;

	private static final DatasetType[] datasetTypes = new DatasetType[]{
			DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.TEXT, DatasetType.CAD, DatasetType.NETWORK,
			DatasetType.LINEM, DatasetType.GRID, DatasetType.IMAGE, DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D,
			DatasetType.GRIDCOLLECTION, DatasetType.IMAGECOLLECTION, DatasetType.PARAMETRICLINE, DatasetType.PARAMETRICREGION,
			DatasetType.NETWORK3D
	};

	public JPanelChooseRefer() {

	}

	private void initTable() {
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		TableDataCellRender cellRenderer = new TableDataCellRender();
		table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
	}

	private void initToolbar() {
		toolBar.setFloatable(false);
		toolBar.add(buttonAdd);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonDel);
	}

	@Override
	protected String getPanelTitle() {
		return DataEditorProperties.getString("String_ChooseRefer");
	}

	@Override
	protected String getDescribeText() {
		return DataEditorProperties.getString("String_ChooseReferDescribe");
	}

	@Override
	protected JPanel getCenterPanel() {
		init();
		return panelCenter;
	}

	private void init() {
		initComponents();
		initLayouts();
		initListener();
		initResources();
		initComponentState();
		setComponentName();
	}

	private void initComponents() {
		panelCenter = new JPanel();
		toolBar = new JToolBar();
		buttonAdd = new SmButton();
		buttonSelectAll = new SmButton();
		buttonSelectInvert = new SmButton();
		buttonDel = new SmButton();
		scrollPane = new JScrollPane();
		table = new SmSortTable();
		tableModel = new ReferenceLayersTableModel();

		initTable();

	}
	private void setComponentName() {
		ComponentUIUtilities.setName(this.panelCenter, "JPanelChooseRefer_panelCenter");
		ComponentUIUtilities.setName(this.toolBar, "JPanelChooseRefer_toolBar");
		ComponentUIUtilities.setName(this.buttonAdd, "JPanelChooseRefer_buttonAdd");
		ComponentUIUtilities.setName(this.buttonSelectAll, "JPanelChooseRefer_buttonSelectAll");
		ComponentUIUtilities.setName(this.buttonSelectInvert, "JPanelChooseRefer_buttonSelectInvert");
		ComponentUIUtilities.setName(this.buttonDel, "JPanelChooseRefer_buttonDel");
		ComponentUIUtilities.setName(this.scrollPane, "JPanelChooseRefer_scrollPane");
		ComponentUIUtilities.setName(this.table, "JPanelChooseRefer_table");
	}
	private void initLayouts() {
		initToolbar();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
		panelCenter.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));

	}

	private void initListener() {
		buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAddClicked();
			}
		});
		buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				table.setRowSelectionInterval(0, table.getRowCount() - 1);
			}
		});
		buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.invertSelection(table);
			}
		});
		buttonDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedModelRows = table.getSelectedModelRows();
				if (selectedModelRows.length == 0) {
					return;
				}
				Arrays.sort(selectedModelRows);
				for (int i = selectedModelRows.length - 1; i >= 0; i--) {
					tableModel.removeDataAt(selectedModelRows[i]);
				}
				int row = selectedModelRows[0];
				if (tableModel.getRowCount() <= row) {
					row = tableModel.getRowCount() - 1;
				}
				if (row != -1) {
					table.setRowSelectionInterval(row, row);
				}
			}
		});
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonState();
			}
		});
		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() != TableModelEvent.DELETE) {
					checkButtonState();
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					buttonAddClicked();
				}
			}
		});
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					buttonAddClicked();
				}
			}
		});
	}


	private void buttonAddClicked() {
		DatasetChooser datasetChooser = new DatasetChooser(DatasetChooseMode.DATASET, DatasetChooseMode.MAP);
		datasetChooser.setSupportDatasetTypes(datasetTypes);
		if (datasetChooser.showDialog() == DialogResult.OK) {
			java.util.List<Object> selectedObjects = datasetChooser.getSelectedObjects();
			for (Object selectedObject : selectedObjects) {
				tableModel.addData(selectedObject);
			}
		}
	}

	private void checkButtonState() {
		buttonSelectAll.setEnabled(table.getRowCount() > 0);
		buttonSelectInvert.setEnabled(table.getRowCount() > 0);
		buttonDel.setEnabled(table.getSelectedRowCount() > 0);
	}

	private void initResources() {
		buttonAdd.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));
		buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		buttonDel.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
	}

	private void initComponentState() {

	}

	@Override
	protected void setButtonNext(SmButton buttonNext) {

	}

	public List<Object> getReferenceObjects() {
		return tableModel.getDataList();
	}

	private class ReferenceLayersTableModel extends DefaultTableModel {
		private ArrayList<Object> dataList = new ArrayList<>();
		private String[] columnNames = new String[]{
				CommonProperties.getString(CommonProperties.stringDataset),
				CommonProperties.getString(CommonProperties.stringDatasource),
		};

		@Override
		public int getRowCount() {
			if (dataList == null) {
				return 0;
			}
			return dataList.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (row >= dataList.size()) {
				if (column == 0) {
					return ControlsProperties.getString("String_AddMore");
				}
				return "";
			}
			if (column == 0) {
				return dataList.get(row);
			} else if (column == 1) {
				Object data = dataList.get(row);
				if (data instanceof Dataset) {
					return ((Dataset) data).getDatasource();
				}
			}
			return "";
		}


		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return Object.class;
			}
			return String.class;
		}

		public java.util.List<Object> getDataList() {
			return dataList;
		}

		public void addData(Object selectedObject) {
			dataList.add(selectedObject);
			fireTableRowsInserted(dataList.size() - 1, dataList.size() - 1);
		}

		public void removeDataAt(int row) {
			if (row >= getRowCount()) {
				throw new UnsupportedOperationException(String.valueOf(row + "_" + getRowCount()));
			}
			Object o = dataList.get(row);
			if (o instanceof com.supermap.mapping.Map) {
				((Map) o).close();
			}
			dataList.remove(o);
			fireTableRowsDeleted(row, row);
		}
	}
}
