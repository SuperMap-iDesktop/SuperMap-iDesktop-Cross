package com.supermap.desktop.ui.mdi.plaf;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.plaf.feature.IMdiFeature;
import com.supermap.desktop.ui.mdi.plaf.feature.MdiGroupFeature;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiGroupUIProperties;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiPagesUIProperties;
import com.supermap.desktop.ui.mdi.util.MdiResource;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

// group 右上角的统一管理图标，定义为 group 功能区 groupOprsFeature
// 所有的 tab 标签绘制区域，定义为 TabsFeature

/**
 * group 右上角的统一管理图标，定义为 group 功能区 groupOprsFeature，所有的 tab 标签绘制区域，定义为 TabsFeature
 */
public class MdiGroupUI extends ComponentUI {

	private static final Rectangle ZERO_RECT = new Rectangle(0, 0, 0, 0);

	private MdiGroup group;
	private MosueHandler mouseHandler = new MosueHandler();
	private MouseMotionHandler mouseMotionHandler = new MouseMotionHandler();
	private KeyHandler keyHandler = new KeyHandler();
	private MdiGroupFeature groupFeature;

	public static ComponentUI createUI(JComponent c) {
		return new MdiGroupUI();
	}

	@Override
	public void installUI(JComponent c) {
		this.group = (MdiGroup) c;
		this.group.setLayout(new MdiGroupLayout());
		this.groupFeature = MdiGroupFeature.instance(this.group);
		installIcons();
		installEvents();
	}

	private void installIcons() {
//		MdiResource.putIconSize(MdiResource.CLOSE, 12, 12);
//		MdiResource.putIconSize(MdiResource.FLOAT, 12, 12);
//		MdiResource.putIconSize(MdiResource.LIST, 12, 12);
//		MdiResource.putIconSize(MdiResource.PRE, 12, 12);
//		MdiResource.putIconSize(MdiResource.NEXT, 12, 12);
	}

	private void installEvents() {
		this.group.addMouseListener(this.mouseHandler);
		this.group.addMouseMotionListener(this.mouseMotionHandler);
		this.group.addKeyListener(this.keyHandler);
	}

	@Override
	public void uninstallUI(JComponent c) {
		c.setLayout(null);
		this.group = null;
		uninstallIcons();
		uninstallEvents();
	}

	private void uninstallIcons() {
		MdiResource.resetDefaults();
	}

	private void uninstallEvents() {
		this.group.removeMouseListener(this.mouseHandler);
		this.group.removeMouseMotionListener(this.mouseMotionHandler);
		this.group.removeKeyListener(this.keyHandler);
		this.mouseHandler = null;
		this.mouseMotionHandler = null;
		this.keyHandler = null;
	}

	public IMdiFeature getGroupFeature() {
		return this.groupFeature;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		if (this.group == null) {
			return;
		}

		this.groupFeature.paint(g);
	}

	private class MdiGroupLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}

		@Override
		public void removeLayoutComponent(Component comp) {

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return calculateSize(false);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return calculateSize(true);
		}

		private Dimension calculateSize(boolean minimum) {
			Dimension size = new Dimension(0, 0);
			int height = 0;
			int width = 0;

			// 遍历所有 page 的控件来决定整个 MdiGroup 的 preferedSize
			for (int i = 0; i < MdiGroupUI.this.group.getPageCount(); i++) {
				MdiPage page = MdiGroupUI.this.group.getPageAt(i);
				Dimension pageSize = minimum ? page.getComponent().getMinimumSize() : page.getComponent().getPreferredSize();

				if (pageSize != null) {
					height = Math.max(size.height, height);
					width = Math.max(size.width, width);
				}
			}

			// 宽度加上 group 的左右 Insets，以及 page 的左右 Insets
			width += MdiPagesUIProperties.INSETS.left + MdiPagesUIProperties.INSETS.right;
			width += MdiGroupUIProperties.INSETS.left + MdiGroupUIProperties.INSETS.right;

			// 高度加上 tabs 的高度，group 的上下 Insets，以及 page 的上下 Insets
			height += MdiGroupUI.this.groupFeature.getTabsFeature().getHeight();
			height += MdiPagesUIProperties.INSETS.top + MdiPagesUIProperties.INSETS.bottom;
			height += MdiGroupUIProperties.INSETS.top + MdiGroupUIProperties.INSETS.bottom;

			return new Dimension(width, height);
		}

		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				MdiGroupUI.this.groupFeature.validate();
				MdiGroupUI.this.groupFeature.layout();

				if (MdiGroupUI.this.group.getPageCount() > 0) {
					IMdiFeature pagesFeature = MdiGroupUI.this.groupFeature.getPagesFeature();
					int x = pagesFeature.getX() + MdiPagesUIProperties.INSETS.left;
					int y = pagesFeature.getY() + MdiPagesUIProperties.INSETS.top;
					int width = pagesFeature.getWidth() - MdiPagesUIProperties.INSETS.left - MdiPagesUIProperties.INSETS.right;
					int height = pagesFeature.getHeight() - MdiPagesUIProperties.INSETS.top - MdiPagesUIProperties.INSETS.bottom;

					// page 的 bounds 由 pagesFeature 以及 pages 的 Insets 属性决定
					for (int i = 0; i < MdiGroupUI.this.group.getPageCount(); i++) {
						MdiPage page = MdiGroupUI.this.group.getPageAt(i);
						page.getComponent().setBounds(x, y, width, height);
					}
				}
			}
		}
	}

	private class MosueHandler implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			MdiGroupUI.this.groupFeature.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			MdiGroupUI.this.groupFeature.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			MdiGroupUI.this.groupFeature.mouseReleased(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			MdiGroupUI.this.groupFeature.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			MdiGroupUI.this.groupFeature.mouseExited(e);
		}
	}

	private class MouseMotionHandler implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			MdiGroupUI.this.groupFeature.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			MdiGroupUI.this.groupFeature.mouseMoved(e);
		}
	}

	private class KeyHandler implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			MdiGroupUI.this.groupFeature.keyTyped(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			MdiGroupUI.this.groupFeature.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			MdiGroupUI.this.groupFeature.keyPressed(e);
		}
	}
}
