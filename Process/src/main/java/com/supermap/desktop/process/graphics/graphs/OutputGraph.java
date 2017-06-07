package com.supermap.desktop.process.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.utilities.DoubleUtilities;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/2/28.
 */
public class OutputGraph extends RectangleGraph {

	private ProcessGraph processGraph;
	private OutputData processData;
	private String name;

	private OutputGraph() {
		super(null);
	}

	public OutputGraph(GraphCanvas canvas, ProcessGraph processGraph, OutputData processData) {
		super(canvas, 30, 30);
		this.processGraph = processGraph;
		this.processData = processData;
	}

	public ProcessGraph getProcessGraph() {
		return processGraph;
	}

	public OutputData getProcessData() {
		return processData;
	}

	public String getTitle() {
		return processData != null ? this.processData.getName() : name;
	}

	@Override
	protected Color getBackColor() {
		return new Color(123, 136, 189);
	}

	@Override
	protected void onPaint(Graphics g) {
		super.onPaint(g);

		Font font = new Font("宋体", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.WHITE);

		String text = getTitle();
		int fontHeight = getCanvas().getFontMetrics(font).getHeight();
		int fontWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), text);
		int fontDescent = getCanvas().getFontMetrics(font).getDescent();

		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
		Point location = getLocation();
		double width = getWidth();
		double height = getHeight();
		text = SwingUtilities2.clipStringIfNecessary(getCanvas(), getCanvas().getFontMetrics(font), text, DoubleUtilities.intValue(width));
		g.drawString(text, DoubleUtilities.intValue(location.getX() + (width - fontWidth) / 2), DoubleUtilities.intValue(location.getY() + height / 2 + fontHeight / 2 - fontDescent));
	}

	@Override
	protected void toXmlHook(JSONObject jsonObject) {
		super.toXmlHook(jsonObject);
		jsonObject.put("processDataName", processData.getName());
	}

	@Override
	protected void formXmlHook(JSONObject xml) {
		super.formXmlHook(xml);
		name = ((String) xml.get("processDataName"));
	}

	public void setProcessGraph(ProcessGraph processGraph) {
		this.processGraph = processGraph;
	}

	public String getName() {
		return name;
	}

	public void setProcessData(OutputData processData) {
		this.processData = processData;
	}

}
