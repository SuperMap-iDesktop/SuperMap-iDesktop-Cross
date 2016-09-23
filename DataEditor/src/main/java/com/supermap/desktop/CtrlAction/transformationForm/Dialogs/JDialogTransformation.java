package com.supermap.desktop.CtrlAction.transformationForm.Dialogs;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Transformation;
import com.supermap.data.TransformationMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationUtilties;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author XiaJT
 */
public class JDialogTransformation extends SmDialog {

	private JToolBar toolBar = new JToolBar();
	private SmButton buttonAdd = new SmButton();
	private SmButton buttonSelectAll = new SmButton();
	private SmButton buttonSelectInvert = new SmButton();
	private SmButton buttonDel = new SmButton();

	private JScrollPane scrollPane = new JScrollPane();
	private SmSortTable table = new SmSortTable();
	private TransformationTableModel tableModel = new TransformationTableModel();

	private JPanel panelProperties = new JPanel();
	private JLabel labelTransformationFile = new JLabel();
	private FileChooserControl fileChooserControl = new FileChooserControl();
	private JLabel labelTransformationMode = new JLabel();
	private JTextField textFieldTransformationMode = new JTextField();

	private JPanel panelResample = new JPanel();
	private TristateCheckBox checkBoxResample = new TristateCheckBox();
	private CompTitledPane titledPanelResample = new CompTitledPane(checkBoxResample, panelResample);
	private JLabel labelResampleMode = new JLabel();
	private JComboBox comboBoxResampleMode = new JComboBox();
	private JLabel labelPixel = new JLabel();
	private SmTextFieldLegit textFieldPixel = new SmTextFieldLegit();

	private JPanel panelSetting = new JPanel();
	private TristateCheckBox checkBoxIsSaveAs = new TristateCheckBox();
	private JComboBox comboBoxDatasources = new JComboBox();

	private JPanel panelButtons = new JPanel();
	private JCheckBox checkBoxAutoClose = new JCheckBox();
	private SmButton buttonOk = new SmButton();
	private SmButton buttonCancel = new SmButton();
	private DatasetType[] supportDatasetTypes = new DatasetType[]{
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

	private Transformation transformation;
	private boolean isListenerEnable = true;

	public JDialogTransformation() {
		init();
		this.setSize(600, 500);
		this.setLocationRelativeTo(null);
	}

	private void init() {
		initComponents();
		initLayout();
		initListener();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		initTable();

	}

	private void initTable() {
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		table.getColumnModel().getColumn(2).setMaxWidth(50);
	}

	//region 布局
	private void initLayout() {
		initToolbar();
		initPanelProperties();
		initPanelResample();
		initPanelSetting();
		initPanelButtons();

		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 10));

		this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));

		this.add(panelProperties, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0.5, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		this.add(titledPanelResample, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(0.5, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));

		this.add(panelSetting, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0.5, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		this.add(new JPanel(), new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(0.5, 0).setAnchor(GridBagConstraints.CENTER));

		this.add(panelButtons, new GridBagConstraintsHelper(0, 4, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
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

	private void initPanelProperties() {
		panelProperties.setPreferredSize(new Dimension(200, 30));
		panelProperties.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString("String_FormEdgeCount_Text")));
		panelProperties.setLayout(new GridBagLayout());
		panelProperties.add(labelTransformationFile, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setInsets(10, 10, 0, 0));
		panelProperties.add(fileChooserControl, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setInsets(10, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		panelProperties.add(labelTransformationMode, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setInsets(5, 10, 0, 0));
		panelProperties.add(textFieldTransformationMode, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setInsets(5, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initPanelResample() {
		panelResample.setPreferredSize(new Dimension(200, 30));
		panelResample.setLayout(new GridBagLayout());
		panelResample.add(labelResampleMode, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setInsets(10, 5, 0, 0));
		panelResample.add(comboBoxResampleMode, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setInsets(10, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));

		panelResample.add(labelPixel, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setInsets(10, 5, 0, 0));
		panelResample.add(textFieldPixel, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setInsets(10, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initPanelSetting() {
		panelSetting.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString("String_ToolBar_SetBatch")));
		panelSetting.setLayout(new GridBagLayout());
		panelSetting.add(checkBoxIsSaveAs, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setInsets(10, 5, 0, 0));
		panelSetting.add(comboBoxDatasources, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setInsets(10, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initPanelButtons() {
		panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(checkBoxAutoClose, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 10, 0));
		panelButtons.add(buttonOk, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(10, 5, 10, 0));
		panelButtons.add(buttonCancel, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(10, 5, 10, 10));
	}
	//endregion

	//region 添加事件
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
				Arrays.sort(selectedModelRows);
				for (int i = selectedModelRows.length - 1; i >= 0; i--) {
					tableModel.delete(i);
				}
			}
		});
		fileChooserControl.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String moduleName = "transformOpen";
				if (!SmFileChoose.isModuleExist(moduleName)) {
					String fileFilters = SmFileChoose.createFileFilter(DataEditorProperties.getString("String_TransformationFileFilter"), "drfu");
					SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"), CommonProperties.getString(CommonProperties.open)
							, moduleName, "OpenOne");
				}
				SmFileChoose fileChoose = new SmFileChoose(moduleName);
				if (fileChoose.showDefaultDialog() == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChoose.getSelectedFile();
					Transformation transformation = TransformationUtilties.getTransformation(selectedFile);
					if (transformation == null) {
						UICommonToolkit.showErrorMessageDialog(DataEditorProperties.getString("String_Transformation_FromXMLFailed"));
					} else {
						setTransformation(transformation);
						fileChooserControl.setText(selectedFile.getPath());
					}
				}
			}
		});
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showAddDialog(e);
			}
		};
		table.addMouseListener(mouseAdapter);
		scrollPane.addMouseListener(mouseAdapter);
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOkClicked();
			}
		});
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void buttonOkClicked() {

	}

	private void showAddDialog(MouseEvent e) {
		int columnAtPoint = table.columnAtPoint(e.getPoint());
		if (e.getClickCount() != 2) {
			return;
		}
		if (table.rowAtPoint(e.getPoint()) == -1) {
			buttonAddClicked();
		} else if (columnAtPoint == TransformationTableModel.column_Dataset || columnAtPoint == TransformationTableModel.column_DataSource) {
			buttonAddClicked();
		}
	}

	private void buttonAddClicked() {
		DatasetChooser datasetChooser = new DatasetChooser();
		datasetChooser.setSupportDatasetTypes(supportDatasetTypes);
		if (datasetChooser.showDialog() == DialogResult.OK) {
			java.util.List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();
			if (selectedDatasets.size() > 0) {
				Datasource defaultDatasource = getDefaultDatasource(selectedDatasets.get(0).getDatasource());
				for (Dataset selectedDataset : selectedDatasets) {
					tableModel.addDataset(selectedDataset, defaultDatasource,
							defaultDatasource.getDatasets().getAvailableDatasetName(selectedDataset.getName() + "_adjust"));
				}
			}
		}
		datasetChooser.dispose();
	}

	private void setTransformation(Transformation transformation) {
		this.transformation = transformation;
		TransformationMode transformMode = transformation.getTransformMode();
		String s = null;
		if (transformMode == TransformationMode.OFFSET) {
			s = DataEditorProperties.getString("String_TransformationModeOFFSET");
		} else if (transformMode == TransformationMode.RECT) {
			s = DataEditorProperties.getString("String_TransformationModeRECT");
		} else if (transformMode == TransformationMode.LINEAR) {
			s = DataEditorProperties.getString("String_TransformationModeLINEAR");
		} else if (transformMode == TransformationMode.SQUARE) {
			s = DataEditorProperties.getString("String_TransformationModeSQUARE");
		}
		textFieldTransformationMode.setText(s);
	}

	private Datasource getDefaultDatasource(Datasource datasource) {
		if (!datasource.isReadOnly()) {
			return datasource;
		}
		Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
		if (activeDatasources.length > 0) {
			for (Datasource activeDatasource : activeDatasources) {
				if (datasource.isOpened() && !activeDatasource.isReadOnly()) {
					return activeDatasource;
				}
			}
		}
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).isOpened() && !datasources.get(i).isReadOnly()) {
				return datasources.get(i);
			}
		}
		throw new UnsupportedOperationException("UnBelievable!");
	}
	//endregion

	private void initResources() {
		labelTransformationFile.setText(DataEditorProperties.getString("String_Transformation_LabelConfigFile"));
		labelTransformationMode.setText(DataEditorProperties.getString("String_TransformationMode"));
		checkBoxResample.setText(DataEditorProperties.getString("String_TransformationMode_ResampleYesOrNot"));
		labelResampleMode.setText(DataEditorProperties.getString("String_TransformationMode_ResampleMode"));
		labelPixel.setText(DataEditorProperties.getString("String_Transformation_LabelCellSize"));
		checkBoxIsSaveAs.setText(DataEditorProperties.getString("String_Transformation_CheckBoxResaveDataset"));
		checkBoxAutoClose.setText(CommonProperties.getString("String_CheckBox_CloseDialog"));
		buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
		buttonAdd.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddMap.png"));
		buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		buttonDel.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
	}

	private void initComponentState() {
		checkButtonState();
		textFieldTransformationMode.setEditable(false);
		checkBoxAutoClose.setSelected(true);

	}

	private void checkButtonState() {
		buttonOk.setEnabled(table.getRowCount() > 0 && transformation != null);
		buttonSelectAll.setEnabled(table.getRowCount() > 0);
		buttonSelectInvert.setEnabled(table.getRowCount() > 0);
		buttonDel.setEnabled(table.getSelectedRowCount() > 0);
		comboBoxDatasources.setEnabled(table.getSelectedRowCount() > 0);
		checkResampleState();
		checkIsSaveState();
	}

	private void checkIsSaveState() {
		checkBoxIsSaveAs.setEnabled(table.getSelectedRowCount() > 0);
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
			isListenerEnable = false;
			if (isSaveAs) {
				checkBoxResample.setSelected(beans.get(0).isSaveAs());
			} else {
				checkBoxResample.setSelectedEx(null);
			}
			comboBoxDatasources.setEnabled(checkBoxResample.isSelected());
			if (isDatasourceSame) {
				comboBoxDatasources.setSelectedItem(beans.get(0).getResultDatasource());
			} else {
				comboBoxDatasources.setSelectedItem(null);
			}
			isListenerEnable = true;
		}
	}

	private void checkResampleState() {
		int[] selectedModelRows = table.getSelectedModelRows();
		ArrayList<TransformationAddObjectBean> beans = new ArrayList<>();
		for (int selectedModelRow : selectedModelRows) {
			TransformationAddObjectBean dataAtRow = tableModel.getDataAtRow(selectedModelRow);
			if (dataAtRow.getDataset() instanceof DatasetGrid || dataAtRow.getDataset() instanceof DatasetImage) {
				beans.add(dataAtRow);
			}
		}
		if (beans.size() == 0) {
			checkBoxResample.setEnabled(false);
			textFieldPixel.setEditable(false);
			comboBoxResampleMode.setEnabled(false);
		} else {
			checkBoxResample.setEnabled(true);
			textFieldPixel.setEditable(true);
			comboBoxResampleMode.setEnabled(true);
			boolean isResampleSame = true;
			boolean isResampleModeSame = true;
			boolean isResamplePixelSame = true;
			if (beans.size() > 1) {
				for (int i = 1; i < beans.size(); i++) {
					if (beans.get(i).isResample() ^ beans.get(0).isResample()) {
						isResampleSame = false;
					}
					if (beans.get(i).getTransformationResampleMode() != beans.get(0).getTransformationResampleMode()) {
						isResampleModeSame = false;
					}
					if (DoubleUtilities.equals(beans.get(i).getCellSize(), beans.get(i).getCellSize(), 6)) {
						isResamplePixelSame = false;
					}
				}
			}
			this.isListenerEnable = false;
			if (isResampleSame) {
				checkBoxResample.setSelected(beans.get(0).isResample());
			} else {
				checkBoxResample.setSelectedEx(null);
			}
			if (isResampleModeSame) {
				comboBoxResampleMode.setSelectedItem(beans.get(0).getTransformationResampleMode());
			} else {
				comboBoxResampleMode.setSelectedItem(null);
			}
			if (isResamplePixelSame) {
				textFieldPixel.setText(DoubleUtilities.toString(beans.get(0).getCellSize(), 10));
			} else {
				textFieldPixel.setText("");
			}
			this.isListenerEnable = true;
		}
	}

}
