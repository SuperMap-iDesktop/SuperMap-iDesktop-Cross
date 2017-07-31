package com.supermap.desktop.dialog.ColorSchemeDialogs;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorScheme;
import com.supermap.desktop.controls.colorScheme.ColorSchemeEditorDialog;
import com.supermap.desktop.controls.colorScheme.ColorSchemeManager;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.JTreeUIUtilities;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class JDialogColorScheme extends SmDialog {

	private JToolBar toolBar;
	private SmButton buttonAddDirectory;
	private SmButton buttonAdd;
	private SmButton buttonEdit;
	private SmButton buttonDel;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonImport;
	private SmButton buttonExport;
	private JTableColorScheme tableColorScheme;
	private JPanel panelButton;
	private SmButton buttonOk;
	private SmButton buttonCancel;
	//存放删除的颜色方案，确定时删除
	private java.util.List<ColorScheme> deletedList;
	private JMenuItem menuItemExport = new JMenuItem(CommonProperties.getString(CommonProperties.EXPORT));
	private JMenuItem menuItemFavorite = new JMenuItem(CoreProperties.getString("String_Favorite"));

	private JScrollPane scrollPane = new JScrollPane();
	private JTree tree = new JTree();
	private JSplitPane splitPane = new JSplitPane();
	private boolean isModified = false;


	public JDialogColorScheme() {
		initComponents();
		initLayout();
		initListeners();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		toolBar = new JToolBar();
		buttonAddDirectory = new SmButton();
		buttonAdd = new SmButton();
		buttonEdit = new SmButton();
		buttonDel = new SmButton();
		buttonSelectAll = new SmButton();
		buttonSelectInvert = new SmButton();
		buttonImport = new SmButton();
		buttonExport = new SmButton();
		tableColorScheme = new JTableColorScheme();
		panelButton = new JPanel();
		buttonOk = new SmButton();
		buttonCancel = new SmButton();
		this.componentList.add(buttonOk);
		this.componentList.add(buttonCancel);
		deletedList = new ArrayList<>();

		tree.setEditable(false);
		scrollPane.setViewportView(tree);
		splitPane.setLeftComponent(scrollPane);
		splitPane.setRightComponent(new JScrollPane(tableColorScheme));
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);


		this.getRootPane().setDefaultButton(buttonOk);
		this.setSize(new Dimension(900, 515));
		this.setLocationRelativeTo(null);
	}

	private void initLayout() {
		initToolBarLayout();
		initPanelButton();
		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 10));
		this.add(splitPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));
		this.add(panelButton, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initPanelButton() {
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 10, 5));
		panelButton.add(buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(5, 0, 10, 10));
	}

	private void initToolBarLayout() {
		toolBar.add(buttonAddDirectory);
		toolBar.add(buttonAdd);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonEdit);
//		toolBar.add(buttonRevert);
		toolBar.add(buttonDel);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonImport);
		toolBar.add(buttonExport);
		toolBar.setFloatable(false);

		buttonAddDirectory.setFocusable(false);
		buttonAdd.setFocusable(false);
		buttonEdit.setFocusable(false);
		buttonDel.setFocusable(false);
		buttonSelectAll.setFocusable(false);
		buttonSelectInvert.setFocusable(false);
		buttonImport.setFocusable(false);
		buttonExport.setFocusable(false);
	}

	private void initListeners() {
		this.buttonAddDirectory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogAddDirectory addDirectoryDialog = getAddDirectoryDialog();
				if (addDirectoryDialog.showDialog() == DialogResult.OK) {
					String name = addDirectoryDialog.getNodeName();
					ColorSchemeTreeNode userDefineNode = (ColorSchemeTreeNode) ((ColorSchemeTreeNode) tree.getModel().getRoot()).getChildAt(1);
					ColorSchemeTreeNode child = new ColorSchemeTreeNode(userDefineNode);
					child.setName(name);
					userDefineNode.addChild(child);
					((DefaultTreeModel) tree.getModel()).nodesWereInserted(userDefineNode, new int[]{userDefineNode.getChildCount() - 1});
					tree.expandPath(new TreePath(userDefineNode.getPath()));
					isModified = true;
				}
				addDirectoryDialog.dispose();
			}
		});
		this.buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(tableColorScheme);
				ColorSchemeEditorDialog colorSchemeEditorDialog = new ColorSchemeEditorDialog();
				ColorSchemeTreeNode lastNode = (ColorSchemeTreeNode) tree.getLastSelectedPathComponent();
				ArrayList<String> names = new ArrayList<>();
				for (ColorScheme colorScheme : lastNode.getColorSchemes()) {
					names.add(colorScheme.getName());
				}
				colorSchemeEditorDialog.setExitNames(names);
				DialogResult dialogResult = colorSchemeEditorDialog.showDialog();
				if (dialogResult == DialogResult.OK) {
					ColorScheme colorScheme = colorSchemeEditorDialog.getColorScheme();
					colorScheme.setParentNode(lastNode);
					tableColorScheme.addColorScheme(colorScheme);
					tableColorScheme.setRowSelectionInterval(tableColorScheme.getRowCount() - 1, tableColorScheme.getRowCount() - 1);
					tableColorScheme.scrollRectToVisible(tableColorScheme.getCellRect(tableColorScheme.getRowCount() - 1, 0, true));
					isModified = true;
				}
				colorSchemeEditorDialog.dispose();
			}
		});

		this.buttonEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(tableColorScheme);
				int selectedRow = tableColorScheme.getSelectedRow();
				editColorSchemeAtRow(selectedRow);
			}
		});


		this.buttonDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isModified = true;
				TableUtilities.stopEditing(tableColorScheme);
				int[] selectedRows = tableColorScheme.getSelectedRows();

				ColorSchemeTreeNode lastSelectedPathComponent = (ColorSchemeTreeNode) tree.getLastSelectedPathComponent();
				boolean isHasFavorites = false;
				ArrayList<ColorScheme> removeFromParentNode = new ArrayList<>();

				if (lastSelectedPathComponent == ((ColorSchemeTreeNode) tree.getModel().getRoot()).getChildAt(2) || lastSelectedPathComponent.getParent() == ((ColorSchemeTreeNode) tree.getModel().getRoot()).getChildAt(2)) {
					// 从收藏删除
					isHasFavorites = true;
					for (int selectedRow : selectedRows) {
						tableColorScheme.getColorScheme(selectedRow).setFavorite(false);
					}
				} else {
					for (int selectedRow : selectedRows) {
						ColorScheme colorScheme = tableColorScheme.getColorScheme(selectedRow);
						deletedList.add(colorScheme);
						removeFromParentNode.add(colorScheme);
						if (colorScheme.isFavorite()) {
							isHasFavorites = true;
						}
					}

				}
				tableColorScheme.deleteSelectedRow();
				if (removeFromParentNode.size() > 0) {
					for (ColorScheme colorScheme : removeFromParentNode) {
						colorScheme.getParentNode().getColorSchemes().remove(colorScheme);
					}
				}
				if (selectedRows[0] < tableColorScheme.getRowCount()) {
					tableColorScheme.setRowSelectionInterval(selectedRows[0], selectedRows[0]);
				} else if (tableColorScheme.getRowCount() > 0) {
					tableColorScheme.setRowSelectionInterval(tableColorScheme.getRowCount() - 1, tableColorScheme.getRowCount() - 1);
				}
				if (isHasFavorites) {
					// 有收藏要删除
					ColorSchemeTreeNode favoriteNode = (ColorSchemeTreeNode) ((ColorSchemeTreeNode) tree.getModel().getRoot()).getChildAt(2);
					for (int childCount = favoriteNode.getChildCount() - 1; childCount >= 0; childCount--) {
						ColorSchemeTreeNode childNode = (ColorSchemeTreeNode) favoriteNode.getChildAt(childCount);
						if (childNode.getColorSchemes() == null || childNode.getColorSchemes().size() == 0) {
							TreeNode parent = childNode.getParent();
							((ColorSchemeTreeNode) parent).removeChild(childNode);
							((DefaultTreeModel) tree.getModel()).nodesWereRemoved(parent, new int[]{childCount}, new Object[]{childNode});
						}
					}
				}

			}
		});

		this.buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(tableColorScheme);
				if (tableColorScheme.getRowCount() > 0) {
					tableColorScheme.setRowSelectionInterval(0, tableColorScheme.getRowCount() - 1);
				}
			}
		});

		this.buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(tableColorScheme);
				if (tableColorScheme.getRowCount() > 0) {
					TableUtilities.invertSelection(tableColorScheme);
				}
			}
		});

		this.buttonImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(tableColorScheme);
				buttonImportClicked();
			}
		});

		this.buttonExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(tableColorScheme);
				buttonExportClicked();

			}
		});

		this.tableColorScheme.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				TableUtilities.stopEditing(tableColorScheme);
				checkButtonState();
			}
		});

		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				dispose();
			}
		});

		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				dispose();
			}
		});
		this.tableColorScheme.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				checkButtonState();
			}
		});

		this.tableColorScheme.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowAtPoint = tableColorScheme.rowAtPoint(e.getPoint());
				if (rowAtPoint == -1) {
					return;
				}
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1
						&& tableColorScheme.columnAtPoint(e.getPoint()) == ColorSchemeTableModel.COLUMN_COLOR_RAMP) {
					editColorSchemeAtRow(tableColorScheme.getSelectedRow());
					return;
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (!tableColorScheme.isRowSelected(rowAtPoint)) {
						tableColorScheme.setRowSelectionInterval(rowAtPoint, rowAtPoint);
					}
					getPopupMenu().show(tableColorScheme, e.getX(), e.getY());
				}
			}
		});
		this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (e.getNewLeadSelectionPath() == null) {
					tableColorScheme.setColorSchemeList(null);
					return;
				}
				ColorSchemeTreeNode lastPathComponent = ((ColorSchemeTreeNode) e.getNewLeadSelectionPath().getLastPathComponent());
				if (lastPathComponent != null) {
					tableColorScheme.setColorSchemeList(lastPathComponent.getColorSchemes());
				} else {
					tableColorScheme.setColorSchemeList(null);
				}
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		menuItemExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonExportClicked();
			}
		});

		menuItemFavorite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ColorSchemeTreeNode lastSelectedPathComponent = ((ColorSchemeTreeNode) tree.getLastSelectedPathComponent());
				if (lastSelectedPathComponent != null && lastSelectedPathComponent.getParent() != null && CoreProperties.getString("String_MyFavorites").equals(((ColorSchemeTreeNode) lastSelectedPathComponent.getParent()).getName())) {
					// 收藏已经在收藏里面的颜色方案时直接返回
					return;
				}
				isModified = true;

				ColorSchemeTreeNode favoriteNode = (ColorSchemeTreeNode) ((ColorSchemeTreeNode) tree.getModel().getRoot()).getChildAt(2);
				for (int i : tableColorScheme.getSelectedRows()) {
					ColorScheme colorScheme = tableColorScheme.getColorScheme(i);
					int childCount = favoriteNode.getChildCount();
					favoriteNode.getChild(colorScheme.getParentNode().getName());
					colorScheme.setFavorite(true);
					if (favoriteNode.getChildCount() != childCount) {
						((DefaultTreeModel) tree.getModel()).nodesWereInserted(favoriteNode, new int[]{favoriteNode.getChildCount() - 1});
					}
					tree.expandPath(new TreePath(favoriteNode.getPath()));
				}
			}
		});
	}

	private JPopupMenu getPopupMenu() {
		ColorSchemeTreeNode root = (ColorSchemeTreeNode) tree.getModel().getRoot();
		ColorSchemeTreeNode lastSelectedPathComponent = (ColorSchemeTreeNode) tree.getLastSelectedPathComponent();
		JPopupMenu jPopupMenu = new JPopupMenu();
		JMenu copyTo = new JMenu(CoreProperties.getString("String_CopyTo"));
		jPopupMenu.add(copyTo);
		JMenu defaultNode = new JMenu(CoreProperties.getString("String_Default"));
		copyTo.add(defaultNode);
		ColorSchemeTreeNode defaultTreeNode = (ColorSchemeTreeNode) root.getChildAt(0);
		for (int i = 0; i < defaultTreeNode.getChildCount(); i++) {
			ColorSchemeTreeNode childAt = (ColorSchemeTreeNode) defaultTreeNode.getChildAt(i);
			MyMenuItem myMenuItem = new MyMenuItem(childAt == lastSelectedPathComponent ? childAt.getShowName() + CoreProperties.getString("String_current") : childAt.getShowName());
			myMenuItem.setColorSchemeTreeNode(childAt);
			defaultNode.add(myMenuItem);
		}

		String userDefineNode = CoreProperties.getString("String_UserDefine");
		JMenu userDefineMenu = new JMenu(userDefineNode);
		copyTo.add(userDefineMenu);
		ColorSchemeTreeNode userDefineTreeNode = (ColorSchemeTreeNode) root.getChildAt(1);
		for (int i = 0; i < userDefineTreeNode.getChildCount(); i++) {
			ColorSchemeTreeNode childAt = (ColorSchemeTreeNode) userDefineTreeNode.getChildAt(i);
			MyMenuItem myMenuItem = new MyMenuItem(childAt == lastSelectedPathComponent ? childAt.getShowName() + CoreProperties.getString("String_current") : childAt.getShowName());
			myMenuItem.setColorSchemeTreeNode(childAt);
			userDefineMenu.add(myMenuItem);
		}

		jPopupMenu.add(menuItemExport);
		if (lastSelectedPathComponent != ((ColorSchemeTreeNode) tree.getModel().getRoot()).getChildAt(2) && lastSelectedPathComponent.getParent() != ((ColorSchemeTreeNode) tree.getModel().getRoot()).getChildAt(2)) {
			jPopupMenu.add(menuItemFavorite);
		}
		return jPopupMenu;
	}

	/**
	 * 询问是否保存
	 *
	 * @return 是/否 -> true  关闭->false
	 */
	private boolean askSave() {
		if (isModified) {
//			int result = UICommonToolkit.showConfirmDialogYesNo(ControlsProperties.getString("String_ColorSchemeHadModified"));
			int result = UICommonToolkit.showConfirmDialogWithCancel(ControlsProperties.getString("String_ColorSchemeHadModified"));
			if (result == JOptionPane.YES_OPTION) {
				save();
			} else if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}

	private void save() {
		for (ColorScheme colorScheme : deletedList) {
			colorScheme.delete();
		}
		ColorSchemeManager.getColorSchemeManager().setRootTreeNode(((ColorSchemeTreeNode) tree.getModel().getRoot()));
		dialogResult = DialogResult.OK;
		isModified = false;
	}

	private void editColorSchemeAtRow(int selectedRow) {
		ColorScheme colorSchemeClone = tableColorScheme.getColorScheme(selectedRow).clone();
		ColorSchemeEditorDialog colorSchemeEditorDialog = new ColorSchemeEditorDialog(colorSchemeClone);
		ArrayList<String> names = new ArrayList<>();
		ColorSchemeTreeNode lastNode = (ColorSchemeTreeNode) tree.getLastSelectedPathComponent();
		for (ColorScheme colorScheme : lastNode.getColorSchemes()) {
			names.add(colorScheme.getName());
		}
		names.remove(colorSchemeClone.getName());
		colorSchemeEditorDialog.setExitNames(names);
		if (colorSchemeEditorDialog.showDialog() == DialogResult.OK) {
			isModified = true;
			tableColorScheme.setColorSchemeAtRow(selectedRow, colorSchemeEditorDialog.getColorScheme());
			tableColorScheme.setRowSelectionInterval(selectedRow, selectedRow);
		}
		colorSchemeEditorDialog.dispose();
	}

	private void buttonImportClicked() {
		if (!SmFileChoose.isModuleExist("ColorSchemeImport")) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_ColorSchemeSaveFileFilter"), "scs", "SCS");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_ImportColorScheme"), "ColorSchemeImport", "OpenMany");
		}
		SmFileChoose fileChooser = new SmFileChoose("ColorSchemeImport");
		int result = fileChooser.showDefaultDialog();
		File[] selectFiles = fileChooser.getSelectFiles();
		int rowCount = tableColorScheme.getRowCount();
		if (result == JFileChooser.APPROVE_OPTION && selectFiles != null && selectFiles.length > 0) {
			isModified = true;
			for (File selectFile : selectFiles) {
				ColorScheme colorScheme = new ColorScheme();
				if (colorScheme.fromXML(selectFile, true)) {
					ColorSchemeTreeNode lastSelectedPathComponent = (ColorSchemeTreeNode) tree.getLastSelectedPathComponent();
					colorScheme.setParentNode(lastSelectedPathComponent);
					lastSelectedPathComponent.addColorScheme(colorScheme);
					((DefaultTableModel) tableColorScheme.getModel()).fireTableDataChanged();
//					tableColorScheme.addColorScheme(colorScheme);
				}
			}
		}
		if (tableColorScheme.getRowCount() > rowCount) {
			tableColorScheme.setRowSelectionInterval(rowCount, tableColorScheme.getRowCount() - 1);
			tableColorScheme.scrollRectToVisible(tableColorScheme.getCellRect(rowCount, 0, true));
		}
	}

	private void buttonExportClicked() {
		if (!SmFileChoose.isModuleExist("ColorSchemeExport")) {

			SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_ExportColorScheme"), "ColorSchemeExport", "GetDirectories");
		}
		SmFileChoose fileChooser = new SmFileChoose("ColorSchemeExport");
		int result = fileChooser.showDefaultDialog();
		String directories = fileChooser.getFilePath();
		if (result == JFileChooser.APPROVE_OPTION && !StringUtilities.isNullOrEmpty(directories)) {
			for (int i : tableColorScheme.getSelectedRows()) {
				ColorScheme colorScheme = tableColorScheme.getColorScheme(i);
				colorScheme.saveAsDirectories(directories);
			}
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_BatchExportColorSchemeSuccess"), directories));
		}
	}

	private void checkButtonState() {
		ColorSchemeTreeNode lastSelectedPathComponent = ((ColorSchemeTreeNode) tree.getLastSelectedPathComponent());
		boolean isSelectedColorSchemeParent = lastSelectedPathComponent != null && !CoreProperties.getString("String_MyFavorites").equals(((ColorSchemeTreeNode) lastSelectedPathComponent.getParent()).getName()) && lastSelectedPathComponent.getParent().getParent() != null;
		this.buttonAdd.setEnabled(isSelectedColorSchemeParent);
		this.buttonEdit.setEnabled(tableColorScheme.getSelectedRowCount() == 1);
		this.buttonDel.setEnabled(tableColorScheme.getSelectedRowCount() > 0);
		this.buttonImport.setEnabled(isSelectedColorSchemeParent);
		this.buttonExport.setEnabled(tableColorScheme.getSelectedRowCount() > 0);
	}

	private void initResources() {
		this.buttonAddDirectory.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Add.png"));
		this.buttonAddDirectory.setToolTipText(ControlsProperties.getString("String_AddColorSchemeGroup"));
		this.buttonAdd.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/add.png"));
		this.buttonAdd.setToolTipText(ControlsProperties.getString("String_AddColorScheme"));
		this.buttonEdit.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/edit.png"));
		this.buttonEdit.setToolTipText(ControlsProperties.getString("String_EditColorScheme"));
		this.buttonDel.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonDel.setToolTipText(ControlsProperties.getString("String_RemoveColorScheme"));

		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString(CommonProperties.selectAll));
		this.buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonSelectInvert.setToolTipText(CommonProperties.getString(CommonProperties.selectInverse));

		this.buttonImport.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Import.png"));
		this.buttonImport.setToolTipText(CommonProperties.getString(CommonProperties.IMPORT));
		this.buttonExport.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Export.png"));
		this.buttonExport.setToolTipText(CommonProperties.getString(CommonProperties.EXPORT));

		this.buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));

		this.setTitle(ControlsProperties.getString("String_ColorSchemeManageForm"));
	}

	private void initComponentState() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				splitPane.setDividerLocation(0.23d);
				removeWindowListener(this);
			}
		});
		ColorSchemeManager colorSchemeManager = ColorSchemeManager.getColorSchemeManager();
		colorSchemeManager.reload();
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setModel(new DefaultTreeModel(colorSchemeManager.getRootTreeNode().clone()));
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		((DefaultTreeCellRenderer) tree.getCellRenderer()).setLeafIcon(((DefaultTreeCellRenderer) tree.getCellRenderer()).getDefaultClosedIcon());
		JTreeUIUtilities.expandTree(tree, true);
		tree.setSelectionRow(0);
		if (tableColorScheme.getRowCount() > 0) {
			tableColorScheme.setRowSelectionInterval(0, 0);
		} else {
			checkButtonState();
		}
	}

	protected JDialogAddDirectory getAddDirectoryDialog() {
		return new JDialogAddDirectory();
	}

	@Override
	public void dispose() {
		if (askSave()) {
			super.dispose();
		}
	}

	protected class JDialogAddDirectory extends SmDialog {
		private JLabel labelParent = new JLabel();
		private JTextField textFieldParent = new JTextField();

		private JLabel labelName = new JLabel();
		private SmTextFieldLegit textFieldName = new SmTextFieldLegit();

		private SmButton buttonOk = new SmButton();
		private SmButton buttonCancle = new SmButton();

		public JDialogAddDirectory() {
			init();
			this.setTitle(ControlsProperties.getString("String_title_newColorSchemeGroup"));
			this.setSize(new Dimension(250, 150));
			this.setLocationRelativeTo(null);
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent e) {
					textFieldName.requestFocus();
					textFieldName.select(0, textFieldName.getText().length());
					JDialogAddDirectory.this.removeWindowListener(this);
				}
			});
		}

		protected void init() {

			final ArrayList<String> names = new ArrayList<>();
			ColorSchemeTreeNode userDefineNode = (ColorSchemeTreeNode) ((ColorSchemeTreeNode) tree.getModel().getRoot()).getChildAt(1);
			for (int i = 0; i < userDefineNode.getChildCount(); i++) {
				names.add(((ColorSchemeTreeNode) userDefineNode.getChildAt(i)).getName());
			}

			labelParent.setText(ControlsProperties.getString("String_GroupID"));
			labelName.setText(ControlsProperties.getString("String_GroupName"));

			textFieldParent.setText(CoreProperties.getString("String_UserDefine"));
			textFieldParent.setEditable(false);
			textFieldName.setText(StringUtilities.getUniqueName("UserDefine", names));

			textFieldName.setSmTextFieldLegit(new ISmTextFieldLegit() {
				@Override
				public boolean isTextFieldValueLegit(String textFieldValue) {
					if (SystemPropertyUtilities.isWindows()) {
						for (String name : names) {
							if (name.compareToIgnoreCase(textFieldValue) == 0) {
								buttonOk.setEnabled(false);
								return false;
							}
						}
					} else {
						if (names.contains(textFieldValue)) {
							buttonOk.setEnabled(false);
							return false;
						}
					}
					if (!FileUtilities.isLegalFolderName(textFieldValue)) {
						buttonOk.setEnabled(false);
						return false;
					}
					buttonOk.setEnabled(true);
					return true;
				}

				@Override
				public String getLegitValue(String currentValue, String backUpValue) {
					buttonOk.setEnabled(true);
					return backUpValue;
				}
			});

			buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
			buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
			componentList.add(buttonOk);
			componentList.add(buttonCancle);

			this.getRootPane().setDefaultButton(buttonOk);

			this.setLayout(new GridBagLayout());
			this.add(labelParent, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setInsets(10, 10, 0, 0));
			this.add(textFieldParent, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0.5).setInsets(10, 5, 0, 10));

			this.add(labelName, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0).setWeight(0, 0));
			this.add(textFieldName, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10).setWeight(1, 0));

			JPanel panelButtons = new JPanel();
			panelButtons.setLayout(new GridBagLayout());
			panelButtons.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 0, 0).setWeight(1, 0));
			panelButtons.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setInsets(0, 5, 0, 0).setWeight(0, 0));

			this.add(panelButtons, new GridBagConstraintsHelper(0, 2, 2, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setInsets(5, 10, 10, 10).setAnchor(GridBagConstraints.SOUTH));

			buttonOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.OK;
					dispose();
				}
			});

			buttonCancle.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					dispose();
				}
			});

		}

		public String getNodeName() {
			return textFieldName.getText();
		}
	}

	protected class MyMenuItem extends JMenuItem {
		private ColorSchemeTreeNode colorSchemeTreeNode;

		public MyMenuItem(String text) {
			super(text);
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					isModified = true;
					int[] selectedRows = tableColorScheme.getSelectedRows();
					for (int i : selectedRows) {
						ColorScheme clone = tableColorScheme.getColorScheme(i).clone();
						clone.setFavorite(false);
						clone.setParentNode(colorSchemeTreeNode);
						// TODO: 2016/7/14
						colorSchemeTreeNode.addColorScheme(clone);
					}
					ColorSchemeTreeNode lastSelectedPathComponent = ((ColorSchemeTreeNode) tree.getLastSelectedPathComponent());
					if (lastSelectedPathComponent != null && lastSelectedPathComponent == colorSchemeTreeNode) {
						((ColorSchemeTableModel) tableColorScheme.getModel()).fireTableDataChanged();
						tableColorScheme.setRowSelectionInterval(tableColorScheme.getRowCount() - selectedRows.length, tableColorScheme.getRowCount() - 1);
						tableColorScheme.scrollRectToVisible(tableColorScheme.getCellRect(tableColorScheme.getRowCount() - selectedRows.length, 0, true));
					}
				}
			});
		}

		public ColorSchemeTreeNode getColorSchemeTreeNode() {
			return colorSchemeTreeNode;
		}

		public void setColorSchemeTreeNode(ColorSchemeTreeNode colorSchemeTreeNode) {
			this.colorSchemeTreeNode = colorSchemeTreeNode;
		}
	}

}
