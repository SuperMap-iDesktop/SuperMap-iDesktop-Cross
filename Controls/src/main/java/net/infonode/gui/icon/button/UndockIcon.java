/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */


// $Id: UndockIcon.java,v 1.7 2005/12/04 13:46:03 jesper Exp $
package net.infonode.gui.icon.button;

import net.infonode.gui.GraphicsUtil;

import java.awt.*;

/**
 * @author johan
 */
public class UndockIcon extends AbstractButtonIcon {
  private static final long serialVersionUID = 1;

  public UndockIcon() {
    super();
  }

  public UndockIcon(Color color) {
    super(color);
  }

  public UndockIcon(Color color, int size) {
    super(color, size);
  }

  public UndockIcon(int size) {
    super(size);
  }

  protected void paintIcon(Component component, final Graphics graphics, final int x1, final int y1, final int x2, final int y2) {
    int xOffs = (x2 - x1) > 6 ? 1 : 0;
    int yOffs = xOffs;

    // Top right
    GraphicsUtil.drawOptimizedLine(graphics, x2 - xOffs - 3, y1 + yOffs, x2 - xOffs, y1 + yOffs);
    GraphicsUtil.drawOptimizedLine(graphics, x2 - xOffs, y1 + yOffs, x2 - xOffs, y1 + yOffs + 3);

    // Bottom left
    GraphicsUtil.drawOptimizedLine(graphics, x1 + xOffs + 1, y2 - yOffs - 2, x1 + xOffs + 1, y2 - yOffs);
    GraphicsUtil.drawOptimizedLine(graphics, x1 + xOffs + 2, y2 - yOffs + 1, x1 + xOffs + 2, y2 - yOffs + 1);

    // Lines
    GraphicsUtil.drawOptimizedLine(graphics, x1 + xOffs + 1, y2 - yOffs - 2, x2 - xOffs - 1, y1 + yOffs);
    GraphicsUtil.drawOptimizedLine(graphics, x1 + xOffs + 1, y2 - yOffs - 1, x2 - xOffs, y1 + yOffs);
    GraphicsUtil.drawOptimizedLine(graphics, x1 + xOffs + 1, y2 - yOffs, x2 - xOffs, y1 + yOffs + 1);
  }
}
