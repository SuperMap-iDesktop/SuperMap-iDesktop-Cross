package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by highsad on 2016/8/30.
 */
class IconFeature extends AbstractMdiFeature {

	private Icon icon;

	protected IconFeature(Icon icon, MdiGroup group, IMdiFeature parent) {
		super(group, parent);
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}

	@Override
	public List<IMdiFeature> getFeatures() {
		return null;
	}

	@Override
	public int getHeight() {
		return getIcon().getIconHeight();
	}

	@Override
	public int getWidth() {
		return getIcon().getIconWidth();
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.drawImage(((ImageIcon) getIcon()).getImage(), getX(), getY(), getWidth(), getHeight(), ((ImageIcon) getIcon()).getImageObserver());
	}

	public static IconFeature instance(Icon icon, MdiGroup group, IMdiFeature parent) {
		if (group != null && parent != null) {
			return new IconFeature(icon, group, parent);
		}
		return null;
	}
}
