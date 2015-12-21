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
import com.supermap.data.conversion.ImportSettingLIDAR;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.util.CommonComboBoxModel;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Administrator 实现右侧导入雷达数据类型的界面
 */
public class ImportPanelLIDAR extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JCheckBox checkboxIgnorePropertyInfo;
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
	private JLabel labelImportModel;
	private JPanel panel;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient ImportSettingLIDAR importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelLIDAR() {
		initComponents();
	}

	public ImportPanelLIDAR(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelLIDAR(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
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
				.getString("string_label_targetDataset"));
		labelCodingType.setText(DataConversionProperties
				.getString("string_label_lblCodingtype"));
		labelDatasetType.setText(DataConversionProperties
				.getString("string_label_lblDatasetType"));
		checkboxIgnorePropertyInfo.setText(DataConversionProperties
				.getString("String_ImportSettingPanel_Checkbox_IsAttributeIgnored"));
		checkboxSpatialIndex.setText(DataConversionProperties
				.getString("string_checkbox_chckbxSpatialIndex"));
		buttonProperty.setText(DataConversionProperties
				.getString("string_button_property"));
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
		comboBoxDataType = new DatasetComboBox(new String[] { DataConversionProperties.getString("String_ComboBoxDatasetCategory_Dataset2D"),
				DataConversionProperties.getString("String_ComboBoxDatasetCategory_Dataset3D") });
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

	private void setDatasetType(String dataType, ImportSettingLIDAR importsetting) {
		if (dataType.equals(DataConversionProperties.getString("String_ComboBoxDatasetCategory_Dataset2D"))) {
			importsetting.setImportingAs3D(false);
		} else {
			importsetting.setImportingAs3D(true);
		}
	}

	private void setSptialIndex(boolean isSpatial, ImportSettingLIDAR importsetting) {
		if (isSpatial) {
			importsetting.setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
		}
	}

	private void setImport(boolean isImport, ImportSettingLIDAR importsetting) {
		importsetting.setAttributeIgnored(isImport);
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

		checkboxSpatialIndex = new JCheckBox();
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		checkboxIgnorePropertyInfo = new JCheckBox();
		panelDatapath = new JPanel();
		labelFilePath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		labelCharset = new JLabel();
		comboBoxCharset = new CharsetComboBox();

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);

		// 设置fileInfo
		importsetting = (ImportSettingLIDAR) ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting, textFieldResultSet);
		// 设置目标数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码格式
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置数据集类型
		comboBoxDataType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dataType = comboBoxDataType.getSelectItem();
				int select = comboBoxDataType.getSelectedIndex();
				if (null != importsetting) {
					setDatasetType(dataType, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelLIDAR tempPanel = (ImportPanelLIDAR) panels.get(i);
						tempPanel.getComboBoxDataType().setSelectedIndex(select);
					}
				}
			}
		});

		// 是否创建空间索引
		checkboxSpatialIndex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSpatial = checkboxSpatialIndex.isSelected();
				if (null != importsetting) {
					setSptialIndex(isSpatial, importsetting);
					fileInfo.setBuildSpatialIndex(isSpatial);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelLIDAR tempPanel = (ImportPanelLIDAR) panels.get(i);
						tempPanel.getChckbxSpatialIndex().setSelected(isSpatial);
						fileInfos.get(i).setBuildSpatialIndex(isSpatial);
					}
				}
			}
		});
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 是否忽略属性信息
		checkboxIgnorePropertyInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isImport = checkboxIgnorePropertyInfo.isSelected();
				if (null != importsetting) {
					setImport(isImport, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelLIDAR tempPanel = (ImportPanelLIDAR) panels.get(i);
						tempPanel.getCheckboxIgnorePropertyInfo().setSelected(isImport);
					}
				}
			}
		});
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		setPreferredSize(new java.awt.Dimension(483, 300));

		comboBoxDataType.setSelectedIndex(1);
		// @formatter:off
        GroupLayout panelLayout = new GroupLayout(panel);
        panelLayout.setHorizontalGroup(
        	panelLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelLayout.createSequentialGroup()
        			.addGroup(panelLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelLayout.createSequentialGroup()
        					.addGroup(panelLayout.createParallelGroup(Alignment.TRAILING, false)
        						.addComponent(labelCodingType, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(labelDatasource, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        					.addGroup(panelLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(panelLayout.createSequentialGroup()
        							.addGap(24)
        							.addComponent(comboBoxCodingType, 0, 104,  GroupLayout.PREFERRED_SIZE))
        						.addGroup(panelLayout.createSequentialGroup()
        							.addGap(24)
        							.addComponent(comboBoxDatasource, 0, 104, GroupLayout.PREFERRED_SIZE))))
        				.addComponent(checkboxSpatialIndex))
        			.addGap(40)
        			.addGroup(panelLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelLayout.createSequentialGroup()
        					.addGroup(panelLayout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(labelDataset, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        						.addComponent(labelDatasetType, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        					.addGap(18)
        					.addGroup(panelLayout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(textFieldResultSet)
        						.addComponent(comboBoxDataType, 0, 104, Short.MAX_VALUE))))
        			.addGap(0, 39, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
        	panelLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelLayout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(panelLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelDatasource)
        				.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(labelDataset)
        				.addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGroup(panelLayout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(panelLayout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addGroup(panelLayout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(labelCodingType)
        						.addComponent(labelDatasetType)
        						.addComponent(comboBoxDataType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.UNRELATED))
        				.addGroup(Alignment.TRAILING, panelLayout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(7)))
        			.addGroup(panelLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(checkboxSpatialIndex)))
        );
        panel.setLayout(panelLayout);

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransform.setLayout(panelTransformLayout);
        panelTransformLayout.setHorizontalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(checkboxIgnorePropertyInfo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelImportModel)
                    .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxIgnorePropertyInfo)))
        );

        GroupLayout panelDatapathLayout = new GroupLayout(panelDatapath);
        panelDatapathLayout.setHorizontalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addComponent(labelFilePath, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addComponent(labelCharset)
        					.addGap(12)
        					.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)))
        			.addGap(0, 38, Short.MAX_VALUE))
        );
        panelDatapathLayout.setVerticalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelFilePath)
        				.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(buttonProperty))
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(labelCharset))
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addGap(9)
        					.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(30, Short.MAX_VALUE))
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
                .addComponent(panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
     // @formatter:on
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

	public JCheckBox getCheckboxIgnorePropertyInfo() {
		return checkboxIgnorePropertyInfo;
	}

	public void setCheckboxIgnorePropertyInfo(JCheckBox checkboxIgnorePropertyInfo) {
		this.checkboxIgnorePropertyInfo = checkboxIgnorePropertyInfo;
	}

	public JCheckBox getCheckboxSpatialIndex() {
		return checkboxSpatialIndex;
	}

	public void setCheckboxSpatialIndex(JCheckBox checkboxSpatialIndex) {
		this.checkboxSpatialIndex = checkboxSpatialIndex;
	}

	public JComboBox<Object> getComboBoxImportModel() {
		return comboBoxImportModel;
	}

	public void setComboBoxImportModel(JComboBox<Object> comboBoxImportModel) {
		this.comboBoxImportModel = comboBoxImportModel;
	}

	public JCheckBox getChckbxSpatialIndex() {
		return checkboxSpatialIndex;
	}

	public void setChckbxSpatialIndex(JCheckBox chckbxSpatialIndex) {
		this.checkboxSpatialIndex = chckbxSpatialIndex;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

}
