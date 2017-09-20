package com.supermap.desktop.process.parameters.ParameterPanels.MultiBufferRadioList;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.dialog.BatchAddDailog;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/22 0022.
 * 多重缓冲区半径列表面板
 */
public class PanelMultiBufferRadioList extends JPanel {

	private static final long serialVersionUID = 1L;

	private JToolBar toolBar;
	private SmButton buttonBatchAddRadio;
	private SmButton buttonInsert;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonDelete;
	private SmButton buttonMoveUp;
	private SmButton buttonMoveDown;

	protected JTable tableRadioList;
	private MultiBufferRadioListTableModel multiBufferRadioListTableModel;

	protected ArrayList<Double> radioLists = new ArrayList<>();

	public PanelMultiBufferRadioList() {
		initComponents();
		initLayout();
		initResources();
		registerEvent();
	}

	private void initComponents() {
		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);
		this.buttonBatchAddRadio = new SmButton();
		this.buttonBatchAddRadio.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/batchAdd.png"));

		this.buttonInsert = new SmButton();
		this.buttonInsert.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/insert.png"));

		this.buttonSelectAll = new SmButton();
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));

		this.buttonSelectInvert = new SmButton();
		this.buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));

		this.buttonDelete = new SmButton();
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));

		this.buttonMoveUp = new SmButton();
		this.buttonMoveUp.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveUp.png"));

		this.buttonMoveDown = new SmButton();
		this.buttonMoveDown.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveDown.png"));

		this.tableRadioList = new JTable();
		this.multiBufferRadioListTableModel = new MultiBufferRadioListTableModel(this.radioLists);
		this.tableRadioList.setModel(multiBufferRadioListTableModel);

		// 设置列不可移动
		this.tableRadioList.getTableHeader().setReorderingAllowed(false);
		this.tableRadioList.setRowHeight(23);
		// 设置列宽
		TableColumn indexColumn = this.tableRadioList.getColumnModel().getColumn(MultiBufferRadioListTableModel.COLUMN_INDEX_INDEX);
		indexColumn.setMinWidth(80);
		indexColumn.setPreferredWidth(80);
		indexColumn.setMaxWidth(150);
		checkButtonStates();
	}

	private void initLayout() {
		this.toolBar.add(this.buttonBatchAddRadio);
		this.toolBar.add(this.buttonInsert);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonSelectAll);
		this.toolBar.add(this.buttonSelectInvert);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonDelete);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonMoveUp);
		this.toolBar.add(this.buttonMoveDown);


		this.setLayout(new GridBagLayout());
		this.add(this.toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		this.add(new JScrollPane(this.tableRadioList), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 0, 0));
	}

	private void initResources() {
		this.buttonBatchAddRadio.setToolTipText(ControlsProperties.getString("String_AddRange"));
		this.buttonInsert.setToolTipText(ControlsProperties.getString("String_InsertDefaultValue"));
		this.buttonSelectAll.setToolTipText(ControlsProperties.getString("String_SelectAll"));
		this.buttonSelectInvert.setToolTipText(ControlsProperties.getString("String_SelectReverse"));
		this.buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
		this.buttonMoveUp.setToolTipText(ControlsProperties.getString("String_MoveUp"));
		this.buttonMoveDown.setToolTipText(ControlsProperties.getString("String_MoveDown"));
	}

	private void registerEvent() {

		this.buttonBatchAddRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BatchAddDailog batchAddColorTableDailog = new BatchAddDailog(10, 30, 3, (JFrame) Application.getActiveApplication().getMainFrame(), true);
				DialogResult result = batchAddColorTableDailog.showDialog();
				if (result == DialogResult.OK && batchAddColorTableDailog.getResultKeys() != null) {
					double[] doubles = batchAddColorTableDailog.getResultKeys();
					for (int i = 0; i < doubles.length; i++) {
						radioLists.add(doubles[i]);
					}
					multiBufferRadioListTableModel.setRadioValues(radioLists);
				}
			}
		});

		this.buttonInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 插入操作其实就是，先新建一条数据，然后将选中行下的所有行整体向下移动一行
				int[] selectedRows = tableRadioList.getSelectedRows();
				radioLists.add(10.0);
				if (selectedRows.length > 0) {
					int selectedFristRow = selectedRows[0];
					tableRadioList.addRowSelectionInterval(selectedFristRow, tableRadioList.getRowCount() - 2);
					selectedRows = tableRadioList.getSelectedRows();
					multiBufferRadioListTableModel.moveDown(selectedRows);
					tableRadioList.addRowSelectionInterval(selectedRows[0] + 1, selectedRows[0] + 1);
					tableRadioList.scrollRectToVisible(tableRadioList.getCellRect(selectedRows[0] + 1, 0, true));

				} else {
					// 当jtable为空时,添加一条数据，并高亮显示
					multiBufferRadioListTableModel.setRadioValues(radioLists);
					tableRadioList.addRowSelectionInterval(tableRadioList.getRowCount() - 1, tableRadioList.getRowCount() - 1);
					tableRadioList.scrollRectToVisible(tableRadioList.getCellRect(tableRadioList.getRowCount() - 1, 0, true));

				}
			}
		});

		this.buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableRadioList.selectAll();
			}
		});

		this.buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();
				if (selectedRows.length > 0) {
					TableUtilities.stopEditing(tableRadioList);
					TableUtilities.invertSelection(tableRadioList);
				}
			}
		});

		this.buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();
				if (selectedRows.length > 0) {
					// 多选删除时，从后往前删除
					for (int i = selectedRows.length - 1; i > -1; i--) {
						multiBufferRadioListTableModel.removeRow(selectedRows[i]);
					}
					int selectedLastRow = selectedRows[selectedRows.length - 1];
					if (selectedLastRow <= tableRadioList.getRowCount() - 1) {
						tableRadioList.addRowSelectionInterval(selectedLastRow, selectedLastRow);
					} else {
						if (tableRadioList.getRowCount() != 0) {
							tableRadioList.addRowSelectionInterval(tableRadioList.getRowCount() - 1, tableRadioList.getRowCount() - 1);
						}
					}
				}
			}
		});

		this.buttonMoveUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();

				if (selectedRows.length > 0 && selectedRows[0] > 0) {
					multiBufferRadioListTableModel.moveUp(selectedRows);
					tableRadioList.clearSelection();
					for (int selectedRow : selectedRows) {
						tableRadioList.addRowSelectionInterval(selectedRow - 1, selectedRow - 1);
					}
					selectedRows = tableRadioList.getSelectedRows();
					Rectangle cellRect = tableRadioList.getCellRect(selectedRows[0], 0, true);
					tableRadioList.scrollRectToVisible(cellRect);
				}
			}
		});

		this.buttonMoveDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();
				if (selectedRows.length > 0 && selectedRows[selectedRows.length - 1] < tableRadioList.getRowCount() - 1) {
					multiBufferRadioListTableModel.moveDown(selectedRows);
					tableRadioList.clearSelection();
					for (int selectedRow : selectedRows) {
						tableRadioList.addRowSelectionInterval(selectedRow + 1, selectedRow + 1);
					}
					selectedRows = tableRadioList.getSelectedRows();
					Rectangle cellRect = tableRadioList.getCellRect(selectedRows[selectedRows.length - 1], 0, true);
					tableRadioList.scrollRectToVisible(cellRect);
				}
			}
		});

		this.tableRadioList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonStates();
			}
		});
	}

	/**
	 * 设置各按钮是否可用
	 */
	private void checkButtonStates() {
		int rowCount = this.tableRadioList.getRowCount();
		int selectedRowCount = this.tableRadioList.getSelectedRowCount();
		this.buttonDelete.setEnabled(selectedRowCount > 0);
		this.buttonSelectAll.setEnabled(rowCount > 0);
		this.buttonSelectInvert.setEnabled(rowCount > 0);
		this.buttonMoveUp.setEnabled(selectedRowCount > 0 && !this.tableRadioList.isRowSelected(0));
		this.buttonMoveDown.setEnabled(selectedRowCount > 0 && !this.tableRadioList.isRowSelected(rowCount - 1));
	}
}
