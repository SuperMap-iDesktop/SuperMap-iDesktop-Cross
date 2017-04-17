package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.decorator.PreviewDecorator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/3/8.
 */
public class GraphCreator extends CanvasEventAdapter {

	private GraphCanvas canvas;
	private PreviewDecorator previewDecorator;
	private IGraph toCreation;
	private Rectangle creationRegion = new Rectangle(0, 0, 0, 0);

	public GraphCreator(GraphCanvas canvas) {
		this.canvas = canvas;
		this.previewDecorator = new PreviewDecorator(this.canvas);
	}

	public void create(IGraph toCreation) {
		if (!this.canvas.getGraphStorage().contains(toCreation)) {
			this.toCreation = toCreation;

			if (this.toCreation instanceof AbstractGraph) {
				this.previewDecorator.decorate((AbstractGraph) this.toCreation);
			}

			this.canvas.setActionEnabled(Selection.class, false);
			this.canvas.setActionEnabled(DraggedHandler.class, false);
		}
	}

	public void stop() {
		clean();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			stop();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (this.isCreating() && SwingUtilities.isLeftMouseButton(e)) {
			this.canvas.addGraphTransformed(this.toCreation);
			clean();
		}
	}

	/**
	 * 随鼠标移动预览对象
	 * Follow the mouse to move the preview graph.
	 * The mosue location is the equivalent of the center of the preview graph.
	 *
	 * @param e
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		if (isCreating()) {
			Point location = e.getPoint();
			Rectangle dirtry = (Rectangle) this.creationRegion.clone();
			int x = location.x - this.toCreation.getWidth() / 2;
			int y = location.y - this.toCreation.getHeight() / 2;
			this.toCreation.setLocation(new Point(x, y));
			this.creationRegion.setBounds(x, y, this.toCreation.getWidth(), this.toCreation.getHeight());

			if (GraphicsUtil.isRegionValid(dirtry)) {
				dirtry = dirtry.union(this.creationRegion);
			} else {
				dirtry = this.creationRegion;
			}
			this.canvas.repaint(dirtry);
		}
	}


	@Override
	public void clean() {
		try {
			this.toCreation = null;
			this.previewDecorator.undecorate();

			if (GraphicsUtil.isRegionValid(this.creationRegion)) {
				this.canvas.repaint(this.creationRegion);
			} else {
				this.canvas.repaint();
			}
			this.creationRegion.setBounds(0, 0, 0, 0);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.canvas.setActionEnabled(Selection.class, true);
			this.canvas.setActionEnabled(DraggedHandler.class, true);
		}
	}

	public void paint(Graphics g) {
		if (isCreating() && this.previewDecorator.isDecorating()) {
			this.canvas.getPainterFactory().getPainter(this.previewDecorator, g).paint();
		}
	}

	public boolean isCreating() {
		return this.toCreation != null;
	}
}
