package com.supermap.desktop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.Point3D;
import com.supermap.data.conversion.*;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.action.VoteElectKeyListener;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.util.CommonComboBoxModel;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.ImportInfoUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 实现右侧导入Model(3ds,osgb,.x,flt)数据类型的界面
 */
public class ImportPanelModel extends AbstractImportPanel {

	private static final long serialVersionUID = 1L;
	private SmButton buttonProperty;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private transient DatasetComboBox comboBoxDataType;
	private transient DatasourceComboBox comboBoxDatasource;
	private JLabel labelCharset;
	private JLabel labelFilePath;
	private JLabel labelDataset;
	private JLabel labelDatasetType;
	private JLabel labelDatasource;
	private JLabel labelImportModel;
	private JLabel labelLongitude;
	private JLabel labelLatitude;
	private JLabel labelElevation;
	private JPanel panelResultSet;
	private JPanel panelAnchor;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private JTextField textFieldLongitude;
	private JTextField textFieldLatitude;
	private transient JTextField textFieldElevation;
	private transient ImportFileInfo fileInfo;
	private transient ImportSetting importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	private ActionListener comboBoxDataTypeAction;
	private VoteElectKeyListener electKeyListener = new VoteElectKeyListener();
	private CommonDocumnetListener documnetListener;
	private ActionListener buttonPropertyAction;

	public ImportPanelModel(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
		initResource();
		registActionListener();
	}

	public ImportPanelModel(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
		initResource();
		registActionListener();
	}

	@Override
	void initResource() {
		this.labelLongitude.setText(DataConversionProperties.getString("string_label_lblx"));
		this.labelLatitude.setText(DataConversionProperties.getString("string_label_lbly"));
		this.labelElevation.setText(DataConversionProperties.getString("string_label_lblz"));
		this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
		this.labelFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
		this.labelImportModel.setText(DataConversionProperties.getString("string_label_lblImportType"));
		this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
		this.labelDataset.setText(DataConversionProperties.getString("string_label_lblDataset"));
		this.labelDatasetType.setText(DataConversionProperties.getString("string_label_lblDatasetType"));
		this.buttonProperty.setText(DataConversionProperties.getString("string_button_property"));
		this.panelResultSet.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panel"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelTransform.setBorder(new TitledBorder(null,
				DataConversionProperties.getString("string_border_panelTransform"),
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panelAnchor.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelAnchor"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelDatapath"),
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(
				new String[]{
						DataConversionProperties.getString("string_comboboxitem_null"),
						DataConversionProperties.getString("string_comboboxitem_add"),
						DataConversionProperties.getString("string_comboboxitem_cover")}));
		this.comboBoxCharset.setModel(new CommonComboBoxModel());
		this.comboBoxCharset.setAutoscrolls(true);
	}

	private void setDatasetType(String dataType, ImportSetting importsetting) {
		if (dataType.equals(DataConversionProperties
				.getString("string_comboboxitem_composite"))) {
			if (importsetting instanceof ImportSettingModel3DS) {
				((ImportSettingModel3DS) importsetting).setImportingAsCAD(true);
			}
			if (importsetting instanceof ImportSettingModelX) {
				((ImportSettingModelX) importsetting).setImportingAsCAD(true);
			}
			if (importsetting instanceof ImportSettingModelOSG) {
				((ImportSettingModelOSG) importsetting).setImportingAsCAD(true);
			}
			if (importsetting instanceof ImportSettingModelFLT) {
				((ImportSettingModelFLT) importsetting).setImportingAsCAD(true);
			}
			if (importsetting instanceof ImportSettingModelDXF) {
				((ImportSettingModelDXF) importsetting).setImportingAsCAD(true);
			}
		} else {
			if (importsetting instanceof ImportSettingModel3DS) {
				((ImportSettingModel3DS) importsetting)
						.setImportingAsCAD(false);
			}
			if (importsetting instanceof ImportSettingModelX) {
				((ImportSettingModelX) importsetting).setImportingAsCAD(false);
			}
			if (importsetting instanceof ImportSettingModelOSG) {
				((ImportSettingModelOSG) importsetting).setImportingAsCAD(false);
			}
			if (importsetting instanceof ImportSettingModelFLT) {
				((ImportSettingModelFLT) importsetting).setImportingAsCAD(false);
			}
			if (importsetting instanceof ImportSettingModelDXF) {
				((ImportSettingModelDXF) importsetting).setImportingAsCAD(false);
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
		this.labelDatasetType = new JLabel();
		this.panelTransform = new JPanel();
		this.labelImportModel = new JLabel();
		this.comboBoxImportModel = new JComboBox<Object>();
		this.panelAnchor = new JPanel();
		this.labelLongitude = new JLabel();
		this.textFieldLongitude = new JTextField();
		this.labelLatitude = new JLabel();
		this.textFieldLatitude = new JTextField();
		this.labelElevation = new JLabel();
		this.textFieldElevation = new JTextField();
		this.panelDatapath = new JPanel();
		this.labelFilePath = new JLabel();
		this.textFieldFilePath = new JTextField();
		this.textFieldFilePath.setEditable(false);
		this.buttonProperty = new SmButton();
		this.labelCharset = new JLabel();
		this.comboBoxCharset = new CharsetComboBox();

		Datasource datasource = CommonFunction.getDatasource();
		this.comboBoxDatasource.setSelectedDatasource(datasource);

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);

		// 设置fileInfo
		this.importsetting = ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo,
				textFieldFilePath, importsetting, textFieldResultSet);
		// 设置目标数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);

		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		this.textFieldLongitude.setText("0");
		this.textFieldLatitude.setText("0");
		this.textFieldElevation.setText("0");
		// 设置源文件字符集
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		initPanelResultSet();

		initPanelTransform();

		initPanelAnchor();

		initPanelDatapath();

		initPanelModel();
	}

	private void initPanelModel() {
		//@formatter:off
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup().addComponent(this.panelResultSet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				   .addComponent(this.panelTransform,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				   .addComponent(this.panelAnchor,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				   .addComponent(this.panelDatapath,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(this.panelResultSet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				   .addContainerGap().addComponent(this.panelTransform, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				   .addContainerGap().addComponent(this.panelAnchor, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				   .addContainerGap().addComponent(this.panelDatapath, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
		this.setLayout(groupLayout);
		//@formatter:on		
	}

	private void initPanelDatapath() {
		//@formatter:off
		this.panelDatapath.setLayout(new GridBagLayout());
		this.panelDatapath.add(this.labelFilePath,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelDatapath.add(this.textFieldFilePath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(70, 1).setInsets(10, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.buttonProperty,    new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelDatapath.add(this.labelCharset,      new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelDatapath.add(this.comboBoxCharset,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 5).setIpad(20, 0));
		//@formatter:on
	}

	private void initPanelAnchor() {
		//@formatter:off
		this.panelAnchor.setLayout(new GridBagLayout());
		this.panelAnchor.add(this.labelLongitude,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelAnchor.add(this.textFieldLongitude, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelAnchor.add(this.labelLatitude,      new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelAnchor.add(this.textFieldLatitude,  new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 10,10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelAnchor.add(this.labelElevation,     new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelAnchor.add(this.textFieldElevation, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0,  0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on		
	}

	private void initPanelTransform() {
		//@formatter:off
		this.panelTransform.setLayout(new GridBagLayout());
		this.panelTransform.add(this.labelImportModel,          new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 10, 5));
		this.panelTransform.add(this.comboBoxImportModel,       new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(90, 1).setInsets(10, 0, 10, 20).setIpad(120, 0));
		//@formatter:on
	}

	private void initPanelResultSet() {
		//@formatter:off
		this.comboBoxDataType = new DatasetComboBox(new String[] { DataConversionProperties.getString("string_comboboxitem_composite"),
				DataConversionProperties.getString("string_comboboxitem_model") });
		this.comboBoxDataType.setSelectedIndex(1);
		this.panelResultSet.setLayout(new GridBagLayout());
		this.panelResultSet.add(this.labelDatasource,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 10, 5, 5));
		this.panelResultSet.add(this.comboBoxDatasource,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelDataset,         new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(10, 0, 5, 5));
		this.panelResultSet.add(this.textFieldResultSet,   new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(10, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.panelResultSet.add(this.labelDatasetType,     new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 5));
		this.panelResultSet.add(this.comboBoxDataType,     new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 0, 10, 20).setFill(GridBagConstraints.HORIZONTAL));
		// @formatter:on
	}

	public JComboBox<Object> getComboBox() {
		return comboBoxImportModel;
	}

	public DatasetComboBox getComboBoxDataType() {
		return comboBoxDataType;
	}

	public CharsetComboBox getComboBoxCharset() {
		return comboBoxCharset;
	}

	public JTextField getTextFieldx() {
		return textFieldLongitude;
	}

	public JTextField getTextFieldy() {
		return textFieldLatitude;
	}

	public JTextField getTextFieldz() {
		return textFieldElevation;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	class CommonDocumnetListener implements DocumentListener {

		private JTextField textFieldLongitude, textFieldLatitude, textFieldElevation;

		public CommonDocumnetListener(JTextField textFieldx,
		                              JTextField textFieldy, JTextField textFieldz) {
			this.textFieldLongitude = textFieldx;
			this.textFieldLatitude = textFieldy;
			this.textFieldElevation = textFieldz;
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			setPoint3D();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			setPoint3D();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setPoint3D();
		}

		private void setPoints(Point3D paramPoint3D, ImportSetting importsetting) {
			if (importsetting instanceof ImportSettingModel3DS) {
				((ImportSettingModel3DS) importsetting).setPosition(paramPoint3D);
			}
			if (importsetting instanceof ImportSettingModelFLT) {
				((ImportSettingModelFLT) importsetting).setPosition(paramPoint3D);
			}
			if (importsetting instanceof ImportSettingModelOSG) {
				((ImportSettingModelOSG) importsetting).setPosition(paramPoint3D);
			}
			if (importsetting instanceof ImportSettingModelX) {
				((ImportSettingModelX) importsetting).setPosition(paramPoint3D);
			}
			if (importsetting instanceof ImportSettingModelDXF) {
				((ImportSettingModelDXF) importsetting).setPosition(paramPoint3D);
			}
		}

		private void setPoint3D() {
			String textx = textFieldLongitude.getText();
			String texty = textFieldLatitude.getText();
			String textz = textFieldElevation.getText();
			if (!textx.isEmpty() && !texty.isEmpty() && !textz.isEmpty()) {
				Point3D paramPoint3D = new Point3D(Double.valueOf(textx),
						Double.valueOf(texty), Double.valueOf(textz));
				if (null != importsetting) {
					setPoints(paramPoint3D, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelModel tempPanel = (ImportPanelModel) panels
								.get(i);
						tempPanel.getTextFieldx().setText(textx);
						tempPanel.getTextFieldy().setText(texty);
						tempPanel.getTextFieldz().setText(textz);
					}
				}
			}
		}

	}

	@Override
	void registActionListener() {
		// 设置数据集类型
		this.comboBoxDataTypeAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dataType = comboBoxDataType.getSelectItem();
				int select = comboBoxDataType.getSelectedIndex();
				if (null != importsetting) {
					setDatasetType(dataType, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						ImportPanelModel tempPanel = (ImportPanelModel) panels
								.get(i);
						tempPanel.getComboBoxDataType().setSelectedIndex(select);
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
		this.documnetListener = new CommonDocumnetListener(this.textFieldLongitude, this.textFieldLatitude, this.textFieldElevation);
		unregistActionListener();
		this.comboBoxDataType.addActionListener(this.comboBoxDataTypeAction);
		// 设置文本内框部只能输入数字
		this.textFieldLongitude.addKeyListener(this.electKeyListener);
		this.textFieldLatitude.addKeyListener(this.electKeyListener);
		this.textFieldElevation.addKeyListener(this.electKeyListener);
		// 设置模型定位点
		this.textFieldLongitude.getDocument().addDocumentListener(this.documnetListener);
		this.textFieldLatitude.getDocument().addDocumentListener(this.documnetListener);
		this.textFieldElevation.getDocument().addDocumentListener(this.documnetListener);
		this.buttonProperty.addActionListener(this.buttonPropertyAction);
	}

	@Override
	void unregistActionListener() {
		this.comboBoxDataType.removeActionListener(this.comboBoxDataTypeAction);
		this.textFieldLongitude.removeKeyListener(this.electKeyListener);
		this.textFieldLatitude.removeKeyListener(this.electKeyListener);
		this.textFieldElevation.removeKeyListener(this.electKeyListener);
		this.textFieldLongitude.getDocument().removeDocumentListener(this.documnetListener);
		this.textFieldLatitude.getDocument().removeDocumentListener(this.documnetListener);
		this.textFieldElevation.getDocument().removeDocumentListener(this.documnetListener);
		this.buttonProperty.removeActionListener(this.buttonPropertyAction);
	}
}
