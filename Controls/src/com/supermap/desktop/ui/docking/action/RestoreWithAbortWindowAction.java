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

// $Id: RestoreWithAbortWindowAction.java,v 1.3 2007/01/28 21:25:10 jesper Exp $
package com.supermap.desktop.ui.docking.action;

import net.infonode.gui.icon.button.RestoreIcon;

import javax.swing.*;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.OperationAbortedException;
import com.supermap.desktop.ui.docking.internalutil.InternalDockingUtil;

import java.io.ObjectStreamException;

/**
 * Restores a window using the {@link DockingWindow#restore()} method. The action calls {@link com.supermap.desktop.ui.docking.DockingWindow#restoreWithAbort()}
 * .
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class RestoreWithAbortWindowAction extends DockingWindowAction {
	private static final long serialVersionUID = 1;

	/**
	 * The only instance of this class.
	 */
	public static final RestoreWithAbortWindowAction INSTANCE = new RestoreWithAbortWindowAction();

	private static final Icon icon = new RestoreIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

	private RestoreWithAbortWindowAction() {
	}

	@Override
	public String getName() {
		return "Restore";
	}

	@Override
	public boolean isPerformable(DockingWindow window) {
		return window != null && (window.isMinimized() || window.isMaximized()) && window.isRestorable();
	}

	@Override
	public void perform(DockingWindow window) {
		try {
			if (window != null && window.isRestorable())
				window.restoreWithAbort();
		} catch (OperationAbortedException e) {
			// Ignore
		}
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

	protected Object readResolve() throws ObjectStreamException {
		return INSTANCE;
	}

}
