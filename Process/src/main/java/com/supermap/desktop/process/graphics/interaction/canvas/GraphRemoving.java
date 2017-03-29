package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.event.KeyEvent;

/**
 * Created by highsad on 2017/3/25.
 */
public class GraphRemoving extends CanvasEventAdapter {
	private GraphCanvas canvas;

	public GraphRemoving(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void clean() {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_DELETE) {
			if (this.canvas.getSelection().size() > 0) {
				this.canvas.removeGraphs(this.canvas.getSelection().getSelectedItems());
			}
		}
	}
}
