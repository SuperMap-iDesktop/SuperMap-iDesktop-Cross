package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.parameter.interfaces.datas.InputData;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.utilities.StringUtilities;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/3/23.
 */
public class GraphRelationLine extends AbstractLine implements GraphBoundsChangedListener {
	private IGraph start;
	private IGraph end;
	private String message;
	private Font font = new Font("宋体", Font.PLAIN, 20);
	private Color textColor = Color.BLACK;
	private boolean isEditable = true;
	private boolean isSelected = true;

	public GraphRelationLine(GraphCanvas canvas, IGraph start, IGraph end) {
		this(canvas, start, end, null);
	}

	public GraphRelationLine(GraphCanvas canvas, IGraph start, IGraph end, String message) {
		super(canvas);
		setStartGraph(start);
		setEndGraph(end);
		this.message = message;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean editable) {
		isEditable = editable;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

//	@Override
//	public Point getStartPoint() {
//		return this.start != null ? this.start.getCenter() : null;
//	}

//	@Override
//	public Point getEndPoint() {
//		return this.end != null ? this.end.getCenter() : null;
//	}

	public IGraph getStartGraph() {
		return this.start;
	}

	public void setStartGraph(IGraph start) {
		if (this.start != null) {
			this.start.removeGraghBoundsChangedListener(this);
		}

		this.start = start;

		if (this.start != null) {
			setStartPoint(getStartLocation());
			this.start.addGraphBoundsChangedListener(this);
		} else {
			setStartPoint(null);
		}
	}

	public IGraph getEndGraph() {
		return this.end;
	}

	public void setEndGraph(IGraph end) {
		if (this.end != null) {
			this.end.removeGraghBoundsChangedListener(this);
		}

		this.end = end;

		if (this.end != null) {
			setEndPoint(getEndLocation());
			this.end.addGraphBoundsChangedListener(this);
		} else {
			setStartPoint(null);
		}
	}

	public void clear() {
		if ((this.start instanceof OutputGraph) && (this.end instanceof ProcessGraph)) {
			OutputData output = ((OutputGraph) this.start).getProcessData();
			InputData[] inputs = ((ProcessGraph) this.end).getProcess().getInputs().getDatas();

			for (int i = 0; i < inputs.length; i++) {
				InputData input = inputs[i];
				if (input.isBind(output)) {
					input.unbind();
				}
			}
		}
	}

	@Override
	public void graghBoundsChanged(GraphBoundsChangedEvent e) {
		setStartPoint(getStartLocation());
		setEndPoint(getEndLocation());
	}

	private Point getEndLocation() {
		if (this.start != null && this.end != null) {
			return GraphicsUtil.chop(((AbstractGraph) this.end).getShape(), this.start.getCenter());
		} else {
			return null;
		}
	}

	private Point getStartLocation() {
		if (this.start != null && this.end != null) {
			return GraphicsUtil.chop(((AbstractGraph) this.start).getShape(), this.end.getCenter());
		} else {
			return null;
		}
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		return StringUtilities.isNullOrEmpty(this.message) ? bounds : bounds.union(getTextBounds());
	}

	private Rectangle getTextBounds() {
		if (!GraphicsUtil.isPointValid(getStartPoint()) || !GraphicsUtil.isPointValid(getEndPoint())) {
			return null;
		}

		int textX = Math.min(getStartPoint().x, getEndPoint().x) + (Math.abs(getEndPoint().x - getStartPoint().x)) / 2;
		int textY = Math.min(getStartPoint().y, getEndPoint().y) + (Math.abs(getEndPoint().y - getStartPoint().y)) / 2;
		int textWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(this.font), this.message);
		int textHeight = GraphicsUtil.getFontHeight(getCanvas(), this.font);
		return StringUtilities.isNullOrEmpty(this.message) ? null : new Rectangle(textX, textY, textWidth, textHeight);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		Graphics2D graphics2D = (Graphics2D) graphics;
		Point startP = getStartPoint();
		Point endP = getEndPoint();

		if (startP != null && endP != null && !StringUtilities.isNullOrEmpty(this.message)) {
			graphics.setColor(this.textColor);
			int textX = Math.min(getStartPoint().x, getEndPoint().x) + (Math.abs(getEndPoint().x - getStartPoint().x)) / 2;
			int textY = Math.min(getStartPoint().y, getEndPoint().y) + (Math.abs(getEndPoint().y - getStartPoint().y)) / 2;
			graphics2D.drawString(this.message, textX, textY);
		}

//		startP = getStartLocation();
//		endP = getEndLocation();
//		if (startP != null && endP != null && this.isSelected) {
//			Rectangle startR = new Rectangle(startP.x - 3, startP.y - 3, 6, 6);
//			Rectangle endR = new Rectangle(endP.x - 3, endP.y - 3, 6, 6);
//			graphics.setColor(Color.WHITE);
//			((Graphics2D) graphics).fill(startR);
//			((Graphics2D) graphics).fill(endR);
//			graphics.setColor(Color.BLACK);
//			((Graphics2D) graphics).draw(startR);
//			((Graphics2D) graphics).draw(endR);
//		}
	}
}
