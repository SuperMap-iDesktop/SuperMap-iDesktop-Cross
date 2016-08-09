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


// $Id: MinimizeWithAbortWindowAction.java,v 1.2 2005/12/04 13:46:04 jesper Exp $
package com.supermap.desktop.ui.docking.action;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.OperationAbortedException;
import com.supermap.desktop.ui.docking.internalutil.InternalDockingUtil;
import net.infonode.gui.icon.button.MinimizeIcon;

import javax.swing.*;
import java.io.ObjectStreamException;

/**
 * Minimizes a window. The action calls {@link com.supermap.desktop.ui.docking.DockingWindow#minimizeWithAbort()}.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.2 $
 * @since IDW 1.4.0
 */
public final class MinimizeWithAbortWindowAction extends DockingWindowAction {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class.
   */
  public static final MinimizeWithAbortWindowAction INSTANCE = new MinimizeWithAbortWindowAction();

  private static final Icon icon = new MinimizeIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

  private MinimizeWithAbortWindowAction() {
  }

  public String getName() {
    return "Minimize";
  }

  public Icon getIcon() {
    return icon;
  }

  public boolean isPerformable(DockingWindow window) {
    return window != null && !window.isMinimized() && window.isMinimizable();
  }

  public void perform(DockingWindow window) {
    try {
      if (isPerformable(window))
        window.minimizeWithAbort();
    }
    catch (OperationAbortedException e) {
      // Ignore
    }
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
