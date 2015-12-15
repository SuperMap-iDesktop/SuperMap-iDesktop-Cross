package com.supermap.desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.TitledBorder;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingBMP;
import com.supermap.data.conversion.ImportSettingGIF;
import com.supermap.data.conversion.ImportSettingIMG;
import com.supermap.data.conversion.ImportSettingJPG;
import com.supermap.data.conversion.ImportSettingPNG;
import com.supermap.data.conversion.ImportSettingTIF;
import com.supermap.desktop.Application;
import com.supermap.desktop.FileChooserControl;
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
 * @author Administrator 实现右侧导入栅格数据类型的界面
 */
public class ImportPanelGRID extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasetComboBox comboBoxDataType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JComboBox<Object> comboBoxImportType;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelFilePath;
	private JLabel labelDatasetType;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JLabel labelNewLabel;
	private JLabel labelPassword;
	private JLabel labelSaveImport;
	private JPanel panel;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldPassword;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient FileChooserControl fileChooser;
	private transient ImportSetting importsetting = null;

	public ImportPanelGRID() {
		initComponents();
	}

	public ImportPanelGRID(List<ImportFileInfo> fileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		initComponents();
	}

	public ImportPanelGRID(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
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
		labelSaveImport.setText(DataConversionProperties
				.getString("string_label_lblSaveImport"));
		labelPassword.setText(DataConversionProperties
				.getString("string_label_lblPassword"));
		labelNewLabel.setText(DataConversionProperties
				.getString("string_label_lblFile"));
		labelDatasource.setText(DataConversionProperties
				.getString("string_label_lblDatasource"));
		labelCodingType.setText(DataConversionProperties
				.getString("string_label_lblCodingtype"));
		labelDatasetType.setText(DataConversionProperties
				.getString("string_label_lblDatasetType"));
		buttonProperty.setText(DataConversionProperties
				.getString("string_button_property"));
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

		comboBoxDataType = new DatasetComboBox(new String[] { DataConversionProperties.getString("string_comboboxitem_image"),
				DataConversionProperties.getString("string_comboboxitem_grid") });

		comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(
				new String[] {
						DataConversionProperties
								.getString("string_comboboxitem_nullcoding"),
						"DCT", "SGL", "PNG", "LZW" }));
		comboBoxCodingType.setSelectedIndex(1);
	}

	private void initComponents() {

		panel = new JPanel();
		labelDatasource = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		labelDatasetType = new JLabel();
		labelCodingType = new JLabel();
		comboBoxCodingType = new JComboBox<Object>();
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		labelSaveImport = new JLabel();
		comboBoxImportType = new JComboBox<Object>();
		labelPassword = new JLabel();
		textFieldPassword = new JTextField();
		labelNewLabel = new JLabel();
		panelDatapath = new JPanel();
		labelFilePath = new JLabel();
		labelCharset = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		comboBoxCharset = new CharsetComboBox();
		fileChooser = new FileChooserControl();
		fileChooser.getEditor().setEnabled(false);

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();
		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, null, comboBoxDatasource);
		// 设置字符集类型
		comboBoxDataType.addActionListener(new LocalActionListener());
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		setPreferredSize(new java.awt.Dimension(483, 300));

		comboBoxCodingType.setSelectedIndex(1);
		// @formatter:off
        GroupLayout panelLayout = new GroupLayout(panel);
        panelLayout.setHorizontalGroup(
        	panelLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelLayout.createSequentialGroup()
        			.addGroup(panelLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelLayout.createSequentialGroup()
        					.addComponent(labelDatasource, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        					.addGap(46)
        					.addComponent(labelDatasetType, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        					.addGap(10)
        					.addComponent(comboBoxDataType, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        				.addGroup(panelLayout.createSequentialGroup()
        					.addComponent(labelCodingType, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(45, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
        	panelLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelLayout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(panelLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelLayout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addComponent(labelDatasetType)
        					.addComponent(comboBoxDataType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addComponent(labelDatasource))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(panelLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(labelCodingType)
        				.addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );
        panel.setLayout(panelLayout);

        comboBoxImportType.setEnabled(false);

        textFieldPassword.setEnabled(false);

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransform.setLayout(panelTransformLayout);
        panelTransformLayout.setHorizontalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelTransformLayout.createSequentialGroup()
                        .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPassword, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(panelTransformLayout.createSequentialGroup()
                                .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(labelSaveImport)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboBoxImportType, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                            .addComponent(textFieldPassword, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelTransformLayout.createSequentialGroup()
                        .addComponent(labelNewLabel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGap(6)
                        .addComponent(fileChooser)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelImportModel)
                    .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSaveImport)
                    .addComponent(comboBoxImportType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPassword)
                    .addComponent(textFieldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(labelNewLabel)
                    .addComponent(fileChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );

        GroupLayout panelDatapathLayout = new GroupLayout(panelDatapath);
        panelDatapathLayout.setHorizontalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(labelFilePath, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        				.addComponent(labelCharset))
        			.addGap(12)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(Alignment.LEADING, panelDatapathLayout.createSequentialGroup()
        					.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
        				.addComponent(comboBoxCharset, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(46, Short.MAX_VALUE))
        );
        panelDatapathLayout.setVerticalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelFilePath)
        				.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(buttonProperty))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelCharset)
        				.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
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
                .addContainerGap())
        );
     // @formatter:on
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String dataType = comboBoxDataType.getSelectItem();
			int select = comboBoxDataType.getSelectedIndex();
			if (null != fileInfos && !fileInfos.isEmpty()) {
				for (int i = 0; i < panels.size(); i++) {
					ImportSetting tempSetting = fileInfos.get(i).getImportSetting();
					JPanel tempPanel = panels.get(i);
					if (dataType.equals(DataConversionProperties.getString("string_comboboxitem_image"))) {
						if (tempSetting instanceof ImportSettingTIF) {
							((ImportSettingTIF) tempSetting).setImportingAsGrid(false);
							((ImportPanelTIF) tempPanel).getComboBoxDataType().setSelectedIndex(select);
						} else {
							((ImportPanelPI) tempPanel).getComboBoxDatasetType().setSelectedIndex(select);
							if (tempSetting instanceof ImportSettingBMP) {
								((ImportSettingBMP) tempSetting).setImportingAsGrid(false);
							}
							if (tempSetting instanceof ImportSettingIMG) {
								((ImportSettingIMG) tempSetting).setImportingAsGrid(false);
							}
							if (tempSetting instanceof ImportSettingPNG) {
								((ImportSettingPNG) tempSetting).setImportingAsGrid(false);
							}
							if (tempSetting instanceof ImportSettingJPG) {
								((ImportSettingJPG) tempSetting).setImportingAsGrid(false);
							}
							if (tempSetting instanceof ImportSettingGIF) {
								((ImportSettingGIF) tempSetting).setImportingAsGrid(false);

							}
						}
					} else {
						if (tempSetting instanceof ImportSettingTIF) {
							((ImportSettingTIF) tempSetting).setImportingAsGrid(true);
							((ImportPanelTIF) tempPanel).getComboBoxDataType().setSelectedIndex(select);
						} else {
							((ImportPanelPI) tempPanel).getComboBoxDatasetType().setSelectedIndex(select);
							if (tempSetting instanceof ImportSettingBMP) {
								((ImportSettingBMP) tempSetting).setImportingAsGrid(true);
							}
							if (tempSetting instanceof ImportSettingIMG) {
								((ImportSettingIMG) tempSetting).setImportingAsGrid(true);
							}
							if (tempSetting instanceof ImportSettingPNG) {
								((ImportSettingPNG) tempSetting).setImportingAsGrid(true);
							}
							if (tempSetting instanceof ImportSettingJPG) {
								((ImportSettingJPG) tempSetting).setImportingAsGrid(true);
							}
							if (tempSetting instanceof ImportSettingGIF) {
								((ImportSettingGIF) tempSetting).setImportingAsGrid(true);

							}
						}
					}

				}
			}
		}

	}
}
