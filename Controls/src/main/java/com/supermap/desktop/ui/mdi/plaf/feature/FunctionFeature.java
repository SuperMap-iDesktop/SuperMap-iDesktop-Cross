package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.action.IMdiAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2016/9/19.
 */
public class FunctionFeature extends IconFeature {

	private static final int NORMAL = 0;
	private static final int ACTIVE = 1;
	private IMdiAction function;
	private int state = NORMAL;
	private MdiPage page;

	protected FunctionFeature(IMdiAction function, MdiGroup group, MdiPage actionPage, IMdiFeature parent) {
		super(function.getIcon(), group, parent);
		this.function = function;
		this.page = actionPage;
	}

	public MdiPage getPage() {
		return this.page == null ? getGroup().getActivePage() : this.page;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		super.mouseExited(e);
		this.state = NORMAL;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		if (this.function.enabled(getPage())) {
			this.function.action(getPage(), this);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
		this.state = ACTIVE;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		this.state = ACTIVE;
		repaint();
	}


	@Override
	public Icon getIcon() {
		if (this.state == ACTIVE) {
			return this.function.getIconActive();
		}

		if (this.state == NORMAL) {
			return this.function.getIcon();
		}
		return super.getIcon();
	}

	@Override
	public void paint(Graphics graphics) {
		ImageIcon icon = (ImageIcon) this.function.getIcon();

		if (!this.function.enabled(getPage())) {
			icon = (ImageIcon) this.function.getIconDisable();
		} else {
			if (this.state == ACTIVE) {
				icon = (ImageIcon) this.function.getIconActive();
			}
		}

		graphics.drawImage(icon.getImage(), getX(), getY(), getWidth(), getHeight(), icon.getImageObserver());
	}

	public static FunctionFeature instance(IMdiAction function, MdiGroup group, IMdiFeature parent) {
		if (function != null && group != null && parent != null) {
			return new FunctionFeature(function, group, group.getActivePage(), parent);
		}
		return null;
	}

	public static FunctionFeature instance(IMdiAction function, MdiGroup group, MdiPage actionPage, IMdiFeature parent) {
		if (function != null && group != null && parent != null) {
			return new FunctionFeature(function, group, actionPage, parent);
		}
		return null;
	}
}
