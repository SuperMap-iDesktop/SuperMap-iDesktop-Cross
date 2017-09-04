package com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass;

import com.supermap.data.DatasetGrid;
import com.supermap.data.PixelFormat;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.dialog.BatchAddDailog;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.PixelFormatUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	private DatasetGrid dataset=null;

	public RasterReclassValuePanel() {
		initComponents();
		initLayout();
		initComponentsState();
		initResources();
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
		this.rasterReclassModel=new RasterReclassModel();
	}

	private void initLayout() {
		this.setPreferredSize(new Dimension(200,600));
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
				PixelFormatUtilities.toString(PixelFormat.BIT64),PixelFormatUtilities.toString(PixelFormat.SINGLE),
				PixelFormatUtilities.toString(PixelFormat.DOUBLE)}));
		this.buttonGroup.add(this.radioButtonLeftClose);
		this.buttonGroup.add(this.radioButtonLeftOpen);
		this.radioButtonLeftClose.setSelected(true);
		this.radioButtonLeftOpen.setSelected(false);
		this.textFieldLegitNoValue.setEnabled(false);
		this.textFieldLegitNoClass.setEnabled(false);
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
		this.buttonBatchAdd.setToolTipText(ControlsProperties.getString("String_AddRange"));
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

	}

	private ActionListener batchAddListener=new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			BatchAddDailog batchAddDailog=new BatchAddDailog(dataset.getMinValue(),dataset.getMaxValue(),10, (JFrame) Application.getActiveApplication().getMainFrame(), true);
			batchAddDailog.showDialog();
			if (batchAddDailog.getDialogResult()== DialogResult.OK){
				batchAddDailog.getResultKeys();
			}
		}
	};

	public void setDataset(DatasetGrid dataset){
		this.dataset=dataset;
		this.rasterReclassModel.setDataset(dataset);
	}


}
