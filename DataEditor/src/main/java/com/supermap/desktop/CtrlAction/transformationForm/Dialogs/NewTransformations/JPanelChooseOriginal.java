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
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
public class JPanelChooseOriginal extends JPanelNewTransformationBase {

	private JPanel panelCenter;

	private JToolBar toolBar;
	private SmButton buttonAdd;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonDel;

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
		if (buttonNext != null) {
			buttonNext.setEnabled(table.getRowCount() > 0);
		}
	}

	private void buttonAddClicked() {
		DatasetChooser datasetChooser = new DatasetChooser();
		datasetChooser.setSupportDatasetTypes(supportDatasetTypes);
		if (datasetChooser.showDialog() == DialogResult.OK) {
			java.util.List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();
			if (selectedDatasets.size() > 0) {
				Datasource defaultDatasource = TransformationUtilties.getDefaultDatasource(selectedDatasets.get(0).getDatasource());
				int count = 0;
				for (Dataset selectedDataset : selectedDatasets) {
					if (tableModel.addDataset(selectedDataset, defaultDatasource,
							defaultDatasource == null ? null : defaultDatasource.getDatasets().getAvailableDatasetName(selectedDataset.getName() + "_adjust"))) {
						++count;
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
}