package com.supermap.desktop.geometryoperation;

public abstract class AbstractEditor implements IEditor {
	private GeometryEditEnv geometryEditEnv;

	public AbstractEditor(GeometryEditEnv geometryEditEnv) {
		this.geometryEditEnv = geometryEditEnv;

	}

	public GeometryEditEnv getGeometryEditEnv() {
		return this.geometryEditEnv;
	}

	@Override
	public boolean enble() {
		return false;
	}

	@Override
	public boolean check() {
		return false;
	}
}
