package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;

import java.awt.*;

/**
 * Created by highsad on 2017/3/23.
 */
public class DefaultLine extends AbstractLine {
	public final static int NORMAL = 1;
	public final static int PREPARING = 2;
	public final static int INVALID = 4;

	private int crossWidth = 6;
	private int status = NORMAL;

	public DefaultLine(GraphCanvas canvas) {
		super(canvas);
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
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
		if (!GraphicsUtil.isPointValid(getStartPoint()) || !GraphicsUtil.isPointValid(getEndPoint())) {
			return null;
		}

		int crossCenterX = Math.min(getStartPoint().x, getEndPoint().x) + (Math.abs(getEndPoint().x - getStartPoint().x)) / 2;
		int crossCenterY = Math.min(getStartPoint().y, getEndPoint().y) + (Math.abs(getEndPoint().y - getStartPoint().y)) / 2;
		int leftTopX = crossCenterX - crossWidth;
		int leftTopY = crossCenterY - crossWidth;
		return this.status == INVALID ? new Rectangle(leftTopX, leftTopY, crossWidth * 2, crossWidth * 2) : null;
	}

	@Override
	public Stroke getStroke() {
		switch (this.status) {
			case INVALID:
			case PREPARING:
				return new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[]{16, 8}, 0);
			case NORMAL:
			default:
				return super.getStroke();
		}
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		Graphics2D graphics2D = (Graphics2D) graphics;

		switch (this.status) {
			case INVALID:
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
				break;
			case PREPARING:

			case NORMAL:
				break;
		}
	}
}
