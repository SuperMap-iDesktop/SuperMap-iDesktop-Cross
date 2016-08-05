package com.supermap.desktop.ui;

import com.supermap.data.conversion.ImportSettingBIL;
import com.supermap.data.conversion.ImportSettingDXF;
import com.supermap.data.conversion.ImportSettingGBDEM;
import com.supermap.data.conversion.ImportSettingGJB;
import com.supermap.data.conversion.ImportSettingGRD;
import com.supermap.data.conversion.ImportSettingLIDAR;
import com.supermap.data.conversion.ImportSettingModelDXF;
import com.supermap.data.conversion.ImportSettingTEMSClutter;
import com.supermap.data.conversion.ImportSettingUSGSDEM;
import com.supermap.desktop.Application;
import com.supermap.desktop.FileTypeLocale;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.action.CommonMouseListener;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.SteppedComboBox;
import com.supermap.desktop.ui.controls.TableRowCellEditor;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.FileInfoModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 数据导入主体界面
 */
public class DataImportFrame extends SmDialog {
	/**
	 * @wbp.parser.constructor
	 */
	public DataImportFrame(JFrame owner, boolean modal) {
		super(owner, modal);
		initResources();
		initComponents();
		registActionListener();
		initDrag();
		this.buttonAddFile.setSelected(false);
		this.componentList.add(this.buttonImport);
		this.componentList.add(this.buttonClose);
		this.setFocusTraversalPolicy(this.policy);
		this.getRootPane().setDefaultButton(this.buttonClose);
	}

	public DataImportFrame(JDialog owner, boolean modal) {
		super(owner, modal);
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private ArrayList<ImportFileInfo> fileInfos;
	private ArrayList<JPanel> panels;
	private FileInfoModel model;
	private JButton buttonAddFile = new JButton();
	private JButton buttonAddDir = new JButton();
	private JButton buttonDelete = new JButton();
	private JButton buttonSelectAll = new JButton();
	private JButton buttonInvertSelect = new JButton();
	private SmButton buttonImport = new SmButton("import");
	private SmButton buttonClose = new SmButton("string_button_close");
	private JPanel panelFiles = new JPanel();
	private JLabel labelTitle = new JLabel("string_label_importData");
	private JLabel labelRemind = new JLabel("string_label_addFileRemind");
	private JPanel panelParams = new JPanel();
	private JPanel panelImportInfo = new JPanel();
	private JCheckBox checkBoxAutoClose = new JCheckBox("string_chcekbox_autoCloseIn");
	private JToolBar toolBar = new JToolBar();
	private JScrollPane scrollPane;

	private DropTarget dropTargetTemper;

	private int count;
	private String fileTypeInfo;

	private transient CommonButtonAction buttonAction = new CommonButtonAction();
	private transient LocalWindowListener windowListener = new LocalWindowListener();
	private transient CommonMouseListener commonMouseListener;
	private transient CommonKeyAction commonKeyListener;
	private ItemListener aListener;
	private transient SteppedComboBox steppedComboBox;
	private MouseListener tableMouseListener = new TableMouseListener();

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.commonMouseListener = new CommonMouseListener(this, panelParams, table, panelImportInfo, fileInfos, panels, labelTitle, model);
		this.commonKeyListener = new CommonKeyAction();
		unRegistAcitonListener();
		this.buttonAddFile.addActionListener(new CommonButtonAction(this));
		this.buttonDelete.addActionListener(this.buttonAction);
		this.buttonSelectAll.addActionListener(this.buttonAction);
		this.buttonInvertSelect.addActionListener(this.buttonAction);
		this.buttonImport.addActionListener(this.buttonAction);
		this.buttonClose.addActionListener(this.buttonAction);
		this.buttonAddDir.addActionListener(this.buttonAction);
		this.addWindowListener(this.windowListener);
		this.table.addKeyListener(this.commonKeyListener);
		this.scrollPane.addMouseListener(this.commonMouseListener);
		this.table.addMouseListener(this.tableMouseListener);
	}

	/**
	 * 注销事件
	 */
	private void unRegistAcitonListener() {
		this.buttonAddFile.removeActionListener(new CommonButtonAction(this));
		this.buttonDelete.removeActionListener(this.buttonAction);
		this.buttonSelectAll.removeActionListener(this.buttonAction);
		this.buttonInvertSelect.removeActionListener(this.buttonAction);
		this.buttonImport.removeActionListener(this.buttonAction);
		this.buttonClose.removeActionListener(this.buttonAction);
		this.table.removeKeyListener(this.commonKeyListener);
		this.scrollPane.removeMouseListener(this.commonMouseListener);
		this.table.removeMouseListener(this.commonMouseListener);
		this.table.removeMouseListener(this.tableMouseListener);
	}

	public void initComponents() {
		this.setLocationByPlatform(true);
		this.labelRemind.setForeground(new Color(32, 178, 170));
		this.labelRemind.setHorizontalAlignment(SwingConstants.CENTER);
		this.labelTitle.setBackground(Color.LIGHT_GRAY);
		this.labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.labelTitle.setOpaque(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(400, 280, 880, 475);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		initPanelFiles();
		initPanelImportInfo();
		initContentPane();
		this.buttonInvertSelect.setEnabled(false);
		this.checkBoxAutoClose.setSelected(true);
	}

	private void initPanelFiles() {
		this.toolBar.setFloatable(false);
		this.scrollPane = new JScrollPane();
		initToolBar();
		//@formatter:off
		this.panelFiles.setLayout(new GridBagLayout());
		this.panelFiles.add(this.toolBar,           new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(0, 0));
		this.panelFiles.add(this.scrollPane,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,5,5,5).setWeight(3, 3).setFill(GridBagConstraints.BOTH));
		this.panelFiles.add(this.checkBoxAutoClose, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,5,5,5).setWeight(0, 0));
		//@formatter:on
		// 为scrollPane和table添加事件响应
		this.table = new JTable();
		this.fileInfos = new ArrayList<ImportFileInfo>();
		this.panels = new ArrayList<JPanel>();
		this.model = new FileInfoModel(this.fileInfos);
		this.table = new JTable();

		this.table.setModel(this.model);
		this.scrollPane.setViewportView(this.table);
	}

	/**
	 * 为table添加下拉列表
	 */
	@SuppressWarnings("unchecked")
	public void initComboBoxColumns() {
		TableTooltipCellRenderer tooltipCellRenderer = TableTooltipCellRenderer.getInstance();
		this.table.getColumn(DataConversionProperties.getString("string_tabletitle_filetype")).setCellRenderer(tooltipCellRenderer);
		this.table.getColumn(DataConversionProperties.getString("string_tabletitle_data")).setCellRenderer(tooltipCellRenderer);
		TableRowCellEditor rowEditor = new TableRowCellEditor(this.table);
		for (int i = 0; i < this.fileInfos.size(); i++) {
			final ImportFileInfo tempFileInfo = this.fileInfos.get(i);
			this.steppedComboBox = new SteppedComboBox(new String[] {});
			this.steppedComboBox.removeAllItems();
			String fileName = tempFileInfo.getFileName();
			if (fileName.lastIndexOf(DataConversionProperties.getString("string_index_pause")) > 0) {
				this.fileTypeInfo = fileName.substring(fileName.lastIndexOf(DataConversionProperties.getString("string_index_pause")), fileName.length());
			}
			if (null == fileTypeInfo) {
				return;
			}
			if (this.fileTypeInfo.equalsIgnoreCase(FileTypeLocale.DXF_STRING)) {
				this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_CAD"));
				this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_FilterModel"));
				this.labelTitle.setText(DataConversionProperties.getString("String_FormImportDXF_Text"));
			} else if (this.fileTypeInfo.equals(FileTypeLocale.BIL_STRING)) {
				this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_GRID"));
				this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_BIL"));
				this.labelTitle.setText(DataConversionProperties.getString("String_FormImportRaster_Text"));
			} else if (fileTypeInfo.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
				steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_ArcGIS"));
				steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_DEM"));
				steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_GBDEM"));
				labelTitle.setText(DataConversionProperties.getString("String_FormImportGRD_Text"));
			} else if (fileTypeInfo.equalsIgnoreCase(FileTypeLocale.TXT_STRING)) {
				this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_ArcGIS"));
				this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_FilterLIDAR"));
				this.labelTitle.setText(DataConversionProperties.getString("String_FormImportGRD_Text"));
			} else {
				this.steppedComboBox.addItem(tempFileInfo.getFileType());
			}
			Dimension d = this.steppedComboBox.getPreferredSize();
			this.steppedComboBox.setPreferredSize(new Dimension(d.width, d.height));
			this.steppedComboBox.setPopupWidth(d.width);

			rowEditor.setEditorAt(i, new DefaultCellEditor(steppedComboBox));
			this.table.getColumn(DataConversionProperties.getString("string_tabletitle_filetype")).setCellEditor(rowEditor);
			this.aListener = new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						String nowType = e.getItem().toString();
						tempFileInfo.setFileType(nowType);
						((FileInfoModel) table.getModel()).updateRows(fileInfos);
						if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_BIL"))) {
							// bil 导入为BIL文件
							tempFileInfo.setImportSetting(new ImportSettingBIL());
							CommonFunction.replace(panelImportInfo, new ImportPanelArcGIS(DataImportFrame.this, tempFileInfo));
							labelTitle.setText(DataConversionProperties.getString("String_FormImportBIL_Text"));
						} else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_GRID"))) {
							// 导入为电信栅格文件
							tempFileInfo.setImportSetting(new ImportSettingTEMSClutter());
							CommonFunction.replace(panelImportInfo, new ImportPanelArcGIS(DataImportFrame.this, tempFileInfo));
							labelTitle.setText(DataConversionProperties.getString("String_FormImportRaster_Text"));
						} else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_CAD"))) {
							// DXF 导入为CAD格式文件
							tempFileInfo.setImportSetting(new ImportSettingDXF());
							CommonFunction.replace(panelImportInfo, new ImportPanelD(DataImportFrame.this, tempFileInfo));
							labelTitle.setText(DataConversionProperties.getString("String_FormImportDXF_Text"));
						} else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_FilterModel"))) {
							// DXF 导入为三维模型文件
							tempFileInfo.setImportSetting(new ImportSettingModelDXF());
							CommonFunction.replace(panelImportInfo, new ImportPanelModel(DataImportFrame.this, tempFileInfo));
							labelTitle.setText(DataConversionProperties.getString("String_FormImportDXFMODEL_Text"));
						} else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_ArcGIS"))) {
							// txt,dem 导入为ArcGIS类型文件
							tempFileInfo.setImportSetting(new ImportSettingGRD());
							CommonFunction.replace(panelImportInfo, new ImportPanelArcGIS(DataImportFrame.this, tempFileInfo));
							labelTitle.setText(DataConversionProperties.getString("String_FormImportGRD_Text"));
						} else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_FilterLIDAR"))) {
							// txt 导入为LIDAR类型文件
							tempFileInfo.setImportSetting(new ImportSettingLIDAR());
							CommonFunction.replace(panelImportInfo, new ImportPanelLIDAR(DataImportFrame.this, tempFileInfo));
							labelTitle.setText(DataConversionProperties.getString("String_FormImportLIDAR_Text"));
						} else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_DEM"))) {
							// dem 导入为美国标准DEM文件
							tempFileInfo.setImportSetting(new ImportSettingUSGSDEM());
							CommonFunction.replace(panelImportInfo, new ImportPanelArcGIS(DataImportFrame.this, tempFileInfo));
							labelTitle.setText(DataConversionProperties.getString("String_FormImportDEM_Text"));
						} else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_GBDEM"))) {
							// dem 导入为中国标准DEM文件
							tempFileInfo.setImportSetting(new ImportSettingGBDEM());
							CommonFunction.replace(panelImportInfo, new ImportPanelArcGIS(DataImportFrame.this, tempFileInfo));
							labelTitle.setText(DataConversionProperties.getString("String_FormImportDEM_Text"));
						}
					}
				}
			};
			this.steppedComboBox.addItemListener(this.aListener);
		}
	}

	private void initToolBar() {
		this.toolBar.add(this.buttonAddFile);
		this.toolBar.add(this.buttonAddDir);
		this.buttonDelete.setEnabled(false);
		this.toolBar.add(this.buttonDelete);
		this.buttonSelectAll.setEnabled(false);
		this.toolBar.add(this.buttonSelectAll);
		this.toolBar.add(this.buttonInvertSelect);
	}

	private void initPanelImportInfo() {
		//@formatter:off
		this.panelImportInfo.setLayout(new GridBagLayout());
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		this.panelImportInfo.add(this.labelTitle,   new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5).setWeight(1, 0));
		this.panelImportInfo.add(pane,              new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraintsHelper.BOTH).setWeight(1, 1));
		this.panelImportInfo.add(this.buttonImport, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 5, 10).setWeight(1, 0));
		this.panelImportInfo.add(this.buttonClose,  new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5,0,5,5).setWeight(0, 0));
		this.labelTitle.setPreferredSize(new Dimension(200,30));
		pane.setBorder(null);
		pane.add(panelParams, new GridBagConstraintsHelper(0,0).setAnchor(GridBagConstraints.CENTER).setWeight(1,1).setFill(GridBagConstraints.BOTH));
		this.panelParams.setLayout(new GridBagLayout());
		this.panelParams.add(this.labelRemind, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER));
		this.buttonImport.setEnabled(false);
		//@formatter:on
	}

	private void initContentPane() {
		//@formatter:off
		this.contentPane.setLayout(new GridBagLayout());
		this.contentPane.add(this.panelFiles,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(0, 1));
		this.contentPane.add(this.panelImportInfo, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		//@formatter:on
	}

	public void initResources() {
		this.setTitle(DataConversionProperties.getString("String_FormImport_FormText"));
		this.buttonAddDir.setIcon(new ImageIcon(DataImportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Add.png")));
		this.buttonAddFile.setIcon(new ImageIcon(DataImportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_AddMap.png")));
		this.buttonSelectAll.setIcon(new ImageIcon(DataImportFrame.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		this.buttonInvertSelect.setIcon(new ImageIcon(DataImportFrame.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		this.buttonDelete.setIcon(new ImageIcon(DataImportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonImport.setText(CommonProperties.getString("String_Button_Import"));
		this.buttonClose.setText(CommonProperties.getString("String_Button_Close"));
		this.checkBoxAutoClose.setText(DataConversionProperties.getString("string_chcekbox_autoCloseIn"));
		this.labelTitle.setText(DataConversionProperties.getString("string_label_importData"));
		this.labelRemind.setText(DataConversionProperties.getString("string_label_addFileRemind"));
		this.buttonAddDir.setToolTipText(DataConversionProperties.getString("String_button_addDir"));
		this.buttonAddFile.setToolTipText(DataConversionProperties.getString("String_button_addFile"));
		this.buttonDelete.setToolTipText(DataConversionProperties.getString("string_button_delete"));
		this.buttonSelectAll.setToolTipText(DataConversionProperties.getString("string_button_selectAll"));
		this.buttonInvertSelect.setToolTipText(DataConversionProperties.getString("string_button_invertSelect"));
		this.labelRemind.setFont(new Font(DataConversionProperties.getString("string_font_song"), Font.PLAIN, 14));
	}

	// 通用的键盘响应事件
	private class CommonKeyAction extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if ((e.isControlDown() == true) && (e.getKeyCode() == KeyEvent.VK_A) && (1 <= table.getRowCount())) {
				// 键盘点击ctrl+A,全选
				CommonFunction.refreshPanelSelectedAll(panelImportInfo, fileInfos, panels, labelTitle);
			}
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				// 键盘点击delete,删除
				CommonFunction.deleteData(table, fileInfos, panels, model, panelImportInfo, labelTitle, panelParams);
				initComboBoxColumns();
				setButtonState();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
				// 键盘点击up/down刷新右边界面
				CommonFunction.refreshPanel(table, panelImportInfo, fileInfos, panels, labelTitle);
			}
			if ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_UP) || ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_DOWN))) {
				// 键盘点击shift+up/down刷新右边界面
				CommonFunction.refreshPanel(table, panelImportInfo, fileInfos, panels, labelTitle);
			}
		}

	}

	/**
	 * 设置按键状态
	 */
	private void setButtonState() {
		boolean hasImportInfo = false;
		if (0 < table.getRowCount()) {
			hasImportInfo = true;
		}
		if (hasImportInfo) {
			this.buttonImport.setEnabled(true);
			this.buttonDelete.setEnabled(true);
			this.buttonSelectAll.setEnabled(true);
			this.buttonInvertSelect.setEnabled(true);
		} else {
			this.buttonImport.setEnabled(false);
			this.buttonDelete.setEnabled(false);
			this.buttonSelectAll.setEnabled(false);
			this.buttonInvertSelect.setEnabled(false);
		}
	}

	// 通用的按钮响应事件
	private class CommonButtonAction implements ActionListener {
		private DataImportFrame dataImportFrame;

		public CommonButtonAction(DataImportFrame dataImportFrame) {
			this.dataImportFrame = dataImportFrame;
		}

		public CommonButtonAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			JComponent c = (JComponent) e.getSource();
			if (c == buttonAddFile) {
				// 添加
				CommonFunction.addData(dataImportFrame, fileInfos, panels, table, model, panelImportInfo, labelTitle);
				initComboBoxColumns();
				setButtonState();
				return;
			}
			if (c == buttonAddDir) {
				// 添加文件夹
				int x = (int) buttonAddDir.getLocation().getX()-buttonAddDir.getWidth();
				int y = buttonAddDir.getHeight();
				AddDirDialog addDirDialog = new AddDirDialog();
				addDirDialog.show(buttonAddDir, x, y);
				initComboBoxColumns();
				return;
			}
			if (c == buttonDelete) {
				// 删除
				CommonFunction.deleteData(table, fileInfos, panels, model, panelImportInfo, labelTitle, panelParams);
				initComboBoxColumns();
				setButtonState();
				return;
			}
			if (c == buttonSelectAll) {
				// 全选
				CommonFunction.selectedAll(fileInfos, panels, table, panelImportInfo, labelTitle);
				return;
			}
			if (c == buttonInvertSelect) {
				// 反选
				CommonFunction.selectInvert(table);
				int[] temp = table.getSelectedRows();
				if (0 == temp.length) {
					labelTitle.setText(DataConversionProperties.getString("string_label_importData"));
					CommonFunction.replace(panelImportInfo, panelParams);
				} else {
					CommonFunction.refreshPanel(table, panelImportInfo, fileInfos, panels, labelTitle);
				}
				return;
			}
			if (c == buttonClose) {
				// 关闭
				dispose();
				return;
			}
			if (c == buttonImport) {
				// 导入
				CommonFunction.importData(table, fileInfos);
				if (checkBoxAutoClose.isSelected()) {
					dispose();
				}
				return;
			}
		}

	}

	class TableMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (1 == e.getClickCount()) {
				// 设置表格的所有行可选
				table.setRowSelectionAllowed(true);
				if (!fileInfos.isEmpty()) {
					// 刷新右边界面
					CommonFunction.refreshPanel(table, panelImportInfo, fileInfos, panels, labelTitle);
				}
				// 如果没有选择行数据时，将右边界面替换为默认界面
				if (0 == table.getSelectedRowCount()) {
					CommonFunction.replace(panelImportInfo, panelParams);
				}
			}
			if (2 == e.getClickCount()) {
				ImportFileInfo fileInfo = fileInfos.get(table.getSelectedRow());
				JFileChooser fileChooser = new JFileChooser(fileInfo.getFilePath());
				if (fileInfo.getImportSetting() instanceof ImportSettingGJB) {
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.showOpenDialog(panelImportInfo);
				} else {
					fileChooser.showOpenDialog(panelImportInfo);
				}
			}
		}
	}

	class AddDirDialog extends JPopupMenu {

		private static final long serialVersionUID = 1L;
		JList<String> list = new JList<String>();

		public AddDirDialog() {
			initComponent();
		}

		private void initComponent() {
			this.setSize(150, 100);
			JScrollPane pane = new JScrollPane();
			this.setLayout(new GridBagLayout());
			this.add(pane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
			pane.setViewportView(list);
			pane.setBorder(null);
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			listModel.addElement(DataConversionProperties.getString("String_FormImportGJB_Text"));
			list.setModel(listModel);
			list.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!SmFileChoose.isModuleExist("DataImportFrame_ImportDirectories")) {
						SmFileChoose.addNewNode("", "", DataConversionProperties.getString("String_ScanDir"),
								"DataImportFrame_ImportDirectories", "GetDirectories");
					}
					SmFileChoose tempfileChooser = new SmFileChoose("DataImportFrame_ImportDirectories");
					tempfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int state = tempfileChooser.showDefaultDialog();
					if (state == JFileChooser.APPROVE_OPTION) {
						String directories = tempfileChooser.getFilePath();
						ImportFileInfo fileInfo = new ImportFileInfo();
						fileInfo.setImportSetting(new ImportSettingGJB());
						fileInfo.setFilePath(directories);
						fileInfo.setFileType("GJB5068");
						fileInfo.setFileName(directories);
						fileInfo.setState(DataConversionProperties.getString("string_change"));
						JPanel gjbPanel = new ImportPanelGJB(DataImportFrame.this, fileInfo);
						panels.add(gjbPanel);
						model.addRow(fileInfo);
						table.updateUI();
						if (table.getSelectedRows().length == 0) {
							table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
						}
						setButtonState();
						// 刷新右边界面
						CommonFunction.replacePanel(panelImportInfo, fileInfos, panels, labelTitle);
					}

				}
			});
		}
	}

	public JCheckBox getCheckBoxAutoClose() {
		return checkBoxAutoClose;
	}

	public void setCheckBoxAutoClose(JCheckBox checkBoxAutoClose) {
		this.checkBoxAutoClose = checkBoxAutoClose;
	}

	private void initDrag() {

		dropTargetTemper = new DropTarget(this, new TableDropTargetAdapter(this));

	}

	class TableDropTargetAdapter extends DropTargetAdapter {
		private DataImportFrame dataImportFrame;

		public TableDropTargetAdapter(DataImportFrame dataImportFrame) {
			this.dataImportFrame = dataImportFrame;
		}

		@Override
		public void drop(DropTargetDropEvent dtde) {
			try {
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);// 接收拖拽来的数据
					// 将文件添加到导入界面
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					CommonFunction.setImportFileInfos(files.toArray(new File[files.size()]), dataImportFrame, panels, model, "");
					initComboBoxColumns();
					if (0 < table.getRowCount()) {
						table.setRowSelectionInterval(0, table.getRowCount() - 1);
						CommonFunction.refreshPanel(table, panelImportInfo, fileInfos, panels, labelTitle);
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
	}

	class LocalWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			DataImportFrame.this.unRegistAcitonListener();
			if (null != panels) {
				for (int i = 0; i < panels.size(); i++) {
					if (panels.get(i) instanceof AbstractImportPanel) {
						((AbstractImportPanel) panels.get(i)).unregistActionListener();
					}
				}
			}
			DataImportFrame.this.fileInfos.clear();
		}
	}

	public DropTarget getDropTargetTemper() {
		return dropTargetTemper;
	}

	public JButton getButtonDelete() {
		return buttonDelete;
	}

	public JButton getButtonSelectAll() {
		return buttonSelectAll;
	}

	public JButton getButtonInvertSelect() {
		return buttonInvertSelect;
	}

	public JButton getButtonImport() {
		return buttonImport;
	}

}
