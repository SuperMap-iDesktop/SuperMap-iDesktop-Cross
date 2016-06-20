package com.supermap.desktop.dialog.ColorSchemeDialogs;

import com.supermap.data.Colors;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorScheme;
import com.supermap.desktop.ui.controls.SortTable.SortTable;
import com.supermap.desktop.utilities.FontUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.util.List;

/**
 * @author XiaJT
 */
public class JTableColorScheme extends SortTable {
	private static final int preferredWidth = 250;
	private ColorSchemeTableModel colorSchemeTableModel;

	public JTableColorScheme() {
		this.colorSchemeTableModel = new ColorSchemeTableModel();
		// 输入时直接开始编辑
		this.setSurrendersFocusOnKeystroke(true);
		this.setModel(colorSchemeTableModel);
		this.setRowHeight(23);
		int indexWidth = FontUtilities.getStringWidth(ControlsProperties.getString("String_identifier"), tableHeader.getFont()) + 30;
		TableColumn columnIndex = this.getColumnModel().getColumn(ColorSchemeTableModel.COLUMN_INDEX);
		columnIndex.setMaxWidth(indexWidth);
		columnIndex.setPreferredWidth(indexWidth);
		columnIndex.setMinWidth(indexWidth);
		columnIndex.setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (rendererComponent instanceof JLabel) {
					((JLabel) rendererComponent).setHorizontalAlignment(CENTER);
				}
				return rendererComponent;
			}
		});
		TableColumn columnColors = this.getColumnModel().getColumn(ColorSchemeTableModel.COLUMN_COLOR_RAMP);
		columnColors.setCellRenderer(new ColorSchemeListCellRender());
		columnColors.setMaxWidth(preferredWidth);
		columnColors.setMinWidth(preferredWidth);
		columnColors.setPreferredWidth(preferredWidth);

		DefaultCellEditor objectCellEditor = new DefaultCellEditor(new JTextField()) {
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
				Component tableCellEditorComponent = super.getTableCellEditorComponent(table, value, isSelected, row, column);
				if (tableCellEditorComponent instanceof JTextField) {
					((JTextField) tableCellEditorComponent).selectAll();
				}
				return tableCellEditorComponent;
			}
		};
		this.setDefaultEditor(Object.class, objectCellEditor);
	}

	public ColorScheme getColorScheme(int row) {
		return colorSchemeTableModel.getColorScheme(row);
	}

	public void setColorSchemeList(List<ColorScheme> colorSchemeList) {
		colorSchemeTableModel.setColorSchemes(colorSchemeList);
	}

	public List<ColorScheme> getColorSchemeList() {
		return colorSchemeTableModel.getColorSchemes();
	}

	public void deleteSelectedRow() {
		colorSchemeTableModel.removeRows(this.getSelectedRows());
	}

	public void addColorScheme(ColorScheme colorScheme) {
		colorSchemeTableModel.addColorScheme(colorScheme);
	}

	public void setColorSchemeAtRow(int row, ColorScheme colorScheme) {
		colorSchemeTableModel.setColorSchemeAtRow(row, colorScheme);
	}

	class ColorSchemeListCellRender extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (value == null) {
				return new JLabel();
			}
			int width = table.getColumnModel().getColumn(column).getWidth();
			int height = table.getRowHeight(row);
			JLabel colorsLabel = ColorScheme.getColorsLabel(((Colors) value), width, 23);
			if (isSelected) {
				colorsLabel.setBorder(new LineBorder(Color.black, 1, false));
			} else {
				colorsLabel.setBorder(new LineBorder(Color.white, 1, false));
			}
			return colorsLabel;
		}
	}

}
