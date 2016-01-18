package com.supermap.desktop.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.action.CommonMouseListener;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.FileInfoModel;

import javax.swing.JCheckBox;

/**
 * @author Administrator 数据导入主体界面
 */
public class DataImportFrame extends SmDialog {
	public DataImportFrame(JFrame owner, boolean modal) {
		super(owner, modal);
		initResources();
		initComponents();
		registActionListener();
		initDrag();
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
	private JButton buttonDelete = new JButton();
	private JButton buttonSelectAll = new JButton();
	private JButton buttonInvertSelect = new JButton();
	private JButton buttonImport = new JButton("import");
	private JButton buttonClose = new JButton("string_button_close");
	private JPanel panelFiles = new JPanel();
	private JLabel labelTitle = new JLabel("string_label_importData");
	private JLabel labelRemind = new JLabel("string_label_addFileRemind");
	private JPanel panelParams = new JPanel();
	private JPanel panelImportInfo = new JPanel();
	private JCheckBox checkBoxAutoClose = new JCheckBox("string_chcekbox_autoCloseIn");
	private JToolBar toolBar = new JToolBar();
	private DropTarget dropTargetTemper;
	private transient CommonButtonAction buttonAction = new CommonButtonAction();
	private transient LocalWindowListener windowListener = new LocalWindowListener();

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.buttonAddFile.addActionListener(new CommonButtonAction(this));
		this.buttonDelete.addActionListener(this.buttonAction);
		this.buttonSelectAll.addActionListener(this.buttonAction);
		this.buttonInvertSelect.addActionListener(this.buttonAction);
		this.buttonImport.addActionListener(this.buttonAction);
		this.buttonClose.addActionListener(this.buttonAction);
		this.addWindowListener(this.windowListener);
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
	}

	public void initComponents() {
		this.setLocationByPlatform(true);
		this.labelRemind.setForeground(new Color(32, 178, 170));
		this.labelRemind.setHorizontalAlignment(SwingConstants.CENTER);
		this.labelTitle.setBackground(Color.LIGHT_GRAY);
		this.labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.labelTitle.setOpaque(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(400, 280, 800, 475);
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
		JScrollPane scrollPane = new JScrollPane();
		initToolBar();
		//@formatter:off
		this.panelFiles.setLayout(new GridBagLayout());
		this.panelFiles.add(this.toolBar,           new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(0, 0));
		this.panelFiles.add(scrollPane,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,5,5,5).setWeight(3, 3).setFill(GridBagConstraints.BOTH));
		this.panelFiles.add(this.checkBoxAutoClose, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,5,5,5).setWeight(0, 0));
		//@formatter:on
		// 为scrollPane和table添加事件响应
		this.table = new JTable();
		this.fileInfos = new ArrayList<ImportFileInfo>();
		this.panels = new ArrayList<JPanel>();
		this.model = new FileInfoModel(this.fileInfos);
		this.table = new JTable();
		this.table.setModel(this.model);
		scrollPane.setViewportView(this.table);
		this.table.addKeyListener(new CommonKeyAction());
		scrollPane.addMouseListener(new CommonMouseListener(this, panelParams, table, panelImportInfo, fileInfos, panels, labelTitle, model));
		this.table.addMouseListener(new CommonMouseListener(this, panelParams, table, panelImportInfo, fileInfos, panels, labelTitle, model));
	}

	private void initToolBar() {
		this.toolBar.add(this.buttonAddFile);
		this.buttonDelete.setEnabled(false);
		this.toolBar.add(this.buttonDelete);
		this.buttonSelectAll.setEnabled(false);
		this.toolBar.add(this.buttonSelectAll);
		this.toolBar.add(this.buttonInvertSelect);
	}

	private void initPanelImportInfo() {
		//@formatter:off
		this.panelImportInfo.setLayout(new GridBagLayout());
		this.panelImportInfo.add(this.labelTitle,  new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5).setWeight(1, 0));
		this.panelImportInfo.add(this.panelParams, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraintsHelper.BOTH).setWeight(1, 3));
		this.panelImportInfo.add(this.buttonImport, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 5, 10).setWeight(100, 0));
		this.panelImportInfo.add(this.buttonClose,  new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5,0,5,5).setWeight(0, 0));
		this.labelTitle.setPreferredSize(new Dimension(200,23));
		this.panelParams.setPreferredSize(new Dimension(483,300));
		this.panelParams.setLayout(new GridBagLayout());
		this.panelParams.add(this.labelRemind, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER));
		//@formatter:on
	}

	private void initContentPane() {
		//@formatter:off
		this.contentPane.setLayout(new GridBagLayout());
		this.contentPane.add(this.panelFiles, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(8, 1));
		this.contentPane.add(this.panelImportInfo, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(92, 1));
		//@formatter:on
	}

	public void initResources() {
		this.setTitle(DataConversionProperties.getString("String_FormImport_FormText"));
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
		this.buttonAddFile.setToolTipText(DataConversionProperties.getString("string_button_add"));
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
			buttonImport.setEnabled(true);
			buttonDelete.setEnabled(true);
			buttonSelectAll.setEnabled(true);
			buttonInvertSelect.setEnabled(true);
		} else {
			buttonImport.setEnabled(false);
			buttonDelete.setEnabled(false);
			buttonSelectAll.setEnabled(false);
			buttonInvertSelect.setEnabled(false);
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
				setButtonState();
			} else if (c == buttonDelete) {
				// 删除
				CommonFunction.deleteData(table, fileInfos, panels, model, panelImportInfo, labelTitle, panelParams);
				setButtonState();
			} else if (c == buttonSelectAll) {
				// 全选
				CommonFunction.selectedAll(fileInfos, panels, table, panelImportInfo, labelTitle);
			} else if (c == buttonInvertSelect) {
				// 反选
				CommonFunction.selectInvert(table);
				int[] temp = table.getSelectedRows();
				if (0 == temp.length) {
					labelTitle.setText(DataConversionProperties.getString("string_label_importData"));
					CommonFunction.replace(panelImportInfo, panelParams);
				} else {
					CommonFunction.refreshPanel(table, panelImportInfo, fileInfos, panels, labelTitle);
				}
			} else if (c == buttonClose) {
				// 关闭
				dispose();
			} else if (c == buttonImport) {
				// 导入
				CommonFunction.importData(table, fileInfos);
				if (checkBoxAutoClose.isSelected()) {
					dispose();
				}
			}
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
