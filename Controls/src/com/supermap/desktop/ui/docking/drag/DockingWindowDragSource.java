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

// $Id: DockingWindowDragSource.java,v 1.8 2009/02/05 15:57:55 jesper Exp $
package com.supermap.desktop.ui.docking.drag;

import javax.swing.JComponent;

import net.infonode.gui.draggable.DraggableComponent;
import net.infonode.gui.draggable.DraggableComponentAdapter;
import net.infonode.gui.draggable.DraggableComponentEvent;

/**
 * Handles the drag and drop of a {@link com.supermap.desktop.ui.docking.DockingWindow} triggered by mouse events on a {@link JComponent}.
 * {@link DockingWindowDragSource} handles drag abort with the right mouse button and the key set in the
 * {@link com.supermap.desktop.ui.docking.properties.RootWindowProperties#ABORT_DRAG_KEY} property of the {@link com.supermap.desktop.ui.docking.RootWindow}
 * which is the drop target.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.8 $
 * @since IDW 1.3.0
 */
public class DockingWindowDragSource {
	private final DraggableComponent draggableComponent;
	private DockingWindowDragger dragger;

	/**
	 * Constructor.
	 *
	 * @param component the component on which to listen to mouse events that affects the drag and drop of a
	 *            {@link com.supermap.desktop.ui.docking.DockingWindow}
	 * @param draggerProvider provides the {@link DockingWindowDragger} when the drag operation begins, typically this provider gets the dragger by calling
	 *            {@link com.supermap.desktop.ui.docking.DockingWindow#startDrag(com.supermap.desktop.ui.docking.RootWindow)} on the window which should be
	 *            dragged
	 */
	public DockingWindowDragSource(JComponent component, final DockingWindowDraggerProvider draggerProvider) {
		draggableComponent = new DraggableComponent(component);
		draggableComponent.setReorderEnabled(false);
		draggableComponent.setEnableInsideDrag(true);

		draggableComponent.addListener(new DraggableComponentAdapter() {
			@Override
			public void dragAborted(DraggableComponentEvent event) {
				abortDrag();
			}

			@Override
			public void dragged(DraggableComponentEvent event) {
				if (dragger == null) {
					dragger = draggerProvider.getDragger(event.getMouseEvent());

					if (dragger == null) {
						draggableComponent.abortDrag();
						return;
					}

					draggableComponent.setAbortDragKeyCode(dragger.getDropTarget().getRootWindowProperties().getAbortDragKey());
				}

				dragger.dragWindow(event.getMouseEvent());
			}

			@Override
			public void dropped(DraggableComponentEvent event) {
				if (dragger != null) {
					dragger.dropWindow(event.getMouseEvent());
					dragger = null;
				}
			}
		});
	}

	public void abortDrag() {
		if (dragger != null)
			dragger.abortDrag();

		dragger = null;
	}

}
