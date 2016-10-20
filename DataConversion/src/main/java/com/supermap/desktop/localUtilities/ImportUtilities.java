package com.supermap.desktop.localUtilities;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by xie on 2016/10/18.
 * 导入工具类，实现导入中的具体方法
 */
public class ImportUtilities {
    private ImportUtilities() {
        //工具类，不提供公共的构造函数
    }

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
}
