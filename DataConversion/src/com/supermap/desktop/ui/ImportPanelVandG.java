package com.supermap.desktop.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.util.ImportInfoUtil;

/**
 *
 * @author Administrator 实现右侧导入矢量和栅格数据类型的界面
 */
public class ImportPanelVandG extends JPanel {

	private static final long serialVersionUID = 1L;
	private transient DatasourceComboBox comboBoxDatasource;
	private JComboBox<Object> comboBoxImportModel;
	private JLabel labelDataSource;
	private JLabel labelImportModel;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient ImportSetting importsetting = null;

	public ImportPanelVandG() {
		initComponents();
	}

	public ImportPanelVandG(List<ImportFileInfo> fileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		initComponents();
	}

	public ImportPanelVandG(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	public void initResource() {
		labelDataSource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));

		comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
	}

	private void initComponents() {

		labelDataSource = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, null, comboBoxDatasource);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);

		setPreferredSize(new java.awt.Dimension(483, 300));
		// @formatter:off
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelDataSource)
                .addGap(18, 18, 18)
                .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDataSource)
                    .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelImportModel)
                    .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(248, Short.MAX_VALUE))
        );
    }

}
