package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.Geometry;

public abstract class AbstractGeometry implements IGeometry {
	private Geometry geometry;

	private AbstractGeometry() {

	}

	protected AbstractGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Geometry getGeometry() {
		return this.geometry;
	}

	public void dispose() {
		if (this.geometry != null) {
			this.geometry.dispose();
		}
	}
}
