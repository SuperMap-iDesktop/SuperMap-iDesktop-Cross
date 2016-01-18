package com.supermap.desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.supermap.data.Datasource;
import com.supermap.data.SpatialIndexInfo;
import com.supermap.data.SpatialIndexType;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingDWG;
import com.supermap.data.conversion.ImportSettingDXF;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.ImportInfoUtil;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * 实现右侧导入DWG,DXF数据类型的界面
 * 
 * @author Administrator
 */
public class ImportPanelD extends AbstractImportPanel {
	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JCheckBox checkboxExtendsData;
	private JCheckBox checkboxFieldIndex;
	private JCheckBox checkboxHeight;
	private JCheckBox checkboxImportLayer;
	private JCheckBox checkboxLineWidth;
	private JCheckBox checkboxMergeLayer;
	private JCheckBox checkboxProperty;
	private JCheckBox checkboxSaveField;
	private JCheckBox checkboxSpatialIndex;
	private JCheckBox checkboxSymbol;
	private JComboBox<Object> comboBoxImportMode;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private transient DatasetComboBox comboBoxDatatype;
	private JLabel labelCodingType;
	private JLabel labelCurve;
	private JLabel labelFilePath;
	private JLabel labelDataset;
	private JLabel labelDatasetType;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JPanel panelResultSetting;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldCure;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient ImportSetting importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	private ActionListener comboboxDatatypeAction;
	private ActionListener checkboxFieldIndexAction;
	private ActionListener checkboxSpatialIndexAction;
	private ActionListener checkboxMergeLayerAction;
	private ActionListener checkboxImportLayerAction;
	private ActionListener checkboxHeightAction;
	private ActionListener checkboxSymbolAction;
	private ActionListener checkboxPropertyAction;
	private ActionListener checkboxSaveFieldAction;
	private ActionListener checkboxExtendsDataAction;
	private ActionListener checkboxLineWidthAction;
	private ActionListener buttonPropertyAction;
	private transient LocalDocumentListener documentListener = new LocalDocumentListener();

	public ImportPanelD(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelD(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	void initResource() {
		this.labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelCurve.setText(DataConversionProperties.getString("string_label_lblCurve"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		this.labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		this.labelDatasetType.setText(DataConversionProperties.getString("string_label_lblDatasetType"));
		this.checkboxMergeLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxMergeLayer"));
		this.checkboxImportLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxImportLayer"));
		this.checkboxHeight.setText(DataConversionProperties.getString("string_checkbox_chckbxHeight"));
		this.checkboxSymbol.setText(DataConversionProperties.getString("string_checkbox_chckbxSymbol"));
		this.checkboxProperty.setText(DataConversionProperties.getString("string_checkbox_chckbxProperty"));
		this.checkboxSaveField.setText(DataConversionProperties.getString("string_checkbox_chckbxSaveField"));
		this.checkboxExtendsData.setText(DataConversionProperties.getString("string_checkbox_chckbxExtendsData"));
		this.checkboxLineWidth.setText(DataConversionProperties.getString("string_checkbox_chckbxLineWidth"));
		this.checkboxFieldIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxFieldIndex"));
		this.checkboxSpatialIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxSpatialIndex"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.panelResultSetting.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING,
				TitledBorder.TOP,
				null, null));
		this.panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.comboBoxImportMode.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
		this.comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_nullcoding"),
				DataConversionProperties.getString("string_comboboxitem_byte"), DataConversionProperties.getString("string_comboboxitem_int16"),
				DataConversionProperties.getString("string_comboboxitem_int24"), DataConversionProperties.getString("string_comboboxitem_int32") }));
	}

	private void setDataset(String dataset, ImportSetting tempSetting) {
		if (dataset.equals(DataConversionProperties.getString("string_comboboxitem_composite"))) {
			if (tempSetting instanceof ImportSettingDWG) {
				((ImportSettingDWG) tempSetting).setImportingAsCAD(true);
			}
			if (tempSetting instanceof ImportSettingDXF) {
				((ImportSettingDXF) tempSetting).setImportingAsCAD(true);
			}
		} else {
			if (tempSetting instanceof ImportSettingDWG) {
				((ImportSettingDWG) tempSetting).setImportingAsCAD(false);
			}
			if (tempSetting instanceof ImportSettingDXF) {
				((ImportSettingDXF) tempSetting).setImportingAsCAD(false);
			}
		}
	}

	private void setSpatialIndex(boolean isSin, ImportSetting tempSetting) {
		if (isSin) {
			if (tempSetting instanceof ImportSettingDXF) {
				((ImportSettingDXF) tempSetting).setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
			}
			if (tempSetting instanceof ImportSettingDWG) {
				((ImportSettingDWG) tempSetting).setSpatialIndex(new SpatialIndexInfo(SpatialIndexType.RTREE));
			}
		}
	}

	private void setMerge(boolean isMerge, ImportSetting tempSetting) {
		if (tempSetting instanceof ImportSettingDXF) {
			((ImportSettingDXF) tempSetting).setImportingByLayer(isMerge);
		}
		if (tempSetting instanceof ImportSettingDWG) {
			((ImportSettingDWG) tempSetting).setImportingByLayer(isMerge);
		}
	}

	private void setImportLayer(boolean isVisible, ImportSetting importsetting) {
		if (importsetting instanceof ImportSettingDXF) {
			((ImportSettingDXF) importsetting).setImportingInvisibleLayer(isVisible);
		}
		if (importsetting instanceof ImportSettingDWG) {
			((ImportSettingDWG) importsetting).setImportingInvisibleLayer(isVisible);
		}
	}

	private void setSymbol(boolean isSymbol, ImportSetting importsetting) {
		if (importsetting instanceof ImportSettingDWG) {
			((ImportSettingDWG) importsetting).setImportingBlockAsPoint(isSymbol);
		}
		if (importsetting instanceof ImportSettingDXF) {
			((ImportSettingDXF) importsetting).setImportingBlockAsPoint(isSymbol);
		}
	}

	private void setProperty(boolean isBlock, ImportSetting importsetting) {
		if (importsetting instanceof ImportSettingDWG) {
			((ImportSettingDWG) importsetting).setBlockAttributeIgnored(isBlock);
		}
		if (importsetting instanceof ImportSettingDXF) {
			((ImportSettingDXF) importsetting).setBlockAttributeIgnored(isBlock);
		}
	}

	private void setParametric(boolean isSave, ImportSetting importsetting) {
		if (importsetting instanceof ImportSettingDWG) {
			((ImportSettingDWG) importsetting).setKeepingParametricPart(isSave);
		}
		if (importsetting instanceof ImportSettingDXF) {
			((ImportSettingDXF) importsetting).setKeepingParametricPart(isSave);
		}
	}

	private void setXRecord(boolean isXRecord, ImportSetting importsetting) {
		if (importsetting instanceof ImportSettingDWG) {
			((ImportSettingDWG) importsetting).setImportingXRecord(isXRecord);
		}
		if (importsetting instanceof ImportSettingDXF) {
			((ImportSettingDXF) importsetting).setImportingXRecord(isXRecord);
		}
	}

	private void setLineWidth(boolean isLineWidth, ImportSetting importsetting) {
		if (importsetting instanceof ImportSettingDWG) {
			((ImportSettingDWG) importsetting).setLWPLineWidthIgnored(isLineWidth);
		}
		if (importsetting instanceof ImportSettingDXF) {
			((ImportSettingDXF) importsetting).setLWPLineWidthIgnored(isLineWidth);

		}
	}

	void initComponents() {

		this.panelResultSetting = new JPanel();
		this.labelDatasource = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.labelDataset = new JLabel();
		this.textFieldResultSet = new JTextField();
		this.textFieldResultSet.setColumns(10);
		this.labelCodingType = new JLabel();
		this.comboBoxCodingType = new JComboBox<Object>();
		this.labelDatasetType = new JLabel();
		this.checkboxFieldIndex = new JCheckBox();
		this.checkboxSpatialIndex = new JCheckBox();
		this.panelTransform = new JPanel();
		this.labelImportModel = new JLabel();
		this.comboBoxImportMode = new JComboBox<Object>();
		this.labelCurve = new JLabel();
		this.textFieldCure = new JTextField("73");
		this.checkboxMergeLayer = new JCheckBox();
		this.checkboxHeight = new JCheckBox();
		this.checkboxImportLayer = new JCheckBox();
		this.checkboxSymbol = new JCheckBox();
		this.checkboxProperty = new JCheckBox();
		this.checkboxSaveField = new JCheckBox();
		this.checkboxExtendsData = new JCheckBox();
		this.checkboxLineWidth = new JCheckBox();
		this.labelFilePath = new JLabel();
		this.textFieldFilePath = new JTextField();
		this.textFieldFilePath.setEditable(false);
		this.buttonProperty = new JButton();

		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置fileInfo
		this.importsetting = ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting, textFieldResultSet);
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportMode);
		// 主panel放两个子panel和三个组件
		initPanelD();
		// 结果设置panelResultSetting
		initPanelResultSetting();
		// 转换参数panelTransform
		initPanelTransform();
		this.checkboxMergeLayer.setSelected(true);
		this.checkboxSymbol.setSelected(true);
		this.checkboxLineWidth.setSelected(true);
		// @formatter:on
	}

	private void initPanelD() {
		// @formatter:off
		this.setLayout(new GridBagLayout());
		this.add(this.panelResultSetting,  new GridBagConstraintsHelper(0, 0, 3, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.BOTH).setInsets(5).setWeight(1, 1));
		this.add(this.panelTransform,      new GridBagConstraintsHelper(0, 1, 3, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.BOTH).setInsets(5).setWeight(1, 1));
		this.add(this.labelFilePath,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.HORIZONTAL).setInsets(5,10,10,5).setWeight(10, 1));
		this.add(this.textFieldFilePath,   new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.HORIZONTAL).setInsets(5,0,10,5).setWeight(70, 1));
		this.add(this.buttonProperty,      new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.HORIZONTAL).setInsets(5,0,10,5).setWeight(20, 1));
		//@formatter:on
	}

	private void initPanelResultSetting() {
		//@formatter:off
		//labelDatasource comboBoxDatasource labelDataset  		textFieldResultSet
		//labelCodingType comboBoxCodingType labelDatasetType 	comboBoxDatatype
		//checkboxFieldIndex				 checkboxSpatialIndex
		String[] datasetTypes = new String[] { DataConversionProperties.getString("string_comboboxitem_composite"),
				DataConversionProperties.getString("string_comboboxitem_sample") };

		this.comboBoxDatatype = new DatasetComboBox(datasetTypes);
		this.panelResultSetting.setLayout(new GridBagLayout());
		this.panelResultSetting.add(this.labelDatasource,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelResultSetting.add(this.comboBoxDatasource,  new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSetting.add(this.labelDataset,        new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelResultSetting.add(this.textFieldResultSet,  new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSetting.add(this.labelCodingType,     new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 5));
		this.panelResultSetting.add(this.comboBoxCodingType,  new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSetting.add(this.labelDatasetType,    new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 0, 5, 5));
		this.panelResultSetting.add(this.comboBoxDatatype,    new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSetting.add(this.checkboxFieldIndex,  new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 10,10, 20));
		this.panelResultSetting.add(this.checkboxSpatialIndex,new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0, 0, 10, 10));
		//@formatter:on
	}

	private void initPanelTransform() {
		// @formatter:off
		JPanel panelCheckBox = new JPanel();
		this.panelTransform.setLayout(new GridBagLayout());
		this.panelTransform.add(this.labelImportModel,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelTransform.add(this.comboBoxImportMode,  new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(35, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.labelCurve,          new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelTransform.add(this.textFieldCure,       new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(panelCheckBox,            new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(0).setFill(GridBagConstraints.BOTH));
		
		panelCheckBox.setLayout(new GridBagLayout());
		panelCheckBox.add(this.checkboxMergeLayer,  new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 10, 5, 20));
		panelCheckBox.add(this.checkboxImportLayer, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 20));
		panelCheckBox.add(this.checkboxHeight,      new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 20));
		panelCheckBox.add(this.checkboxSymbol,      new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 10, 5, 20));
		panelCheckBox.add(this.checkboxProperty,    new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 20));
		panelCheckBox.add(this.checkboxSaveField,   new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 10, 20));
		panelCheckBox.add(this.checkboxExtendsData, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 10, 10, 20));
		panelCheckBox.add(this.checkboxLineWidth,   new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 10, 20));
		// @formatter:on
	}

	public JCheckBox getChckbxExtendsData() {
		return checkboxExtendsData;
	}

	public JCheckBox getChckbxFieldIndex() {
		return checkboxFieldIndex;
	}

	public JCheckBox getChckbxHeight() {
		return checkboxHeight;
	}

	public JCheckBox getChckbxImportLayer() {
		return checkboxImportLayer;
	}

	public JCheckBox getChckbxLineWidth() {
		return checkboxLineWidth;
	}

	public JCheckBox getChckbxMergeLayer() {
		return checkboxMergeLayer;
	}

	public JCheckBox getChckbxProperty() {
		return checkboxProperty;
	}

	public JCheckBox getChckbxSaveField() {
		return checkboxSaveField;
	}

	public JCheckBox getChckbxSpatialIndex() {
		return checkboxSpatialIndex;
	}

	public JCheckBox getChckbxSymbol() {
		return checkboxSymbol;
	}

	public JComboBox<Object> getComboBox() {
		return comboBoxImportMode;
	}

	public JComboBox<Object> getComboBoxCodingType() {
		return comboBoxCodingType;
	}

	public DatasetComboBox getComboBoxDatatype() {
		return comboBoxDatatype;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public JTextField getTextFieldCure() {
		return textFieldCure;
	}

	class LocalDocumentListener implements DocumentListener {

		@Override
		public void removeUpdate(DocumentEvent e) {
			setCureInfo();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			setCureInfo();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setCureInfo();
		}

		private void setCureInfo() {
			String text = textFieldCure.getText();
			if (!text.isEmpty()) {
				if (null != importsetting) {
					setCure(text, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getTextFieldCure().setText(text);
					}
				}
			}
		}

		private void setCure(String text, ImportSetting importsetting) {
			if (importsetting instanceof ImportSettingDWG) {
				((ImportSettingDWG) importsetting).setCurveSegment(Integer.valueOf(text));
			}
			if (importsetting instanceof ImportSettingDXF) {
				((ImportSettingDXF) importsetting).setCurveSegment(Integer.valueOf(text));
			}
		}

	}

	@Override
	void registActionListener() {
		// 设置数据集类型
		this.comboboxDatatypeAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dataset = comboBoxDatatype.getSelectItem();
				int select = comboBoxDatatype.getSelectedIndex();
				if (null != importsetting) {
					setDataset(dataset, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getComboBoxDatatype().setSelectedIndex(select);
					}
				}
			}
		};

		// 是否创建字段索引
		this.checkboxFieldIndexAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isField = checkboxFieldIndex.isSelected();
				if (null != fileInfos) {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempJPanel = (ImportPanelD) panels.get(i);
						tempJPanel.getChckbxFieldIndex().setSelected(isField);
						fileInfos.get(i).setBuildFiledIndex(isField);
					}
				} else if (null != fileInfo) {
					fileInfo.setBuildFiledIndex(isField);
				}
			}
		};

		// 是否创建空间索引
		this.checkboxSpatialIndexAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSin = checkboxSpatialIndex.isSelected();
				if (null != importsetting) {
					setSpatialIndex(isSin, importsetting);
					fileInfo.setBuildSpatialIndex(isSin);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getChckbxSpatialIndex().setSelected(isSin);
						fileInfos.get(i).setBuildSpatialIndex(isSin);
					}
				}
			}
		};

		// 是否合并图层
		this.checkboxMergeLayerAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isMerge = checkboxMergeLayer.isSelected();
				if (null != importsetting) {
					setMerge(isMerge, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getChckbxMergeLayer().setSelected(isMerge);
					}
				}
			}
		};

		// 导入不可见图层
		this.checkboxImportLayerAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isVisible = checkboxImportLayer.isSelected();
				if (null != importsetting) {
					setImportLayer(isVisible, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getChckbxImportLayer().setSelected(isVisible);
					}
				}
			}
		};

		// 是否保留对象高度
		this.checkboxHeightAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSaveH = checkboxHeight.isSelected();
				if (null != fileInfos) {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempJPanel = (ImportPanelD) panels.get(i);
						tempJPanel.getChckbxHeight().setSelected(isSaveH);
					}
				}
			}
		};

		// 是否保留符号块
		this.checkboxSymbolAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSymbol = checkboxSymbol.isSelected();
				if (null != importsetting) {
					setSymbol(isSymbol, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getChckbxSymbol().setSelected(isSymbol);
					}
				}
			}
		};

		// 是否导入块属性
		this.checkboxPropertyAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isBlock = checkboxProperty.isSelected();
				if (null != importsetting) {
					setProperty(isBlock, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getChckbxProperty().setSelected(isBlock);
					}
				}
			}
		};
		// 保留参数化对象
		this.checkboxSaveFieldAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSave = checkboxSaveField.isSelected();
				if (null != importsetting) {
					setParametric(isSave, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getChckbxSaveField().setSelected(isSave);
					}
				}
			}
		};
		// 导入扩展数据
		this.checkboxExtendsDataAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isXRecord = checkboxExtendsData.isSelected();
				if (null != importsetting) {
					setXRecord(isXRecord, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getChckbxExtendsData().setSelected(isXRecord);
					}
				}
			}
		};
		// 保留多义线宽度
		this.checkboxLineWidthAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isLineWidth = checkboxLineWidth.isSelected();
				if (null != importsetting) {
					setLineWidth(isLineWidth, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelD tempPanel = (ImportPanelD) panels.get(i);
						tempPanel.getChckbxLineWidth().setSelected(isLineWidth);
					}
				}
			}
		};
		this.buttonPropertyAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		};
		this.comboBoxDatatype.addActionListener(this.comboboxDatatypeAction);
		this.checkboxFieldIndex.addActionListener(this.checkboxFieldIndexAction);
		this.checkboxSpatialIndex.addActionListener(this.checkboxSpatialIndexAction);
		// 设置曲线拟合精度
		this.textFieldCure.getDocument().addDocumentListener(this.documentListener);
		this.checkboxMergeLayer.addActionListener(this.checkboxMergeLayerAction);
		this.checkboxImportLayer.addActionListener(this.checkboxImportLayerAction);
		this.checkboxHeight.addActionListener(this.checkboxHeightAction);
		this.checkboxSymbol.addActionListener(this.checkboxSymbolAction);
		this.checkboxProperty.addActionListener(this.checkboxPropertyAction);
		this.checkboxSaveField.addActionListener(this.checkboxSaveFieldAction);
		this.checkboxExtendsData.addActionListener(this.checkboxExtendsDataAction);
		this.checkboxLineWidth.addActionListener(this.checkboxLineWidthAction);
		this.buttonProperty.addActionListener(this.buttonPropertyAction);
	}

	@Override
	void unregistActionListener() {
		this.comboBoxDatatype.removeActionListener(this.comboboxDatatypeAction);
		this.checkboxFieldIndex.removeActionListener(this.checkboxFieldIndexAction);
		this.checkboxSpatialIndex.removeActionListener(this.checkboxSpatialIndexAction);
		// 设置曲线拟合精度
		this.textFieldCure.getDocument().removeDocumentListener(this.documentListener);
		this.checkboxMergeLayer.removeActionListener(this.checkboxMergeLayerAction);
		this.checkboxImportLayer.removeActionListener(this.checkboxImportLayerAction);
		this.checkboxHeight.removeActionListener(this.checkboxHeightAction);
		this.checkboxSymbol.removeActionListener(this.checkboxSymbolAction);
		this.checkboxProperty.removeActionListener(this.checkboxPropertyAction);
		this.checkboxSaveField.removeActionListener(this.checkboxSaveFieldAction);
		this.checkboxExtendsData.removeActionListener(this.checkboxExtendsDataAction);
		this.checkboxLineWidth.removeActionListener(this.checkboxLineWidthAction);
		this.buttonProperty.removeActionListener(this.buttonPropertyAction);
	}
}
