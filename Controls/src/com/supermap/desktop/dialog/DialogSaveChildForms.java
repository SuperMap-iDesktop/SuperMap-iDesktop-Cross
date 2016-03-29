package com.supermap.desktop.dialog;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;

public class DialogSaveChildForms extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<IForm> allForms;

	final JPanel contentPanel = new JPanel();
	private JToolBar toolBar;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonActive;
	private SmButton buttonRename;
	private MutiTable tableChildForms;
	private JCheckBox checkBoxSaveLayer3DKML;
	private SmButton buttonSave;
	private SmButton buttonUnSave;
	private SmButton buttonCancel;

	/**
	 * Create the dialog.
	 */
	public DialogSaveChildForms() {
		allForms = new ArrayList<IForm>();

		this.setModal(true);
		setTitle("Save");
		setBounds(100, 100, 554, 361);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		toolBar = new JToolBar();
		toolBar.setFloatable(false);

		tableChildForms = new MutiTable();

		FormInfoTableModel tableModel = new FormInfoTableModel();
		tableChildForms.setModel(tableModel);

		tableChildForms.setCheckHeaderColumn(0);
		tableChildForms.setEnabled(true);

		tableChildForms.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tableChildForms_valueChanged();
			}
		});

		tableChildForms.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableChildForms.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				stopEditing();
			}
		});

		JScrollPane scrollPaneTable = new JScrollPane(tableChildForms);
		scrollPaneTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				// 触发这个事件说明点到了表格区域的空白处，如果此时表格处于可编辑状态，那么提交一下编辑
				stopEditing();
			}
		});

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
				.addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPanel.createSequentialGroup().addGap(7)
						.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)));

		buttonSelectAll = new SmButton("SelectAll");
		buttonSelectAll.setIcon(new ImageIcon(DialogSaveChildForms.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectAll_Click();
			}
		});
		buttonSelectAll.setToolTipText("SelectAll");
		toolBar.add(buttonSelectAll);

		buttonSelectInvert = new SmButton("SelectInvert");
		buttonSelectInvert.setIcon(new ImageIcon(DialogSaveChildForms.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectInvert_Click();
			}
		});
		buttonSelectInvert.setToolTipText("SelectInvert");
		toolBar.add(buttonSelectInvert);
		addSeparator();
		buttonActive = new SmButton("Active");
		buttonActive.setIcon(new ImageIcon(DialogSaveChildForms.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Active.png")));
		buttonActive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonActive_Click();
			}
		});
		buttonActive.setToolTipText("Active");
		buttonActive.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(buttonActive);
		addSeparator();
		buttonRename = new SmButton("Rename");
		buttonRename.setIcon(new ImageIcon(DialogSaveChildForms.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Rename.png")));
		buttonRename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRename_Click();
			}
		});
		buttonRename.setToolTipText("ReName");
		toolBar.add(buttonRename);
		contentPanel.setLayout(gl_contentPanel);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		checkBoxSaveLayer3DKML = new JCheckBox("Is Save Layer3D KML");
		checkBoxSaveLayer3DKML.setVisible(false);
		checkBoxSaveLayer3DKML.setVerticalAlignment(SwingConstants.TOP);
		checkBoxSaveLayer3DKML.setHorizontalAlignment(SwingConstants.LEFT);

		buttonSave = new SmButton("Save");
		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSave_Click();
			}
		});
		getRootPane().setDefaultButton(buttonSave);
		buttonSave.setToolTipText("");
		buttonSave.setActionCommand("OK");

		buttonUnSave = new SmButton("UnSave");
		buttonUnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonUnSave_Click();
			}
		});
		buttonUnSave.setToolTipText("");
		buttonUnSave.setActionCommand("OK");

		buttonCancel = new SmButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonClose_Click();
			}
		});
		buttonCancel.setToolTipText("");
		buttonCancel.setActionCommand("Cancel");

		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(checkBoxSaveLayer3DKML)
						.addPreferredGap(ComponentPlacement.RELATED, 176, Short.MAX_VALUE).addComponent(buttonSave).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(buttonUnSave).addPreferredGap(ComponentPlacement.RELATED).addComponent(buttonCancel).addContainerGap()));
		gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_buttonPane
						.createSequentialGroup()
						.addGap(5)
						.addGroup(
								gl_buttonPane.createParallelGroup(Alignment.BASELINE, false).addComponent(buttonUnSave).addComponent(buttonSave)
										.addComponent(checkBoxSaveLayer3DKML).addComponent(buttonCancel))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		buttonPane.setLayout(gl_buttonPane);

		this.setLocationRelativeTo(null);
		initializeResources();
	}

	private void addSeparator() {
		JToolBar.Separator separator = new JToolBar.Separator();
		separator.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator);
	}

	private void initializeResources() {
		try {
			this.setTitle(CoreProperties.getString("String_Save"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
			this.buttonSave.setText(CoreProperties.getString("String_Save"));
			this.buttonUnSave.setText(CoreProperties.getString("String_FormSaveWindow_ButtonNotSave"));

			tableChildForms.getColumnModel().getColumn(FormInfoTableModel.NAME).setHeaderValue(CoreProperties.getString("String_Name"));
			tableChildForms.getColumnModel().getColumn(FormInfoTableModel.TYPE).setHeaderValue(CoreProperties.getString("String_DataType"));

			this.buttonActive.setText(CoreProperties.getString("String_FormSaveWindow_ToolStripButtonActive"));
			this.buttonRename.setText(ControlsProperties.getString("String_Rename"));
			this.buttonSelectAll.setText(CommonProperties.getString("String_ToolBar_SelectAll"));
			this.buttonSelectInvert.setText(CommonProperties.getString("String_ToolBar_SelectInverse"));
			this.checkBoxSaveLayer3DKML.setText(CoreProperties.getString("String_FormSaveWindow_SaveSceneLayer3DKML"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public IForm[] getAllForms() {
		return allForms.toArray(new IForm[allForms.size()]);
	}

	public void setAllForms(IForm[] forms) {
		allForms.clear();
		if (forms != null) {
			Object[][] datas = new Object[forms.length][2];
			for (int i = 0; i < forms.length; i++) {
				IForm form = forms[i];
				allForms.add(form);
				String type = CoreProperties.getString("String_Other");
				if (form instanceof IFormMap) {
					type = CoreProperties.getString("String_WorkspaceNodeMap");
				} else if (form instanceof IFormScene) {
					type = CoreProperties.getString("String_WorkspaceNodeScene");
				} else if (form instanceof IFormLayout) {
					type = CoreProperties.getString("String_WorkspaceNodeLayout");
				} else if (form instanceof IFormTabular) {
					type = CoreProperties.getString("String_WorkspaceNodeTabular");
				}
				datas[i] = new Object[] { true, form.getText(), type };
			}

			try {
				this.tableChildForms.refreshContents(datas);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}

			this.checkBoxSaveLayer3DKML.setEnabled(false);
		}
	}

	public boolean isSaveLayer3DKML() {
		return this.checkBoxSaveLayer3DKML.isSelected();
	}

	public IForm[] getSelectedForms() {
		ArrayList<IForm> selectedForms = new ArrayList<IForm>();
		try {
			for (int i = 0; i < tableChildForms.getRowCount(); i++) {
				boolean isChecked = Boolean.parseBoolean(tableChildForms.getModel().getValueAt(i, 0).toString());
				if (isChecked) {
					selectedForms.add(this.allForms.get(i));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return selectedForms.toArray(new IForm[selectedForms.size()]);
	}

	public IForm[] getUnselectedForms() {
		ArrayList<IForm> selectedForms = new ArrayList<IForm>();
		try {
			for (int i = 0; i < tableChildForms.getRowCount(); i++) {
				boolean isChecked = Boolean.parseBoolean(tableChildForms.getModel().getValueAt(i, 0).toString());
				if (!isChecked) {
					selectedForms.add(this.allForms.get(i));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return selectedForms.toArray(new IForm[selectedForms.size()]);
	}

	private void tableChildForms_valueChanged() {
		try {
			this.buttonActive.setEnabled(true);
			if (1 == tableChildForms.getSelectedRowCount()) {
				String type = CoreProperties.getString("String_Other");
				IForm form = Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
				if (form instanceof IFormMap) {
					type = CoreProperties.getString("String_WorkspaceNodeMap");
				} else if (form instanceof IFormScene) {
					type = CoreProperties.getString("String_WorkspaceNodeScene");
				} else if (form instanceof IFormLayout) {
					type = CoreProperties.getString("String_WorkspaceNodeLayout");
				} else if (form instanceof IFormTabular) {
					type = CoreProperties.getString("String_WorkspaceNodeTabular");
				}
				if (tableChildForms.getModel().getValueAt(tableChildForms.getSelectedRow(), 1).equals(form.getText())
						&& type.equals(tableChildForms.getModel().getValueAt(tableChildForms.getSelectedRow(), 2))) {
					this.buttonActive.setEnabled(false);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonSelectAll_Click() {
		try {

			// 全选之前提交一下编辑
			stopEditing();

			for (int i = 0; i < tableChildForms.getRowCount(); i++) {
				tableChildForms.getModel().setValueAt(true, i, tableChildForms.getCheckHeaderColumn());
			}
			tableChildForms.checkColumnHeader();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonSelectInvert_Click() {
		try {

			// 反选之前提交一下编辑
			stopEditing();

			for (int i = 0; i < tableChildForms.getRowCount(); i++) {
				Object value = tableChildForms.getModel().getValueAt(i, tableChildForms.getCheckHeaderColumn());
				boolean isChecked = Boolean.parseBoolean(value.toString());
				tableChildForms.getModel().setValueAt(!isChecked, i, tableChildForms.getCheckHeaderColumn());
			}
			tableChildForms.checkColumnHeader();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonActive_Click() {
		try {
			if (tableChildForms.getSelectedRowCount() > 0) {
				IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(tableChildForms.getSelectedRow());
				Application.getActiveApplication().getMainFrame().getFormManager().setActiveForm(form);
				this.buttonActive.setEnabled(false);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonRename_Click() {
		try {
			int selectedRow = this.tableChildForms.getSelectedRow();

			if (selectedRow > -1) {
				this.tableChildForms.editCellAt(selectedRow, FormInfoTableModel.NAME);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonSave_Click() {
		try {

			// 保存之前先提交一下编辑
			stopEditing();

			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			for (int i = 0; i < tableChildForms.getRowCount(); i++) {
				IForm form = formManager.get(i);
				String newName = tableChildForms.getModel().getValueAt(i, FormInfoTableModel.NAME).toString();
				if (form instanceof IFormMap) {
					((IFormMap) form).getMapControl().getMap().setName(newName);
				} else if (form instanceof IFormScene) {
					((IFormScene) form).getSceneControl().getScene().setName(newName);
				} else if (form instanceof IFormLayout) {
					((IFormLayout) form).getMapLayoutControl().getMapLayout().setName(newName);
				}
				form.setText(newName);
			}

			this.dispose();
			this.dialogResult = DialogResult.YES;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonUnSave_Click() {
		try {
			this.dispose();
			this.dialogResult = DialogResult.NO;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonClose_Click() {
		try {
			this.setVisible(false);
			this.dialogResult = DialogResult.CANCEL;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void stopEditing() {
		if (DialogSaveChildForms.this.tableChildForms.isEditing()) {
			DialogSaveChildForms.this.tableChildForms.getCellEditor().stopCellEditing();
		}
	}

	private class FormInfoTableModel extends MutiTableModel {

		public static final int CHECKED = 0; // 选择列
		public static final int NAME = 1; // 名称列
		public static final int TYPE = 2; // 类型列

		public FormInfoTableModel() {
			super(new String[] { "", "Form Name", "Form Type" });
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == NAME;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			if (col == NAME) {
				if (value instanceof String) {
					String newName = (String) value;

					if (isMapNameDuplicated(row, newName) || isNewMapNameDuplicated(row, newName)) {
						Application.getActiveApplication().getOutput()
								.output(MessageFormat.format(CoreProperties.getString("String_RenameMap_Failed"), newName));
					} else {
						((Vector) contents.get(row)).set(col, value);
						this.fireTableCellUpdated(row, col);
					}
				}
			} else {
				((Vector) contents.get(row)).set(col, value);
				this.fireTableCellUpdated(row, col);
			}
		}

		/**
		 * 与工作空间中已存在的地图对比，地图名是否重复
		 *
		 * @param newName
		 * @return
		 */
		private boolean isMapNameDuplicated(int row, String newName) {
			boolean result = false;

			if (!StringUtilties.isNullOrEmpty(newName)) {

				// 判断工作空间中的已有地图是否有重复的命名
				result = Application.getActiveApplication().getWorkspace().getMaps().indexOf(newName) > -1;

				// @formatter:off
				// 如果存在重复的命名，则需要考虑当前正在修改的地图是否是新打开的地图，
				// 如果当前正在修改地图名的地图窗口不是新打开的地图，那么就需要过滤掉自己，
				// 来判断是否与工作空间中已打开的地图重名。
				// 如果已经确认与已有地图重名，那么就判断是否与当前正修改的地图窗口名一致，
				// 如果一致，则非重名，如果不一致，即是与其他窗口重名
				if (result) {
					String oldName=DialogSaveChildForms.this.allForms.get(row).getText();
					result= !oldName.equalsIgnoreCase(newName);
				}
			}
			return result;
		}

		/**
		 * 与即将保存的地图对比，地图名是否重复
		 * 
		 * @param row
		 * @param newName
		 * @return
		 */
		private boolean isNewMapNameDuplicated(int row, String newName) {
			boolean result = false;

			if (!StringUtilties.isNullOrEmpty(newName) && row > -1) {
				for (int i = 0; i < this.contents.size(); i++) {
					if (row != i) {
						String mapName = (String) getValueAt(i, NAME);

						if (newName.equalsIgnoreCase(mapName)) {
							result = true;
							break;
						}
					}
				}
			}
			return result;
		}
	}
}
