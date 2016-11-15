package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.Dataset;

public abstract class PropertySelectChangeListener {
	public abstract void selectChanged(int[] selectRows,Dataset dataset);
}
