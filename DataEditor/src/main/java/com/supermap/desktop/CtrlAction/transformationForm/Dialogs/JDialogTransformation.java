package com.supermap.desktop.CtrlAction.transformationForm.Dialogs;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Transformation;
import com.supermap.data.TransformationMode;
import com.supermap.data.TransformationResampleMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.CtrlAction.TransformCallable;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationUtilties;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.TableUtilities;

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
	private JComboBox<TransformationResampleMode> comboBoxResampleMode = new JComboBox<>(new TransformationResampleMode[]{
			// 没有最邻近法
			TransformationResampleMode.NEAREST,
			TransformationResampleMode.BILINEAR,
			TransformationResampleMode.CUBIC
	});
	private JLabel labelPixel = new JLabel();
	private SmTextFieldLegit textFieldPixel = new SmTextFieldLegit();

	private JPanel panelSetting = new JPanel();
	private TristateCheckBox checkBoxIsSaveAs = new TristateCheckBox();
	private JComboBox<Datasource> comboBoxDatasources = new JComboBox<>();

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
	private ArrayList<Datasource> datasourceArrayList;

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
		table.getColumnModel().getColumn(TransformationTableModel.COLUMN_ENABLE).setMaxWidth(50);
		table.getColumnModel().getColumn(TransformationTableModel.COLUMN_SAVE_AS).setMaxWidth(50);

	}

	private void initComponents() {
		initTable();
		comboBoxResampleMode.setRenderer(new ListCellRenderer<TransformationResampleMode>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends TransformationResampleMode> list, TransformationResampleMode value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel jLabel = new JLabel();
				if (value == TransformationResampleMode.NEAREST) {
					jLabel.setText(DataEditorProperties.getString("String_Transformation_ResampleModeNearest"));
				} else if (value == TransformationResampleMode.BILINEAR) {
					jLabel.setText(DataEditorProperties.getString("String_Transformation_ResampleModeBilinear"));
				} else {
					jLabel.setText(DataEditorProperties.getString("String_Transformation_ResampleModeCubic"));
				}
				jLabel.setOpaque(true);
				jLabel.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
				return jLabel;
			}
		});
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		this.datasourceArrayList = new ArrayList<>();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).isOpened() && !datasources.get(i).isReadOnly()) {
				this.datasourceArrayList.add(datasources.get(i));
				comboBoxDatasources.addItem(datasources.get(i));
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
		comboBoxDatasources.setRenderer(renderer);
		DefaultCellEditor cellEditor = new DefaultCellEditor(datasourceJComboBox);
		cellEditor.setClickCountToStart(2);
//		table.getColumnModel().getColumn(TransformationTableModel.column_ResultDatasource).setCellEditor(cellEditor);
		table.setDefaultEditor(Datasource.class, cellEditor);
	}

	private void initTable() {
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
	}

	//region 布局
	private void initLayout() {
		initToolbar();
		initPanelProperties();
		initPanelResample();
		initPanelSetting();
		initPanelButtons();
		Dimension size = new Dimension(500, 120);

		panelProperties.setPreferredSize(size);
		panelProperties.setMaximumSize(size);
		panelProperties.setMinimumSize(size);
		titledPanelResample.setPreferredSize(size);
		titledPanelResample.setMaximumSize(size);
		titledPanelResample.setMinimumSize(size);

		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 10));

		this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));

		this.add(panelProperties, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0.5, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 0));
		this.add(titledPanelResample, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(0.5, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 5, 0, 10));

		this.add(panelSetting, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0.5, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 0));
		this.add(new JPanel(), new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(0.5, 0).setAnchor(GridBagConstraints.CENTER));

		this.add(panelButtons, new GridBagConstraintsHelper(0, 4, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 0, 0));
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
		panelProperties.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString("String_FormEdgeCount_Text")));
		panelProperties.setLayout(new GridBagLayout());
		panelProperties.add(labelTransformationFile, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setInsets(10, 10, 0, 0));
		panelProperties.add(fileChooserControl, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setInsets(10, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		panelProperties.add(labelTransformationMode, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setInsets(5, 10, 0, 0));
		panelProperties.add(textFieldTransformationMode, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setInsets(5, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initPanelResample() {
		panelResample.setLayout(new GridBagLayout());
		panelResample.add(labelResampleMode, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setInsets(2, 10, 0, 0));
		panelResample.add(comboBoxResampleMode, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setInsets(2, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		panelResample.add(labelPixel, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setInsets(5, 10, 0, 0));
		panelResample.add(textFieldPixel, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setInsets(5, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initPanelSetting() {
		panelSetting.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString("String_ToolBar_SetBatch")));
		panelSetting.setLayout(new GridBagLayout());
		panelSetting.add(checkBoxIsSaveAs, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setInsets(10, 5, 0, 0));
		panelSetting.add(comboBoxDatasources, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setInsets(10, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

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
				int row = selectedModelRows[0];
				if (tableModel.getRowCount() <= row) {
					row = tableModel.getRowCount() - 1;
				}
				if (row != -1) {
					table.setRowSelectionInterval(row, row);
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
						checkButtonState();
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
		checkBoxResample.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (isListenerEnable) {
					boolean selected = checkBoxResample.isSelected();
					int[] selectedModelRows = table.getSelectedModelRows();
					if (selectedModelRows.length > 0) {
						for (int selectedModelRow : selectedModelRows) {
							tableModel.setResampleEnable(selectedModelRow, selected);
						}
					}
					comboBoxResampleMode.setEnabled(selected);
					textFieldPixel.setEditable(selected);
				}
			}
		});
		comboBoxResampleMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (isListenerEnable && e.getStateChange() == ItemEvent.SELECTED) {
					TransformationResampleMode resampleMode = (TransformationResampleMode) comboBoxResampleMode.getSelectedItem();
					int[] selectedModelRows = table.getSelectedModelRows();
					if (selectedModelRows.length > 0) {
						for (int selectedModelRow : selectedModelRows) {
							tableModel.setResampleMode(selectedModelRow, resampleMode);
						}
					}
				}
			}
		});
		textFieldPixel.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					setPixel(0d);
					return true;
				}
				try {
					Double aDouble = Double.valueOf(textFieldValue);
					if (aDouble > 0) {
						setPixel(aDouble);
					}
				} catch (Exception e) {
					return false;
				}
				return true;

			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				if (StringUtilities.isNullOrEmpty(currentValue)) {
					return "0";
				}
				return backUpValue;
			}
		});
		checkBoxIsSaveAs.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (isListenerEnable) {
					isListenerEnable = false;
					boolean selected = checkBoxIsSaveAs.isSelected();
					int[] selectedModelRows = table.getSelectedModelRows();
					if (selectedModelRows.length > 0) {
						for (int selectedModelRow : selectedModelRows) {
							tableModel.setValueAt(selected, selectedModelRow, TransformationTableModel.COLUMN_SAVE_AS);
						}
					}
					comboBoxDatasources.setEnabled(selected);
					isListenerEnable = true;
				}
			}
		});
		comboBoxDatasources.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (isListenerEnable && e.getStateChange() == ItemEvent.SELECTED) {
					Datasource selectedItem = (Datasource) comboBoxDatasources.getSelectedItem();
					int[] selectedModelRows = table.getSelectedModelRows();
					if (selectedModelRows.length > 0) {
						for (int selectedModelRow : selectedModelRows) {
							tableModel.setValueAt(selectedItem, selectedModelRow, TransformationTableModel.column_ResultDatasource);
						}
					}
				}
			}
		});
	}

	private void setPixel(Double aDouble) {
		int[] selectedModelRows = table.getSelectedModelRows();
		if (selectedModelRows.length > 0) {
			for (int selectedModelRow : selectedModelRows) {
				tableModel.setPixel(selectedModelRow, aDouble);
			}
		}
	}

	private void buttonOkClicked() {
		TableUtilities.stopEditing(table);
		FormProgressTotal formProgress = new FormProgressTotal(DataEditorProperties.getString("String_transformation"));
		formProgress.doWork(new TransformCallable(transformation, tableModel.getEnableDatas()));
		if (checkBoxAutoClose.isSelected()) {
			dispose();
		}
	}

	private void showAddDialog(MouseEvent e) {
		int columnAtPoint = table.columnAtPoint(e.getPoint());
		if (e.getClickCount() != 2) {
			return;
		}
		if (table.rowAtPoint(e.getPoint()) == -1) {
			buttonAddClicked();
		} else if (columnAtPoint == TransformationTableModel.COLUMN_DATASET || columnAtPoint == TransformationTableModel.COLUMN_DATA_SOURCE) {
			buttonAddClicked();
		}
	}

	private void buttonAddClicked() {
		DatasetChooser datasetChooser = new DatasetChooser();
		datasetChooser.setSupportDatasetTypes(supportDatasetTypes);
		if (datasetChooser.showDialog() == DialogResult.OK) {
			java.util.List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();
			if (selectedDatasets.size() > 0) {
				Datasource defaultDatasource = TransformationUtilties.getDefaultDatasource(selectedDatasets.get(0).getDatasource());
				for (Dataset selectedDataset : selectedDatasets) {
					tableModel.addDataset(selectedDataset, defaultDatasource,
							defaultDatasource.getDatasets().getAvailableDatasetName(selectedDataset.getName() + "_adjust"));
				}
				table.setRowSelectionInterval(table.getRowCount() - selectedDatasets.size(), table.getRowCount() - 1);
			}
		}
		datasetChooser.dispose();
	}

	public void setTransformation(Transformation transformation) {
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
		fileChooserControl.getEditor().setEditable(false);
		this.setTitle(DataEditorProperties.getString("String_FormBatchTransformation"));
	}

	private void checkButtonState() {
		if (!isListenerEnable) {
			return;
		}
		boolean buttonOkState = false;
		for (int i = 0; i < table.getRowCount(); i++) {
			buttonOkState = (Boolean) table.getValueAt(i, TransformationTableModel.COLUMN_ENABLE);
			if (buttonOkState) {
				break;
			}
		}
		buttonOk.setEnabled(buttonOkState && transformation != null);
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
				checkBoxIsSaveAs.setSelected(beans.get(0).isSaveAs());
			} else {
				checkBoxIsSaveAs.setSelectedEx(null);
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
			textFieldPixel.setEditable(checkBoxResample.isSelected());
			comboBoxResampleMode.setEnabled(checkBoxResample.isSelected());
			this.isListenerEnable = true;
		}
	}

	public void addBeans(TransformationAddObjectBean[] transformationAddObjectBeen) {
		ArrayList<TransformationAddObjectBean> transformationAddObjectBeen1 = new ArrayList<>();
		for (TransformationAddObjectBean transformationAddObjectBean : transformationAddObjectBeen) {
			if (TransformationUtilties.isSupportDatasetType(transformationAddObjectBean.getDataset().getType())) {
				transformationAddObjectBeen1.add(transformationAddObjectBean);
			}
		}
		tableModel.addDatas(transformationAddObjectBeen1.toArray(new TransformationAddObjectBean[transformationAddObjectBeen1.size()]));
		checkButtonState();
	}

	public void setBeOnly() {
		labelTransformationFile.setVisible(false);
		fileChooserControl.setVisible(false);
		this.setTitle(DataEditorProperties.getString("String_transformation"));
		panelProperties.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 1));
	}
}
