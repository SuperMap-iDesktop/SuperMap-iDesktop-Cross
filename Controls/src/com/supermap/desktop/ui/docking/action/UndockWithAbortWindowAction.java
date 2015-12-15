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


// $Id: UndockWithAbortWindowAction.java,v 1.3 2005/12/04 13:46:04 jesper Exp $

package com.supermap.desktop.ui.docking.action;

import net.infonode.gui.icon.button.UndockIcon;

import javax.swing.*;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.OperationAbortedException;
import com.supermap.desktop.ui.docking.internalutil.InternalDockingUtil;

import java.awt.*;
import java.io.ObjectStreamException;

/**
 * Undocks a window using the {@link DockingWindow#undockWithAbort(Point)} method.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class UndockWithAbortWindowAction extends DockingWindowAction {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class
   */
  public static final UndockWithAbortWindowAction INSTANCE = new UndockWithAbortWindowAction();

  private static final Icon icon = new UndockIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

  private UndockWithAbortWindowAction() {
  }

  public Icon getIcon() {
    return icon;
  }

  public String getName() {
    return UndockWindowAction.INSTANCE.getName();
  }

  public boolean isPerformable(DockingWindow window) {
    return UndockWindowAction.INSTANCE.isPerformable(window);
  }

  public void perform(DockingWindow window) {
    if (isPerformable(window)) {
      Point p = window.getLocation();
      SwingUtilities.convertPointToScreen(p, window.getParent());
      try {
        window.undockWithAbort(p);
      }
      catch (OperationAbortedException e) {
        // Ignore
      }
    }
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
