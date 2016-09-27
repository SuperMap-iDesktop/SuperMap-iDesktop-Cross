package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingMrSID;
import com.supermap.data.conversion.ImportSettingTIF;
import com.supermap.data.conversion.MultiBandImportMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 实现右侧导入tif,MrSID数据类型的界面
 */
public class ImportPanelTIF extends AbstractImportPanel {

	private static final long serialVersionUID = 1L;
	private SmButton buttonProperty;
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
	private JPanel panelResultSet;
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

	private LocalActionListener localActionListener;
	private ActionListener comboBoxDataTypeAction;
	private ActionListener comboBoxSaveImportAction;
	private ActionListener buttonPropertyAction;

	public ImportPanelTIF(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelTIF(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	private void setImportsave(int save, ImportSetting tempsetting) {
		switch (save) {
			case 0:
				setSingleBand(tempsetting);
				break;
			case 1:
				setMultiBand(tempsetting);
				break;
			case 2:
				setCompositeBand(tempsetting);
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

	private void setCompositeBand(ImportSetting tempsetting) {
		if (tempsetting instanceof ImportSettingTIF) {
			((ImportSettingTIF) tempsetting).setMultiBandImportMode(MultiBandImportMode.COMPOSITE);
		}
		if (tempsetting instanceof ImportSettingMrSID) {
			((ImportSettingMrSID) tempsetting).setMultiBandImportMode(MultiBandImportMode.COMPOSITE);
		}
	}

	@Override
	void initResource() {
		this.labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelFileOfCoordinates.setText(DataConversionProperties.getString("string_label_lblFile"));
		this.labelSaveImport.setText(DataConversionProperties.getString("string_label_lblSaveImport"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		this.labelCodingType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
		this.labelDatasetType.setText(DataConversionProperties.getString("string_label_lblDatasetType"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.panelResultSet.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		this.panelTransform.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelTransform"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.comboBoxCharset.setAutoscrolls(true);
		this.comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_null"),
				DataConversionProperties.getString("string_comboboxitem_add"), DataConversionProperties.getString("string_comboboxitem_cover")}));
		this.comboBoxSaveImport.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_singleBand"),
				DataConversionProperties.getString("string_multiBand"), DataConversionProperties.getString("string_compositeBand")}));
		this.comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_comboboxitem_nullcoding"),
				"DCT", "PNG", "LZW"}));
		this.comboBoxCodingType.setSelectedIndex(1);
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

	@Override
	void initComponents() {
		this.panelResultSet = new JPanel();
		this.labelDatasource = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.labelDataset = new JLabel();
		this.textFieldResultSet = new JTextField();
		this.textFieldResultSet.setColumns(10);
		this.labelCodingType = new JLabel();
		this.comboBoxCodingType = new JComboBox<Object>();
		this.labelDatasetType = new JLabel();
		this.panelTransform = new JPanel();
		this.labelImportModel = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.labelSaveImport = new JLabel();
		this.comboBoxSaveImport = new JComboBox<Object>();
		this.labelFileOfCoordinates = new JLabel();
		this.panelDatapath = new JPanel();
		this.labelFilePath = new JLabel();
		this.textFieldFilePath = new JTextField();
		this.textFieldFilePath.setEditable(false);
		this.buttonProperty = new SmButton();
		this.labelCharset = new JLabel();
		this.comboBoxCharset = new CharsetComboBox();
		this.fileChooserc = new FileChooserControl();

		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);
		// 设置fileInfo
		this.importsetting = ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo, textFieldFilePath, importsetting, textFieldResultSet);
		if (importsetting instanceof ImportSettingMrSID) {
			this.fileChooserc.setEnabled(false);
		}
		if (null != importsetting.getSourceFileCharset()) {
			comboBoxCharset.setSelectCharset(importsetting.getSourceFileCharset().name());
		}
		// 设置结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);

		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		initPanelResultSet();

		initPanelTransform();

		initPanelDatapath();

		initPanelTIF();
	}

	private void initPanelTIF() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup().addComponent(this.panelResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelTransform, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelDatapath, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(this.panelResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap().addComponent(this.panelTransform, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap().addComponent(this.panelDatapath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		this.setLayout(groupLayout);
	}

	private void initPanelDatapath() {
		this.panelDatapath.setLayout(new GridBagLayout());
		this.panelDatapath.add(this.labelFilePath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 5, 5));
		this.panelDatapath.add(this.textFieldFilePath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(10, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.buttonProperty, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.labelCharset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelDatapath.add(this.comboBoxCharset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 5).setIpad(20, 0));
	}

	private void initPanelTransform() {
		this.panelTransform.setLayout(new GridBagLayout());
		this.panelTransform.add(this.labelImportModel, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelTransform.add(this.comboBoxImportModel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.labelSaveImport, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelTransform.add(this.comboBoxSaveImport, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelTransform.add(this.labelFileOfCoordinates, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelTransform.add(this.fileChooserc, new GridBagConstraintsHelper(1, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(90, 1).setInsets(0, 0, 10, 10).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initPanelResultSet() {
		this.comboBoxDataType = new DatasetComboBox(new String[]{DataConversionProperties.getString("string_comboboxitem_image"),
				DataConversionProperties.getString("string_comboboxitem_grid")});
		this.panelResultSet.setLayout(new GridBagLayout());
		this.panelResultSet.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelResultSet.add(this.comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelDataset, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelResultSet.add(this.textFieldResultSet, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelCodingType, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelResultSet.add(this.comboBoxCodingType, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelDatasetType, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 0, 10, 5));
		this.panelResultSet.add(this.comboBoxDataType, new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 10).setFill(GridBagConstraints.HORIZONTAL));
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

	public CharsetComboBox getComboBoxCharset() {
		return comboBoxCharset;
	}

	public JComboBox<Object> getComboBoxCodingType() {
		return comboBoxCodingType;
	}

	public DatasetComboBox getComboBoxDataType() {
		return comboBoxDataType;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public JComboBox<Object> getComboBoxImport() {
		return comboBoxSaveImport;
	}

	public FileChooserControl getFileChooserc() {
		return fileChooserc;
	}

	@Override
	void registActionListener() {
		this.localActionListener = new LocalActionListener();
		// 设置数据集类型
		this.comboBoxDataTypeAction = new ActionListener() {
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
		};
		// 设置波段导入模式
		this.comboBoxSaveImportAction = new ActionListener() {
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
		};
		this.buttonPropertyAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FileProperty(dataImportFrame, fileInfo).setVisible(true);
			}
		};
		unregistActionListener();
		this.fileChooserc.getButton().addActionListener(this.localActionListener);
		this.comboBoxDataType.addActionListener(this.comboBoxDataTypeAction);
		this.comboBoxSaveImport.addActionListener(this.comboBoxSaveImportAction);
		this.buttonProperty.addActionListener(this.buttonPropertyAction);
	}

	@Override
	void unregistActionListener() {
		this.fileChooserc.getButton().removeActionListener(this.localActionListener);
		this.comboBoxDataType.removeActionListener(this.comboBoxDataTypeAction);
		this.comboBoxSaveImport.removeActionListener(this.comboBoxSaveImportAction);
		this.buttonProperty.removeActionListener(this.buttonPropertyAction);
	}

}
