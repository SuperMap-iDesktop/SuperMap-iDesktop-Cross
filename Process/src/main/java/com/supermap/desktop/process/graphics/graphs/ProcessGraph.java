package com.supermap.desktop.process.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.WorkflowParser;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.metaProcessImplements.EmptyMetaProcess;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/1/24.
 */
public class ProcessGraph extends RectangleGraph {

	private IProcess process;

	private ProcessGraph() {
		super(null);
	}

	public ProcessGraph(GraphCanvas canvas, IProcess process) {
		super(canvas, 30, 30);
		this.process = process;
		if (getCanvas() != null) {
		}
	}

	@Override
	protected Color getBackColor() {
		return new Color(39, 162, 223);
	}

	public IProcess getProcess() {
		return process;
	}

	public String getTitle() {
		return this.process == null ? "未知" : this.process.getTitle();
	}

	@Override
	public void onPaint(Graphics g) {
		super.onPaint(g);

		Font font = new Font("宋体", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.WHITE);

		String tilte = SwingUtilities2.clipStringIfNecessary(getCanvas(), getCanvas().getFontMetrics(font), getTitle(), getWidth());
		int fontHeight = getCanvas().getFontMetrics(font).getHeight();
		int fontWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), tilte);
		int fontDescent = getCanvas().getFontMetrics(font).getDescent();

		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
		Point location = getLocation();
		double width = getWidth();
		double height = getHeight();
		g.drawString(tilte, DoubleUtilities.intValue(location.getX() + (width - fontWidth) / 2), DoubleUtilities.intValue(location.getY() + height / 2 + fontHeight / 2 - fontDescent));
	}

	@Override
	protected void toXmlHook(JSONObject jsonObject) {
		super.toXmlHook(jsonObject);
		jsonObject.put("process", process.getKey());
		if (process instanceof EmptyMetaProcess) {
			jsonObject.put("title", process.getTitle());
		}
	}

	@Override
	protected void formXmlHook(JSONObject xml) {
		super.formXmlHook(xml);
		String key = (String) xml.get("process");
		if (key.equals(MetaKeys.Empty) && !StringUtilities.isNullOrEmpty((String) xml.get("title"))) {
			process = new EmptyMetaProcess((String) xml.get("title"));
		} else {
			process = WorkflowParser.getMetaProcess(key);
		}
	}


}
