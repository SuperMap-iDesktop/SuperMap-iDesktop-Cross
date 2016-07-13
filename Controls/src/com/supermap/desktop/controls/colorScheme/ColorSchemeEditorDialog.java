package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ColorSelectionPanel;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.FontUtilities;
import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * 自定义颜色编辑对话框
 *
 * @author xuzw
 */
public class ColorSchemeEditorDialog extends SmDialog {

	private static final long serialVersionUID = 1L;


	//region 工具条
	private JToolBar toolBar;
	private SmButton jButtonMoveBottomButton;
	private SmButton jButtonMoveDownButton;
	private SmButton jButtonMoveUpButton;
	private SmButton jButtonMoveTopButton;
	private SmButton jButtonRemoveColorButton;
	private SmButton jButtonAddColorButton;
	private SmButton jButtonSelectInvert;
	private SmButton jButtonSelectAllButton;
	private SmButton buttonInvertColors;
	private SmButton buttonImport;
	private SmButton buttonExport;
	//endregion

	//region table
	private JTable tableColorsTable;
	private ColorsTableModel colorsTableModel;
	//endregion

	//region 预览面板
	private JPanel jPanelPreView;
	private JLabel jLabelPreViewLabel;
	//endregion

	//region 按钮
	private JPanel panelButtons;
	private SmButton jButtonCancelButton;
	private SmButton jButtonConfirmButton;
	//endregion

	//region 基础信息面板
	private JPanel panelBasicInfo;
	private JLabel labelName;
	private SmTextFieldLegit textFieldName;
	private JLabel labelColorBuildMethod;
	private JComboBox<String> comboBoxColorBuildMethod;
	private JLabel labelAuthor;
	private SmTextFieldLegit textFieldAuthor;
	private JLabel labelIntervalColorCount;
	private SmTextFieldLegit textFieldColorCount;
	private JLabel labelDescribe;
	private SmTextFieldLegit textFieldDescribe;
	//endregion


	private ColorScheme colorScheme;

	private java.util.List<String> exitNames;

	/**
	 * 构造函数
	 */
	public ColorSchemeEditorDialog() {
		this((ColorScheme) null);
	}

	public ColorSchemeEditorDialog(ColorScheme colorScheme) {
		super();
		init(colorScheme);
	}

	private void init(ColorScheme colorScheme) {
		setSize(new Dimension(700, 500));

		if (colorScheme == null) {
			this.colorScheme = new ColorScheme();
		} else {
			this.colorScheme = colorScheme.clone();
		}
		this.setLocationRelativeTo(null);
		setTitle(ControlsProperties.getString("String_ColorEditor"));

		initComponents();
		initLayout();
		initResources();
		initListeners();
		initComponentStates();
		componentList.add(jButtonConfirmButton);
		componentList.add(jButtonCancelButton);
		this.getRootPane().setDefaultButton(jButtonConfirmButton);
		// 启动后刷新一下预览框
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				refreshViewLabel();
				ColorSchemeEditorDialog.this.removeWindowListener(this);
			}
		});
	}

	private void initComponents() {
		initColorsTable();
		this.jButtonSelectAllButton = new SmButton();
		this.jButtonSelectAllButton.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));

		this.jButtonSelectInvert = new SmButton();
		this.jButtonSelectInvert.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));

		this.jButtonAddColorButton = new SmButton();
		this.jButtonAddColorButton.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/addColor.png")));

		this.jButtonRemoveColorButton = new SmButton();
		this.jButtonRemoveColorButton.setToolTipText(ControlsProperties.getString("String_RemoveColorScheme"));
		this.jButtonRemoveColorButton.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));

		this.jButtonMoveTopButton = new SmButton();
		this.jButtonMoveTopButton.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/moveToTop.png")));

		this.jButtonMoveUpButton = new SmButton();
		this.jButtonMoveUpButton.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/moveUp.png")));

		this.jButtonMoveDownButton = new SmButton();
		this.jButtonMoveDownButton.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/moveDown.png")));

		this.jButtonMoveBottomButton = new SmButton();
		this.jButtonMoveBottomButton.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/moveBottom.png")));

		buttonInvertColors = new SmButton();
		buttonInvertColors.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/invert.png")));
		this.buttonImport = new SmButton();
		this.buttonImport.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Import.png")));

		this.buttonExport = new SmButton();
		this.buttonExport.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Export.png")));

		this.panelBasicInfo = new JPanel();
		this.labelName = new JLabel();
		this.textFieldName = new SmTextFieldLegit();
		this.textFieldName.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
//				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
//					return false;
//				}
				if (exitNames != null && exitNames.contains(textFieldValue)) {
					return false;
				}
				colorScheme.setName(textFieldValue);
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});

		this.labelColorBuildMethod = new JLabel();
		this.comboBoxColorBuildMethod = new JComboBox<>(new String[]{
				ColorScheme.IntervalColorBuildMethod.ICBM_GRADIENT.toString(),
				ColorScheme.IntervalColorBuildMethod.ICBM_RANDOM.toString(),
		});
		this.labelAuthor = new JLabel();
		this.textFieldAuthor = new SmTextFieldLegit();

		this.labelIntervalColorCount = new JLabel();
		this.textFieldColorCount = new SmTextFieldLegit();
		this.textFieldColorCount.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				try {
					Integer integer = Integer.valueOf(textFieldValue);
					if (integer > 256 || integer < 0) {
						return false;
					}
				} catch (Exception e) {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		this.labelDescribe = new JLabel();
		this.textFieldDescribe = new SmTextFieldLegit();

		this.jButtonConfirmButton = new SmButton();
		this.jButtonCancelButton = new SmButton();

	}

	private void initLayout() {
		initToolBar();
		initBasicInfoPanel();
		initPreviewPanel();
		initPanelButton();
		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 10));
		this.add(new JScrollPane(tableColorsTable), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
		this.add(jPanelPreView, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
		this.add(panelBasicInfo, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
		this.add(panelButtons, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 10, 10));
	}

	/**
	 * 工具条
	 *
	 * @return
	 */
	protected void initToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.add(jButtonAddColorButton);
			toolBar.add(jButtonRemoveColorButton);
			toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
			toolBar.add(jButtonSelectAllButton);
			toolBar.add(jButtonSelectInvert);
			toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
			toolBar.add(buttonInvertColors);
			toolBar.add(jButtonMoveTopButton);
			toolBar.add(jButtonMoveUpButton);
			toolBar.add(jButtonMoveDownButton);
			toolBar.add(jButtonMoveBottomButton);
			toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
			toolBar.add(buttonImport);
			toolBar.add(buttonExport);

			jButtonAddColorButton.setFocusable(false);
			jButtonRemoveColorButton.setFocusable(false);
			jButtonSelectAllButton.setFocusable(false);
			jButtonSelectInvert.setFocusable(false);
			buttonInvertColors.setFocusable(false);
			jButtonMoveTopButton.setFocusable(false);
			jButtonMoveUpButton.setFocusable(false);
			jButtonMoveDownButton.setFocusable(false);
			jButtonMoveBottomButton.setFocusable(false);
			buttonImport.setFocusable(false);
			buttonExport.setFocusable(false);
		}
	}

	private void initPanelButton() {
		panelButtons = new JPanel();
		panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(jButtonConfirmButton, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(1, 1));
		panelButtons.add(jButtonCancelButton, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(0, 1).setInsets(0, 5, 0, 0));
	}

	private void initBasicInfoPanel() {
		panelBasicInfo.setLayout(new GridBagLayout());
		panelBasicInfo.setBorder(new TitledBorder(null, ControlsProperties.getString("String_BasicInfo"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null));
		panelBasicInfo.add(labelName, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelBasicInfo.add(textFieldName, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
		panelBasicInfo.add(labelColorBuildMethod, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelBasicInfo.add(comboBoxColorBuildMethod, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelBasicInfo.add(labelAuthor, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelBasicInfo.add(textFieldAuthor, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
		panelBasicInfo.add(labelIntervalColorCount, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelBasicInfo.add(textFieldColorCount, new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelBasicInfo.add(labelDescribe, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelBasicInfo.add(textFieldDescribe, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
		panelBasicInfo.add(new JPanel(), new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(4, 1).setFill(GridBagConstraints.BOTH).setInsets(5, 5, 0, 10));
	}

	private void initResources() {
		jButtonAddColorButton.setToolTipText(ControlsProperties.getString("String_AddColor"));
		jButtonRemoveColorButton.setToolTipText(ControlsProperties.getString("String_RemoveColor"));
		jButtonSelectAllButton.setToolTipText(ControlsProperties.getString("String_SelectAll"));
		jButtonSelectInvert.setToolTipText(ControlsProperties.getString("String_SelectReverse"));
		jButtonMoveTopButton.setToolTipText(ControlsProperties.getString("String_FirstColor"));
		jButtonMoveUpButton.setToolTipText(ControlsProperties.getString("String_UpColor"));
		jButtonMoveDownButton.setToolTipText(ControlsProperties.getString("String_DownColor"));
		jButtonMoveBottomButton.setToolTipText(ControlsProperties.getString("String_LastColor"));
		buttonImport.setToolTipText(CommonProperties.getString(CommonProperties.IMPORT));
		buttonExport.setToolTipText(CommonProperties.getString(CommonProperties.EXPORT));
		jButtonConfirmButton.setText(CommonProperties.getString(CommonProperties.OK));
		jButtonCancelButton.setText(CommonProperties.getString(CommonProperties.Cancel));
		buttonInvertColors.setToolTipText(ControlsProperties.getString("String_ReverseColor"));
		labelName.setText(ControlsProperties.getString("String_Label_Name"));
		labelColorBuildMethod.setText(ControlsProperties.getString("String_labelColorChangeStyle"));
		labelAuthor.setText(ControlsProperties.getString("String_labelAuthor"));
		labelIntervalColorCount.setText(ControlsProperties.getString("String_labelIntervalColorCount"));
		labelDescribe.setText(ControlsProperties.getString("String_labelDescription"));
	}

	private void initListeners() {
		jButtonSelectAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableColorsTable.selectAll();
			}
		});
		jButtonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(tableColorsTable);
				TableUtilities.invertSelection(tableColorsTable);
			}

		});

		jButtonAddColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (colorsTableModel.getRowCount() == 0) {
					// 古锭刀效果触发
					colorsTableModel.add();
				}
				colorsTableModel.add();

				tableColorsTable.setRowSelectionInterval(tableColorsTable.getRowCount() - 1, tableColorsTable.getRowCount() - 1);
				tableColorsTable.scrollRectToVisible(tableColorsTable.getCellRect(tableColorsTable.getRowCount() - 1, 0, true));
			}
		});
		jButtonRemoveColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRow = tableColorsTable.getSelectedRows();
				colorsTableModel.removeRow(selectedRow);
				if (selectedRow[0] < tableColorsTable.getRowCount()) {
					tableColorsTable.setRowSelectionInterval(selectedRow[0], selectedRow[0]);
				} else if (tableColorsTable.getRowCount() > 0) {
					tableColorsTable.setRowSelectionInterval(tableColorsTable.getRowCount() - 1, tableColorsTable.getRowCount() - 1);
				}
			}

		});
		jButtonMoveTopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int length = tableColorsTable.getSelectedRows().length;
				colorsTableModel.moveToTop(tableColorsTable.getSelectedRows());
				tableColorsTable.setRowSelectionInterval(0, length - 1);
			}
		});
		jButtonMoveUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableColorsTable.getSelectedRows();
				colorsTableModel.moveUp(selectedRows);
				tableColorsTable.clearSelection();
				for (int selectedRow : selectedRows) {
					tableColorsTable.addRowSelectionInterval(selectedRow - 1, selectedRow - 1);
				}
			}
		});

		jButtonMoveDownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableColorsTable.getSelectedRows();
				colorsTableModel.moveDown(selectedRows);
				tableColorsTable.clearSelection();
				for (int selectedRow : selectedRows) {
					tableColorsTable.addRowSelectionInterval(selectedRow + 1, selectedRow + 1);
				}
			}
		});

		jButtonMoveBottomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int length = tableColorsTable.getSelectedRows().length;
				colorsTableModel.moveToBottom(tableColorsTable.getSelectedRows());
				tableColorsTable.setRowSelectionInterval(tableColorsTable.getRowCount() - length, tableColorsTable.getRowCount() - 1);
			}
		});

		jButtonConfirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.OK;
				setVisible(false);
			}
		});

		jButtonCancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				setVisible(false);
			}

		});

		buttonInvertColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorsTableModel.colorInvert();
			}
		});

		tableColorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonStates();
			}
		});

		tableColorsTable.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				refreshViewLabel();
			}
		});


		comboBoxColorBuildMethod.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					colorScheme.setIntervalColorBuildMethod(ColorScheme.IntervalColorBuildMethod.getMethod((String) comboBoxColorBuildMethod.getSelectedItem()));
					refreshViewLabel();
				}
			}
		});

		textFieldAuthor.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateAuthor();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateAuthor();

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateAuthor();

			}

			private void updateAuthor() {
				colorScheme.setAuthor(textFieldAuthor.getText());
			}
		});

		textFieldColorCount.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateCount();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateCount();

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateCount();

			}

			private void updateCount() {
				if (textFieldColorCount.isLegitValue(textFieldColorCount.getText())) {
					colorScheme.setIntervalColorCount(Integer.valueOf(textFieldColorCount.getText()));
					refreshViewLabel();
				}
			}
		});

		textFieldDescribe.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateDescribe();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateDescribe();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateDescribe();
			}

			private void updateDescribe() {
				colorScheme.setDescription(textFieldDescribe.getText());
			}
		});

		buttonImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonImportClicked();
			}
		});

		buttonExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonExportClicked();
			}
		});

		tableColorsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1
						&& tableColorsTable.columnAtPoint(e.getPoint()) == 1 && tableColorsTable.rowAtPoint(e.getPoint()) != -1) {
					final JPopupMenu popupMenu = new JPopupMenu();
					final int selectedRow = tableColorsTable.getSelectedRow();
					popupMenu.setBorderPainted(false);
					ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
					popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
					colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
					popupMenu.show(tableColorsTable, (int) e.getPoint().getX(), (int) e.getPoint().getY());
					colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							Color color = (Color) evt.getNewValue();
							tableColorsTable.setValueAt(color, selectedRow, 1);
							popupMenu.setVisible(false);
						}
					});
					tableColorsTable.setRowSelectionInterval(selectedRow, selectedRow);
				}
			}
		});

		jLabelPreViewLabel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				refreshViewLabel();
			}
		});
	}

	private void buttonImportClicked() {
		if (!SmFileChoose.isModuleExist("ColorSchemeImport")) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_ColorSchemeSaveFileFilter"), "scs", "SCS");
			SmFileChoose.addNewNode(fileFilters, PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeBasicDirectory"), true),
					ControlsProperties.getString("String_ImportColorScheme"), "ColorSchemeImport", "OpenMany");
		}
		SmFileChoose fileChooser = new SmFileChoose("ColorSchemeImport");
		int result = fileChooser.showDefaultDialog();
		if (result == JFileChooser.APPROVE_OPTION && fileChooser.getSelectFiles().length > 0 && fileChooser.getSelectFiles()[0] != null) {
			colorScheme.fromXML(fileChooser.getSelectFiles()[0], true);
			colorsTableModel.fireTableDataChanged();
			initComponentStates();
		}
	}

	private void buttonExportClicked() {
		if (!SmFileChoose.isModuleExist("ColorSchemeExportSingle")) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_ColorSchemeSaveFileFilter"), "scs", "SCS");
			SmFileChoose.addNewNode(fileFilters, PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeBasicDirectory"), true),
					ControlsProperties.getString("String_ExportColorScheme"), "ColorSchemeExportSingle", "SaveOne");
		}
		SmFileChoose fileChooser = new SmFileChoose("ColorSchemeExportSingle");
		int result = fileChooser.showDefaultDialog();
		String filePath = fileChooser.getFilePath();
		if (result == JFileChooser.APPROVE_OPTION && !StringUtilities.isNullOrEmpty(filePath)) {
			colorScheme.saveAsFilePath(filePath);
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_BatchExportColorSchemeSuccess"), filePath));
		}
	}

	private void checkButtonStates() {
		int rowCount = tableColorsTable.getRowCount();
		int selectedRowCount = tableColorsTable.getSelectedRowCount();
		jButtonRemoveColorButton.setEnabled(selectedRowCount > 0);
		jButtonSelectAllButton.setEnabled(rowCount > 0);
		jButtonSelectInvert.setEnabled(rowCount > 0);
		buttonInvertColors.setEnabled(rowCount > 0);

		jButtonMoveTopButton.setEnabled(selectedRowCount > 0 && tableColorsTable.getSelectedRows()[selectedRowCount - 1] != selectedRowCount - 1);
		jButtonMoveUpButton.setEnabled(selectedRowCount > 0 && !tableColorsTable.isRowSelected(0));
		jButtonMoveDownButton.setEnabled(selectedRowCount > 0 && !tableColorsTable.isRowSelected(rowCount - 1));

		jButtonMoveBottomButton.setEnabled(selectedRowCount > 0 && tableColorsTable.getSelectedRow() != rowCount - selectedRowCount);

		jButtonConfirmButton.setEnabled(rowCount > 1);
		buttonExport.setEnabled(rowCount > 1);
	}

	private void initComponentStates() {
		colorsTableModel.setColors(colorScheme.getColorsList());
		textFieldDescribe.setText(colorScheme.getDescription());
		textFieldColorCount.setText(String.valueOf(colorScheme.getIntervalColorCount()));
		textFieldName.setText(StringUtilities.getUniqueName(colorScheme.getName(), exitNames));
		textFieldAuthor.setText(colorScheme.getAuthor());
		comboBoxColorBuildMethod.setSelectedItem(colorScheme.getIntervalColorBuildMethod().toString());
		if (tableColorsTable.getRowCount() > 0) {
			tableColorsTable.setRowSelectionInterval(0, 0);
		} else {
			checkButtonStates();
		}
	}

	public ColorSchemeEditorDialog(JDialog jDialog) {
		this(jDialog, null);
	}

	public ColorSchemeEditorDialog(JDialog dialog, ColorScheme colorScheme) {
		super(dialog);
		init(colorScheme);
	}


	public void setExitNames(java.util.List<String> exitNames) {
		this.exitNames = exitNames;
		this.textFieldName.setText(StringUtilities.getUniqueName(this.textFieldName.getText(), exitNames));
	}

	/**
	 * 颜色集合预览面板
	 *
	 * @return
	 */
	protected JPanel initPreviewPanel() {
		if (jPanelPreView == null) {
			jPanelPreView = new JPanel();
			jPanelPreView.setLayout(new BorderLayout());
			jPanelPreView.setBorder(new TitledBorder(null, ControlsProperties.getString("String_Title_Preview"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanelPreView.add(getPreViewLabel(), BorderLayout.CENTER);
		}
		return jPanelPreView;
	}


	/**
	 * 颜色集合表格
	 *
	 * @return
	 */
	protected JTable initColorsTable() {
		if (tableColorsTable == null) {
			tableColorsTable = new JTable();
			tableColorsTable.setRowHeight(25);
			colorsTableModel = new ColorsTableModel();
			tableColorsTable.setModel(colorsTableModel);

			// 设置表格颜色列的显示效果为指定的颜色
			TableColumn colorColumn = tableColorsTable.getColumnModel().getColumn(1);
			colorColumn.setCellRenderer(new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					JLabel label = new JLabel();
					label.setOpaque(true);
					if (value instanceof Color) {
						label.setBackground((Color) value);
					}
					return label;
				}

			});
			TableColumn column = tableColorsTable.getColumnModel().getColumn(0);
			int indexWidth = FontUtilities.getStringWidth(ControlsProperties.getString("String_identifier"), tableColorsTable.getTableHeader().getFont()) + 30;
			column.setMaxWidth(indexWidth);
			column.setPreferredWidth(indexWidth);
			column.setMinWidth(indexWidth);
			column.setCellRenderer(new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if (rendererComponent instanceof JLabel) {
						((JLabel) rendererComponent).setHorizontalAlignment(CENTER);
					}
					return rendererComponent;
				}
			});
		}
		return tableColorsTable;
	}


	/**
	 * 预览标签
	 *
	 * @return 预览标签
	 */
	protected JLabel getPreViewLabel() {
		if (jLabelPreViewLabel == null) {
			jLabelPreViewLabel = new JLabel();
			jLabelPreViewLabel.setPreferredSize(new Dimension(10, 35));
		}
		return jLabelPreViewLabel;
	}

	/**
	 * 刷新预览标签
	 */
	private void refreshViewLabel() {
		int imageWidth = jLabelPreViewLabel.getSize().width > 0 ? jLabelPreViewLabel.getSize().width : 484;
		int imageHeight = jLabelPreViewLabel.getSize().height > 0 ? jLabelPreViewLabel.getSize().height : 23;
		BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

		// 根据当前渲染单元格的宽度和颜色数计算出每个颜色应当渲染的步长
		int size = colorScheme.getColorsList().size();
		if (size == 0) {
			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, imageWidth, imageHeight);
		} else if (size == 1) {
			graphics.setColor(colorScheme.getColorsList().get(0));
			graphics.fillRect(0, 0, imageWidth, imageHeight);
		} else {
			Colors colorsShow = colorScheme.getColors();
			int colorsCount = colorsShow.getCount();
			double step = (double) imageWidth / colorsCount;
			for (int i = 0; i < colorsCount; i++) {
				graphics.setColor(colorsShow.get(i));
				graphics.fillRect((int) (step * i), 0, (int) (step + 1), imageHeight);
			}
		}
		jLabelPreViewLabel.setIcon(new ImageIcon(bufferedImage));
	}

	public ColorScheme getColorScheme() {
		return colorScheme;
	}
}
