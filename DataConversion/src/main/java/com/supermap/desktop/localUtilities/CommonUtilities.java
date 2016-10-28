package com.supermap.desktop.localUtilities;

import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/28.
 */
public class CommonUtilities {
    private CommonUtilities() {
        //工具类不提供公共构造方法
    }

    /**
     * 修改JComboBox的显示方式
     *
     * @param comboBox
     */
    public static void setComboBoxTheme(JComboBox comboBox) {
        comboBox.setEditable(true);
        ((JTextField) comboBox.getEditor().getEditorComponent()).setEditable(false);
    }

    /**
     * 全选
     *
     * @param table
     */
    public static void selectAll(JTable table) {
        if (table.getRowCount() - 1 < 0) {
            table.setRowSelectionAllowed(true);
        } else {
            table.setRowSelectionAllowed(true);
            // 设置所有项全部选中
            table.setRowSelectionInterval(0, table.getRowCount() - 1);
        }
    }

    /**
     * 反选
     *
     * @param table
     */
    public static void invertSelect(JTable table) {
        int[] temp = table.getSelectedRows();
        ArrayList<Integer> selectedRows = new ArrayList<Integer>();
        for (int index = 0; index < temp.length; index++) {
            selectedRows.add(temp[index]);
        }

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.clearSelection();
        for (int index = 0; index < table.getRowCount(); index++) {
            if (!selectedRows.contains(index)) {
                selectionModel.addSelectionInterval(index, index);
            }
        }
    }

    /**
     * 替换父容器的panel为新的panel
     *
     * @param parentPanel
     * @param newPanel
     */
    public static void replace(JPanel parentPanel, JPanel newPanel) {
        for (int i = 0; i < parentPanel.getComponentCount(); i++) {
            if (parentPanel.getComponent(i) instanceof JPanel) {
                JPanel panel = (JPanel) parentPanel.getComponent(i);
                panel.removeAll();
                panel.add(newPanel, new GridBagConstraintsHelper(0, 0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
                panel.revalidate();
                break;
            }
        }
        parentPanel.repaint();
    }

    /**
     * 根据fileType资源化
     *
     * @param fileType
     * @return
     */
    public static String getDatasetName(String fileType) {
        String resultFileType = fileType;
        String tempFileType = "String_FileType" + fileType;
        if (tempFileType.contains(resultFileType)) {
            boolean isVisibleName = true;
            try {
                DataConversionProperties.getString(tempFileType);
            } catch (Exception e) {
                // 此处通过抛出异常来确定导出类型是否已经对应
                isVisibleName = false;
            }
            if (isVisibleName) {
                resultFileType = DataConversionProperties.getString(tempFileType);
            }
        }
        return resultFileType;
    }
}
