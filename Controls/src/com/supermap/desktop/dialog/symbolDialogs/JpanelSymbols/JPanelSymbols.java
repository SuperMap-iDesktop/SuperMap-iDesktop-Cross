package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.Resources;
import com.supermap.data.SymbolGroup;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJt
 */
public abstract class JPanelSymbols extends JPanel {

	protected SymbolGroup symbolGroup;
	protected Resources resources;

	public JPanelSymbols() {
		this.setBackground(Color.WHITE);
		FlowLayout mgr = new FlowLayout(FlowLayout.LEADING) {

			public Dimension preferredLayoutSize(Container target) {
				return computeSize(target);
			}

			private Dimension computeSize(Container target) {
				synchronized (target.getTreeLock()) {

					int hgap = getHgap();
					int vgap = getVgap();
					int w = target.getWidth();

					// Let this behave like a regular FlowLayout (single row)
					// if the container hasn't been assigned any size yet
					if (w == 0) {
						w = Integer.MAX_VALUE;
					}

					Insets insets = target.getInsets();
					if (insets == null) {
						insets = new Insets(0, 0, 0, 0);
					}
					int reqdWidth = 0;

					int maxWidth = w - (insets.left + insets.right + hgap * 2);
					int n = target.getComponentCount();
					int x = 0;
					int y = insets.top + vgap; // FlowLayout starts by adding vgap, so do that here too.
					int rowHeight = 0;

					for (int i = 0; i < n; i++) {
						Component c = target.getComponent(i);
						if (c.isVisible()) {
							Dimension d = c.getPreferredSize();
							if (x == 0 || x + d.width <= maxWidth) {
								// fits in current row.
								if (x > 0) {
									x += hgap;
								}
								x += d.width;
								rowHeight = Math.max(rowHeight, d.height);
							} else {
								// Start of new row
								x = d.width;
								y += vgap + rowHeight;
								rowHeight = d.height;
							}
							reqdWidth = Math.max(reqdWidth, x);
						}
					}
					y += rowHeight;
					y += insets.bottom;
					return new Dimension(reqdWidth + insets.left + insets.right, y);
				}
			}
		};
		mgr.setAlignOnBaseline(true);
		this.setLayout(mgr);
	}

	public void setSymbolGroup(Resources resources, final SymbolGroup symbolGroup) {
		if (symbolGroup == null || this.symbolGroup == symbolGroup) {
			return;
		}
		this.resources = resources;
		this.removeAll();
		this.symbolGroup = symbolGroup;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (symbolGroup.getLibrary().getRootGroup() == symbolGroup) {
					initSystemPanels();
				}
				initDefaultPanel();
			}
		});
	}

	protected abstract void initSystemPanels();


	protected abstract void initDefaultPanel();
}
