package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSettingGJB;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ImportFileInfo;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xie GJB军用格式数据集导入
 */
public class ImportPanelGJB extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelTargetDatasource;
	private JLabel labelResultDataset;
	private JLabel labelImportModel;
	private JLabel labelDatapath;
	private JLabel labelCharset;
	private transient DatasourceComboBox comboBoxDatasource;
	private JTextField textFieldResultDataset;
	private JComboBox<Object> comboBoxImportModel;
	private JCheckBox checkBoxIsImportEmptyDataset;
	private JTextField textFieldResultDatapath;
	private SmButton buttonProperty;
	private CharsetComboBox comboBoxCharset;
	private JPanel panelResultSet;
	private JPanel panelDatapath;

	private DataImportFrame dataImportFrame;
	private ImportFileInfo fileInfo;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private ImportSettingGJB importsetting;

	private ActionListener buttonPropertyAction;
	private ItemListener importEmptyDatasetListener;

	/**
	 * @wbp.parser.constructor
	 */
	public ImportPanelGJB(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelGJB(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	private void initComponents() {
		this.labelTargetDatasource = new JLabel();
		this.labelResultDataset = new JLabel();
		this.labelImportModel = new JLabel();
		this.labelCharset = new JLabel();
		this.labelDatapath = new JLabel();
		this.comboBoxCharset = new CharsetComboBox();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.textFieldResultDatapath = new JTextField();
		this.textFieldResultDataset = new JTextField();
		this.buttonProperty = new SmButton();
		this.checkBoxIsImportEmptyDataset = new JCheckBox();
		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);
		this.panelDatapath = new JPanel();
		this.panelResultSet = new JPanel();
		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);

		// 设置fileInfo信息
		this.importsetting = (ImportSettingGJB) ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldResultDatapath, importsetting,
				textFieldResultDataset);
		// 设置数据集结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultDataset, importsetting);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置字符集类型
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);
		// formmater:off
		initPanelResultSet();

		initPanelDatapath();

		initPanelGJB();
		this.textFieldResultDatapath.setEnabled(false);
		// formatter:on
	}

	private void initPanelGJB() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup().addComponent(this.panelResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(this.panelResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()
				.addComponent(this.panelDatapath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		this.setLayout(groupLayout);
	}

	private void initPanelDatapath() {
		this.panelDatapath.setLayout(new GridBagLayout());
		this.panelDatapath.add(this.labelDatapath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 5, 5));
		this.panelDatapath.add(this.textFieldResultDatapath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(10, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.buttonProperty, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.labelCharset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelDatapath.add(this.comboBoxCharset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 5).setIpad(20, 0));
	}

	private void initPanelResultSet() {
		//@formatter:off
		this.panelResultSet.setLayout(new GridBagLayout());
		this.comboBoxDatasource.setPreferredSize(packageInfo.dimension);
		this.textFieldResultDataset.setPreferredSize(packageInfo.dimension);
		this.textFieldResultDataset.setEnabled(false);
		this.panelResultSet.add(this.labelTargetDatasource,        new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelResultSet.add(this.comboBoxDatasource,           new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelResultDataset,           new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelResultSet.add(this.textFieldResultDataset,       new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelImportModel,             new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 10, 5));
		this.panelResultSet.add(this.comboBoxImportModel,          new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.checkBoxIsImportEmptyDataset, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(10, 0, 10, 5));
		//@formatter:on
	}

	private void registActionListener() {
		this.buttonPropertyAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		};
		this.importEmptyDatasetListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean isImportEmptyDataset = checkBoxIsImportEmptyDataset.isSelected();
				importsetting.setImportEmptyDataset(isImportEmptyDataset);
			}
		};
		unregistActionListener();
		this.buttonProperty.addActionListener(this.buttonPropertyAction);
		this.checkBoxIsImportEmptyDataset.addItemListener(importEmptyDatasetListener);
	}

	private void unregistActionListener() {
		this.buttonProperty.removeActionListener(this.buttonPropertyAction);
		this.checkBoxIsImportEmptyDataset.removeItemListener(importEmptyDatasetListener);
	}

	private void initResource() {
		this.labelTargetDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelResultDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelDatapath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		this.panelResultSet.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel")));
		this.panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath")));
		this.comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover")}));
		this.checkBoxIsImportEmptyDataset.setText(DataConversionProperties.getString("String_ImportEmptyDataset"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
	}

}
