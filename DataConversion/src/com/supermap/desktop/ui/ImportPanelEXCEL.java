package com.supermap.desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import com.supermap.desktop.ui.controls.comboBox.ComboBoxDatasource;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Administrator 实现右侧导入excel数据类型的界面
 */
public class ImportPanelEXCEL extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JCheckBox checkboxDataInfo;
	private JCheckBox checkboxFieldIndex;
	private transient ComboBoxDatasource comboBoxDatasource;
	private JLabel labelFilepath;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private JPanel panel;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private ArrayList<ImportFileInfo> fileInfos;
	private transient ImportSetting importsetting = null;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelEXCEL() {
		initComponents();
	}

	public ImportPanelEXCEL(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelEXCEL(List<ImportFileInfo> fileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		initComponents();
	}

	private void initResource() {
		labelFilepath.setText(DataConversionProperties
				.getString("string_label_lblDataPath"));
		labelDatasource.setText(DataConversionProperties
				.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties
				.getString("string_label_lblDataset"));
		buttonProperty.setText(DataConversionProperties
				.getString("string_button_property"));
		checkboxDataInfo.setText(DataConversionProperties
				.getString("string_checkbox_chckbxDataInfo"));
		checkboxFieldIndex.setText(DataConversionProperties
				.getString("string_checkbox_chckbxFieldIndex"));
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

	}

	private void initComponents() {

		panel = new JPanel();
		labelDatasource = new JLabel();
		comboBoxDatasource = new ComboBoxDatasource();
		labelDataset = new JLabel();
		textFieldResultSet = new JTextField();
		textFieldResultSet.setColumns(10);
		checkboxFieldIndex = new JCheckBox();
		panelTransform = new JPanel();
		checkboxDataInfo = new JCheckBox();
		labelFilepath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		
		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();
		// 设置fileInfo
		importsetting = ImportInfoUtil.setFileInfo(datasource,fileInfos, fileInfo,
				textFieldFilePath, importsetting, textFieldResultSet);

		setPreferredSize(new java.awt.Dimension(483, 300));
		// @formatter:off
        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelDatasource, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkboxFieldIndex))
                .addGap(0, 37, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelDataset)
                        .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelDatasource))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkboxFieldIndex))
        );

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransform.setLayout(panelTransformLayout);
        panelTransformLayout.setHorizontalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addComponent(checkboxDataInfo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(checkboxDataInfo))
        );

        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        		.addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(labelFilepath)
        			.addGap(18)
        			.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 80, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelFilepath)
        				.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(buttonProperty))
        			.addGap(0, 132, Short.MAX_VALUE))
        );
        this.setLayout(layout);
     // @formatter:on
	}

}
