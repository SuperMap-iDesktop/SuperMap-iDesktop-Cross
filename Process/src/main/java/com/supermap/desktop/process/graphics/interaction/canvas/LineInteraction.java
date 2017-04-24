package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.GraphRelationLine;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/4/21.
 */
public class LineInteraction extends CanvasActionAdapter {
	private GraphRelationLine selectedLine;
	private GraphCanvas canvas;

	public LineInteraction(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			GraphRelationLine line = this.canvas.getConnection().find(e.getPoint());
			if (line != null) {
				this.selectedLine = line;
			} else {
				this.selectedLine = null;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_DELETE) {
			if (this.selectedLine != null) {
				this.canvas.getConnection().removeConnectLine(this.selectedLine);
			}
		}
	}

	@Override
	public void clean() {

	}
}
