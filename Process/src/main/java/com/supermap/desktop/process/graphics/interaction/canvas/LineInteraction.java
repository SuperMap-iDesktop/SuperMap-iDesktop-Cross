package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.GraphConnectionLine;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/4/21.
 */
public class LineInteraction extends CanvasActionAdapter {
	private final static int BOX_WIDTH = 8;
	private GraphConnectionLine selectedLine;
	private Rectangle selectedBox1;
	private Rectangle selectedBox2;
	private GraphCanvas canvas;

	public LineInteraction(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			GraphConnectionLine line = this.canvas.getConnection().find(e.getPoint());
			GraphConnectionLine preSelected = this.selectedLine;

			if (line != null) {
				this.selectedLine = line;
				Point start = this.selectedLine.getStartPoint();
				Point end = this.selectedLine.getEndPoint();
				this.selectedBox1 = new Rectangle(start.x - BOX_WIDTH / 2, start.y - BOX_WIDTH / 2, BOX_WIDTH, BOX_WIDTH);
				this.selectedBox2 = new Rectangle(end.x - BOX_WIDTH / 2, end.y - BOX_WIDTH / 2, BOX_WIDTH, BOX_WIDTH);
			} else {
				this.selectedLine = null;
			}

			if (preSelected != this.selectedLine) {
				this.canvas.repaint();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_DELETE) {
			if (this.selectedLine != null) {
				if (this.selectedLine.getStartGraph() instanceof ProcessGraph
						&& this.selectedLine.getEndGraph() instanceof OutputGraph) {
					return;
				}

				this.canvas.getConnection().removeConnectLine(this.selectedLine);
				this.selectedLine = null;
				this.selectedBox1 = null;
				this.selectedBox2 = null;
				this.canvas.repaint();
			}
		}
	}

	public void paint(Graphics g) {
		if (this.selectedLine != null) {
			Graphics2D graphics2D = (Graphics2D) g;

			boolean canDelete = this.selectedLine.getStartGraph() instanceof OutputGraph && this.selectedLine.getEndGraph() instanceof ProcessGraph;
			Color backColor = canDelete ? Color.WHITE : Color.MAGENTA;
			graphics2D.setColor(backColor);
			graphics2D.fill(this.selectedBox1);
			graphics2D.fill(this.selectedBox2);

			graphics2D.setColor(Color.BLACK);
			graphics2D.draw(this.selectedBox1);
			graphics2D.draw(this.selectedBox2);
		}
	}

	@Override
	public void clean() {
		this.selectedLine = null;
		this.selectedBox1 = null;
		this.selectedBox2 = null;
	}
}
