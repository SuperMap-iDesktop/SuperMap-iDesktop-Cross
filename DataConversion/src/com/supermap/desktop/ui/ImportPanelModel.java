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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.supermap.data.Datasource;
import com.supermap.data.Point3D;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingModel3DS;
import com.supermap.data.conversion.ImportSettingModelDXF;
import com.supermap.data.conversion.ImportSettingModelFLT;
import com.supermap.data.conversion.ImportSettingModelOSG;
import com.supermap.data.conversion.ImportSettingModelX;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.action.VoteElectKeyListener;
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
 * @author Administrator 实现右侧导入Model(3ds,osgb,.x,flt)数据类型的界面
 */
public class ImportPanelModel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
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
	private JPanel panel;
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

	public ImportPanelModel() {
		initComponents();
	}

	public ImportPanelModel(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelModel(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void initResource() {
		labelLongitude.setText(DataConversionProperties.getString("string_label_lblx"));
		labelLatitude.setText(DataConversionProperties.getString("string_label_lbly"));
		labelElevation.setText(DataConversionProperties.getString("string_label_lblz"));
		labelCharset.setText(DataConversionProperties
				.getString("string_label_lblCharset"));
		labelFilePath.setText(DataConversionProperties
				.getString("string_label_lblDataPath"));
		labelImportModel.setText(DataConversionProperties
				.getString("string_label_lblImportType"));
		labelDatasource.setText(DataConversionProperties
				.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties
				.getString("string_label_lblDataset"));
		labelDatasetType.setText(DataConversionProperties
				.getString("string_label_lblDatasetType"));
		buttonProperty.setText(DataConversionProperties
				.getString("string_button_property"));
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
		panelAnchor.setBorder(new TitledBorder(null, DataConversionProperties
				.getString("string_border_panelAnchor"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelDatapath.setBorder(new TitledBorder(null, DataConversionProperties
				.getString("string_border_panelDatapath"),
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBoxImportModel.setModel(new DefaultComboBoxModel<Object>(
				new String[] {
						DataConversionProperties
								.getString("string_comboboxitem_null"),
						DataConversionProperties
								.getString("string_comboboxitem_add"),
						DataConversionProperties
								.getString("string_comboboxitem_cover") }));
		comboBoxCharset.setModel(new CommonComboBoxModel());
		comboBoxCharset.setAutoscrolls(true);
		comboBoxDataType = new DatasetComboBox(new String[] { DataConversionProperties.getString("string_comboboxitem_composite"),
				DataConversionProperties.getString("string_comboboxitem_model") });
		comboBoxDataType.setSelectedIndex(1);
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

	private void initComponents() {

		panel = new JPanel();
		labelDatasource = new JLabel();
		comboBoxDatasource = new DatasourceComboBox();
		labelDataset = new JLabel();
		textFieldResultSet = new JTextField();
		textFieldResultSet.setColumns(10);
		labelDatasetType = new JLabel();
		panelTransform = new JPanel();
		labelImportModel = new JLabel();
		comboBoxImportModel = new JComboBox<Object>();
		panelAnchor = new JPanel();
		labelLongitude = new JLabel();
		textFieldLongitude = new JTextField();
		labelLatitude = new JLabel();
		textFieldLatitude = new JTextField();
		labelElevation = new JLabel();
		textFieldElevation = new JTextField();
		panelDatapath = new JPanel();
		labelFilePath = new JLabel();
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		buttonProperty = new JButton();
		labelCharset = new JLabel();
		comboBoxCharset = new CharsetComboBox();

		Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
		comboBoxDatasource.setSelectedDatasource(datasource);
		initResource();

		// 设置目标数据源
		ImportInfoUtil.setDataSource(panels, fileInfos, fileInfo, comboBoxDatasource);

		// 设置fileInfo
		importsetting = ImportInfoUtil.setFileInfo(datasource, fileInfos, fileInfo,
				textFieldFilePath, importsetting, textFieldResultSet);
		// 设置目标数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置数据集类型
		comboBoxDataType.addActionListener(new ActionListener() {
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
		});
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		textFieldLongitude.setText("0");
		textFieldLatitude.setText("0");
		textFieldElevation.setText("0");
		// 设置文本内框部只能输入数字
		textFieldLongitude.addKeyListener(new VoteElectKeyListener());
		textFieldLatitude.addKeyListener(new VoteElectKeyListener());
		textFieldElevation.addKeyListener(new VoteElectKeyListener());
		// 设置模型定位点
		textFieldLongitude.getDocument().addDocumentListener(
				new CommonDocumnetListener(textFieldLongitude, textFieldLatitude, textFieldElevation));
		textFieldLatitude.getDocument().addDocumentListener(
				new CommonDocumnetListener(textFieldLongitude, textFieldLatitude, textFieldElevation));
		textFieldElevation.getDocument().addDocumentListener(
				new CommonDocumnetListener(textFieldLongitude, textFieldLatitude, textFieldElevation));
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
                    .addComponent(labelDatasetType, GroupLayout.Alignment.LEADING, 72, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDatasource, GroupLayout.Alignment.LEADING, 72, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboBoxDatasource, 0, 104, GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxDataType, 0, 104, GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDatasource)
                    .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDataset)
                    .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDatasetType)
                    .addComponent(comboBoxDataType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransform.setLayout(panelTransformLayout);
        panelTransformLayout.setHorizontalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
            panelTransformLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(labelImportModel)
                .addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

      
        

        GroupLayout panelAnchorLayout = new GroupLayout(panelAnchor);
        panelAnchor.setLayout(panelAnchorLayout);
        panelAnchorLayout.setHorizontalGroup(
            panelAnchorLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelAnchorLayout.createSequentialGroup()
                .addGroup(panelAnchorLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelElevation, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelLongitude, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelAnchorLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(textFieldLongitude)
                    .addComponent(textFieldElevation, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                .addGap(46, 46, 46)
                .addComponent(labelLatitude, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(textFieldLatitude, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelAnchorLayout.setVerticalGroup(
            panelAnchorLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelAnchorLayout.createSequentialGroup()
                .addGroup(panelAnchorLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelLongitude)
                    .addComponent(textFieldLongitude, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelLatitude)
                    .addComponent(textFieldLatitude, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAnchorLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelElevation)
                    .addComponent(textFieldElevation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );

        GroupLayout panelDatapathLayout = new GroupLayout(panelDatapath);
        panelDatapathLayout.setHorizontalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(labelFilePath)
        				.addComponent(labelCharset))
        			.addGap(12)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panelDatapathLayout.createSequentialGroup()
        					.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
        				.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(38, Short.MAX_VALUE))
        );
        panelDatapathLayout.setVerticalGroup(
        	panelDatapathLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelDatapathLayout.createSequentialGroup()
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelFilePath)
        				.addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(buttonProperty))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(panelDatapathLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelCharset)
        				.addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelDatapath.setLayout(panelDatapathLayout);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelAnchor, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelDatapath, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTransform, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAnchor, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDatapath, GroupLayout.PREFERRED_SIZE, 79, Short.MAX_VALUE))
        );
     // @formatter:on
	}

	public JComboBox<Object> getComboBox() {
		return comboBoxImportModel;
	}

	public void setComboBox(JComboBox<Object> comboBox) {
		this.comboBoxImportModel = comboBox;
	}

	public DatasetComboBox getComboBoxDataType() {
		return comboBoxDataType;
	}

	public void setComboBoxDataType(DatasetComboBox comboBoxDataType) {
		this.comboBoxDataType = comboBoxDataType;
	}

	public CharsetComboBox getComboBoxCharset() {
		return comboBoxCharset;
	}

	public void setComboBoxCharset(CharsetComboBox comboBoxCharset) {
		this.comboBoxCharset = comboBoxCharset;
	}

	public JTextField getTextFieldx() {
		return textFieldLongitude;
	}

	public void setTextFieldx(JTextField textFieldx) {
		this.textFieldLongitude = textFieldx;
	}

	public JTextField getTextFieldy() {
		return textFieldLatitude;
	}

	public void setTextFieldy(JTextField textFieldy) {
		this.textFieldLatitude = textFieldy;
	}

	public JTextField getTextFieldz() {
		return textFieldElevation;
	}

	public void setTextFieldz(JTextField textFieldz) {
		this.textFieldElevation = textFieldz;
	}

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
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
}
