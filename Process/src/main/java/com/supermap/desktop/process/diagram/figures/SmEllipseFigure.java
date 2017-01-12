package com.supermap.desktop.process.diagram.figures;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.diagram.interfaces.IAttribute;
import com.supermap.desktop.process.diagram.interfaces.IFigure;
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.samples.teddy.Main;

import java.awt.*;
import java.io.IOException;

/**
 * Created by highsad on 2017/1/5.
 */
public class SmEllipseFigure extends EllipseFigure implements IFigure {
	private IProcess process;
	private IAttribute attribute;
	private String name = "";

	public static void main(String[] args) {
		Main.main(args);
	}

	public SmEllipseFigure() {
	}

	public SmEllipseFigure(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	@Override
	public IProcess getProcess() {
		return process;
	}

	@Override
	public void setProcess(IProcess process) {
		this.process = process;
	}

	@Override
	public IAttribute getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(IAttribute attribute) {

	}

//	@Override
//	public void basicDisplayBox(Point origin, Point corner) {
//		if (origin.equals(corner)) {
//			super.basicDisplayBox(new Point(corner.x - 50, corner.y - 30), new Point(corner.x + 50, corner.y + 30));
//		} else {
//			super.basicDisplayBox(origin, corner);
//		}
//	}
//
//	@Override
//	public void setAttribute(IAttribute attribute) {
//		this.attribute = attribute;
//	}
//
//	@Override
//	public void drawFrame(Graphics g) {
//		super.drawFrame(g);
//		DrawUtilties.drawName(g, displayBox(), getName());
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	@Override
//	public void write(StorableOutput dw) {
//		super.write(dw);
//		dw.writeString(ProcessSerializationUtilties.getProcessName(process));
//		dw.writeString(name);
//	}
//
//	@Override
//	public void read(StorableInput dr) throws IOException {
//		super.read(dr);
//		String className = dr.readString();
//		this.process = ProcessSerializationUtilties.getIProcess(className);
//		this.setName(dr.readString());
//	}

//	@Override
//	public Object clone() {
//		SmEllipseFigure clone = new SmEllipseFigure(new Point(0, 0), new Point(100, 100));
//		return clone;
//	}
}
