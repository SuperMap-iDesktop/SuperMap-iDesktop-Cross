package com.supermap.desktop.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.LayoutStyle.ComponentPlacement;
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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.action.CommonMouseListener;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
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
		initCompanent();
		initDrag();
	}

	public DataImportFrame(JDialog owner, boolean modal) {
		super(owner, modal);
	}

	/**
	 * 
	 */
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
	private JLabel lblRemind = new JLabel("string_label_addFileRemind");
	private JPanel panelParams = new JPanel();
	private JCheckBox checkBoxAutoClose = new JCheckBox("string_chcekbox_autoCloseIn");
	private DropTarget dropTargetTemper;

	/**
	 * Create the frame.
	 */
	public DataImportFrame() {
		initResources();
		initCompanent();
	}

	public void initCompanent() {
		setLocationByPlatform(true);
		lblRemind.setForeground(new Color(32, 178, 170));
		lblRemind.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setBackground(Color.LIGHT_GRAY);
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setOpaque(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 280, 845, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setAutoCreateGaps(true);
		gl_contentPane.setAutoCreateContainerGaps(true);

		// @formatter:off
				gl_contentPane.setHorizontalGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panelFiles, 300, 300, 300)
								.addComponent(checkBoxAutoClose))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(labelTitle,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panelParams)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(buttonImport)
										.addComponent(buttonClose))));
				buttonImport.setEnabled(false);
				
				gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup()
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(panelFiles)
								.addComponent(checkBoxAutoClose))
						.addGroup(gl_contentPane.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(labelTitle,GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panelParams)
								.addGroup(gl_contentPane.createParallelGroup()
										.addComponent(buttonImport)
										.addComponent(buttonClose))));
				// @formatter:on

		GroupLayout gl_newPanel = new GroupLayout(panelParams);
		gl_newPanel.setHorizontalGroup(gl_newPanel.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_newPanel.createSequentialGroup().addContainerGap(143, Short.MAX_VALUE)
						.addComponent(lblRemind, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE).addGap(119)));
		gl_newPanel.setVerticalGroup(gl_newPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_newPanel.createSequentialGroup().addGap(118).addComponent(lblRemind, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(164, Short.MAX_VALUE)));
		panelParams.setLayout(gl_newPanel);

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		JScrollPane scrollPane = new JScrollPane();
		BorderLayout gl_panel = new BorderLayout();
		panelFiles.setLayout(gl_panel);
		panelFiles.add(toolBar, BorderLayout.NORTH);
		panelFiles.add(scrollPane, BorderLayout.CENTER);

		toolBar.add(buttonAddFile);
		buttonDelete.setEnabled(false);

		toolBar.add(buttonDelete);
		buttonSelectAll.setEnabled(false);

		toolBar.add(buttonSelectAll);
		buttonInvertSelect.setEnabled(false);
		checkBoxAutoClose.setSelected(true);
		toolBar.add(buttonInvertSelect);

		// 为btnNewButton，btnNewButton_1,btnNewButton_2,btnNewButton_3,btnNewButton_4添加事件响应

		buttonAddFile.addActionListener(new CommonButtonAction(this));
		buttonDelete.addActionListener(new CommonButtonAction());
		buttonSelectAll.addActionListener(new CommonButtonAction());
		buttonInvertSelect.addActionListener(new CommonButtonAction());
		buttonImport.addActionListener(new CommonButtonAction());
		buttonClose.addActionListener(new CommonButtonAction());

		table = new JTable();
		fileInfos = new ArrayList<ImportFileInfo>();
		panels = new ArrayList<JPanel>();
		model = new FileInfoModel(fileInfos);
		table = new JTable();
		table.setModel(model);
		scrollPane.setViewportView(table);
		// 为scrollPane和table添加事件响应
		scrollPane.addMouseListener(new CommonMouseListener(this, panelParams, table, contentPane, fileInfos, panels, labelTitle, model));
		table.addMouseListener(new CommonMouseListener(this, panelParams, table, contentPane, fileInfos, panels, labelTitle, model));
		table.addKeyListener(new CommonKeyAction());
		contentPane.setLayout(gl_contentPane);
	}

	public void initResources() {
		setTitle(DataConversionProperties.getString("String_FormImport_FormText"));
		buttonAddFile.setIcon(new ImageIcon(DataImportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_AddMap.png")));
		buttonSelectAll.setIcon(new ImageIcon(DataImportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		buttonInvertSelect.setIcon(new ImageIcon(DataImportFrame.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		buttonDelete.setIcon(new ImageIcon(DataImportFrame.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonImport.setText(CommonProperties.getString("String_Button_Import"));
		this.buttonClose.setText(CommonProperties.getString("String_Button_Close"));
		this.checkBoxAutoClose.setText(DataConversionProperties.getString("string_chcekbox_autoCloseIn"));
		this.labelTitle.setText(DataConversionProperties.getString("string_label_importData"));
		this.lblRemind.setText(DataConversionProperties.getString("string_label_addFileRemind"));
		this.buttonAddFile.setToolTipText(DataConversionProperties.getString("string_button_add"));
		this.buttonDelete.setToolTipText(DataConversionProperties.getString("string_button_delete"));
		this.buttonSelectAll.setToolTipText(DataConversionProperties.getString("string_button_selectAll"));
		this.buttonInvertSelect.setToolTipText(DataConversionProperties.getString("string_button_invertSelect"));
		lblRemind.setFont(new Font(DataConversionProperties.getString("string_font_song"), Font.PLAIN, 14));
	}

	// 通用的键盘响应事件
	private class CommonKeyAction extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if ((e.isControlDown() == true) && (e.getKeyCode() == KeyEvent.VK_A) && (1 <= table.getRowCount())) {
				// 键盘点击ctrl+A,全选
				CommonFunction.refreshPanelSelectedAll(contentPane, fileInfos, panels, labelTitle);
			}
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				// 键盘点击delete,删除
				CommonFunction.deleteData(table, fileInfos, panels, model, contentPane, labelTitle, panelParams);
				setButtonState();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
				// 键盘点击up/down刷新右边界面
				CommonFunction.refreshPanel(table, contentPane, fileInfos, panels, labelTitle);
			}
			if ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_UP) || ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_DOWN))) {
				// 键盘点击shift+up/down刷新右边界面
				CommonFunction.refreshPanel(table, contentPane, fileInfos, panels, labelTitle);
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
				CommonFunction.addData(dataImportFrame, fileInfos, panels, table, model, contentPane, labelTitle);
				setButtonState();
			} else if (c == buttonDelete) {
				// 删除
				CommonFunction.deleteData(table, fileInfos, panels, model, contentPane, labelTitle, panelParams);
				setButtonState();
			} else if (c == buttonSelectAll) {
				// 全选
				CommonFunction.selectedAll(fileInfos, panels, table, contentPane, labelTitle);
			} else if (c == buttonInvertSelect) {
				// 反选
				CommonFunction.selectInvert(table);
				int[] temp = table.getSelectedRows();
				if (0 == temp.length) {
					JPanel panel = CommonFunction.getRightPanel(contentPane);
					GroupLayout layout = (GroupLayout) contentPane.getLayout();
					layout.replace(panel, panelParams);
				} else {
					CommonFunction.refreshPanel(table, contentPane, fileInfos, panels, labelTitle);
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
						CommonFunction.refreshPanel(table, contentPane, fileInfos, panels, labelTitle);
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
	}

	public DropTarget getDropTargetTemper() {
		return dropTargetTemper;
	}

	public void setDropTargetTemper(DropTarget dropTargetTemper) {
		this.dropTargetTemper = dropTargetTemper;
	}

	public JButton getButtonDelete() {
		return buttonDelete;
	}

	public void setButtonDelete(JButton buttonDelete) {
		this.buttonDelete = buttonDelete;
	}

	public JButton getButtonSelectAll() {
		return buttonSelectAll;
	}

	public void setButtonSelectAll(JButton buttonSelectAll) {
		this.buttonSelectAll = buttonSelectAll;
	}

	public JButton getButtonInvertSelect() {
		return buttonInvertSelect;
	}

	public void setButtonInvertSelect(JButton buttonInvertSelect) {
		this.buttonInvertSelect = buttonInvertSelect;
	}

	public JButton getButtonImport() {
		return buttonImport;
	}

	public void setButtonImport(JButton buttonImport) {
		this.buttonImport = buttonImport;
	}

}
