package com.supermap.desktop.implement;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * Created by xie on 2017/4/18.
 */
public class InnerMetaComboBoxIcon implements Icon {
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        JComponent component = (JComponent) c;
        int iconWidth = getIconWidth();

        g.translate(x, y);

        g.setColor(component.isEnabled() ? MetalLookAndFeel.getControlInfo() : MetalLookAndFeel.getControlShadow());
        g.drawLine(1, 1, iconWidth - 1, 1);
        g.drawLine(2, 2, 1 + (iconWidth - 3), 2);
        g.drawLine(3, 3, 2 + (iconWidth - 5), 3);
        g.drawLine(4, 4, 3 + (iconWidth - 7), 4);

        g.translate(-x, -y);
    }

    @Override
    public int getIconWidth() {
        return 8;
    }

    @Override
    public int getIconHeight() {
        return 4;
    }
}
