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
import com.supermap.data.SpatialIndexInfo;
import com.supermap.data.SpatialIndexType;
import com.supermap.data.conversion.ImportSettingE00;
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
 * @author Administrator 实现右侧导入E00数据类型的界面
 */
public class ImportPanelE00 extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
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
	private JPanel panel;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient ImportSettingE00 importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelE00() {
		initComponents();
	}

	public ImportPanelE00(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelE00(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void initResource() {
		labelDataPath.setText(DataConversionProperties
				.getString("string_label_lblDataPath"));
		labelCharset.setText(DataConversionProperties
				.getString("string_label_lblCharset"));
		labelImportType.setText(DataConversionProperties
				.getString("string_label_lblImportType"));
		labelDatasource.setText(DataConversionProperties
				.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties
				.getString("string_label_lblDataset"));
		labelCodingType.setText(DataConversionProperties
				.getString("string_label_lblCodingtype"));
		checkboxIngoreProperty.setText(DataConversionProperties
				.getString("string_checkbox_chckIngoreProperty"));
		buttonProperty.setText(DataConversionProperties
				.getString("string_button_property"));
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

	private void initComponents() {

		panel = new JPanel();
		labelDatasource = new JLabel();
		labelDataset = new JLabel();
		textFieldResultSet = new JTextField();
		textFieldResultSet.setColumns(10);
		labelCodingType = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		comboBoxCodingType = new JComboBox<Object>();
		checkboxSpatialIndex = new JCheckBox();
		panelTransform = new JPanel();
		labelImportType = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		checkboxIngoreProperty = new JCheckBox();
		panelDatapath = new JPanel();
		labelDataPath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		labelCharset = new JLabel();
		comboBoxCharset = new CharsetComboBox();

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);

		setPreferredSize(new java.awt.Dimension(483, 300));

		initResource();

		importsetting = (ImportSettingE00) ImportInfoUtil.setFileInfo(datasource,
				fileInfos, fileInfo, textFieldFilePath, importsetting,
				textFieldResultSet);
		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting,
				comboBoxCodingType);
		// 设置是否创建空间索引
		checkboxSpatialIndex.addActionListener(new ActionListener() {
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
		});
		// 设置导入模式
		ImportInfoUtil
				.setImportMode(panels, importsetting, comboBoxImportModel);
		// 是否忽略属性信息
		checkboxIngoreProperty.addActionListener(new ActionListener() {
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
		});
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		GroupLayout panelLayout = new GroupLayout(panel);
		panel.setLayout(panelLayout);
		//@formatter:off
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelDatasource, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelCodingType, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxCodingType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(packageInfo.DEFAULT_COMPONENT_GAP)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkboxSpatialIndex))
                .addGap(0, 37, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDatasource)
                    .addComponent(labelDataset)
                    .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCodingType)
                    .addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxSpatialIndex)))
        );

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransform.setLayout(panelTransformLayout);
        panelTransformLayout.setHorizontalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addComponent(labelImportType, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, GroupLayout.PREFERRED_SIZE)
                .addGap(packageInfo.DEFAULT_COMPONENT_GAP)
                .addComponent(checkboxIngoreProperty)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelImportType)
                    .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxIngoreProperty)))
        );

        GroupLayout panelDatapathLayout = new GroupLayout(panelDatapath);
        panelDatapathLayout.setHorizontalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(labelDataPath, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
        				.addComponent(labelCharset))
        			.addGap(12)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
        				.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(22, Short.MAX_VALUE))
        );
        panelDatapathLayout.setVerticalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelDataPath)
        				.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(buttonProperty))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelCharset)
        				.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(57, Short.MAX_VALUE))
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
                .addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTransform, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }//@formatter:on

	public JCheckBox getChckIngoreProperty() {
		return checkboxIngoreProperty;
	}

	public void setChckIngoreProperty(JCheckBox chckIngoreProperty) {
		this.checkboxIngoreProperty = chckIngoreProperty;
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

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

}
