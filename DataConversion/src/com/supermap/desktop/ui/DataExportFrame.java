package com.supermap.desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.supermap.data.Dataset;
import com.supermap.data.conversion.CADVersion;
import com.supermap.data.conversion.ExportSetting;
import com.supermap.data.conversion.ExportSettingBMP;
import com.supermap.data.conversion.ExportSettingDWG;
import com.supermap.data.conversion.ExportSettingDXF;
import com.supermap.data.conversion.ExportSettingGIF;
import com.supermap.data.conversion.ExportSettingJPG;
import com.supermap.data.conversion.ExportSettingPNG;
import com.supermap.data.conversion.ExportSettingSIT;
import com.supermap.data.conversion.ExportSettingTIF;
import com.supermap.data.conversion.FileType;
import com.supermap.desktop.ExportFileInfo;
import com.supermap.desktop.FileChooserControl;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CommonListCellRenderer;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.DataExportCallable;
import com.supermap.desktop.util.DatasetUtil;
import com.supermap.desktop.util.ExportFunction;
import com.supermap.desktop.util.ExportModel;

/**
 * @author Administrator 数据导出主体界面
 */
public class DataExportFrame extends SmDialog {
	public DataExportFrame(JFrame owner, boolean modal) {
		super(owner, modal);
	}

	public DataExportFrame(JDialog owner, boolean modal) {
		super(owner, modal);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private FileChooserControl fileChooser = new FileChooserControl();
	private FileChooserControl filePath = new FileChooserControl();
	private ExportModel model;
	private JButton buttonAddFile = new JButton();
	private JButton buttonDelete = new JButton();
	private JButton buttonSelectAll = new JButton();
	private JButton buttonInvertSelect = new JButton();
	private JButton buttonExport = new JButton("string_button_outport");
	private JPanel panelTable = new JPanel();
	private JLabel labelCompression = new JLabel("string_label_compression");
	private JLabel labelRecordFile = new JLabel("string_label_lblFile");
	private JPanel panelCommon = new JPanel();
	private final JCheckBox checkboxIsClose = new JCheckBox("string_chcekbox_autoCloseOut");
	private JTextField textFieldCompression;
	private JPasswordField textFieldPassword;
	private JPasswordField textFieldConfrim;
	private JCheckBox checkboxTFW = new JCheckBox("string_chcekbox_TFW");

	private JCheckBox checkboxExtends = new JCheckBox("string_chcekbox_extends");
	private JLabel labelCAD = new JLabel("string_label_lblCAD");
	private JLabel labelPassword = new JLabel("string_label_lblPassword");
	private JLabel labelExportType = new JLabel("String_FormGiveSameValue_CheckBoxExportType");
	private JLabel labelConfrimPassword = new JLabel("string_label_lblConfrimPassword");
	private JComboBox<Object> comboBoxCAD = new JComboBox<Object>();
	private ArrayList<ExportFileInfo> exports;
	private final JButton buttonClose = new JButton("string_button_close");
	private final JRadioButton radioButtonNO = new JRadioButton("no");
	private JLabel labelCover = new JLabel("Cover");
	private JLabel labelFilePath = new JLabel("FilePath");
	private JRadioButton radioButtonOK = new JRadioButton("yes");
	private JComboBox<String> comboBoxFileType = new JComboBox<String>();
	private transient Dataset[] datasets;

	/**
	 * @wbp.parser.constructor
	 */
	public DataExportFrame(Dataset[] datasets, JFrame owner, boolean flag) {
		super(owner, flag);
		this.datasets = datasets;
		initResources();
		initCompanent();
		setButtonState();
	}

	/**
	 * 初始化table列表
	 * 
	 * @return
	 */
	private ArrayList<ExportFileInfo> initExports() {
		ArrayList<ExportFileInfo> result = new ArrayList<ExportFileInfo>();
		if (null != datasets) {
			for (int i = 0; i < datasets.length; i++) {
				ExportFileInfo temp = new ExportFileInfo();
				Dataset dataset = datasets[i];
				String datasourceAlias = dataset.getDatasource().getAlias();
				temp.setDataset(dataset);
				temp.setDatasetName(dataset.getName() + DataConversionProperties.getString("string_index_and") + datasourceAlias);
				temp.setDatasource(dataset.getDatasource());
				String datasetType = dataset.getType().toString();
				temp.setDataType(DatasetUtil.getDatasetName(datasetType, "", 0));
				ExportSetting exportSetting = new ExportSetting();
				exportSetting.setTargetFilePath(System.getProperty("user.dir") + File.separator);
				temp.setExportSetting(exportSetting);
				exportSetting.setSourceData(dataset);
				temp.setFileTypes(exportSetting.getSupportedFileType());
				temp.setFileName(dataset.getName());
				temp.setFilePath(exportSetting.getTargetFilePath());
				temp.setState(DataConversionProperties.getString("string_change"));
				FileType fileType = null;
				if (0 < temp.getFileTypes().length) {
					fileType = temp.getFileTypes()[0];
				} else {
					fileType = FileType.NONE;
				}
				temp.setTargetFileType(fileType);
				ExportFunction.initExportSetting(temp, fileType.name());
				result.add(temp);
			}
		}
		return result;
	}

	public void initCompanent() {
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 280, 860, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		textFieldCompression = new JTextField("75");
		textFieldCompression.setEnabled(false);
		textFieldCompression.setColumns(10);

		textFieldPassword = new JPasswordField();
		textFieldPassword.setEnabled(false);
		textFieldPassword.setColumns(10);

		textFieldConfrim = new JPasswordField();
		textFieldConfrim.setEnabled(false);
		textFieldConfrim.setColumns(10);
		ButtonGroup bg = new ButtonGroup();
		bg.add(radioButtonOK);
		bg.add(radioButtonNO);
		//@formatter:off
		
		//labelCompression textFieldCompression
		//labelRecordFile
		//fileChooser
		//checkboxTFW
		//checkboxExtends
		//labelCAD comboBoxCAD
		//labelPassword textFieldPassword
		//labelConfrimPassword  textFieldConfrim
		//checkBoxFileType comboBoxFileType
		//checkBoxCover radioButtonOK radioButtonNO
		//checkBoxFilePath filePath
		GroupLayout gl_panelCommon = new GroupLayout(panelCommon);
		gl_panelCommon.setAutoCreateContainerGaps(true);
		gl_panelCommon.setAutoCreateGaps(true);
		gl_panelCommon.setHorizontalGroup(
			gl_panelCommon.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelCommon.createSequentialGroup()
					.addComponent(labelCompression, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldCompression, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_TEXT_WIDTH, Short.MAX_VALUE))
				.addComponent(labelRecordFile)
				.addComponent(fileChooser, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_TEXTFILED_WIDTH, Short.MAX_VALUE)
				.addComponent(checkboxTFW)
				.addComponent(checkboxExtends)
				.addGroup(gl_panelCommon.createSequentialGroup()
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.TRAILING)
						.addComponent(labelCAD, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelPassword, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelConfrimPassword, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelExportType,packageInfo.DEFAULT_ZERO,packageInfo.DEFAULT_LABEL_WIDTH,Short.MAX_VALUE)
						.addComponent(labelCover, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelFilePath, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE))
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBoxCAD, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_TEXT_WIDTH, Short.MAX_VALUE)
						.addComponent(textFieldPassword, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_TEXT_WIDTH, Short.MAX_VALUE)
						.addComponent(textFieldConfrim, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_TEXT_WIDTH, Short.MAX_VALUE)
						.addComponent(comboBoxFileType, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_TEXT_WIDTH, Short.MAX_VALUE)
						.addGroup(gl_panelCommon.createSequentialGroup()
							.addComponent(radioButtonOK)
							.addComponent(radioButtonNO))
						.addComponent(filePath, packageInfo.DEFAULT_ZERO, packageInfo.DEFAULT_TEXT_WIDTH, Short.MAX_VALUE)))
		);
		gl_panelCommon.setVerticalGroup(
			gl_panelCommon.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelCommon.createSequentialGroup()
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.CENTER)
						.addComponent(labelCompression)
						.addComponent(textFieldCompression))
					.addComponent(labelRecordFile)
					.addComponent(fileChooser)
					.addComponent(checkboxTFW)
					.addComponent(checkboxExtends)
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.CENTER)
						.addComponent(labelCAD)
						.addComponent(comboBoxCAD))
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.CENTER)
						.addComponent(labelPassword)
						.addComponent(textFieldPassword))
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.CENTER)
						.addComponent(labelConfrimPassword)
						.addComponent(textFieldConfrim))
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.CENTER)
						.addComponent(labelExportType)
						.addComponent(comboBoxFileType))
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.CENTER)
						.addComponent(labelCover)
						.addComponent(radioButtonOK)
						.addComponent(radioButtonNO))
					.addGroup(gl_panelCommon.createParallelGroup(Alignment.CENTER)
						.addComponent(labelFilePath)
						.addComponent(filePath)))
		);
		gl_panelCommon.setAutoCreateContainerGaps(true);
		gl_panelCommon.setAutoCreateGaps(true);
		
		radioButtonNO.setSelected(true);
		fileChooser.getEditor().setEnabled(false);
		fileChooser.getButton().setEnabled(false);
		checkboxTFW.setEnabled(false);
		checkboxExtends.setEnabled(false);
		comboBoxCAD.setEnabled(false);
								comboBoxCAD.setModel(new DefaultComboBoxModel<Object>(new String[] {
										"CAD12", "CAD13", "CAD14", "CAD2000", "CAD2004", "CAD2007" }));
								comboBoxCAD.setSelectedIndex(5);
								panelCommon.setLayout(gl_panelCommon);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelTable, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
						.addComponent(checkboxIsClose))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(176)
							.addComponent(buttonExport, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonClose, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(panelCommon, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)))
					.addGap(20))
		);
		buttonExport.setEnabled(false);
		checkboxIsClose.setSelected(true);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panelTable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panelCommon, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonExport)
						.addComponent(buttonClose)
						.addComponent(checkboxIsClose))
					.addGap(37))
		);
		gl_contentPane.setAutoCreateContainerGaps(true);
		gl_contentPane.setAutoCreateGaps(true);

		JToolBar toolBar = new JToolBar();
		toolBar.setForeground(UIManager.getColor("Button.light"));
		toolBar.setBackground(UIManager.getColor("Button.light"));
		toolBar.setFloatable(false);

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panelTable = new GroupLayout(panelTable);
		gl_panelTable.setHorizontalGroup(
			gl_panelTable.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTable.createSequentialGroup()
					.addGroup(gl_panelTable.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTable.createSequentialGroup()
							.addGap(1)
							.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_panelTable.createSequentialGroup()
							.addGap(2)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 465, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panelTable.setVerticalGroup(
			gl_panelTable.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTable.createSequentialGroup()
					.addGap(7)
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
		);
		//@formatter:on
		toolBar.add(buttonAddFile);
		buttonDelete.setEnabled(false);

		toolBar.add(buttonDelete);
		buttonSelectAll.setEnabled(false);

		toolBar.add(buttonSelectAll);
		buttonInvertSelect.setEnabled(false);

		toolBar.add(buttonInvertSelect);

		table = new JTable();
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		exports = initExports();
		model = new ExportModel(exports);
		table.setModel(model);
		table.getColumnModel().getColumn(0).setCellRenderer(new CommonListCellRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(new CommonListCellRenderer());
		table.setRowHeight(20);
		if (0 < table.getRowCount()) {
			table.setRowSelectionInterval(0, 0);
		}
		ExportFunction.getRigthPanel(this, table);
		table.getColumnModel().getColumn(1).setPreferredWidth(10);
		table.getColumnModel().getColumn(2).setPreferredWidth(10);
		scrollPane.setViewportView(table);
		panelTable.setLayout(gl_panelTable);
		buttonAddFile.addActionListener(new CommonListener(this));
		buttonDelete.addActionListener(new CommonListener(this));
		buttonClose.addActionListener(new CommonListener(this));
		buttonSelectAll.addActionListener(new CommonListener(this));
		buttonInvertSelect.addActionListener(new CommonListener(this));
		buttonExport.addActionListener(new CommonListener(this));
		table.addKeyListener(new ExportKeyAction(this));
		table.addMouseListener(new OutportMouseListener(this));
		scrollPane.addMouseListener(new OutportMouseListener(this));
		contentPane.setLayout(gl_contentPane);
		// 为comboBoxFileType添加响应事件,设置选中行的导出类型
		comboBoxFileType.addActionListener(new CommonListener(this));
		radioButtonOK.addActionListener(new CommonListener());
		radioButtonNO.addActionListener(new CommonListener());
		filePath.getButton().addActionListener(new CommonListener());
		fileChooser.getButton().addActionListener(new CommonListener());
		checkboxTFW.addActionListener(new CommonListener());
		checkboxExtends.addActionListener(new CommonListener());
		comboBoxCAD.addActionListener(new CommonListener());
		// 设置密码
		textFieldConfrim.addKeyListener(new LocalKeyAdapter());
		textFieldPassword.addKeyListener(new LocalKeyAdapter());
		textFieldCompression.getDocument().addDocumentListener(new LocalDocumentListener());
	}

	public void initResources() {
		setTitle(DataConversionProperties.getString("String_FormExport_FormText"));
		buttonAddFile.setIcon(new ImageIcon(DataExportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_AddMap.png")));
		buttonSelectAll.setIcon(new ImageIcon(DataExportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		buttonInvertSelect.setIcon(new ImageIcon(DataExportFrame.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		buttonDelete.setIcon(new ImageIcon(DataExportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		labelCover.setText(DataConversionProperties.getString("String_Label_OverWrite"));
		labelFilePath.setText(DataConversionProperties.getString("String_FormGiveSameValue_CheckBoxExportPath"));
		radioButtonOK.setText(DataConversionProperties.getString("String_FormGiveSameValue_RadioYes"));
		radioButtonNO.setText(DataConversionProperties.getString("String_FormGiveSameValue_RadioNo"));
		buttonExport.setText(CommonProperties.getString("String_Button_Export"));
		checkboxIsClose.setText(DataConversionProperties.getString("string_chcekbox_autoCloseOut"));
		labelCompression.setText(DataConversionProperties.getString("string_label_compression"));
		buttonClose.setText(CommonProperties.getString("String_Button_Close"));
		labelRecordFile.setText(DataConversionProperties.getString("string_label_lblFile"));
		buttonAddFile.setToolTipText(DataConversionProperties.getString("string_button_add"));
		buttonDelete.setToolTipText(DataConversionProperties.getString("string_button_delete"));
		buttonSelectAll.setToolTipText(DataConversionProperties.getString("string_button_selectAll"));
		buttonInvertSelect.setToolTipText(DataConversionProperties.getString("string_button_invertSelect"));
		buttonClose.setToolTipText(DataConversionProperties.getString("string_button_close"));
		filePath.getEditor().setText(System.getProperty("user.dir"));
		checkboxTFW.setText(DataConversionProperties.getString("string_chcekbox_TFW"));
		checkboxExtends.setText(DataConversionProperties.getString("string_chcekbox_extends"));
		labelCAD.setText(DataConversionProperties.getString("string_label_lblCAD"));
		labelPassword.setText(DataConversionProperties.getString("string_label_lblPassword"));
		labelConfrimPassword.setText(DataConversionProperties.getString("string_label_lblConfrimPassword"));
		labelExportType.setText(DataConversionProperties.getString("String_FormGiveSameValue_CheckBoxExportType"));

		panelCommon.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelproperty"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
	}

	/**
	 * 设置按键状态
	 */
	private void setButtonState() {
		boolean hasExportInfo = false;
		if (0 < table.getRowCount()) {
			hasExportInfo = true;
		}
		if (hasExportInfo) {
			buttonDelete.setEnabled(true);
			buttonExport.setEnabled(true);
			buttonInvertSelect.setEnabled(true);
			buttonSelectAll.setEnabled(true);
		} else {
			buttonDelete.setEnabled(false);
			buttonExport.setEnabled(false);
			buttonInvertSelect.setEnabled(false);
			buttonSelectAll.setEnabled(false);
		}
	}

	class CommonListener implements ActionListener {
		private DataExportFrame frame;

		public CommonListener() {
			super();
		}

		public CommonListener(DataExportFrame frame) {
			this.frame = frame;
		}

		private CADVersion getCADVersion(String item) {
			CADVersion version = null;
			if ("CAD2007".equalsIgnoreCase(item)) {
				version = CADVersion.CAD2007;
			}
			if ("CAD2004".equalsIgnoreCase(item)) {
				version = CADVersion.CAD2004;
			}
			if ("CAD2000".equalsIgnoreCase(item)) {
				version = CADVersion.CAD2000;
			}
			if ("CAD12".equalsIgnoreCase(item)) {
				version = CADVersion.CAD12;
			}
			if ("CAD14".equalsIgnoreCase(item)) {
				version = CADVersion.CAD14;
			}
			if ("CAD13".equalsIgnoreCase(item)) {
				version = CADVersion.CAD13;
			}
			return version;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			if (c == comboBoxCAD) {
				String item = (String) comboBoxCAD.getSelectedItem();
				int[] rows = table.getSelectedRows();
				if (0 < table.getRowCount()) {
					for (int i = 0; i < rows.length; i++) {
						ExportFileInfo tempExport = model.getTagValueAt(rows[i]);
						ExportSetting tempExportSetting = tempExport.getExportSetting();
						if (tempExportSetting instanceof ExportSettingDWG) {
							((ExportSettingDWG) tempExportSetting).setVersion(getCADVersion(item));
						}
						if (tempExportSetting instanceof ExportSettingDXF) {
							((ExportSettingDXF) tempExportSetting).setVersion(getCADVersion(item));
						}
					}
				}
			}
			if (c == checkboxExtends) {
				boolean isSelect = checkboxExtends.isSelected();
				int[] rows = table.getSelectedRows();
				if (0 < table.getRowCount()) {
					for (int i = 0; i < rows.length; i++) {
						ExportFileInfo tempExport = model.getTagValueAt(rows[i]);
						ExportSetting tempExportSetting = tempExport.getExportSetting();
						if (tempExportSetting instanceof ExportSettingDWG) {
							((ExportSettingDWG) tempExportSetting).setExportingExternalData(isSelect);
						}
						if (tempExportSetting instanceof ExportSettingDXF) {
							((ExportSettingDXF) tempExportSetting).setExportingExternalData(isSelect);
						}
					}
				}
			}
			if (c == checkboxTFW) {
				boolean isSelect = checkboxTFW.isSelected();
				int[] rows = table.getSelectedRows();
				if (0 < table.getRowCount()) {
					for (int i = 0; i < rows.length; i++) {
						ExportFileInfo tempExport = model.getTagValueAt(rows[i]);
						ExportSetting tempExportSetting = tempExport.getExportSetting();
						((ExportSettingTIF) tempExportSetting).setExportingGeoTransformFile(isSelect);
					}
				}
			}
			if (c == fileChooser.getButton()) {
				if (!SmFileChoose.isModuleExist("DataExportFrame")) {
					String fileFilters = SmFileChoose.createFileFilter(DataConversionProperties.getString("string_filetype_tfw"), "tfw");
					SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
							DataConversionProperties.getString("String_Export"), "DataExportFrame", "OpenOne");
				}

				SmFileChoose fileChooserc = new SmFileChoose("DataExportFrame");
				int state = fileChooserc.showDefaultDialog();
				String tempfilePath = fileChooserc.getFilePath();
				File file = null;
				if (tempfilePath != null && tempfilePath.length() <= 0) {
					file = new File(fileChooserc.getFilePath());
				}
				if (state == JFileChooser.APPROVE_OPTION && null != file) {
					fileChooser.getEditor().setText(file.getAbsolutePath());
					// 设置坐标参考文件
					String worldFile = file.getAbsolutePath();
					File existFile = new File(worldFile);
					if (existFile.exists()) {
						int[] rows = table.getSelectedRows();
						if (0 < rows.length) {
							for (int i = 0; i < rows.length; i++) {
								ExportFileInfo tempExport = model.getTagValueAt(rows[i]);
								ExportSetting exportSetting = tempExport.getExportSetting();
								if (exportSetting instanceof ExportSettingBMP) {
									((ExportSettingBMP) exportSetting).setWorldFilePath(worldFile);
								}
								if (exportSetting instanceof ExportSettingGIF) {
									((ExportSettingGIF) exportSetting).setWorldFilePath(worldFile);
								}
								if (exportSetting instanceof ExportSettingJPG) {
									((ExportSettingJPG) exportSetting).setWorldFilePath(worldFile);
								}
								if (exportSetting instanceof ExportSettingPNG) {
									((ExportSettingPNG) exportSetting).setWorldFilePath(worldFile);
								}
							}
						}
					}
				}
			}
			if (c == comboBoxFileType) {
				String item = (String) comboBoxFileType.getSelectedItem();
				FileType fileType = DatasetUtil.getFileType(item);
				ArrayList<ExportFileInfo> tempFileInfos = new ArrayList<ExportFileInfo>();
				int[] rows = table.getSelectedRows();
				if (0 < rows.length) {
					for (int i = 0; i < rows.length; i++) {
						ExportFileInfo tempExport = model.getTagValueAt(rows[i]);
						ExportFunction.initExportSetting(tempExport, fileType.name());
						tempExport.setTargetFileType(fileType);
						tempFileInfos.add(tempExport);
					}
				}
				ExportFunction.refreshPanelForCombobox(ExportFunction.getRightPanelState(fileType.name()), frame);
			}
			if (c == radioButtonOK) {
				boolean isSelect = radioButtonOK.isSelected();
				int[] rows = table.getSelectedRows();
				if (0 < rows.length) {
					for (int i = 0; i < rows.length; i++) {
						ExportFileInfo tempExport = model.getTagValueAt(rows[i]);
						if (isSelect) {
							tempExport.getExportSetting().setOverwrite(true);
							tempExport.setCover(true);
						} else {
							tempExport.getExportSetting().setOverwrite(false);
							tempExport.setCover(false);
						}
					}
				}
			}
			if (c == radioButtonNO) {
				boolean isSelect = radioButtonNO.isSelected();
				int[] rows = table.getSelectedRows();
				if (0 < rows.length) {
					for (int i = 0; i < rows.length; i++) {
						ExportFileInfo tempExport = model.getTagValueAt(rows[i]);
						if (isSelect) {
							tempExport.getExportSetting().setOverwrite(false);
							tempExport.setCover(false);
						} else {
							tempExport.getExportSetting().setOverwrite(true);
							tempExport.setCover(true);
						}
					}
				}
			}
			if (c == filePath.getButton()) {
				if (!SmFileChoose.isModuleExist("DataExportFrame_OutPutDirectories")) {
					SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), DataConversionProperties.getString("String_Export"),
							"DataExportFrame_OutPutDirectories", "GetDirectories");
				}
				SmFileChoose tempfileChooser = new SmFileChoose("DataExportFrame_OutPutDirectories");
				int state = tempfileChooser.showDefaultDialog();
				if (state == JFileChooser.APPROVE_OPTION) {
					String directories = tempfileChooser.getFilePath();
					filePath.getEditor().setText(directories);
					int[] rows = table.getSelectedRows();
					if (0 < rows.length) {
						for (int i = 0; i < rows.length; i++) {
							ExportFileInfo tempExport = model.getTagValueAt(rows[i]);
							String filePathTemp = directories + File.separator;
							tempExport.setFilePath(filePathTemp);
							tempExport.getExportSetting().setTargetFilePath(filePathTemp);
						}
					}
				}
			}
			if (c == buttonAddFile) {
				// 添加
				DataSetChooserExPort datasetChooser = new DataSetChooserExPort(frame, table);
				datasetChooser.setVisible(true);
				ExportFunction.getRigthPanel(frame, table);
				setButtonState();
			} else if (c == buttonDelete) {
				// 删除
				if (table.getRowCount() > 0) {
					int[] selects = table.getSelectedRows();
					model = (ExportModel) table.getModel();
					model.removeRows(selects);
					// 没有删除完时，设置第一行被选中
					if (table.getRowCount() > 0) {
						table.setRowSelectionInterval(0, 0);
						ExportFunction.getRigthPanelAsSet(frame, table);
					} else {
						comboBoxFileType.setModel(new DefaultComboBoxModel<String>());
						ExportFunction.setRightPanelAsDefualt(frame);
						table.clearSelection();
						setButtonState();
					}
				}
			} else if (c == buttonSelectAll) {
				// 全选
				if (table.getRowCount() > 0) {
					table.setRowSelectionAllowed(true);
					table.setRowSelectionInterval(0, table.getRowCount() - 1);
				}
			} else if (c == buttonInvertSelect) {
				// 反选
				CommonFunction.selectInvert(table);
				ExportFunction.getRigthPanel(frame, table);

			} else if (c == buttonClose) {
				// 关闭
				dispose();
			} else if (c == buttonExport) {
				// 导出
				if (exports.isEmpty()) {
					UICommonToolkit.showMessageDialog(DataConversionProperties.getString("String_ExportSettingPanel_Cue_AddFiles"));
				} else {
					FormProgressTotal formProgress = new FormProgressTotal();
					formProgress.doWork(new DataExportCallable(exports, table, radioButtonOK.isSelected()));
					if (checkboxIsClose.isSelected()) {
						dispose();
					}
				}
			}

		}
	}

	class OutportMouseListener extends MouseAdapter {
		private DataExportFrame frame;

		public OutportMouseListener(DataExportFrame frame) {
			this.frame = frame;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (2 == e.getClickCount()) {
				table.setRowSelectionAllowed(true);
				DataSetChooserExPort datasetChooser = new DataSetChooserExPort(frame, table);
				datasetChooser.setVisible(true);
				ExportFunction.getRigthPanel(frame, table);
				setButtonState();
			} else if (DataExportFrame.this.table.getSelectedRows().length==1 && 1 == e.getClickCount()) {
				ExportFunction.getRigthPanelAsSet(frame, table);
			}else {
				ExportFunction.getRigthPanel(frame, table);
				setButtonState();
			}
		}
	}

	// 通用的键盘响应事件
	class ExportKeyAction extends KeyAdapter {
		DataExportFrame frame;

		public ExportKeyAction(DataExportFrame frame) {
			this.frame = frame;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if ((e.isControlDown() == true) && (e.getKeyCode() == KeyEvent.VK_A) && (1 <= table.getRowCount())) {
				// 键盘点击ctrl+A,全选
				ExportFunction.getRigthPanel(frame, table);
				table.setRowSelectionInterval(0, table.getRowCount() - 1);
				ExportFunction.getRigthPanel(frame, table);
			}
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				// 键盘点击delete,删除
				int[] counts = table.getSelectedRows();
				model.removeRows(counts);
				if (0 < table.getRowCount()) {
					table.setRowSelectionInterval(0, 0);
					ExportFunction.getRigthPanelAsSet(frame, table);
				} else {
					comboBoxFileType.setModel(new DefaultComboBoxModel<String>());
					ExportFunction.setRightPanelAsDefualt(frame);
					table.clearSelection();
					setButtonState();
				}
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
				ExportFunction.getRigthPanelAsSet(frame, table);
			}
			if ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_UP) || ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_DOWN))) {
				ExportFunction.getRigthPanel(frame, table);
			}
		}

	}

	class LocalDocumentListener implements DocumentListener {

		private void setCompression() {
			int[] selectRows = table.getSelectedRows();
			String compression = textFieldCompression.getText();
			for (int i = 0; i < selectRows.length; i++) {
				ExportFileInfo tempFileInfo = model.getTagValueAt(selectRows[i]);
				ExportSetting exportSetting = tempFileInfo.getExportSetting();
				if ((!compression.isEmpty()) && (!"".equals(compression)) && (exportSetting instanceof ExportSettingJPG)) {
					((ExportSettingJPG) exportSetting).setCompression(Integer.valueOf(compression));
				}
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			setCompression();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			setCompression();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setCompression();
		}

	}

	class LocalKeyAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			String password = String.valueOf(textFieldPassword.getPassword());
			String confrim = String.valueOf(textFieldConfrim.getPassword());
			if (!confrim.equals(password)) {
				buttonExport.setEnabled(false);
			} else {
				int[] selectRows = table.getSelectedRows();
				for (int i = 0; i < selectRows.length; i++) {
					ExportFileInfo tempFileInfo = model.getTagValueAt(selectRows[i]);
					ExportSetting exportSetting = tempFileInfo.getExportSetting();
					if (!password.isEmpty() && !"".equals(password)) {
						((ExportSettingSIT) exportSetting).setPassword(password);
					}
				}
				buttonExport.setEnabled(true);
			}
		}

	}

	public FileChooserControl getFilePath() {
		return filePath;
	}

	public void setFilePath(FileChooserControl filePath) {
		this.filePath = filePath;
	}

	public FileChooserControl getFileChooser() {
		return fileChooser;
	}

	public void setFileChooser(FileChooserControl fileChooser) {
		this.fileChooser = fileChooser;
	}

	public JTextField getTextFieldCompression() {
		return textFieldCompression;
	}

	public void setTextFieldCompression(JTextField textFieldCompression) {
		this.textFieldCompression = textFieldCompression;
	}

	public JTextField getTextFieldPassword() {
		return textFieldPassword;
	}

	public JTextField getTextFieldConfrim() {
		return textFieldConfrim;
	}

	public JCheckBox getCheckboxTFW() {
		return checkboxTFW;
	}

	public void setCheckboxTFW(JCheckBox checkboxTFW) {
		this.checkboxTFW = checkboxTFW;
	}

	public JCheckBox getCheckboxExtends() {
		return checkboxExtends;
	}

	public void setCheckboxExtends(JCheckBox checkboxExtends) {
		this.checkboxExtends = checkboxExtends;
	}

	public JComboBox<Object> getComboBoxCAD() {
		return comboBoxCAD;
	}

	public void setComboBoxCAD(JComboBox<Object> comboBoxCAD) {
		this.comboBoxCAD = comboBoxCAD;
	}

	public JRadioButton getRadioButtonOK() {
		return radioButtonOK;
	}

	public void setRadioButtonOK(JRadioButton radioButtonOK) {
		this.radioButtonOK = radioButtonOK;
	}

	public JRadioButton getRadioButtonNO() {
		return radioButtonNO;
	}

	public JComboBox<String> getComboBoxFileType() {
		return comboBoxFileType;
	}

	public void setComboBoxFileType(JComboBox<String> comboBoxFileType) {
		this.comboBoxFileType = comboBoxFileType;
	}

}
