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

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingBIL;
import com.supermap.data.conversion.ImportSettingBIP;
import com.supermap.data.conversion.ImportSettingBSQ;
import com.supermap.data.conversion.ImportSettingGRD;
import com.supermap.data.conversion.ImportSettingRAW;
import com.supermap.data.conversion.ImportSettingTEMSClutter;
import com.supermap.data.conversion.ImportSettingUSGSDEM;
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
 * @author Administrator 实现右侧导入dem,bil,bip,bsq,raw,grd,txt栅格数据类型的界面
 */
public class ImportPanelArcGIS extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JCheckBox checkBoxImageInfo;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelDatapath;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JPanel panelDataSet;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels = null;
	private transient ImportSetting importsetting = null;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelArcGIS() {
		initComponents();
	}

	public ImportPanelArcGIS(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelArcGIS(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void setImageInfo(boolean flag, ImportSetting importsetting) {
		if (importsetting instanceof ImportSettingTEMSClutter) {
			// 如果选择的是电信栅格文件
			((ImportSettingTEMSClutter) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingBIL) {
			// 如果选择的是BIL格式的文件
			((ImportSettingBIL) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingBIP) {
			// 如果选择的是BIP格式的文件
			((ImportSettingBIP) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingBSQ) {
			// 如果选择的是BIP格式的文件
			((ImportSettingBSQ) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingGRD) {
			// 如果选择的是grd,txt格式的文件
			((ImportSettingGRD) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingUSGSDEM) {
			// 如果选择的是dem格式的文件
			((ImportSettingUSGSDEM) importsetting).setPyramidBuilt(flag);
		}
		if (importsetting instanceof ImportSettingRAW) {
			// 如果选择的是raw格式的文件
			((ImportSettingRAW) importsetting).setPyramidBuilt(flag);
		}
	}

	private void initResource() {
		labelDatapath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		checkBoxImageInfo.setText(DataConversionProperties.getString("string_checkbox_chckbxImageInfo"));
		buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		buttonProperty.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		});
		panelDataSet.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTransform.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDatapath.setBorder(
				new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBoxCharset.setModel(new CommonComboBoxModel());
		comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
		comboBoxCodingType.setModel(
				new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_nullcoding"), "SGL", "LZW" }));
	}

	private void initComponents() {

		panelDataSet = new JPanel();
		labelDatasource = new JLabel();
		labelCodingType = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		comboBoxCodingType = new JComboBox<Object>();
		labelDataset = new JLabel();
		textFieldResultSet = new JTextField();
		textFieldResultSet.setColumns(10);
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		checkBoxImageInfo = new JCheckBox();
		panelDatapath = new JPanel();
		labelDatapath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		labelCharset = new JLabel();
		comboBoxCharset = new CharsetComboBox();
		initResource();

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		importsetting = ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting, textFieldResultSet);
		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置源文件字符集类型
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		// 设置是否建立影像金字塔
		checkBoxImageInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean flag = checkBoxImageInfo.isSelected();
				if (null != importsetting) {
					setImageInfo(flag, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelArcGIS tempPanel = (ImportPanelArcGIS) panels.get(i);
						tempPanel.getChckbxImageInfo().setSelected(flag);
					}
				}
			}
		});

		// @formatter:off
		//主panel添加三个子panel
		GroupLayout gl_panelArcGIS = new GroupLayout(this);
		gl_panelArcGIS.setAutoCreateGaps(true);
		gl_panelArcGIS.setAutoCreateContainerGaps(true);
		this.setLayout(gl_panelArcGIS);
		gl_panelArcGIS.setHorizontalGroup(gl_panelArcGIS.createParallelGroup(Alignment.LEADING)
					.addComponent(panelDataSet,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
					.addComponent(panelTransform,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
					.addComponent(panelDatapath,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE));
		gl_panelArcGIS.setVerticalGroup(gl_panelArcGIS.createSequentialGroup()			
					.addComponent(panelDataSet,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
					.addComponent(panelTransform,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
					.addComponent(panelDatapath,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE));
		
		//结果设置panel 
		GroupLayout gl_panel = new GroupLayout(panelDataSet);
		gl_panel.setAutoCreateContainerGaps(true);
		gl_panel.setAutoCreateGaps(true);
		panelDataSet.setLayout(gl_panel);
		gl_panel.setHorizontalGroup(gl_panel.createSequentialGroup()
			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(labelDatasource,GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
						.addComponent(labelCodingType,GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE))
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBoxDatasource,GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE)
						.addComponent(comboBoxCodingType,GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE))
			.addGap(packageInfo.DEFAULT_COMPONENT_GAP)
			.addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addComponent(textFieldResultSet,GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH,Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createSequentialGroup()
			.addGroup(gl_panel.createParallelGroup(Alignment.CENTER)
				.addComponent(labelDatasource,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(comboBoxDatasource,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(labelDataset,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(textFieldResultSet,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(gl_panel.createParallelGroup(Alignment.CENTER)
				.addComponent(labelCodingType,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(comboBoxCodingType,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)));
		
		//转换参数panelTransform
		GroupLayout gl_panelTransform = new GroupLayout(panelTransform);
		gl_panelTransform.setAutoCreateContainerGaps(true);
		gl_panelTransform.setAutoCreateGaps(true);
		panelTransform.setLayout(gl_panelTransform);
		gl_panelTransform.setHorizontalGroup(gl_panelTransform.createSequentialGroup()
			.addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
			.addGap(packageInfo.DEFAULT_COMPONENT_GAP)
			.addComponent(checkBoxImageInfo, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
			.addGap(GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE));
		
		gl_panelTransform.setVerticalGroup(gl_panelTransform.createParallelGroup (Alignment.CENTER)
		    .addComponent(labelImportModel,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
		    .addComponent(comboBoxImportModel,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
		    .addComponent(checkBoxImageInfo,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE));
		
		//源文件信息panelDatapath
		GroupLayout gl_panelDatapath = new GroupLayout(panelDatapath);
		gl_panelDatapath.setAutoCreateContainerGaps(true);
		gl_panelDatapath.setAutoCreateGaps(true);
		panelDatapath.setLayout(gl_panelDatapath);
		gl_panelDatapath.setHorizontalGroup(gl_panelDatapath.createParallelGroup(Alignment.LEADING)
			.addGroup(gl_panelDatapath.createSequentialGroup()
						.addComponent(labelDatapath, GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,packageInfo.DEFAULT_LABEL_WIDTH)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH,packageInfo.DEFAULT_COMPONENT_WIDTH))
			.addGroup(gl_panelDatapath.createSequentialGroup()
						.addComponent(labelCharset, GroupLayout.PREFERRED_SIZE,packageInfo.DEFAULT_LABEL_WIDTH,packageInfo.DEFAULT_LABEL_WIDTH)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, packageInfo.DEFAULT_COMPONENT_WIDTH,packageInfo.DEFAULT_COMPONENT_WIDTH)));
		gl_panelDatapath.setVerticalGroup(gl_panelDatapath.createSequentialGroup()
			.addGroup(gl_panelDatapath.createParallelGroup(Alignment.CENTER)
				.addComponent(labelDatapath, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
			.addGroup(gl_panelDatapath.createParallelGroup(Alignment.CENTER)
				.addComponent(labelCharset, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(comboBoxCharset,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)));
	}

	public JCheckBox getChckbxImageInfo() {
		return checkBoxImageInfo;
	}

	public void setChckbxImageInfo(JCheckBox chckbxImageInfo) {
		this.checkBoxImageInfo = chckbxImageInfo;
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
