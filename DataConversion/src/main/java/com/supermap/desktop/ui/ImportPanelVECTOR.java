package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.SpatialIndexInfo;
import com.supermap.data.SpatialIndexType;
import com.supermap.data.conversion.*;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
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
 * @author Administrator 实现右侧导入矢量数据类型的界面
 */
public class ImportPanelVECTOR extends AbstractImportPanel {

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
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelCurve;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JPanel panelResultSet;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldCure;
	private JTextField textFieldResultSet;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient ImportSetting importsetting = null;
	private LocalActionListener localActionListener;
	private LocalTextActionListener textActionListener;

	public ImportPanelVECTOR(List<ImportFileInfo> fileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelVECTOR(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	@Override
	void initResource() {
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelCurve.setText(DataConversionProperties.getString("string_label_lblCurve"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_targetDatasource"));
		this.labelDataset.setText(DataConversionProperties.getString("string_label_targetDataset"));
		this.labelCodingType.setText(DataConversionProperties.getString("string_label_encodingType"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));

		this.checkboxMergeLayer.setEnabled(false);
		this.checkboxMergeLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxMergeLayer"));
		this.checkboxImportLayer.setEnabled(false);
		this.checkboxImportLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxImportLayer"));
		this.checkboxSymbol.setEnabled(false);
		this.checkboxSymbol.setText(DataConversionProperties.getString("string_checkbox_chckbxSymbol"));
		this.checkboxImport.setEnabled(false);
		this.checkboxImport.setText(DataConversionProperties.getString("string_checkbox_chckbxImport"));
		this.checkboxImportProperty.setEnabled(false);
		this.checkboxImportProperty.setText(DataConversionProperties.getString("string_checkbox_chckbxImportProperty"));
		this.checkboxExtendsData.setEnabled(false);
		this.checkboxExtendsData.setText(DataConversionProperties.getString("string_checkbox_chckbxExtendsData"));
		this.checkboxLineWidth.setEnabled(false);
		this.checkboxLineWidth.setText(DataConversionProperties.getString("string_checkbox_chckbxLineWidth"));
		this.checkboxFieldIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxFieldIndex"));
		this.checkboxSpatialIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxSpatialIndex"));
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
				DataConversionProperties.getString("string_comboboxitem_byte"), DataConversionProperties.getString("string_comboboxitem_int16"),
				DataConversionProperties.getString("string_comboboxitem_int24"), DataConversionProperties.getString("string_comboboxitem_int32")}));
	}

	@Override
	void initComponents() {

		this.panelResultSet = new JPanel();
		this.labelDatasource = new JLabel();
		this.labelCodingType = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxCodingType = new JComboBox<Object>();
		this.labelDataset = new JLabel();
		this.textFieldResultSet = new JTextField();
		this.textFieldResultSet.setColumns(10);
		this.checkboxFieldIndex = new JCheckBox();
		this.checkboxSpatialIndex = new JCheckBox();
		this.panelTransform = new JPanel();
		this.labelImportModel = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.labelCurve = new JLabel();
		this.textFieldCure = new JTextField();
		this.checkboxSymbol = new JCheckBox();
		this.checkboxMergeLayer = new JCheckBox();
		this.checkboxExtendsData = new JCheckBox();
		this.checkboxImportLayer = new JCheckBox();
		this.checkboxImport = new JCheckBox();
		this.checkboxImportProperty = new JCheckBox();
		this.checkboxLineWidth = new JCheckBox();
		this.panelDatapath = new JPanel();
		this.labelCharset = new JLabel();
		this.comboBoxCharset = new CharsetComboBox();

		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);
        if (null != importsetting.getSourceFileCharset()) {
            comboBoxCharset.setSelectCharset(importsetting.getSourceFileCharset().name());
        }
        // 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, null, comboBoxDatasource);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		initPanelResultSet();

		initpanelTransform();

		initPanelDatapath();

		initPanelVECTOR();
		this.textFieldCure.setEnabled(false);

		this.checkboxSymbol.setEnabled(false);
		this.checkboxMergeLayer.setEnabled(false);
		this.checkboxExtendsData.setEnabled(false);
		this.checkboxImportLayer.setEnabled(false);
		this.checkboxImport.setEnabled(false);
		this.checkboxImportProperty.setEnabled(false);
		this.checkboxLineWidth.setEnabled(false);
	}

	private void initPanelVECTOR() {
		//@formatter:off
		this.setLayout(new GridBagLayout());
		this.add(this.panelResultSet,       new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraintsHelper.BOTH).setInsets(5).setWeight(1, 1));
		this.add(this.panelTransform,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraintsHelper.BOTH).setInsets(5).setWeight(1, 1));
		this.add(this.panelDatapath,        new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraintsHelper.BOTH).setInsets(5).setWeight(1, 1));
		//@formatter:on
	}

	private void initPanelDatapath() {
		//@formatter:off
		this.panelDatapath.setLayout(new GridBagLayout());
		this.panelDatapath.add(this.labelCharset,          new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 10, 5));
		this.panelDatapath.add(this.comboBoxCharset,       new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(90, 1).setInsets(10, 0, 10, 20));
		//@formatter:on
	}

	private void initpanelTransform() {
		//@formatter:off
		this.panelTransform.setLayout(new GridBagLayout());
		this.panelTransform.add(this.labelImportModel,       new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelTransform.add(this.comboBoxImportModel,    new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.labelCurve,             new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelTransform.add(this.textFieldCure,          new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.checkboxSymbol,         new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 10, 5, 20));
		this.panelTransform.add(this.checkboxImportLayer,    new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 0,  5, 10));
		this.panelTransform.add(this.checkboxMergeLayer,     new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 10, 5, 20));
		this.panelTransform.add(this.checkboxImport,         new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 0,  5, 10));
		this.panelTransform.add(this.checkboxExtendsData,    new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 10, 5, 20));
		this.panelTransform.add(this.checkboxImportProperty, new GridBagConstraintsHelper(2, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 0,  5, 10));
		this.panelTransform.add(this.checkboxLineWidth,      new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 10, 10,10));
		// @formatter:on
	}

	private void initPanelResultSet() {
		//@formatter:off
		this.panelResultSet.setLayout(new GridBagLayout());
		this.panelResultSet.add(this.labelDatasource,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelResultSet.add(this.comboBoxDatasource,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelDataset,         new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelResultSet.add(this.textFieldResultSet,   new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelCodingType,      new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelResultSet.add(this.comboBoxCodingType,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.checkboxFieldIndex,   new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 0, 10, 5));
		this.panelResultSet.add(this.checkboxSpatialIndex, new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 10));
		// @formatter:on
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

	@Override
	void registActionListener() {
		this.localActionListener = new LocalActionListener();
		this.textActionListener = new LocalTextActionListener();
		unregistActionListener();
		// 创建字段索引
		this.checkboxFieldIndex.addActionListener(this.textActionListener);
		// 创建空间索引
		this.checkboxSpatialIndex.addActionListener(this.localActionListener);
	}

	@Override
	void unregistActionListener() {
		this.checkboxFieldIndex.removeActionListener(this.textActionListener);
		this.checkboxSpatialIndex.removeActionListener(this.localActionListener);
	}
}
