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
import com.supermap.data.conversion.ImportSettingBMP;
import com.supermap.data.conversion.ImportSettingGIF;
import com.supermap.data.conversion.ImportSettingJPG;
import com.supermap.data.conversion.ImportSettingPNG;
import com.supermap.desktop.Application;
import com.supermap.desktop.FileChooserControl;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.util.CommonComboBoxModel;
import com.supermap.desktop.util.ImportInfoUtil;

/**
 *
 * @author Administrator 实现右侧导入 BMP,JPG,PNG,GIF共用面板
 */
public class ImportPanelPI extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasetComboBox comboBoxDatasetType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelCodingtype;
	private JLabel labelFilePath;
	private JLabel labelDataset;
	private JLabel labelDatasetType;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JLabel labelFileOfCoordinates;
	private JPanel panel;
	private JPanel panelFilepath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient FileChooserControl fileChooserc;
	private transient ImportSetting importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelPI() {
		initComponents();
	}

	public ImportPanelPI(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelPI(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void initResource() {
		labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		labelFileOfCoordinates.setText(DataConversionProperties.getString("string_label_lblFile"));
		labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		labelCodingtype.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
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
		panelFilepath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		comboBoxCharset.setModel(new CommonComboBoxModel());
		comboBoxCharset.setAutoscrolls(true);
		comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover") }));
		comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[] { DataConversionProperties.getString("string_comboboxitem_nullcoding"),
				"DCT", "PNG", "LZW" }));
		comboBoxCodingType.setSelectedIndex(1);

		comboBoxDatasetType = new DatasetComboBox(new String[] { DataConversionProperties.getString("string_comboboxitem_image"),
				DataConversionProperties.getString("string_comboboxitem_grid") });
	}

	private void setWorldFilePath(ImportSetting tempsetting, String worldFile) {
		if (tempsetting instanceof ImportSettingBMP) {
			((ImportSettingBMP) tempsetting).setWorldFilePath(worldFile);
		}
		if (tempsetting instanceof ImportSettingJPG) {
			((ImportSettingJPG) tempsetting).setWorldFilePath(worldFile);
		}
		if (tempsetting instanceof ImportSettingPNG) {
			((ImportSettingPNG) tempsetting).setWorldFilePath(worldFile);
		}
		if (tempsetting instanceof ImportSettingGIF) {
			((ImportSettingGIF) tempsetting).setWorldFilePath(worldFile);
		}
	};

	private void setDatasetType(String dataType, ImportSetting tempSetting) {
		if (dataType.equals(DataConversionProperties.getString("string_comboboxitem_grid"))) {
			if (tempSetting instanceof ImportSettingBMP) {
				((ImportSettingBMP) tempSetting).setImportingAsGrid(true);
			}
			if (tempSetting instanceof ImportSettingJPG) {
				((ImportSettingJPG) tempSetting).setImportingAsGrid(true);
			}
			if (tempSetting instanceof ImportSettingPNG) {
				((ImportSettingPNG) tempSetting).setImportingAsGrid(true);
			}
			if (tempSetting instanceof ImportSettingGIF) {
				((ImportSettingGIF) tempSetting).setImportingAsGrid(true);
			}
		} else {
			if (tempSetting instanceof ImportSettingBMP) {
				((ImportSettingBMP) tempSetting).setImportingAsGrid(false);
			}
			if (tempSetting instanceof ImportSettingJPG) {
				((ImportSettingJPG) tempSetting).setImportingAsGrid(false);
			}
			if (tempSetting instanceof ImportSettingPNG) {
				((ImportSettingPNG) tempSetting).setImportingAsGrid(false);
			}
			if (tempSetting instanceof ImportSettingGIF) {
				((ImportSettingGIF) tempSetting).setImportingAsGrid(false);
			}
		}
	}

	private void initComponents() {

		panel = new JPanel();
		labelDatasource = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		labelDataset = new JLabel();
		textFieldResultSet = new JTextField(10);
		labelCodingtype = new JLabel();
		comboBoxCodingType = new JComboBox<Object>();
		labelDatasetType = new JLabel();
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		labelFileOfCoordinates = new JLabel();
		panelFilepath = new JPanel();
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
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置数据集类型
		comboBoxDatasetType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dataType = comboBoxDatasetType.getSelectItem();
				int selectIndex = comboBoxDatasetType.getSelectedIndex();
				if (null != importsetting) {
					setDatasetType(dataType, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelPI tempPanel = (ImportPanelPI) panels.get(i);
						tempPanel.getComboBoxDatasetType().setSelectedIndex(selectIndex);
					}
				}
			}
		});
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);

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
                    .addComponent(labelCodingtype, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(textFieldResultSet)
                    .addComponent(comboBoxDatasetType, 0, 104, Short.MAX_VALUE))
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
                    .addComponent(labelCodingtype)
                    .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelDatasetType)
                        .addComponent(comboBoxDatasetType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
        );

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransform.setLayout(panelTransformLayout);
        panelTransformLayout.setHorizontalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelFileOfCoordinates, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelImportModel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileChooserc, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE))
                .addGap(0, 39, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelImportModel)
                    .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelFileOfCoordinates)
                    .addComponent(fileChooserc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );

        GroupLayout panelFilepathLayout = new GroupLayout(panelFilepath);
        panelFilepath.setLayout(panelFilepathLayout);
        panelFilepathLayout.setHorizontalGroup(
            panelFilepathLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelFilepathLayout.createSequentialGroup()
                .addGroup(panelFilepathLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelCharset, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelFilePath, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFilepathLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelFilepathLayout.createSequentialGroup()
                        .addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
                    .addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelFilepathLayout.setVerticalGroup(
            panelFilepathLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelFilepathLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFilepathLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelFilePath)
                    .addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonProperty))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFilepathLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(labelCharset)
                    .addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelFilepath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFilepath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 2, Short.MAX_VALUE))
        );
     // @formatter:on
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!SmFileChoose.isModuleExist("ImportPI")) {
				String fileFilters = SmFileChoose.createFileFilter(DataConversionProperties.getString("string_filetype_tfw"), "tfw");
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						DataConversionProperties.getString("String_Import"), "ImportPI", "OpenMany");
			}
			SmFileChoose fileChooser = new SmFileChoose("ImportPI");

			int state = fileChooser.showDefaultDialog();
			File file = fileChooser.getSelectedFile();
			if (state == JFileChooser.APPROVE_OPTION && null != file) {
				fileChooserc.getEditor().setText(file.getAbsolutePath());
				// 设置坐标参考文件
				String worldFile = fileChooserc.getEditor().getText();

				if (ImportInfoUtil.isExtendsFile(worldFile)) {
					if (null != importsetting) {
						setWorldFilePath(importsetting, worldFile);
					} else {
						for (int i = 0; i < panels.size(); i++) {
							((ImportPanelPI) panels.get(i)).getFileChooserc().getEditor().setText(worldFile);
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

	public DatasetComboBox getComboBoxDatasetType() {
		return comboBoxDatasetType;
	}

	public void setComboBoxDatasetType(DatasetComboBox comboBoxDatasetType) {
		this.comboBoxDatasetType = comboBoxDatasetType;
	}

	public FileChooserControl getFileChooserc() {
		return fileChooserc;
	}

	public void setFileChooserc(FileChooserControl fileChooserc) {
		this.fileChooserc = fileChooserc;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

}
