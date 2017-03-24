package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;

/**
 * Created by highsad on 2017/3/23.
 */
public class DefaultLine extends AbstractLine {
	private boolean isEnabled = true;
	private int crossWidth = 4;

	public DefaultLine(GraphCanvas canvas) {
		super(canvas);
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		Rectangle crossBounds = getCrossBounds();
		return crossBounds == null ? bounds : bounds.union(crossBounds);
	}

	public Point getCenter() {
		if (getStartPoint() != null && getEndPoint() != null) {
			int crossCenterX = getStartPoint().x + (getEndPoint().x - getStartPoint().x) / 2;
			int crossCenterY = getStartPoint().y + (getEndPoint().y - getStartPoint().y) / 2;
			return new Point(crossCenterX, crossCenterY);
		} else {
			return null;
		}
	}

	public Rectangle getCrossBounds() {
		int crossCenterX = Math.min(getStartPoint().x, getEndPoint().x) + (Math.abs(getEndPoint().x - getStartPoint().x)) / 2;
		int crossCenterY = Math.min(getStartPoint().y, getEndPoint().y) + (Math.abs(getEndPoint().y - getStartPoint().y)) / 2;
		int leftTopX = crossCenterX - crossWidth;
		int leftTopY = crossCenterY - crossWidth;
		return !this.isEnabled ? new Rectangle(leftTopX, leftTopY, crossWidth * 2, crossWidth * 2) : null;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);

		if (!isEnabled) {
			Graphics2D graphics2D = (Graphics2D) graphics;
			if (getStartPoint() != null && getEndPoint() != null) {
				graphics.setColor(Color.RED);
				Stroke originStroke = graphics2D.getStroke();
				BasicStroke stroke = new BasicStroke(2);
				graphics2D.setStroke(stroke);

				int crossCenterX = Math.min(getStartPoint().x, getEndPoint().x) + (Math.abs(getEndPoint().x - getStartPoint().x)) / 2;
				int crossCenterY = Math.min(getStartPoint().y, getEndPoint().y) + (Math.abs(getEndPoint().y - getStartPoint().y)) / 2;
				graphics2D.drawLine(crossCenterX - crossWidth, crossCenterY - crossWidth, crossCenterX + crossWidth, crossCenterY + crossWidth);
				graphics.drawLine(crossCenterX - crossWidth, crossCenterY + crossWidth, crossCenterX + crossWidth, crossCenterY - crossWidth);
				graphics2D.setStroke(originStroke);
			}
		}
	}
}
