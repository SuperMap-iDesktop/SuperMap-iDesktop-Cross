package com.supermap.desktop.ui.controls.ChooseTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by lixiaoyao on 2017/3/20.
 */
public class MultipleCheckboxTableRenderer implements TableCellRenderer {
    public MultipleCheckboxTableRenderer() {

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel result = initRenderPanel(value);
        if (isSelected) {
            result.setBackground(table.getSelectionBackground());
            result.setForeground(table.getSelectionForeground());
        } else {
            result.setBackground(table.getBackground());
            result.setForeground(table.getForeground());
        }
        return result;
    }

    private JPanel initRenderPanel(Object value) {
        JPanel panelContent = new JPanel();
        JCheckBox checkBox = new JCheckBox();
        if (value instanceof MultipleCheckboxItem) {
            checkBox.setSelected(((MultipleCheckboxItem) value).getSelected());
            checkBox.setText(((MultipleCheckboxItem) value).getText());
        }
        GroupLayout groupLayout = new GroupLayout(panelContent);
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(checkBox)
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(checkBox)
        );

        panelContent.setLayout(groupLayout);
        setComponentTheme(panelContent);
        setComponentTheme(checkBox);
        return panelContent;
    }

    private void setComponentTheme(JComponent component) {
        component.setBackground(UIManager.getColor("List.textBackground"));
        component.setForeground(UIManager.getColor("List.textForeground"));
    }
}
