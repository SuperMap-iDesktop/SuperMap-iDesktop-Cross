package com.supermap.desktop.ui.controls;

import java.awt.Graphics;

import com.supermap.data.Geometry;
import com.supermap.data.SymbolLine;

class InternalSymbolLine extends SymbolLine {

	protected InternalSymbolLine(long handle) {
		super(handle);
	}

	public static final boolean internalDraw(SymbolLine symbolLine,
			Graphics graphics, Geometry geometry) {
		return SymbolLine.internalDraw(symbolLine, graphics, geometry);
	}

}
