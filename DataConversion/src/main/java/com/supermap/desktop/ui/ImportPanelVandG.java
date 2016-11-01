package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ImportFileInfo;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 实现右侧导入矢量和栅格数据类型的界面
 */
public class ImportPanelVandG extends AbstractImportPanel {

	private static final long serialVersionUID = 1L;
	private transient DatasourceComboBox comboBoxDatasource;
	private JComboBox<Object> comboBoxImportModel;
	private JLabel labelDataSource;
	private JLabel labelImportModel;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient ImportSetting importsetting = null;

	public ImportPanelVandG(List<ImportFileInfo> fileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		initComponents();
		initResource();
	}

	public ImportPanelVandG(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
	}

	@Override
	void initResource() {
		this.labelDataSource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover")}));
	}

	@Override
	void initComponents() {

		this.labelDataSource = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.labelImportModel = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();

		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, null, comboBoxDatasource);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);

		initPanelVandG();
	}

	private void initPanelVandG() {
		//@formatter:off
		this.setLayout(new GridBagLayout());
		this.add(this.labelDataSource,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setWeight(10, 1).setInsets(10, 10, 10, 5));
		this.add(this.comboBoxDatasource,  new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setWeight(40, 1).setInsets(10, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.add(this.labelImportModel,    new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setWeight(10, 1).setInsets(10, 0, 10, 5));
		this.add(this.comboBoxImportModel, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setWeight(40, 1).setInsets(10, 0, 10, 10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	@Override
	void registActionListener() {
		// TODO Auto-generated method stub

	}

	@Override
	void unregistActionListener() {
		// TODO Auto-generated method stub

	}

}
