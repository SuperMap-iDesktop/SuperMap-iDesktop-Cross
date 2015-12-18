package com.supermap.desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSettingCSV;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.util.CommonComboBoxModel;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Administrator 实现右侧导入CSV数据类型的界面
 */
public class ImportPanelCSV extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton buttonProperty;
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
	private transient JTextField textFieldSperator;
	private transient ImportFileInfo fileInfo;
	private transient ArrayList<ImportFileInfo> fileInfos;
	private transient ArrayList<JPanel> panels;
	private transient ImportSettingCSV importsetting = null;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelCSV() {
		initComponents();
	}

	public ImportPanelCSV(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelCSV(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void initResource() {
		buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		labelImportType.setText(DataConversionProperties.getString("string_label_lblImportType"));
		labelSeparator.setText(DataConversionProperties.getString("string_label_lblSeparator"));
		labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		checkboxDataInfo.setText(DataConversionProperties.getString("string_checkbox_chckbxDataInfo"));
		textFieldSperator.setText(DataConversionProperties.getString("string_index_comma"));
		buttonProperty.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		});
		panelResultSetting.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTransformParam.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelFileInfo.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBoxCharset.setModel(new CommonComboBoxModel());
		comboBoxCharset.setAutoscrolls(true);
		comboBoxImportMode.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
	}

	private void initComponents() {

		panelResultSetting = new JPanel();
		labelDatasource = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		labelDataset = new JLabel();
		textFieldResultSet = new JTextField();
		textFieldResultSet.setColumns(10);
		panelTransformParam = new JPanel();
		labelImportType = new JLabel();
		comboBoxImportMode = new JComboBox<Object>();
		labelSeparator = new JLabel();
		textFieldSperator = new JTextField();
		checkboxDataInfo = new JCheckBox();
		panelFileInfo = new JPanel();
		labelFilePath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		labelCharset = new JLabel();
		comboBoxCharset = new CharsetComboBox();
		initResource();
		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		setPreferredSize(new java.awt.Dimension(483, 300));
		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置fileInfo
		importsetting = (ImportSettingCSV) ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting, textFieldResultSet);
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportMode);

		Document document = textFieldSperator.getDocument();
		// 设置分隔符
		document.addDocumentListener(new LocalDocumentListener());
		// 设置首行为字段信息
		checkboxDataInfo.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				boolean isFrist = checkboxDataInfo.isSelected();
				if (null != importsetting) {
					importsetting.setFirstRowIsField(isFrist);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelCSV tempPanel = (ImportPanelCSV) panels.get(i);
						tempPanel.getChckbxDataInfo().setSelected(isFrist);
					}
				}

			}
		});
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		// @formatter:off
		GroupLayout gl_panelCSV=new GroupLayout(this);
		gl_panelCSV.setAutoCreateContainerGaps(true);
		gl_panelCSV.setAutoCreateGaps(true);
		this.setLayout(gl_panelCSV);
		
		gl_panelCSV.setHorizontalGroup(gl_panelCSV.createParallelGroup(Alignment.LEADING)
				.addComponent(panelResultSetting,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
				.addComponent(panelTransformParam,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
				.addComponent(panelFileInfo,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE));
		
		gl_panelCSV.setVerticalGroup(gl_panelCSV.createSequentialGroup()
				.addComponent(panelResultSetting,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
				.addComponent(panelTransformParam,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
				.addComponent(panelFileInfo,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE));
				
				GroupLayout gl_panelResultSetting=new GroupLayout(panelResultSetting);
				gl_panelResultSetting.setAutoCreateContainerGaps(true);
				gl_panelResultSetting.setAutoCreateGaps(true);
				panelResultSetting.setLayout(gl_panelResultSetting);
				
				gl_panelResultSetting.setHorizontalGroup(gl_panelResultSetting.createSequentialGroup()
						.addComponent(labelDatasource, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addGap(46)
						.addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textFieldResultSet,GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE));

				gl_panelResultSetting.setVerticalGroup(gl_panelResultSetting.createParallelGroup(Alignment.CENTER)
						.addComponent(labelDatasource)
						.addComponent(comboBoxDatasource,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelDataset)
						.addComponent(textFieldResultSet,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
				
				GroupLayout gl_panelTransformParam=new GroupLayout(panelTransformParam);
				gl_panelTransformParam.setAutoCreateContainerGaps(true);
				gl_panelTransformParam.setAutoCreateGaps(true);
				panelTransformParam.setLayout(gl_panelTransformParam);
				
				gl_panelTransformParam.setHorizontalGroup(gl_panelTransformParam.createSequentialGroup()
						.addGroup(gl_panelTransformParam.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelTransformParam.createSequentialGroup()
										.addComponent(labelImportType, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(comboBoxImportMode, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE))
								.addComponent(checkboxDataInfo))
						.addGap(46)
						.addGroup(gl_panelTransformParam.createSequentialGroup()
								.addComponent(labelSeparator, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textFieldSperator, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)));
				
				gl_panelTransformParam.setVerticalGroup(gl_panelTransformParam.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTransformParam.createSequentialGroup()
								.addGroup(gl_panelTransformParam.createParallelGroup(Alignment.CENTER)
										.addComponent(labelImportType)
										.addComponent(comboBoxImportMode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(checkboxDataInfo))
						.addGroup(gl_panelTransformParam.createParallelGroup(Alignment.CENTER)
								.addComponent(labelSeparator)
								.addComponent(textFieldSperator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
				
				GroupLayout gl_panelFileInfo=new GroupLayout(this.panelFileInfo);
				gl_panelFileInfo.setAutoCreateContainerGaps(true);
				gl_panelFileInfo.setAutoCreateGaps(true);
				this.panelFileInfo.setLayout(gl_panelFileInfo);
				
				gl_panelFileInfo.setHorizontalGroup(gl_panelFileInfo.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelFileInfo.createSequentialGroup()
								.addComponent(labelFilePath, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH, packageInfo.DEFAULT_LABEL_WIDTH)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(buttonProperty))
						.addGroup(gl_panelFileInfo.createSequentialGroup()
								.addComponent(labelCharset)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(comboBoxCharset,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,packageInfo.DEFAULT_COMPONENT_WIDTH)));
				
				gl_panelFileInfo.setVerticalGroup(gl_panelFileInfo.createSequentialGroup()
						.addGroup(gl_panelFileInfo.createParallelGroup(Alignment.CENTER)
								.addComponent(labelFilePath)
								.addComponent(textFieldFilePath,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelFileInfo.createParallelGroup(Alignment.CENTER)
								.addComponent(labelCharset)
								.addComponent(comboBoxCharset,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
				// @formatter:on
	}

	public JCheckBox getChckbxDataInfo() {
		return checkboxDataInfo;
	}

	public void setChckbxDataInfo(JCheckBox chckbxDataInfo) {
		this.checkboxDataInfo = chckbxDataInfo;
	}

	public JComboBox<Object> getComboBox() {
		return comboBoxImportMode;
	}

	public void setComboBox(JComboBox<Object> comboBox) {
		this.comboBoxImportMode = comboBox;
	}

	public CharsetComboBox getComboBoxCharset() {
		return comboBoxCharset;
	}

	public void setComboBoxCharset(CharsetComboBox comboBoxCharset) {
		this.comboBoxCharset = comboBoxCharset;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

	public JTextField getTextFieldSperator() {
		return textFieldSperator;
	}

	public void setTextFieldSperator(JTextField textFieldSperator) {
		this.textFieldSperator = textFieldSperator;
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
}
