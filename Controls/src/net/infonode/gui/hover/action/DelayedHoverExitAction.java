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

// $Id: DelayedHoverExitAction.java,v 1.5 2005/02/16 11:28:11 jesper Exp $

package net.infonode.gui.hover.action;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * @author johan
 */
public class DelayedHoverExitAction implements HoverListener {

	private HashMap timers = new HashMap();
	private HashMap exitEvents = new HashMap();

	private HoverListener action;
	private int delay;

	public DelayedHoverExitAction(HoverListener action, int delay) {
		this.action = action;
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public HoverListener getHoverAction() {
		return action;
	}

	public void forceExit(Component component) {
		if (timers.containsKey(component)) {
			((Timer) timers.get(component)).stop();
			timers.remove(component);
			HoverEvent event = (HoverEvent) exitEvents.get(component);
			exitEvents.remove(component);
			action.mouseExited(event);
		}
	}

	@Override
	public void mouseEntered(HoverEvent event) {
		final Component component = event.getSource();

		if (timers.containsKey(component))
			((Timer) timers.get(component)).stop();
		else {
			Timer timer = new Timer(delay, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					forceExit(component);
				}
			});
			timer.setRepeats(false);
			timers.put(component, timer);

			action.mouseEntered(event);
		}
	}

	@Override
	public void mouseExited(HoverEvent event) {
		exitEvents.put(event.getSource(), event);
		((Timer) timers.get(event.getSource())).restart();
	}
}