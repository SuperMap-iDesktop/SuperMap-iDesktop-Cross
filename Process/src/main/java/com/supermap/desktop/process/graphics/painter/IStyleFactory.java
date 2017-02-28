package com.supermap.desktop.process.graphics.painter;

import java.awt.*;

/**
 * 用于在 {@link IGraphPainter} 中对 IGraph 进行绘制时，定制对应 GraphPainter 的 Graphics 绘制属性。
 * Created by highsad on 2017/2/25.
 */
public interface IStyleFactory {
	void normalPoint(Graphics graphics);

	void normalLine(Graphics graphics);

	void border(Graphics graphics);

	void normalRegion(Graphics graphics);

	void normalText(Graphics graphics);

	void hotPoint(Graphics graphics);

	void hotLine(Graphics graphics);

	void hotBorder(Graphics graphics);

	void hotRegion(Graphics graphics);

	void hotText(Graphics graphics);

	void selectedPoint(Graphics graphics);

	void selectedLine(Graphics graphics);

	void selectedBorder(Graphics graphics);

	void selectedRegion(Graphics graphics);

	void selectedText(Graphics graphics);

	void unablePoint(Graphics graphics);

	void unableLine(Graphics graphics);

	void unableRegion(Graphics graphics);

	void unableText(Graphics graphics);

	void previewPoint(Graphics graphics);

	void previewLine(Graphics graphics);

	void previewBorder(Graphics graphics);

	void previewRegion(Graphics graphics);

	void previewText(Graphics graphics);

	void progressLine(Graphics graphics);

	void progressRegion(Graphics graphics);

	void progressText(Graphics graphics);
}
