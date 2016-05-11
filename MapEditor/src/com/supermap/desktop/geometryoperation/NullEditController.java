package com.supermap.desktop.geometryoperation;

public class NullEditController extends EditControllerAdapter {

	private NullEditController() {

	}

	public static NullEditController instance() {
		return new NullEditController();
	}
}
