package com.supermap.desktop.controls.GeometryPropertyBindWindow;

/**
 * Created by highsad on 2016/12/27.
 */
public class UserObject {
	private String msg = "";

	public UserObject(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return this.msg;
	}
}
