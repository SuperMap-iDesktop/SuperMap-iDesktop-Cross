package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSettingSIT;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 实现右侧导入sit数据类型的界面
 */
public class ImportPanelSIT extends AbstractImportPanel {

	private static final long serialVersionUID = 1L;
	private SmButton buttonProperty;
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
	private JLabel labelPassword;
	private JPanel panelResultSet;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JPasswordField textFieldPassword;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient ImportSettingSIT importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	private LocalDocumentListener documentListener;
	private ActionListener buttonPropertyAction;

	public ImportPanelSIT() {
		initComponents();
	}

	public ImportPanelSIT(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelSIT(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	@Override
	void initResource() {
		this.labelDatapath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelPassword.setText(DataConversionProperties.getString("string_label_lblPassword"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		this.labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.panelResultSet.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		this.panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.comboBoxCharset.setAutoscrolls(true);
		this.comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover")}));
		this.comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_nullcoding"),
				"DCT", "PNG", "LZW"}));
		this.comboBoxCodingType.setSelectedIndex(1);
	}

	@Override
	void initComponents() {

		this.panelResultSet = new JPanel();
		this.labelDatasource = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.labelDataset = new JLabel();
		this.textFieldResultSet = new JTextField();
		this.textFieldResultSet.setColumns(10);
		this.labelCodingType = new JLabel();
		this.comboBoxCodingType = new JComboBox<Object>();
		this.panelTransform = new JPanel();
		this.labelImportModel = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.labelPassword = new JLabel();
		this.textFieldPassword = new JPasswordField();
		this.panelDatapath = new JPanel();
		this.labelDatapath = new JLabel();
		this.textFieldFilePath = new JTextField();
		this.textFieldFilePath.setEditable(false);
		this.buttonProperty = new SmButton();
		this.labelCharset = new JLabel();
		this.comboBoxCharset = new CharsetComboBox();

		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置fileInfo
		this.importsetting = (ImportSettingSIT) ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting,
				textFieldResultSet);
		if (importsetting != null && null != importsetting.getSourceFileCharset()) {
			comboBoxCharset.setSelectCharset(importsetting.getSourceFileCharset().name());
		}
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置密码
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		initPanelResultSet();

		initPanelTransform();

		initPanelDatapath();

		initPanelSIT();
	}

	private void initPanelSIT() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup().addComponent(this.panelResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelTransform, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(this.panelResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap().addComponent(this.panelTransform, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap().addComponent(this.panelDatapath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		this.setLayout(groupLayout);
	}

	private void initPanelDatapath() {
		this.panelDatapath.setLayout(new GridBagLayout());
		this.panelDatapath.add(this.labelDatapath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 5, 5));
		this.panelDatapath.add(this.textFieldFilePath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(10, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.buttonProperty, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.labelCharset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelDatapath.add(this.comboBoxCharset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 5).setIpad(20, 0));
	}

	private void initPanelTransform() {
		this.panelTransform.setLayout(new GridBagLayout());
		this.panelTransform.add(this.labelImportModel, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 10, 5));
		this.panelTransform.add(this.comboBoxImportModel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(10, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.labelPassword, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 10, 5));
		this.panelTransform.add(this.textFieldPassword, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 10, 10).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initPanelResultSet() {
		this.panelResultSet.setLayout(new GridBagLayout());
		this.panelResultSet.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelResultSet.add(this.comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelDataset, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelResultSet.add(this.textFieldResultSet, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelCodingType, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelResultSet.add(this.comboBoxCodingType, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
	}

	class LocalDocumentListener implements DocumentListener {

		@Override
		public void removeUpdate(DocumentEvent e) {
			setpassword();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			setpassword();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setpassword();
		}

		private void setpassword() {
			String text = String.valueOf(textFieldPassword.getPassword());
			if (!text.isEmpty() && !"".equals(text)) {
				if (null != importsetting) {
					importsetting.setPassword(text);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelSIT tempPanel = (ImportPanelSIT) panels.get(i);
						tempPanel.getTextFieldPassword().setText(text);
					}
				}
			}
		}

	}

	public JComboBox<Object> getComboBox() {
		return comboBoxImportModel;
	}

	public void setComboBox(JComboBox<Object> comboBox) {
		this.comboBoxImportModel = comboBox;
	}

	public CharsetComboBox getComboBoxCharset() {
		return comboBoxCharset;
	}

	public void setComboBoxCharset(CharsetComboBox comboBoxCharset) {
		this.comboBoxCharset = comboBoxCharset;
	}

	public JComboBox<Object> getComboBoxCodingType() {
		return comboBoxCodingType;
	}

	public void setComboBoxCodingType(JComboBox<Object> comboBoxCodingType) {
		this.comboBoxCodingType = comboBoxCodingType;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

	public JTextField getTextFieldPassword() {
		return textFieldPassword;
	}

	public void setTextFieldPassword(JPasswordField textFieldPassword) {
		this.textFieldPassword = textFieldPassword;
	}

	@Override
	void registActionListener() {
		this.documentListener = new LocalDocumentListener();
		this.buttonPropertyAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		};
		unregistActionListener();
		this.textFieldPassword.getDocument().addDocumentListener(this.documentListener);
		this.buttonProperty.addActionListener(this.buttonPropertyAction);
	}

	@Override
	void unregistActionListener() {
		this.textFieldPassword.getDocument().removeDocumentListener(this.documentListener);
		this.buttonProperty.removeActionListener(this.buttonPropertyAction);
	}

}
