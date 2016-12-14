package com.supermap.desktop.ui.controls;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableTooltipCellRenderer extends JLabel implements TableCellRenderer {
    private static TableTooltipCellRenderer tooltipCellRenderer;

    private TableTooltipCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (null != value) {
            this.setText(" " + value);
            this.setToolTipText((String) value);
        } else {
            this.setText((String) value);
        }
        if (isSelected) {
            this.setOpaque(true);
            this.setBackground(new Color(51, 153, 255));
            this.setForeground(Color.white);
        } else {
            this.setOpaque(true);
            this.setBackground(Color.white);
            this.setForeground(Color.black);
        }
        return this;
    }

    public static TableTooltipCellRenderer getInstance() {
        if (null == tooltipCellRenderer) {
            tooltipCellRenderer = new TableTooltipCellRenderer();
        }
        return tooltipCellRenderer;
    }
}