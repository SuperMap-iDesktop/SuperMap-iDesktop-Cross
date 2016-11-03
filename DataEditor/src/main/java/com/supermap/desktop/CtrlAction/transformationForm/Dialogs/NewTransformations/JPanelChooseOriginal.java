package com.supermap.desktop.CtrlAction.transformationForm.Dialogs.NewTransformations;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.Dialogs.TransformationTableModel;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationUtilties;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author XiaJT
 */
public class JPanelChooseOriginal extends JPanelNewTransformationBase {

	private JPanel panelCenter;

	private JToolBar toolBar;
	private SmButton buttonAdd;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonDel;
	private SmButton buttonSetting;

	private JScrollPane scrollPane;
	private SmSortTable table;
	private TransformationTableModel tableModel;
	private static final DatasetType[] supportDatasetTypes = new DatasetType[]{
			DatasetType.POINT,
			DatasetType.LINE,
			DatasetType.REGION,
			DatasetType.POINT3D,
			DatasetType.LINE3D,
			DatasetType.REGION3D,
			DatasetType.TEXT,
			DatasetType.CAD,
			DatasetType.NETWORK,
			DatasetType.GRID,
			DatasetType.IMAGE
	};

	private SmButton buttonNext;

	public JPanelChooseOriginal() {

	}

	private void init() {
		initComponents();
		initLayouts();
		initListener();
		initResources();
		initComponentState();
		table.getColumnModel().getColumn(TransformationTableModel.COLUMN_SAVE_AS).setMaxWidth(50);
	}

	private void initComponents() {
		panelCenter = new JPanel();
		toolBar = new JToolBar();
		buttonAdd = new SmButton();
		buttonSelectAll = new SmButton();
		buttonSelectInvert = new SmButton();
		buttonDel = new SmButton();
		buttonSetting = new SmButton();
		scrollPane = new JScrollPane();
		table = new SmSortTable();
		tableModel = new TransformationTableModel();

		initTable();
	}

	private void initTable() {
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		ArrayList<Datasource> datasourceArrayList = new ArrayList<>();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).isOpened() && !datasources.get(i).isReadOnly()) {
				datasourceArrayList.add(datasources.get(i));
			}
		}
		JComboBox<Datasource> datasourceJComboBox = new JComboBox<>(datasourceArrayList.toArray(new Datasource[datasourceArrayList.size()]));
		ListCellRenderer<Datasource> renderer = new ListCellRenderer<Datasource>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Datasource> list, Datasource value, int index, boolean isSelected, boolean cellHasFocus) {
				DataCell dataCell = new DataCell(value);
				dataCell.setOpaque(true);
				dataCell.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
				return dataCell;
			}
		};
		datasourceJComboBox.setRenderer(renderer);
		DefaultCellEditor cellEditor = new DefaultCellEditor(datasourceJComboBox);
		cellEditor.setClickCountToStart(2);
		table.setDefaultEditor(Datasource.class, cellEditor);
	}

	private void initLayouts() {
		initToolbar();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
		panelCenter.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	private void initToolbar() {
		toolBar.setFloatable(false);
		toolBar.add(buttonAdd);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonDel);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonSetting);
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
					tableModel.delete(selectedModelRows[i]);
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
		buttonSetting.addActionListener(new ActionListener() {
			private JDialogSetting jDialogSetting = new JDialogSetting();

			@Override
			public void actionPerformed(ActionEvent e) {
				jDialogSetting.initData();
				if (this.jDialogSetting.showDialog() == DialogResult.OK) {
					boolean selected = jDialogSetting.checkBox.isSelected();
					Datasource selectedItem = null;
					if (selected) {
						selectedItem = (Datasource) jDialogSetting.comboBoxDatasources.getSelectedItem();
					}
					int[] selectedModelRows = table.getSelectedModelRows();
					if (selectedModelRows.length > 0) {
						for (int selectedModelRow : selectedModelRows) {
							if (tableModel.getValueAt(selectedModelRow, TransformationTableModel.COLUMN_SAVE_AS) != selected && tableModel.isCellEditable(selectedModelRow, TransformationTableModel.COLUMN_SAVE_AS)) {
								tableModel.setValueAt(selected, selectedModelRow, TransformationTableModel.COLUMN_SAVE_AS);
							}
							if ((Boolean) tableModel.getValueAt(selectedModelRow, TransformationTableModel.COLUMN_SAVE_AS) && selectedItem != null && tableModel.isCellEditable(selectedModelRow, TransformationTableModel.column_ResultDatasource)) {
								tableModel.setValueAt(selectedItem, selectedModelRow, TransformationTableModel.column_ResultDatasource);
							}
						}
					}
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

	private void checkButtonState() {
		buttonSelectAll.setEnabled(table.getRowCount() > 0);
		buttonSelectInvert.setEnabled(table.getRowCount() > 0);
		buttonDel.setEnabled(table.getSelectedRowCount() > 0);
		buttonSetting.setEnabled(table.getSelectedRowCount() > 0);
		if (buttonNext != null) {
			buttonNext.setEnabled(table.getRowCount() > 0);
		}
	}

	private void buttonAddClicked() {
		DatasetChooser datasetChooser = new DatasetChooser(DatasetChooseMode.DATASET, DatasetChooseMode.MAP);
		datasetChooser.setSupportDatasetTypes(supportDatasetTypes);
		if (datasetChooser.showDialog() == DialogResult.OK) {
			java.util.List<Object> selectedDatasets = datasetChooser.getSelectedObjects();
			if (selectedDatasets.size() > 0) {
				Datasource defaultDatasource = null;
				Datasource usableDatasource;
				int count = 0;
				for (int i = selectedDatasets.size() - 1; i >= 0; i--) {
					Object selectedDataset = selectedDatasets.get(i);
					if (selectedDataset instanceof Map) {
						selectedDatasets.remove(selectedDataset);
						List<Object> mapDatasets = TransformationUtilties.getMapDatasets((Map) selectedDataset);
						for (Object mapDataset : mapDatasets) {
							if (!selectedDatasets.contains(mapDataset)) {
								selectedDatasets.add(mapDataset);
							}
						}
					}
				}
				for (Object selectedDataset : selectedDatasets) {
					if (selectedDataset instanceof Dataset) {
						if (defaultDatasource == null) {
							defaultDatasource = TransformationUtilties.getDefaultDatasource(((Dataset) selectedDataset).getDatasource());
						}
						Datasource datasource = ((Dataset) selectedDataset).getDatasource();
						usableDatasource = datasource.isReadOnly() ? defaultDatasource : datasource;
						if (tableModel.addDataset((Dataset) selectedDataset, usableDatasource,
								defaultDatasource == null ? "" : defaultDatasource.getDatasets().getAvailableDatasetName(((Dataset) selectedDataset).getName() + "_adjust"))) {
							++count;
						}
					}
				}
				if (count != 0) {
					table.setRowSelectionInterval(table.getRowCount() - count, table.getRowCount() - 1);
				}
			}
		}
		datasetChooser.dispose();
	}

	private void initResources() {
		buttonAdd.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddMap.png"));
		buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		buttonDel.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		buttonSetting.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Setting.PNG"));
	}

	private void initComponentState() {
		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
		if (activeDatasets != null && activeDatasets.length > 0) {
			Datasource defaultDatasource = TransformationUtilties.getDefaultDatasource(activeDatasets[0].getDatasource());
			if (defaultDatasource != null) {
				for (Dataset activeDataset : activeDatasets) {
					tableModel.addDatas(new TransformationAddObjectBean(activeDataset, defaultDatasource, defaultDatasource.getDatasets().getAvailableDatasetName(activeDataset.getName() + "_adjust")));
				}
			}
		} else {
			checkButtonState();
		}
	}

	@Override
	protected String getPanelTitle() {
		return DataEditorProperties.getString("String_ChooseOriginal");
	}

	@Override
	protected String getDescribeText() {
		return DataEditorProperties.getString("String_ChooseOriginalDescribe");
	}

	@Override
	protected JPanel getCenterPanel() {
		if (panelCenter == null) {
			init();
		}
		return panelCenter;
	}

	@Override
	protected void setButtonNext(SmButton buttonNext) {
		this.buttonNext = buttonNext;
		buttonNext.setEnabled(table.getRowCount() > 0);
	}

	public List<Object> getTargetObjects() {
		List<TransformationAddObjectBean> datas = tableModel.getDatas();
		ArrayList<Object> objects = new ArrayList<>();
		objects.addAll(datas);
		return objects;
	}

	private class JDialogSetting extends SmDialog {
		private TristateCheckBox checkBox = new TristateCheckBox();
		private JComboBox<Datasource> comboBoxDatasources = new JComboBox<>();
		private SmButton buttonOk = new SmButton();
		private SmButton buttonCancle = new SmButton();

		public JDialogSetting() {
			this.setTitle(CoreProperties.getString("String_toolStripButtonAdvanced"));
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				if (datasources.get(i).isOpened() && !datasources.get(i).isReadOnly()) {
					comboBoxDatasources.addItem(datasources.get(i));
				}
			}
			ListCellRenderer<Datasource> renderer = new ListCellRenderer<Datasource>() {
				@Override
				public Component getListCellRendererComponent(JList<? extends Datasource> list, Datasource value, int index, boolean isSelected, boolean cellHasFocus) {
					DataCell dataCell = new DataCell(value);
					dataCell.setOpaque(true);
					dataCell.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
					return dataCell;
				}
			};
			comboBoxDatasources.setRenderer(renderer);
			comboBoxDatasources.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (comboBoxDatasources.getSelectedItem() != null && (checkBox.isSelectedEx() == null || !checkBox.isSelectedEx())) {
						checkBox.setSelected(true);
					}
				}
			});
			checkBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					comboBoxDatasources.setEnabled(checkBox.isSelected());
				}
			});
			buttonOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.OK;
					dispose();
				}
			});

			buttonCancle.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					dispose();
				}
			});
			buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
			buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
			checkBox.setText(DataEditorProperties.getString("String_Transformation_CheckBoxResaveDataset"));

			JPanel panelButton = new JPanel();

			panelButton.setLayout(new GridBagLayout());
			panelButton.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST));
			panelButton.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 0, 0));

			this.setLayout(new GridBagLayout());
			this.add(checkBox, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setInsets(20, 20, 0, 0));
			this.add(comboBoxDatasources, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(20, 5, 0, 20));
			this.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
			this.add(panelButton, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 20, 10, 10));
		}

		public void initData() {
			int[] selectedModelRows = table.getSelectedModelRows();
			ArrayList<TransformationAddObjectBean> beans = new ArrayList<>();
			for (int selectedModelRow : selectedModelRows) {
				beans.add(tableModel.getDataAtRow(selectedModelRow));
			}
			if (beans.size() > 0) {
				boolean isSaveAs = true;
				boolean isDatasourceSame = true;
				if (beans.size() > 1) {
					for (int i = 1; i < beans.size(); i++) {
						if (beans.get(i).isSaveAs() ^ beans.get(0).isSaveAs()) {
							isSaveAs = false;
						}
						if (beans.get(i).getResultDatasource() != beans.get(0).getResultDatasource()) {
							isDatasourceSame = false;
						}
					}
				}
				if (isSaveAs) {
					checkBox.setSelected(beans.get(0).isSaveAs());
				} else {
					checkBox.setSelectedEx(null);
				}
				comboBoxDatasources.setEnabled(checkBox.isSelected());
				if (isDatasourceSame) {
					comboBoxDatasources.setSelectedItem(beans.get(0).getResultDatasource());
				} else {
					comboBoxDatasources.setSelectedItem(null);
				}
			}
			this.pack();
			this.setLocationRelativeTo(null);
			buttonOk.requestFocus();
		}
	}
}