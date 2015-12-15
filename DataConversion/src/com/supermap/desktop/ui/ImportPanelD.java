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

import com.supermap.data.Datasource;
import com.supermap.data.SpatialIndexInfo;
import com.supermap.data.SpatialIndexType;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingDWG;
import com.supermap.data.conversion.ImportSettingDXF;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Dimension;

/**
 * 实现右侧导入DWG,DXF数据类型的界面
 * 
 * @author Administrator
 */
public class ImportPanelD extends JPanel {
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

	public ImportPanelD() {
		initComponents();
	}

	public ImportPanelD(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelD(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void initResource() {
		labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		labelCurve.setText(DataConversionProperties.getString("string_label_lblCurve"));
		labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		labelDatasetType.setText(DataConversionProperties.getString("string_label_lblDatasetType"));
		checkboxMergeLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxMergeLayer"));
		checkboxImportLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxImportLayer"));
		checkboxHeight.setText(DataConversionProperties.getString("string_checkbox_chckbxHeight"));
		checkboxSymbol.setText(DataConversionProperties.getString("string_checkbox_chckbxSymbol"));
		checkboxProperty.setText(DataConversionProperties.getString("string_checkbox_chckbxProperty"));
		checkboxSaveField.setText(DataConversionProperties.getString("string_checkbox_chckbxSaveField"));
		checkboxExtendsData.setText(DataConversionProperties.getString("string_checkbox_chckbxExtendsData"));
		checkboxLineWidth.setText(DataConversionProperties.getString("string_checkbox_chckbxLineWidth"));
		checkboxFieldIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxFieldIndex"));
		checkboxSpatialIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxSpatialIndex"));
		buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		buttonProperty.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		});
		panelResultSetting.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		comboBoxImportMode.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
		String[] datasetTypes = new String[] { DataConversionProperties.getString("string_comboboxitem_composite"),
				DataConversionProperties.getString("string_comboboxitem_sample") };

		comboBoxDatatype = new DatasetComboBox(datasetTypes);
		comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_nullcoding"),
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

	private void initComponents() {

		panelResultSetting = new JPanel();
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
		comboBoxImportMode = new JComboBox<Object>();
		labelCurve = new JLabel();
		textFieldCure = new JTextField("73");
		checkboxMergeLayer = new JCheckBox();
		checkboxHeight = new JCheckBox();
		checkboxImportLayer = new JCheckBox();
		checkboxSymbol = new JCheckBox();
		checkboxProperty = new JCheckBox();
		checkboxSaveField = new JCheckBox();
		checkboxExtendsData = new JCheckBox();
		checkboxLineWidth = new JCheckBox();
		labelFilePath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		setPreferredSize(new Dimension(654, 332));

		initResource();
		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置fileInfo
		importsetting = ImportInfoUtil.setFileInfo(datasource,fileInfos, fileInfo, textFieldFilePath, importsetting, textFieldResultSet);
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置数据集类型
		comboBoxDatatype.addActionListener(new ActionListener() {
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
		});
		// 是否创建字段索引
		checkboxFieldIndex.addActionListener(new ActionListener() {
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
		});
		// 是否创建空间索引
		checkboxSpatialIndex.addActionListener(new ActionListener() {
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
		});
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportMode);
		// 设置曲线拟合精度
		textFieldCure.getDocument().addDocumentListener(new LocalDocumentListener());
		// 是否合并图层
		checkboxMergeLayer.addActionListener(new ActionListener() {
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
		});
		// 导入不可见图层
		checkboxImportLayer.addActionListener(new ActionListener() {
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
		});
		// 是否保留对象高度
		checkboxHeight.addActionListener(new ActionListener() {
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
		});
		// 是否保留符号块
		checkboxSymbol.addActionListener(new ActionListener() {
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
		});
		// 是否导入块属性
		checkboxProperty.addActionListener(new ActionListener() {
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
		});
		// 保留参数化对象
		checkboxSaveField.addActionListener(new ActionListener() {
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
		});
		// 导入扩展数据
		checkboxExtendsData.addActionListener(new ActionListener() {
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
		});
		// 保留多义线宽度
		checkboxLineWidth.addActionListener(new ActionListener() {
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
		});
		// @formatter:off
		GroupLayout gl_panelD = new GroupLayout(this);
		gl_panelD.setAutoCreateContainerGaps(true);
		gl_panelD.setAutoCreateGaps(true);
		this.setLayout(gl_panelD);
		
		//主panel放两个子panel和三个组件
		//水平组
		gl_panelD.setHorizontalGroup(gl_panelD.createParallelGroup(Alignment.LEADING)
			.addComponent(panelResultSetting,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
			.addComponent(panelTransform,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
			.addGroup(gl_panelD.createSequentialGroup()
				.addComponent(labelFilePath,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,packageInfo.DEFAULT_LABEL_WIDTH)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(textFieldFilePath,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(buttonProperty,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,packageInfo.DEFAULT_COMPONENT_WIDTH)));
		//竖直组
		gl_panelD.setVerticalGroup(gl_panelD.createSequentialGroup()
			.addComponent(panelResultSetting, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(panelTransform,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
			.addGroup(gl_panelD.createParallelGroup(Alignment.CENTER)
				.addComponent(labelFilePath,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(textFieldFilePath,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(buttonProperty,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
				.addGap(packageInfo.DEFAULT_COMPONENT_GAP));
		
		//结果设置panelResultSetting 
		//labelDatasource comboBoxDatasource labelDataset  		textFieldResultSet
		//labelCodingType comboBoxCodingType labelDatasetType 	comboBoxDatatype
		//checkboxFieldIndex				 checkboxSpatialIndex
		
		GroupLayout gl_panelResultSetting = new GroupLayout(panelResultSetting);
		gl_panelResultSetting.setAutoCreateContainerGaps(true);
		gl_panelResultSetting.setAutoCreateGaps(true);
		panelResultSetting.setLayout(gl_panelResultSetting);
		//水平组
		gl_panelResultSetting.setHorizontalGroup(gl_panelResultSetting.createParallelGroup(Alignment.LEADING)
			.addGroup(gl_panelResultSetting.createSequentialGroup()
				.addGroup(gl_panelResultSetting.createParallelGroup(Alignment.LEADING)
					.addComponent(labelDatasource, GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
					.addComponent(labelCodingType, GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panelResultSetting.createParallelGroup(Alignment.LEADING)
					.addComponent(comboBoxDatasource,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
					.addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE))
				.addGap(packageInfo.DEFAULT_COMPONENT_GAP)
				.addGroup(gl_panelResultSetting.createParallelGroup(Alignment.LEADING)
					.addComponent(labelDataset, GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
					.addComponent(labelDatasetType, GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panelResultSetting.createParallelGroup(Alignment.LEADING)
					.addComponent(textFieldResultSet,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
					.addComponent(comboBoxDatatype, GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)))
			.addGroup(gl_panelResultSetting.createSequentialGroup()
				.addComponent(checkboxFieldIndex,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
				.addGap(packageInfo.DEFAULT_COMPONENT_GAP)
				.addComponent(checkboxSpatialIndex,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)));
		//竖直组
		gl_panelResultSetting.setVerticalGroup(gl_panelResultSetting.createParallelGroup(Alignment.CENTER)
			.addGroup(gl_panelResultSetting.createSequentialGroup()
				.addGroup(gl_panelResultSetting.createParallelGroup(Alignment.CENTER)
					.addComponent(labelDatasource, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
					.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelResultSetting.createParallelGroup(Alignment.CENTER)
					.addComponent(labelCodingType, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
					.addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
				.addComponent(checkboxFieldIndex))
			.addGroup(gl_panelResultSetting.createSequentialGroup()
				.addGroup(gl_panelResultSetting.createParallelGroup(Alignment.CENTER)
					.addComponent(labelDataset, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
					.addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelResultSetting.createParallelGroup(Alignment.CENTER)
					.addComponent(labelDatasetType, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
					.addComponent(comboBoxDatatype, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
				.addComponent(checkboxSpatialIndex)));
		
		checkboxMergeLayer.setSelected(true);
        checkboxSymbol.setSelected(true);
        checkboxLineWidth.setSelected(true);
		
		//转换参数panelTransform
		//labelImportType	 comboBoxImportMode 	labelCurve	 textFieldCure
		//checkboxMergeLayer		checkboxImportLayer			checkboxHeight
		//checkboxSymbol			checkboxProperty			checkboxSaveField
		//checkboxExtendsData		checkboxLineWidth
        GroupLayout gl_panelTransform = new GroupLayout(panelTransform);
        gl_panelTransform.setAutoCreateContainerGaps(true);
        gl_panelTransform.setAutoCreateGaps(true);
        panelTransform.setLayout(gl_panelTransform);
        //水平组
        gl_panelTransform.setHorizontalGroup(gl_panelTransform.createParallelGroup(Alignment.LEADING)
        	.addGroup(gl_panelTransform.createSequentialGroup()
        		.addComponent(labelImportModel,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
        		.addPreferredGap(ComponentPlacement.RELATED)
        		.addComponent(comboBoxImportMode,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
        		.addGap(packageInfo.DEFAULT_COMPONENT_GAP)
        		.addComponent(labelCurve,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
        		.addPreferredGap(ComponentPlacement.RELATED)
        		.addComponent(textFieldCure,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE))
        	.addGroup(gl_panelTransform.createSequentialGroup()
                .addComponent(checkboxMergeLayer,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(checkboxImportLayer,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(checkboxHeight,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE))
            .addGroup(gl_panelTransform.createSequentialGroup()
                .addComponent(checkboxSymbol,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(checkboxProperty,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(checkboxSaveField,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE))
             .addGroup(gl_panelTransform.createSequentialGroup()
                .addComponent(checkboxExtendsData,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(checkboxLineWidth,GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
                .addGap(GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)));
        //竖直组
        gl_panelTransform.setVerticalGroup(gl_panelTransform.createSequentialGroup()
             .addGroup(gl_panelTransform.createParallelGroup(Alignment.CENTER)
                .addComponent(labelImportModel,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                .addComponent(comboBoxImportMode,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                .addComponent(labelCurve,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                .addComponent(textFieldCure,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
             .addGroup(gl_panelTransform.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panelTransform.createSequentialGroup()
                    .addComponent(checkboxMergeLayer,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxSymbol,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxExtendsData,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
                .addGroup(gl_panelTransform.createSequentialGroup()
                    .addComponent(checkboxImportLayer,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxProperty,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxLineWidth,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
                .addGroup(gl_panelTransform.createSequentialGroup()
                    .addComponent(checkboxHeight,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxSaveField,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))));
     // @formatter:on
	}

	public JCheckBox getChckbxExtendsData() {
		return checkboxExtendsData;
	}

	public void setChckbxExtendsData(JCheckBox chckbxExtendsData) {
		this.checkboxExtendsData = chckbxExtendsData;
	}

	public JCheckBox getChckbxFieldIndex() {
		return checkboxFieldIndex;
	}

	public void setChckbxFieldIndex(JCheckBox chckbxFieldIndex) {
		this.checkboxFieldIndex = chckbxFieldIndex;
	}

	public JCheckBox getChckbxHeight() {
		return checkboxHeight;
	}

	public void setChckbxHeight(JCheckBox chckbxHeight) {
		this.checkboxHeight = chckbxHeight;
	}

	public JCheckBox getChckbxImportLayer() {
		return checkboxImportLayer;
	}

	public void setChckbxImportLayer(JCheckBox chckbxImportLayer) {
		this.checkboxImportLayer = chckbxImportLayer;
	}

	public JCheckBox getChckbxLineWidth() {
		return checkboxLineWidth;
	}

	public void setChckbxLineWidth(JCheckBox chckbxLineWidth) {
		this.checkboxLineWidth = chckbxLineWidth;
	}

	public JCheckBox getChckbxMergeLayer() {
		return checkboxMergeLayer;
	}

	public void setChckbxMergeLayer(JCheckBox chckbxMergeLayer) {
		this.checkboxMergeLayer = chckbxMergeLayer;
	}

	public JCheckBox getChckbxProperty() {
		return checkboxProperty;
	}

	public void setChckbxProperty(JCheckBox chckbxProperty) {
		this.checkboxProperty = chckbxProperty;
	}

	public JCheckBox getChckbxSaveField() {
		return checkboxSaveField;
	}

	public void setChckbxSaveField(JCheckBox chckbxSaveField) {
		this.checkboxSaveField = chckbxSaveField;
	}

	public JCheckBox getChckbxSpatialIndex() {
		return checkboxSpatialIndex;
	}

	public void setChckbxSpatialIndex(JCheckBox chckbxSpatialIndex) {
		this.checkboxSpatialIndex = chckbxSpatialIndex;
	}

	public JCheckBox getChckbxSymbol() {
		return checkboxSymbol;
	}

	public void setChckbxSymbol(JCheckBox chckbxSymbol) {
		this.checkboxSymbol = chckbxSymbol;
	}

	public JComboBox<Object> getComboBox() {
		return comboBoxImportMode;
	}

	public void setComboBox(JComboBox<Object> comboBox) {
		this.comboBoxImportMode = comboBox;
	}

	public JComboBox<Object> getComboBoxCodingType() {
		return comboBoxCodingType;
	}

	public void setComboBoxCodingType(JComboBox<Object> comboBoxCodingType) {
		this.comboBoxCodingType = comboBoxCodingType;
	}

	public DatasetComboBox getComboBoxDatatype() {
		return comboBoxDatatype;
	}

	public void setComboBoxDatatype(DatasetComboBox comboBoxDatatype) {
		this.comboBoxDatatype = comboBoxDatatype;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

	public JTextField getTextFieldCure() {
		return textFieldCure;
	}

	public void setTextFieldCure(JTextField textFieldCure) {
		this.textFieldCure = textFieldCure;
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
}
