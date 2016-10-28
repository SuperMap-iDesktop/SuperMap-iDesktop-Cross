package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.SpatialIndexInfo;
import com.supermap.data.SpatialIndexType;
import com.supermap.data.conversion.ImportSettingE00;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 实现右侧导入E00数据类型的界面
 */
public class ImportPanelE00 extends AbstractImportPanel {

	private static final long serialVersionUID = 1L;
	private SmButton buttonProperty;
	private JCheckBox checkboxIngoreProperty;
	private JCheckBox checkboxSpatialIndex;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelDataPath;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private JLabel labelImportType;
	private JPanel panelResultSet;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient ImportSettingE00 importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	private ActionListener buttonPropertyAction;
	private ActionListener checkboxIngorePropertyAction;
	private ActionListener checkboxSpatialIndexAction;

	public ImportPanelE00(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelE00(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	@Override
	void initResource() {
		this.labelDataPath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		this.labelImportType.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		this.labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		this.checkboxIngoreProperty.setText(DataConversionProperties.getString("string_checkbox_chckIngoreProperty"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.checkboxSpatialIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxSpatialIndex"));
		this.panelResultSet.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		this.panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));

		if (importsetting != null) {
			importsetting.getSourceFileCharset();
		}
		this.comboBoxCharset.setAutoscrolls(true);
		this.comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(
				new String[]{
						DataConversionProperties
								.getString("string_comboboxitem_null"),
						DataConversionProperties
								.getString("string_comboboxitem_add"),
						DataConversionProperties
								.getString("string_comboboxitem_cover")}));
		this.comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(
				new String[]{
						DataConversionProperties
								.getString("string_comboboxitem_nullcoding"),
						DataConversionProperties
								.getString("string_comboboxitem_byte"),
						DataConversionProperties
								.getString("string_comboboxitem_int16"),
						DataConversionProperties
								.getString("string_comboboxitem_int24"),
						DataConversionProperties
								.getString("string_comboboxitem_int32")}));
	}

	@Override
	void initComponents() {

		this.panelResultSet = new JPanel();
		this.labelDatasource = new JLabel();
		this.labelDataset = new JLabel();
		this.textFieldResultSet = new JTextField();
		this.textFieldResultSet.setColumns(10);
		this.labelCodingType = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxCodingType = new JComboBox<Object>();
		this.checkboxSpatialIndex = new JCheckBox();
		this.panelTransform = new JPanel();
		this.labelImportType = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.checkboxIngoreProperty = new JCheckBox();
		this.panelDatapath = new JPanel();
		this.labelDataPath = new JLabel();
		this.textFieldFilePath = new JTextField();
		this.textFieldFilePath.setEditable(false);
		this.buttonProperty = new SmButton();
		this.labelCharset = new JLabel();
		this.comboBoxCharset = new CharsetComboBox();

		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);

		this.importsetting = (ImportSettingE00) ImportInfoUtil.setFileInfo(datasource,
				fileInfos, fileInfo, textFieldFilePath, importsetting,
				textFieldResultSet);
		if (null != importsetting && null != importsetting.getSourceFileCharset()) {
			comboBoxCharset.setSelectCharset(importsetting.getSourceFileCharset().name());
		}

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		initPanelE();

		initPanelResultSet();

		initPanelTransform();

		initPanelDatapath();

	}// @formatter:on

	private void initPanelE() {
		//@formatter:off
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup().addComponent(this.panelResultSet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				   .addComponent(this.panelTransform,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				   .addComponent(panelDatapath,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(this.panelResultSet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				   .addContainerGap().addComponent(this.panelTransform, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				   .addContainerGap().addComponent(panelDatapath, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
		this.setLayout(groupLayout);
		//@formatter:on
	}

	private void initPanelDatapath() {
		this.panelDatapath.setLayout(new GridBagLayout());
		this.panelDatapath.add(this.labelDataPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 5, 5));
		this.panelDatapath.add(this.textFieldFilePath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(10, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.buttonProperty, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.labelCharset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelDatapath.add(this.comboBoxCharset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 5).setIpad(20, 0));
	}

	private void initPanelTransform() {
		//@formatter:off
		this.panelTransform.setLayout(new GridBagLayout());
		this.panelTransform.add(this.labelImportType,        new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 15, 10, 5));
		this.panelTransform.add(this.comboBoxImportModel,    new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 10, 25).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.checkboxIngoreProperty, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(10, 0, 10, 10));
		//@formatter:on
	}

	private void initPanelResultSet() {
		//@formatter:off
		this.panelResultSet.setLayout(new GridBagLayout());
		this.panelResultSet.add(this.labelDatasource,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelResultSet.add(this.comboBoxDatasource,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelDataset,         new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelResultSet.add(this.textFieldResultSet,   new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelCodingType,      new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelResultSet.add(this.comboBoxCodingType,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.checkboxSpatialIndex, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 0, 10, 10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	public JCheckBox getChckIngoreProperty() {
		return checkboxIngoreProperty;
	}

	public JCheckBox getChckbxSpatialIndex() {
		return checkboxSpatialIndex;
	}

	public JComboBox<Object> getComboBox() {
		return comboBoxImportModel;
	}

	public CharsetComboBox getComboBoxCharset() {
		return comboBoxCharset;
	}

	public JComboBox<Object> getComboBoxCodingType() {
		return comboBoxCodingType;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	@Override
	void registActionListener() {
		this.buttonPropertyAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		};
		// 是否忽略属性信息
		this.checkboxIngorePropertyAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isIngore = checkboxIngoreProperty.isSelected();
				if (null != importsetting) {
					importsetting.setAttributeIgnored(isIngore);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelE00 tempPanel = (ImportPanelE00) panels
								.get(i);
						tempPanel.getChckIngoreProperty().setSelected(isIngore);
					}
				}
			}
		};
		// 设置是否创建空间索引
		this.checkboxSpatialIndexAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSpatialIndex = checkboxSpatialIndex.isSelected();
				if (isSpatialIndex) {
					if (null != importsetting) {
						importsetting.setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
						fileInfo.setBuildSpatialIndex(isSpatialIndex);
					} else {
						for (int i = 0; i < panels.size(); i++) {
							ImportPanelE00 tempPanel = (ImportPanelE00) panels.get(i);
							tempPanel.getChckbxSpatialIndex().setSelected(isSpatialIndex);
							fileInfos.get(i).setBuildSpatialIndex(isSpatialIndex);
						}
					}
				}
			}
		};
		unregistActionListener();
		this.buttonProperty.addActionListener(this.buttonPropertyAction);
		this.checkboxIngoreProperty.addActionListener(this.checkboxIngorePropertyAction);
		this.checkboxSpatialIndex.addActionListener(checkboxSpatialIndexAction);
	}

	@Override
	void unregistActionListener() {
		this.buttonProperty.removeActionListener(this.buttonPropertyAction);
		this.checkboxIngoreProperty.removeActionListener(this.checkboxIngorePropertyAction);
		this.checkboxSpatialIndex.removeActionListener(checkboxSpatialIndexAction);
	}

}
