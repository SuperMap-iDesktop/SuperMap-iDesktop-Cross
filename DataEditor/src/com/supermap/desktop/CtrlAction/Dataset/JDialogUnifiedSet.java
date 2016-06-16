package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Charset;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetGridCollection;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetImageCollection;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.EncodeType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.desktop.utilties.CharsetUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class JDialogUnifiedSet extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int COLUMN_INDEX_Dataset = 0;
	private static final int COLUMN_INDEX_CurrentDatasource = 1;

	private static final int DATASET_VECTOR = 1;
	private static final int DATASET_GRID = 2;
	private static final int DATASET_IMAGE = 3;

	private final JPanel contentPanel = new JPanel();
	private SmButton okButton = new SmButton("OK");
	private SmButton cancelButton = new SmButton("Cancel");
	private final JPanel panel = new JPanel();
	private JPanel buttonPane;
	private JCheckBox checkboxTargetDatasource = new JCheckBox("targetDatasource");
	private JCheckBox checkboxEncodeType = new JCheckBox("encodeType");
	private JCheckBox checkboxCharset = new JCheckBox("charset");
	private transient DatasourceComboBox comboBoxTargetDatasource;
	private JComboBox<String> comboBoxEncodeType = new JComboBox<String>();
	private JComboBox<String> comboBoxCharset = new JComboBox<String>();
	private transient MutiTable table;

	public JDialogUnifiedSet(JDialogDatasetCopy owner, Boolean flag, MutiTable table) {
		super(owner, flag);
		this.table = table;
		initComponents();
		initResources();
		setLocationRelativeTo(null);
	}

	public JDialogUnifiedSet() {
		initComponents();
		initResources();
	}

	private void initComponents() {

		initComboBox();
		setSize(350, 270);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okButton_clicked();
			}
		});

		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_clcked();
			}
		});

		// 主界面
		contentPanel.setLayout(new GridBagLayout());
		contentPanel.add(panel, new GridBagConstraintsHelper(0, 0).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(5));
		contentPanel.add(buttonPane, new GridBagConstraintsHelper(0, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setInsets(0, 0, 5, 5));

		comboBoxTargetDatasource.setEnabled(false);
		comboBoxEncodeType.setEnabled(false);
		comboBoxCharset.setEnabled(false);

		checkboxTargetDatasource.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				targetDatasource_changed();
			}
		});
		checkboxEncodeType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				encodeType_changed();
			}
		});
		checkboxCharset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				charset_changed();
			}
		});
		// 中间的设置面板
		// checkboxTargetDatasource comboBoxTargetDatasource
		// checkboxEncodeType comboBoxEncodeType
		// checkboxCharset comboBoxCharset

		panel.setLayout(new GridBagLayout());
		panel.add(checkboxTargetDatasource, new GridBagConstraintsHelper(0, 0).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5));
		panel.add(comboBoxTargetDatasource,
				new GridBagConstraintsHelper(1, 0).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.HORIZONTAL));

		panel.add(checkboxEncodeType, new GridBagConstraintsHelper(0, 1).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5));
		panel.add(comboBoxEncodeType,
				new GridBagConstraintsHelper(1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.HORIZONTAL));

		panel.add(checkboxCharset, new GridBagConstraintsHelper(0, 2).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5));
		panel.add(comboBoxCharset,
				new GridBagConstraintsHelper(1, 2).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.HORIZONTAL));

		// 设置不可拖动
		this.setResizable(false);
	}

	protected void cancelButton_clcked() {
		dispose();
	}

	protected void okButton_clicked() {
		int[] selectedRows = table.getSelectedRows();
		MutiTableModel tableModel = (MutiTableModel) table.getModel();
		List<Object> contents = tableModel.getContents();
		DataCell targetDatasource = (DataCell) comboBoxTargetDatasource.getSelectedItem();
		String encodeType = (String) comboBoxEncodeType.getSelectedItem();
		String charset = (String) comboBoxCharset.getSelectedItem();
		Object[][] datas = new Object[table.getRowCount()][6];
		for (int i = 0; i < table.getRowCount(); i++) {
			@SuppressWarnings("unchecked")
			Vector<Object> vector = (Vector<Object>) contents.get(i);
			datas[i][0] = vector.get(0);
			datas[i][1] = vector.get(1);
			datas[i][3] = vector.get(3);
			datas[i][2] = vector.get(2);
			datas[i][4] = vector.get(4);
			datas[i][5] = vector.get(5);
			// 用替换的方法实现统一设置
			for (int j = 0; j < selectedRows.length; j++) {
				int count = selectedRows[j];
				if (i == count) {
					Datasource tempDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(targetDatasource.toString());
					String tempDataset = tempDatasource.getDatasets().getAvailableDatasetName(vector.get(0).toString());
					if (checkboxTargetDatasource.isSelected()) {
						datas[count][2] = targetDatasource;
						datas[count][3] = tempDataset;
					}
					if (checkboxEncodeType.isSelected()) {
						datas[count][4] = encodeType;
					}
					if (checkboxCharset.isSelected()) {
						datas[count][5] = charset;
					}
				}
			}
		}
		try {
			tableModel.refreshContents(datas);
			table.updateUI();
			dispose();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	protected void charset_changed() {
		boolean isSelected = checkboxCharset.isSelected();
		if (isSelected) {
			comboBoxCharset.setEnabled(true);
		} else {
			comboBoxCharset.setEnabled(false);
		}
	}

	protected void encodeType_changed() {
		boolean isSelected = checkboxEncodeType.isSelected();
		if (isSelected) {
			comboBoxEncodeType.setEnabled(true);
		} else {
			comboBoxEncodeType.setEnabled(false);
		}
	}

	protected void targetDatasource_changed() {
		boolean isSelected = checkboxTargetDatasource.isSelected();
		if (isSelected) {
			comboBoxTargetDatasource.setEnabled(true);
		} else {
			comboBoxTargetDatasource.setEnabled(false);
		}
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		setTitle(DataEditorProperties.getString("String_GroupBox_ParameterSetting"));
		okButton.setText(CommonProperties.getString("String_Button_OK"));
		cancelButton.setText(CommonProperties.getString("String_Button_Cancel"));
		panel.setBorder(new TitledBorder(null, DataEditorProperties.getString("String_GroupBox_ParameterSetting"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		checkboxTargetDatasource.setText(CommonProperties.getString("String_ColumnHeader_TargetDatasource"));
		checkboxEncodeType.setText(CommonProperties.getString("String_ColumnHeader_EncodeType"));
		checkboxCharset.setText(DataEditorProperties.getString("String_Charset"));
	}

	/**
	 * 初始化panel中的JComboBox
	 */
	private void initComboBox() {
		// 目标数据源
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		comboBoxTargetDatasource = new DatasourceComboBox(datasources);
		// 编码类型
		ArrayList<String> encodeType = new ArrayList<String>();
		// 记录已添加的数据集的类型
		int datasetKind = -1;
		for (int i = 0; i < table.getRowCount(); i++) {
			String datasourceName = table.getValueAt(i, COLUMN_INDEX_CurrentDatasource).toString();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			String datasetName = table.getValueAt(i, COLUMN_INDEX_Dataset).toString();
			Dataset dataset = datasource.getDatasets().get(datasetName);

			if (dataset instanceof DatasetVector && (DatasetType.LINE == dataset.getType() || DatasetType.REGION == dataset.getType())) {
				if (datasetKind == -1) {
					datasetKind = DATASET_VECTOR;
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.BYTE));
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT16));
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT24));
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT32));
					continue;
				} else if (datasetKind != DATASET_VECTOR) {
					encodeType.clear();
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.NONE));
					break;
				}

			} else if (dataset instanceof DatasetGrid || dataset instanceof DatasetGridCollection) {
				if (datasetKind == -1) {
					datasetKind = DATASET_GRID;
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.SGL));
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.LZW));
					continue;
				} else if (datasetKind != DATASET_GRID) {
					encodeType.clear();
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.NONE));
					break;
				}

			} else if (dataset instanceof DatasetImage || dataset instanceof DatasetImageCollection) {
				if (datasetKind == -1) {
					datasetKind = DATASET_IMAGE;
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.DCT));
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.LZW));
					continue;
				} else if (datasetKind != DATASET_IMAGE) {
					encodeType.clear();
					encodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.NONE));
					break;
				}

			}
		}

		comboBoxEncodeType.setModel(new DefaultComboBoxModel<String>(encodeType.toArray(new String[encodeType.size()])));
		// 字符集
		ArrayList<String> charsetes = new ArrayList<String>();
		datasetKind = -1;
		for (int i = 0; i < table.getRowCount(); i++) {
			String datasourceName = table.getValueAt(i, COLUMN_INDEX_CurrentDatasource).toString();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			String datasetName = table.getValueAt(i, COLUMN_INDEX_Dataset).toString();
			Dataset dataset = datasource.getDatasets().get(datasetName);

			if (dataset instanceof DatasetVector) {
				if(datasetKind == -1){
					datasetKind=DATASET_VECTOR;
					charsetes.add(CharsetUtilities.toString(Charset.OEM));
					charsetes.add(CharsetUtilities.toString(Charset.EASTEUROPE));
					charsetes.add(CharsetUtilities.toString(Charset.THAI));
					charsetes.add(CharsetUtilities.toString(Charset.RUSSIAN));
					charsetes.add(CharsetUtilities.toString(Charset.BALTIC));
					charsetes.add(CharsetUtilities.toString(Charset.ARABIC));
					charsetes.add(CharsetUtilities.toString(Charset.HEBREW));
					charsetes.add(CharsetUtilities.toString(Charset.VIETNAMESE));
					charsetes.add(CharsetUtilities.toString(Charset.TURKISH));
					charsetes.add(CharsetUtilities.toString(Charset.GREEK));
					charsetes.add(CharsetUtilities.toString(Charset.CHINESEBIG5));
					charsetes.add(CharsetUtilities.toString(Charset.JOHAB));
					charsetes.add(CharsetUtilities.toString(Charset.HANGEUL));
					charsetes.add(CharsetUtilities.toString(Charset.SHIFTJIS));
					charsetes.add(CharsetUtilities.toString(Charset.MAC));
					charsetes.add(CharsetUtilities.toString(Charset.SYMBOL));
					charsetes.add(CharsetUtilities.toString(Charset.DEFAULT));
					charsetes.add(CharsetUtilities.toString(Charset.ANSI));
					charsetes.add(CharsetUtilities.toString(Charset.UTF8));
					charsetes.add(CharsetUtilities.toString(Charset.UTF7));
					charsetes.add(CharsetUtilities.toString(Charset.WINDOWS1252));
					charsetes.add(CharsetUtilities.toString(Charset.KOREAN));
					charsetes.add(CharsetUtilities.toString(Charset.UNICODE));
					charsetes.add(CharsetUtilities.toString(Charset.CYRILLIC));
					charsetes.add(CharsetUtilities.toString(Charset.XIA5));
					charsetes.add(CharsetUtilities.toString(Charset.XIA5GERMAN));
					charsetes.add(CharsetUtilities.toString(Charset.XIA5SWEDISH));
					charsetes.add(CharsetUtilities.toString(Charset.XIA5NORWEGIAN));
					charsetes.add(CharsetUtilities.toString(Charset.GB18030));
				}else if (datasetKind != DATASET_VECTOR) {
					charsetes.clear();
					charsetes.add(CharsetUtilities.toString(null));
					checkboxCharset.setEnabled(false);
					break;
				}
			}else {
				if(datasetKind == -1){
					datasetKind = DATASET_GRID;
					charsetes.add(CharsetUtilities.toString(null));
				}else if(datasetKind != DATASET_GRID){
					charsetes.clear();
					charsetes.add(CharsetUtilities.toString(null));
					checkboxCharset.setEnabled(false);
					break;
				}
			}
		}
		comboBoxCharset.setModel(new DefaultComboBoxModel<String>(charsetes.toArray(new String[charsetes.size()])));
	}
}
