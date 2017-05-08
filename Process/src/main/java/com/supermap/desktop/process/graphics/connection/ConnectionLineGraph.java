package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/3/23.
 */
public class ConnectionLineGraph extends LineGraph {
	private IConnection connection;
	private Font font = new Font("宋体", Font.PLAIN, 20);
	private Color textColor = Color.BLACK;
	private boolean isEditable = true;
	private boolean isSelected = true;

	private GraphBoundsChangedListener startGraphBoundsChangedListener = new GraphBoundsChangedListener() {
		@Override
		public void graghBoundsChanged(GraphBoundsChangedEvent e) {

		}
	};

	private GraphBoundsChangedListener endGraphBoundsChangedListener = new GraphBoundsChangedListener() {
		@Override
		public void graghBoundsChanged(GraphBoundsChangedEvent e) {

		}
	};

	public ConnectionLineGraph(GraphCanvas canvas, IConnection connection) {
		super(canvas);
		this.connection = connection;
		IGraph startGraph = this.connection.getStartGraph();
		IGraph endGraph = this.connection.getEndGraph();

		if (startGraph != null && endGraph != null) {
			startGraph.addGraphBoundsChangedListener(this.startGraphBoundsChangedListener);
			endGraph.addGraphBoundsChangedListener(this.endGraphBoundsChangedListener);
		}
	}

	public IConnection getConnection() {
		return this.connection;
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

	public void startGraphBoundsChanged(GraphBoundsChangedEvent e) {

	}

	public void endGraphBoundsChanged(GraphBoundsChangedEvent e) {

	}

	@Override
	public void paint(Graphics graphics) {
//		super.paint(graphics);
//		Graphics2D graphics2D = (Graphics2D) graphics;
//		Point startP = getStartPoint();
//		Point endP = getEndPoint();
//
//		if (startP != null && endP != null && !StringUtilities.isNullOrEmpty(this.message)) {
//			graphics.setColor(this.textColor);
//			int textX = Math.min(getStartPoint().x, getEndPoint().x) + (Math.abs(getEndPoint().x - getStartPoint().x)) / 2;
//			int textY = Math.min(getStartPoint().y, getEndPoint().y) + (Math.abs(getEndPoint().y - getStartPoint().y)) / 2;
//			graphics2D.drawString(this.message, textX, textY);
//		}
	}
}
