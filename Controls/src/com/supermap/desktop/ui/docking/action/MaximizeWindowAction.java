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


// $Id: MaximizeWindowAction.java,v 1.6 2005/12/04 13:46:04 jesper Exp $
package com.supermap.desktop.ui.docking.action;

import net.infonode.gui.icon.button.MaximizeIcon;

import javax.swing.*;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.TabWindow;
import com.supermap.desktop.ui.docking.internalutil.InternalDockingUtil;
import com.supermap.desktop.ui.docking.util.DockingUtil;

import java.io.ObjectStreamException;

/**
 * Maximizes a {@link TabWindow}. Finds the parent {@link TabWindow} if the window this action is performed on is not
 * a {@link TabWindow}.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.6 $
 * @since IDW 1.3.0
 */
public final class MaximizeWindowAction extends DockingWindowAction {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class.
   */
  public static final MaximizeWindowAction INSTANCE = new MaximizeWindowAction();

  private static final Icon icon = new MaximizeIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

  private MaximizeWindowAction() {
  }

  public Icon getIcon() {
    return icon;
  }

  public String getName() {
    return "Maximize";
  }

  public boolean isPerformable(DockingWindow window) {
    if (!window.isMaximizable())
      return false;

    TabWindow tabWindow = DockingUtil.getTabWindowFor(window);
    return tabWindow != null && !tabWindow.isMaximized() && tabWindow.isMaximizable();
  }

  public void perform(DockingWindow window) {
    TabWindow tabWindow = DockingUtil.getTabWindowFor(window);

    if (tabWindow != null && !tabWindow.isMaximized() && tabWindow.isMaximizable())
      tabWindow.maximize();
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
