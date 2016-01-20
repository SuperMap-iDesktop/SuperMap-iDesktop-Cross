package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.*;
import com.supermap.desktop.FileChooserControl;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.util.CommonComboBoxModel;
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
 * @author Administrator 实现右侧导入栅格数据类型的界面
 */
public class ImportPanelGRID extends AbstractImportPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasetComboBox comboBoxDataType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JComboBox<Object> comboBoxImportType;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelFilePath;
	private JLabel labelDatasetType;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JLabel labelNewLabel;
	private JLabel labelPassword;
	private JLabel labelSaveImport;
	private JPanel panelResultSet;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldPassword;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient FileChooserControl fileChooser;
	private transient ImportSetting importsetting = null;

	private transient LocalActionListener actionListener = new LocalActionListener();

	public ImportPanelGRID(List<ImportFileInfo> fileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelGRID(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	@Override
	void initResource() {
		this.labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelSaveImport.setText(DataConversionProperties.getString("string_label_lblSaveImport"));
		this.labelPassword.setText(DataConversionProperties.getString("string_label_lblPassword"));
		this.labelNewLabel.setText(DataConversionProperties.getString("string_label_lblFile"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		this.labelDatasetType.setText(DataConversionProperties.getString("string_label_lblDatasetType"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.panelResultSet.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		this.panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.comboBoxCharset.setModel(new CommonComboBoxModel());
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
						"DCT", "SGL", "PNG", "LZW"}));
		this.comboBoxCodingType.setSelectedIndex(1);
	}

	@Override
	void initComponents() {

		this.panelResultSet = new JPanel();
		this.labelDatasource = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.labelDatasetType = new JLabel();
		this.labelCodingType = new JLabel();
		this.comboBoxCodingType = new JComboBox<Object>();
		this.panelTransform = new JPanel();
		this.labelImportModel = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.labelSaveImport = new JLabel();
		this.comboBoxImportType = new JComboBox<Object>();
		this.labelPassword = new JLabel();
		this.textFieldPassword = new JTextField();
		this.labelNewLabel = new JLabel();
		this.panelDatapath = new JPanel();
		this.labelFilePath = new JLabel();
		this.labelCharset = new JLabel();
		this.textFieldFilePath = new JTextField();
		this.textFieldFilePath.setEditable(false);
		this.buttonProperty = new JButton();
		this.comboBoxCharset = new CharsetComboBox();
		this.fileChooser = new FileChooserControl();
		this.fileChooser.getEditor().setEnabled(false);

		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);
		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, null, comboBoxDatasource);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		initPanelResultSet();

		initPanelTransform();

		initPanelDatapath();

		initPanelGRID();
		this.comboBoxImportType.setEnabled(false);
		this.textFieldPassword.setEnabled(false);
	}

	private void initPanelGRID() {
		//@formatter:off
		this.setLayout(new GridBagLayout());
		this.add(this.panelResultSet,       new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.BOTH).setInsets(5).setWeight(1, 1));
		this.add(this.panelTransform,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.BOTH).setInsets(5).setWeight(1, 1));
		this.add(this.panelDatapath,        new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.BOTH).setInsets(5).setWeight(1, 1));
		//@formatter:on
	}

	private void initPanelDatapath() {
		//@formatter:off
		this.panelDatapath.setLayout(new GridBagLayout());
		this.panelDatapath.add(this.labelFilePath,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelDatapath.add(this.textFieldFilePath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(70, 1).setInsets(10, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.buttonProperty,    new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.labelCharset,      new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelDatapath.add(this.comboBoxCharset,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 5).setIpad(20, 0));
		//@formatter:on
	}

	private void initPanelTransform() {
		//@formatter:off
		this.panelTransform.setLayout(new GridBagLayout());
		this.panelTransform.add(this.labelImportModel,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelTransform.add(this.comboBoxImportModel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.labelSaveImport,     new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelTransform.add(this.comboBoxImportType,  new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.labelPassword,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 5));
		this.panelTransform.add(this.textFieldPassword,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.labelNewLabel,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelTransform.add(this.fileChooser,         new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(90, 1).setInsets(0, 0, 10, 10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void initPanelResultSet() {
		//@formatter:off
		this.comboBoxDataType = new DatasetComboBox(new String[] { DataConversionProperties.getString("string_comboboxitem_image"),
				DataConversionProperties.getString("string_comboboxitem_grid") });
		// 设置字符集类型
		this.panelResultSet.setLayout(new GridBagLayout());
		this.panelResultSet.add(this.labelDatasource,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelResultSet.add(this.comboBoxDatasource,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelDatasetType,     new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelResultSet.add(this.comboBoxDataType,     new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelCodingType,      new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelResultSet.add(this.comboBoxCodingType,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String dataType = comboBoxDataType.getSelectItem();
			int select = comboBoxDataType.getSelectedIndex();
			if (null != fileInfos && !fileInfos.isEmpty()) {
				for (int i = 0; i < panels.size(); i++) {
					ImportSetting tempSetting = fileInfos.get(i).getImportSetting();
					JPanel tempPanel = panels.get(i);
					if (dataType.equals(DataConversionProperties.getString("string_comboboxitem_image"))) {
						if (tempSetting instanceof ImportSettingTIF) {
							((ImportSettingTIF) tempSetting).setImportingAsGrid(false);
							((ImportPanelTIF) tempPanel).getComboBoxDataType().setSelectedIndex(select);
						} else {
							((ImportPanelPI) tempPanel).getComboBoxDatasetType().setSelectedIndex(select);
							if (tempSetting instanceof ImportSettingBMP) {
								((ImportSettingBMP) tempSetting).setImportingAsGrid(false);
							}
							if (tempSetting instanceof ImportSettingIMG) {
								((ImportSettingIMG) tempSetting).setImportingAsGrid(false);
							}
							if (tempSetting instanceof ImportSettingPNG) {
								((ImportSettingPNG) tempSetting).setImportingAsGrid(false);
							}
							if (tempSetting instanceof ImportSettingJPG) {
								((ImportSettingJPG) tempSetting).setImportingAsGrid(false);
							}
							if (tempSetting instanceof ImportSettingGIF) {
								((ImportSettingGIF) tempSetting).setImportingAsGrid(false);

							}
						}
					} else {
						if (tempSetting instanceof ImportSettingTIF) {
							((ImportSettingTIF) tempSetting).setImportingAsGrid(true);
							((ImportPanelTIF) tempPanel).getComboBoxDataType().setSelectedIndex(select);
						} else {
							((ImportPanelPI) tempPanel).getComboBoxDatasetType().setSelectedIndex(select);
							if (tempSetting instanceof ImportSettingBMP) {
								((ImportSettingBMP) tempSetting).setImportingAsGrid(true);
							}
							if (tempSetting instanceof ImportSettingIMG) {
								((ImportSettingIMG) tempSetting).setImportingAsGrid(true);
							}
							if (tempSetting instanceof ImportSettingPNG) {
								((ImportSettingPNG) tempSetting).setImportingAsGrid(true);
							}
							if (tempSetting instanceof ImportSettingJPG) {
								((ImportSettingJPG) tempSetting).setImportingAsGrid(true);
							}
							if (tempSetting instanceof ImportSettingGIF) {
								((ImportSettingGIF) tempSetting).setImportingAsGrid(true);

							}
						}
					}

				}
			}
		}

	}

	@Override
	void registActionListener() {
		unregistActionListener();
		this.comboBoxDataType.addActionListener(this.actionListener);
	}

	@Override
	void unregistActionListener() {
		this.comboBoxDataType.removeActionListener(this.actionListener);
	}
}
