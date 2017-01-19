package com.supermap.desktop.process.graphics.graphs;

import java.awt.*;

/**
 * Created by highsad on 2017/1/18.
 */
public interface IGraph {

	double getWidth();

	double getHeight();

	void paint(Graphics2D g);
}
