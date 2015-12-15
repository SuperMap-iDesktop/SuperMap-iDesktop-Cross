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


// $Id: CloseWindowAction.java,v 1.5 2005/02/16 11:28:14 jesper Exp $
package com.supermap.desktop.ui.docking.action;

import net.infonode.gui.icon.button.CloseIcon;

import javax.swing.*;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.internalutil.InternalDockingUtil;

import java.io.ObjectStreamException;

/**
 * <p>
 * Closes a window using the {@link DockingWindow#close()} method.
 * </p>
 * <p>
 * In a GUI, you would typically use {@link CloseWithAbortWindowAction} instead of this class.
 * </p>
 *
 * @author $Author: jesper $
 * @version $Revision: 1.5 $
 * @since IDW 1.3.0
 */
public final class CloseWindowAction extends DockingWindowAction {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class
   */
  public static final CloseWindowAction INSTANCE = new CloseWindowAction();

  private static final Icon icon = new CloseIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

  private CloseWindowAction() {
  }

  public Icon getIcon() {
    return icon;
  }

  public String getName() {
    return "Close";
  }

  public boolean isPerformable(DockingWindow window) {
    return window.isClosable();
  }

  public void perform(DockingWindow window) {
    if (isPerformable(window))
      window.close();
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
