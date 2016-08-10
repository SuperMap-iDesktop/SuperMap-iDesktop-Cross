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

// $Id: TabAdapter.java,v 1.9 2004/09/22 14:33:49 jesper Exp $
package net.infonode.tabbedpanel;

/**
 * An adapter class for receiving events from a TabbedPanel or a Tab. The methods in this class are empty and it's purpose is to make it easier to create
 * listeners when not all events are of interest.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.9 $
 * @see TabbedPanel
 */
public class TabAdapter implements TabListener {
	@Override
	public void tabAdded(TabEvent event) {
		// do nothing
	}

	@Override
	public void tabRemoved(TabRemovedEvent event) {
		// do nothing
	}

	@Override
	public void tabDragged(TabDragEvent event) {
		// do nothing
	}

	@Override
	public void tabDropped(TabDragEvent event) {
		// do nothing
	}

	@Override
	public void tabDragAborted(TabEvent event) {
		// do nothing
	}

	@Override
	public void tabSelected(TabStateChangedEvent event) {
		// do nothing
	}

	@Override
	public void tabDeselected(TabStateChangedEvent event) {
		// do nothing
	}

	@Override
	public void tabHighlighted(TabStateChangedEvent event) {
		// do nothing
	}

	@Override
	public void tabDehighlighted(TabStateChangedEvent event) {
		// do nothing
	}

	@Override
	public void tabMoved(TabEvent event) {
		// do nothing
	}
}
