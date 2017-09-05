package com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass;

import com.supermap.analyst.spatialanalyst.ReclassMappingTable;
import com.supermap.analyst.spatialanalyst.ReclassPixelFormat;
import com.supermap.analyst.spatialanalyst.ReclassSegmentType;
import com.supermap.analyst.spatialanalyst.ReclassType;
import com.supermap.data.DatasetGrid;
import com.supermap.data.PixelFormat;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/31.
 */
public class RasterReclassValuePanel extends JPanel {

	private JButton buttonBatchAdd;
	private JButton buttonDefault;
	private JButton buttonSelectedAll;
	private JButton buttonSelectedInverse;
	private JButton buttonCombine;
	private JButton buttonSplit;
	private JButton buttonImport;
	private JButton buttonExport;
	private JButton buttonInverse;
	private JToolBar toolBar;
	private JTable table;
	private RasterReclassModel rasterReclassModel;
	private JScrollPane jScrollPane;
	private JLabel labelPixFormat;
	private JComboBox comboBoxPixFormat;
	private JLabel labelRange;
	private ButtonGroup buttonGroup;
	private JRadioButton radioButtonLeftClose;
	private JRadioButton radioButtonLeftOpen;
	private JCheckBox checkBoxNoValueCell;
	private SmTextFieldLegit textFieldLegitNoValue;
	private JCheckBox checkBoxNoClassCell;
	private SmTextFieldLegit textFieldLegitNoClass;
	private JPanel panelSetting;

	private DatasetGrid dataset = null;
	private ReclassValueChange reclassValueChange = null;
	private ReclassMappingTable reclassMappingTable = new ReclassMappingTable();
	private boolean isImporting = false;

	public RasterReclassValuePanel() {
		initComponents();
		initLayout();
		initComponentsState();
		initResources();
		removeEvent();
		registerEvent();
	}

	private void initComponents() {
		this.buttonBatchAdd = new JButton();
		this.buttonDefault = new JButton();
		this.buttonSelectedAll = new JButton();
		this.buttonSelectedInverse = new JButton();
		this.buttonCombine = new JButton();
		this.buttonSplit = new JButton();
		this.buttonImport = new JButton();
		this.buttonExport = new JButton();
		this.buttonInverse = new JButton();
		this.toolBar = new JToolBar();
		this.table = new JTable();
		this.jScrollPane = new JScrollPane();
		this.labelPixFormat = new JLabel();
		this.comboBoxPixFormat = new JComboBox();
		this.labelRange = new JLabel();
		this.buttonGroup = new ButtonGroup();
		this.radioButtonLeftClose = new JRadioButton();
		this.radioButtonLeftOpen = new JRadioButton();
		this.checkBoxNoValueCell = new JCheckBox();
		this.textFieldLegitNoValue = new SmTextFieldLegit();
		this.checkBoxNoClassCell = new JCheckBox();
		this.textFieldLegitNoClass = new SmTextFieldLegit();
		this.panelSetting = new JPanel();
		this.rasterReclassModel = new RasterReclassModel();
	}

	private void initLayout() {
		this.setPreferredSize(new Dimension(200, 500));
		initPanelSettingLayout();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.toolBar)
						.addComponent(this.jScrollPane)
						.addComponent(this.panelSetting))
		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.toolBar, 30, 30, 30)
				.addComponent(this.jScrollPane)
				.addComponent(this.panelSetting)
		);
		this.setLayout(groupLayout);
	}

	private void initPanelSettingLayout() {
		GroupLayout groupLayout = new GroupLayout(this.panelSetting);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelPixFormat)
						.addComponent(this.labelRange)
						.addComponent(this.checkBoxNoValueCell)
						.addComponent(this.checkBoxNoClassCell))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.comboBoxPixFormat)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(this.radioButtonLeftClose)
								.addComponent(this.radioButtonLeftOpen))
						.addComponent(this.textFieldLegitNoValue)
						.addComponent(this.textFieldLegitNoClass))
		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelPixFormat)
						.addComponent(this.comboBoxPixFormat))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelRange)
						.addComponent(this.radioButtonLeftClose)
						.addComponent(this.radioButtonLeftOpen))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.checkBoxNoValueCell)
						.addComponent(this.textFieldLegitNoValue))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.checkBoxNoClassCell)
						.addComponent(this.textFieldLegitNoClass))
		);
		this.panelSetting.setLayout(groupLayout);
	}

	private void initComponentsState() {
		this.toolBar.add(this.buttonBatchAdd);
		this.toolBar.add(this.buttonDefault);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonSelectedAll);
		this.toolBar.add(this.buttonSelectedInverse);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonCombine);
		this.toolBar.add(this.buttonSplit);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonImport);
		this.toolBar.add(this.buttonExport);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonInverse);
		this.toolBar.setFloatable(false);
		this.jScrollPane.setViewportView(this.table);
		this.comboBoxPixFormat.setModel(new DefaultComboBoxModel<String>(new String[]{PixelFormatUtilities.toString(PixelFormat.BIT32),
				PixelFormatUtilities.toString(PixelFormat.BIT64), PixelFormatUtilities.toString(PixelFormat.SINGLE),
				PixelFormatUtilities.toString(PixelFormat.DOUBLE)}));
		this.buttonGroup.add(this.radioButtonLeftClose);
		this.buttonGroup.add(this.radioButtonLeftOpen);

		this.table.setModel(this.rasterReclassModel);
		this.table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(40);
		this.table.setRowHeight(23);
	}

	private void initResources() {
		this.buttonBatchAdd.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/batchAdd.png"));
		this.buttonDefault.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/defaultColors.png"));
		this.buttonSelectedAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonSelectedInverse.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonCombine.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonSplit.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonImport.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/Image_ToolButton_Import.png"));
		this.buttonExport.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/Image_ToolButton_Export.png"));
		this.buttonInverse.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/invert.png"));
		this.buttonBatchAdd.setToolTipText(ControlsProperties.getString("String_SeriesSetting"));
		this.buttonDefault.setToolTipText(ControlsProperties.getString("String_DefaultClass"));
		this.buttonSelectedAll.setToolTipText(ControlsProperties.getString("String_SelectAll"));
		this.buttonSelectedInverse.setToolTipText(ControlsProperties.getString("String_SelectReverse"));
		this.buttonCombine.setToolTipText(ControlsProperties.getString("String_CombineManyRows"));
		this.buttonSplit.setToolTipText(ControlsProperties.getString("String_SplitRow"));
		this.buttonImport.setToolTipText(ControlsProperties.getString("String_ImportRasterReclassFile"));
		this.buttonExport.setToolTipText(ControlsProperties.getString("String_ExportRasterReclassFile"));
		this.buttonInverse.setToolTipText(ControlsProperties.getString("String_Inverse"));
		this.labelPixFormat.setText(ControlsProperties.getString("String_LabelPixelFormat"));
		this.labelRange.setText(ControlsProperties.getString("String_RangeSection"));
		this.radioButtonLeftClose.setText(ControlsProperties.getString("String_LeftCloseRightOpen"));
		this.radioButtonLeftOpen.setText(ControlsProperties.getString("String_LeftOpenRightClose"));
		this.checkBoxNoValueCell.setText(ControlsProperties.getString("String_NoValueCell"));
		this.checkBoxNoClassCell.setText(ControlsProperties.getString("String_NoClassCell"));
	}

	private void registerEvent() {
		this.buttonBatchAdd.addActionListener(this.batchAddListener);
		this.buttonDefault.addActionListener(this.defaultListener);
		this.buttonSelectedAll.addActionListener(this.selectAllListener);
		this.buttonSelectedInverse.addActionListener(this.selectInverseListener);
		this.buttonCombine.addActionListener(this.combineListener);
		this.buttonSplit.addActionListener(this.splitListener);
		this.buttonImport.addActionListener(this.importListener);
		this.buttonExport.addActionListener(this.exportListener);
		this.buttonInverse.addActionListener(this.inverseListener);
		this.table.getSelectionModel().addListSelectionListener(this.listSelectionListener);
		this.textFieldLegitNoValue.setSmTextFieldLegit(this.iSLegitNoValue);
		this.textFieldLegitNoClass.setSmTextFieldLegit(this.iSLegitNoClass);
		this.checkBoxNoValueCell.addActionListener(this.checkBoxListener);
		this.checkBoxNoClassCell.addActionListener(this.checkBoxListener);
		this.radioButtonLeftOpen.addActionListener(this.radioListener);
		this.radioButtonLeftClose.addActionListener(this.radioListener);
		this.comboBoxPixFormat.addItemListener(this.itemListener);
	}

	private void removeEvent() {
		this.buttonBatchAdd.removeActionListener(this.batchAddListener);
		this.buttonDefault.removeActionListener(this.defaultListener);
		this.buttonSelectedAll.removeActionListener(this.selectAllListener);
		this.buttonSelectedInverse.removeActionListener(this.selectInverseListener);
		this.buttonCombine.removeActionListener(this.combineListener);
		this.buttonSplit.removeActionListener(this.splitListener);
		this.buttonImport.removeActionListener(this.importListener);
		this.buttonExport.removeActionListener(this.exportListener);
		this.buttonInverse.removeActionListener(this.inverseListener);
		this.table.getSelectionModel().removeListSelectionListener(this.listSelectionListener);
		this.checkBoxNoValueCell.removeActionListener(this.checkBoxListener);
		this.checkBoxNoClassCell.removeActionListener(this.checkBoxListener);
		this.radioButtonLeftOpen.removeActionListener(this.radioListener);
		this.radioButtonLeftClose.removeActionListener(this.radioListener);
		this.comboBoxPixFormat.removeItemListener(this.itemListener);
	}

	private ActionListener batchAddListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			seriesSetting();
		}
	};

	private ActionListener defaultListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			setDataset(dataset);
		}
	};

	private ActionListener selectAllListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			table.setRowSelectionInterval(0, table.getRowCount() - 1);
		}
	};

	private ActionListener selectInverseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			selectedInverse();
		}
	};

	private ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			tableSelectChange();
		}
	};

	private ActionListener combineListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			rasterReclassModel.combine(table.getSelectedRows());
			getNewMappingTable();
		}
	};

	private ActionListener splitListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			rasterReclassModel.split(table.getSelectedRow());
			getNewMappingTable();
		}
	};

	private ActionListener importListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
//			importFile();
//			getNewMappingTable();
		}
	};

	private ActionListener exportListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			exportFile();
		}
	};

	private ActionListener inverseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			rasterReclassModel.inverse();
			getNewMappingTable();
		}
	};

	private ActionListener radioListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (radioButtonLeftOpen.isSelected()) {
				rasterReclassModel.setReclassSegmentType(ReclassSegmentType.OPENCLOSE);
			} else {
				rasterReclassModel.setReclassSegmentType(ReclassSegmentType.CLOSEOPEN);
			}
			getNewMappingTable();
		}
	};

	private ISmTextFieldLegit iSLegitNoClass = new ISmTextFieldLegit() {
		@Override
		public boolean isTextFieldValueLegit(String textFieldValue) {
			if (StringUtilities.isNullOrEmpty(textFieldValue)) {
				return false;
			}
			try {
				Double newValue = Double.valueOf(textFieldValue);
				reclassMappingTable.setChangeMissingValueTo(newValue);
				currentMappingTableChange();
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		public String getLegitValue(String currentValue, String backUpValue) {
			return backUpValue;
		}
	};

	private ISmTextFieldLegit iSLegitNoValue = new ISmTextFieldLegit() {
		@Override
		public boolean isTextFieldValueLegit(String textFieldValue) {
			if (StringUtilities.isNullOrEmpty(textFieldValue)) {
				return false;
			}
			try {
				Double newValue = Double.valueOf(textFieldValue);
				reclassMappingTable.setChangeNoValueTo(newValue);
				currentMappingTableChange();
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		public String getLegitValue(String currentValue, String backUpValue) {
			return backUpValue;
		}
	};

	private ActionListener checkBoxListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (checkBoxNoValueCell.isSelected()) {
				textFieldLegitNoValue.setEnabled(true);
			} else {
				textFieldLegitNoValue.setEnabled(false);
			}
			if (checkBoxNoClassCell.isSelected()) {
				textFieldLegitNoClass.setEnabled(true);
			} else {
				textFieldLegitNoClass.setEnabled(false);
			}
		}
	};

	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (comboBoxPixFormat.getSelectedIndex() == 0) {
				if (reclassValueChange != null) {
					reclassValueChange.reClassPixelFormat(ReclassPixelFormat.BIT32);
				}
			} else if (comboBoxPixFormat.getSelectedIndex() == 1) {
				if (reclassValueChange != null) {
					reclassValueChange.reClassPixelFormat(ReclassPixelFormat.BIT64);
				}
			} else if (comboBoxPixFormat.getSelectedIndex() == 2) {
				if (reclassValueChange != null) {
					reclassValueChange.reClassPixelFormat(ReclassPixelFormat.SINGLE);
				}
			} else if (comboBoxPixFormat.getSelectedIndex() == 3) {
				if (reclassValueChange != null) {
					reclassValueChange.reClassPixelFormat(ReclassPixelFormat.DOUBLE);
				}
			}
		}
	};

	private void seriesSetting() {
		SeriesSettingDialog seriesSettingDialog = new SeriesSettingDialog(this.dataset.getMinValue(), this.dataset.getMaxValue(), 10, (JFrame) Application.getActiveApplication().getMainFrame(), true);
		DialogResult result = seriesSettingDialog.showDialog();
		if (result == DialogResult.OK && seriesSettingDialog.getResultKeys() != null) {
			this.rasterReclassModel.setData(seriesSettingDialog.getResultKeys());
			getNewMappingTable();
		}
	}

	private void selectedInverse() {
		int[] temp;
		ListSelectionModel selectionModel;
		int allRowCount;

		selectionModel = table.getSelectionModel();
		temp = table.getSelectedRows();
		allRowCount = table.getRowCount();

		ArrayList<Integer> selectedRows = new ArrayList<Integer>();
		for (int index = 0; index < temp.length; index++) {
			selectedRows.add(temp[index]);
		}

		selectionModel.clearSelection();
		for (int index = 0; index < allRowCount; index++) {
			if (!selectedRows.contains(index)) {
				selectionModel.addSelectionInterval(index, index);
			}
		}
	}

	private void importFile() {
		String moduleName = "ImportMappingTable";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_ReclassFile"), "xml");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_OpenRasterAlgebraExpressionFile"), moduleName, "OpenMany");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		int state = smFileChoose.showDefaultDialog();
		String filePath = "";
		if (state == JFileChooser.APPROVE_OPTION) {
			filePath = smFileChoose.getFilePath();

			if (this.reclassMappingTable.fromXmlFile(filePath)) {
				this.isImporting = true;
//				System.out.println(this.reclassMappingTable.getChangeNoValueTo());
				this.textFieldLegitNoValue.setText(String.valueOf(this.reclassMappingTable.getChangeNoValueTo()));
				this.textFieldLegitNoClass.setText(String.valueOf(this.reclassMappingTable.getChangeMissingValueTo()));
//				if (this.reclassMappingTable.getSegments()[0].getSegmentType() == ReclassSegmentType.OPENCLOSE) {
//					this.rasterReclassModel.setReclassSegmentType(ReclassSegmentType.OPENCLOSE);
//					this.radioButtonLeftOpen.setSelected(true);
//				} else {
//					this.rasterReclassModel.setReclassSegmentType(ReclassSegmentType.CLOSEOPEN);
//					this.radioButtonLeftClose.setSelected(true);
//				}
				this.rasterReclassModel.setSegmentData(this.reclassMappingTable.getSegments());
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_ReclassFileInputSuccess") + filePath);
			} else {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_ReclassFileInputFailed"));
			}
		}
	}

	private void exportFile() {
		String moduleName = "ExportMappingTable";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_ReclassFile"), "xml");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_SaveAsFile"), moduleName, "SaveOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		smFileChoose.setSelectedFile(new File("ReclassInfo.xml"));
		int state = smFileChoose.showDefaultDialog();
		String filePath = "";
		if (state == JFileChooser.APPROVE_OPTION) {
			filePath = smFileChoose.getFilePath();
			File oleFile = new File(filePath);
			filePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".xml";
			File NewFile = new File(filePath);
			oleFile.renameTo(NewFile);
			if (oleFile.isFile() && oleFile.exists()) {
				oleFile.delete();
			}
			if (this.reclassMappingTable.toXmlFile(filePath)) {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_ReclassFileSuccess") + filePath);
			} else {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_ReclassFileFailed"));
			}
		}
	}

	private void tableSelectChange() {
		if (this.table.getSelectedRowCount() == 1) {
			this.buttonSplit.setEnabled(true);
			this.buttonCombine.setEnabled(false);
		} else {
			boolean result = true;
			int[] rows = this.table.getSelectedRows();
			for (int i = 0; i < rows.length - 1; i++) {
				if (rows[i] + 1 != rows[i + 1]) {
					result = false;
					break;
				}
			}
			if (result) {
				this.buttonSplit.setEnabled(false);
				this.buttonCombine.setEnabled(true);
			} else {
				this.buttonSplit.setEnabled(false);
				this.buttonCombine.setEnabled(false);
			}
		}
	}

	private void getNewMappingTable() {
		if (!isImporting) {
			this.reclassMappingTable.setSegments(this.rasterReclassModel.getSegmentData());
			currentMappingTableChange();
		}
	}

	private void currentMappingTableChange() {
		if (this.reclassValueChange != null) {
			this.reclassValueChange.reclassMappingTableChange(this.reclassMappingTable);
		}
	}

	public void setDataset(DatasetGrid dataset) {
		this.dataset = dataset;
		this.rasterReclassModel.setDataset(dataset);
		getNewMappingTable();
	}

	public void initComponentsEnable() {
		this.radioButtonLeftClose.setSelected(true);
		this.radioButtonLeftOpen.setSelected(false);
		this.textFieldLegitNoValue.setEnabled(false);
		this.textFieldLegitNoClass.setEnabled(false);
		this.buttonSplit.setEnabled(false);
		this.buttonCombine.setEnabled(false);
		this.textFieldLegitNoValue.setText(String.valueOf(this.dataset.getNoValue()));
		this.textFieldLegitNoClass.setText("-9999");
		this.reclassMappingTable.setChangeNoValueTo(this.dataset.getNoValue());
		this.reclassMappingTable.setChangeMissingValueTo(-9999.0);
		this.reclassMappingTable.setReclassType(ReclassType.RANGE);
		this.reclassMappingTable.setRetainMissingValue(true);
		this.reclassValueChange.reClassPixelFormat(ReclassPixelFormat.BIT32);
	}

	public void addReclassValueChangeListener(ReclassValueChange reclassValueChange) {
		this.reclassValueChange = reclassValueChange;
	}

	public void removeReclassValueChangeListener(ReclassValueChange reclassValueChange) {
		this.reclassValueChange = null;
	}
}
