package com.supermap.desktop.WorkflowView.graphics.interaction.canvas;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/3/2.
 */
public class SingleSelection extends Selection {
	public SingleSelection(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public boolean isSelecting() {
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
	}

	@Override
	public void paint(Graphics graphics) {

	}

	@Override
	public void clean() {

	}
}
