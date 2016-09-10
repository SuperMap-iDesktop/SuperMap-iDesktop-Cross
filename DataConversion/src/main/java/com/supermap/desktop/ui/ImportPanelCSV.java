package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSettingCSV;
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
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator 实现右侧导入CSV数据类型的界面
 */
public class ImportPanelCSV extends AbstractImportPanel {
	private static final long serialVersionUID = 1L;

	private SmButton buttonProperty;
	private JCheckBox checkboxDataInfo;
	private JComboBox<Object> comboBoxImportMode;
	private transient CharsetComboBox comboBoxCharset;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelFilePath;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private JLabel labelImportType;
	private JLabel labelSeparator;
	private JPanel panelResultSetting;
	private JPanel panelFileInfo;
	private JPanel panelTransformParam;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient JTextField textFieldSperator = new JTextField();
	private transient ImportFileInfo fileInfo;
	private transient ArrayList<ImportFileInfo> fileInfos;
	private transient ArrayList<JPanel> panels;
	private transient ImportSettingCSV importsetting = null;
	private transient DataImportFrame dataImportFrame;
	private Document document = this.textFieldSperator.getDocument();
	private transient LocalDocumentListener documentListener = new LocalDocumentListener();
	private transient LocalActionListener actionListener = new LocalActionListener();

	public ImportPanelCSV(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelCSV(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	@Override
	void initResource() {
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		this.labelImportType.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelSeparator.setText(DataConversionProperties.getString("string_label_lblSeparator"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		this.checkboxDataInfo.setText(DataConversionProperties.getString("string_checkbox_chckbxDataInfo"));
		this.textFieldSperator.setText(DataConversionProperties.getString("string_index_comma"));
		this.panelResultSetting.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panelTransformParam.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panelFileInfo.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.comboBoxCharset.setAutoscrolls(true);
		this.comboBoxImportMode.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover")}));
	}

	@Override
	void initComponents() {

		this.panelResultSetting = new JPanel();
		this.labelDatasource = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.labelDataset = new JLabel();
		this.textFieldResultSet = new JTextField();
		this.textFieldResultSet.setColumns(10);
		this.panelTransformParam = new JPanel();
		this.labelImportType = new JLabel();
		this.comboBoxImportMode = new JComboBox<Object>();
		this.labelSeparator = new JLabel();
		this.checkboxDataInfo = new JCheckBox();
		this.panelFileInfo = new JPanel();
		this.labelFilePath = new JLabel();
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
		this.importsetting = (ImportSettingCSV) ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting,
				textFieldResultSet);
        if (null != importsetting.getSourceFileCharset()) {
            comboBoxCharset.setSelectCharset(importsetting.getSourceFileCharset().name());
        }
        // 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportMode);

		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		initPanelCSV();

		initResultSetting();

		initPanelTransformParam();

		initPanelFileInfo();

	}

	private void initPanelCSV() {
		//@formatter:off
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup().addComponent(this.panelResultSetting,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				.addComponent(this.panelTransformParam,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				.addComponent(this.panelFileInfo,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(this.panelResultSetting,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				.addContainerGap().addComponent(this.panelTransformParam, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				.addContainerGap().addComponent(this.panelFileInfo, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
		this.setLayout(groupLayout);
		//@formatter:on
	}

	private void initResultSetting() {
		// @formatter:off
		this.panelResultSetting.setLayout(new GridBagLayout());
		this.panelResultSetting.add(this.labelDatasource,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 10, 5));
		this.panelResultSetting.add(this.comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSetting.add(this.labelDataset,       new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 10, 5));
		this.panelResultSetting.add(this.textFieldResultSet, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(5, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		// @formatter:on
	}

	private void initPanelTransformParam() {
		// @formatter:off
		this.panelTransformParam.setLayout(new GridBagLayout());
		this.panelTransformParam.add(this.labelImportType,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelTransformParam.add(this.comboBoxImportMode, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(35, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransformParam.add(this.labelSeparator,     new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelTransformParam.add(this.textFieldSperator,  new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransformParam.add(this.checkboxDataInfo,   new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(0, 5, 10, 10));
		// @formatter:on
	}

	private void initPanelFileInfo() {
		// @formatter:off
		this.panelFileInfo.setLayout(new GridBagLayout());
		this.panelFileInfo.add(this.labelFilePath,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 0).setInsets(10, 10, 5, 5));
		this.panelFileInfo.add(this.textFieldFilePath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(70, 0).setInsets(10, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFileInfo.add(this.buttonProperty,    new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFileInfo.add(this.labelCharset,      new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 0).setInsets(0, 10, 10, 5));
		this.panelFileInfo.add(this.comboBoxCharset,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 0).setInsets(0, 0, 10, 5).setIpad(20, 0));
		// @formatter:on
	}

	public JCheckBox getChckbxDataInfo() {
		return checkboxDataInfo;
	}

	public JComboBox<Object> getComboBox() {
		return comboBoxImportMode;
	}

	public CharsetComboBox getComboBoxCharset() {
		return comboBoxCharset;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public JTextField getTextFieldSperator() {
		return textFieldSperator;
	}

	class LocalDocumentListener implements DocumentListener {
		@Override
		public void removeUpdate(DocumentEvent e) {
			setSeperator();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			setSeperator();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setSeperator();

		}

		// 设置分隔符
		private void setSeperator() {
			String text = textFieldSperator.getText();
			if (!text.isEmpty()) {
				if (null != importsetting) {
					importsetting.setSeparator(text);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportSettingCSV tempSetting = (ImportSettingCSV) fileInfos.get(i).getImportSetting();
						ImportPanelCSV tempPanel = (ImportPanelCSV) panels.get(i);
						tempSetting.setSeparator(text);
						tempPanel.getTextFieldSperator().setText(text);
					}
				}

			}
		}
	}

	class LocalActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == checkboxDataInfo) {
				// 设置首行为字段信息
				boolean isFrist = checkboxDataInfo.isSelected();
				if (null != importsetting) {
					importsetting.setFirstRowIsField(isFrist);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelCSV tempPanel = (ImportPanelCSV) panels.get(i);
						tempPanel.getChckbxDataInfo().setSelected(isFrist);
					}
				}
			} else {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		}
	}

	@Override
	void registActionListener() {
		unregistActionListener();
		this.document.addDocumentListener(this.documentListener);
		this.checkboxDataInfo.addActionListener(this.actionListener);
		this.buttonProperty.addActionListener(this.actionListener);
	}

	@Override
	void unregistActionListener() {
		this.document.removeDocumentListener(this.documentListener);
		this.checkboxDataInfo.removeActionListener(this.actionListener);
		this.buttonProperty.removeActionListener(this.actionListener);
	}
}
