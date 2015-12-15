package com.supermap.desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
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
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingDWG;
import com.supermap.data.conversion.ImportSettingDXF;
import com.supermap.data.conversion.ImportSettingE00;
import com.supermap.data.conversion.ImportSettingMAPGIS;
import com.supermap.data.conversion.ImportSettingMIF;
import com.supermap.data.conversion.ImportSettingSHP;
import com.supermap.data.conversion.ImportSettingTAB;
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
 * @author Administrator 实现右侧导入矢量数据类型的界面
 */
public class ImportPanelVECTOR extends JPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkboxExtendsData;
	private JCheckBox checkboxFieldIndex;
	private JCheckBox checkboxImport;
	private JCheckBox checkboxImportLayer;
	private JCheckBox checkboxImportProperty;
	private JCheckBox checkboxLineWidth;
	private JCheckBox checkboxMergeLayer;
	private JCheckBox checkboxSpatialIndex;
	private JCheckBox checkboxSymbol;
	private JComboBox<Object> comboBox;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelCurve;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JPanel panel;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldCure;
	private JTextField textFieldResultSet;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient ImportSetting importsetting = null;

	public ImportPanelVECTOR() {
		initComponents();
	}

	public ImportPanelVECTOR(List<ImportFileInfo> fileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		initComponents();
	}

	public ImportPanelVECTOR(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	public void initResource() {
		labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		labelCurve.setText(DataConversionProperties.getString("string_label_lblCurve"));
		labelDatasource.setText(DataConversionProperties.getString("string_label_targetDatasource"));
		labelDataset.setText(DataConversionProperties.getString("string_label_targetDataset"));
		labelCodingType.setText(DataConversionProperties.getString("string_label_encodingType"));
		labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));

		checkboxMergeLayer.setEnabled(false);
		checkboxMergeLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxMergeLayer"));
		checkboxImportLayer.setEnabled(false);
		checkboxImportLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxImportLayer"));
		checkboxSymbol.setEnabled(false);
		checkboxSymbol.setText(DataConversionProperties.getString("string_checkbox_chckbxSymbol"));
		checkboxImport.setEnabled(false);
		checkboxImport.setText(DataConversionProperties.getString("string_checkbox_chckbxImport"));
		checkboxImportProperty.setEnabled(false);
		checkboxImportProperty.setText(DataConversionProperties.getString("string_checkbox_chckbxImportProperty"));
		checkboxExtendsData.setEnabled(false);
		checkboxExtendsData.setText(DataConversionProperties.getString("string_checkbox_chckbxExtendsData"));
		checkboxLineWidth.setEnabled(false);
		checkboxLineWidth.setText(DataConversionProperties.getString("string_checkbox_chckbxLineWidth"));
		checkboxFieldIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxFieldIndex"));
		checkboxSpatialIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxSpatialIndex"));
		panel.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		comboBoxCharset.setModel(new CommonComboBoxModel());
		comboBoxCharset.setAutoscrolls(true);
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
		comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_nullcoding"),
				DataConversionProperties.getString("string_comboboxitem_byte"), DataConversionProperties.getString("string_comboboxitem_int16"),
				DataConversionProperties.getString("string_comboboxitem_int24"), DataConversionProperties.getString("string_comboboxitem_int32") }));
	}

	private void initComponents() {

		panel = new JPanel();
		labelDatasource = new JLabel();
		labelCodingType = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		comboBoxCodingType = new JComboBox<Object>();
		labelDataset = new JLabel();
		textFieldResultSet = new JTextField();
		textFieldResultSet.setColumns(10);
		checkboxFieldIndex = new JCheckBox();
		checkboxSpatialIndex = new JCheckBox();
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBox = new JComboBox<Object>();
		labelCurve = new JLabel();
		textFieldCure = new JTextField();
		checkboxSymbol = new JCheckBox();
		checkboxMergeLayer = new JCheckBox();
		checkboxExtendsData = new JCheckBox();
		checkboxImportLayer = new JCheckBox();
		checkboxImport = new JCheckBox();
		checkboxImportProperty = new JCheckBox();
		checkboxLineWidth = new JCheckBox();
		panelDatapath = new JPanel();
		labelCharset = new JLabel();
		comboBoxCharset = new CharsetComboBox();

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, null, comboBoxDatasource);
		// 创建字段索引
		checkboxFieldIndex.addActionListener(new LocalTextActionListener());
		// 创建空间索引
		checkboxSpatialIndex.addActionListener(new LocalActionListener());
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBox);
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		setPreferredSize(new java.awt.Dimension(483, 300));

		GroupLayout panelLayout = new GroupLayout(panel);
		panelLayout.setHorizontalGroup(panelLayout.createParallelGroup(Alignment.LEADING).addGroup(
				panelLayout
						.createSequentialGroup()
						.addGroup(
								panelLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(labelDatasource, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(labelCodingType, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(18)
						.addGroup(
								panelLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
										.addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
						.addGap(46)
						.addGroup(
								panelLayout
										.createParallelGroup(Alignment.LEADING, false)
										.addGroup(
												panelLayout.createSequentialGroup()
														.addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE).addGap(18)
														.addComponent(textFieldResultSet))
										.addGroup(
												panelLayout.createSequentialGroup().addComponent(checkboxFieldIndex)
														.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(checkboxSpatialIndex)))
						.addGap(0, 41, Short.MAX_VALUE)));
		panelLayout.setVerticalGroup(panelLayout.createParallelGroup(Alignment.LEADING).addGroup(
				panelLayout
						.createSequentialGroup()
						.addContainerGap(12, Short.MAX_VALUE)
						.addGroup(
								panelLayout.createParallelGroup(Alignment.BASELINE).addComponent(labelDatasource)
										.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(labelDataset)
										.addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(
								panelLayout.createParallelGroup(Alignment.BASELINE).addComponent(labelCodingType)
										.addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(checkboxFieldIndex).addComponent(checkboxSpatialIndex))));
		panel.setLayout(panelLayout);

		textFieldCure.setEnabled(false);

		checkboxSymbol.setEnabled(false);
		checkboxMergeLayer.setEnabled(false);
		checkboxExtendsData.setEnabled(false);
		checkboxImportLayer.setEnabled(false);
		checkboxImport.setEnabled(false);
		checkboxImportProperty.setEnabled(false);
		checkboxLineWidth.setEnabled(false);

		GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
		panelTransform.setLayout(panelTransformLayout);
		panelTransformLayout.setHorizontalGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				panelTransformLayout
						.createSequentialGroup()
						.addGroup(
								panelTransformLayout
										.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(
												panelTransformLayout
														.createSequentialGroup()
														.addGroup(
																panelTransformLayout
																		.createParallelGroup(GroupLayout.Alignment.LEADING)
																		.addComponent(checkboxExtendsData)
																		.addGroup(
																				GroupLayout.Alignment.TRAILING,
																				panelTransformLayout
																						.createSequentialGroup()
																						.addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																						.addGap(28)
																						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 104,
																								GroupLayout.PREFERRED_SIZE).addGap(46, 46, 46)
																						.addComponent(labelCurve)))
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGap(12)
														.addComponent(textFieldCure, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
										.addComponent(checkboxLineWidth)
										.addGroup(
												panelTransformLayout
														.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
														.addGroup(
																GroupLayout.Alignment.LEADING,
																panelTransformLayout.createSequentialGroup().addComponent(checkboxSymbol).addGap(154, 154, 154)
																		.addComponent(checkboxImportLayer))
														.addGroup(
																panelTransformLayout
																		.createSequentialGroup()
																		.addComponent(checkboxMergeLayer)
																		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addGroup(
																				panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
																						.addComponent(checkboxImportProperty).addComponent(checkboxImport)))))
						.addGap(0, 0, Short.MAX_VALUE)));
		panelTransformLayout.setVerticalGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				panelTransformLayout
						.createSequentialGroup()
						.addGroup(
								panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(labelImportModel)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(labelCurve)
										.addComponent(textFieldCure, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(checkboxSymbol)
										.addComponent(checkboxImportLayer))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(checkboxMergeLayer)
										.addComponent(checkboxImport))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(checkboxExtendsData)
										.addComponent(checkboxImportProperty)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(checkboxLineWidth)));

		GroupLayout panelDatapathLayout = new GroupLayout(panelDatapath);
		panelDatapath.setLayout(panelDatapathLayout);
		panelDatapathLayout.setHorizontalGroup(panelDatapathLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				panelDatapathLayout.createSequentialGroup().addComponent(labelCharset).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)));
		panelDatapathLayout.setVerticalGroup(panelDatapathLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				panelDatapathLayout
						.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(
								panelDatapathLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(labelCharset)
										.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))));

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isSin = checkboxSpatialIndex.isSelected();
			if (isSin) {
				for (int i = 0; i < panels.size(); i++) {
					ImportSetting tempSetting = fileInfos.get(i).getImportSetting();
					JPanel tempPanel = panels.get(i);
					if (tempPanel instanceof ImportPanelSCV) {
						((ImportPanelSCV) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempSetting instanceof ImportSettingDXF) {
						((ImportSettingDXF) tempSetting).setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
						((ImportPanelD) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempSetting instanceof ImportSettingDWG) {
						((ImportSettingDWG) tempSetting).setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
						((ImportPanelD) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempSetting instanceof ImportSettingSHP) {
						((ImportSettingSHP) tempSetting).setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
						((ImportPanelSHP) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempSetting instanceof ImportSettingE00) {
						((ImportSettingE00) tempSetting).setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
						((ImportPanelE00) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempSetting instanceof ImportSettingMIF) {
						((ImportSettingMIF) tempSetting).setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
						((ImportPanelMapInfo) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempSetting instanceof ImportSettingTAB) {
						((ImportSettingTAB) tempSetting).setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
						((ImportPanelMapInfo) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempSetting instanceof ImportSettingMAPGIS) {
						((ImportPanelMapGIS) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempPanel instanceof ImportPanelKML) {
						((ImportPanelKML) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					fileInfos.get(i).setBuildSpatialIndex(isSin);
				}
			} else {
				for (int i = 0; i < panels.size(); i++) {
					JPanel tempPanel = panels.get(i);
					if (tempPanel instanceof ImportPanelSCV) {
						((ImportPanelSCV) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempPanel instanceof ImportPanelD) {
						((ImportPanelD) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempPanel instanceof ImportPanelSHP) {
						((ImportPanelSHP) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempPanel instanceof ImportPanelE00) {
						((ImportPanelE00) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempPanel instanceof ImportPanelMapInfo) {
						((ImportPanelMapInfo) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempPanel instanceof ImportPanelMapInfo) {
						((ImportPanelMapInfo) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempPanel instanceof ImportPanelMapGIS) {
						((ImportPanelMapGIS) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
					if (tempPanel instanceof ImportPanelKML) {
						((ImportPanelKML) tempPanel).getChckbxSpatialIndex().setSelected(isSin);
					}
				}
			}
		}

	}

	class LocalTextActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isField = checkboxFieldIndex.isSelected();
			for (int i = 0; i < panels.size(); i++) {
				JPanel tempPanel = panels.get(i);
				if (tempPanel instanceof ImportPanelSCV) {
					((ImportPanelSCV) tempPanel).getChckbxFieldIndex().setSelected(isField);
				}
				if (tempPanel instanceof ImportPanelD) {
					((ImportPanelD) tempPanel).getChckbxFieldIndex().setSelected(isField);
				}
				if (tempPanel instanceof ImportPanelSHP) {
					((ImportPanelSHP) tempPanel).getChckbxFieldIndex().setSelected(isField);
				}
				if (tempPanel instanceof ImportPanelMapInfo) {
					((ImportPanelMapInfo) tempPanel).getChckbxFieldIndex().setSelected(isField);
				}
				if (tempPanel instanceof ImportPanelMapInfo) {
					((ImportPanelMapInfo) tempPanel).getChckbxFieldIndex().setSelected(isField);
				}
				if (tempPanel instanceof ImportPanelMapGIS) {
					((ImportPanelMapGIS) tempPanel).getChckbxFieldIndex().setSelected(isField);
				}
				if (tempPanel instanceof ImportPanelKML) {
					((ImportPanelKML) tempPanel).getChckbxFieldIndex().setSelected(isField);
				}
				fileInfos.get(i).setBuildFiledIndex(isField);
			}
		}

	}
}
