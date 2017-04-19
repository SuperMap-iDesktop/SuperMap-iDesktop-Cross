package com.supermap.desktop.implement;

import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;

/**
 * Created by xie on 2017/4/18.
 */
public class InnerArrowButton extends BasicArrowButton {
    private Color darkShadow;
    private Color shadow;
    private Color highlight;

    public InnerArrowButton(int direction, Color background, Color shadow, Color darkShadow, Color highlight) {
        super(direction, background, shadow, darkShadow, highlight);
        this.darkShadow = darkShadow;
        this.shadow = shadow;
        this.highlight = highlight;
        setFocusable(true);
    }

    public void paintTriangle(Graphics g, int x, int y, int size,
                              int direction, boolean isEnabled) {
        Color oldColor = g.getColor();
        int mid, i, j;

        j = 0;
        size = Math.max(size, 2) - 1;
        mid = (size / 2) - 1;

        g.translate(x, y);
        if (isEnabled)
            g.setColor(darkShadow);
        else
            g.setColor(shadow);

        if (!isEnabled) {
            g.translate(1, 1);
            g.setColor(highlight);
            for (i = size - 1; i >= 0; i--) {
                g.drawLine(mid - i, j, mid + i, j);
                j++;
            }
            g.translate(-1, -1);
            g.setColor(shadow);
        }

        j = 0;
        for (i = size - 1; i >= 0; i--) {
            g.drawLine(mid - i, j, mid + i, j);
            j++;
        }
        g.translate(-x, -y);
        g.setColor(oldColor);
    }

}
