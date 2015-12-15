package com.supermap.desktop.ui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.TitledBorder;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSettingMAPGIS;
import com.supermap.desktop.Application;
import com.supermap.desktop.FileChooserControl;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.util.CommonComboBoxModel;
import com.supermap.desktop.util.ImportInfoUtil;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Administrator 实现右侧导入MapGIS(wap,wat,wal,wan)数据类型的界面
 */
public class ImportPanelMapGIS extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JCheckBox checkboxFieldIndex;
	private JCheckBox checkboxSpatialIndex;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasetComboBox comboBoxDataType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelFilePath;
	private JLabel labelDataset;
	private JLabel labelDatasetType;
	private JLabel labelDatasource;
	private JLabel labelColorIndexFile;
	private JLabel labelImportModel;
	private JPanel panel;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient FileChooserControl fileChooserc;
	private transient ImportSettingMAPGIS importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelMapGIS() {
		initComponents();
	}

	public ImportPanelMapGIS(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelMapGIS(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void initResource() {
		labelFilePath.setText(DataConversionProperties
				.getString("string_label_lblDataPath"));
		labelCharset.setText(DataConversionProperties
				.getString("string_label_lblCharset"));
		labelImportModel.setText(DataConversionProperties
				.getString("string_label_lblImportType"));
		labelDatasource.setText(DataConversionProperties
				.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties
				.getString("string_label_lblDataset"));
		labelCodingType.setText(DataConversionProperties
				.getString("string_label_lblCodingtype"));
		labelDatasetType.setText(DataConversionProperties
				.getString("string_label_lblDatasetType"));
		labelColorIndexFile.setText(DataConversionProperties
				.getString("string_label_lblColorFile"));
		buttonProperty.setText(DataConversionProperties
				.getString("string_button_property"));
		checkboxFieldIndex.setText(DataConversionProperties
				.getString("string_checkbox_chckbxFieldIndex"));
		checkboxSpatialIndex.setText(DataConversionProperties
				.getString("string_checkbox_chckbxSpatialIndex"));
		buttonProperty.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		});
		panel.setBorder(new TitledBorder(null, DataConversionProperties
				.getString("string_border_panel"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelTransform.setBorder(new TitledBorder(null,
				DataConversionProperties
						.getString("string_border_panelTransform"),
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties
				.getString("string_border_panelDatapath"),
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBoxCharset.setModel(new CommonComboBoxModel());
		comboBoxCharset.setAutoscrolls(true);
		comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(
				new String[] {
						DataConversionProperties
								.getString("string_comboboxitem_null"),
						DataConversionProperties
								.getString("string_comboboxitem_add"),
						DataConversionProperties
								.getString("string_comboboxitem_cover") }));
		comboBoxDataType = new DatasetComboBox(new String[] {
				DataConversionProperties.getString("string_comboboxitem_composite"),
				DataConversionProperties.getString("string_comboboxitem_sample") });
		comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(
				new String[] {
						DataConversionProperties
								.getString("string_comboboxitem_nullcoding"),
						DataConversionProperties
								.getString("string_comboboxitem_byte"),
						DataConversionProperties
								.getString("string_comboboxitem_int16"),
						DataConversionProperties
								.getString("string_comboboxitem_int24"),
						DataConversionProperties
								.getString("string_comboboxitem_int32") }));
	}

	private void setDataType(String dataType, ImportSettingMAPGIS tempSetting) {
		if (dataType.equals(DataConversionProperties
				.getString("string_comboboxitem_composite"))) {
			tempSetting.setImportingAsCAD(true);
		} else {
			tempSetting.setImportingAsCAD(false);
		}
	}

	private void initComponents() {

		panel = new JPanel();
		labelDatasource = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		labelDataset = new JLabel();
		textFieldResultSet = new JTextField();
		textFieldResultSet.setColumns(10);
		labelCodingType = new JLabel();
		comboBoxCodingType = new JComboBox<Object>();
		labelDatasetType = new JLabel();
		checkboxFieldIndex = new JCheckBox();
		checkboxSpatialIndex = new JCheckBox();
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		labelColorIndexFile = new JLabel();
		panelDatapath = new JPanel();
		labelFilePath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		labelCharset = new JLabel();
		comboBoxCharset = new CharsetComboBox();
		fileChooserc = new FileChooserControl();
		fileChooserc.getButton().addActionListener(new LocalActionListener());

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);

		// 设置fileInfo
		importsetting = (ImportSettingMAPGIS) ImportInfoUtil.setFileInfo(datasource,
				fileInfos, fileInfo, textFieldFilePath, importsetting,
				textFieldResultSet);
		// 设置目标数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置数据集类型
		comboBoxDataType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dataType = comboBoxDataType.getSelectItem();
				int select = comboBoxDataType.getSelectedIndex();
				if (null != importsetting) {
					setDataType(dataType, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						((ImportPanelMapGIS) panels.get(i)).getComboBoxDataType().setSelectedIndex(select);
					}
				}

			}
		});
		// 是否创建字段索引
		checkboxFieldIndex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isField = checkboxFieldIndex.isSelected();
				if (null != fileInfos) {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelMapGIS tempJPanel = (ImportPanelMapGIS) panels.get(i);
						tempJPanel.getChckbxFieldIndex().setSelected(isField);
						fileInfos.get(i).setBuildFiledIndex(isField);
					}
				} else if (null != fileInfo) {
					fileInfo.setBuildFiledIndex(isField);
				}
			}
		});
		// 是否创建空间索引
		checkboxSpatialIndex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSpatialIndex = checkboxSpatialIndex.isSelected();
				if (null != fileInfos) {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelMapGIS tempJPanel = (ImportPanelMapGIS) panels.get(i);
						tempJPanel.getChckbxSpatialIndex().setSelected(isSpatialIndex);
						fileInfos.get(i).setBuildSpatialIndex(isSpatialIndex);
					}
				} else if (null != fileInfo) {
					fileInfo.setBuildSpatialIndex(isSpatialIndex);
				}
			}
		});
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);

		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		setPreferredSize(new java.awt.Dimension(483, 300));
		// @formatter:off
        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(labelCodingType, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelDatasource, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboBoxDatasource, 0, 104, Short.MAX_VALUE)
                            .addComponent(comboBoxCodingType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(checkboxFieldIndex))
                .addGap(46, 46, 46)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelDatasetType, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldResultSet)
                            .addComponent(comboBoxDataType, 0, 104, Short.MAX_VALUE)))
                    .addComponent(checkboxSpatialIndex))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelDataset)
                        .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelDatasource)
                        .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelCodingType)
                        .addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelDatasetType)
                        .addComponent(comboBoxDataType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(checkboxFieldIndex)
                    .addComponent(checkboxSpatialIndex)))
        );

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransformLayout.setHorizontalGroup(
        	panelTransformLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelTransformLayout.createSequentialGroup()
        			.addGroup(panelTransformLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelTransformLayout.createSequentialGroup()
        					.addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        					.addGap(10)
        					.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        				.addGroup(panelTransformLayout.createSequentialGroup()
        					.addComponent(labelColorIndexFile)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(fileChooserc, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)))
        			.addGap(0, 45, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
        	panelTransformLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelTransformLayout.createSequentialGroup()
        			.addGroup(panelTransformLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelTransformLayout.createSequentialGroup()
        					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(labelImportModel)
        					.addPreferredGap(ComponentPlacement.UNRELATED))
        				.addGroup(panelTransformLayout.createSequentialGroup()
        					.addGap(9)
        					.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)))
        			.addGroup(panelTransformLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelColorIndexFile)
        				.addComponent(fileChooserc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );
        panelTransform.setLayout(panelTransformLayout);

        GroupLayout panelDatapathLayout = new GroupLayout(panelDatapath);
        panelDatapathLayout.setHorizontalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(labelFilePath, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        				.addComponent(labelCharset, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
        			.addGap(1)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
        					.addGap(4)
        					.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
        				.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(45, Short.MAX_VALUE))
        );
        panelDatapathLayout.setVerticalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(panelDatapathLayout.createSequentialGroup()
        							.addContainerGap()
        							.addComponent(labelFilePath))
        						.addGroup(panelDatapathLayout.createSequentialGroup()
        							.addGap(11)
        							.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(labelCharset)
        						.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addGap(9)
        					.addComponent(buttonProperty)))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelDatapath.setLayout(panelDatapathLayout);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
     // @formatter:on
	}

	public JCheckBox getChckbxFieldIndex() {
		return checkboxFieldIndex;
	}

	public void setChckbxFieldIndex(JCheckBox chckbxFieldIndex) {
		this.checkboxFieldIndex = chckbxFieldIndex;
	}

	public JCheckBox getChckbxSpatialIndex() {
		return checkboxSpatialIndex;
	}

	public void setChckbxSpatialIndex(JCheckBox chckbxSpatialIndex) {
		this.checkboxSpatialIndex = chckbxSpatialIndex;
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

	public DatasetComboBox getComboBoxDataType() {
		return comboBoxDataType;
	}

	public void setComboBoxDataType(DatasetComboBox comboBoxDataType) {
		this.comboBoxDataType = comboBoxDataType;
	}

	public JTextField getTextField() {
		return textFieldFilePath;
	}

	public void setTextField(JTextField textField) {
		this.textFieldFilePath = textField;
	}

	public FileChooserControl getFileChooserc() {
		return fileChooserc;
	}

	public void setFileChooserc(FileChooserControl fileChooserc) {
		this.fileChooserc = fileChooserc;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

	class LocalActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!SmFileChoose.isModuleExist("ImportMapGIS")) {
				String fileFilters = SmFileChoose.createFileFilter(DataConversionProperties.getString("string_filetype_color"), "wat");
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						DataConversionProperties.getString("String_Import"), "ImportMapGIS", "OpenMany");
			}
			SmFileChoose fileChooser = new SmFileChoose("ImportMapGIS");
			int state = fileChooser.showDefaultDialog();
			File file = fileChooser.getSelectedFile();
			if (state == JFileChooser.APPROVE_OPTION && null != file) {
				fileChooserc.getEditor().setText(file.getAbsolutePath());
				// 设置颜色索引文件
				String colorFile = fileChooserc.getEditor().getText();

				if (ImportInfoUtil.isExtendsFile(colorFile)) {
					if (null != importsetting) {
						importsetting.setColorIndexFilePath(colorFile);
					} else {
						for (int i = 0; i < panels.size(); i++) {
							((ImportPanelMapGIS) panels.get(i))
									.getFileChooserc().getEditor()
									.setText(colorFile);
						}
					}
				}
			}
		}

	}
}
