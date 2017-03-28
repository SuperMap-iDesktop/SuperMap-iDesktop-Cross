package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.graphics.GraphCanvas;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/1/24.
 */
public class ProcessGraph extends RectangleGraph {

	private IProcess process;

	public ProcessGraph(GraphCanvas canvas, IProcess process) {
		super(canvas);
		this.process = process;
		if (getCanvas() != null) {
			Font font = new Font("宋体", Font.PLAIN, 24);
			int titleWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), this.process.getTitle());
			setSize(titleWidth + 20, getHeight());
		}
	}

	public IProcess getProcess() {
		return process;
	}

	public String getTitle() {
		return this.process == null ? "未知" : this.process.getTitle();
	}
}
