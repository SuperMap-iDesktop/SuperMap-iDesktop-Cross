package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSettingWOR;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.util.CommonComboBoxModel;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 实现右侧导入wor数据类型的界面
 */
public class ImportPanelWOR extends AbstractImportPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelFilePath;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JPanel panelResultSet;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private transient ImportFileInfo fileInfo;
	private transient ImportSettingWOR importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	private ActionListener buttonApplyAction;

	public ImportPanelWOR(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelWOR(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
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
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.panelResultSet.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.comboBoxCharset.setModel(new CommonComboBoxModel());
		this.comboBoxCharset.setAutoscrolls(true);
		this.comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover")}));
		this.comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_nullcoding"),
				DataConversionProperties.getString("string_comboboxitem_byte"), DataConversionProperties.getString("string_comboboxitem_int16"),
				DataConversionProperties.getString("string_comboboxitem_int24"), DataConversionProperties.getString("string_comboboxitem_int32")}));
	}

	@Override
	void initComponents() {

		this.panelResultSet = new JPanel();
		this.labelDatasource = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.labelCodingType = new JLabel();
		this.comboBoxCodingType = new JComboBox<Object>();
		this.panelTransform = new JPanel();
		this.labelImportModel = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.panelDatapath = new JPanel();
		this.labelFilePath = new JLabel();
		this.textFieldFilePath = new JTextField();
		this.textFieldFilePath.setEditable(false);
		this.buttonProperty = new JButton();
		this.labelCharset = new JLabel();
		this.comboBoxCharset = new CharsetComboBox();

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		this.comboBoxDatasource.setSelectedDatasource(datasource);

		ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting, null);
		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		initPanelResultSet();

		initPanelTransform();

		initPanelDatapath();

		initPanelWOR();
	}

	private void initPanelWOR() {
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
		this.panelTransform.add(this.labelImportModel,          new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 10, 5));
		this.panelTransform.add(this.comboBoxImportModel,       new GridBagConstraintsHelper(1, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(90, 1).setInsets(10, 0, 10, 20).setIpad(130, 0));
		//@formatter:on
	}

	private void initPanelResultSet() {
		//@formatter:off
		this.panelResultSet.setLayout(new GridBagLayout());
		this.panelResultSet.add(this.labelDatasource,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10,10, 5));
		this.panelResultSet.add(this.comboBoxDatasource,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelCodingType,      new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 10, 5));
		this.panelResultSet.add(this.comboBoxCodingType,   new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 10, 10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
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
		this.buttonApplyAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		};
		unregistActionListener();
		this.buttonProperty.addActionListener(this.buttonApplyAction);
	}

	@Override
	void unregistActionListener() {
		this.buttonProperty.removeActionListener(this.buttonApplyAction);
	}


}
