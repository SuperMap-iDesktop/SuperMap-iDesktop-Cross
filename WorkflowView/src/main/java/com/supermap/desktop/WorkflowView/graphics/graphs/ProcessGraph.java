package com.supermap.desktop.WorkflowView.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.WorkflowView.meta.WorkflowParser;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.EmptyMetaProcess;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.awt.*;

/**
 * Created by highsad on 2017/1/24.
 */
public class ProcessGraph extends RectangleGraph {

	private IProcess process;
	private int percent;

	private ProcessGraph() {
		super(null);
	}

	public ProcessGraph(GraphCanvas canvas, IProcess process) {
		super(canvas, 0, 0);
		this.process = process;
		this.process.addRunningListener(new RunningListener() {
			@Override
			public void running(RunningEvent e) {
				ProcessGraph.this.percent = e.getProgress();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						getCanvas().repaint();
					}
				});
			}
		});

		this.process.addStatusChangeListener(new StatusChangeListener() {
			@Override
			public void statusChange(StatusChangeEvent e) {
				getCanvas().repaint();
			}
		});
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

		Font font = new Font("宋体", Font.BOLD, 16);
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

		// 绘制进度和 process 状态
		if (percent > 0 && percent < 100) {
			g.setColor(Color.RED);
			Rectangle rectangle = new Rectangle(getLocation().x, getLocation().y, getWidth() * percent / 100, 2);
			((Graphics2D) g).fill(rectangle);
		} else {
			if (this.process.getStatus() == RunningStatus.COMPLETED) {
				g.drawImage(((ImageIcon) ProcessResources.getIcon("/processresources/task/image_finish.png")).getImage(), getLocation().x + getWidth() - 20, getLocation().y + 2, 18, 18, null);
			}
		}
//		font = new Font(ControlsProperties.getString("String_Boldface"), Font.BOLD, 10);
//		String progress = this.percent + "%";
//		fontHeight = getCanvas().getFontMetrics(font).getHeight();
//		fontWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), progress);
//		location = new Point(getLocation().x + getWidth() - fontWidth - 4, getLocation().y + fontHeight + 4 - fontDescent);
//		g.drawString(progress, location.x, location.y);
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
		if (key.equals(MetaKeys.EMPTY) && !StringUtilities.isNullOrEmpty((String) xml.get("title"))) {
			process = new EmptyMetaProcess((String) xml.get("title"));
		} else {
			process = WorkflowParser.getMetaProcess(key);
		}
	}


}
