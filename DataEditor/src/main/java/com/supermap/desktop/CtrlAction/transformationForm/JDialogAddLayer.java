package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooseMode;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class JDialogAddLayer extends SmDialog {
//	private JLabel labelTargetMap = new JLabel();
//	private JComboBox<FormTransformationSubFormType> comboBoxTargetMap = new JComboBox<>();

	private JScrollPane scrollPane = new JScrollPane();
	private JTable table = new JTable();
	private TableModelAddLayer tableModelAddLayer = new TableModelAddLayer();

	private JToolBar toolBar = new JToolBar();
	private SmButton buttonAdd = new SmButton();
	private SmButton buttonDelete = new SmButton();
	private SmButton buttonUp = new SmButton();
	private SmButton buttonDown = new SmButton();
	private SmButton buttonSelectedAll = new SmButton();
	private SmButton buttonSelectedInvert = new SmButton();
//	private SmButton buttonSetting = new SmButton();

	private SmButton smButtonOk = new SmButton();
	private SmButton smButtonCancle = new SmButton();

	private static final DatasetType[] datasetTypes = new DatasetType[]{
			DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.TEXT, DatasetType.CAD, DatasetType.NETWORK,
			DatasetType.LINEM, DatasetType.GRID, DatasetType.IMAGE, DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D,
			DatasetType.GRIDCOLLECTION, DatasetType.IMAGECOLLECTION, DatasetType.PARAMETRICLINE, DatasetType.PARAMETRICREGION,
			DatasetType.NETWORK3D
	};

	public JDialogAddLayer() {
		init();
	}

	private void init() {
		setSize(500, 380);
		setLocationRelativeTo(null);
		initComponent();
		initListener();
		initLayout();
		initResources();
		initComponentState();
	}

	private void initComponent() {
		initToolBar();
		initTable();

		scrollPane.setViewportView(table);
	}

	private void initToolBar() {
		toolBar.setFloatable(false);
		toolBar.add(buttonAdd);
		toolBar.add(buttonDelete);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonSelectedAll);
		toolBar.add(buttonSelectedInvert);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonUp);
		toolBar.add(buttonDown);
	}

	private void initTable() {
		table.setModel(tableModelAddLayer);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(23);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		TableDataCellRender cellRenderer = new TableDataCellRender();
		table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
		FormTransformationSubFormType[] subFormTypes = {FormTransformationSubFormType.Target, FormTransformationSubFormType.Reference};
		table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox<>(subFormTypes)));
	}

	private void initListener() {
		buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButtonClicked();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2
						&& table.isCellEditable(table.rowAtPoint(point), table.columnAtPoint(point))) {
					addButtonClicked();
				}
			}
		});
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				tableSelectionChanged();
			}
		});
		buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModelAddLayer.delete(table.getSelectedRows());
			}
		});
		buttonSelectedAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.getRowCount() > 0) {
					table.setRowSelectionInterval(0, table.getRowCount() - 1);
				}
			}
		});
		buttonSelectedInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.invertSelection(table);
			}
		});

		buttonUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: 2016/9/13
			}
		});
		buttonDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: 2016/9/13
			}
		});
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					addButtonClicked();
				}
			}
		});
		smButtonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOkClicked();
			}
		});
		smButtonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.OK;
				dispose();
			}
		});
	}

	private void buttonOkClicked() {
		dialogResult = DialogResult.OK;
		dispose();
		// TODO: 2016/9/13
	}

	public ArrayList<AddLayerItemBean> getAddItems() {
		return tableModelAddLayer.getDatas();
	}

	private void tableSelectionChanged() {

	}

	private void initLayout() {
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());
		jPanel.add(smButtonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 10, 10, 0).setFill(GridBagConstraints.NONE));
		jPanel.add(smButtonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 10, 10).setFill(GridBagConstraints.NONE));

		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 10));
		this.add(scrollPane, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10).setAnchor(GridBagConstraints.CENTER));

		this.add(jPanel, new GridBagConstraintsHelper(0, 3, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));
	}

	private void initResources() {
		smButtonOk.setText(CommonProperties.getString(CommonProperties.OK));
		smButtonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));

	}

	private void initComponentState() {
		this.buttonAdd.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddMap.png"));
		this.buttonAdd.setToolTipText(CommonProperties.getString(CommonProperties.Add));
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonDelete.setToolTipText(CommonProperties.getString(CommonProperties.Delete));
		this.buttonSelectedAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonSelectedAll.setToolTipText(CommonProperties.getString(CommonProperties.selectAll));
		this.buttonSelectedInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonSelectedInvert.setToolTipText(CommonProperties.getString(CommonProperties.selectInverse));
		this.buttonUp.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveUp.png"));
		this.buttonUp.setToolTipText(CommonProperties.getString(CommonProperties.up));
		this.buttonDown.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveDown.png"));
		this.buttonDown.setToolTipText(CommonProperties.getString(CommonProperties.down));
	}

	private void addButtonClicked() {
		DatasetChooser datasetChooser = new DatasetChooser(DatasetChooseMode.DATASET, DatasetChooseMode.MAP);
		datasetChooser.setSupportDatasetTypes(datasetTypes);
		if (datasetChooser.showDialog() == DialogResult.OK) {
			java.util.List<Object> selectedObjects = datasetChooser.getSelectedObjects();
			tableModelAddLayer.addDatas(selectedObjects.toArray(new Object[selectedObjects.size()]));
		}
	}

	protected class TableModelAddLayer extends DefaultTableModel {

		private ArrayList<AddLayerItemBean> datas = new ArrayList<>();
		private String[] columnNames = new String[]{
				CommonProperties.getString(CommonProperties.Index),
				CommonProperties.getString(CommonProperties.stringDataset),
				CommonProperties.getString(CommonProperties.stringDatasource),
				DataEditorProperties.getString("String_AddLayerTarget")
		};

		@Override
		public int getRowCount() {
			if (datas == null) {
				return 0;
			}
			return datas.size();
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
			return column == 3;
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (aValue == null) {
				return;
			}
			datas.get(row).setType(((FormTransformationSubFormType) aValue));
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column == 0) {
				return row + 1;
			} else if (column == 1) {
				return datas.get(row).getData();
			} else if (column == 2) {
				Object object = datas.get(row).getData();
				return object instanceof Dataset ? ((Dataset) object).getDatasource() : "";
			} else if (column == 3) {
				return datas.get(row).getType();
			}
			return super.getValueAt(row, column);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 3) {
				return FormTransformationSubFormType.class;
			}
			return super.getColumnClass(columnIndex);
		}


		public int addDatas(Object... objects) {
			int count = 0;
			for (Object object : objects) {
				if (!isContain(object)) {
					datas.add(getRowData(object));
					count++;
				}
			}
			fireTableRowsInserted(datas.size() - count, datas.size() - 1);
			return count;
		}

		public boolean isContain(Object object) {
			for (AddLayerItemBean data : datas) {
				if (data.getData() == object) {
					return true;
				}
			}
			return false;
		}

		public void delete(int... rows) {
			for (int i = rows.length - 1; i >= 0; i--) {
				datas.remove(rows[i]);
				fireTableRowsDeleted(rows[i], rows[i]);
			}
		}

		public ArrayList<AddLayerItemBean> getDatas() {
			return datas;
		}

		protected AddLayerItemBean getRowData(Object object) {
			return new AddLayerItemBean(object);
		}
	}

}