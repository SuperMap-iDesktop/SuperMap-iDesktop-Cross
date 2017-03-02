package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.decorator.AbstractDecorator;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by highsad on 2017/2/23.
 */
public class SelectedDecorator extends AbstractDecorator {

	private Shape shape;

	@Override
	public Rectangle getBounds() {
		Rectangle rect = (Rectangle) super.getBounds().clone();
		rect.grow(2, 2);
		return rect;
	}

//	@Override
//	public void decorate(AbstractGraph graph) {
//		if (graph.getShape() instanceof RoundRectangle2D) {
//this.shape=shap
//		} else if (graph.getShape() instanceof Ellipse2D) {
//
//		}
//		super.decorate(graph);
//	}

	public SelectedDecorator(GraphCanvas canvas) {
		super(canvas, null);
	}
}
