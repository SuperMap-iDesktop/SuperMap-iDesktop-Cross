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
import javax.swing.LayoutStyle;
import javax.swing.border.TitledBorder;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Administrator 实现右侧导入scv,dbf,vct数据类型的界面
 */
public class ImportPanelSCV extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JCheckBox checkboxFieldIndex;
	private JCheckBox checkboxSpatialIndex;
	private JComboBox<Object> comboBoxImportModel;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCodingType;
	private JLabel labelFilepath;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JPanel panel;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient ImportSetting importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelSCV() {
		initComponents();
	}

	public ImportPanelSCV(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		setCheckBoxInVisible(this.fileInfo);
	}

	public ImportPanelSCV(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		setAllInVisible();
	}

	private void setAllInVisible() {
		int count = 0;
		for (int i = 0; i < fileInfos.size(); i++) {
			ImportFileInfo importFileInfo = fileInfos.get(i);
			if (DataConversionProperties.getString("String_FormImport_DBF").equals(importFileInfo.getFileType())) {
				count++;
			}
		}
		if (count == fileInfos.size()) {
			this.checkboxFieldIndex.setVisible(false);
			this.checkboxSpatialIndex.setVisible(false);
		}
	}

	private void setCheckBoxInVisible(ImportFileInfo fileInfo) {
		if (DataConversionProperties.getString("String_FormImport_DBF").equals(fileInfo.getFileType())) {
			this.checkboxFieldIndex.setVisible(false);
			this.checkboxSpatialIndex.setVisible(false);
		}
	}

	private void initResource() {
		labelFilepath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		checkboxFieldIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxFieldIndex"));
		checkboxSpatialIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxSpatialIndex"));
		buttonProperty.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		});
		panel.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
		comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_nullcoding"),
				DataConversionProperties.getString("string_comboboxitem_byte"), DataConversionProperties.getString("string_comboboxitem_int16"),
				DataConversionProperties.getString("string_comboboxitem_int24"), DataConversionProperties.getString("string_comboboxitem_int32") }));
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
		checkboxFieldIndex = new JCheckBox();
		checkboxSpatialIndex = new JCheckBox();
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		labelFilepath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置fileInfo
		importsetting = ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting, textFieldResultSet);
		// 设置字段索引
		checkboxFieldIndex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean select = checkboxFieldIndex.isSelected();
				if (null != fileInfos) {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelSCV tempPanel = (ImportPanelSCV) panels.get(i);
						tempPanel.getChckbxFieldIndex().setSelected(select);
						fileInfos.get(i).setBuildFiledIndex(select);
					}
				} else if (null != fileInfo) {
					fileInfo.setBuildFiledIndex(select);
				}
			}
		});
		// 设置空间索引
		checkboxSpatialIndex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean select = checkboxSpatialIndex.isSelected();
				if (null != fileInfos) {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelSCV tempPanel = (ImportPanelSCV) panels.get(i);
						tempPanel.getChckbxSpatialIndex().setSelected(select);
						fileInfos.get(i).setBuildSpatialIndex(select);
					}
				} else if (null != fileInfo) {
					fileInfo.setBuildSpatialIndex(select);
				}
			}
		});
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);

		setPreferredSize(new java.awt.Dimension(483, 300));
		// @formatter:off
        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelCodingType, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelDatasource, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboBoxDatasource, 0, 104, Short.MAX_VALUE)
                    .addComponent(comboBoxCodingType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(checkboxFieldIndex)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkboxSpatialIndex)))
                .addGap(0, 35, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDatasource)
                    .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDataset)
                    .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(labelCodingType)
                    .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkboxFieldIndex)
                        .addComponent(checkboxSpatialIndex))))
        );

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransformLayout.setHorizontalGroup(
        	panelTransformLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelTransformLayout.createSequentialGroup()
        			.addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(281, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
        	panelTransformLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelTransformLayout.createSequentialGroup()
        			.addContainerGap(12, Short.MAX_VALUE)
        			.addGroup(panelTransformLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelImportModel)
        				.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );
        panelTransform.setLayout(panelTransformLayout);

        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(panel, GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
        		.addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(labelFilepath, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(48, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 89, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelFilepath)
        				.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(buttonProperty))
        			.addGap(0, 119, Short.MAX_VALUE))
        );
        this.setLayout(layout);
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

}
