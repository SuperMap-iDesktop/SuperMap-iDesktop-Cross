package com.supermap.desktop.process.parameters.ParameterPanels.MultiBufferRadioList;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/22 0022.
 */
public class PanelMultiBufferRadioList extends JPanel {
	public JToolBar getToolBar() {
		return toolBar;
	}

	public JTable getTableRadioList() {
		return tableRadioList;
	}

	/**
	 *
	 */
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

	protected Double[] radioList;

	public PanelMultiBufferRadioList() {
		initComponents();
		initLayout();
		initResources();
		registerEvent();
	}

	private void initComponents() {
		toolBar = new JToolBar();
		buttonBatchAddRadio = new SmButton();
		buttonBatchAddRadio.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/batchAdd.png"));

		buttonInsert = new SmButton();
		buttonInsert.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/insert.png"));

		buttonSelectAll = new SmButton();
		buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));

		buttonSelectInvert = new SmButton();
		buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));

		buttonDelete = new SmButton();
		buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));

		buttonMoveUp = new SmButton();
		buttonMoveUp.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveUp.png"));

		buttonMoveDown = new SmButton();
		buttonMoveDown.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveDown.png"));

		tableRadioList = new JTable();
		ArrayList arrayList = new ArrayList();
		arrayList.add(10.0);
		multiBufferRadioListTableModel = new MultiBufferRadioListTableModel(arrayList);
		tableRadioList.setModel(multiBufferRadioListTableModel);
	}

	private void initLayout() {
		toolBar.add(buttonBatchAddRadio);
		toolBar.add(buttonInsert);
		toolBar.addSeparator();
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.addSeparator();
		toolBar.add(buttonDelete);
		toolBar.addSeparator();
		toolBar.add(buttonMoveUp);
		toolBar.add(buttonMoveDown);

		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 10));
		this.add(new JScrollPane(tableRadioList), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
	}

	private void initResources() {
		buttonBatchAddRadio.setToolTipText(ControlsProperties.getString("String_AddRange"));
		buttonInsert.setToolTipText(ControlsProperties.getString("String_Insert"));
		buttonSelectAll.setToolTipText(ControlsProperties.getString("String_SelectAll"));
		buttonSelectInvert.setToolTipText(ControlsProperties.getString("String_SelectReverse"));
		buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
		buttonMoveUp.setToolTipText(ControlsProperties.getString("String_MoveUp"));
		buttonMoveDown.setToolTipText(ControlsProperties.getString("String_MoveDown"));
	}

	private void registerEvent() {
		buttonInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();
				if (selectedRows.length > 0) {
					multiBufferRadioListTableModel.insertRow(selectedRows[0], 10.0);
				} else {
					ArrayList<Double> newRadioList = new ArrayList<>();
					newRadioList.add(10.0);
					multiBufferRadioListTableModel.setRadioValues(newRadioList);
				}
			}
		});

		buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();
				if (selectedRows.length > 0) {
					tableRadioList.selectAll();
				}
			}
		});

		buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();
				if (selectedRows.length > 0) {
					TableUtilities.stopEditing(tableRadioList);
					TableUtilities.invertSelection(tableRadioList);
				}
			}
		});

		buttonMoveUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();
				if (selectedRows.length > 0) {
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

		buttonMoveDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableRadioList.getSelectedRows();
				if (selectedRows.length > 0) {
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
	}

}
