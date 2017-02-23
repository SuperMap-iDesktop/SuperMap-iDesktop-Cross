package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.graphs.decorator.AbstractDecorator;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public class HotDecorator extends AbstractDecorator {
	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public boolean contains(Point point) {
		return false;
	}

	@Override
	public void draw(Graphics graphics) {

	}
}
