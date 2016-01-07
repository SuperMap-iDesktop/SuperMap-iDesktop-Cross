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

// $Id: CloseOthersWindowAction.java,v 1.2 2005/05/20 14:48:12 johan Exp $
package com.supermap.desktop.ui.docking.action;

import net.infonode.gui.icon.button.CloseIcon;

import javax.swing.*;

import com.supermap.desktop.ui.docking.AbstractTabWindow;
import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.OperationAbortedException;
import com.supermap.desktop.ui.docking.internalutil.InternalDockingUtil;

import java.io.ObjectStreamException;

/**
 * Closes all tabs (with abort possibility) except the one belonging to the window the action is performed upon in the AbstractTabWindow parent of the window.
 *
 * @author $Author: johan $
 * @version $Revision: 1.2 $
 * @since IDW 1.4.0
 */
public class CloseOthersWindowAction extends DockingWindowAction {
	private static final long serialVersionUID = 1;

	/**
	 * The only instance of this class
	 */
	public static final CloseOthersWindowAction INSTANCE = new CloseOthersWindowAction();

	private static final Icon icon = new CloseIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

	private CloseOthersWindowAction() {
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

	@Override
	public String getName() {
		return "Close Others";
	}

	@Override
	public boolean isPerformable(DockingWindow window) {
		return window.getWindowParent() instanceof AbstractTabWindow;
	}

	@Override
	public void perform(DockingWindow window) {
		if (isPerformable(window)) {
			AbstractTabWindow tw = (AbstractTabWindow) window.getWindowParent();
			int childWindowCount = tw.getChildWindowCount();
			int i = 0;
			while (i < childWindowCount) {
				if (tw.getChildWindow(i) != window && tw.getChildWindow(i).isClosable()) {
					try {
						tw.getChildWindow(i).closeWithAbort();
					} catch (OperationAbortedException e) {
						i++;
					}
				} else {
					i++;
				}
			}
		}
	}

	protected Object readResolve() throws ObjectStreamException {
		return INSTANCE;
	}
}
