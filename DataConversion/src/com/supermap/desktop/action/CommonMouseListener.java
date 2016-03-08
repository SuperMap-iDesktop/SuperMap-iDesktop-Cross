package com.supermap.desktop.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.ui.DataImportFrame;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.FileInfoModel;

/**
 * 通用的鼠标响应事件
 * 
 * @author Administrator
 *
 */
public class CommonMouseListener extends MouseAdapter {
	private JTable table;
	private JPanel panelImportInfo;
	private JPanel newPanel;
	private ArrayList<ImportFileInfo> fileInfos;
	private JLabel lblDataimportType;
	private FileInfoModel model;
	private ArrayList<JPanel> panels;
	private DataImportFrame dataImportFrame;
	private boolean hasImportInfo;

	public CommonMouseListener(DataImportFrame dataImportFrame, JPanel newPanel, JTable table,
			JPanel panelImportInfo, List<ImportFileInfo> fileInfos,
			List<JPanel> panels, JLabel lblDataimportType,
			FileInfoModel model) {
		this.dataImportFrame = dataImportFrame;
		this.newPanel = newPanel;
		this.table = table;
		this.panelImportInfo = panelImportInfo;
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		this.panels = (ArrayList<JPanel>) panels;
		this.lblDataimportType = lblDataimportType;
		this.model = model;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (1 == e.getClickCount()) {
			// 设置表格的所有行可选
			table.setRowSelectionAllowed(true);
			if (!fileInfos.isEmpty()) {
				// 刷新右边界面
				CommonFunction.refreshPanel(table, panelImportInfo, fileInfos,
						panels, lblDataimportType);
			}
			// 如果没有选择行数据时，将右边界面替换为默认界面
			if (0 == table.getSelectedRowCount()) {
				CommonFunction.replace(panelImportInfo, newPanel);
			}
		}
		if (2 == e.getClickCount()) {
			refreshTable();
			// 刷新右边界面
			if (0 < table.getRowCount()) {
				CommonFunction.refreshPanel(table, panelImportInfo, fileInfos, panels, lblDataimportType);
				hasImportInfo = true;
			}
			if (hasImportInfo) {
				dataImportFrame.getButtonDelete().setEnabled(true);
				dataImportFrame.getButtonImport().setEnabled(true);
				dataImportFrame.getButtonInvertSelect().setEnabled(true);
				dataImportFrame.getButtonSelectAll().setEnabled(true);
			}
		}
	}

	// 刷新table
	public void refreshTable() {
		int select = 0;
		if (0 != table.getRowCount()) {
			select = table.getRowCount() - 1;
		}
		CommonFunction.setTableInfo(dataImportFrame, panels, model);
		this.dataImportFrame.initComboBoxColumns();
		if (table.getRowCount() - 1 < 0) {
			// 设置表格的所有行可选
			table.setRowSelectionAllowed(true);
		} else {
			// 设置新添加项可选
			int selected = table.getRowCount() - 1;
			table.setRowSelectionInterval(select, selected);
		}
		table.updateUI();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// 刷新右边界面
		if (0 < table.getRowCount()) {
			CommonFunction.refreshPanel(table, panelImportInfo, fileInfos, panels,
					lblDataimportType);
		}
	}

}
