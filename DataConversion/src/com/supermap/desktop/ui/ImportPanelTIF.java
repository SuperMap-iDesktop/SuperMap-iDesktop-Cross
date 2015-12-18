package com.supermap.desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.TitledBorder;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingMrSID;
import com.supermap.data.conversion.ImportSettingTIF;
import com.supermap.data.conversion.MultiBandImportMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.FileChooserControl;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.util.CommonComboBoxModel;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Administrator 实现右侧导入tif,MrSID数据类型的界面
 */
public class ImportPanelTIF extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasetComboBox comboBoxDataType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JComboBox<Object> comboBoxSaveImport;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelFilePath;
	private JLabel labelDataset;
	private JLabel labelDatasetType;
	private JLabel labelDatasource;
	private JLabel labelFileOfCoordinates;
	private JLabel labelImportModel;
	private JLabel labelSaveImport;
	private JPanel panel;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient FileChooserControl fileChooserc;
	private transient ImportSetting importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelTIF() {
		initComponents();
	}

	public ImportPanelTIF(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelTIF(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void setImportsave(int save, ImportSetting tempsetting) {
		switch (save) {
		case 0:
			setSingleBand(tempsetting);
			break;
		case 1:
			setMultiBand(tempsetting);
			break;
		default:
			Application.getActiveApplication().getOutput().output(DataConversionProperties.getString("string_exception_importmode"));
			break;
		}
	}

	private void setSingleBand(ImportSetting tempsetting) {
		if (tempsetting instanceof ImportSettingTIF) {
			((ImportSettingTIF) tempsetting).setMultiBandImportMode(MultiBandImportMode.SINGLEBAND);
		}
		if (tempsetting instanceof ImportSettingMrSID) {
			((ImportSettingMrSID) tempsetting).setMultiBandImportMode(MultiBandImportMode.SINGLEBAND);
		}
	}

	private void setMultiBand(ImportSetting tempsetting) {
		if (tempsetting instanceof ImportSettingTIF) {
			((ImportSettingTIF) tempsetting).setMultiBandImportMode(MultiBandImportMode.MULTIBAND);
		}
		if (tempsetting instanceof ImportSettingMrSID) {
			((ImportSettingMrSID) tempsetting).setMultiBandImportMode(MultiBandImportMode.MULTIBAND);
		}
	}

	private void initResource() {
		labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		labelFileOfCoordinates.setText(DataConversionProperties.getString("string_label_lblFile"));
		labelSaveImport.setText(DataConversionProperties.getString("string_label_lblSaveImport"));
		labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		labelDatasetType.setText(DataConversionProperties.getString("string_label_lblDatasetType"));
		buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		buttonProperty.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		});
		panel.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		comboBoxCharset.setModel(new CommonComboBoxModel());
		comboBoxCharset.setAutoscrolls(true);
		comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
		comboBoxSaveImport.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_mssave"),
				DataConversionProperties.getString("string_comboboxitem_msave") }));
		comboBoxDataType = new DatasetComboBox(new String[] { DataConversionProperties.getString("string_comboboxitem_image"),
				DataConversionProperties.getString("string_comboboxitem_grid") });
		comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_nullcoding"),
				"DCT", "PNG", "LZW" }));
		comboBoxCodingType.setSelectedIndex(1);
	}

	private void setDatasetType(String dataType, ImportSetting importsetting) {
		if (dataType.equals(DataConversionProperties.getString("string_comboboxitem_image"))) {
			if (importsetting instanceof ImportSettingTIF) {
				((ImportSettingTIF) importsetting).setImportingAsGrid(false);
			}
			if (importsetting instanceof ImportSettingMrSID) {
				((ImportSettingMrSID) importsetting).setImportingAsGrid(false);
			}
		} else {
			if (importsetting instanceof ImportSettingTIF) {
				((ImportSettingTIF) importsetting).setImportingAsGrid(true);
			}
			if (importsetting instanceof ImportSettingMrSID) {
				((ImportSettingMrSID) importsetting).setImportingAsGrid(true);
			}
		}
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
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		labelSaveImport = new JLabel();
		comboBoxSaveImport = new JComboBox<Object>();
		labelFileOfCoordinates = new JLabel();
		panelDatapath = new JPanel();
		labelFilePath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		labelCharset = new JLabel();
		comboBoxCharset = new CharsetComboBox();
		fileChooserc = new FileChooserControl();

		fileChooserc.getButton().addActionListener(new LocalActionListener());

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置fileInfo
		importsetting = ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting, textFieldResultSet);
		if (importsetting instanceof ImportSettingMrSID) {
			this.fileChooserc.setEnabled(false);
		}
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置数据集类型
		comboBoxDataType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dataType = comboBoxDataType.getSelectItem();
				int selected = comboBoxDataType.getSelectedIndex();
				if (null != importsetting) {
					setDatasetType(dataType, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						((ImportPanelTIF) panels.get(i)).getComboBoxDataType().setSelectedIndex(selected);
					}
				}

			}
		});
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置波段导入模式
		comboBoxSaveImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int save = comboBoxSaveImport.getSelectedIndex();
				if (null != importsetting) {
					setImportsave(save, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						((ImportPanelTIF) panels.get(i)).getComboBoxImport().setSelectedIndex(save);
					}
				}

			}
		});
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		setPreferredSize(new java.awt.Dimension(483, 300));
		// @formatter:off
        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelCodingType, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelDatasource, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboBoxDatasource, 0, 104, Short.MAX_VALUE)
                    .addComponent(comboBoxCodingType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelDatasetType, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(textFieldResultSet, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(comboBoxDataType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(labelCodingType)
                    .addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDatasetType)
                    .addComponent(comboBoxDataType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransformLayout.setHorizontalGroup(
        	panelTransformLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelTransformLayout.createSequentialGroup()
        			.addGroup(panelTransformLayout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(panelTransformLayout.createSequentialGroup()
        					.addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        					.addGap(48)
        					.addComponent(labelSaveImport)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(comboBoxSaveImport, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        				.addGroup(panelTransformLayout.createSequentialGroup()
        					.addComponent(labelFileOfCoordinates)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(fileChooserc, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)))
        			.addGap(51, 51, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
        	panelTransformLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelTransformLayout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(panelTransformLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelImportModel)
        				.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(labelSaveImport)
        				.addComponent(comboBoxSaveImport, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(panelTransformLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(labelFileOfCoordinates)
        				.addComponent(fileChooserc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );
        panelTransform.setLayout(panelTransformLayout);

        GroupLayout panelDatapathLayout = new GroupLayout(panelDatapath);
        panelDatapathLayout.setHorizontalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.TRAILING, false)
        				.addComponent(labelCharset, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(labelFilePath, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
        				.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(56, Short.MAX_VALUE))
        );
        panelDatapathLayout.setVerticalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addComponent(buttonProperty))
        				.addComponent(labelFilePath))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelCharset)
        				.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(28, Short.MAX_VALUE))
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

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!SmFileChoose.isModuleExist("ImportTIF")) {
				String fileFilters = SmFileChoose.createFileFilter(DataConversionProperties.getString("string_filetype_tfw"), "tfw");
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						DataConversionProperties.getString("String_Import"), "ImportTIF", "OpenMany");
			}
			SmFileChoose fileChooser = new SmFileChoose("ImportTIF");

			int state = fileChooser.showDefaultDialog();
			File file = fileChooser.getSelectedFile();
			if (state == JFileChooser.APPROVE_OPTION && null != file) {
				fileChooserc.getEditor().setText(file.getAbsolutePath());
				// 设置坐标参考文件
				String worldFile = fileChooserc.getEditor().getText();

				if (ImportInfoUtil.isExtendsFile(worldFile)) {
					if (null != importsetting) {
						if (importsetting instanceof ImportSettingTIF) {
							((ImportSettingTIF) importsetting).setWorldFilePath(worldFile);
						}
					} else {
						for (int i = 0; i < panels.size(); i++) {
							((ImportPanelTIF) panels.get(i)).getFileChooserc().getEditor().setText(worldFile);
						}
					}
				}
			}
		}

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

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

	public JComboBox<Object> getComboBoxImport() {
		return comboBoxSaveImport;
	}

	public void setComboBoxImport(JComboBox<Object> comboBoxImport) {
		this.comboBoxSaveImport = comboBoxImport;
	}

	public FileChooserControl getFileChooserc() {
		return fileChooserc;
	}

	public void setFileChooserc(FileChooserControl fileChooserc) {
		this.fileChooserc = fileChooserc;
	}

}
