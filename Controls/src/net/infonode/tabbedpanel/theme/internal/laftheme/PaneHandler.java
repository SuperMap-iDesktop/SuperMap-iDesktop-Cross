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

// $Id: PaneHandler.java,v 1.7 2005/12/04 13:46:05 jesper Exp $
package net.infonode.tabbedpanel.theme.internal.laftheme;

import net.infonode.gui.DynamicUIManager;
import net.infonode.gui.DynamicUIManagerListener;
import net.infonode.util.Direction;

import javax.swing.*;

import java.awt.*;

public class PaneHandler {
	private JFrame frame;

	private PanePainter[] panePainters;

	private PaneHandlerListener listener;

	private DynamicUIManagerListener uiListener = new DynamicUIManagerListener() {
		@Override
		public void lookAndFeelChanged() {
			doUpdate();
		}

		@Override
		public void propertiesChanging() {
			listener.updating();
		}

		@Override
		public void propertiesChanged() {
			doUpdate();
		}

		@Override
		public void lookAndFeelChanging() {
			listener.updating();
		}

	};

	PaneHandler(PaneHandlerListener listener) {
		this.listener = listener;

		DynamicUIManager.getInstance().addPrioritizedListener(uiListener);

		Direction[] directions = Direction.getDirections();
		panePainters = new PanePainter[directions.length];

		JPanel panel = new JPanel(null);
		for (int i = 0; i < directions.length; i++) {
			panePainters[i] = new PanePainter(directions[i]);
			panel.add(panePainters[i]);
			panePainters[i].setBounds(0, 0, 600, 600);
		}

		frame = new JFrame();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.pack();
	}

	void dispose() {
		if (frame != null) {
			DynamicUIManager.getInstance().removePrioritizedListener(uiListener);
			frame.removeAll();
			frame.dispose();
			frame = null;
		}
	}

	PanePainter getPainter(Direction d) {
		for (int i = 0; i < panePainters.length; i++)
			if (panePainters[i].getDirection() == d)
				return panePainters[i];

		return null;
	}

	JFrame getFrame() {
		return frame;
	}

	void update() {
		listener.updating();

		doUpdate();
	}

	private void doUpdate() {
		SwingUtilities.updateComponentTreeUI(frame);
		listener.updated();
	}
}
