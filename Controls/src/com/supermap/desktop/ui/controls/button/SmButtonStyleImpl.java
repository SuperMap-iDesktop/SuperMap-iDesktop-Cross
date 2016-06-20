package com.supermap.desktop.ui.controls.button;

import java.awt.*;

/**
 * Created by Administrator on 2016/3/29.
 */
public class SmButtonStyleImpl implements ISmButtonStyle {

	private Dimension preferredSize;
	private Dimension maximumSize;
	private Dimension minimumSize;

	public SmButtonStyleImpl() {
		defaultInit();
	}

	private void defaultInit() {
		preferredSize = new Dimension(80, 25);
		maximumSize = new Dimension(80, 25);
		minimumSize = new Dimension(80, 25);
	}

	@Override
	public Dimension getPreferredSize() {
		return preferredSize;
	}

	@Override
	public Dimension getMaximumSize() {
		return maximumSize;
	}

	@Override
	public Dimension getMinimumSize() {
		return minimumSize;
	}
}
