package com.supermap.desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.TitledBorder;

import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportSettingIMG;
import com.supermap.data.conversion.MultiBandImportMode;
import com.supermap.desktop.Application;
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
 * @author Administrator 实现右侧导入image数据类型的界面
 */
public class ImportPanelIMG extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton buttonProperty;
	private JComboBox<Object> comboBoxImportModel;
	private transient CharsetComboBox comboBoxCharset;
	private JComboBox<Object> comboBoxCodingType;
	private transient DatasourceComboBox comboBoxDatasource;
	private transient DatasetComboBox comboBoxDatatype;
	private JComboBox<Object> comboBoxImportSave;
	private JLabel labelCharset;
	private JLabel labelCodingType;
	private JLabel labelFilePath;
	private JLabel labelDataset;
	private JLabel labelDatasetType;
	private JLabel labelDatasource;
	private JLabel labelImportSave;
	private JLabel labelImportModel;
	private JPanel panel;
	private JPanel panelDatapath;
	private JPanel panelTransform;
	private JTextField textFieldFilePath;
	private JTextField textFieldResultSet;
	private transient ImportFileInfo fileInfo;
	private transient ImportSettingIMG importsetting = null;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private transient DataImportFrame dataImportFrame;

	public ImportPanelIMG() {
		initComponents();
	}

	public ImportPanelIMG(DataImportFrame dataImportFrame, ImportFileInfo fileInfo) {
		this.dataImportFrame = dataImportFrame;
		this.fileInfo = fileInfo;
		initComponents();
	}

	public ImportPanelIMG(List<ImportFileInfo> fileInfos, List<JPanel> panels) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		initComponents();
	}

	private void setImportsave(int save, ImportSettingIMG tempsetting) {
		switch (save) {
		case 0:
			tempsetting.setMultiBandImportMode(MultiBandImportMode.SINGLEBAND);
			break;
		case 1:
			tempsetting.setMultiBandImportMode(MultiBandImportMode.MULTIBAND);
			break;
		default:
			Application.getActiveApplication().getOutput().output(DataConversionProperties.getString("String_ImageGridParam_Failed"));
			break;
		}
	}

	private void setDataset(String dataType, ImportSettingIMG tempSetting) {
		if (dataType.equals(DataConversionProperties
				.getString("string_comboboxitem_image"))) {
			tempSetting.setImportingAsGrid(false);
		} else {
			tempSetting.setImportingAsGrid(true);
		}
	}

	private void initResource() {
		labelFilePath.setText(DataConversionProperties
				.getString("string_label_lblDataPath"));
		labelCharset.setText(DataConversionProperties
				.getString("string_label_lblCharset"));
		labelImportModel.setText(DataConversionProperties
				.getString("string_label_lblImportType"));
		labelImportSave.setText(DataConversionProperties
				.getString("string_label_lblSaveImport"));
		labelDatasource.setText(DataConversionProperties
				.getString("string_label_lblDatasource"));
		labelDataset.setText(DataConversionProperties
				.getString("string_label_lblDataset"));
		labelCodingType.setText(DataConversionProperties
				.getString("string_label_lblCodingtype"));
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
		comboBoxImportSave.setModel(new DefaultComboBoxModel<Object>(
				new String[] {
						DataConversionProperties
								.getString("string_comboboxitem_mssave"),
						DataConversionProperties
								.getString("string_comboboxitem_msave") }));
		comboBoxImportSave.setSelectedIndex(1);
		comboBoxDatatype = new DatasetComboBox(new String[] {
				DataConversionProperties.getString("string_comboboxitem_image"),
				DataConversionProperties.getString("string_comboboxitem_grid") });
		comboBoxCodingType.setModel(new DefaultComboBoxModel<Object>(
				new String[] {
						DataConversionProperties
								.getString("string_comboboxitem_nullcoding"),
						"DCT", "PNG", "LZW" }));
		comboBoxCodingType.setSelectedIndex(1);
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
		labelImportSave = new JLabel();
		comboBoxImportSave = new JComboBox<Object>();
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

		// 设置fileInfo信息
		importsetting = (ImportSettingIMG) ImportInfoUtil.setFileInfo(datasource,
				fileInfos, fileInfo, textFieldFilePath, importsetting,
				textFieldResultSet);
		// 设置数据集结果数据集名称
		ImportInfoUtil.setDatasetName(textFieldResultSet, importsetting);
		// 设置编码类型
		ImportInfoUtil.setCodingType(panels, importsetting, comboBoxCodingType);
		// 设置结果数据集类型
		comboBoxDatatype.addActionListener(new CommonActionLitener());
		// 设置导入模式
		ImportInfoUtil.setImportMode(panels, importsetting, comboBoxImportModel);
		// 设置波段导入模式
		comboBoxImportSave.addActionListener(new CommonActionLitener());
		// 设置字符集类型
		ImportInfoUtil.setCharset(panels, importsetting, comboBoxCharset);

		setPreferredSize(new java.awt.Dimension(483, 300));

		comboBoxCodingType.setSelectedIndex(1);
		// @formatter:off

        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelDatasource, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelCodingType, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxCodingType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(46, 46, 46)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldResultSet, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(labelDatasetType, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxDatatype, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 37, Short.MAX_VALUE))
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
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(labelCodingType)
                    .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(comboBoxCodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelDatasetType)
                        .addComponent(comboBoxDatatype, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
        );

        GroupLayout panelTransformLayout = new GroupLayout(panelTransform);
        panelTransformLayout.setHorizontalGroup(
        	panelTransformLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelTransformLayout.createSequentialGroup()
        			.addComponent(labelImportModel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        			.addGap(46)
        			.addComponent(labelImportSave)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(comboBoxImportSave, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(38, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
        	panelTransformLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panelTransformLayout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(panelTransformLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelImportModel)
        				.addComponent(comboBoxImportModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(labelImportSave)
        				.addComponent(comboBoxImportSave, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );
        panelTransform.setLayout(panelTransformLayout);

        GroupLayout panelDatapathLayout = new GroupLayout(panelDatapath);
        panelDatapath.setLayout(panelDatapathLayout);
        panelDatapathLayout.setHorizontalGroup(
            panelDatapathLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelDatapathLayout.createSequentialGroup()
                .addGroup(panelDatapathLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatapathLayout.createSequentialGroup()
                        .addComponent(labelFilePath, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonProperty, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDatapathLayout.createSequentialGroup()
                        .addComponent(labelCharset, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGap(6)
                        .addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelDatapathLayout.setVerticalGroup(
            panelDatapathLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelDatapathLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatapathLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelFilePath)
                    .addComponent(textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonProperty))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatapathLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCharset)
                    .addComponent(comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );

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
                .addComponent(panelDatapath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
     // @formatter:on
	}

	class CommonActionLitener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			if (c == comboBoxDatatype) {
				String dataType = comboBoxDatatype.getSelectItem();
				if (null != importsetting) {
					setDataset(dataType, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						((ImportPanelIMG) panels.get(i)).getComboBoxDatatype().setSelectedItem(dataType);
					}
				}
			} else if (c == comboBoxImportSave) {
				int save = comboBoxImportSave.getSelectedIndex();
				if (null != importsetting) {
					setImportsave(save, importsetting);
				} else {
					for (int i = 0; i < panels.size(); i++) {
						((ImportPanelIMG) panels.get(i)).getComboBoxImport()
								.setSelectedIndex(save);
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

	public DatasourceComboBox getComboBoxDatasource() {
		return comboBoxDatasource;
	}

	public void setComboBoxDatasource(DatasourceComboBox comboBoxDatasource) {
		this.comboBoxDatasource = comboBoxDatasource;
	}

	public DatasetComboBox getComboBoxDatatype() {
		return comboBoxDatatype;
	}

	public void setComboBoxDatatype(DatasetComboBox comboBoxDatatype) {
		this.comboBoxDatatype = comboBoxDatatype;
	}

	public JComboBox<Object> getComboBoxImport() {
		return comboBoxImportSave;
	}

	public void setComboBoxImport(JComboBox<Object> comboBoxImport) {
		this.comboBoxImportSave = comboBoxImport;
	}

}
