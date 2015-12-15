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

// $Id: DeveloperUtil.java,v 1.9 2007/01/28 21:25:10 jesper Exp $
package com.supermap.desktop.ui.docking.util;

import javax.swing.*;

import com.supermap.desktop.ui.docking.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 * Utility methods to make certain tasks easier during the development of an application using IDW.
 * </p>
 *
 * <p>
 * <strong>Note:</strong> These methods might be changed/removed or not be compatible with future versions of IDW.
 * </p>
 *
 * @author $Author: jesper $
 * @version $Revision: 1.9 $
 * @since IDW 1.4.0
 */
public class DeveloperUtil {
	private static String INDENT_STRING = "    ";

	/**
	 * <p>
	 * Returns a Java code pseudo-like string with information about the current window layout in a docking window.
	 * </p>
	 *
	 * <p>
	 * If the given window is a root window a complete layout is returned i.e. windows inside the root window, windows on window bars and floating windows. This
	 * is useful when for example creating a default layout. Just add all the views to the root window, drag them around to create a nice layout and the call
	 * this function to retrieve the layout as a string.
	 * </p>
	 *
	 * <p>
	 * <strong>Note:</strong> The returned string contains pseudo-like Java code. All views in the layout are called <i>View: "title" - view class</i>.
	 * </p>
	 *
	 * <p>
	 * <strong>Note:</strong> The method might be changed/removed or not be compatible with future versions of IDW.
	 * </p>
	 *
	 * @param window the docking window to retrieve layout for
	 * @return the layout as a pseudo-like Java code
	 */
	private DeveloperUtil() {
		// 工具类，不提供构造函数
	}

	public static String getWindowLayoutAsString(DockingWindow window) {
		return getDockingWindowLayout(window, 0);
	}

	/**
	 * <p>
	 * Creates a JFrame with a text area that shows the layout of the given window as pseudo-like Java code, i.e. the layout retrieved by
	 * {@link DeveloperUtil#getWindowLayoutAsString(DockingWindow)}. The frame also has a button that when clicked gets the current layout from the window.
	 * </p>
	 *
	 * <p>
	 * The frame is useful when designing window layouts in an application. Just create a frame and use your root window as window. Drag around your views,
	 * press the "Get Layout" button and you'll se your layout in the text area.
	 * </p>
	 *
	 * <p>
	 * <strong>Note:</strong> The method might be changed/removed or not be compatible with future versions of IDW.
	 * </p>
	 *
	 * @param title frame title
	 * @param window the docking window to retrieve layout for
	 * @return the frame
	 */
	public static JFrame createWindowLayoutFrame(String title, final DockingWindow window) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final JTextArea layoutArea = new JTextArea(getWindowLayoutAsString(window));
		JButton getLayoutButton = new JButton("Get Current Layout");
		getLayoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layoutArea.setText(getWindowLayoutAsString(window));
			}
		});

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(getLayoutButton);

		frame.getContentPane().add(new JScrollPane(layoutArea), BorderLayout.CENTER);
		frame.getContentPane().add(box, BorderLayout.NORTH);

		frame.pack();

		return frame;
	}

	private static String getDockingWindowLayout(DockingWindow window, int depth) {
		if (window instanceof RootWindow)
			return getRootWindowLayout((RootWindow) window, depth);

		String s = depth > 0 ? "\n" : "";

		for (int i = 0; i < depth; i++)
			s += INDENT_STRING;

		if (window instanceof TabWindow)
			s += getTabWindowLayout((TabWindow) window, depth + 1);
		else if (window instanceof SplitWindow)
			s += getSplitWindowLayout((SplitWindow) window, depth + 1);
		else
			s += getViewLayout((View) window, depth + 1);

		return s;
	}

	private static String getRootWindowLayout(RootWindow window, int depth) {
		String s = "";
		if (window.getWindow() != null)
			s += "<rootWindow>.setWindow(" + getDockingWindowLayout(window.getWindow(), depth) + ");\n\n";

		for (int i = 0; i < window.getChildWindowCount(); i++) {
			DockingWindow w = window.getChildWindow(i);
			if (w != window.getWindow()) {
				if (w instanceof WindowBar) {
					WindowBar bar = (WindowBar) w;

					if (bar.getChildWindowCount() > 0) {
						for (int k = 0; k < bar.getChildWindowCount(); k++)
							s += "<rootWindow>.getWindowBar(Direction." + bar.getDirection().toString().toUpperCase() + ").addTab("
									+ getDockingWindowLayout(bar.getChildWindow(k), depth) + ");\n";
						s += "\n";
					}
				} else if (w instanceof FloatingWindow) {
					FloatingWindow fw = (FloatingWindow) w;
					Point loc = fw.getTopLevelAncestor().getLocation();
					Dimension size = fw.getRootPane().getSize();
					s += "<rootWindow>.createFloatingWindow(new Point(" + loc.x + ", " + loc.y + "), new Dimension(" + size.width + ", " + size.height + "), ";
					s += getDockingWindowLayout(fw.getChildWindow(0), depth);
					s += ");\n\n";
				}
			}
		}

		return s;
	}

	private static String getTabWindowLayout(TabWindow window, int depth) {
		if (window.getChildWindowCount() == 1 && window.getChildWindow(0) instanceof View) {
			return getViewLayout((View) window.getChildWindow(0), depth);
		}

		String s = "new TabWindow(new DockingWindow[]{";

		for (int i = 0; i < window.getChildWindowCount(); i++) {
			s += getDockingWindowLayout(window.getChildWindow(i), depth);
			if (i < window.getChildWindowCount() - 1)
				s += ", ";
		}

		s += "})";

		return s;
	}

	private static String getSplitWindowLayout(SplitWindow window, int depth) {
		String s = "new SplitWindow(" + window.isHorizontal() + ", " + window.getDividerLocation() + "f, ";
		s += getDockingWindowLayout(window.getLeftWindow(), depth) + ", ";
		s += getDockingWindowLayout(window.getRightWindow(), depth);
		s += ")";

		return s;
	}

	private static String getViewLayout(View view, int depth) {
		return "View: \"" + view.getTitle() + "\" - " + view.getClass();
	}
}
