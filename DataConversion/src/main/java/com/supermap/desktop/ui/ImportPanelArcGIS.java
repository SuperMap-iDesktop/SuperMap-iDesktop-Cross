package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.*;
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
 * @author Administrator 实现右侧导入dem,bil,bip,bsq,raw,grd,txt栅格数据类型的界面
 */
public class ImportPanelArcGIS extends AbstractImportPanel {
	private static final long serialVersionUID = 1L;
	private SmButton buttonProperty;
	private JCheckBox checkBoxImageInfo;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelDatapath;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JPanel panelDataSet;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels = null;
	private transient ImportSetting importsetting = null;
	private transient DataImportFrame dataImportFrame;
	private transient LocalActionListener actionListener = new LocalActionListener();

	public ImportPanelArcGIS(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelArcGIS(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	private void setImageInfo(boolean flag, ImportSetting importsetting) {
		if (importsetting instanceof ImportSettingTEMSClutter) {
			// 如果选择的是电信栅格文件
			((ImportSettingTEMSClutter) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingBIL) {
			// 如果选择的是BIL格式的文件
			((ImportSettingBIL) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingBIP) {
			// 如果选择的是BIP格式的文件
			((ImportSettingBIP) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingBSQ) {
			// 如果选择的是BIP格式的文件
			((ImportSettingBSQ) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingGRD) {
			// 如果选择的是grd,txt格式的文件
			((ImportSettingGRD) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingUSGSDEM) {
			// 如果选择的是DEM格式的文件
			((ImportSettingUSGSDEM) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingRAW) {
			// 如果选择的是raw格式的文件
			((ImportSettingRAW) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingGBDEM) {
			((ImportSettingGBDEM) importsetting).setPyramidBuilt(flag);
		}
	}

	@Override
	void initResource() {
		this.labelDatapath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		this.labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		this.checkBoxImageInfo.setText(DataConversionProperties.getString("string_checkbox_chckbxImageInfo"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.panelDataSet.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panelTransform.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panelDatapath.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover")}));
		this.comboBoxCodingType.setModel(
				new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_nullcoding"), "SGL", "LZW"}));
	}

	@Override
	void initComponents() {

		this.panelDataSet = new JPanel();
		this.labelDatasource = new JLabel();
		this.labelCodingType = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxCodingType = new JComboBox<Object>();
		this.labelDataset = new JLabel();
		this.textFieldResultSet = new JTextField();
		this.textFieldResultSet.setColumns(10);
		this.panelTransform = new JPanel();
		this.labelImportModel = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.checkBoxImageInfo = new JCheckBox();
		this.panelDatapath = new JPanel();
		this.labelDatapath = new JLabel();
		this.textFieldFilePath = new JTextField();
		this.textFieldFilePath.setEditable(false);
		this.buttonProperty = new SmButton();
		this.labelCharset = new JLabel();
		this.comboBoxCharset = new CharsetComboBox();
		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);
		this.importsetting = ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting,
				textFieldResultSet);

		if (null != importsetting && null != importsetting.getSourceFileCharset()) {
			comboBoxCharset.setSelectCharset(importsetting.getSourceFileCharset().name());
		}
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置源文件字符集类型
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		// 主panel添加三个子panel
		initPanelArcGIS();

		// 结果设置panel
		initPanelDataSet();

		// 转换参数panelTransform
		initPanelTransform();
		// 源文件信息panelDatapath
		initPanelDatapath();
	}

	private void initPanelDatapath() {
		//@formatter:off
        this.panelDatapath.setLayout(new GridBagLayout());
        this.panelDatapath.add(this.labelDatapath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 5, 5));
        this.panelDatapath.add(this.textFieldFilePath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(10, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL));
        this.panelDatapath.add(this.buttonProperty, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));

        this.panelDatapath.add(this.labelCharset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(0, 10, 10, 5));
        this.panelDatapath.add(this.comboBoxCharset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(0, 0, 10, 5).setIpad(20, 0));
        this.panelDatapath.add(new JPanel(), new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setFill(GridBagConstraints.BOTH));
        //@formatter:on
	}

	private void initPanelTransform() {
		//@formatter:off
        this.panelTransform.setLayout(new GridBagLayout());
        this.panelTransform.add(this.labelImportModel, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 10, 5).setIpad(30, 0));
        this.panelTransform.add(this.comboBoxImportModel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(10, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
        this.panelTransform.add(this.checkBoxImageInfo, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(10, 0, 10, 10));
        //@formatter:on
	}

	private void initPanelDataSet() {
		//@formatter:off
        this.panelDataSet.setLayout(new GridBagLayout());
        this.panelDataSet.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 5, 5).setIpad(18, 0));
        this.panelDataSet.add(this.comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
        this.panelDataSet.add(this.labelDataset, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 0, 5, 5));
        this.panelDataSet.add(this.textFieldResultSet, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));

        this.panelDataSet.add(this.labelCodingType, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(0, 10, 10, 5).setIpad(18, 0));
        this.panelDataSet.add(this.comboBoxCodingType, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(0, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
        this.panelDataSet.add(new JPanel(), new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        //@formatter:on
	}

	private void initPanelArcGIS() {
		this.setLayout(new GridBagLayout());
		this.add(this.panelDataSet, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 0, 0).setFill(GridBagConstraints.BOTH));
		this.add(this.panelTransform, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 0, 0).setFill(GridBagConstraints.BOTH));
		this.add(this.panelDatapath, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 0, 0).setFill(GridBagConstraints.BOTH));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		//@formatter:off
//		GroupLayout groupLayout = new GroupLayout(this);
//		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup().addComponent(this.panelDataSet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
//				.addComponent(this.panelTransform,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE).
//				addComponent(this.panelDatapath,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE));
//		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(this.panelDataSet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE).addContainerGap()
//				.addComponent(this.panelTransform, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE).addContainerGap()
//				.addComponent(this.panelDatapath,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
//		this.setLayout(groupLayout);
        //@formatter:on
	}

	public JCheckBox getChckbxImageInfo() {
		return checkBoxImageInfo;
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
		unregistActionListener();
		this.buttonProperty.addActionListener(this.actionListener);
		this.checkBoxImageInfo.addActionListener(this.actionListener);
	}

	@Override
	void unregistActionListener() {
		this.buttonProperty.removeActionListener(this.actionListener);
		this.buttonProperty.removeActionListener(this.actionListener);
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == checkBoxImageInfo) {
				// 设置是否建立影像金字塔
				boolean flag = checkBoxImageInfo.isSelected();
				if (null != importsetting) {
					setImageInfo(flag, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelArcGIS tempPanel = (ImportPanelArcGIS) panels.get(i);
						tempPanel.getChckbxImageInfo().setSelected(flag);
					}
				}
			} else {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		}

	}

}
